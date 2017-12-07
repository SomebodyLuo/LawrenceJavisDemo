package com.x.components.node;

import java.nio.FloatBuffer;

import com.x.Director;
import com.x.opengl.kernel.ObjDrawable;
import com.x.components.node.View;

/**
		OpenGL默认是关闭光照处理的。要打开光照处理功能，使用下面的语句：
		glEnable(GL_LIGHTING);
		要关闭光照处理功能，使用glDisable(GL_LIGHTING);即可。
		
		
		*  光照系统分为三部分，分别是光源、材质和光照环境。
		*  
		* 一. 光源:   glLight*函数设置
		*    
		*    		(1) 特性
			GL_AMBIENT  表示该光源所发出的光，经过非常多次的反射后，最终遗留在整个光照环境中的强度（颜色）。----  光的环境成分
			GL_DIFFUSE  表示该光源所发出的光，照射到粗糙表面时经过漫反射，所得到的光的强度（颜色）      ---------光的漫反射成分
			GL_SPECULAR  表示该光源所发出的光，照射到光滑表面时经过镜面反射，所得到的光的强度（颜色）。------- 光的镜面反射成分
			
			这三个属性表示了光源所发出的光的反射特性（以及颜色）。
			每个属性由四个值表示，分别代表了颜色的R, G, B, A值。
			
			(2)
			GL_POSITION  位置
			表示光源所在的位置。
			由四个值（X, Y, Z, W）表示。
			<1 当W为零，则表示该光源位于无限远处，前三个值表示了它所在的方向。
			这种光源称为方向性光源，通常，太阳可以近似的被认为是方向性光源。
			
			<2 当W不为零，则X/W, Y/W, Z/W表示了光源的位置。这种叫做位置型光源
			以下这几个属性只对位置光源有效
			
			很多光源都是向四面八方发射光线，但有时候一些光源则是只向某个方向发射，比如手电筒，只向一个较小的角度发射光线。
			GL_SPOT_DIRECTION、 属性有三个值，表示一个向量，即光源发射的方向。
			GL_SPOT_EXPONENT、属性只有一个值，表示聚光的程度，为零时表示光照范围内向各方向发射的光线强度相同，为正数时表示光照向中央集中，正对发射方向的位置受到更多光照，其它位置受到较少光照。数值越大，聚光效果就越明显。
			GL_SPOT_CUTOFF 属性也只有一个值，表示一个角度，它是光源发射光线所覆盖角度的一半（见下图），其取值范围在0到90之间，也可以取180这个特殊值。取值为180时表示光源发射光线覆盖360度，即不使用聚光灯，向全周围发射。
				  /	
				 /
				/___半角_____ 
				\
				 \
				  \
			这三个属性表示了光源所发出的光线的直线传播特性（这些属性只对位置性光源有效）。
			GL_CONSTANT_ATTENUATION、  ------------k1
			GL_LINEAR_ATTENUATION、---------------k2
			GL_QUADRATIC_ATTENUATION属性。---------k3
			
			现实生活中，光线的强度随着距离的增加而减弱，OpenGL把这个减弱的趋势抽象成函数：
			衰减因子 = 1 / (k1 + k2 * d + k3 * k3 * d)
			
			
			
		  二.材质 :  glMaterial*函数
		 glMaterial*函数有三个参数。第一个参数表示指定哪一面的属性。可以是GL_FRONT、GL_BACK或者GL_FRONT_AND_BACK。
		
		（1）
			这三个属性与光源的三个对应属性类似，每一属性都由四个值组成。 
			GL_AMBIENT 表示各种光线照射到该材质上，经过很多次反射后最终遗留在环境中的光线强度（颜色）。---材质的环境光成分
			GL_DIFFUSE 表示光线照射到该材质上，经过漫反射后形成的光线强度（颜色）。-----------------材质的漫反射成分
			GL_SPECULAR 表示光线照射到该材质上，经过镜面反射后形成的光线强度（颜色）。--------------材质的镜面反射成分
			通常，GL_AMBIENT和GL_DIFFUSE都取相同的值，可以达到比较真实的效果。
			使用GL_AMBIENT_AND_DIFFUSE可以同时设置GL_AMBIENT和GL_DIFFUSE属性。
		
		
		（2）GL_SHININESS属性。
		
			该属性只有一个值，称为“镜面指数”，取值范围是0到128。
			该值越小，表示材质越粗糙，点光源发射的光线照射到上面，也可以产生较大的亮点。该值越大，表示材质越类似于镜面，光源照射到上面后，产生较小的亮点。
			
		
		（3）GL_EMISSION属性。该属性由四个值组成，表示一种颜色。OpenGL认为该材质本身就微微的向外发射光线，以至于眼睛感觉到它有这样的颜色，但这光线又比较微弱，以至于不会影响到其它物体的颜色。
		
		（4）GL_COLOR_INDEXES属性。
			该属性仅在颜色索引模式下使用，由于颜色索引模式下的光照比RGBA模式要复杂，并且使用范围较小，这里不做讨论。
			
		*/
//
public class LightView extends   View{

	public static final String TAG = "Material";
	
	public  String   Name        = null;
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

	private boolean mRun;

	private int rotate;
	
	public LightView() {
		mMainColor          = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
		mSpecular           = new float[]{0.6f,0.6f,0.6f,1.0f};
		mDiffuse            = new float[]{0.40f,0.40f,0.40f,1.0f};
		mAmbient            = new float[]{0.45f,0.45f,0.45f,1.0f};
		mEmission           = new float[4];
		mTransmissionFilter = new float[4];
		
	}


	public LightView(ObjDrawable objDrawable) {
		super(objDrawable);

		mMainColor          = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
		mSpecular           = new float[]{0.6f,0.6f,0.6f,1.0f};
		mDiffuse            = new float[]{0.50f,0.50f,0.50f,1.0f};
		mAmbient            = new float[]{0.45f,0.45f,0.45f,1.0f};
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


	public float[] getPosition() {
		return new float[]{mTransform.Position.X,mTransform.Position.Y,mTransform.Position.Z};
	}


	public float[] getLightMMatrix() {
		return getFinalModelMatrix();
	}


	public void setRotateEnable(boolean b) {
		mRun = b;
	}

	@Override
	protected void onAnimation() {
		// TODO Auto-generated method stub
		super.onAnimation();
		if(!mRun){
			return;
		}
		rotate += 1;
		if(rotate > 360){
			rotate = 0;
		}
//		Log.d("ming", "rotate "+rotate);
		setRotate(0, rotate,0);
//		setScale(rotate,rotate, 1);
	}
}
