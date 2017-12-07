package com.x.opengl.kernel;

import java.nio.FloatBuffer;

import com.x.Director;

/**
 * 材质
 * @date   2013-08-08 11:10:10
 */
public class Material {

	public static final String TAG = "Material";
	
	public  String   Name        = null;
	public  Texture  Texture             = null;
	private int      mIllumination       = 2;    // 光照模型
	private float[]  mMainColor          = null; // 主调颜色
	private float[]  mSpecular           = null; // 反射光
	private float[]  mDiffuse            = null; // 漫反射
	private float[]  mAmbient            = null; // 环境光
	private float[]  mEmission           = null; // 自发光
	private float[]  mTransmissionFilter = null; // 滤光透射率
	private float    mShininess          = 0;    // 光泽度
	private float    mOpticalDensity     = 1.0f; // 折射值
	private float    mTransparent        = 1.0f; // 透明度
	private float    mSharpness          = 60;   // 锐利度

	public FloatBuffer mMainColorBuffer;
	public FloatBuffer mSpecularBuffer;
	public FloatBuffer mDiffuseBuffer;
	public FloatBuffer mAmbientBuffer;
	public FloatBuffer mEmissionBuffer;
	
	public Material() {
		mMainColor          = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
		mSpecular           = new float[4];
		mDiffuse            = new float[4];
		mAmbient            = new float[4];
		mEmission           = new float[4];
		mTransmissionFilter = new float[4];
	}
	
	/**
	 * 设置环境光颜色 Ka
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setAmbient(float r, float g, float b, float a) {
		mAmbient[0] = r;
		mAmbient[1] = g;
		mAmbient[2] = b;
		mAmbient[3] = a;
		mAmbientBuffer = Director.sResourcer.floatBuffer(mAmbient);
	}
	
	/**
	 * 设置环境光颜色 Ka
	 * @param ambient
	 */
	public void setAmbient(float[] ambient) {
		int size = ambient.length;
		for (int i = 0; i < size; i ++) {
			mAmbient[i] = ambient[i];
		}
		if (mAmbient.length < 4) {
			mAmbient[3] = mMainColor[3];
		}
		
		mAmbientBuffer = Director.sResourcer.floatBuffer(mAmbient);
	}
	
	/**
	 * 获取环境光
	 * @return
	 */
	public float[] getAmbient() {
		return mAmbient;
	}

	/**
	 * 设置反射光颜色 Ks
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setSpecular(float r, float g, float b, float a) {
		mSpecular[0] = r;
		mSpecular[1] = g;
		mSpecular[2] = b;
		mSpecular[3] = a;
		mSpecularBuffer = Director.sResourcer.floatBuffer(mSpecular);
	}
	
	/**
	 * 设置反射光颜色 Ks
	 * @param specular
	 */
	public void setSpecular(float[] specular) {
		int size = specular.length;
		for (int i = 0; i < size; i ++) {
			mSpecular[i] = specular[i];
		}
		if (mSpecular.length < 4) {
			mSpecular[3] = mMainColor[3];
		}
		mSpecularBuffer = Director.sResourcer.floatBuffer(mSpecular);
	}

	
	/**
	 * 获取反射光
	 * @return
	 */
	public float[] getSpecular() {
		return mSpecular;
	}
	
	/**
	 * 设置漫反射光颜色 Kd
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setDiffuse(float r, float g, float b, float a) {
		mDiffuse[0] = r;
		mDiffuse[1] = g;
		mDiffuse[2] = b;
		mDiffuse[3] = a;
		mDiffuseBuffer = Director.sResourcer.floatBuffer(mDiffuse);
	}
	
	/**
	 * 设置漫反射光颜色 Kd
	 * @param diffuse
	 */
	public void setDiffuse(float[] diffuse) {
		int size = diffuse.length;
		for (int i = 0; i < size; i ++) {
			mDiffuse[i] = diffuse[i];
		}
		if (mDiffuse.length < 4) {
			mDiffuse[3] = mMainColor[3];
		}
		mDiffuseBuffer = Director.sResourcer.floatBuffer(mDiffuse);
	}
	
	/**
	 * 获取漫反射光
	 * @return
	 */
	public float[] getDiffuse() {
		return mDiffuse;
	}
	
