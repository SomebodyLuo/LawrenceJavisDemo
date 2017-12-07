package com.x.opengl.kernel.sound;

import java.util.ArrayList;
import java.util.List;

import android.media.audiofx.Visualizer;

import com.x.EngineGLView;

/**
 * 视觉数据提供器
 * @date   2014-02-27 14:57:08
 */
public class VisualizorObserver implements Visualizer.OnDataCaptureListener{
	
	protected static final String TAG = null;
	private Visualizer                mVisualizer              = null;
	private byte[]                    mWaveBuffer              = null;                           // 波形数据
	private int                       mAudioSessionId          = 0;                              // 音频会话ID
	private int                       mRefleshRate             = Visualizer.getMaxCaptureRate(); // 可视化组件刷新频率(mHZ)
	private int                       mCaptureSize             = 512;                            // 可视化组件缓存
	private VisualizorStyle           mVisualizorStyle         = VisualizorStyle.WAVE;           // 可视化组件的样式
	private List<OnDataFlushListener> mOnDataFlushListeners    = new ArrayList<OnDataFlushListener>();
	private EngineGLView                 mWolfView               = null;
	private static VisualizorObserver me                       = null;
	
	public interface OnDataFlushListener {
		void onDataFlushListener(byte[] buffer, VisualizorStyle bufferStyle);
	}
	
	public enum VisualizorStyle{
		FFT,
		WAVE
	}
	
	public static VisualizorObserver getInstance() {
		if (me == null) {
			me = new VisualizorObserver();
		}
		
		return me;
	}
	
	private VisualizorObserver() {
		
	}
	
	/**
	 * 重建可视化组件
	 * @param audioSessionId
	 */
	public void createVisualizer(){
		if(mVisualizer == null){
			mVisualizer = new Visualizer(mAudioSessionId);
		}

		switch (mVisualizorStyle) {
		case FFT:
			try {
				mVisualizer.setCaptureSize(getClosestCaptureSize(mCaptureSize));
			} catch (IllegalStateException exp) {
				//TODO
			}
			mVisualizer.setDataCaptureListener(this, mRefleshRate, false, true);
			mVisualizer.setEnabled(true);
			mWaveBuffer = null;
			break;

		case WAVE:
			try {
				mVisualizer.setCaptureSize(getClosestCaptureSize(mCaptureSize));
			} catch (IllegalStateException exp) {
				//TODO
			}
			mVisualizer.setDataCaptureListener(this, mRefleshRate, true, false);
			mVisualizer.setEnabled(true);
			mWaveBuffer = null;
			break;
		}
	}

	/**
	 * 释放可视化组件
	 */
	public void releaseVisualizer(){
		if(mVisualizer != null){
			mVisualizer.setEnabled(false);
			mVisualizer.release();
			mVisualizer = null;
		}
	}
	
	/**
	 * 设置可视化组件的样式
	 * @param style IMAGE_VIEW 显示图片
	 *              FFT        显示频谱仪
	 *              WAVE       显示波形
	 */
	public void setVisualizorStyle(VisualizorStyle style){
		mVisualizorStyle = style;
	}

	/**
	 * 设置音频会话ID
	 * 如果id = 0，则为声卡的最终混合的输出
	 */
	public void setAudioSessionId(int audioSessionId){
		mAudioSessionId = audioSessionId;
		mAudioSessionId = mAudioSessionId < 0 ? 0 : mAudioSessionId;
	}
	
	/**
	 * 重新指定音频会话ID
	 * 如果id = 0，则为声卡的最终混合的输出
	 */
	public void redirectAudioSessionId(int audioSessionId){
		if(audioSessionId != mAudioSessionId){
			mAudioSessionId = audioSessionId;
			releaseVisualizer();
			createVisualizer();
			enableVisualizer();
		}
	}
	
	/**
	 * 启用可视化组件
	 * @param enable
	 */
	public void enableVisualizer(){
		if(mVisualizer == null){
			createVisualizer();
		}
		mVisualizer.setEnabled(true);
	}
	
	/**
	 * 停用可视化组件
	 * @param enable
	 */
	public void disableVisualizer(){
		mVisualizer.setEnabled(false);
	}
	
	/**
	 * 获取最接近的采样大小
	 * @param sampleSize 样本
	 * @return
	 */
	private int getClosestCaptureSize(int sampleSize) {
		int   minDiff              = 999999999;
		int   closetCaptureSize    = sampleSize;
		int[] supportedCaptureSize = Visualizer.getCaptureSizeRange();
		
		for (int i = 0; i < supportedCaptureSize.length; i++) {
			int currentDiff = Math.abs(sampleSize - supportedCaptureSize[i]);
			if (currentDiff < minDiff) {
				closetCaptureSize = supportedCaptureSize[i];
				minDiff = currentDiff;
			}
		}
		
		return closetCaptureSize;
	}
	
	@Override
	public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
		mWaveBuffer = waveform;
		
		if (mWolfView != null) {
			mWolfView.queueEvent(new Runnable() {
				
				@Override
				public void run() {
					int size = mOnDataFlushListeners.size();
					for (int i = 0; i < size; i++) {
						if (mOnDataFlushListeners.get(i) != null) {
							mOnDataFlushListeners.get(i).onDataFlushListener(mWaveBuffer, VisualizorStyle.WAVE);
						}
					}
				}
			});
		}else{
			int size = mOnDataFlushListeners.size();
			for (int i = 0; i < size; i++) {
				if (mOnDataFlushListeners.get(i) != null) {
					mOnDataFlushListeners.get(i).onDataFlushListener(mWaveBuffer, VisualizorStyle.WAVE);
				}
			}
		}
	}

	@Override
	public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
		mWaveBuffer = fft;

		if (mWolfView != null) {
			mWolfView.queueEvent(new Runnable() {

				@Override
				public void run() {
					int size = mOnDataFlushListeners.size();
					for (int i = 0; i < size; i++) {
						if (mOnDataFlushListeners.get(i) != null) {
							mOnDataFlushListeners.get(i).onDataFlushListener(mWaveBuffer, VisualizorStyle.FFT);
						}
					}
				}
			});
		}else{
			int size = mOnDataFlushListeners.size();
			for (int i = 0; i < size; i++) {
				if (mOnDataFlushListeners.get(i) != null) {
					mOnDataFlushListeners.get(i).onDataFlushListener(mWaveBuffer, VisualizorStyle.FFT);
				}
			}
		}
	}
	
	public void setOnDataFlushListener(OnDataFlushListener listener) {
		mOnDataFlushListeners.add(listener);
	}
}
