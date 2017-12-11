package com.x.opengl.gles;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;

import com.x.Director;
import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.Mesh;


public class ObjShader extends T_Shader{

	protected int				mProgram;				// 自定义渲染管线着色器程序id
	protected int				muMVPMatrixHandle;		// 总变换矩阵引用
	protected int				muMMatrixHandle;		// 模型变换矩阵

	protected int				muAmbientHandle;		// 材质环境光属性引用
	protected int				muDiffuseHandle;		// 材质漫反射光属性引用
	protected int				muSpecularHandle;		// 材质镜面反射属性引用
	
	protected int				muLightAmbientHandle;		// 光的环境光属性引用
	protected int				muLightDiffuseHandle;		// 光的漫反射光属性引用
	protected int				muLightSpecularHandle;		// 光的镜面反射属性引用

	protected int				muEyePositionHandle;		// 眼睛的位置
	protected int				muAlphaHandle;      	// 透明度引用
	protected int				muLightEnableHandle;      	// 启用光照引用
	protected int				muU_LightStyle;      	// 光照方式引用
	
	
	protected int 				muEXTERNAL_OES_Handle;  // 纹理渲染方式引用 1为TEXTURE_2D方式  , 0为TEXTURE_EXTERNAL_OES
	protected int 				muLightPositionHandle;	// 光源位置属性引用

	
	protected int				maPositionHandle;		// 顶点位置属性引用
	protected int				maColorHandle;			// 顶点颜色属性引用
	
	protected int				maTexCoorHandle;		// 顶点纹理属性引用
	
	protected int 				maNormalHandle; 		// 顶点法向量属性引用

	protected String			mVertexShader;			// 顶点着色器
	protected String			mFragmentShader;		// 片元着色器

//	protected FloatBuffer 		mLightBuffer = null;
//	protected float[] 			mLightInitPosition = new float[]{-4f,2f,4f};
	
	//顶点着色器
	protected static   String 	U_MVPMatrix = 		"uMVPMatrix";
	protected static   String 	U_MMatrix = 		"uMMatrix";
	
	protected static   String 	U_LightPosition = 	"uLightLocation";
	protected static   String 	U_EyePosition = 	"uEyeLocation";
	
	protected static   String 	U_Alpha = 			"uAlpha";
	protected static   String   U_LightEnable = 	"uLightEnable";//是否启用光照 1为启用 0为拒绝
	protected static   String   U_LightStyle = 		"U_LightStyle";//是否是否为平行光  1为平行光  0为点光源
	protected static   String 	U_STexture_ = 		"sTexture_";
	protected static   String 	U_OES = 			"uOES";
	protected static   String 	U_Ambient = 		"uAmbient";//材质环境光成分
	protected static   String 	U_Diffuse = 		"uDiffuse";//材质漫反射光成分
	protected static   String   U_Specular	= 		"uSpecular";//材质的镜面反射光成分
	
	protected static   String 	U_Light_Ambient  = 		"uLight_Ambient";//光的环境光成分
	protected static   String 	U_Light_Diffuse  = 		"uLight_Diffuse";//光的漫反射光成分
	protected static   String   U_Light_Specular = 		"uLight_Specular";//光的镜面反射光成分
	
	protected static   String 	A_TexCoor = 		"aTexCoor";
	protected static   String 	A_Position = 		"aPosition";
	protected static   String 	A_Color = 			"aColor";
	protected static   String 	A_Normal = 			"aNormal";

	protected static   String 	V_TextureCoord_ = 	"vTextureCoord_";
	protected static   String 	V_Ambient = 		"vAmbient";
	protected static   String 	V_Diffuse = 		"vDiffuse";
	protected static   String 	V_Color = 			"vColor";

	@Override
	protected String getVetexShell() {
//		return "shader/oesVertex.sh"; 
		return "shader/vertex.sh"; 
	}
	@Override
	protected String getFragShell() {
//		return "shader/oesFrag.sh";
		return "shader/frag.sh";
	}
	
