package com.x.opengl.kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLU;

import com.x.components.node.View;

/**
 * 
 * @date   2013-10-23 14:55:14
 */
@SuppressLint("NewApi") 
public class AABBBox implements Cloneable{

	private static final String TAG = "AABBBox";
	
	private GL11          mGl                    = null;
	private View    mParent                = null;
	private boolean       mIsHitted              = false;
	private boolean       mIsInitBound           = false;          
	private Vector2       mScreenTouch           = null;
	private BoundBox      mBoundBox              = new BoundBox();
	private BoundSphere   mBoundSphere           = new BoundSphere();
	private RectF         mScreenRect            = new RectF();
	public int[]         mViewportMatrix        = new int[4];
	public float[]       mModelViewMatrix       = new float[16];
	public float[]       mProjectionMatrix      = new float[16];
	private List<float[]> mMeshVertexes          = null;
	
	public AABBBox() {
		mMeshVertexes = new ArrayList<float[]>();
	}
	
	@Override
	protected AABBBox clone(){
		AABBBox cloned = null;
		try {
			cloned = (AABBBox) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			cloned = new AABBBox();
		}
		cloned.mGl               = mGl;
		cloned.mParent           = mParent;
		cloned.mScreenTouch      = mScreenTouch;
		cloned.mBoundBox         = mBoundBox.clone();
		cloned.mBoundSphere      = mBoundSphere.clone();
		cloned.mScreenRect.set(mScreenRect);
		cloned.mViewportMatrix   = mViewportMatrix.clone();
		cloned.mModelViewMatrix  = mModelViewMatrix.clone();
		cloned.mProjectionMatrix = mProjectionMatrix.clone();
		cloned.mMeshVertexes     = null;
		if (mMeshVertexes != null) {
			cloned.mMeshVertexes = new ArrayList<float[]>(mMeshVertexes);
		}
		
		return cloned;
	}
	
	public AABBBox(View parent) {
		setParent(parent);
	}
	
	public void setParent(View parent) {
		mParent = parent;
	}
	
