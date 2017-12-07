package com.x.opengl.kernel;

import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLU;

/**
 * 射线
 * @date   2013-10-26 11:46:41
 */
public class Ray {

	private GL11    mGl               = null;
	private int[]   mViewportMatrix   = new int[4];
	private float[] mModelViewMatrix  = new float[16];
	private float[] mProjectionMatrix = new float[16];
	private Vector3 mRayStartPoint    = null;
	private Vector3 mRayEndPoint      = null;
	private Vector3 mRayDirection     = null;
	private Vector3 mRayCastPoint     = null;
	private float   mRayCastDistance  = Float.MAX_VALUE;
	
	public Ray() {
	}
	
	public void generateRay(float screenX, float screenY, float[] modelViewMatrix, float[] projectionMatrix, int[] viewportMatrix) {
		float   winX          = screenX;
		float   winY          = EngineConstanst.SCREEN_HEIGHT - screenY;
		float[] rayStartPoint = new float[4];
		float[] rayStartEnd   = new float[4];
		
		mModelViewMatrix  = modelViewMatrix;
		mProjectionMatrix = projectionMatrix;
		mViewportMatrix   = viewportMatrix;
		
		GLU.gluUnProject(
				winX, winY, 0,
				mModelViewMatrix, 0,
				mProjectionMatrix, 0,
				mViewportMatrix, 0,
				rayStartPoint, 0);
		GLU.gluUnProject(
				winX, winY, 1,
				mModelViewMatrix, 0,
				mProjectionMatrix, 0,
				mViewportMatrix, 0,
				rayStartEnd, 0);
		
		mRayStartPoint = new Vector3(rayStartPoint[0] / rayStartPoint[3], rayStartPoint[1] / rayStartPoint[3], rayStartPoint[2] / rayStartPoint[3]);
		mRayEndPoint   = new Vector3(rayStartEnd[0] / rayStartEnd[3], rayStartEnd[1] / rayStartEnd[3], rayStartEnd[2] / rayStartEnd[3]);
		mRayDirection  = Vector3.normalize(Vector3.subtraction(mRayEndPoint, mRayStartPoint));
	}

	public Matrix getModelViewMatrix() {
		return new Matrix(mModelViewMatrix);
	}
	
	public Matrix getProjectionMatrix() {
		return new Matrix(mProjectionMatrix);
	}

	/**
	 * 碰撞包围球
	 * @param mBoundSphere
	 * @return
	 */
	public boolean intersectSphere(BoundSphere mBoundSphere) {
		boolean isInSphere     = false;
		Vector3 diff           = Vector3.subtraction(mRayStartPoint, mBoundSphere.CenterPoint);
		float   areaDistance   = Vector3.dotProduct(diff, diff) - mBoundSphere.Radius * mBoundSphere.Radius;
		float   directDistance = 0;    
		
		if (areaDistance <= 0) {
			isInSphere = true;
		}else{
			directDistance = Vector3.dotProduct(mRayDirection, diff);
			if (directDistance < 0) {
				isInSphere = (directDistance * directDistance - areaDistance) >= 0;
			}else{
				isInSphere = false;
			}
		}
		
		return isInSphere;
	}

