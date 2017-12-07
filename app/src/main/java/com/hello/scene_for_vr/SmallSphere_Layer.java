package com.hello.scene_for_vr;

import android.util.Log;

import com.x.Director;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.MaterialGroup;
import com.x.opengl.kernel.ObjDrawable;
import com.x.opengl.kernel.Texture;
import com.x.components.node.View;
import com.x.components.node.View.OnClickListener;
import com.x.components.node.ViewGroup;

public class SmallSphere_Layer {

	private MyTransView  mSphere;
	private ViewGroup mViewGroup;
	public View getLayer() {
		return mSphere;
	}
	public SmallSphere_Layer() {
		

	//	ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MySphere.obj");
		//	ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/teaport.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/TankWorld.obj");
 //ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MyCube.obj");
		//ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/plane.obj");
		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/teaport.obj");


//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadModelByAssimpAPI(Director.getInstance().getContext(),new String[]{"models/MyModel/rectangle.FBX","models/MyModel/20150805095213114.jpg"});

		Log.d("ming", "finish assembleScene =============== = " );
		mSphere = new MyTransView(objDrawable);
		mSphere.setWidth(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
		mSphere.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
		mSphere.setThickness(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
		mSphere.setTranslate(-EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.25f, -EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f, 0);
		//mSphere.setCullFrontFace(true);
		//mSphere.setScale(0.5f, 0.5f, 0.5f);
 		//mSphere.setRotate(30, 0, 0);
		
		//mSphere.setTranslate(0, 0, 0);
		mSphere.setFocusable(false);
		mSphere.setTouchAble(false);
		//mSphere.setRotateEnable(true);
//		mSphere.setAlpha(0.5f);
//		Director.getInstance().getGLHandler().postDelay(new Runnable() {
//			
//			@Override
//			public void run() {
//				float x = (float) (2*EngineConstanst.REFERENCE_SCREEN_HEIGHT*(Math.random() - 0.5));
//				float y = (float) (2*EngineConstanst.REFERENCE_SCREEN_WIDTH*(Math.random() - 0.5));
//				mSphere.translateTo(x, y, -EngineConstanst.REFERENCE_SCREEN_HEIGHT*2, 400);
//
//				Director.getInstance().getGLHandler().postDelay(this,2000);
//			}
//		},2000);
 	mSphere.setRotateEnable(true);
//		mSphere.setCullFrontFace(true);
//		mSphere.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mSphere.changeState();
//			}
//		});

	}
	private Texture mRestoreTextre;
	public int getVeodioTextureID() {
		MaterialGroup mGroup = ((ObjDrawable)mSphere.getBaseDrawable()).mMaterials;
		for (int i = 0; i < mGroup.size(); i++) {
			Material material = mGroup.getMaterial(i );
			Log.d("ming", "getVeodioTextureID material = "+material.Name);
		}
		if(mGroup.size() > 0){
			Material material = mGroup.getMaterial(0);
			mRestoreTextre = material.Texture;
	        material.Texture = Director.sResourcer.generateOES_Texture();
			return material.Texture.getTextureID();
		}
		return -1;
	} 

	public void restoreTexture() {

		MaterialGroup mGroup = ((ObjDrawable)mSphere.getBaseDrawable()).mMaterials;
		if(mGroup.size() > 0){
			Material material = mGroup.getMaterial(0);
			if(mRestoreTextre != null){
				material.Texture = mRestoreTextre;
			}
		}
	}
	
	public void ddd(){
		
		
//	close all   ; 
//	x=[0:0.1:10]
//	y=[0:0.1:10]
//	[x,y] = meshgrid(x,y) 
//	
//	xx =[0:0.1:10]; 
//	yy = [0:0.001:0.1]; 
//	[xx,yy] = meshgrid(xx,yy) 
//	A = unidrnd(100)/100;
//	B = unidrnd(100)/100;
//	for i = 1:0.3:1000  
//
//
//	hold off 
// 
//	z=  y.*cos(y*0.8-i)+ x.*sin(1.2* x-i);  
//	surf(x,y,z) 
//
//	hold on
//	z=-yy*100
//	surf(xx,yy,z),shading interp  
//	light('position',[-1,-1,120],'style','local')  
//	lighting phong    
//
//	%axis equal
//	axis([0,10,0,10,-100,100])
//	c = [0,1,1];
//	 colormap(c);
//		pause(0.016);
//	end
//	
////////////////////////////////
//	
//	
//	close all   ; 
//	x=[0:0.1:10]
//	y=[0:0.01:1]  
//			
//	[x,y] = meshgrid(x,y)
//	
//	for i = 1:0.3:1000  
//
// 
// 
//	%y=   x*2;  
//	z = y*110;
//	surf(x,y,z)  ,shading interp  
//	light('position',[-1,-1,120],'style','local')  
//	lighting phong    
//
//	%axis equal
//	axis([0,10,0,10,0,100])
//	c = [0,1,1];
//	 colormap(c);
//		pause(0.016);
//	end
//	
//	
//	
///////////////////////////
//	
//	
//	close all   ; 
//	x=[0:0.1:10]
//	y=[0:0.1:10]
//	[x,y] = meshgrid(x,y) 
//	
//	A = unidrnd(100)/100;
//	B = unidrnd(100)/100;
//	for i = 1:0.3:1000  
//
//
//	hold off 
// 
//	z=  y.*cos(y*0.8-i)+ x.*sin(1.2* x-i);  
//	surf(x,y,z) 
//
//	hold on
//	yy = [0:0.01:1];
//	surf(x,[],z),shading interp  
//	light('position',[-1,-1,120],'style','local')  
//	lighting phong    
//
//	%axis equal
//	axis([0,10,0,10,-100,100])
//	c = [0,1,1];
//	 colormap(c);
//		pause(0.016);
//	end
//	
//
//	
//	
//	//[x, y] = [x0, y0] - Sum([waveDirX, waveDirY] * [x(n - 1), y(n - 1) - ([waveDirX, waveDirY] * time * waveLength)
//
//	////////////////////////////////////////////////
//	/////////////////////////////
//	close all   ; 
//	x=[0:0.1:10] 
//			start = unidrnd(100);
//	A=[start:0.1:start+10];
//		 
//		i=0;
//	for i = 1:0.03:1000
//	z=   A.*sin( x-i);  
//
//	plot(x,z); 
//	%axis equal
//	%axis([0,10,0,20,-10,10])
//	pause(0.016);
//	end
//
//	////////////////////////////////////////////////
//	/////////////////////////////
//	close all   ; 
//	x=[0:0.1:10] 
//	A=[10:0.1:20];
//	z=  A.*sin( x);  
//
//	plot(x,z); 
//	%axis equal
//	%axis([0,10,0,20,-10,10])
//	pause(0.016);
//	%end
//	
////////////////////////////////////////////
//	
//
//	close all   ; 
//a=-2*pi:.001:2*pi; %设定角度
//b=(1-sin(a)); %设定对应角度的半径
//polar(a, b,'r') %绘图
//
//
//
//close all   ; 
//a=-2*pi:.001:2*pi; %设定角度
//n =4;
//x = cos(a).^(2.0/n)
//y = sin(a) + cos(a)*i  ;
//left = x.^2
//right = y.^2
//b=left+right; %设定对应角度的半径
//b = sqrt(b);
//y = y;
//plot(y)
//%polar(a, b,'r') %绘图
//
//
//close all   ; 
//a=-2*pi:.001:2*pi; %设定角度
//left = cos(a).*cos(a)
//right = sin(a).*sin(a) 
//b=left+right; %设定对应角度的半径
//b = sqrt(b);
//polar(a, b,'r') %绘图
//
//
//close all   ; 
//[A B] = meshgrid(1:500);
//x = A + B*i;
//w = x.^2 + 2.*x.*i;
//plot(x,w)
//
//
//close all   ; 
//z=1*cplxgrid(30);
//for x = 0:0.1:1000
//cplxmap(z, sin( z.^3+x)  );
//colorbar
//axis([-1,1,-1,1,-3,3])
//pause(0.016);
//end
//
//
//%axis([-1000,1000,-1000,1000,-1000,1000])
//%pause(0.016);
//%end
//
//
//t=0:0.01:2*pi;
//y=t+i*t.*sin(t); %直角坐标表示
//r=abs(y);
//delta=angle(y);%极坐标表示
//subplot(2,1,1)
//plot(y)%绘制直角坐标图
//title('直角坐标图');
//subplot(2,1,2)
//polar(delta,r)%绘制极坐标图
//title('极坐标图')

	}
}
