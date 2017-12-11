package com.x.opengl.kernel;

import java.util.ArrayList;

import android.graphics.PointF;

import com.x.Director;

public class ObjDrawable extends Drawable{
	
	public MaterialGroup mMaterials = null;//专为加载Obj格式的文件而用个的临时变量
	public Mesh[] mMeshes = null;//专为加载Obj格式的文件而用个的临时变量
	public ArrayList<Box> mAABBBoxCheckList = new ArrayList<Box>();
	private boolean mCullFrontFlag  = false;
	

	public void setCullFrontFace(boolean b) {
		mCullFrontFlag =b;
	}
	class Box{
		public Box(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
			this.minX = minX;
			this.minY = minY;
			this.minZ = minZ;

			this.maxX = maxX;
			this.maxY = maxY;
			this.maxZ = maxZ;
		}
		public float  minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
		public float  maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
		public void generateBound(BoundSphere boundSphere, BoundBox boundBox) {
//			Log.d("bound", "objDrawable =========== ");
//
//			Log.d("bound", "minX = " + minX + ",minY = "+minY + ",minZ = "+minZ);
//			Log.d("bound", "maxX = " + maxX + ",maxY = "+maxY + ",maxZ = "+maxZ);

			boundSphere.generateBoundSphere(minX, minY, minZ, maxX, maxY, maxZ);
			boundBox.generateBoundBox(minX, minY, minZ, maxX, maxY, maxZ);
		}

	}
	
	public ObjDrawable(int depth) {
		super(depth);
	}

	/**
	 * 由于三维模型一个单位和我们这里的一个单位并不对应，但是导出模型时又无法确定我们这边的EngineConstanst.PIX_REFERENCE的具体值，所以导出模型时只能将模型缩小为3dmax里面的默认的一个单位
	 * 而后续的乘法（ * EngineConstanst.PIX_REFERENCE）由如下代码实现
	 * 
	 * （EngineConstanst.PIX_REFERENCE的值是跟屏幕相关的），故这里采用在代码中乘以我们算出的EngineConstanst.PIX_REFERENCE值，
	 */
	@Override
	public void setWidth(float f) {
		super.setWidth(f*EngineConstanst.PIX_REFERENCE);
	}
	@Override
	public void setHeight(float f) {
		super.setHeight(f*EngineConstanst.PIX_REFERENCE);
	}
	@Override
	public void setThickness(float f) {
		super.setThickness(f*EngineConstanst.PIX_REFERENCE);
	}
	public ObjDrawable cloneObjDrawable() {

		ObjDrawable objDrawable  = new ObjDrawable(-1);
		objDrawable.mMaterials = mMaterials;
		objDrawable.mMeshes = mMeshes;
		objDrawable.mAABBBoxCheckList = mAABBBoxCheckList;
		return objDrawable;
	}


	public void addAABBBoxPoint(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		mAABBBoxCheckList.add(new Box(minX, minY, minZ, maxX, maxY, maxZ));
	}

	@Override
	public boolean generateBound() {

		boolean flag = false;

//		Log.d("generate", "mAABBBoxCheckList = "+mAABBBoxCheckList.size());
		for (int i = 0; i < mAABBBoxCheckList.size(); i++) {
			Box box = mAABBBoxCheckList.get(i);

			BoundBox boundBox = new BoundBox();
			boundBox.generateBoundBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
			mBoundBoxList.add(boundBox);


			BoundSphere boundSphere = new BoundSphere();
			boundSphere.generateBoundSphere(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
			mBoundSphereList.add(boundSphere);

			flag = true;
		}
		return flag;
	}

	@Override
	public ArrayList<PointF> findDrawableScreenCoordinate(T_AABBBox t_AABBBox) {

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


			// 投射左上点
			PointF pointAback = new PointF();
			t_AABBBox.transformToScreenCoordinate(pointAback,boundBox.getMinX(),boundBox.getMaxY(),boundBox.getMaxZ());
			raycastPointList.add(pointAback);
			// 投射左下点
			PointF pointBback = new PointF();
			t_AABBBox.transformToScreenCoordinate(pointBback,boundBox.getMinX(),boundBox.getMinY(),boundBox.getMaxZ());
			raycastPointList.add(pointBback);
			// 投射右下点
			PointF pointCback = new PointF();
			t_AABBBox.transformToScreenCoordinate(pointCback,boundBox.getMaxX(),boundBox.getMinY(),boundBox.getMaxZ());
			raycastPointList.add(pointCback);
			// 投射右上点
			PointF pointDback = new PointF();
			t_AABBBox.transformToScreenCoordinate(pointDback,boundBox.getMaxX(),boundBox.getMaxY(),boundBox.getMaxZ());
			raycastPointList.add(pointDback);

		}
		return raycastPointList;
	}
	public void draw( ) {
			

			if(mCullFrontFlag){
				Director.sGLESVersion.openCullFrontFace();
			}else{
				Director.sGLESVersion.openCullBackFace();
			}
			Director.sGLESVersion.openDepthTest(); //这里需要启用深度测试，因为这是一个obj文件load出来的view中drawable，因为 scene的原因，我们关闭了深度测试，所以需要对该部分重新启用深度测试
			Director.sGLESVersion.pushMatrix();

			onAnimation();
			onDrawbleTransform();

//			Log.d("ming", "lallalallala========================mMeshes =="+mMeshes.length );
//			Log.d("ming", "lallalallala========================mMeshes =="+mMeshes[0].Name );
//			Log.d("ming", "lallalallala========================mMaterials =="+mMaterials.size());
//			Log.d("ming", "lallalallala========================mMaterials =="+mMaterials.getMaterial(0).Name);
//			for (int j = 0; j < mMaterials.size(); j++) {
//				Material gj = mMaterials.getMaterial(j);
//				Log.d("ming", "materialgroup["+j+"] name = "+gj.Name );
//			}
			
			
			int meshSize = mMeshes.length;
			for (int i = 0; i < meshSize; i++) {
				if (mMeshes[i].isEnabled()) {
					 onRender(mMeshes[i],mMaterials.getMaterial(mMeshes[i].Name));
				}
			}
			onRayCast();  
			
			Director.sGLESVersion.popMatrix();
			Director.sGLESVersion.closeDepthTest();
			Director.sGLESVersion.closeCullFace();
			
	} 
	private void onRender(Mesh mesh, Material material)
	{
		Director.sGLESVersion.onRender(mesh,  material,null,false);
	}
	
	

}
