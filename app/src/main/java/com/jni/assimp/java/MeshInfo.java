package com.jni.assimp.java;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshInfo {

    public String mName;
    public int     numberOfFaces;
    public ShortBuffer  mIndices;
    public FloatBuffer  mVertexes ;
    public FloatBuffer  mTextures;
    public FloatBuffer  mNormals ;
    public int mMaterialIndex;//mesh对象本身带有的MaterialIndex，即材质索引
    
     
    
    
	public float  minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
	public float  maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;

    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 4;
    
    public void setIndiceBuffer(short[] sArray){ 
        
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(sArray.length * BYTES_PER_SHORT);  
		byteBuffer.order(ByteOrder.nativeOrder());  
		mIndices = byteBuffer.asShortBuffer();  
		mIndices.put(sArray);  
		mIndices.position(0);  
		
    }
    public void setVertexBuffer(float[] floatArray){

    	mVertexes = ByteBuffer.allocateDirect(floatArray .length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
    	mVertexes.put(floatArray).position(0);
    }
    public void setNormalBuffer(float[] floatArray){

    	mNormals = ByteBuffer.allocateDirect(floatArray .length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
    	mNormals.put(floatArray).position(0);
    }
    public void setTextureCoordBuffer(float[] floatArray){

    	mTextures = ByteBuffer.allocateDirect(floatArray .length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
    	mTextures.put(floatArray).position(0);
//        mCubeColors = ByteBuffer.allocateDirect(ai.cubeColor.length * BYTES_PER_FLOAT)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mCubeColors.put(ai.cubeColor).position(0);
    }
    
    
}
