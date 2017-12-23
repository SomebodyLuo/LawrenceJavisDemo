package com.x.opengl.gles;
import java.nio.ByteBuffer;

import android.annotation.SuppressLint;
import android.opengl.Matrix;
import android.util.Log;

import com.x.opengl.kernel.T_AABBBox;
import com.x.opengl.kernel.Vector3;

@SuppressLint("NewApi") 
public class MatrixState 
{  
	private static float[] mProjMatrix = new float[16];
    private static float[] mCameraMatrix = new float[16];   
    private static float[] mModelMatrix=new float[16];

    
    private static float[] mModelViewMatrix=new float[16];
    private static float[] m_M_V_P_Matrix=new float[16];
    private static float[][] mStack =new float[10][16];
    private static float[][] mStackAlpha =new float[10][1];
    private  static int stackTop=-1;
	private static int[] mViewportMatrix = new int[4];

	private static float[] 	mCameraLookVector = new float[6];
	private static float 	mAlpha = 1;
    public static void setInitStack()
    {

    	Matrix.setIdentityM(mModelMatrix, 0);
    	Matrix.setRotateM(mModelMatrix, 0, 0, 1, 0, 0);



    	mAlpha = 1;
    }
    
    public static void pushMatrix()
    {
    	stackTop++;
    	for(int i=0;i<16;i++)
    	{
    		mStack[stackTop][i]=mModelMatrix[i];
    	}
    	mStackAlpha[stackTop][0] = mAlpha;
    }
    
    public static void popMatrix()
    {
    	for(int i=0;i<16;i++)
    	{
    		mModelMatrix[i]=mStack[stackTop][i];
    	}
    	mAlpha  = mStackAlpha[stackTop][0];
    	stackTop--;
    }

	private static final float[] mSourceFrontward_four = new float[ ]{Vector3.FRONT_AXIS.X, Vector3.FRONT_AXIS.Y, Vector3.FRONT_AXIS.Z, 0};

	// luoyouren: 让某些场景跟随视线移动
	public static void updateEyeMatrixToScene(float[] gyroscopeMatrix)
	{
		float[] tmpMatrix = new float[16];
		Matrix.setIdentityM(tmpMatrix, 0);

		Matrix.invertM(tmpMatrix, 0, gyroscopeMatrix, 0);

		tmpMatrix[4] = 0.0f;
		tmpMatrix[5] = 1.0f;
		tmpMatrix[6] = 0.0f;

//		float[] mFrontward_four = new float[ ]{Vector3.FRONT_AXIS.X, Vector3.FRONT_AXIS.Y, Vector3.FRONT_AXIS.Z, 0};
//
//		Matrix.multiplyMV(mFrontward_four, 0, tmpMatrix, 0, mSourceFrontward_four, 0);

		Matrix.multiplyMM(mModelMatrix, 0, tmpMatrix, 0, mModelMatrix.clone(), 0);
	}
    
