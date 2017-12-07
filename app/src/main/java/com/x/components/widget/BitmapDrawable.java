package com.x.components.widget;
//package com.kaiboer.wolf.widget;
//
//import android.graphics.Bitmap;
//
//import com.kaiboer.wolf.WolfRenderer;
//import com.kaiboer.wolf.kernel.Material;
//import com.kaiboer.wolf.kernel.MaterialGroup;
//import com.kaiboer.wolf.kernel.Mesh;
//
//public class BitmapDrawable extends Drawable
//{
//	private Bitmap mBitmap = null;
//
//	public BitmapDrawable()
//	{
//		super();
//	}
//
//	public BitmapDrawable(Bitmap bitmap)
//	{
//		super();
//		setTexture(bitmap);
//	}
//
//	public BitmapDrawable(int resid)
//	{
//		super();
//		setTexture(resid);
//	}
//
//	@Override
//	protected void initMesh()
//	{
//		mMesh = new Mesh();
//		mMesh.setVertexes(new float[] { +0.5000f, +0.5000f, +0.0000f, -0.5000f, +0.5000f, +0.0000f, -0.5000f, -0.5000f, -0.0000f, +0.5000f, -0.5000f, -0.0000f });
//		mMesh.setNormals(new float[] { +0.0000f, -0.0000f, +1.0000f, +0.0000f, -0.0000f, +1.0000f, +0.0000f, -0.0000f, +1.0000f, +0.0000f, -0.0000f, +1.0000f });
//		mMesh.setCoordinates(new float[] { +1.0000f, +0.0000f, +0.0000f, +0.0000f, +0.0000f, +1.0000f, +1.0000f, +1.0000f });
//		mMesh.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });
//	}
//
//	@Override
//	protected void initMaterial()
//	{
//		mMaterial = new Material();
//		// 设置材质名称
//		// boardMaterial.Name = "BoardMaterial" + i;
//		// 设置材质属性
//		mMaterial.setAmbient(0.8000f, 0.8000f, 0.8000f, 1.0000f);
//		mMaterial.setDiffuse(0.8000f, 0.8000f, 0.8000f, 1.0000f);
//		mMaterial.setSpecular(0.8000f, 0.8000f, 0.8000f, 1.0000f);
//		mMaterial.setEmission(0.0000f, 0.0000f, 0.0000f, 1.0000f);
//		mMaterial.setAlpha(0.99999f);
//		mMaterial.setOpticalDensity(1.5000f);
//		mMaterial.setShininess(30.0000f);
//		mMaterial.setTransparent(0.3000f);
//		mMaterial.setTransmissionFilter(0.7000f, 0.7000f, 0.7000f, 0.7000f);
//		mMaterial.setIllumination(2);
//		
//		mMaterialGroup = new MaterialGroup();
//		mMaterialGroup.addMaterial(mMaterial);
//	}
//
//
//}
