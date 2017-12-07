package com.x.opengl.kernel.assetloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.util.Log;

import com.jni.assimp.AiScene;
import com.jni.assimp.java.DecoderManager;
import com.jni.assimp.java.MaterialInfo;
import com.jni.assimp.java.MeshInfo;
import com.x.Director;
import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.MaterialGroup;
import com.x.opengl.kernel.Mesh;
import com.x.opengl.kernel.ObjDrawable;
import com.x.opengl.kernel.Texture;
import com.x.opengl.kernel.assetloader.ObjLoader.MaterialPackage;
import com.x.opengl.kernel.assetloader.ObjLoader.MeshPackage;

public class Model {

	public static  ObjDrawable assembleObj(ObjLoader objLoader) {
//		mProduct = new View(new ObjDrawable(-1));
//		ObjDrawable objDrawable = (ObjDrawable) mProduct.getBaseDrawable();
		ObjDrawable mTheObjDrawable =  new ObjDrawable(0);
		// 组装Mesh
		int meshSize = objLoader.mMeshes.size();
		mTheObjDrawable = new ObjDrawable(-1);
		mTheObjDrawable.mMeshes = new Mesh[meshSize];
		for (int i = 0; i < meshSize; i++) {
			MeshPackage meshPackage = objLoader.mMeshes.get(i);
//			Log.d("bound", " =======assembleGameObject========== ");
//			Log.d("bound", "minX = " + meshPackage.minX + ",minY = "+meshPackage.minY + ",minZ = "+meshPackage.minZ);
//			Log.d("bound", "maxX = " + meshPackage.maxX + ",maxY = "+meshPackage.maxY + ",maxZ = "+meshPackage.maxZ);
//			Log.d("bound", " ================= ");
			mTheObjDrawable.addAABBBoxPoint(meshPackage.minX,meshPackage.minY,meshPackage.minZ,meshPackage.maxX,meshPackage.maxY,meshPackage.maxZ);
			
			mTheObjDrawable.mMeshes[i] = new Mesh();
			if (meshPackage.mVertexes != null) {
				mTheObjDrawable.mMeshes[i].setVertexes(meshPackage.mVertexes);
			}
			if (meshPackage.mNormals != null) {
				mTheObjDrawable.mMeshes[i].setNormals(meshPackage.mNormals);
			}
			if (meshPackage.mTextures != null) {
				mTheObjDrawable.mMeshes[i].setCoordinates(meshPackage.mTextures);
			}
			if (meshPackage.mIndices != null) {
				mTheObjDrawable.mMeshes[i].setIndices(meshPackage.mIndices);
			}
			
			if (meshPackage.mName != null) {
				mTheObjDrawable.mMeshes[i].Name = meshPackage.mName;
//				Log.d("debug", "meshPackage.mName = "+meshPackage.mName );
			}
		}

		// 组装Material
		if (objLoader.mMaterials != null && objLoader.mMaterials.size() > 0) {
			int materialSize = objLoader.mMaterials.size();
			mTheObjDrawable.mMaterials = new MaterialGroup();
			for (int i = 0; i < materialSize; i++) {
				MaterialPackage materialPackage = objLoader.mMaterials.get(i);
				Material material = new Material();
				material = materialPackage.mMaterial;
				material.Name = materialPackage.mName;
				mTheObjDrawable.mMaterials.addMaterial(material);
				Log.d("ming", "material.mName = "+material.Name );
			}
		}
		
		return mTheObjDrawable;
	}

