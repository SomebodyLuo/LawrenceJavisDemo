package com.hello.scene_for_vr.mymediaplayerpackage;

import java.io.IOException;

import com.x.Director;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

@SuppressLint("NewApi") 
public class MediaPlayerManager implements SurfaceTexture.OnFrameAvailableListener{

	 public static final String videoPath = Environment.getExternalStorageDirectory().getPath()+"/abc.mp4";
    private MediaPlayer mediaPlayer;
    private boolean frameAvailable = false;
    
    private SurfaceTexture videoTexture;
    public  void playVideo(int texturesid) {
    	
    	
    	if(mediaPlayer != null){
    		mediaPlayer.stop();
    		mediaPlayer.release();
    		mediaPlayer = null;
    		videoTexture.release();
    		videoTexture = null;
    	}
    	
        videoTexture = new SurfaceTexture(texturesid);
        videoTexture.setOnFrameAvailableListener(this  );
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            Surface surface = new Surface(videoTexture);
            mediaPlayer.setSurface(surface);
            surface.release();
            try {
                mediaPlayer.setDataSource(videoPath);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.start();
        }
    }


	public void onPause() {

        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }		
	}


	public void onDestroy() {
		 if (mediaPlayer != null) {
	            mediaPlayer.stop();
	            mediaPlayer.release();
	            mediaPlayer = null;
	        }		
	}


	public void updateTexImage(float[] videoTextureTransform) {
        synchronized (this) {
            if (frameAvailable) {

                frameAvailable = false;
                videoTexture.updateTexImage();
//                videoTexture.getTransformMatrix(videoTextureTransform);	
            }
        }
	}


	public void changeSurface(int texturesid){
		
	}
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
//    	Log.d("ming", "onFrameAvailable = ");
        synchronized (this) {
            frameAvailable = true;
        }
    }

}
