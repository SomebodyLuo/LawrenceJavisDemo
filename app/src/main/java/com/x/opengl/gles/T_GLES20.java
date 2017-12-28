package com.x.opengl.gles;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.x.components.node.View;
import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.Matrix;
import com.x.opengl.kernel.Mesh;
import com.x.opengl.kernel.T_AABBBox;
import com.x.opengl.kernel.Texture;
import com.x.opengl.kernel.Transform;

@SuppressLint("NewApi")
public class T_GLES20 extends GLES {

	private GL10	mGL;


	private BaseShader mBaseShader = new BaseShader();
	private ObjShader mObjShader = new ObjShader();
	private OESShader mOESShader = new OESShader();
	

	@Override 
	public Bitmap getSnapshot(int width,int heght){
		return SavePixels(0, 0, width, heght, mGL);
	}


	private Bitmap SavePixels(int x, int y, int w, int h, GL10 gl) {
		int b[] = new int[w * h];
		int bt[] = new int[w * h];
		IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		GLES20.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int pix = b[i * w + j];
				int pb = (pix >> 16) & 0xff;
				int pr = (pix << 16) & 0x00ff0000;
				int pix1 = (pix & 0xff00ff00) | pr | pb;
				bt[(h - i - 1) * w + j] = pix1;
			}
		}
		Bitmap sb = Bitmap.createBitmap(bt, w, h, Config.ARGB_8888);
		return sb;
	}

//	private Bitmap readPixels(int x, int y, int w, int h, GL10 gl) {
//		int pix[] = new int[w * h];
//		IntBuffer pixelBuffer = IntBuffer.allocate(w * h);
//		pixelBuffer.position(0);
//		gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixelBuffer);
//		pixelBuffer.get(pix);
//
//		Bitmap sb = Bitmap.createBitmap(pix, w, h, Config.ARGB_8888);
//		return sb;
//	}
	@Override
	public void initGLSystem(GL10 gl) {

		this.mGL = gl;
		// 设置屏幕背景色RGBA
//		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glCullFace(GLES20.GL_BACK);
		
//		GLES20.glCullFace(GLES20.GL_FRONT);
//		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ZERO);

		
		mBaseShader.init();
		mObjShader.init();
		mOESShader.init();
	}

	@Override
	public void resetFrame(GL10 gl) {
          
//		IntBuffer  intBuffer = IntBuffer.allocate(10);
//		GLES20.glGetIntegerv(GLES20.GL_DEPTH_CLEAR_VALUE, intBuffer);
//		Log.d("DEPTH", ""+intBuffer.get(0));

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
//		GLES20.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
		GLES20.glClearStencil(0);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT |GLES20.GL_STENCIL_BUFFER_BIT);
		StencilValueStatck.clear();
	}

	
	@Override
	public void pushMatrix() {
		MatrixState.pushMatrix();
	}

	@Override
	public void popMatrix() {
		MatrixState.popMatrix();
	}

	@Override
	public void getViewportMatrix(T_AABBBox aabbBox) {
		MatrixState.getViewportMatrix(aabbBox);
	}

	@Override
	public void getCameraLookVector(T_AABBBox aabbBox) {
		MatrixState.getCameraLookVector(aabbBox);
	}
	@Override
	public void getProjectionMatrix(T_AABBBox aabbBox) {
		MatrixState.getProjectionMatrix(aabbBox);
	}

	@Override
	public void getModelMatrix(T_AABBBox aabbBox) {
		MatrixState.getModelMatrix(aabbBox);
	}

	@Override
	public void getViewMatrix(T_AABBBox aabbBox) {
		// TODO Auto-generated method stub
		MatrixState.getViewMatrix(aabbBox);
	}

