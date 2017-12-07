package com.x.opengl.kernel;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.x.Director;

/**
 * 模型数据
 * @date   2013-08-08 11:10:10
 */
public class Mesh {
	
	public static final String TAG = "Mesh";
	
	public static final int    TRIANGLE_TYPE_INDEPENDENT = GL10.GL_TRIANGLES;
	public static final int    TRIANGLE_TYPE_STRIP       = GL10.GL_TRIANGLE_STRIP;
	public static final int    TRIANGLE_TYPE_FAN         = GL10.GL_TRIANGLE_FAN;

	public              String      Name = null; // 网格名称
	public              int         TriangleType = TRIANGLE_TYPE_STRIP;
	public              boolean     Enabled      = true; // 可用状态
	public              FloatBuffer VertexBuffer = null; // 3个值一组 = X Y Z
	public              FloatBuffer CoordBuffer  = null; // 2个值一组 = U V
	public              FloatBuffer ColorBuffer  = null; // 4个值一组 = R G B A
	public              FloatBuffer NormBuffer   = null; // 3个值一组 = X Y Z
	public              ShortBuffer IndexBuffer  = null; // 1个值一组 = I
	
	public Mesh(){
		
	}
	
	public void setVertexes(FloatBuffer vertexBuffer) {
		VertexBuffer = vertexBuffer;
	} 
	
	public void setVertexes(float[] vertexes) {
		VertexBuffer = Director.sResourcer.floatBuffer(vertexes);
	}
	
	public void setCoordinates(FloatBuffer coordBuffer) {
		CoordBuffer = coordBuffer;
	}
	
	public void setCoordinates(float[] coordinates) {
		CoordBuffer = Director.sResourcer.floatBuffer(coordinates);
	}
	
	public void setNormals(FloatBuffer normalBuffer) {
		NormBuffer = normalBuffer;
	}
	
	public void setNormals(float[] normals) {
		NormBuffer = Director.sResourcer.floatBuffer(normals);
	}
	
	public void setIndices(ShortBuffer indexBuffer) {
		IndexBuffer = indexBuffer;
	}
	
	public void setIndices(short[] indexes) {
		IndexBuffer = Director.sResourcer.shortBuffer(indexes);
	}
	
	public void setColors(FloatBuffer colorBuffer) {
		ColorBuffer = colorBuffer;
	}
	
	public void setColors(float[] colors) {
		ColorBuffer = Director.sResourcer.floatBuffer(colors);
	}

	public boolean isEnabled() {
		return Enabled;
	}
	
	public void setEnabled(boolean enabled) {
		Enabled = enabled;
	}
}
