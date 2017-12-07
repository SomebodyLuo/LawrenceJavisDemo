package com.x.opengl.gles;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.Mesh;
import com.x.opengl.kernel.T_AABBBox;
import com.x.opengl.kernel.Texture;
import com.x.opengl.kernel.Transform;

public abstract class GLES {

	public abstract void initGLSystem(GL10 gl);

	public abstract void resetFrame(GL10 gl) ;

	public abstract void pushMatrix() ;

	public abstract void popMatrix() ;

	public abstract void getViewportMatrix(T_AABBBox aabbBox);
	public abstract void getProjectionMatrix(T_AABBBox aabbBox) ;
	public abstract void getModelMatrix(T_AABBBox aabbBox) ;
	public abstract void getViewMatrix(T_AABBBox aabbBox) ;
	

	
	public abstract void onRender(Mesh mesh, Material mMaterial,  float[] mSelfModelViewMatrix, boolean baseShader) ;

	public abstract void onViewTransform(Transform mFinalTransform) ;
	public abstract void onViewTransformAlpha(Transform mFinalTransform) ;
	public abstract void onViewTransformRotate(Transform mFinalTransform) ;
	public abstract void onViewTransformTranslate(Transform mFinalTransform) ;
	public abstract void onViewTransformScale(Transform mFinalTransform) ;
	
	public abstract void onDrawableTransform(Transform transform ) ;

	public abstract void cameraSetUp(int left,int top,float width, float height, float fovy, float near, float far) ;

	public abstract void cameraViewMatrix(float eyeX, float eyeY, float eyeZ, float lookX, float lookY, float lookZ, float upX, float upY, float upZ) ;

	public abstract Texture generateTexture(Bitmap bmp) ;
	
	public abstract Texture generateTexture(Bitmap bmp,boolean recycle) ;

	public abstract Texture generateTextureOldVersion(Bitmap bmp) ;

	public abstract Texture generateVideoTexture() ;

	public abstract void glDeleteTextures(int[] textureIDs) ;

	public abstract void openScissorFrame(int left, int top, int width, int height);
	
	public abstract void closeScissorTest();

	public abstract void closeDepthTest() ;

	public abstract void openDepthTest() ;

	public abstract void openStencilTest() ;

	public abstract void openCullFace() ;
	
	public abstract void openCullBackFace() ;
	
	public abstract void openCullFrontFace() ;
	
	public abstract void closeCullFace() ;
	
	public abstract void sceneStencil(int i) ;
	
	public abstract void sceneChildStencil(int i) ;

	public abstract void closeStencilTest() ;

	public abstract void viewGroupStencilRecursion(int i) ;
	
	public abstract void viewGroupChildStencilRecursion(int i) ;

	public abstract void onDecode(Transform mTransform) ;

	public abstract Bitmap getSnapshot(int width, int height) ;

	public abstract Texture generateOES_Texture( ) ;

	public abstract void getCameraLookVector(T_AABBBox t_AABBBox) ;

		
}
