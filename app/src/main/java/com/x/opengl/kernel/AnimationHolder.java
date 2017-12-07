package com.x.opengl.kernel;

public class AnimationHolder {
	
	private float	mStartValue;
	private float	mTargetValue;
	private float	mPointValue;
	
	private static final int	BEFORE_STATIC	= -1;//静止前夕，该标志位主要用于在动画结束的最后一帧处理一些事情
	private static final int	STATIC	= 0;//静止
	public  static final int	RUNNING	= 1;	 //普通运动方式
	public  static final int	HARMONIC_MOTION	= 2;//简谐运动方式
	public  static final int	HARMONIC_	= 3;//简谐运动方式
	public  static final int	DRAG	= 4;//拖动运动
	public  static final int	DRAG_STOP	= 5;//拖动停止
	public  static final int	DRAG_STOP_ATTRACT = 6;//拖动停止后的牵引自最近目标点
	
	public  static final float  F = 0.3f ; //普通运动方式是加速度 
	public  static final float  F2 = 0.8f;  //简谐运动方式的加速度
	
	public  int	mState = STATIC;
	private float	mSpace;
	private float	mLimiteSpace;
	private float   mF = F; //加速度
	private float   mBete = 0.3f;
	private float	mLimiteMaxSpeed;
	private float	mLimiteMinSpeed;
	
	private boolean mLimitMinEnable; //是否限制最小速度
	private boolean mLimitMaxEnable;//是否限制最大速度
	
	private float	mDragOffset;
	private float	mDragSpeed;
	
	private int		mDragCount;
	private float	mAllDragOffset;
	private float	mDragF;
	private float	mLimiteDragSpeed;
	private float	mTargetIndex;
	

	public AnimationHolder(int index, float space){
		this.mSpace = space;
		this.mStartValue = mSpace * index ;
		this.mTargetValue = 0;
		this.mLimiteSpace = 1;
		this.mState = STATIC;
	}

	public float getCurrentIndex() {
		return mStartValue/mSpace;
	}
	public float getCurrentPara() {
		return mStartValue;
	}
	public float getDestinationIndex() {
		return mTargetIndex;
	}

	public void setCurrentIndex(float index){

		this.mStartValue = mSpace * index;
		this.mTargetValue =  mStartValue;
		this.mF = F;
		this.mState = RUNNING;
	};
	

	public void setDestinationIndex(float index) {

		this.mTargetIndex = index;
		this.mTargetValue =  mSpace * index;
		this.mF = F;
		this.mLimiteMaxSpeed = (mTargetValue - mStartValue) * mF * 0.2f;
		this.mLimiteMinSpeed = (mTargetValue - mStartValue) * mF * 0.2f;
		this.mLimiteSpace = Math.abs(mTargetValue - mStartValue) * mF * 0.1f;
		this.mState = RUNNING;
	}
	public void setDestinationIndex(float index,Parameter parameter) {

		this.mTargetIndex = index;
		this.mTargetValue =  mSpace * index;
		this.mF = F;
		this.mLimiteMaxSpeed = (mTargetValue - mStartValue) * mF * 0.2f;
		this.mLimiteMinSpeed = (mTargetValue - mStartValue) * mF * 0.2f;
		this.mLimiteSpace = Math.abs(mTargetValue - mStartValue) * mF * 0.1f;
		this.mState = RUNNING;
	}
	/**
	 * 
	 * @param index
	 * @param method  运动方式
	 */
	public void setDestinationIndex(float index,int method,float f,boolean limitMin,boolean limitMax) {

		this.mTargetIndex = index;
		this.mTargetValue =  mSpace * index;
		this.mF = f;
		
		this.mLimiteMaxSpeed = (mTargetValue - mStartValue) * mF * 0.2f;
		this.mLimiteMinSpeed = (mTargetValue - mStartValue) * mF * 0.2f;

		this.mLimiteSpace = Math.abs(mTargetValue - mStartValue) * mF * 0.01f;
		this.mPointValue = mTargetValue + (mTargetValue - mStartValue) * mBete;
		
		this.mLimitMinEnable = limitMin;
		this.mLimitMaxEnable = limitMax;
		
		this.mState = method;
		
	}
	public void setCurrentIndex(float index,int method){
		
		this.mStartValue = mSpace * index;
		this.mTargetValue =  mStartValue;
		this.mState = method;
	};