	protected String getVetexString(){
		   
		String vetexString = TextUtils.join("\n", new String[]{
				"uniform mat4 "+U_MVPMatrix+"; ",//总变换矩阵
				"uniform mat4 "+U_MMatrix+"; ",	//模型矩阵
				
				"uniform vec3 "+U_EyePosition+";",
				"uniform vec3 "+U_LightPosition+";",
				"uniform float "+U_LightStyle+";",
				
				"attribute vec2 "+A_TexCoor+";",		//顶点纹理坐标
				"attribute vec3 "+A_Position+";",	//顶点位置
				"attribute vec4 "+A_Color+";", //obj格式默认没有颜色，所以这里是没有数据传过来的 ，我们这里采用材质的漫反射
				"attribute vec3 "+A_Normal+";", 
				
//				"attribute vec3 "+A_Ambient+";", 

				"varying vec2 "+V_TextureCoord_+";",	//用于传递给片元着色器的变量
				"varying vec4 "+V_Color+";",	//用于传递给片元着色器的变量
//				"varying vec4 "+V_Ambient+";",	//用于传递给片元着色器的变量
				"varying vec3 L;",
				"varying vec3 N;",
				"varying vec3 Eye;",
				
				"void main()", 
				"{ ",
					"gl_Position = "+U_MVPMatrix+" * vec4("+A_Position+",1);",
					V_TextureCoord_+" = "+A_TexCoor+";",
					
					//-----------------基本变量数据准备
					"vec4 mm_A_Position  = "+U_MMatrix+" * vec4("+A_Position+",1);",//计算模型变换变换后的顶点
					"N  =   ("+U_MMatrix+" * vec4("+A_Normal+",1)).xyz;",//计算模型变换后的法线
					
					//光源的位置暂时未经过转换，如果程序中将光源定义为一个有这自己模型矩阵的对象，则也需要进行对光源位置的转换

					//"if("+U_LightStyle+" == 1.0){",
						"L = normalize( -"+U_LightPosition+" );",//这个是平行光,取光源位置指向原点的方向为光源方向,如果想拿物体做参照物，则物体要停在原点的位置
					//"}else{",
					//	"L = normalize( vec3(mm_A_Position.xyz - "+U_LightPosition+"  ) );",//从光源位置向量指向物体顶点,这个是点光源
					//"}",
					"Eye = normalize( vec3("+U_EyePosition+"- mm_A_Position.xyz));",
					
					//-----------------
					
			//		"vec4 ambientValue = "+U_Light_Ambient +"*"+ U_Ambient+"*ambient();",//变量名和方法名不能同名，不然什么东西都不会显示，这应该是glsl的bug
			//		"vec4 diffuseValue = "+U_Light_Diffuse +"*"+ U_Diffuse+"*diffuse(N,L);",
			//		"vec4 specularValue = "+U_Light_Specular +"*"+ U_Specular+"*specular(N,L,Eye);",
					
					
			//		V_Color+" = ambientValue+diffuseValue+specularValue;",
			//		V_Ambient+" = vec4("+A_Ambient+",1);",
					
			//		V_Color +"= "+A_Color+";",//obj文件没有颜色传递过来

				"} ",
				
		}); 

//		Log.d("ming", vetexString + "\n\n");
		
		return vetexString;
	}
	protected String getFragString(){
		   
		String fragString = TextUtils.join("\n", new String[]{
				 
				"precision mediump float;",
				"uniform sampler2D "+U_STexture_+";",//纹理内容数据
				 
				//U_OES
				"uniform float "+U_Alpha+";",
				"uniform float "+U_LightEnable+";",

				"uniform vec4 "+U_Ambient+";",		//用于传递给片元着色器的环境光变量
				"uniform vec4 "+U_Diffuse+";",		 
				"uniform vec4 "+U_Specular+";",		 
				
				"uniform vec4 "+U_Light_Ambient+";",		 
				"uniform vec4 "+U_Light_Diffuse+";",		 
				"uniform vec4 "+U_Light_Specular+";",		 
				
				"float alphaFinal ;",
				
				"varying vec2 "+V_TextureCoord_+";",	//用于接受从定点着色器传递的变量
//				"varying vec4 "+V_Ambient+";",		//用于传递给片元着色器的环境光变量
				"varying vec4 "+V_Diffuse+";",		//用于传递给片元着色器的漫反射变量
				"varying vec4 "+V_Color+";", 
				"varying vec3 L;",
				"varying vec3 N;",
				"varying vec3 Eye;",
				
				"float ambient( )",//环境光
				"{",
				   "return 1.0;",
				"}",
				
				"float diffuse(vec3 N,vec3 L)",//漫反射
				"{",
					"float percent =  max( dot(N,-L),0.0)"+";",
					"return  percent "+";",
				"}",
				
				/**
				 * 镜面反射，假设模型如下图所示称为Blin-Phong模型的光照模型,以下是简化版，完整版是什么呢，暂时没去看
				 * 
				 * L是入射光线 R是反射光线 ，N是该处的法线
				 * 
				 * Blin-Phong模型认为光照 模型镜面反射成分主要与     反射光线与视线夹角的余弦   相关，如下图a角的余弦
				 *  
				 *  设 R点指向L点的位置 向量为2P
				 *   
				 *   R = 2P - L
				 *   
				 *    P  = L + s 
				 *   
				 *   因为 s = (L.N)* N/(|N|*|N|) //请谨慎推导 
				 *   
				 *   所以 R = 2*(L.N)*N + L
				 * 
				 *  cos(a) = (R.Eye) / (|R|*|Eye|) ;//
				 *  因为都是单位向量
				 *  所以cos(a) = R.Eye;
				 *  
				 * 
				 * 			R         N    			L
				 * 			\——p      |——p 		/
				 * 			 \        |s  	   /
				 * 			  \	      |  	  /  
				 * 			   \      |thita /   
				 * 				\     |     /   
				 * 				 \	  |    /
				 * 				  \   |   /
				 * 				   \  |  /
				 * 					\ | /
				 * 				a/   \|/
				 * Eye—————————————————
				 * 
				 */
				
				"float specular(vec3 N,vec3 L ,vec3 Eye)",
				"{",
				    
					"vec3 R = normalize(2.0*dot(L,N)*N + L  )"+";",
					"float percent =  pow(max(dot( Eye,R),0.0) ,20.0)"+";",//这里的数字是衰减因子，数值越大，高光区越小
						
					"return  percent "+";",
				"}",
				
				"void main() ",
				"{ ",
					"if("+U_LightEnable+" == 1.0){",
//						
						"vec4 textureColor = texture2D("+U_STexture_+", "+V_TextureCoord_+"); ",
	
	//					"vec4 ambientValue = "+U_Light_Ambient +"*"+ U_Ambient+"*ambient();",//变量名和方法名不能同名，不然什么东西都不会显示，这应该是glsl的bug
	//					"vec4 diffuseValue = "+U_Light_Diffuse +"*"+ U_Diffuse+"*diffuse(N,L);", 
	//					"vec4 specularValue = "+U_Light_Specular +"*"+ U_Specular+"*specular(N,L,Eye);", 
	//					"vec4 colorFinal = (ambientValue + diffuseValue + specularValue+textureColor) ;",
						
						"vec4 ambientValue =    "+U_Light_Ambient +"*(  textureColor+"+U_Ambient+  " )*ambient();",//变量名和方法名不能同名，不然什么东西都不会显示，这应该是glsl的bug
						"vec4 diffuseValue =    "+U_Light_Diffuse +"*(  textureColor+"+U_Diffuse+ " ) *diffuse(N,L);", 
						"vec4 specularValue =   "+U_Light_Specular +"*( textureColor+"+U_Specular+  ") *specular(N,L,Eye);",
	
//						"vec4 colorFinal =   vec4(normalize((ambientValue + diffuseValue + specularValue).xyz),1.0)  ;",
						"vec4 colorFinal =   (ambientValue + diffuseValue  + specularValue  )    ;",
//						"vec4 colorFinal =  vec4(-2.1,-1.1,-4.10,1.0)    ;",
						"colorFinal.a = 1.0 *"+U_Alpha+";",
						
//						"gl_FragColor = textureColor; ",
						"gl_FragColor = colorFinal; ",
						
					"}else {",

					
						"vec4 textureColor = texture2D("+U_STexture_+", "+V_TextureCoord_+"); ",

						"gl_FragColor = textureColor   "+ "; ",
						
						
					"}",
					
//					"alphaFinal =  color.a*"+U_Alpha+";",
//					"gl_FragColor = vec4(color.rgb/color.a,alphaFinal); ",
					
				"} ",
				
		});

		Log.d("ming", fragString);
		
		return fragString;
	}

