package com.x.opengl.gles;



public class StencilValueStatck {

	private static int			stackTop		= 0;
	private static int			parentStack		= 0;


	
	public static void push() {
		stackTop++;
	}

	public static void pop() {

		// mTempAlpha=mAlphaStack[stackTop];
		stackTop--;
	}


	public static int getStackSize() {
		return stackTop;
	}

	public static void pushParent() {
		parentStack++ ;
	}
	public static void popParent() {
		parentStack-- ;
	}
	public static int getParentLevel() {
		return parentStack;
	}

	public static void clear() {
		stackTop = 0;
		parentStack = 0 ;
	}



}
