package com.x.opengl.kernel.shader;

import java.util.HashMap;

import android.opengl.GLES20;

/**
 * 着色器
 * @date   2014-02-18 15:47:54
 */
public abstract class Shader {

	public    static final int                              VALID_SHADER            = 0;
	public    static final int                              VERTEX_SHADER           = GLES20.GL_VERTEX_SHADER;
	public    static final int                              FRAGMENT_SHADER         = GLES20.GL_FRAGMENT_SHADER;
	
	protected              int                              mShaderID               = VALID_SHADER;
	protected              String                           mShaderSource           = null;
	protected              String                           mShaderLog              = null;
	protected              boolean                          mIsCompiled             = false;
	protected              HashMap<String, ShaderParameter> mUniformParameterList   = null;
	protected              HashMap<String, ShaderParameter> mAttributeParameterList = null;
	protected              HashMap<String, ShaderParameter> mVaryingParameterList   = null;
	
	public Shader() {
		
	}
	
	/**
	 * 加载一个着色器文件
	 * @param shaderFile
	 * @return
	 */
	@Deprecated // Unfinished
	public static Shader loadShader(String shaderFile) {
		return null;
	}
	
	/**
	 * 设置着色器源码
	 * @param source
	 */
	public void setShaderSource(String source) {
		mShaderSource = source;
	}
	
	/**
	 * 过滤着色器变量
	 */
	@Deprecated // Unfinished
	private void filterShaderParameters() {
		if (mShaderSource != null && !mShaderSource.isEmpty()) {
			// 过滤Uniform变量
			
			// 过滤Attribut变量
			
		}
	}
	
	/**
	 * 自动提取着色器变量
	 * @return
	 */
	@Deprecated
	public Shader autoExtractShaderParameters() {
		filterShaderParameters();
		return this;
	}
	
	/**
	 * 手动提取着色器变量
	 * @param paramType UNIFORM_PARAMETER, ATTRIBUTE_PARAMETER, VARYING_PARAMETER
	 * @param paramName
	 * @return
	 */
	public Shader manualExtractShaderParameters(int paramType, String paramName) {
		ShaderParameter parameter = new ShaderParameter();
		
		switch (paramType) {
		case ShaderParameter.UNIFORM_PARAMETER:
		{
			// 初始化变量
			parameter.setParameterType(ShaderParameter.UNIFORM_PARAMETER);
			parameter.setParameterName(paramName);
			
			// 添加到变量列表
			if (!mUniformParameterList.containsKey(paramName)) {
				mUniformParameterList.put(paramName, parameter);
			}
		}
			break;

		case ShaderParameter.ATTRIBUTE_PARAMETER:
		{
			// 初始化变量
			parameter.setParameterType(ShaderParameter.ATTRIBUTE_PARAMETER);
			parameter.setParameterName(paramName);
			
			// 添加到变量列表
			if (!mAttributeParameterList.containsKey(paramName)) {
				mAttributeParameterList.put(paramName, parameter);
			}
		}
			break;
			
		case ShaderParameter.VARYING_PARAMETER:
		{
			// 初始化变量
			parameter.setParameterType(ShaderParameter.VARYING_PARAMETER);
			parameter.setParameterName(paramName);
			
			// 添加到变量列表
			if (!mVaryingParameterList.containsKey(paramName)) {
				mVaryingParameterList.put(paramName, parameter);
			}
		}
			break;
			
		default:
			break;
		}
		
		
		
		return this;
	}
	
	/**
	 * 获取Uniform着色器变量列表
	 * @return
	 */
	public HashMap<String, ShaderParameter> getUniformParameterList() {
		return mUniformParameterList;
	}
	
	/**
	 * 获取Attribute着色器变量列表
	 * @return
	 */
	public HashMap<String, ShaderParameter> getAttributeParameterList() {
		return mAttributeParameterList;
	}
	
	/**
	 * 获取Varying着色器变量列表
	 * @return
	 */
	public HashMap<String, ShaderParameter> getVaryingParameterList() {
		return mVaryingParameterList;
	}
	
	/**
	 * 编译着色器
	 */
	public abstract boolean compileShader();
	
	/**
	 * 销毁着色器
	 */
	public void destoryShader() {
		if (mShaderID != VALID_SHADER && GLES20.glIsShader(mShaderID)) {
			GLES20.glDeleteShader(mShaderID);
			mShaderID = VALID_SHADER;
			mIsCompiled = false;
		}
	}
	
	/**
	 * 获取着色器源码编译日志
	 */
	public String getShaderLog() {
		return mShaderLog;
	}
	
	/**
	 * 获取着色器ID
	 * @return
	 */
	public int getShaderID() {
		return mShaderID;
	}

	/**
	 * 获取着色器源码
	 * @return
	 */
	public String getShaderSource() {
		return mShaderSource;
	}

	/**
	 * 着色器是否已经编译
	 * @return
	 */
	public boolean isCompiled() {
		return mIsCompiled;
	}
}
