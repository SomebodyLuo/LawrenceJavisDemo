package com.x.opengl.kernel.sound;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sounder {

	public static int       SOUND_STAR_TOUCH_01  = 0;
	public static int       SOUND_STAR_TOUCH_02  = 0;
	public static int       SOUND_STAR_TOUCH_03  = 0;
	public static int       SOUND_ALL_STAR_TOUCH = 0;
	public static int       SOUND_PICKED_NUMBER  = 0;
	
	public static final int SOUND_LOOP_ONCE      = 0;
	public static final int SOUND_LOOP_FOREVER   = -1;
	
	private Context         mContext             = null;
	private SoundPool       mSoundPool           = null;
	private List<String>    mSoundFiles          = null;
	
	private int             mMaxStreams          = 4;
	private int             mSrcQuality          = 0;
	private int             mSoundPriority       = 1;
	
	public Sounder(Context context) {
		mContext = context;
		initialize();
	}

	private void initialize() {
		mSoundPool = new SoundPool(mMaxStreams, AudioManager.STREAM_MUSIC, mSrcQuality);
	}
	
	/**
	 * 加载一个声音资源
	 * @param assetPath
	 * @return soundID, zero is load failed, non-zero is successed
	 */
	public int loadSound(String assetPath) {
		int result = 0;
		
		if (mSoundPool != null) {
			try {
				result = mSoundPool.load(mContext.getAssets().openFd(assetPath), mSoundPriority);
			} catch (IOException e) {
				e.printStackTrace();
				result = 0;
			}
		}
		
		return result;
	}
	
	/**
	 * 卸载一个声音资源
	 * @param sound is a soundID
	 */
	public void unloadSound(int sound) {
		if (mSoundPool != null) {
			mSoundPool.unload(sound);
		}
	}
	
	/**
	 * 正常模式播放声音
	 * @param sound is a soundID
	 */
	public void play(int sound) {
		if (mSoundPool != null) {
			mSoundPool.play(sound, 1.0f, 1.0f, mSoundPriority, 0, 1);
		}
	}
	
	/**
	 * 播放声音
	 * @param sound is a soundID
	 * @param leftVolume 左声道音量
	 * @param rightVolume 右声道音量
	 * @param loopMode 循环模式
	 * @param speed 播放速率
	 */
	public void play(int sound, int leftVolume, int rightVolume, int loopMode, int speed) {
		if (mSoundPool != null) {
			mSoundPool.play(sound, leftVolume, rightVolume, mSoundPriority, loopMode, speed);
		}
	}
	
	public void destory() {
		if (mSoundPool != null) {
			mSoundPool.unload(SOUND_PICKED_NUMBER);
			mSoundPool.unload(SOUND_STAR_TOUCH_01);
			mSoundPool.unload(SOUND_STAR_TOUCH_02);
			mSoundPool.unload(SOUND_STAR_TOUCH_03);
			mSoundPool.unload(SOUND_ALL_STAR_TOUCH);
			mSoundPool.release();
		}
	}
}
