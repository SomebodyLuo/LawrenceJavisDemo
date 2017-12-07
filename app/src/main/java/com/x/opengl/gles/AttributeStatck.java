package com.x.opengl.gles;

import com.x.opengl.kernel.Rotate;
import com.x.opengl.kernel.Transform;

public class AttributeStatck {

	private static int			stackTop		= -1;

	private static float[]		mAlphaStack		= new float[10];
//	public static Position[]	mPositionStack	= new Position[10];
//	public static Scale[]		mScaleStack		= new Scale[10];
	public static Rotate[]		mRotateStack	= new Rotate[10];

//	private static float		mAlpha			= 1;
//	private static Position		mPosition		= new Position(0,0,0);
//	private static Scale		mScale			= new Scale();
//	private static Rotate		mRotate			= new Rotate();

	public static void push(Transform transform) {
		stackTop++;
		mAlphaStack[stackTop] = transform.Alpha;
//		mScaleStack[stackTop] = transform.Scale;
		mRotateStack[stackTop] = transform.Rotate;
//		mPositionStack[stackTop] = transform.Position;
	}


	public static void pop() {

		// mTempAlpha=mAlphaStack[stackTop];
		stackTop--;
	}

	public static float getFinalAlpha() {
		float alpha = 1;
		for (int i = 0; i <= stackTop; i++) {
			alpha *= mAlphaStack[i];
		}
		return alpha;
	}

//	public static Position getPosition(int i) {
//		return mPositionStack[i];
//	}
//
//	public static Rotate getRotate(int i) {
//		return mRotateStack[i];
//	}
//
//	public static Scale getScale(int i) {
//		return mScaleStack[i];
//	}
//
//	public static Position getPosition() {
//		return mPositionStack[stackTop];
//	}
//
//	public static Rotate getRotate() {
//		return mRotateStack[stackTop];
//	}
//
//	public static Scale getScale() {
//		return mScaleStack[stackTop];
//	}


	public static int getStackSize() {
		return stackTop+1;
	}


	public static Rotate getFinalRotate() {
		Rotate rotate = new Rotate();
		for (int i = 0; i <= stackTop; i++) {
			rotate.add(mRotateStack[i]);
		}
		return rotate;
	}


}
