package com.x.opengl.gles;

import javax.microedition.khronos.opengles.GL10;

import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.Mesh;

import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;



public class OESShader extends T_Shader{

	protected int				mProgram;				// 自定义渲染管线着色器程序id
	protected int				muMVPMatrixHandle;		// 总变换矩阵引用
	protected int				muMMatrixHandle;		// 旋转位移缩放变换矩阵
	protected int				maPositionHandle;		// 顶点位置属性引用
	protected int				maColorHandle;			// 顶点颜色属性引用
	protected int				maTexCoorHandle;		// 顶点纹理属性引用
	protected int				muAlphaHandle;      	// 透明度引用
	protected int 				muEXTERNAL_OES_Handle;  // 纹理渲染方式引用 1为TEXTURE_2D方式  , 0为TEXTURE_EXTERNAL_OES
	protected int 				maNormalHandle; 		// 顶点法向量属性引用
	protected int 				maLightLocationHandle;	// 光源位置属性引用

	protected String			mVertexShader;			// 顶点着色器
	protected String			mFragmentShader;		// 片元着色器

	protected float[] 			mLightInitPosition = new float[]{-4f,2f,4f};
	
	//顶点着色器
	protected static   String 	U_MVPMatrix = 		"uMVPMatrix";
	protected static   String 	U_MMatrix = 		"uMMatrix";
	protected static   String 	U_LightLocation = 	"uLightLocation";
	protected static   String 	U_Alpha = 			"uAlpha";
	protected static   String 	U_STexture_ = 		"sTexture_";
	protected static   String 	U_OESTexture_ = 			"oesTexture";
	
	protected static   String 	A_TexCoor = 		"aTexCoor";
	protected static   String 	A_Position = 		"aPosition";
	protected static   String 	A_Color = 			"aColor";
	protected static   String 	A_Normal = 			"aNormal";

	protected static   String 	V_TextureCoord_ = 	"vTextureCoord_";
	protected static   String 	V_Ambient = 		"vAmbient";
	protected static   String 	V_Diffuse = 		"vDiffuse";
	protected static   String 	V_Color = 			"vColor";

	public OESShader() {

//		mLightBuffer = Director.sResourcer.floatBuffer(new float[]{-4f,2f,4f});
	}
	@Override
	protected String getVetexShell() {
		return "shader/vertex.sh"; 
	}
	@Override
	protected String getFragShell() {
		return "shader/frag.sh";
	}
	protected String getVetexString(){
		   
		String vetexString = TextUtils.join("\n", new String[]{
				"uniform mat4 "+U_MVPMatrix+"; ",//总变换矩阵
				"uniform mat4 "+U_MMatrix+"; ",	//模型视图矩阵
				
				"uniform vec3 "+U_LightLocation+";",
				
				"attribute vec2 "+A_TexCoor+";",		//顶点纹理坐标
				"attribute vec3 "+A_Position+";",	//顶点位置
				"attribute vec4 "+A_Color+";", 
				"attribute vec3 "+A_Normal+";", 

				"varying vec2 "+V_TextureCoord_+";",	//用于传递给片元着色器的变量
				"varying vec4 "+V_Color+";",	//用于传递给片元着色器的变量
				
				"void main()", 
				"{ ",
					"gl_Position = "+U_MVPMatrix+" * vec4("+A_Position+",1);",
					V_TextureCoord_+" = "+A_TexCoor+";",
					V_Color+" = "+A_Color+";",
				"} ",
		}); 

		Log.d("ming", vetexString + "\n\n");
		return vetexString;
	}
	protected String getFragString(){
		   
		String fragString = TextUtils.join("\n", new String[]{ 

				"#extension GL_OES_EGL_image_external : require ",/**注意这条语句结束位置不用写分号*/
				"precision mediump float;",
				"uniform samplerExternalOES "+ U_OESTexture_+" ;",
				//"uniform sampler2D "+U_OESTexture_+";",//纹理内容数据
				
				
				"uniform float "+U_Alpha+";",
				"float alphaFinal ;",
				
				"varying vec2 "+V_TextureCoord_+";",	//用于接受从定点着色器传递的变量
				"varying vec4 "+V_Ambient+";",		//用于传递给片元着色器的环境光变量
				"varying vec4 "+V_Diffuse+";",		//用于传递给片元着色器的漫反射变量
				"varying vec4 "+V_Color+";", 
				
				"void main() ",
				"{ ",
					"vec4 color = texture2D("+U_OESTexture_+", "+V_TextureCoord_+"); ",
					"gl_FragColor = color; ",
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
		
		// 获取程序的旋转缩放变换矩阵
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram,  U_MMatrix );
	
		// 获取程序中透明度引用
		muAlphaHandle = GLES20.glGetUniformLocation(mProgram, U_Alpha);
		
//		// 获取纹理渲染方式
//		muEXTERNAL_OES_Handle = GLES20.glGetUniformLocation(mProgram, U_OES);
		
		//获取程序中顶点法向量属性引用  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, A_Normal);
        
        
        //获取程序中光源位置引用
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, U_LightLocation);
	}

	protected void goRender(Mesh mesh, Material material,  float[] selfModelViewMatrix) {
		
		GLES20.glUseProgram(mProgram);


		if (material.Texture != null && material.Texture.isValid()) {
//			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, material.Texture.getTextureID());
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(material.Texture.getBindTarget(), material.Texture.getTextureID());
		} 
		GLES20.glUniform1f(muAlphaHandle,MatrixState.getFinalAlpha());
		GLES20.glUniform1f(muEXTERNAL_OES_Handle, material.Texture.isBindTarget2d());
		
		// 将最终变换矩阵传入shader程序
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(selfModelViewMatrix), 0);
		  //将模型矩阵传入着色器程序
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);

//		if(mLightBuffer != null){
//			//将光源位置传入着色器程序   
//			GLES20.glUniform3fv(maLightLocationHandle, 1, mLightBuffer);
//		}
		
		if (mesh.VertexBuffer != null) {
			// 为画笔指定顶点位置数据
			GLES20.glVertexAttribPointer(maPositionHandle, 3/*该参数表示一组数据有几个数组元素*/, GLES20.GL_FLOAT, false, 3 * 4/*该参数表示一组数据有多少个字节*/, mesh.VertexBuffer.rewind());
			GLES20.glEnableVertexAttribArray(maPositionHandle);
		}
		
		if(mesh.ColorBuffer != null){
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
		}
		
		if (mesh.IndexBuffer != null) {
			mesh.IndexBuffer.rewind();
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.IndexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, mesh.IndexBuffer);
//			GLES20.glDrawElements(GLES20.GL_LINES, mesh.IndexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, mesh.IndexBuffer);
		}
	}

//	protected void goMaterial(MaterialGroup materialGroup, Material material,float te_2d) {
//		GLES20.glUniform1f(muAlphaHandle, 1/*material.getAlpha() * materialGroup.getAlpha() * animator.TransformState.getAlpha()*/);
//		GLES20.glUniform1f(muEXTERNAL_OES_Handle, te_2d);
//	}
}

