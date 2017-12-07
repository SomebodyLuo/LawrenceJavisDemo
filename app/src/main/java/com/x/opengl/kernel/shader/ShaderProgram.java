package com.x.opengl.kernel.shader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import android.opengl.GLES20;

/**
 * 着色器程序
 * @date   2014-02-18 15:06:43
 */
public class ShaderProgram {

	public static final int VALID_SHADER_PROGRAM = 0;
	
	private LinkedList<VertexShader>         mVertexShaders    = null; // 顶点着色器组
	private LinkedList<FragmentShader>       mFragmentShaders  = null; // 片段着色器组
	private HashMap<String, ShaderParameter> mParametersList   = null; // 着色器参数组
	private int                              mShaderProgramID  = VALID_SHADER_PROGRAM;
	private String                           mShaderProgramLog = null;
	private boolean                          mIsCompiled       = false;
	
	/**
	 * 着色器参数过多异常
	 * @date   2014-02-24 13:58:49
	 */
	public class ShaderParameterCountInvalidException extends Exception {
		private static final long serialVersionUID = 3213995928030066764L;
		
		public ShaderParameterCountInvalidException() {
			
		}
		
		public ShaderParameterCountInvalidException(String msg) {
			super(msg);
		}
	}
	
	public ShaderProgram() {
		mVertexShaders   = new LinkedList<VertexShader>();
		mFragmentShaders = new LinkedList<FragmentShader>();
		mParametersList  = new HashMap<String, ShaderParameter>();
	}
	
	/**
	 * 加载着色器程序文件
	 * @param shaderProgramFile
	 * @return
	 */
	@Deprecated // Unfinished
	public static ShaderProgram loadShaderProgram(String shaderProgramFile) {
		return null;
	}
	
	/**
	 * 加载顶点着色器
	 * @param vertexShader
	 * @return
	 */
	public boolean addVertexShader(VertexShader vertexShader) {
		boolean installResult = false;
		
		if (vertexShader != null) {
			installResult = vertexShader.compileShader();
			mVertexShaders.add(vertexShader);
		}
		
		return installResult;
	}
	
	/**
	 * 加载片段着色器
	 * @param fragmentShader
	 * @return
	 */
	public boolean addFragmentShader(FragmentShader fragmentShader) {
		boolean installResult = false;
		
		if (fragmentShader != null) {
			installResult = fragmentShader.compileShader();
			mFragmentShaders.add(fragmentShader);
		}
		
		return installResult;
	}
	
	/**
	 * 析出着色器变量
	 * @param paramList
	 */
	private void extractShaderParameters(HashMap<String, ShaderParameter> paramList) {
		if  (paramList != null) {
			Iterator<Entry<String, ShaderParameter>> iter = paramList.entrySet().iterator();
			
			while (iter.hasNext()) {
				Entry<String, ShaderParameter> entry = (Entry<String, ShaderParameter>) iter.next();
				String          key   = entry.getKey();
				ShaderParameter value = entry.getValue();
				
				// 获取变量ID
				if (value != null) {
					switch (value.getParameterType()) {
					case ShaderParameter.UNIFORM_PARAMETER:
						value.setParameterID(GLES20.glGetUniformLocation(mShaderProgramID, key));
						break;
						
					case ShaderParameter.ATTRIBUTE_PARAMETER:
						value.setParameterID(GLES20.glGetAttribLocation(mShaderProgramID, key));
						break;
						
					case ShaderParameter.VARYING_PARAMETER:
						value.setParameterID(ShaderParameter.VALID_PARAMETER);
						break;
					}
				}
				
				// 添加到快速查找列表
				mParametersList.put(key, value);
			}
		}
	}
	
	/**
	 * 取得变量ID
	 * @param paramName 变量名
	 * @return
	 */
	private int getShaderParameterID(String paramName) {
		int             parameterID = ShaderParameter.VALID_PARAMETER;
		ShaderParameter parameter   = mParametersList.get(paramName);
		
		if (parameter != null && parameter.isValid()) {
			parameterID = parameter.getParameterID();
		}
		
		return parameterID;
	}

