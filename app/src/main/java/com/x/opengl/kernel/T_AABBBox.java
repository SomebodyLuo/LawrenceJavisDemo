package com.x.opengl.kernel;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.opengl.GLU;
import android.opengl.Matrix;

import com.x.Director;
import com.x.components.node.View;

/**
 * 目前该类的作用主要是存储了即时的矩阵信息，
 * 现在的碰撞检测和物体选择即时方向向量，都可以用这个进行计算。
 * 
 * @date   2013-10-23 14:55:14
 */
@SuppressLint("NewApi") 
public class T_AABBBox implements Cloneable{

	private static final String TAG = "AABBBox";
	
	private GL11          mGl                    = null;
	private View    	  mAttachView                = null;
	private boolean       mIsHitted              = false;
	private boolean       mIsInitBound           = false;          
	private Vector2       mScreenTouch           = null;
	public int[]         mViewportMatrix        = new int[4];//视口
	public float[]       mModelMatrix       = new float[16];//模型
	public float[]       mViewMatrix       = new float[16];//视图
	public float[]       mProjectionMatrix      = new float[16];//投影
	
	public float[]       mModelViewMatrix      = new float[16];//模型视图

	private float[] mCameraLookVector = new float[6];;//0，1，2 位置             3，4，5，向量
	
	private List<float[]> mMeshVertexes          = null;

	public Position	mFinalTranslate = new Position();
	public Scale	mFinalScale = new Scale();
	public T_AABBBox() {
		mMeshVertexes = new ArrayList<float[]>();
	}
	
	@Override
	protected T_AABBBox clone(){
		T_AABBBox cloned = null;
		try {
			cloned = (T_AABBBox) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			cloned = new T_AABBBox();
		}
		cloned.mGl               = mGl;
		cloned.mAttachView           = mAttachView;
		cloned.mScreenTouch      = mScreenTouch;
//		cloned.mBoundBox         = mBoundBox.clone();
//		cloned.mBoundSphere      = mBoundSphere.clone();
		cloned.mViewportMatrix   = mViewportMatrix.clone();
		cloned.mModelMatrix  = mModelMatrix.clone();
		cloned.mProjectionMatrix = mProjectionMatrix.clone();
		cloned.mMeshVertexes     = null;
		if (mMeshVertexes != null) {
			cloned.mMeshVertexes = new ArrayList<float[]>(mMeshVertexes);
		}
		
		return cloned;
	}
	
	public T_AABBBox(View parent) {
		setAttachView(parent);
	}
	
	public void setAttachView(View parent) {
		mAttachView = parent;
	}
	
	public void scanMeshes() {
		mAttachView.getBaseDrawable().generateBound();;
	}
	
	public boolean isHit(float screenX, float screenY, float[] innerHitPoint ) {
		
		ArrayList<PointF> ScreenCoordinatePointList = mAttachView.getBaseDrawable().findDrawableScreenCoordinate(this);
		
		ArrayList<PointF> dlist  = AABBBoxUtil.caculateMinPolygon(ScreenCoordinatePointList);

		boolean click = true;
		PointF oF = new PointF(screenX, screenY);
		String log = "";
		
		for (int i = 0; i < dlist.size(); i++) {
			PointF pointFS = dlist.get(i);
			PointF pointFD = dlist.get((i+1)%dlist.size());
			log +=""+i+ ":"+ pointFS.toString();
			// 如果point列表有一个不在屏幕内，则提前判断为false .后续再优化更改
			if(	pointFS.x > EngineConstanst.SCREEN_WIDTH || 
				pointFS.y > EngineConstanst.SCREEN_HEIGHT ||
				pointFS.x < 0 ||
				pointFS.y < 0   ){

				click = false;
				break;
			}
			//如果发现叉乘还是顺时针
			click  = Vector2.isCWcross(new Vector2(oF,pointFS),new Vector2(oF,pointFD));
			if(!click){
				break;
			}
		}
//		Log.d("ming",mAttachView.getDebugName()+",,,"+"click = "+click + "[ "+log);
		if(click){
			//检测是面向还是刚好背对物体,背对则丢弃
			
			ArrayList<Vector3> worldCoordinatePointList = mAttachView.getBaseDrawable().findDrawableWorldCoordinate(this);
			boolean faceTo = false;
			for (int i = 0; i < worldCoordinatePointList.size(); i++) {

				Vector3 vWorld = worldCoordinatePointList.get(i);//世界坐标
				Vector3 vCameraToPoint= new Vector3(vWorld.X - mCameraLookVector[0] ,vWorld.Y - mCameraLookVector[1],vWorld.Z - mCameraLookVector[2]) ;
				Vector3 vCameraVector3 = new Vector3(mCameraLookVector[3],mCameraLookVector[4],mCameraLookVector[5]) ;

				float angle = Vector3.angleBetween(vCameraToPoint,vCameraVector3);
				if(angle < Math.PI/2){//锐角
					faceTo = true;
					break;
				}
				
			}
			click &= faceTo;
		} 
		if(click){
			Director.getInstance().requstAABBBoxDisplay(dlist);
		}
//		Log.d("ming","click = "+click);
//		innerHitPoint[0] = (pointA.x  + pointB.x + pointC.x + pointD.x)/4f;
//		innerHitPoint[1] = (pointA.y  + pointB.y + pointC.y + pointD.y)/4f;
		
		return click;
	}

