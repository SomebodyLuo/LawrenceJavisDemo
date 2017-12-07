package com.jni.assimp.java;

import android.content.res.AssetManager;

import com.jni.assimp.AiScene;


public class JniNativeLoad {

	 public  static  native AiScene getObjectScene(String[] pathArray);

	 public static native void CreateObjectNative(AssetManager assetManager, String pathToInternalDir);
	 public static native void DeleteObjectNative();
    /**
     * load libModelAssimpNative.so since it has all the native functions
     */
	 

	 static {
	        System.loadLibrary("ModelAssimpNative");
	 } 
}
