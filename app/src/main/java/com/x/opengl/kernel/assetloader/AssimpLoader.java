package com.x.opengl.kernel.assetloader;

import com.jni.assimp.AiScene;
import com.jni.assimp.java.JniNativeLoad;


public class AssimpLoader {


	/**
	 * 
	 * @param assetFiles   string数组元素的第一个元素必须为模型文件的地址，其他元素则不限定，但必须保证构成该模型的文件路径依次全部写在这个数组里面。
	 * 
	 */
	public AiScene loadAsset(String[] pathArray) {
	 AiScene aiScene = JniNativeLoad.getObjectScene(pathArray);
	 return aiScene;
	}

}