	public static ObjDrawable assembleScene(Context context ,AiScene aiScene) {
		DecoderManager dm = new DecoderManager();
		dm.decodeGlBuffer(context, aiScene);
		
		ObjDrawable mTheObjDrawable =  new ObjDrawable(0);
		
		// 组装Mesh
		int meshSize = dm.meshInfoStacks.size();
		mTheObjDrawable = new ObjDrawable(-1);
		mTheObjDrawable.mMeshes = new Mesh[meshSize];
		for (int i = 0; i < meshSize; i++) {
			MeshInfo meshPackage = dm.meshInfoStacks.get(i);
//			Log.d("bound", " =======assembleGameObject========== ");
//			Log.d("bound", "minX = " + meshPackage.minX + ",minY = "+meshPackage.minY + ",minZ = "+meshPackage.minZ);
//			Log.d("bound", "maxX = " + meshPackage.maxX + ",maxY = "+meshPackage.maxY + ",maxZ = "+meshPackage.maxZ);
//			Log.d("bound", " ================= ");
			mTheObjDrawable.addAABBBoxPoint(meshPackage.minX,meshPackage.minY,meshPackage.minZ,meshPackage.maxX,meshPackage.maxY,meshPackage.maxZ);
			
			mTheObjDrawable.mMeshes[i] = new Mesh();
			if (meshPackage.mVertexes != null) {
				mTheObjDrawable.mMeshes[i].setVertexes(meshPackage.mVertexes);
			}
			if (meshPackage.mNormals != null) {
				mTheObjDrawable.mMeshes[i].setNormals(meshPackage.mNormals);
			}
			if (meshPackage.mTextures != null) {
				mTheObjDrawable.mMeshes[i].setCoordinates(meshPackage.mTextures);
			}
			if (meshPackage.mIndices != null) {
				mTheObjDrawable.mMeshes[i].setIndices(meshPackage.mIndices);
			}
			
//			if (meshPackage.mName != null) {
//				mTheObjDrawable.mMeshes[i].Name = meshPackage.mName;
				//assimp解析的mesh不带名字，所以使用materialIndex代替
				mTheObjDrawable.mMeshes[i].Name =  String.valueOf(meshPackage.mMaterialIndex);
//				Log.d("debug", "meshPackage.mName = "+meshPackage.mName );
//			}
		}
		// 组装Material
		int materialSize = dm.materialInfoStacks.size();
		
		mTheObjDrawable.mMaterials = new MaterialGroup();
		
		for (int i = 0; i < meshSize; i++) {
			if(i <= dm.materialInfoStacks.size() -1){
				
				MaterialInfo materialInfo = dm.materialInfoStacks.get(i);
				Material material = new Material();
				material.Texture = materialInfo.mTexture;
				material.Name = String.valueOf(materialInfo.mMaterialTextureIndex);//assimp解析的material不带名字，故用index代替
				mTheObjDrawable.mMaterials.addMaterial(material);
				Log.d("ming", "material.mName = "+material.Name );
			}else{//如果物体没有材质，则给他一个默认纹理材质

				Material material = new Material();
				material.Texture = getDefalutTexture();
				material.Name = String.valueOf(mTheObjDrawable.mMeshes[i].Name);//assimp解析的material不带名字，故用index代替
				mTheObjDrawable.mMaterials.addMaterial(material);
			}
		}
//		for (int i = 0; i < materialSize; i++) {
//			MaterialInfo materialInfo = dm.materialInfoStacks.get(i);
//			
//		}
		
		return mTheObjDrawable;
	}

	public static Texture getDefalutTexture(){

			//如果没有纹理图，则使用材质颜色构造一张纹理
			int c   = Color.parseColor("#44ffffff");
			int widthe = 100;
			int height = 100;
			Bitmap bitmap = Bitmap.createBitmap(widthe, height, Config.ARGB_8888);
			bitmap.eraseColor(c);
			Paint p = new Paint();
			p.setColor( Color.RED);
			p.setTextSize(widthe * 0.05f);
			p.setTextAlign(Align.CENTER);
			Canvas can = new Canvas(bitmap);
			can.drawText("光能蜗牛from assimp", widthe/2, height/2+p.ascent()/2, p);
			return Director.getInstance().sResourcer.generateTexture(bitmap);
	}
}