	public void drag(float offset) {
		this.mDragOffset = offset;
		this.mDragCount ++;
		this.mAllDragOffset += mDragOffset;
		this.mStartValue += mDragOffset;
		this.mState = DRAG;
	}
	
	public void dragReset( ) {
		this.mDragOffset = 0;
		this.mDragCount = 0;
		this.mAllDragOffset = 0;
		this.mState = DRAG;
	}

	public void dragStop(float g) {

		this.mDragSpeed = mAllDragOffset / mDragCount * 2;//将初始速度扩大一定倍数
		
		if(mDragSpeed != 0){
			this.mLimiteDragSpeed = EngineConstanst.REFERENCE_MOVE_ATTRACT;
			this.mDragF = g;
			this.mState = DRAG_STOP;
		}
//		this.mLimiteDragSpeed = 10;
		mAllDragOffset = 0;
		mDragCount = 0;
	}

	public void run(Object sourceObject) {
		

//		MLog.d(sourceObject.getClass().getSimpleName() + " running ");
		
		switch (mState)
			{
			case BEFORE_STATIC:

				this.mState = STATIC;
			break;
			case STATIC:
				
				this.mState = STATIC;
			break;
			case RUNNING:
				

				
				float speed = (this.mTargetValue - this.mStartValue) * mF;
				
				if(Math.abs(speed) > Math.abs(mLimiteMaxSpeed)){ 

					if(mLimitMaxEnable){// 如果限制最大速度的标志位启用
						this.mStartValue += mLimiteMaxSpeed;
					}else{
						this.mStartValue += speed;
					}
				}else if(Math.abs(speed) < Math.abs(mLimiteMinSpeed) && Math.abs(this.mTargetValue - this.mStartValue) > Math.abs(mLimiteMinSpeed) ){ //如果速度过小，且离目标值还比较远,则采用限制最小速度 

					if(mLimitMinEnable){// 如果限制最小速度的标志位启用
						this.mStartValue += mLimiteMinSpeed;
					}else{
						this.mStartValue += speed;
					}
				}else{

					this.mStartValue += speed;
				}

				if(speed >= 0 &&  mStartValue > mTargetValue){
					mStartValue = mTargetValue;
					this.mState = BEFORE_STATIC;
					return;
				}else if(speed <= 0 &&  mStartValue < mTargetValue){ 
					mStartValue = mTargetValue;
					this.mState = BEFORE_STATIC;
					return;
				}
			break;

			case HARMONIC_MOTION:
				this.mStartValue += (this.mPointValue - this.mStartValue) * mF;
				
				if(Math.abs(this.mStartValue - this.mPointValue) <= mLimiteSpace * 5){
					this.mStartValue = this.mPointValue;
					this.mPointValue = this.mTargetValue + (mTargetValue - mStartValue) * mBete;
					this.mF = this.mBete;
				}

				if(Math.abs(this.mPointValue - this.mTargetValue) <= mLimiteSpace){
					this.mStartValue = this.mTargetValue;
					this.mState = BEFORE_STATIC;
				}
			break;
			
			case DRAG:
				
				
//				this.mState = BEFORE_STATIC;
				
			break;
			case DRAG_STOP:
				
				this.mStartValue += mDragSpeed;
				mDragSpeed = mDragF * mDragSpeed;
				
				if(Math.abs(mDragSpeed) < Math.abs(mLimiteDragSpeed)){
					this.mState = DRAG_STOP_ATTRACT;
				}
				
			break;
			case DRAG_STOP_ATTRACT:
			break;
			default:
			break;
			}
	}

	public boolean isStatic() {
		return mState == STATIC;
	}
	
	public boolean isRunning() {
		return mState != STATIC;
	}
	
	public class Parameter{

		
		private int	mMethod;
		private float	mF;
		private boolean	mLimitMin;
		private boolean	mLimitMax;

		public Parameter(int method, float f, boolean limitMin, boolean limitMax) {
		
			this.mMethod = method;
			this.mF = f;
			this.mLimitMin = limitMin;
			this.mLimitMax = limitMax;
		}
		
	}

	public int getState() {
		return mState;
	}

	public float getDragSpeed() {
		return mDragSpeed;
	}


	
}
