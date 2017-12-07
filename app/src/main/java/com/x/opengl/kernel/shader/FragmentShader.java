package com.x.opengl.kernel.shader;

import android.annotation.SuppressLint;
import android.opengl.GLES20;

/**
 * 片段着色器
 * @date   
 */
public class FragmentShader extends Shader{
	
	public static final String TAG = "FragmentShader";
	
	protected final static int mShaderType = GLES20.GL_FRAGMENT_SHADER;

	public FragmentShader() {
		
	}

	@SuppressLint("NewApi") @Override
	public boolean compileShader() {
		int[]   compileStatus = new int[1];
		boolean compileResult = false;
		
		// 获取着色器ID
		mShaderID = GLES20.glCreateShader(mShaderType);
		if (mShaderID != GLES20.GL_FALSE && mShaderSource != null && !mShaderSource.isEmpty()) {
			// 设置着色器源码
			GLES20.glShaderSource(mShaderID, mShaderSource);
			// 编译着色器
			GLES20.glCompileShader(mShaderID);
			// 检查编译情况
			GLES20.glGetShaderiv(mShaderID, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
			// 获取编译日志
			mShaderLog = GLES20.glGetShaderInfoLog(mShaderID);
			// 判断编译结果
			if (compileStatus[0] != Shader.VALID_SHADER) {
				compileResult = true;
			}else{
				compileResult = false;
				// 删除当前着色器
				GLES20.glDeleteShader(mShaderID);
				mShaderID = VALID_SHADER;
			}
		}else{
			compileResult = false;
		}
		
		mIsCompiled = compileResult;
		return compileResult;
	}
	
}