	/**
	 * 设置自发光颜色 Ke
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setEmission(float r, float g, float b, float a) {
		mEmission[0] = r;
		mEmission[1] = g;
		mEmission[2] = b;
		mEmission[3] = a;
		mEmissionBuffer = Director.sResourcer.floatBuffer(mEmission);
	}
	
	/**
	 * 设置自发光颜色 Ke
	 * @param emission
	 */
	public void setEmission(float[] emission) {
		int size = emission.length;
		for (int i = 0; i < size; i ++) {
			mEmission[i] = emission[i];
		}
		if (mEmission.length < 4) {
			mEmission[3] = mMainColor[3];
		}
		mEmissionBuffer = Director.sResourcer.floatBuffer(mEmission);
	}
	
	/**
	 * 获取自发光
	 * @return
	 */
	public float[] getEmission() {
		return mEmission;
	}
	
	/**
	 * 设置光泽度 Ns
	 * @param shininess 0 ~ 128
	 */
	public void setShininess(float shininess) {
		mShininess = shininess;
	}
	
	public float getShininess() {
		return mShininess;
	}
	
	/**
	 * 设置折射值 Ni
	 * @param opticalDensity [0.001, 1.0, 10]
	 */
	public void setOpticalDensity(float opticalDensity) {
		mOpticalDensity = opticalDensity;
	}
	
	public float getOpticalDensity() {
		return mOpticalDensity;
	}
	
	/**
	 * 设置透明度 Tr
	 * @param transparent [0.0 1.0]
	 */
	public void setTransparent(float transparent) {
		mTransparent = transparent;
	}
	
	public float getTransparent() {
		return mTransparent;
	}
	
	/**
	 * 设置透明度 d
	 * @param alpha [0.0 1.0]
	 */
	public void setAlpha(float alpha) {
		mMainColor[3] = alpha;
		mAmbient[3] = mMainColor[3];
		mDiffuse[3] = mMainColor[3];
		mSpecular[3] = mMainColor[3];
		mEmission[3] = mMainColor[3];
	}
	
	public float getAlpha() {
		return mMainColor[3];
	}
	
	public float[] getMainColor() {
		return mMainColor;
	}
	
	public void setMainColor(float r, float g, float b, float a) {
		mMainColor[0] = r;
		mMainColor[1] = g;
		mMainColor[2] = b;
		mMainColor[3] = a;
	}
	
	public void setMainColor(float[] mainColor) {
		mMainColor = mainColor;
	}
	
	/**
	 * 设置滤波透射率 Tf
	 * @param transmissionFilter
	 */
	public void setTransmissionFilter(float r, float g, float b, float a) {
		mTransmissionFilter[0] = r;
		mTransmissionFilter[1] = g;
		mTransmissionFilter[2] = b;
		mTransmissionFilter[3] = a;
	}
	
	public void setTransmissionFilter(float[] transmissionFilter) {
		mTransmissionFilter = transmissionFilter;
	}
	
	public float[] getTransmissionFilter() {
		return mTransmissionFilter;
	}
	
	/**
	 * 设置锐利度 sharpness
	 * @param sharpness [0 60 1000]
	 */
	public void setSharpness(float sharpness) {
		mSharpness = sharpness;
	}
	
	public float getSharpness() {
		return mSharpness;
	}
	
	
	/**
	 * 设置光照模型 illum
	 * @param illumination = 
	 *                   0 Color on and Ambient off
     *                   1 Color on and Ambient on
     *                   2 Highlight on
     *                   3 Reflection on and Ray trace on
     *                   4 Transparency: Glass on
     *                     Reflection: Ray trace on
     *                   5 Reflection: Fresnel on and Ray trace on
     *                   6 Transparency: Refraction on
     *                     Reflection: Fresnel off and Ray trace on
     *                   7 Transparency: Refraction on
     *                     Reflection: Fresnel on and Ray trace on
     *                   8 Reflection on and Ray trace off
     *                   9 Transparency: Glass on
     *                     Reflection: Ray trace off
     *                  10 Casts shadows onto invisible surfaces
	 * @return
	 */
	public void setIllumination(int illumination) {
		mIllumination = illumination;
	}
	
	public int getIllumination() {
		return mIllumination;
	}

	public void recycle() {
		if (Texture != null) {
			Texture.recycle();
		}
	}

}