    public static void translate(float x,float y,float z)
    {
    	Matrix.translateM(mModelMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)
    {
    	Matrix.rotateM(mModelMatrix,0,angle,x,y,z);
    }

    public static void scale(float x,float y,float z)
    {
    	Matrix.scaleM(mModelMatrix,0, x, y, z);
    }

	public static void alpha(float alpha) {
		if(alpha > 1){
			alpha = 1;
		}
		if(alpha < 0){
			alpha = 0;
		}
		mAlpha *= alpha;
	}
	
	public static void setFinalModeMatrix(float[] finalMatrix) {
//		mFinalModeMatrix =  finalMatrix ;
	}
    
    static ByteBuffer llbb= ByteBuffer.allocateDirect(3*4);
    static float[] cameraLocation=new float[3];


   
	/**
	 * 视口矩阵
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 */
	public static void setViewPort(int left, int top, int width, int height) {
		mViewportMatrix [0]= left;
		mViewportMatrix [1]= top;
		mViewportMatrix [2]= width;
		mViewportMatrix [3]= height;
	}
    /**
     * 透视投影矩阵
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public static void setProjectFrustum
    ( 
    	float left,		 
    	float right,     
    	float bottom,    
    	float top,      
    	float near,		 
    	float far       
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }
    /**
     * 正交投影矩阵
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public static void setProjectOrtho
    (
    	float left,		//near���left
    	float right,    //near���right
    	float bottom,   //near���bottom
    	float top,      //near���top
    	float near,		//near�����
    	float far       //far�����
    )
    {    	
    	Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }
    /**
     *  视图矩阵
     * @param cx
     * @param cy
     * @param cz
     * @param tx
     * @param ty
     * @param tz
     * @param upx
     * @param upy
     * @param upz
     */
    public static void setCamera
    (
    		float cx,	 
    		float cy,    
    		float cz,    
    		float tx,    
    		float ty,    
    		float tz,    
    		float upx,   
    		float upy,   
    		float upz    		
    )
    {
        	Matrix.setLookAtM
            (
            		mCameraMatrix, 
            		0, 
            		cx,
            		cy,
            		cz,
            		tx,
            		ty,
            		tz,
            		upx,
            		upy,
            		upz
            );

        	mCameraLookVector[0] = cx;
        	mCameraLookVector[1] = cy;
        	mCameraLookVector[2] = cz;
        	
            float fx = tx - cx;
            float fy = ty - cy;
            float fz = tz - cz;
            float rlf = 1.0f / Matrix.length(fx, fy, fz);
            fx *= rlf;
            fy *= rlf;
            fz *= rlf;
        	mCameraLookVector[0+3] = fx;
        	mCameraLookVector[1+3] = fy;
        	mCameraLookVector[2+3] = fz;
        	
    }


    // luoyouren: 或者View的MVP矩阵
    public static float[] getFinalMatrix(float[] selfModelMatrix)
    {	

		if(selfModelMatrix != null){
    		
			Matrix.multiplyMM(mModelViewMatrix, 0, mCameraMatrix, 0, selfModelMatrix, 	0);
			Matrix.multiplyMM(m_M_V_P_Matrix, 0, mProjMatrix, 0, mModelViewMatrix, 0);
			
    	}else{
    		
    		Matrix.multiplyMM(mModelViewMatrix, 0, mCameraMatrix, 0, mModelMatrix, 	0);
    		Matrix.multiplyMM(m_M_V_P_Matrix, 0, mProjMatrix, 	0, mModelViewMatrix, 	0);

    	}
        
        return m_M_V_P_Matrix;
    } 

    public static float getFinalAlpha(){
    	return mAlpha;
    }
//    public static float[] getFinalMatrix(float[] selfModelViewMatrix)
//    {	
//
//    	if(selfModelViewMatrix == null){
//    		
//    		Matrix.multiplyMM(m_M_V_P_Matrix, 0, mCameraMatrix, 0, mModelMatrix, 0);
//    		Matrix.multiplyMM(m_M_V_P_Matrix, 0, mProjMatrix, 0, m_M_V_P_Matrix, 0);
//    	}else{
//
//    		Matrix.multiplyMM(m_M_V_P_Matrix, 0, mProjMatrix, 0, selfModelViewMatrix, 0);
//    	}
//        
//        return m_M_V_P_Matrix;
//    } 
    public static float[] getMMatrix()
    {       
        return mModelMatrix;
    }

	public static void getViewportMatrix(T_AABBBox aabbBox) {
		aabbBox.callbackUpdateViewportMatrix(mViewportMatrix.clone());
	}

	public static void getProjectionMatrix(T_AABBBox aabbBox) {
		aabbBox.callbackUpdateProjectionMatrix(mProjMatrix.clone());
	}

	public static void getViewMatrix(T_AABBBox aabbBox ){
    	aabbBox.callbackUpdateViewMatrix(mCameraMatrix.clone());
	};
	public static void getModelMatrix(T_AABBBox aabbBox ) {
    	aabbBox.callbackUpdateModelMatrix(mModelMatrix.clone());
	}
	public static void getCameraLookVector(T_AABBBox aabbBox) {
		aabbBox.callbackUpateLookVector(mCameraLookVector.clone());
	}

	private static void output(String string, float[] matrix) {
		
		String matrixString =string + "\n";
		for (int i = 0; i < matrix.length; i++) {
			if(i%4 == 0){
				matrixString += "\n";
			}
			matrixString +="["+matrix[i]+"]"+"\t,";
		}
		Log.d("ming", "debug " +matrixString);
	}

	private static void output(String string, int[] matrix) {
		
		String matrixString =string + "\n";
		for (int i = 0; i < matrix.length; i++) {
			if(i%4 == 0){
				matrixString += "\n";
			}
			matrixString +="["+matrix[i]+"]"+"\t,";
		}
		Log.d("ming", "debug " +matrixString);
	}




}
