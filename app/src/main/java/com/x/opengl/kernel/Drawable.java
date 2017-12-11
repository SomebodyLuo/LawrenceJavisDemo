package com.x.opengl.kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.PointF;

import com.x.Director;
import com.x.components.node.View;

@SuppressLint("NewApi")
public class Drawable   
{
	
	
	public Mesh mMesh = new Mesh();
	public Material mMaterial = new Material();
	protected  Transform mTransform = new Transform();
	
	protected ArrayList<BoundBox>      mBoundBoxList              = new ArrayList<BoundBox>();
	protected ArrayList<BoundSphere>   mBoundSphereList           = new ArrayList<BoundSphere>();

	private float mDrawableWidth = 1;
	private float mDrawableHeight = 1;
	private float mDrawableThickness = 1;

	private boolean mFirst = true;
	
	protected Texture mNormalTexture = null;

//	private T_AnimationSet mT_AnimationSet = new T_AnimationSet(); // 所有动画集合
	private float[] mSelfModelViewMatrix;
	private boolean	mRenderable = true;
	private float mHalfUnit = 0.5f;
	private View mView;
	protected T_AABBBox			mAabbBox			 ;
	
	public Drawable(int depth)
	{
		initMesh(depth);
		initMaterial(depth);

		setWidth(1);
		setHeight(1);
		setThickness(1);
	}
	public void draw( )
	{
		mMaterial.Texture = mNormalTexture;
		
		if (mMaterial.Texture != null && mMaterial.Texture.isValid())
		{
			Director.sGLESVersion.pushMatrix();

			onAnimation();
			onDrawbleTransform();
			
			onRender();
			onRayCast();  
			
			Director.sGLESVersion.popMatrix();
		}
	}

	public void setAttachView(View view) {
		mView = view;
	}
	public void scanMeshes() {
			if(mAabbBox == null){
				mAabbBox = new T_AABBBox();
			}
			mAabbBox.setAttachView(mView);
			mAabbBox.scanMeshes();	
	}
	private boolean	mFlagRayCast;

	/***
	 * 读取实时的各种矩阵信息，用于后续计算。
	 */
	protected void onRayCast() {
		if(mAabbBox != null){
			if(mFlagRayCast){ 
				Director.sGLESVersion.getViewportMatrix(mAabbBox);
				Director.sGLESVersion.getProjectionMatrix(mAabbBox);
				Director.sGLESVersion.getModelMatrix(mAabbBox);
				Director.sGLESVersion.getViewMatrix(mAabbBox);
				Director.sGLESVersion.getCameraLookVector(mAabbBox);
			}
			mFlagRayCast = !mFlagRayCast; 
			
		}
	}
	public boolean isHit(float screenX, float screenY, float[] innerHitPoint) {
		// TODO Auto-generated method stub
		if(mAabbBox != null){
			return mAabbBox.isHit(screenX, screenY, innerHitPoint);
		}
		return false;
	}
	/*
	 * 获取绝对位移三元数
	 */
	public Position getFinalModelViewTranslate() {
		return mAabbBox.mFinalTranslate;
	}

	/**
	 * 绝对缩放三元数
	 * @return
	 */
	public Scale getFinalModelViewScale() {
		return mAabbBox.mFinalScale;
	}

	/*
	 * 获取当前view 的绝对模型视图转换矩阵，即参考世界坐标系而得来
	 */
	public float[] getFinalModelMatrix() {
		return mAabbBox.mModelMatrix;
	}
	protected void initMesh(int i)
	{
		mMesh = new Mesh();
		mMesh.setVertexes(new float[] { 
				+mHalfUnit * EngineConstanst.PIX_REFERENCE, +mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * i,
				-mHalfUnit * EngineConstanst.PIX_REFERENCE, +mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * i, 
				-mHalfUnit * EngineConstanst.PIX_REFERENCE, -mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * i, 
				+mHalfUnit * EngineConstanst.PIX_REFERENCE, -mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * i 
				});

		mMesh.setNormals(new float[] { 
				+0.0000f, -0.0000f, +1.0000f, 
				+0.0000f, -0.0000f, +1.0000f, 
				+0.0000f, -0.0000f, +1.0000f, 
				+0.0000f, -0.0000f, +1.0000f });

		mMesh.setColors(new float[] { 
				+1.0f, 0.0f, +1.0f, +1.0f, 
				+0.0f, 1.0f, +1.0f, +1.0f, 
				+1.0f, 1.0f, +1.0f, +1.0f, 
				+1.0f, 1.0f, +0.0f, +1.0f, });

		mMesh.setCoordinates(new float[] { 
				+1.0000f, +0.0000f, 
				+0.0000f, +0.0000f, 
				+0.0000f, +1.0000f, 
				+1.0000f, +1.0000f });
		mMesh.setIndices(new short[] { 
				0, 1, 2, 
				2, 3, 0 
				});
		mMesh.Name = "BoardMaterial" + i;
	}
	
