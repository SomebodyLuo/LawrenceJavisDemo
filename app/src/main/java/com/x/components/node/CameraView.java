package com.x.components.node;

import android.opengl.Matrix;
import android.util.Log;

import com.x.Director;
import com.x.opengl.kernel.Quat;
import com.x.opengl.kernel.Vector3;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 *
 * CameraView 除了具有普通view所具有的特征之外，还需要三个方向，确切的说，应该是所有view都应该具有三个方向
 * 即面向的向量，--
 *  头顶的向量，
 *  以及右侧的向量，
 *  
 *  由以上三个向量可以推出背对，脚底的，左侧的三个向量
 *  
 *  当对cameraView进行一些基本的属性变换（位移，缩放，旋转）时，同时需要更改view的 前方向，上方向，和右方向
 *  目前暂时只对cameraView进行如上处理，对普通view暂时不做
 * @author LightSnail
 *
 */
public class CameraView extends View{

	
	private final float[] mSourceFrontward_four = new float[ ]{Vector3.FRONT_AXIS.X, Vector3.FRONT_AXIS.Y, Vector3.FRONT_AXIS.Z, 0};

	private final float[] mSourceUpward_four = new float[ ]{Vector3.UP_AXIS.X,Vector3.UP_AXIS.Y,Vector3.UP_AXIS.Z,0};

	private final float[] mSourceRightward_four = new float[ ]{Vector3.RIGHT_AXIS.X,Vector3.RIGHT_AXIS.Y,Vector3.RIGHT_AXIS.Z,0};
	
	public float[] mFrontward_four = new float[ ]{Vector3.FRONT_AXIS.X,Vector3.FRONT_AXIS.Y,Vector3.FRONT_AXIS.Z,0};
	public float[] mUpward_four = new float[ ]{Vector3.UP_AXIS.X,Vector3.UP_AXIS.Y,Vector3.UP_AXIS.Z,0}; 
	public float[] mRightward_four = new float[ ]{Vector3.RIGHT_AXIS.X,Vector3.RIGHT_AXIS.Y,Vector3.RIGHT_AXIS.Z,0};

	private float[] mMatrixRotate = new float[16];
	private Vector3 LookVector = Vector3.BACK_AXIS.clone();
	private float[] mGyroscopeMatrix = new float[16];
	private float[] mFinalMatrix = new float[16];
	
	public CameraView(){
		super();
		Matrix.setIdentityM(mGyroscopeMatrix, 0);
		Matrix.setIdentityM(mMatrixRotate, 0);
	}
	
	private Vector3 rotate(Vector3 source,Quat operaQuat){
		
		Quat qSoure = new Quat(0,source.X,source.Y,source.Z);//生成原始的相机up方向的四元数
		Quat qCenter = Quat.product(operaQuat, qSoure); //左乘四元操作数
		Quat qFinal = Quat.product(qCenter,Quat.inverse(operaQuat) );//右乘以其共轭
		return new Vector3(qFinal.X,qFinal.Y,qFinal.Z);
	}

	@Override
	public void setRotate(float x, float y, float z) {
		super.setRotate(x, y, z);
		Matrix.setRotateM(mMatrixRotate, 0, x, 1, 0, 0);
		Matrix.rotateM(mMatrixRotate, 0, y, 0, 1, 0);
		Matrix.rotateM(mMatrixRotate, 0, z, 0, 0, 1);

	}

	@Override
	public void rotateTo(float x, float y, float z) {
		// TODO Auto-generated method stub
		super.rotateTo(x, y, z);
		Matrix.setRotateM(mMatrixRotate, 0, x, 1, 0, 0);
		Matrix.rotateM(mMatrixRotate, 0, y, 0, 1, 0);
		Matrix.rotateM(mMatrixRotate, 0, z, 0, 0, 1);
		
	}

	/**
	 * 设置停在代理view的位置的camera视图，这里的代理view位置可能改变，但视线方向不变
	 */
	public void updateUiViewMatrix() {
		
		Director.sGLESVersion.cameraViewMatrix(
				mTransform.Position.X, mTransform.Position.Y,  mTransform.Position.Z , 
				mTransform.Position.X+ (-mSourceFrontward_four[0]), mTransform.Position.Y+ -mSourceFrontward_four[1],  mTransform.Position.Z+ -mSourceFrontward_four[2] , 
				mSourceUpward_four[0] ,mSourceUpward_four[1],mSourceUpward_four[2]);
		
	}	
	/**
	 * 设置停在代理view的位置的camera视图，这里的代理view位置可能改变，但视线方向不变
	 */
	public void updateUiGraivityViewMatrix() {
		
		Director.sGLESVersion.cameraViewMatrix(
				mTransform.Position.X, mTransform.Position.Y,  mTransform.Position.Z , 
				mTransform.Position.X+ (-mSourceFrontward_four[0]), mTransform.Position.Y+ -mSourceFrontward_four[1],  mTransform.Position.Z+ -mSourceFrontward_four[2] , 
				mUpward_four[0] ,mUpward_four[1],mUpward_four[2]);
		
	}	
	
	/**
	 * 设置停在代理view的位置的camera视图，这里代理的位置，视线都在变化
	 */
	public void updateWorldViewMatrix() {
		Matrix.multiplyMM(mFinalMatrix, 0, mGyroscopeMatrix, 0, mMatrixRotate, 0);
		fixDirection(mFinalMatrix); 
		
		Director.sGLESVersion.cameraViewMatrix(
				mTransform.Position.X, 						mTransform.Position.Y,  						mTransform.Position.Z,
				mTransform.Position.X+ (-mFrontward_four[0]), 	mTransform.Position.Y+ -mFrontward_four[1], 	mTransform.Position.Z+ -mFrontward_four[2] , 
				mUpward_four[0], mUpward_four[1], mUpward_four[2]);
		
		
	}	

	/**
	 * 注意该方法不要被多次调用
	 * @param matrix
	 */
	private void fixDirection(float[] matrix) {

		Matrix.multiplyMV(mFrontward_four, 0, matrix, 0, mSourceFrontward_four, 0);
		Matrix.multiplyMV(mUpward_four, 0, matrix, 0, mSourceUpward_four, 0);
		Matrix.multiplyMV(mRightward_four, 0, matrix, 0, mSourceRightward_four, 0);		
	}

	// luoyouren: 传感器的数据进来，给到Camera
	public void setGyroscopeMatrix(float[] matrix) {
		Log.i("luoyouren", "setGyroscopeMatrix 1: " + Arrays.toString(matrix));
		Matrix.invertM(mGyroscopeMatrix, 0, matrix, 0);
		Log.i("luoyouren", "setGyroscopeMatrix 2: " + Arrays.toString(mGyroscopeMatrix));
	}

	public float[] getEyePosition() {


		float[] resultMatrix = new float[4];
		float[]  matrix = getFinalModelMatrix();
		Matrix.multiplyMV(resultMatrix, 0, matrix, 0, new float[]{0,0,0,1}, 0);
		Matrix.multiplyMV(resultMatrix, 0, mGyroscopeMatrix, 0, resultMatrix, 0);
		
		float[] resultposition = new float[3];
		resultposition[0] = resultMatrix[0];
		resultposition[1] = resultMatrix[1];
		resultposition[2] = resultMatrix[2];
		return resultposition;
	}
	

	
}