	protected void init() {
		
//		mLightBuffer = Director.sResourcer.floatBuffer(mLightInitPosition);

		// 加载顶点着色器的脚本内容
		mVertexShader = getVetexString();
		// 加载片元着色器的脚本内容
		mFragmentShader = getFragString();

		// 基于顶点着色器与片元着色器创建程序
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// 获取程序中顶点位置属性引用
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, A_Position );
		// 获取程序中顶点颜色属性引用
		maColorHandle = GLES20.glGetAttribLocation(mProgram, A_Color);
		
		// 获取程序中顶点纹理坐标属性引用
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, A_TexCoor);
		// 获取程序中总变换矩阵引用
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, U_MVPMatrix);

		muAmbientHandle = GLES20.glGetUniformLocation(mProgram, U_Ambient);
		muDiffuseHandle = GLES20.glGetUniformLocation(mProgram, U_Diffuse);
		muSpecularHandle = GLES20.glGetUniformLocation(mProgram, U_Specular);
		
		muLightAmbientHandle = GLES20.glGetUniformLocation(mProgram, U_Light_Ambient);
		muLightDiffuseHandle = GLES20.glGetUniformLocation(mProgram, U_Light_Diffuse);
		muLightSpecularHandle = GLES20.glGetUniformLocation(mProgram, U_Light_Specular);

		// 获取程序的旋转缩放变换矩阵
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram,  U_MMatrix );
		
	
		// 获取程序中透明度引用
		muAlphaHandle = GLES20.glGetUniformLocation(mProgram, U_Alpha);
		
		muLightEnableHandle = GLES20.glGetUniformLocation(mProgram, U_LightEnable);
		
		muU_LightStyle = GLES20.glGetUniformLocation(mProgram, U_LightStyle);
		
		
		
		// 获取纹理渲染方式
		muEXTERNAL_OES_Handle = GLES20.glGetUniformLocation(mProgram, U_OES);
		
		//获取程序中顶点法向量属性引用  
        maNormalHandle		= GLES20.glGetAttribLocation(mProgram, A_Normal);
        
        //获取程序中光源位置引用
        muLightPositionHandle = GLES20.glGetUniformLocation(mProgram, U_LightPosition);
        
        muEyePositionHandle   = GLES20.glGetUniformLocation(mProgram, U_EyePosition);
        
        //
	}

	// luoyouren: 最底层的绘制函数
	protected void goRender(Mesh mesh, Material material,  float[] selfModelViewMatrix) {
		
		GLES20.glUseProgram(mProgram);
		
		uniform(mesh,material,selfModelViewMatrix);
		attrib(mesh,material,selfModelViewMatrix);
		
		if (material.Texture != null && material.Texture.isValid()) {
//			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, material.Texture.getTextureID());
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(material.Texture.getBindTarget(), material.Texture.getTextureID());
		} 
		
		if (mesh.IndexBuffer != null) {
			mesh.IndexBuffer.rewind();
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.IndexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, mesh.IndexBuffer);
//			GLES20.glDrawElements(GLES20.GL_LINES, mesh.IndexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, mesh.IndexBuffer);
		}
		
	}

	private void attrib(Mesh mesh, Material material,
			float[] selfModelViewMatrix) {

		if (mesh.VertexBuffer != null) {
			// 为画笔指定顶点位置数据
			GLES20.glVertexAttribPointer(maPositionHandle, 3/*该参数表示一组数据有几个数组元素*/, GLES20.GL_FLOAT, false, 3 * 4/*该参数表示一组数据有多少个字节*/, mesh.VertexBuffer.rewind());
			GLES20.glEnableVertexAttribArray(maPositionHandle);
		}
		
		if(mesh.ColorBuffer != null){
			//obj格式默认没有颜色，所以这里只有mesh初始带的四个数据，其他都是空的 ，因此我们采用在材质里面的ka kd ks属性
			GLES20.glVertexAttribPointer(maColorHandle,4,GLES20.GL_FLOAT,false,4*4,mesh.ColorBuffer);
			GLES20.glEnableVertexAttribArray(maColorHandle);
		}
		
		if (mesh.CoordBuffer != null) {
			// 将顶点纹理坐标数据传递到渲染管线
			GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mesh.CoordBuffer);
			GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		}

        //将顶点法向量数据传入渲染管线
		if(mesh.NormBuffer != null){
			GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false,	3 * 4, mesh.NormBuffer);
			GLES20.glEnableVertexAttribArray(maNormalHandle);
		}
				
	}
	private void uniform(Mesh mesh, Material material ,float[] selfModelViewMatrix) {

		// 将最终变换矩阵传入shader程序
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(selfModelViewMatrix), 0);
		  //将模型矩阵传入着色器程序
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 		1, false, MatrixState.getMMatrix(), 0);
//		GLES20.glUniformMatrix4fv(muEyeMMatrixHandle, 	1, false, Director.getInstance().getEyeMMatrix(), 0);
		
		GLES20.glUniform1f(muLightEnableHandle, Director.getInstance().getLightEnable() ? 1.0f:0.0f);
		GLES20.glUniform1f(muU_LightStyle, Director.getInstance().getLightStyle()  );
		
		
		GLES20.glUniform3fv(muLightPositionHandle, 1,Director.getInstance().getLightPosition() , 0);
		GLES20.glUniform3fv(muEyePositionHandle,   1, Director.getInstance().getEyePosition() , 0);
		
		GLES20.glUniform4fv(muLightAmbientHandle, 1, Director.getInstance().getAmbient(), 0);
		GLES20.glUniform4fv(muLightDiffuseHandle, 1, Director.getInstance().getDiffuse(), 0);
		GLES20.glUniform4fv(muLightSpecularHandle, 1, Director.getInstance().getSpecular(), 0);
		
		GLES20.glUniform1f(muAlphaHandle,MatrixState.getFinalAlpha());
		GLES20.glUniform1f(muEXTERNAL_OES_Handle, material.Texture.isBindTarget2d());

		if(material.mAmbientBuffer != null){
			GLES20.glUniform4fv(muAmbientHandle, 1, material.getAmbient(), 0);
		}
		if(material.mDiffuseBuffer != null){
			GLES20.glUniform4fv(muDiffuseHandle, 1, material.getDiffuse(), 0);
		}
		if(material.mSpecularBuffer != null){
			GLES20.glUniform4fv(muSpecularHandle, 1, material.getSpecular(), 0);
		}		


//		if(mLightBuffer != null){
//			//将光源位置传入着色器程序   
//			GLES20.glUniform3fv(maLightLocationHandle, 1, mLightBuffer);
//		}
	}

}