	protected void initMaterial(int i)
	{
		// 设置材质名称
		mMaterial.Name = "BoardMaterial" + i;
		// 设置材质属性
		mMaterial.setAmbient(0.8000f, 0.8000f, 0.8000f, 1.0000f);
		mMaterial.setDiffuse(0.8000f, 0.8000f, 0.8000f, 1.0000f);
		mMaterial.setSpecular(0.8000f, 0.8000f, 0.8000f, 1.0000f);
		mMaterial.setEmission(0.0000f, 0.0000f, 0.0000f, 1.0000f);
		mMaterial.setAlpha(1.0f);
		mMaterial.setOpticalDensity(1.5000f);
		mMaterial.setShininess(30.0000f);
		mMaterial.setTransparent(0.3000f);
		mMaterial.setTransmissionFilter(0.7000f, 0.7000f, 0.7000f, 0.7000f);
		mMaterial.setIllumination(2);
		// 添加到材质组
	}
//	protected void initMesh(int i)
//	{
//		mMesh = new Mesh();
//		mMesh.setVertexes(new float[] { 
//				
//		+mHalfUnit * EngineConstanst.PIX_REFERENCE, +mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * (i + mHalfUnit),
//		-mHalfUnit * EngineConstanst.PIX_REFERENCE, +mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * (i + mHalfUnit), 
//		-mHalfUnit * EngineConstanst.PIX_REFERENCE, -mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * (i + mHalfUnit), 
//		+mHalfUnit * EngineConstanst.PIX_REFERENCE, -mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * (i + mHalfUnit),
//		
//		+mHalfUnit * EngineConstanst.PIX_REFERENCE, +mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * (i - mHalfUnit),
//		-mHalfUnit * EngineConstanst.PIX_REFERENCE, +mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * (i - mHalfUnit), 
//		-mHalfUnit * EngineConstanst.PIX_REFERENCE, -mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * (i - mHalfUnit), 
//		+mHalfUnit * EngineConstanst.PIX_REFERENCE, -mHalfUnit * EngineConstanst.PIX_REFERENCE, +EngineConstanst.PIX_REFERENCE * (i - mHalfUnit), 
//
//
//		});
//		
//		mMesh.setNormals(new float[] { 
//				
//				+0.0000f, 0.0000f, +1.0000f, //0
//				+0.0000f, 0.0000f, +1.0000f, //1
//				+0.0000f, 0.0000f, +1.0000f, //2
//				+0.0000f, 0.0000f, +1.0000f, //3
//				
//				});
//		
//		mMesh.setCoordinates(new float[] { 
//				
//				+1.0000f, +0.0000f,//0 
//				+0.0000f, +0.0000f,//1
//				+0.0000f, +1.0000f, //2
//				+1.0000f, +1.0000f,//3
//				
//				+1.0000f, +0.0000f,//0 
//				+0.0000f, +0.0000f,//1
//				+0.0000f, +1.0000f, //2
//				+1.0000f, +1.0000f,//3
//				
//				});
//		mMesh.setIndices(new short[] {
//				
////				0,1,
////				1,2,
////				2,3,
////				3,0,
////				
////				0+4,1+4,
////				1+4,2+4,
////				2+4,3+4,
////				3+4,0+4,
////
////				0,1+3,
////				1,2+3,
////				2,3+3,
////				3,4+3,
//				0, 1, 2,//前 
//				0, 2, 3, 
//				
//				0, 3, 7, //右
//				7, 4, 0,
//				
//				1, 2, 6, //左
//				6, 5, 1,
//				
//				5, 6, 7, //后
//				5, 7, 4, 
//				
//				1, 5, 4,//上 
//				4, 0, 1,
//				
//				2, 6, 7,//下 
//				7, 3, 2,
//				
//				});
//		mMesh.Name = "BoardMaterial" + i;
//
//		
//	}


	public void setTexture(Bitmap bitmap,boolean recycleLast)
	{
		
		Texture tempTexture = mNormalTexture;
		mNormalTexture = Director.sResourcer.generateTexture(bitmap);
		if(recycleLast){
			Director.sResourcer.recycleTexture(tempTexture);
		}
		
	}