//	@Override
//	public void onMesh(Mesh mesh,float[] selfModelViewMatrix) {
//
//		// 制定使用某套shader程序
//		GLES20.glUseProgram(mProgram);
//		// 将最终变换矩阵传入shader程序
//		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(selfModelViewMatrix), 0);
//
//		if (mesh.VertexBuffer != null) {
//			// 为画笔指定顶点位置数据
//			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mesh.VertexBuffer.rewind());
//			GLES20.glEnableVertexAttribArray(maPositionHandle);
//		}
//		
////		if(mesh.ColorBuffer != null){
////			GLES20.glVertexAttribPointer(maColorHandle,4,GLES20.GL_FLOAT,false,4*4,mesh.ColorBuffer);
////			GLES20.glEnableVertexAttribArray(maColorHandle);
////		}
//		
//		if (mesh.CoordBuffer != null) {
//			// 将顶点纹理坐标数据传递到渲染管线
//			GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mesh.CoordBuffer);
//			GLES20.glEnableVertexAttribArray(maTexCoorHandle);
//		}
//		
//
//		if (mesh.IndexBuffer != null) {
//			mesh.IndexBuffer.rewind();
//			GLES20.glDrawElements(mesh.TriangleType, mesh.IndexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, mesh.IndexBuffer);
//		}
//
//	}

	// luoyouren:
	@Override
	public void onRender(Mesh mesh,Material mMaterial,float[] selfModelViewMatrix,boolean baseShader) {
		
		if(baseShader){
			if(mMaterial.Texture.getBindTarget() == GLES11Ext.GL_TEXTURE_EXTERNAL_OES){
				mOESShader.goRender(mesh, mMaterial, selfModelViewMatrix);
			}else{
				mBaseShader.goRender(mesh,  mMaterial,selfModelViewMatrix);
			}
		}else{
			if(mMaterial.Texture.getBindTarget() == GLES11Ext.GL_TEXTURE_EXTERNAL_OES){
				mOESShader.goRender(mesh, mMaterial, selfModelViewMatrix);
			}else{
				mObjShader.goRender(mesh,  mMaterial,selfModelViewMatrix);
			}
		}
		checkGlError("onMesh()");
	}

    public void checkGlError(String op)
    {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("SurfaceTest", op + ": glError " + GLUtils.getEGLErrorString(error));
        }
    }

	@Override
	public void onViewTransform(Transform finalTransform) {

//		float translateX = finalTransform.Position.X;
//		float translateY = finalTransform.Position.Y;
//		float translateZ = finalTransform.Position.Z;
//
//		float scaleX = finalTransform.Scale.X;
//		float scaleY = finalTransform.Scale.Y;
//		float scaleZ = finalTransform.Scale.Z;


		MatrixState.alpha(finalTransform.Alpha);
		MatrixState.translate(finalTransform.Position.X,finalTransform.Position.Y,finalTransform.Position.Z);
		MatrixState.rotate(finalTransform.Rotate.X, 1, 0, 0);
		MatrixState.rotate(finalTransform.Rotate.Y, 0, 1, 0);
		MatrixState.rotate(finalTransform.Rotate.Z, 0, 0, 1);
		MatrixState.scale(finalTransform.Scale.X, finalTransform.Scale.Y, finalTransform.Scale.Z);

	}

	@Override
	public void onViewTransform(Transform finalTransform, float[] gyroscopeMatrix) {
		
//		float translateX = finalTransform.Position.X;
//		float translateY = finalTransform.Position.Y;
//		float translateZ = finalTransform.Position.Z;
//
//		float scaleX = finalTransform.Scale.X;
//		float scaleY = finalTransform.Scale.Y;
//		float scaleZ = finalTransform.Scale.Z;



		MatrixState.alpha(finalTransform.Alpha);
		MatrixState.translate(finalTransform.Position.X,finalTransform.Position.Y,finalTransform.Position.Z);



		MatrixState.rotate(finalTransform.Rotate.X, 1, 0, 0);
		MatrixState.rotate(finalTransform.Rotate.Y, 0, 1, 0);
		MatrixState.rotate(finalTransform.Rotate.Z, 0, 0, 1);

		// luoyouren: 使用陀螺仪的旋转数据
		MatrixState.updateEyeMatrixToScene(gyroscopeMatrix);

		MatrixState.scale(finalTransform.Scale.X, finalTransform.Scale.Y, finalTransform.Scale.Z);



	}



	@Override
	public void onViewTransformAlpha(Transform finalTransform) {

		MatrixState.alpha(finalTransform.Alpha);

	}
	@Override
	public void onViewTransformTranslate(Transform finalTransform) {

		MatrixState.translate(finalTransform.Position.X,finalTransform.Position.Y,finalTransform.Position.Z);

	}
	@Override
	public void onViewTransformRotate(Transform finalTransform) {


		MatrixState.rotate(finalTransform.Rotate.X, 1, 0, 0);
		MatrixState.rotate(finalTransform.Rotate.Y, 0, 1, 0);
		MatrixState.rotate(finalTransform.Rotate.Z, 0, 0, 1);
	}
	@Override
	public void onViewTransformScale(Transform finalTransform) {


		MatrixState.scale(finalTransform.Scale.X, finalTransform.Scale.Y, finalTransform.Scale.Z);
	}

	@Override
	public void onDecode(Transform finalTransform) {

//		float translateX = finalTransform.Position.X;
//		float translateY = finalTransform.Position.Y;
//		float translateZ = finalTransform.Position.Z;
		
		float scaleX = finalTransform.Scale.X;
		float scaleY = finalTransform.Scale.Y;
		float scaleZ = finalTransform.Scale.Z;
		MatrixState.scale(scaleX, scaleY, scaleZ);
//		MatrixState.translate(translateX,translateY,translateZ);
	}
	@Override
	public void cameraSetUp(int left,int top ,float width, float height, float fovy, float near, float far) {

		GLES20.glViewport(left, top, (int) width, (int) height);
		float ratio = width / height;
		// 初始化变换矩阵
		MatrixState.setInitStack();
		//
		// 调用此方法计算产生透视投影矩阵
//		MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, near, far);
		MatrixState.setProjectFrustum(-ratio/4, +ratio/4, -1f/4, 1f/4, near, far);
		// MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 0.01f, 100);
	}

	@Override
	public void cameraViewMatrix(float eyeX, float eyeY, float eyeZ, float lookX, float lookY, float lookZ, float upX, float upY, float upZ) {

		// 调用此方法产生摄像机9参数位置矩阵
		MatrixState.setCamera(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
	}

	@Override
	public Texture generateTexture(Bitmap bmp) { 
		return geTexture(bmp,true);
	}
	@Override
	public Texture generateTexture(Bitmap bmp,boolean recycleBitmap) { 

		return geTexture(bmp,recycleBitmap);
	}

	//luoyouren: 生成纹理
	private Texture geTexture(Bitmap bmp,boolean recycleLast ){

//		Log.d("debug","generateTexture" );
		// 生成纹理ID
		Texture texture = new Texture();
		int[] textureID = new int[1];
		GLES20.glGenTextures(1, // 产生的纹理id的数量
				textureID, // 纹理id的数组
				0 // 偏移量
		);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
//		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
//		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
 
//		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
//		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
//		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
//		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

		// 实际加载纹理
//		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, // 纹理类型，在OpenGL
//													// ES中必须为GL10.GL_TEXTURE_2D
//				0, // 纹理的层次，0表示基本图像层，可以理解为直接贴图
//				bmp, // 纹理图像
//				0 // 纹理边框尺寸
//		);
		
		// 输出像素
//		int   x = 0, y = 0; 
//		int   width     = bmp.getWidth(); 
//		int   height    = bmp.getHeight();
//		int   byteIndex = 0; 
//		int[] pixels    = new int[width * height]; 
//		bmp.getPixels(pixels, 0, width, x, y, width, height);
//		
//	    byte[] pixelComponents = new byte[pixels.length * 4]; 
//	    for (int i = 0; i < pixels.length; i++) { 
//	        int pixel = pixels[i]; 
//	        pixelComponents[byteIndex++] = (byte) ((pixel >> 16) & 0xFF); // red 
//			pixelComponents[byteIndex++] = (byte) ((pixel >> 8) & 0xFF);  // green 
//			pixelComponents[byteIndex++] = (byte) ((pixel) & 0xFF);       // blue 
//			pixelComponents[byteIndex++] = (byte) ((pixel >> 24) );          // alpha 
//		} 
//		ByteBuffer pixelBuffer = ByteBuffer.wrap(pixelComponents); 
//		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp.getWidth(), bmp.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
//		GLES20.glDisable(GLES20.GL_TEXTURE_2D);
//		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

//		Log.d("debug","GLUtils.getInternalFormat(bmp) = "+GLUtils.getInternalFormat(bmp));
//		Log.d("debug","GLUtils.getType(bmp) = "+GLUtils.getType(bmp));
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		GLUtils.texImage2D
	    (
	    		GLES20.GL_TEXTURE_2D, //纹理类型
	     		0, 
//	     		GLUtils.getInternalFormat(bmp),
	     		bmp, //纹理图像
//	     		GLUtils.getType(bmp), 
	     		0 //纹理边框尺寸
	     );   
//        //自动生成Mipmap纹理
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

		texture.setWidth(width);
		texture.setHeight(height);
		texture.setTextureID(textureID[0]);
		if(recycleLast){
			bmp.recycle(); // 纹理加载成功后释放图片
		}
		return texture;

	
	}

	@Override
	public Texture generateTextureOldVersion(Bitmap bmp) {
		return generateTexture(bmp);
	}

	@Override
	public Texture generateVideoTexture() {
		// 生成纹理ID
		Texture texture = new Texture();
		int[] textureID = new int[1];
		GLES20.glGenTextures(1, // 产生的纹理id的数量
				textureID, // 纹理id的数组
				0 // 偏移量
		);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		// 实际加载纹理

		texture.setTextureID(textureID[0]);
		return texture;

	}

	@Override
	public void glDeleteTextures(int[] textureIDs) {

		GLES20.glDeleteTextures(textureIDs.length, textureIDs, 0);
	}

	@Override
	public void onDrawableTransform(Transform transform ) {
		
//		AngleAxis angleAxis = transform.Rotate.Quaternion.toAngleAxis();
//		float translateX = transform.Position.X;
//		float translateY = transform.Position.Y;
//		float translateZ = transform.Position.Z;
//		
//		float scaleX = transform.Scale.X;
//		float scaleY = transform.Scale.Y;
//		float scaleZ = transform.Scale.Z;
//
//		MatrixState.translate(translateX,translateY,translateZ);
//		MatrixState.rotate(Radian.toDegree(angleAxis.Angle), angleAxis.xAxis, angleAxis.yAxis, angleAxis.zAxis);
//		MatrixState.scale(scaleX, scaleY, scaleZ);


//		MatrixState.setFinalModeMatrix(transform.mFinalMatrix)  ;

		MatrixState.alpha(transform.Alpha);
		MatrixState.translate(transform.Position.X,transform.Position.Y,transform.Position.Z);
		MatrixState.rotate(transform.Rotate.X, 1, 0, 0);
		MatrixState.rotate(transform.Rotate.Y, 0, 1, 0);
		MatrixState.rotate(transform.Rotate.Z, 0, 0, 1);
		MatrixState.scale(transform.Scale.X, transform.Scale.Y, transform.Scale.Z);

	}


	@Override
	public void openScissorFrame(int left,int top ,int width,int height) {
		
		GLES20.glEnable(GL10.GL_SCISSOR_TEST);
		// 设置区域
		GLES20.glScissor(left,top, width, height);

		// 设置屏幕背景色RGBA
//		GLES20.glClearColor(0.1f, 0.3f, 0.1f, 1.0f);
		// 清除颜色缓存于深度缓存
//		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void closeScissorTest() {

		GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
	}

	@Override
	public void openCullFace(){
		GLES20.glEnable(GLES20.GL_CULL_FACE);
	};
	@Override
	public void openCullFrontFace(){
		GLES20.glCullFace(GLES20.GL_FRONT);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
	};
	@Override
	public void openCullBackFace(){
		GLES20.glCullFace(GLES20.GL_BACK);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
	};
	
	@Override
	public void closeCullFace(){

		GLES20.glDisable(GLES20.GL_CULL_FACE);
	};
	
	@Override
	public void openDepthTest() {

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}
	
	@Override
	public void closeDepthTest() {

		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
	}

	@Override
	public void openStencilTest() {

		GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT);
		GLES20.glEnable(GLES20.GL_STENCIL_TEST);
	}
	
	@Override
	public void sceneStencil(int i) {

		/**
		 * 参数位置说明
		 * 位置1.通过条件，    
		 * 
		 * 即：如下八种取值
		 * 	public static final int GL_NEVER                                   = 0x0200;
		    public static final int GL_LESS                                    = 0x0201;
		    public static final int GL_EQUAL                                   = 0x0202;
		    public static final int GL_LEQUAL                                  = 0x0203;
		    public static final int GL_GREATER                                 = 0x0204;
		    public static final int GL_NOTEQUAL                                = 0x0205;
		    public static final int GL_GEQUAL                                  = 0x0206;
		    public static final int GL_ALWAYS                                  = 0x0207;
		      始终通过、始终不通过、大于则通过、小于则通过、大于等于则通过、小于等于则通过、等于则通过、不等于则通过。
		      
		 *    
		 * 位置2.取值
		 * 位置3.掩码mask  如果是八位就是0xff，，参数的意义为：如果进行比较，则只比较mask中二进制为1的位
		 * 
		 * --------------
		 * 这个函数是指定模板测试的条件
		 */
		GLES20.glStencilFunc(GLES20.GL_ALWAYS, i, 0xff);
		/**
		 * 0。

			void glStencilOp(GLenum  sfail, GLenum  dpfail, GLenum  dppass);
			这个函数指定了对模板值的更新操作。这三个参数所接受的枚举值都是同一组：GL_KEEP,GL_ZERO,GL_REPLACE,GL_INCR,GL_INCR_WRAP,GL_DECR,GL_DECR_WRAP, GL_INVERT。
			sfail说明当模板测试失败后，用何种方式更新模板值；dpfail说明当模板测试成功，但深度测试失败后用何种方式更新模板值；dppass说明当模板测试与深度测试均成功后用何种方式更新模板值。
			GL_ZERO表示用0来更新模板值；GL_KEEP说明保留原来的模板值；GL_REPLACE表明用参考值替代原来的模板值。
		 */
		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);

	}
	@Override
	public void sceneChildStencil(int i) {

		GLES20.glStencilFunc(GLES20.GL_EQUAL, i, 0xff);
		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
		
	}

	@Override
	public  void viewGroupStencilRecursion(int i) {
//		IntBuffer ib =  IntBuffer.allocate(1);
//		GLES20.glGetIntegerv(GLES20.GL_STENCIL_REF, ib);
//		Log.d("ming", "viewGroupStencilRecursion before = "+i + ",,,ib.get() = "+ib.get());
		GLES20.glStencilFunc(GLES20.GL_EQUAL, i-1, 0xff);
		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_INCR);

		
	};

	@Override
	public  void viewGroupChildStencilRecursion(int i){

		GLES20.glStencilFunc(GLES20.GL_EQUAL,i, 0xff);
		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_INCR);
	} ;

