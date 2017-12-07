package com.x.opengl.kernel;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Looper;
import android.util.Log;

public class GLHandler {

    private boolean died;

    private Queue<TaskInfo> mAddQueue = new LinkedBlockingQueue<>();

    private Queue<TaskInfo> mWorkQueue = new LinkedBlockingQueue<>();

    private final Object addLock = new Object();

    private class TaskInfo{

		public long startTime;
		public long delayTime;
		public Runnable runnable;
		public boolean done;
		public boolean tryRun() {
			if(System.currentTimeMillis() - startTime > delayTime){
				runnable.run();
				return true;
			}
			return false;
		}
    	
    }
    public GLHandler() {
    }

    public void postDelay(Runnable runnable,long time){
        // destroyed ?
        if (died){
            return;
        }
        TaskInfo ti = new TaskInfo();
        ti.startTime = System.currentTimeMillis();
        ti.delayTime = time;
        ti.runnable = runnable;

        mAddQueue.remove(ti);
        mAddQueue.offer(ti);
//        if (Looper.getMainLooper() == Looper.myLooper()){
//            synchronized (addLock){
//                mAddQueue.remove(ti);
//                mAddQueue.offer(ti);
//            }
//        } else {
//        	ti.tryRun();
//        }

    }

    public void post(Runnable runnable){
        // destroyed ?
        if (died){
            return;
        }

        TaskInfo ti = new TaskInfo();
        ti.startTime = System.currentTimeMillis();
        ti.delayTime = 0;
        ti.runnable = runnable;

        mAddQueue.remove(ti);
        mAddQueue.offer(ti);
//        if (Looper.getMainLooper() == Looper.myLooper()){
//            synchronized (addLock){
//                mAddQueue.remove(ti);
//                mAddQueue.offer(ti);
//            }
//        } else {
//        	ti.tryRun();
//        }

    }

    // gl thread
    public void dealMessage(){
        synchronized (addLock){
            mWorkQueue.addAll(mAddQueue);
            mAddQueue.clear();
        }

        Log.d("ming", "mWorkQueue "+mWorkQueue.size());
        

        Queue<TaskInfo> tempQueue = new LinkedBlockingQueue<>();
        while (mWorkQueue.size() > 0){
        	TaskInfo ti = mWorkQueue.poll();
        	boolean done = ti.tryRun();
        	if(!done){
            	tempQueue.add(ti);
        	}
        }
        mWorkQueue.addAll(tempQueue);
    }

    public void destroy() {
        died = true;
    }
}