	/**
	 * 编译着色器程序
	 * @return
	 */
	public boolean compileShaderProgram() {
		int[]   linkStatus    = new int[1];
		boolean compileResult = false;
		
		mShaderProgramID = GLES20.glCreateProgram();
		if (mShaderProgramID != VALID_SHADER_PROGRAM) {
			
			// 挂载顶点着色器
			int vertexShadersSize = mVertexShaders.size();
			for (int i = 0; i < vertexShadersSize; i++) {
				GLES20.glAttachShader(mShaderProgramID, mVertexShaders.get(i).getShaderID());
			}
			
			// 挂载片段着色器
			int fragmentShadersSize = mFragmentShaders.size();
			for (int i = 0; i < fragmentShadersSize; i++) {
				GLES20.glAttachShader(mShaderProgramID, mFragmentShaders.get(i).getShaderID());
			}
			
			// 链接着色器程序
			GLES20.glLinkProgram(mShaderProgramID);
			
			// 检查着色器程序链接情况
			GLES20.glGetProgramiv(mShaderProgramID, GLES20.GL_LINK_STATUS, linkStatus, 0);
			
			// 获取链接日志
			mShaderProgramLog = GLES20.glGetProgramInfoLog(mShaderProgramID);
			
			// 判断链接结果
			if (linkStatus[0] != VALID_SHADER_PROGRAM) {
				compileResult = true;
				
				// 提取顶点着色器变量
				vertexShadersSize = mVertexShaders.size();
				for (int i = 0; i < vertexShadersSize; i++) {
					// 析出Uniform着色器变量
					extractShaderParameters(mVertexShaders.get(i).getUniformParameterList());
					// 析出Attribute着色器变量
					extractShaderParameters(mVertexShaders.get(i).getAttributeParameterList());
					// 析出Varying着色器变量
					extractShaderParameters(mVertexShaders.get(i).getVaryingParameterList());
				}
				
				// 提取片段着色器变量
				fragmentShadersSize = mFragmentShaders.size();
				for (int i = 0; i < fragmentShadersSize; i++) {
					// 析出Uniform着色器变量
					extractShaderParameters(mFragmentShaders.get(i).getUniformParameterList());
					// 析出Attribute着色器变量
					extractShaderParameters(mFragmentShaders.get(i).getAttributeParameterList());
					// 析出Varying着色器变量
					extractShaderParameters(mFragmentShaders.get(i).getVaryingParameterList());
				}
				
			}else{
				compileResult = false;
				
				// 删除当前着色器程序
				GLES20.glDeleteProgram(mShaderProgramID);
				mShaderProgramID = VALID_SHADER_PROGRAM;
			}
		}
		
		mIsCompiled = compileResult;
		return compileResult;
	}
	