//	@Override
//	public  void viewGroupStencilRecursion(int i) {
//
//		GLES20.glStencilFunc(GLES20.GL_EQUAL, i, 0xff);
//		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_INCR);
////		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_INCR, GLES20.GL_INCR);
//	};
//
//	@Override
//	public  void viewGroupChildStencilRecursion(int i){
//
//		GLES20.glStencilFunc(GLES20.GL_EQUAL, i+1, 0xff);
//		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
//	} ;
//	
	
	@Override
	public void closeStencilTest() {

		GLES20.glDisable(GLES20.GL_STENCIL_TEST);
		GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT);
	}
	@Override
	public Texture generateOES_Texture( ) {
 
		int[] textures = new int[1];
        // Generate the actual texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        
//        Bitmap bmp = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
//        bmp.eraseColor(Color.RED);
//    	int width = bmp.getWidth();
//		int height = bmp.getHeight();
//		GLUtils.texImage2D
//	    (
//	    		GLES20.GL_TEXTURE_2D, //纹理类型
//	     		0, 
////	     		GLUtils.getInternalFormat(bmp),
//	     		bmp, //纹理图像
////	     		GLUtils.getType(bmp), 
//	     		0 //纹理边框尺寸
//	     );   
////        //自动生成Mipmap纹理
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        
        Texture texture = new Texture();
//		texture.setWidth(width);
//		texture.setHeight(height);
        texture.setTextureID(textures[0]);
        texture.setBindTarget(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
        
        return texture;
	}
}
