package com.x.opengl.kernel;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;

import com.x.EngineGLView;
import com.x.opengl.kernel.assetloader.CBResourcer;

/**
 * 异步资源加载辅助器
 * 它会开启一个异步资源加载线程
 * 在有加载请求时，高速运行，在无加载请求时，低速等待
 * 
 * @date   2013-10-31 15:35:55
 */
@SuppressLint("NewApi") public class ASyncResourcerHelper {

	private int                      mServitorCounter          = 0;
	private int                      mRequestIDCounter         = 0;
	private String                   mServitorName             = "ASyncResourcerThread";
	private Thread                   mThrASyncServitor         = null;
	private CBResourcer              mResourcer                = null;
	private ASyncResourcerRunnbale   mRblASyncServitorRunnable = null;
	private LinkedList<ResourceNode> mRequestList              = null;
	private LinkedList<ResourceNode> mResponseList             = null;              
	
	public ASyncResourcerHelper(Context context,  EngineGLView glView) {
		mResourcer = new CBResourcer(context,  glView);
		mRequestList = new LinkedList<ResourceNode>();
		mResponseList = new LinkedList<ResourceNode>();
	}
	
	public void startASyncThread() {
		if (mThrASyncServitor != null) {
			stopASyncThread();
		}
		
		mRblASyncServitorRunnable = getASyncRunnable();
		mThrASyncServitor = new Thread(mRblASyncServitorRunnable);
		mThrASyncServitor.setName(mServitorName + " #" + mServitorCounter++);
		mThrASyncServitor.start();
		mRblASyncServitorRunnable.quit();
	}
	
	public void stopASyncThread() {
		if (mThrASyncServitor != null) {
			if (mRblASyncServitorRunnable != null) {
				mRblASyncServitorRunnable.quit();
			}
			mThrASyncServitor.interrupt();
			mThrASyncServitor = null;
			mRblASyncServitorRunnable = null;
		}
	}
	
	/**
	 * 请求异步加载Res贴图
	 * @param resid
	 * @return 请求ID
	 */
	public int loadTexture(int resid) {
		int requestIDCode = 0;
		
		synchronized (mRequestList) {
			ResourceNode node = new ResourceNode();
			node.UID   = mRequestIDCounter++;
			node.Type  = ResourceNode.RESOURCE_TYPE_RES_TEXTURE;
			node.Key   = resid;
			node.Value = null;
			mRequestList.add(node);
			requestIDCode = node.UID;
		}
		
		return requestIDCode;
	}
	
	/**
	 * 请求异步加载Assets贴图
	 * @param assetFile
	 * @return 请求ID
	 */
	public int loadTexture(String assetFile) {
		int requestIDCode = 0;
		
		synchronized (mRequestList) {
			ResourceNode node = new ResourceNode();
			node.UID   = mRequestIDCounter++;
			node.Type  = ResourceNode.RESOURCE_TYPE_ASSETS_TEXTURE;
			node.Key   = assetFile;
			node.Value = null;
			mRequestList.add(node);
			requestIDCode = node.UID;
		}
		
		return requestIDCode;
	}

	/**
	 * 请求异步加载文字贴图
	 * @param text
	 * @return
	 */
	public int loadTextTexture(String text) {
		int requestIDCode = 0;
		
		synchronized (mRequestList) {
			ResourceNode node = new ResourceNode();
			node.UID   = mRequestIDCounter++;
			node.Type  = ResourceNode.RESOURCE_TYPE_TEXT_TEXTURE;
			node.Key   = text;
			node.Value = null;
			mRequestList.add(node);
			requestIDCode = node.UID;
		}
		
		return requestIDCode;
	}
	
	/**
	 * 请求异步加载OBJ模型
	 * @param assetFile
	 * @return 请求ID
	 */
	public int loadObjModel(String assetFile) {
		int requestIDCode = 0;
		
		synchronized (mRequestList) {
			ResourceNode node = new ResourceNode();
			node.UID   = mRequestIDCounter++;
			node.Type  = ResourceNode.RESOURCE_TYPE_OBJ_MODEL;
			node.Key   = assetFile;
			node.Value = null;
			mRequestList.add(node);
			requestIDCode = node.UID;
		}
		
		return requestIDCode;
	}
	
	/**
	 * 询问
	 * @param requestID
	 * @return
	 */
	public Object enquireRequestedResource(int requestID) {
		Object requestResource = null;
		
		synchronized (mResponseList) {
			Iterator<ResourceNode> itr = mResponseList.descendingIterator();
			
			while (itr.hasNext()) {
				ResourceNode node = itr.next();
				if (node.UID == requestID) {
					requestResource = node;
					break;
				}
			}
		}
		
		return requestResource;
	}
	
	private ASyncResourcerRunnbale getASyncRunnable() {
		return mRblASyncServitorRunnable =  new ASyncResourcerRunnbale() {
			
			final long mShortSleepTime      = 0;
			final long mLongSleepTime       = 200;
			long       mThreadSleepInterval = mShortSleepTime;
			boolean    mRuning              = true;
			
			@Override
			public void run() {
				
				ResourceNode node = null;
				
				while (mRuning) {
					
					// 取出请求
					synchronized (mRequestList) {
						if (!mRequestList.isEmpty()) {
							mThreadSleepInterval = mShortSleepTime;
							node = mRequestList.remove();
						}else{
							mThreadSleepInterval = mLongSleepTime;
						}
					}

					// 加载资源
					switch(node.Type) {
					case ResourceNode.RESOURCE_TYPE_UNKOWN:
						// 直接返回该请求
						break;

					case ResourceNode.RESOURCE_TYPE_RES_TEXTURE:
						node.Value = mResourcer.generateTexture((Integer)node.Key);
						break;

					case ResourceNode.RESOURCE_TYPE_TEXT_TEXTURE:
						node.Value = mResourcer.generateTextTexture((String)node.Key);
						break;

					case ResourceNode.RESOURCE_TYPE_ASSETS_TEXTURE:
						try {
							node.Value = mResourcer.generateTexture(BitmapFactory.decodeStream(mResourcer.getContext().getAssets().open((String)node.Key)));
						} catch (IOException exp) {
							node.Value = null;
						}
						break;

					case ResourceNode.RESOURCE_TYPE_OBJ_MODEL:
//						node.Value = mResourcer.loadObjModel((String)node.Key);
						break;
					}
					
					// 派送回应
					synchronized (mResponseList) {
						if (node != null) {
							mResponseList.add(node);
						}
					}

					// 线程休眠
					try {
						Thread.sleep(mThreadSleepInterval);
					}catch(Exception exp) {
						// 被唤醒
					}
					
				}
					
			}
			
			@Override
			public void quit() {
				mRuning = false;
			}
			
		};
	}
	
	/**
	 * 需要加载的资源封装体
	 * @date      
	 */
	private class ResourceNode {
		public static final int RESOURCE_TYPE_UNKOWN         = 0x0000;
		public static final int RESOURCE_TYPE_RES_TEXTURE    = 0x0001;
		public static final int RESOURCE_TYPE_TEXT_TEXTURE   = 0x0002;
		public static final int RESOURCE_TYPE_ASSETS_TEXTURE = 0x0003;
		public static final int RESOURCE_TYPE_OBJ_MODEL      = 0x0004;
		
		public int    UID   = 0;
		public int    Type  = RESOURCE_TYPE_UNKOWN;
		public Object Key   = null;
		public Object Value = null;
	}
	
	/**
	 * 异步资源加载运行体
	 * @date   2013-10-31 16:14:18
	 */
	private interface ASyncResourcerRunnbale extends Runnable{
		void quit();
	}
}