	/**
	 * 精确碰撞
	 * @param meshes
	 * @return
	 */
	public boolean intersectMesh(Mesh[] meshes) {
		boolean isIntersect      = false;
		float[] intersetLocation = new float[]{0, 0, 0, Float.MAX_VALUE};
		
		mRayCastPoint = null;
		mRayCastDistance = Float.MAX_VALUE;
		
		if (meshes == null || meshes.length <= 0) {
			return false;
		}else{
			for (int i = 0; i < meshes.length; i++) {
				Mesh    mesh        = meshes[i];
				float[] vertexArray = null; 
				short[] indiceArray = null;
				
				if (mesh == null) {
					return false;
				}else if (mesh.VertexBuffer.capacity() <= 0) {
					return false;
				}else{
					vertexArray = new float[mesh.VertexBuffer.capacity()];
					mesh.VertexBuffer.position(0);
					mesh.VertexBuffer.get(vertexArray);
					if (mesh.IndexBuffer.capacity() <= 0) {
						int indiceSize = vertexArray.length / 3;
						indiceArray = new short[indiceSize];
						for (short j = 0; j < indiceSize; j++) {
							indiceArray[j] = j;
						} 
					}else{
						indiceArray = new short[mesh.IndexBuffer.capacity()];
						mesh.IndexBuffer.position(0);
						mesh.IndexBuffer.get(indiceArray);
					}
				}
				
				// 碰撞所有三角形
				int faceSize = indiceArray.length / 3;
				int pointSize = indiceArray.length;
				for (int faceItr = 0, pointItr = 0; faceItr < faceSize && pointItr < pointSize; faceItr++) {
					Vector3 pointA = null;
					Vector3 pointB = null;
					Vector3 pointC = null;
					
					pointA = new Vector3(
							vertexArray[indiceArray[pointItr] * 3 + 0],
							vertexArray[indiceArray[pointItr] * 3 + 1],
							vertexArray[indiceArray[pointItr] * 3 + 2]);
					pointItr++;
					
					pointB = new Vector3(
							vertexArray[indiceArray[pointItr] * 3 + 0],
							vertexArray[indiceArray[pointItr] * 3 + 1],
							vertexArray[indiceArray[pointItr] * 3 + 2]);
					pointItr++;
					
					pointC = new Vector3(
							vertexArray[indiceArray[pointItr] * 3 + 0],
							vertexArray[indiceArray[pointItr] * 3 + 1],
							vertexArray[indiceArray[pointItr] * 3 + 2]);
					pointItr++;
					
					float[] intersectPoint = intersectTriangle(pointA, pointB, pointC);
					if (intersectPoint != null) {
						if (!isIntersect) {
							isIntersect = true;
							intersetLocation = intersectPoint;
						}else{
							if (intersetLocation[3] > intersectPoint[3]) {
								intersetLocation = intersectPoint;
							}
						}
					}
				}
				
				mRayCastPoint = new Vector3(intersetLocation[0], intersetLocation[1], intersetLocation[2]);
				mRayCastDistance = intersetLocation[3];
			}
		}
		
		return isIntersect;
	}

	/**
	 * 三角碰撞
	 * @param pointA
	 * @param pointB
	 * @param pointC
	 * @return 碰撞点的四元向量，如果不存在碰撞点，则返回null
	 */
	private float[] intersectTriangle(Vector3 pointA, Vector3 pointB, Vector3 pointC) {
		Vector3 diff       = Vector3.subtraction(mRayStartPoint, pointA);
		Vector3 edgeA      = Vector3.subtraction(pointB, pointA);
		Vector3 edgeB      = Vector3.subtraction(pointC, pointA);
		Vector3 norm       = Vector3.crossProduct(edgeA, edgeB);
		int     sign       = 0;
		float   parameterA = 0;
		float   parameterB = 0;
		float   parameterC = Vector3.dotProduct(mRayDirection, norm);
		float   parameterD = 0;
		
		if (parameterC > 10E-6) {
			sign = 1;
		} else if (parameterC < -10E-6) {
			sign = -1;
			parameterC = -parameterC;
		} else {
			// 射线与三角形平行
			return null;
		}
		
		parameterA = sign * Vector3.dotProduct(mRayDirection, Vector3.crossProduct(diff, edgeB));
		if (parameterA >= 0.0f) {
			parameterB = sign * Vector3.dotProduct(mRayDirection, Vector3.crossProduct(edgeA, diff));
			if (parameterB >= 0.0f) {
				if (parameterA + parameterB <= parameterC) {
					parameterD = -sign * Vector3.dotProduct(diff, norm);
					if (parameterD >= 0.0f) {
						// 射线与三角形相交
						float intersectPointDistance = parameterD / parameterC;
						return new float[]{
								mRayStartPoint.X + mRayDirection.X * intersectPointDistance,
								mRayStartPoint.Y + mRayDirection.Y * intersectPointDistance,
								mRayStartPoint.Z + mRayDirection.Z * intersectPointDistance,
								intersectPointDistance
						};
					}
				}
			}
		}
		
		// 射线与三角形不相交
		return null;
	}

	/**
	 * 从世界坐标系转换到模型视图坐标系
	 * @param ray
	 * @param invertTransfrom 模型视图矩阵的逆
	 * @return
	 */
	public static Ray tranformFromWorldToModelView(Ray ray, Matrix invertTransfrom) {
		Vector3 startPoint     = ray.mRayStartPoint;
		Vector3 endPoint       = ray.mRayEndPoint;
		Ray     transformedRay = new Ray(); 
		
		transformedRay.mRayStartPoint    = invertTransfrom.concatVector(startPoint);
		transformedRay.mRayEndPoint      = invertTransfrom.concatVector(endPoint);
		transformedRay.mRayDirection     = Vector3.subtraction(endPoint, startPoint);
		transformedRay.mGl               = ray.mGl;
		transformedRay.mModelViewMatrix  = ray.mModelViewMatrix;
		transformedRay.mProjectionMatrix = ray.mProjectionMatrix;
		transformedRay.mViewportMatrix   = ray.mViewportMatrix;
		
		return transformedRay;
	}

}
