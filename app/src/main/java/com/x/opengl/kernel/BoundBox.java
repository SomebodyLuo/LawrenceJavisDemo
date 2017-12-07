package com.x.opengl.kernel;

/**
 * 包围盒
 * @date   2013-10-26 11:03:59
 */
public class BoundBox implements Cloneable{

	private static final String TAG = null;
	private float         mLeft                  = 0;
	private float         mRight                 = 0;
	private float         mTop                   = 0;
	private float         mBottom                = 0;
	private float         mFront                 = 0;
	private float         mBack                  = 0;
	
	private Vector3       mLeftTopBack           = null;
	private Vector3       mLeftTopFront          = null;
	private Vector3       mRightTopBack          = null;
	private Vector3       mRightTopFront         = null;
	private Vector3       mLeftBottomBack        = null;
	private Vector3       mLeftBottomFront       = null;
	private Vector3       mRightBottomBack       = null;
	private Vector3       mRightBottomFront      = null;
	
	public BoundBox() {
		
	}
	
	@Override
	protected BoundBox clone(){
		BoundBox cloned = null;
		try {
			cloned = (BoundBox) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			cloned = new BoundBox();
		}
		cloned.mLeftTopBack      = mLeftTopBack.clone();
		cloned.mLeftTopFront     = mLeftTopFront.clone();
		cloned.mRightTopBack     = mRightTopBack.clone();
		cloned.mRightTopFront    = mRightTopFront.clone();
		cloned.mLeftBottomBack   = mLeftBottomBack.clone();
		cloned.mLeftBottomFront  = mLeftBottomFront.clone();
		cloned.mRightBottomBack  = mRightBottomBack.clone();
		cloned.mRightBottomFront = mRightBottomFront.clone();
		return cloned;
	}
	
	public BoundBox(float left, float right, float top, float bottom, float front, float back) {
		mLeft             = left;
		mRight            = right;
		mTop              = top;
		mBottom           = bottom;
		mFront            = front;
		mBack             = back;
		mLeftTopBack      = new Vector3(left, top, back);
		mLeftTopFront     = new Vector3(left, top, front);
		mRightTopBack     = new Vector3(right, top, back);
		mRightTopFront    = new Vector3(right, top, front);
		mLeftBottomBack   = new Vector3(left, bottom, back);
		mLeftBottomFront  = new Vector3(left, bottom, front);
		mRightBottomBack  = new Vector3(right, bottom, back);
		mRightBottomFront = new Vector3(right, bottom, front);
	}
	
	public void generateBoundBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		mLeft             = minX;
		mRight            = maxX;
		mTop              = maxY;
		mBottom           = minY;
		mFront            = maxZ;
		mBack             = minZ;
		mLeftTopBack      = new Vector3(mLeft, mTop, mBack);
		mLeftTopFront     = new Vector3(mLeft, mTop, mFront);
		mRightTopBack     = new Vector3(mRight, mTop, mBack);
		mRightTopFront    = new Vector3(mRight, mTop, mFront);
		mLeftBottomBack   = new Vector3(mLeft, mBottom, mBack);
		mLeftBottomFront  = new Vector3(mLeft, mBottom, mFront);
		mRightBottomBack  = new Vector3(mRight, mBottom, mBack);
		mRightBottomFront = new Vector3(mRight, mBottom, mFront);
	}
	
	public float[] getBoundVertexes() {
		return new float[]{
				mLeftTopBack.X,      mLeftTopBack.Y,      mLeftTopBack.Z,
				mLeftTopFront.X,     mLeftTopFront.Y,     mLeftTopFront.Z,
				mRightTopBack.X,     mRightTopBack.Y,     mRightTopBack.Z,
				mRightTopFront.X,    mRightTopFront.Y,    mRightTopFront.Z,
				mLeftBottomBack.X,   mLeftBottomBack.Y,   mLeftBottomBack.Z,
				mLeftBottomFront.X,  mLeftBottomFront.Y,  mLeftBottomFront.Z,
				mRightBottomBack.X,  mRightBottomBack.Y,  mRightBottomBack.Z,
				mRightBottomFront.X, mRightBottomFront.Y, mRightBottomFront.Z
		};
	}
	
	public Vector3 getCenter() {
		return new Vector3(
				mLeft + (mRight - mLeft) / 2f,
				mTop  + (mBottom - mTop) / 2f,
				mBack + (mFront - mBack) / 2f
		);
	}
	
	public float[] getCenterArray() {
		return new float[]{
				mLeft + (mRight - mLeft) / 2f,
				mTop  + (mBottom - mTop) / 2f,
				mBack + (mFront - mBack) / 2f
		};
	}
	
	public float getMinX() {
		return mLeft;
	}
	
	public float getMaxX() {
		return mRight;
	}
	
	public float getMinY() {
		return mBottom;
	}
	
	public float getMaxY() {
		return mTop;
	}
	
	public float getMinZ() {
		return mBack;
	}
	
	public float getMaxZ() {
		return mFront;
	}
}
