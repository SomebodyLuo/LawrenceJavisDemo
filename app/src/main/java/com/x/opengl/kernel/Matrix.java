package com.x.opengl.kernel;

/**
 * 三维变换用4x4矩阵
 * @date   2013-10-11 15:37:59
 */
public class Matrix {
	
	public static final String TAG = "Matrix";
	
    private float[] mMatrix = new float[16];
 
    public Matrix() {
    	reset();
    }
    
    public Matrix(Matrix src) {
    	set(src);
    }
    
    public Matrix(float[] src) {
    	set(src);
    }
    
    /**
     * 重置矩阵为单位矩阵
     */
	public void reset() {
		android.opengl.Matrix.setIdentityM(mMatrix, 0);
	}

	/**
	 * 复制src矩阵的内容，如果src为空，则生成单位矩阵
	 * @param matrix
	 */
	public void set(Matrix src) {
		if (src == null) {
			reset();
		}else{
			float[] srcMatrix = src.get();
			for (int i = 0; i < 16; i++) {
				mMatrix[i] = srcMatrix[i];
			}
		}
	}
	
	public void set(float[] src) {
		if (src == null) {
    		reset();
    	}else if (src.length == 9) {
    		mMatrix[ 0] = src[0];
    		mMatrix[ 1] = src[1];
    		mMatrix[ 2] = src[2];
    		mMatrix[ 3] = 0;
    		mMatrix[ 4] = src[3];
    		mMatrix[ 5] = src[4];
    		mMatrix[ 6] = src[5];
    		mMatrix[ 7] = 0;
    		mMatrix[ 8] = src[6];
    		mMatrix[ 9] = src[7];
    		mMatrix[10] = src[8];
    		mMatrix[11] = 0;
    		mMatrix[12] = 0;
    		mMatrix[13] = 0;
    		mMatrix[14] = 0;
    		mMatrix[15] = 1;
    	}else if(src.length == 16) {
    		for (int i = 0; i < 16; i++) {
    			mMatrix[i] = src[i];
    		}
    	}else{
    		reset();
    	}
	}
	
	/**
	 * 获取矩阵序列
	 * @return
	 */
	public float[] get() {
		return mMatrix;
	}

	/**
	 * 矩阵右乘 M' = M * other
	 * @param matrix
	 */
	public void preConcat(Matrix other) {
		if (other != null) {
			float[] result = new float[16];
			android.opengl.Matrix.multiplyMM(result, 0, mMatrix, 0, other.get(), 0);
			for (int i = 0; i < 16; i++) {
				mMatrix[i] = result[i];
			}
		}
	}

	/**
	 * 矩阵左乘 M' = other * M
	 * @param matrix
	 */
	public void postConcat(Matrix other) {
		if (other != null) {
			float[] result = new float[16];
			android.opengl.Matrix.multiplyMM(result, 0, other.get(), 0, mMatrix, 0);
			for (int i = 0; i < 16; i++) {
				mMatrix[i] = result[i];
			}
		}
	}
	
	/**
	 * 右乘向量 M' = M * vector
	 * @param vector
	 */
	public Vector3 concatVector(Vector3 vector) {
		if (vector != null) {
			float[] result = new float[4];
			android.opengl.Matrix.multiplyMV(result, 0, mMatrix, 0, new float[]{vector.X, vector.Y, vector.Z, 1}, 0);
			
			if (result[3] == 0) {
				return new Vector3(result[0], result[1], result[2]);
			}else{
				return new Vector3(result[0] / result[3], result[1] / result[2], result[2] / result[3]);
			}
		}
		
		return null;
	}

	/**
	 * 设置平移
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setTranslate(float dx, float dy, float dz) {
		float[] result = new Matrix().get();
		
		result[12] = dx;
		result[13] = dy;
		result[14] = dz;
	}
	
	public void setScale(float dx, float dy, float dz) {
		float[] result = new Matrix().get();
		
		result[ 0] = dx;
		result[ 5] = dy;
		result[10] = dz;
	}
	
	public void setRotate(float angle, float ax, float ay, float az) {
		float[] result = new Matrix().get();
		
		float xx = ax * ax;
		float xy = ax * ay;
		float xz = ax * az;
		float yy = ay * ay;
		float yz = ay * az;
		float zz = az * az;
		float sinAngle = (float) Math.sin(angle);
		float cosAngle = (float) Math.cos(angle);
		float complementaryCosAngle = 1 - cosAngle;
		
		result[ 0] = xx * complementaryCosAngle + cosAngle;
		result[ 1] = xy * complementaryCosAngle + az * sinAngle;
		result[ 2] = xz * complementaryCosAngle - ay * sinAngle;
		result[ 3] = 0;
		result[ 4] = xy * complementaryCosAngle - az * sinAngle;
		result[ 5] = yy * complementaryCosAngle + cosAngle;
		result[ 6] = yz * complementaryCosAngle + ax * sinAngle;
		result[ 7] = 0;
		result[ 8] = xz * complementaryCosAngle + ay * sinAngle;
		result[ 9] = yz * complementaryCosAngle - ax * sinAngle;
		result[10] = zz * complementaryCosAngle + cosAngle;
		result[11] = 0;
		result[12] = 0;
		result[13] = 0;
		result[14] = 0;
		result[15] = 1;
	}
	
	/**
	 * 逆
	 */
	public void invert() {
		set(invert(this).get());
	}
	
	/**
	 * 矩阵的逆
	 * @param matrix
	 * @return
	 */
	public static Matrix invert(Matrix matrix) {
		float[] invArray = new float[16];
		
		android.opengl.Matrix.invertM(invArray, 0, matrix.get(), 0);
		
		return new Matrix(invArray);
	}
}