	public void setTexture(Texture texture,boolean recycleLast)
	{
		Texture tempTexture = mNormalTexture;
		mNormalTexture = texture;

		if(recycleLast){
			Director.sResourcer.recycleTexture(tempTexture);
		}
	}
	public void setTexture(Bitmap bitmap)
	{
		mNormalTexture = Director.sResourcer.generateTexture(bitmap);
	}

	public void setTexture(Texture texture)
	{
		mNormalTexture = texture;
	}

	public Texture getTexture() {
		return mNormalTexture;
	}
	public void setTexture(int resid)
	{
		mNormalTexture = Director.sResourcer.generateTexture(resid);
	}
	public void setTexture(int resid,boolean recycleLast)
	{

		Texture tempTexture = mNormalTexture;
		mNormalTexture = Director.sResourcer.generateTexture(resid);

		if(recycleLast){
			Director.sResourcer.recycleTexture(tempTexture);
		}
	}

	/**
	 * @param selectorResid
	 * @Deprecated 暂时没有实现此方法
	 */
	@Deprecated
	public void setSelectorDrawable(int selectorResid)
	{
		// ***
	}


	protected void onAnimation()
	{

		mTransform.run();
		if (mTransform.hasMoreAnimation() || mFirst){

			mTransform.normal(mDrawableWidth, mDrawableHeight, mDrawableThickness, 1, 1, 1);
			mFirst = false;
			invalidate();
			
		}
	}

	public void onDrawbleTransform()
	{
		Director.sGLESVersion.onDrawableTransform(mTransform);
	}

	public void onRender()
	{
		if (mMesh != null)
		{
			// 在T_GLES20.java中
			Director.sGLESVersion.onRender(mMesh,mMaterial, mSelfModelViewMatrix,true);
		}
	}

	

	public float getWidth()
	{
		return mDrawableWidth;
	}
	/**
	 * 
	 * @return  返回drawable各自的透明度
	 */
	public float getDrawableAlpha(){
		return mTransform.Alpha;
	}

	public void setWidth(float f)
	{
		this.mDrawableWidth = f;
		mFirst = true;
	}

	public float getHeight()
	{
		return mDrawableHeight;
	}

	public void setHeight(float f)
	{
		this.mDrawableHeight = f;
		mFirst = true;
	}

	public float getThickness()
	{
		return mDrawableThickness;
	}

	public void setThickness(float f)
	{
		this.mDrawableThickness = f;
	}

	/**
	 * 刷新绘图帧
	 */
	public void invalidate()
	{
		Director.getInstance().postInvalidate();
	}

	public void setModelViewMatrix(float[] finalModelViewMatrix)
	{
		this.mSelfModelViewMatrix = finalModelViewMatrix;
	}

	public void setRenderable(boolean b) {

		this.mRenderable = b;
	}

