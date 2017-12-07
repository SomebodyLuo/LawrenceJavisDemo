package com.x.opengl.kernel.particles;

import java.util.HashMap;

import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.Texture;
import com.x.components.node.View;
import com.x.components.node.ViewGroup;
import com.x.components.node.XScene;


/**
 * @hide
 *
 */
public class ParticleSystemScene extends XScene{

	
	private ViewGroup mViewGroup = new ViewGroup();
	private HashMap<Integer,ParticlePackage> mHashMap = new HashMap<Integer,ParticlePackage>();
	private long mStartTime;
	private boolean mPlay;
	public ParticleSystemScene() {

		SetDebugName("ParticleSystemScene");
	}
	@Override
	public void initScene() {


		mViewGroup.setWidth( 202);
		mViewGroup.setHeight( 20);
		mViewGroup.setTranslate(0, 0, -EngineConstanst .REFERENCE_SCREEN_HEIGHT/4);
		addLayer(mViewGroup);
	}
	
	@Override
	protected void onAnimation() {
		super.onAnimation();
		if(mPlay){
			long offsetTime = System.currentTimeMillis() - mStartTime; 
			boolean finish = true;
			for (int i = 0; i < mHashMap.size(); i++) {
				ParticlePackage mParticlePackage = mHashMap.get(i);
				finish &=mParticlePackage.invalidate(offsetTime);
			}
			
			if(finish ){
				mPlay = false;
			}
		}
		
	}
	public  static final int EFFECT_0  = 0;
	public static final int EFFECT_1  = 1;
	public static final int EFFECT_2  = 2;
	public static final int EFFECT_3  = 3;
	private Texture mTexture;
	private int mEffect = EFFECT_0;
	public void playParticleEffect(int Effect ,Texture texture) {

		this.mEffect  = Effect;
		if(texture !=mTexture ){
			mTexture = texture;
		}
		if(mViewGroup.getChildCount() == 0){ 
			for (int i = 0; i < 20; i++) {
				View view1 = new View( );
				view1.setFocusable(false);
				view1.setClickable(false);
				//view1.setBackground(mTexture,false);
				mHashMap .put(i,new ParticlePackage(view1));
				mViewGroup.addChild(view1);
			}
		}
		for (int i = 0; i < mHashMap.size(); i++) {
			ParticlePackage mParticlePackage = mHashMap.get(i);
			mParticlePackage.mView1.setBackground(mTexture,false);
			mParticlePackage.init();
		}
		mStartTime = System.currentTimeMillis();
		mPlay = true;
		postInvalidate();
	}

	class ParticlePackage{

		private View mView1;
		//气泡
		private int xPosition;
		private int yPosition;
		private float xSpeed ;
		private float ySpeed;
		private float yGravity = -9.8f*0;
		private float rotateSpeed ;
		private float xStart;
		private float yStart;
		
		//玫瑰
		private float scale;
		private float angle = 0 ;
		
		public ParticlePackage(View view1) {
			this.mView1 = view1;
		}

		public boolean invalidate(long offsetTime) {
			float t = offsetTime/1000f;
			boolean finish = true;
			switch(mEffect){
			case EFFECT_0:
				break;
			case EFFECT_1:
				angle = t;

				scale =(float) Math.sin(angle);
				if(angle <= Math.PI){
					finish = false;
				}
				if(finish){
					scale = 0;
				}
				mView1.setTranslate(xStart,yStart, 0);
				mView1.setScale(scale, scale, scale);
				break;
			case EFFECT_2:
				break;
			case EFFECT_3:
				xPosition = (int) (xSpeed * t);
				yPosition = (int) (ySpeed * t + 0.5f * yGravity * t * t);
				float y = yStart+ yPosition;
				if(y < (EngineConstanst.REFERENCE_SCREEN_HEIGHT+200)/2){
					//y-=EngineConstanst.REFERENCE_SCREEN_HEIGHT+200;
					finish = false;
				}
				mView1.setTranslate(xStart+xPosition,y, 0);
				mView1.setRotate(0, 0, rotateSpeed * t);
				break;
				default:
					break;
			}
			return finish;
		}

		public void init() {
			switch(mEffect){
			case EFFECT_0:
				break;
			case EFFECT_1:
				angle = 0;
				scale = 0.1f;
				xStart = ((float)Math.random()-0.5f) * EngineConstanst.REFERENCE_SCREEN_WIDTH ;
				yStart = ((float)Math.random()-0.5f) * EngineConstanst.REFERENCE_SCREEN_HEIGHT ;

				float scale =  (float)Math.random()+(float)Math.random(); 
				mView1.setWidth(mTexture.getWidth()  *scale);
				 mView1.setHeight(mTexture.getHeight()*scale );
				break;
			case EFFECT_2:
				break;
			case EFFECT_3:
				float scale2 = (float)Math.random()/2+(float)Math.random()/2; 
				mView1.setWidth(mTexture.getWidth()  *scale2);
				 mView1.setHeight(mTexture.getHeight()*scale2 );
				 
				xPosition = 0 ;
				yPosition = 0;
				xSpeed = (float)( Math.random() - 0.5f ) * 0; 
				ySpeed = (float)( Math.random() + 0.1f ) * 950;
				rotateSpeed = (float)( Math.random() - 0.5f ) * 360;;
				xStart = ((float)Math.random()-0.5f) * EngineConstanst.REFERENCE_SCREEN_WIDTH ;
				yStart = ( -0.8f) * EngineConstanst.REFERENCE_SCREEN_HEIGHT ;
				break;
				default:
					break;
			}
		}
		
	}

}