	void transformToScreenCoordinate(PointF pointF, float x, float y, float z) {
//
		 Matrix.multiplyMM(mModelViewMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		float[] win    = new float[3];
		int flag =  GLU.gluProject(	
				x, y, z,
				mModelViewMatrix, 0,
				mProjectionMatrix, 0,
				mViewportMatrix, 0,
				win, 0) ;
		
		if (flag == GL10.GL_TRUE) {
			pointF.x = win[0];
			pointF.y = mViewportMatrix[3] - win[1];
		}
		
		

//		float[] viewPortmatrix = new float[]{
//				mViewportMatrix[2]/2,	0,						0,		mViewportMatrix[0]+mViewportMatrix[2]/2,
//				0,						mViewportMatrix[3]/2,	0,		mViewportMatrix[1]+mViewportMatrix[3]/2,
//				0,						0,						0,		0,
//				0,						0,						0,		1,
//		};
//		
//		String tag = "before / ("+win[0]+","+win[1]+")";
//		float[] target = new float[4];
//		Matrix.multiplyMM(mTempMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
// 		Matrix.multiplyMM(mTempMatrix, 0, mProjectionMatrix, 	0, mTempMatrix, 	0);
// 		Matrix.multiplyMM(mTempMatrix, 0, viewPortmatrix, 	0, mTempMatrix, 	0);
// 		Matrix.multiplyMV(target, 0, mTempMatrix, 	0, new float[]{x,y,z,1}, 	0);
// 		tag += "after 	("+target[0]+",,"+target[1]+")";
// 		Log.d("ming", tag);
 		
 		
 		
	}
	public float[]       mTempMatrix      = new float[16]; 

	void transformToWorldCoordinate(float[] pointF, float x, float y, float z) {
		
		 Matrix.multiplyMM(mModelViewMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		 Matrix.multiplyMV(pointF, 0, mModelViewMatrix, 0, new float[]{x,y,z,0}, 0);
		
	}

	public void update() {
//		mIsHitted = false; 
		 Director.sGLESVersion.getViewportMatrix(this);
		 Director.sGLESVersion.getProjectionMatrix(this);
		 Director.sGLESVersion.getModelMatrix(this);
		 Director.sGLESVersion.getViewMatrix(this);
		 Director.sGLESVersion.getCameraLookVector(this);

	}

	public void callbackUpdateProjectionMatrix(float[] projMatrix) {

		mProjectionMatrix = projMatrix ;
	}
	public void callbackUpdateViewportMatrix(int[] viewPortMatrix) {

		mViewportMatrix = viewPortMatrix ;
	}

	float[][] mTempArray = new float[4][4];

	public void callbackUpdateModelMatrix(float[] modelMatrix) {

		mModelMatrix = modelMatrix ;

	}
	public void callbackUpdateViewMatrix(float[] viewMatrix) {

		mViewMatrix = viewMatrix ;

	}
	public void callbackUpateLookVector(float[] clone) {
		mCameraLookVector = clone;
	}
//	{
//    	int index = 0;
//    	for (int j = 0; j < mTempArray.length; j++) {
//	    	for (int i = 0; i < mTempArray[j].length; i++) {
//					mTempArray[i][j] = modelViewMatrix[index++];
//			}
//    	}
//
//    	//位移等于矩阵最后一列
//    	float w = 1f/mTempArray[3][3];
//    	this.mFinalTranslate.X = mTempArray[0][3]*w; 
//    	this.mFinalTranslate.Y = mTempArray[1][3]*w; 
//    	this.mFinalTranslate.Z = mTempArray[2][3]*w; 
//    	
//
//    	//缩放因子x,y,z分别是第一，二，三列的范数
//    	mFinalScale.X = (float)Math.sqrt(
//    			 Math.pow(mTempArray[0][0], 2) 
//     			+Math.pow(mTempArray[1][0], 2) 
//    			+Math.pow(mTempArray[2][0], 2) 
//    			);
//    	mFinalScale.Y = (float)Math.sqrt(
//    			Math.pow(mTempArray[0][1], 2) 
//    			+Math.pow(mTempArray[1][1], 2) 
//    			+Math.pow(mTempArray[2][1], 2) 
//    			);
//    	mFinalScale.Z = (float)Math.sqrt(
//    			Math.pow(mTempArray[0][2], 2) 
//    			+Math.pow(mTempArray[1][2], 2) 
//    			+Math.pow(mTempArray[2][2], 2) 
//    			);
//	}

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
	

	
//	public boolean getRayCastResult() {
//		return mIsHitted;
//	}
	
//	public float getAABBBoxWidth() {
//		return mBoundBox.getMaxX() - mBoundBox.getMinX();
//	}
//	
//	public float getAABBBoxHeight() {
//		return mBoundBox.getMaxY() - mBoundBox.getMinY();
//	}
//	
//	public float getAABBBoxDepth() {
//		return mBoundBox.getMaxZ() - mBoundBox.getMinZ();
//	}

}