	public boolean isRenderable() {
		return mRenderable;
	}
	public boolean generateBound() {
		
		boolean returnFlag = false;
		float  minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
		float  maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
		

		List<float[]> mMeshVertexes          = new ArrayList<float[]>();
		if (mMesh != null) {
			float[] array = new float[mMesh.VertexBuffer.capacity()];
			mMesh.VertexBuffer.position(0);
			mMesh.VertexBuffer.get(array);
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
			returnFlag = true;
		}

//		Log.d("bound", " =======drawable========= ");
//		Log.d("bound", "minX = " + minX + ",minY = "+minY + ",minZ = "+minZ);
//		Log.d("bound", "maxX = " + maxX + ",maxY = "+maxY + ",maxZ = "+maxZ);
//		Log.d("bound", " ================= ");
		
		if(returnFlag){
			BoundSphere boundSphere = new BoundSphere();
			boundSphere.generateBoundSphere(minX, minY, minZ, maxX, maxY, maxZ);
			mBoundSphereList.add(boundSphere);
			
			BoundBox boundBox = new BoundBox();
			boundBox.generateBoundBox(minX, minY, minZ, maxX, maxY, maxZ);
			mBoundBoxList.add(boundBox);
		}
		
		return returnFlag;
	}
	public ArrayList<PointF> findDrawableScreenCoordinate(T_AABBBox t_AABBBox) {
		
//		String pointString = "";
		ArrayList<PointF> raycastPointList = new ArrayList<PointF>();
		for (int i = 0; i < mBoundBoxList.size(); i++) {
			BoundBox boundBox = mBoundBoxList.get(i);
			// 投射左上点
			PointF pointA = new PointF();
			t_AABBBox.transformToScreenCoordinate(pointA,boundBox.getMinX(),boundBox.getMaxY(),boundBox.getMinZ());
			raycastPointList.add(pointA);
			// 投射左下点
			PointF pointB = new PointF();
			t_AABBBox.transformToScreenCoordinate(pointB,boundBox.getMinX(),boundBox.getMinY(),boundBox.getMinZ());
			raycastPointList.add(pointB);
			// 投射右下点
			PointF pointC = new PointF();
			t_AABBBox.transformToScreenCoordinate(pointC,boundBox.getMaxX(),boundBox.getMinY(),boundBox.getMinZ());
			raycastPointList.add(pointC);
			// 投射右上点
			PointF pointD = new PointF();
			t_AABBBox.transformToScreenCoordinate(pointD,boundBox.getMaxX(),boundBox.getMaxY(),boundBox.getMinZ());
			raycastPointList.add(pointD);
//			pointString +=  " getMinX--> "+boundBox.getMinX();
//			pointString +=  " getMaxX--> "+boundBox.getMaxX();;
//			pointString +=  " getMinY--> "+boundBox.getMinY();;
//			pointString +=  " getMaxY--> "+boundBox.getMaxY();;
//			pointString +=  " getMinZ--> "+boundBox.getMinZ();;
//			pointString +=  " getMaxZ--> "+boundBox.getMaxZ();;
		}

//		Log.d("ming", "findAllTransformPoint "+pointString);
		return raycastPointList;
	}
	public ArrayList<Vector3> findDrawableWorldCoordinate(T_AABBBox t_AABBBox) {	
//		String pointString = "";
		ArrayList<Vector3> worldPointList = new ArrayList<Vector3>();
		for (int i = 0; i < mBoundBoxList.size(); i++) {
			BoundBox boundBox = mBoundBoxList.get(i);
			// 投射左上点
			float[] vectorA = new float[4] ;
			t_AABBBox.transformToWorldCoordinate(vectorA,boundBox.getMinX(),boundBox.getMaxY(),boundBox.getMinZ());
			worldPointList.add(new Vector3(vectorA[0],vectorA[1],vectorA[2]));
			// 投射左下点
			float[] vectorB = new float[4];
			t_AABBBox.transformToWorldCoordinate(vectorB,boundBox.getMinX(),boundBox.getMinY(),boundBox.getMinZ());
			worldPointList.add(new Vector3(vectorB[0],vectorB[1],vectorB[2]));
			// 投射右下点
			float[] vectorC = new float[4];
			t_AABBBox.transformToWorldCoordinate(vectorC,boundBox.getMaxX(),boundBox.getMinY(),boundBox.getMinZ());
			worldPointList.add(new Vector3(vectorC[0],vectorC[1],vectorC[2]));
			// 投射右上点
			float[] vectorD = new float[4];
			t_AABBBox.transformToWorldCoordinate(vectorD,boundBox.getMaxX(),boundBox.getMaxY(),boundBox.getMinZ());
			worldPointList.add(new Vector3(vectorD[0],vectorD[1],vectorD[2]));
//			pointString +=  " getMinX--> "+boundBox.getMinX();
//			pointString +=  " getMaxX--> "+boundBox.getMaxX();;
//			pointString +=  " getMinY--> "+boundBox.getMinY();;
//			pointString +=  " getMaxY--> "+boundBox.getMaxY();;
//			pointString +=  " getMinZ--> "+boundBox.getMinZ();;
//			pointString +=  " getMaxZ--> "+boundBox.getMaxZ();;
		}
//		Log.d("ming", "findAllTransformPoint "+pointString);
		return worldPointList;
	}
	
	public void alphaTo(float alpha) {
		mTransform.alphaTo(alpha);
	}
	public void alphaTo(float alpha, int time) {
		mTransform.alphaTo(alpha,time);
	}
	public void setScale(float x, float y, int z) {
		mTransform.setScale(x, y, z);
	}
	public void setAlpha(float alpha) {
		mTransform.setAlpha(alpha);
	}
	public void scaleTo(float x, float y, float z) {
		mTransform.scaleTo(x, y, z);
	}
	public void scaleTo(float x, float y, float z, int time) {
		mTransform.scaleTo(x, y, z,time);
	}
	public void translateTo(float x, float y, float z) {
		mTransform.translateTo(x, y, z);
	}
	public void setTranslate(float x, float y, float z) {
		mTransform.setTranslate(x, y, z);
	}
	public void setCullFrontFace(boolean b) {
		
	}

}