	/**
	 * 验证着色器程序在当前OpenGLES环境下是否有效
	 * @return
	 */
	public boolean validateShaderProgram() {
		int[] validateStatus = new int[1];
		
		if (mShaderProgramID != VALID_SHADER_PROGRAM && GLES20.glIsProgram(mShaderProgramID) && mIsCompiled) {
			GLES20.glValidateProgram(mShaderProgramID);
			// 检查着色器程序验证情况
			GLES20.glGetProgramiv(mShaderProgramID, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
		}
		
		return validateStatus[0] == GLES20.GL_TRUE ? true : false;
	}
	
	/**
	 * 销毁着色器程序
	 */
	public void destoryShaderProgram() {
		if (mShaderProgramID != VALID_SHADER_PROGRAM && GLES20.glIsProgram(mShaderProgramID)) {
			GLES20.glDeleteProgram(mShaderProgramID);
			mShaderProgramID = VALID_SHADER_PROGRAM;
			mIsCompiled = false;
		}
	}
	
	/**
	 * 执行着色器程序
	 */
	public void run() {
		if (mIsCompiled) {
			GLES20.glUseProgram(mShaderProgramID);
		}
	}
	
	/**
	 * 着色器程序是否已经编译
	 * @return
	 */
	public boolean isCompiled() {
		return mIsCompiled;
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	//   设置着色器程序运行参数 
	///////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 设置着色器程序运行参数, int
	 * @param paramName 
	 * @param value
	 */
	public void setShaderParameters(String paramName, int value) {
		int             parameterID   = ShaderParameter.VALID_PARAMETER;
		int             parameterType = ShaderParameter.UNSPECIFIED_PARAMETER;
		ShaderParameter parameter     = mParametersList.get(paramName);
		
		if (parameter != null) {
			parameterID = parameter.getParameterID();
			parameterType = parameter.getParameterType();
		}
		
		if (parameterID != ShaderParameter.VALID_PARAMETER) {
			switch (parameterType) {
			case ShaderParameter.UNIFORM_PARAMETER:
				GLES20.glUniform1i(parameterID, value);
				break;
				
			case ShaderParameter.ATTRIBUTE_PARAMETER:
				GLES20.glVertexAttrib1f(parameterID, value);
				break;
				
			case ShaderParameter.VARYING_PARAMETER:
				// no supported
				break;
			}
		}
	}
	
	/**
	 * 设置着色器程序运行参数, float
	 * @param paramName
	 * @param value
	 */
	public void setShaderParameters(String paramName, float value) {
		int             parameterID   = ShaderParameter.VALID_PARAMETER;
		int             parameterType = ShaderParameter.UNSPECIFIED_PARAMETER;
		ShaderParameter parameter     = mParametersList.get(paramName);
		
		if (parameter != null) {
			parameterID = parameter.getParameterID();
			parameterType = parameter.getParameterType();
		}
		
		if (parameterID != ShaderParameter.VALID_PARAMETER) {
			switch (parameterType) {
			case ShaderParameter.UNIFORM_PARAMETER:
				GLES20.glUniform1f(parameterID, value);
				break;
				
			case ShaderParameter.ATTRIBUTE_PARAMETER:
				GLES20.glVertexAttrib1f(parameterID, value);
				break;
				
			case ShaderParameter.VARYING_PARAMETER:
				// no supported
				break;
			}
		}
	}
	
	/**
	 * 设置着色器程序运行参数, intArray
	 * @param paramName
	 * @param value 不能为矩阵参数
	 * @throws ShaderParameterTooManyException 输入的数组元素过多，参数数组元素个数只应该有1~4个
	 */
	public void setShaderParameters(String paramName, int[] value) throws ShaderParameterCountInvalidException {
		if (value != null && value.length > 0) {
			int             parameterID   = ShaderParameter.VALID_PARAMETER;
			int             parameterType = ShaderParameter.UNSPECIFIED_PARAMETER;
			ShaderParameter parameter     = mParametersList.get(paramName);
			
			if (parameter != null) {
				parameterID = parameter.getParameterID();
				parameterType = parameter.getParameterType();
			}
			
			switch (parameterType) {
			case ShaderParameter.UNIFORM_PARAMETER:
			{
				if (value.length == 1) {
					GLES20.glUniform1i(parameterID, value[0]);
				}else if (value.length == 2) {
					GLES20.glUniform2i(parameterID, value[0], value[1]);
				}else if (value.length == 3) {
					GLES20.glUniform3i(parameterID, value[0], value[2], value[3]);
				}else if (value.length == 4) {
					GLES20.glUniform4i(parameterID, value[0], value[2], value[3], value[4]);
				}else{
					throw new ShaderParameterCountInvalidException(
							"Length of value is " + value.length + ", length should be bwteen 1 and 4");
				}
			}
				break;
				
			case ShaderParameter.ATTRIBUTE_PARAMETER:
			{
				if (value.length == 1) {
					GLES20.glVertexAttrib1f(parameterID, value[0]);
				}else if (value.length == 2) {
					GLES20.glVertexAttrib2f(parameterID, value[0], value[1]);
				}else if (value.length == 3) {
					GLES20.glVertexAttrib3f(parameterID, value[0], value[2], value[3]);
				}else if (value.length == 4) {
					GLES20.glVertexAttrib4f(parameterID, value[0], value[2], value[3], value[4]);
				}else{
					throw new ShaderParameterCountInvalidException(
							"Length of value is " + value.length + ", length should be bwteen 1 and 4");
				}
			}
				break;
				
			case ShaderParameter.VARYING_PARAMETER:
				// no supported
				break;
			}
		}
	}
	
	/**
	 * 设置着色器程序运行参数, floatArray
	 * @param paramName
	 * @param value 不能为矩阵参数
	 * @throws ShaderParameterTooManyException 输入的数组元素过多，参数数组元素个数只应该有1~4个
	 */
	public void setShaderParameters(String paramName, float[] value) throws ShaderParameterCountInvalidException {
		if (value != null && value.length > 0) {
			int             parameterID   = ShaderParameter.VALID_PARAMETER;
			int             parameterType = ShaderParameter.UNSPECIFIED_PARAMETER;
			ShaderParameter parameter     = mParametersList.get(paramName);
			
			if (parameter != null) {
				parameterID = parameter.getParameterID();
				parameterType = parameter.getParameterType();
			}
			
			switch (parameterType) {
			case ShaderParameter.UNIFORM_PARAMETER:
			{
				if (value.length == 1) {
					GLES20.glUniform1f(parameterID, value[0]);
				}else if (value.length == 2) {
					GLES20.glUniform2f(parameterID, value[0], value[1]);
				}else if (value.length == 3) {
					GLES20.glUniform3f(parameterID, value[0], value[2], value[3]);
				}else if (value.length == 4) {
					GLES20.glUniform4f(parameterID, value[0], value[2], value[3], value[4]);
				}else{
					throw new ShaderParameterCountInvalidException(
							"Length of value is " + value.length + ", length should be bwteen 1 and 4");
				}
			}
				break;
				
			case ShaderParameter.ATTRIBUTE_PARAMETER:
			{
				if (value.length == 1) {
					GLES20.glVertexAttrib1f(parameterID, value[0]);
				}else if (value.length == 2) {
					GLES20.glVertexAttrib2f(parameterID, value[0], value[1]);
				}else if (value.length == 3) {
					GLES20.glVertexAttrib3f(parameterID, value[0], value[2], value[3]);
				}else if (value.length == 4) {
					GLES20.glVertexAttrib4f(parameterID, value[0], value[2], value[3], value[4]);
				}else{
					throw new ShaderParameterCountInvalidException(
							"Length of value is " + value.length + ", length should be bwteen 1 and 4");
				}
			}
				break;
				
			case ShaderParameter.VARYING_PARAMETER:
				// no supported
				break;
			}
		}
	}
	
	/**
	 * 设置着色器程序运行参数, floatMatrix
	 * @param paramName
	 * @param value
	 * @throws ShaderParameterCountInvalidException
	 */
	public void setShaderMatrixParameters(String paramName, float[] value) throws ShaderParameterCountInvalidException {
		if (value != null && value.length > 0) {
			int             parameterID   = ShaderParameter.VALID_PARAMETER;
			int             parameterType = ShaderParameter.UNSPECIFIED_PARAMETER;
			ShaderParameter parameter     = mParametersList.get(paramName);
			
			if (parameter != null) {
				parameterID = parameter.getParameterID();
				parameterType = parameter.getParameterType();
			}
			
			if (parameterID != ShaderParameter.VALID_PARAMETER) {
				switch (parameterType) {
				case ShaderParameter.UNIFORM_PARAMETER:
				{
					if (value.length == 4) {
						GLES20.glUniformMatrix2fv(parameterID, 4, false, value, 0);
					}else if (value.length == 9) {
						GLES20.glUniformMatrix3fv(parameterID, 9, false, value, 0);
					}else if (value.length == 16) {
						GLES20.glUniformMatrix4fv(parameterID, 16, false, value, 0);
					}else{
						throw new ShaderParameterCountInvalidException(
								"Length of value is " + value.length + ", length should be 4(2x2) or 9(3x3) or 16(4x4)");
					}
				}
					break;
					
				case ShaderParameter.ATTRIBUTE_PARAMETER:
					// no supported
					break;
					
				case ShaderParameter.VARYING_PARAMETER:
					// no supported
					break;
				}
			}
		}
	}
	
	/**
	 * 设置着色器程序运行参数, floatMatrix
	 * @param paramName
	 * @param value
	 * @throws ShaderParameterCountInvalidException
	 */
	public void setShaderTransposeMatrixParameters(String paramName, float[] value) throws ShaderParameterCountInvalidException {
		if (value != null && value.length > 0) {
			int             parameterID   = ShaderParameter.VALID_PARAMETER;
			int             parameterType = ShaderParameter.UNSPECIFIED_PARAMETER;
			ShaderParameter parameter     = mParametersList.get(paramName);
			
			if (parameter != null) {
				parameterID = parameter.getParameterID();
				parameterType = parameter.getParameterType();
			}
			
			if (parameterID != ShaderParameter.VALID_PARAMETER) {
				switch (parameterType) {
				case ShaderParameter.UNIFORM_PARAMETER:
				{
					if (value.length == 4) {
						GLES20.glUniformMatrix2fv(parameterID, 4, true, value, 0);
					}else if (value.length == 9) {
						GLES20.glUniformMatrix3fv(parameterID, 9, true, value, 0);
					}else if (value.length == 16) {
						GLES20.glUniformMatrix4fv(parameterID, 16, true, value, 0);
					}else{
						throw new ShaderParameterCountInvalidException(
								"Length of value is " + value.length + ", length should be 4(2x2) or 9(3x3) or 16(4x4)");
					}
				}
					break;
					
				case ShaderParameter.ATTRIBUTE_PARAMETER:
					// no supported
					break;
					
				case ShaderParameter.VARYING_PARAMETER:
					// no supported
					break;
				}
			}
		}
	}
	
}