	public void scanMeshes() {
		float  minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
		float  maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
		
		Mesh mesh = mParent.getMesh();
		if (mesh != null) {
			float[] array = new float[mesh.VertexBuffer.capacity()];
			mesh.VertexBuffer.position(0);
			mesh.VertexBuffer.get(array);
			mMeshVertexes.add(Arrays.copyOf(array, array.length));
			int arraySize = array.length;
			for (int j = 0; j < arraySize; j += 3) {
				minX = minX < array[j] ? minX : array[j];
				maxX = maxX > array[j] ? maxX : array[j];
			}
			for (int j = 1; j < arraySize; j += 3) {
				minY = minY < array[j] ? minY : array[j];
				maxY = maxY > array[j] ? maxY : array[j];
			}
			for (int j = 2; j < arraySize; j += 3) {
				minZ = minZ < array[j] ? minZ : array[j];
				maxZ = maxZ > array[j] ? maxZ : array[j];
			}
			mIsInitBound = true;
		}
		
		mBoundSphere.generateBoundSphere(minX, minY, minZ, maxX, maxY, maxZ);
		mBoundBox.generateBoundBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public void setBoundBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		mIsInitBound = true;
		mBoundSphere.generateBoundSphere(minX, minY, minZ, maxX, maxY, maxZ);
		mBoundBox.generateBoundBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
//	boolean isHit(float screenX, float screenY) {
//		Matrix      modelMatrix      = null;
//		Matrix      projectionMatrix = null;
//		Ray         ray              = null;
//		Ray         invertRay        = null;
//		BoundSphere boundSphere      = null;
//		
//		if (!mIsInitBound) {
//			scanMeshes();
//		}
//		
//		// 求解世界坐标系中的射线
//		ray = new Ray();
//		boundSphere = new BoundSphere();
//		ray.generateRay(screenX, screenY, mModelViewMatrix, mProjectionMatrix, mViewportMatrix);
//		modelMatrix = ray.getModelViewMatrix();
//		boundSphere.CenterPoint = modelMatrix.concatVector(mBoundSphere.CenterPoint);
//		boundSphere.Radius      = mBoundSphere.Radius * Math.min(Math.min(mParent.Scale.X, mParent.Scale.Y), mParent.Scale.Z);
//		
//		CBLog.w(TAG, "[isHit] boundSphere.CenterPoint = " + boundSphere.CenterPoint + ", boundSphere.Radius = " + boundSphere.Radius);
//		
//		if (ray.intersectSphere(boundSphere)) {
//			// 求解模型视图坐标系中的射线
//			invertRay = Ray.tranformFromWorldToModelView(ray, Matrix.invert(modelMatrix));
//			mIsHitted = invertRay.intersectMesh(mParent.Meshes);
//		}
//		
//		return mIsHitted;
//	}
	
	public boolean isHit(float screenX, float screenY, float[] innerHitPoint) {
		
		if (!mIsInitBound) {
			scanMeshes();
		}
		// 投射左上点
		PointF pointA = new PointF();
		transform(pointA,mBoundBox.getMinX(),mBoundBox.getMaxY());
		// 投射左下点
		PointF pointB = new PointF();
		transform(pointB,mBoundBox.getMinX(),mBoundBox.getMinY());
		// 投射右下点
		PointF pointC = new PointF();
		transform(pointC,mBoundBox.getMaxX(),mBoundBox.getMinY());
		// 投射右上点
		PointF pointD = new PointF();
		transform(pointD,mBoundBox.getMaxX(),mBoundBox.getMaxY());
		
//		Log.d("tag", "==================================================================="  );
//		Log.d("tag", "pointA = "+pointA);
//		Log.d("tag", "pointB = "+pointB);
//		Log.d("tag", "pointC = "+pointC);
//		Log.d("tag", "pointD = "+pointD);
		
		PointF oF = new PointF(screenX, screenY);
		
		boolean  isCCW4 = Vector2.cross(new Vector2(oF,pointD),new Vector2(oF,pointA));//ODA的顺逆时针判断
		boolean  isCCW3 = Vector2.cross(new Vector2(oF,pointC),new Vector2(oF,pointD));//OCD的顺逆时针判断
		if(isCCW3 != isCCW4){
			return false;
		}
		boolean  isCCW2 = Vector2.cross(new Vector2(oF,pointB),new Vector2(oF,pointC));//OBC的顺逆时针判断
		if(isCCW2 != isCCW3){
			return false;
		}
		boolean  isCCW1 = Vector2.cross(new Vector2(oF,pointA),new Vector2(oF,pointB));//OAB的顺逆时针判断
		if(isCCW1 != isCCW2){
			return false;
		}
//		Log.d("tag", isCCW1+","+isCCW2+","+isCCW3+","+isCCW4);
		return true;
		// 碰撞结果
//		result = mScreenRect.contains(screenX, screenY);
//		
//		// 传出内部碰撞点
//		if (innerHitPoint != null) {
//			if (result) {
//				innerHitPoint[0] = screenX - mScreenRect.left;
//				innerHitPoint[1] = screenY - mScreenRect.bottom;
//			}
//		}
		
//		return result;
	}

	private void transform(PointF pointF, float x, float y) {
		float[] win    = new float[3];
		int flag =  GLU.gluProject(	
				x, y, 0,
				mModelViewMatrix, 0,
				mProjectionMatrix, 0,
				mViewportMatrix, 0,
				win, 0) ;
		
		if (flag == GL10.GL_TRUE) {
			pointF.x = win[0];
			pointF.y = mViewportMatrix[3] - win[1];
		}
	}

	public void update() {
		mIsHitted = false;

//		Log.d("tag", "aabbbox update()");
//		WolfRenderer.mGLESVersion.getViewportMatrix(this);
//		WolfRenderer.mGLESVersion.getProjectionMatrix(this);
//		WolfRenderer.mGLESVersion.getModelviewMatrix(this);
		
//		if (mScreenTouch != null) {
//			mIsHitted = isHit(mScreenTouch.X, mScreenTouch.Y, null);
//			mScreenTouch = null;
//		}
	}
	
	/**
	 * 射线检测，异步返回本帧碰撞结果
	 * 结果通过{@link #getRayCastResult()}获得
	 * @see rayCast(float x, float y)
	 * @param x
	 * @param y
	 */
	public void injectScreenPosition(float x, float y) {
		mScreenTouch = new Vector2(x, y);
	}
	
	/**
	 * 射线检测，即时返回本帧或上一帧的碰撞结果
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean rayCast(float x, float y, float[] innerHitPoint) {
		return isHit(x, y, innerHitPoint);
	}
	
	public boolean getRayCastResult() {
		return mIsHitted;
	}
	
	public RectF getScreenRect() {
		float[] win = new float[3];
		// 投射左上点
		if (GLU.gluProject(
				mBoundBox.getMinX(), mBoundBox.getMaxY(), 0,
				mModelViewMatrix, 0,
				mProjectionMatrix, 0,
				mViewportMatrix, 0,
				win, 0) == GL10.GL_TRUE) {
			mScreenRect.left = win[0];
			mScreenRect.top  = mViewportMatrix[3] - win[1];
		}
		// 投射右下点
		if (GLU.gluProject(
				mBoundBox.getMaxX(), mBoundBox.getMinY(), 0,
				mModelViewMatrix, 0,
				mProjectionMatrix, 0,
				mViewportMatrix, 0,
				win, 0) == GL10.GL_TRUE) {
			mScreenRect.right  = win[0];
			mScreenRect.bottom = mViewportMatrix[3] - win[1];
		}
		
		return mScreenRect;
	}
	
	public float getAABBBoxWidth() {
		return mBoundBox.getMaxX() - mBoundBox.getMinX();
	}
	
	public float getAABBBoxHeight() {
		return mBoundBox.getMaxY() - mBoundBox.getMinY();
	}
	
	public float getAABBBoxDepth() {
		return mBoundBox.getMaxZ() - mBoundBox.getMinZ();
	}
}
