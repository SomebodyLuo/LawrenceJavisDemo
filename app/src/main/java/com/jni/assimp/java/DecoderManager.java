package com.jni.assimp.java;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.jni.assimp.AiScene;
import com.jni.assimp.material.AiMaterialTexture;
import com.jni.assimp.mesh.AiFace;
import com.jni.assimp.mesh.AiMesh;
import com.jni.assimp.mesh.AiVector3D;
import com.x.Director;
import com.x.opengl.kernel.Texture;

public class DecoderManager {


	public List<MeshInfo> meshInfoStacks = new ArrayList<MeshInfo>();
	public List<MaterialInfo> materialInfoStacks = new ArrayList<MaterialInfo>();
	
	public void decodeGlBuffer(Context context,AiScene scene) {
		



	    // For every mesh -- load face indices, vertex positions, vertex texture coords
	    // also copy texture index for mesh into newMeshInfo.textureIndex
	    for (  int n = 0; n < scene.mNumMeshes; ++n) {

		    FloatBuffer buffer;
			MeshInfo newMeshInfo = new MeshInfo();; // this struct is updated for each mesh in the model
	    	
	    	 ArrayList<AiMesh> meshList = scene.mMeshes.mMeshs;
	    	 AiMesh mesh = meshList.get(n);
	        // create array with faces
	        // convert from Assimp's format to array for GLES
	    	 short[] faceArray = new  short[mesh.mNumFaces * 3];
	         int faceIndex = 0;
	        for (int t = 0; t < mesh.mNumFaces; ++t) {
	            // read a face from assimp's mesh and copy it into faceArray
	            AiFace  face =  mesh.mFaces[t];
	            faceArray[faceIndex] = (short) face.mIndices[0];
	            faceArray[faceIndex+1] =(short) face.mIndices[1];
	            faceArray[faceIndex+2] =(short) face.mIndices[2];
	            faceIndex += 3;

	        }
	        newMeshInfo.numberOfFaces = mesh.mNumFaces;

	        // buffer for faces
	        if (newMeshInfo.numberOfFaces != 0) {
	            newMeshInfo.setIndiceBuffer(faceArray);
	        }

	        // buffer for vertex positions
	        if (mesh.HasPositions()) {
	        	AiVector3D[] aiVector3d = mesh.mVertices;
	        	float[] vertexBuffer = new float[aiVector3d.length * 3];
	        	int vertexIndex = 0 ;
	        	for (int i = 0; i < aiVector3d.length; i++) {
	        		vertexBuffer[vertexIndex+0] = aiVector3d[i].X;
	        		vertexBuffer[vertexIndex+1] = aiVector3d[i].Y;
	        		vertexBuffer[vertexIndex+2] = aiVector3d[i].Z;
	        		vertexIndex +=3;
				}
	            newMeshInfo.setVertexBuffer(vertexBuffer);
	        }
	        
	        if (mesh.HasNormals()) {
	        	AiVector3D[] aiVector3d = mesh.mNormals;
	        	float[] vertexBuffer = new float[aiVector3d.length * 3];
	        	int normalIndex = 0 ;
	        	for (int i = 0; i < aiVector3d.length; i++) {
	        		vertexBuffer[normalIndex+0] = aiVector3d[i].X;
	        		vertexBuffer[normalIndex+1] = aiVector3d[i].Y;
	        		vertexBuffer[normalIndex+2] = aiVector3d[i].Z;
	        		normalIndex +=3;
				}
	            newMeshInfo.setNormalBuffer(vertexBuffer);
	        } 


	        // buffer for vertex texture coordinates
	        // ***ASSUMPTION*** -- handle only one texture for each mesh
	        if (mesh.HasTextureCoords(0)) {

	            float[]  textureCoords = new float[2 * mesh.mNumVertices];
	            for (int k = 0; k < mesh.mNumVertices; ++k) {
	                textureCoords[k * 2] 	 = mesh.mTextureCoords[0].mTextureCoords[k].X;
	                textureCoords[k * 2 + 1] = mesh.mTextureCoords[0].mTextureCoords[k].Y;
	            }
	            newMeshInfo.setTextureCoordBuffer(textureCoords);
	        }
	        newMeshInfo.mMaterialIndex = mesh.mMaterialIndex;

//	        // unbind buffers
//	        glBindBuffer(GL_ARRAY_BUFFER, 0);
//	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//
//	        // copy texture index (= texture name in GL) for the mesh from textureNameMap
//	        aiMaterial *mtl = scene->mMaterials[mesh->mMaterialIndex];
//	        aiString texturePath;	//contains filename of texture
//	        if (AI_SUCCESS == mtl->GetTexture(aiTextureType_DIFFUSE, 0, &texturePath)) {
//	            unsigned int textureId = textureNameMap[texturePath.data];
//	            newMeshInfo.textureIndex = textureId;
//	        } else {
//	            newMeshInfo.textureIndex = 0;
//	        }

			meshInfoStacks.add(newMeshInfo);
	    }

	    int n = 0;
        if(scene.mMaterials != null && scene.mMaterials.mAiMaterialTextures != null){
        	n = scene.mMaterials.mAiMaterialTextures.length;
        }
	    for (int i = 0; i < n; i++) {
	    	
	    	AiMaterialTexture aiMatreialTexture = scene.mMaterials.mAiMaterialTextures[i];
	    	MaterialInfo materilaInfo = new MaterialInfo();
        	Bitmap bitmap = BitmapFactory.decodeFile(aiMatreialTexture.mTextureFullpath, null);
//        	Bitmap bitmap = Bitmap.createBitmap(16, 16, Config.ARGB_8888);
//        	bitmap.eraseColor(Color.RED);
//        	materilaInfo.mTexture  = geTexture(bitmap, false);
        	materilaInfo.mTexture  = Director.sResourcer.generateTexture(bitmap, false);
        	materilaInfo.mMaterialTextureIndex = aiMatreialTexture.mMaterialTextureIndex ;
        	
        	materialInfoStacks.add(materilaInfo);
		}
		
	}

    @SuppressLint("NewApi") 
    private Texture  geTexture(Bitmap bmp,boolean recycleLast ){

//		Log.d("debug","generateTexture" );
		// 生成纹理ID
		Texture texture = new Texture();
		int[] textureID = new int[1];
		GLES20.glGenTextures(1, // 产生的纹理id的数量
				textureID, // 纹理id的数组
				0 // 偏移量
		);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
 
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
}
