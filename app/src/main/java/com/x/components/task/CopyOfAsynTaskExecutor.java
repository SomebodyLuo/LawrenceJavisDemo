package com.x.components.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import android.annotation.SuppressLint;

import com.x.opengl.utils.MLog;

/**
 * 
 * 
 * 多个线程取同一列表
 * 异步任务执行器
 * @date   2014-04-01 13:41:15
 */
public class CopyOfAsynTaskExecutor {
	
	public static final String TAG = "AsynTaskExecutor";

	private List<TaskExecuteThread> mThreadGroup    = null;
	private LinkedList<AsynTask>    mTaskList       = null;
	private AsynInitializeTask      mInitializeTask = null;
	private AsynDisposeTask         mDisposeTask    = null;
	private boolean                 mIsAllStop      = false;
	private int                     mMaxThreadCount = 2;
	private String                  mThreadName     = "AsynTaskExecutor";
	
	public CopyOfAsynTaskExecutor() {
		
		mThreadGroup = new ArrayList<TaskExecuteThread>();
		mTaskList    = new LinkedList<AsynTask>();
		
		MLog.d("aaa","======AsynTaskExecutor()==="+this);
		
	}
	
	/**
	 * 设置执行线程数量
	 * @param executorSize
	 */
	public void setExecutorSize(int executorSize) {
		mMaxThreadCount = executorSize;
	}
	
	/**
	 * 设置任务执行器的初始化任务
	 * @param initializeTask
	 */
	public void setTaskExecutorInitializeTask(AsynInitializeTask initializeTask) {
		mInitializeTask = initializeTask;
	}
	
	/**
	 * 请求一个加载任务
	 * @param task
	 * @return
	 */
	public boolean request(AsynTask task) {
		boolean result = false;
		
		// 更新线程组中的线程数量
		updateThreadGroup();
		
		// 推入一个异步异步任务入队列
		synchronized (mTaskList) {
			result = mTaskList.offer(task);
		}
		
		return result;
	}
	
	/**
	 * 请求一个插队任务
	 * @param task
	 * @return
	 */
	@SuppressLint("NewApi") public boolean requestAtFrontOfQueue(AsynTask task) {
		boolean result = false;
		
		// 更新线程组中的线程数量
		updateThreadGroup();
		
		// 推入一个异步异步任务入队列
		synchronized (mTaskList) {
			result = mTaskList.offerFirst(task);
		}
		
		return result;
	}

	/**
	 * 请求取消所有未完成的任务
	 * @return
	 */
	public boolean cancelAllTask() {
		boolean result = false;
		
		synchronized (mTaskList) {
			Iterator<AsynTask> itr = mTaskList.iterator();
			while (itr.hasNext()) {
				itr.next().onCanceled(null);
			}
			mTaskList.clear();
		}
		
		return result;
	}
	
	/**
	 * 领取任务
	 * @return
	 */
	public AsynTask pickTask() {
		AsynTask task = null;
		
		// 提取任务
		if (!mIsAllStop) {
			synchronized (mTaskList) {
				task = mTaskList.poll();
			}
		}
	
		return task;
	}
	
	/**
	 * 设置任务执行器的析构任务
	 * @param disposeTask
	 */
	public void setTaskExecutorDisposeTask(AsynDisposeTask disposeTask) {
		mDisposeTask = disposeTask;
	}
	
	/**
	 * 启动所有执行线程
	 */
	public void startExecutor() {
		mIsAllStop = false;
	}
	
	/**
	 * 停止所有执行线程
	 */
	public void stopExecutor() {
		mIsAllStop = true;
	}
	
	/**
	 * 管理当前线程数量
	 */
	private void updateThreadGroup() {
		int threadSize = 0;
		
		// 检查僵尸线程
		Iterator<TaskExecuteThread> itr = mThreadGroup.iterator();
		while(itr.hasNext()) {
			if (itr.next().getState() == Thread.State.TERMINATED) {
				itr.remove();
			}
		}

		if (mIsAllStop) {
			for (int i = 0; i < mThreadGroup.size(); i++) {
				TaskExecuteThread thread = mThreadGroup.remove(i);
				if (thread != null) {
//					thread.interrupt();
					thread.stopSelf();
					
				}
			}
			return;
		}
		// 重组线程组
		threadSize = mThreadGroup.size();
		if (threadSize < mMaxThreadCount) {
			// 添加线程到线程组
				TaskExecuteThread thread = new TaskExecuteThread();
				thread.setName(mThreadName + "#" + threadSize);
				thread.start();
				mThreadGroup.add(thread);
		}else if (threadSize > mMaxThreadCount) {
			// 通知多余线程退出任务
			TaskExecuteThread thread = mThreadGroup.remove(threadSize - 1);
			if (thread != null) {
//				thread.interrupt();
				thread.stopSelf();
			}
		}else{
			// 线程数量正好，无需增减线程
		}
	}

	/**
	 * 异步任务执行线程
	 * @date   2014-04-01 13:59:22
	 */
	protected class TaskExecuteThread extends Thread{

		private long	mTurboTime			= 5;
		private long	mSleepTime			= 200;
		private boolean	mUnderSleeping		= false;
		private Object	mEnvironment		= null;
		private boolean	mThreadAliveFlag	= true;
		
		public TaskExecuteThread() {
			
		}
		
		public void stopSelf() {
			mThreadAliveFlag = false;
		}

		@Override
		public void run() {
			try {
				
				MLog.d("aaa",Thread.currentThread().getId()  + "===start===");
				
				// 执行初始化任务
				if (mInitializeTask != null) {
					mEnvironment = mInitializeTask.initializedRun();
				}
				MLog.d("aaa", "===Initiali==="+mEnvironment);
				
				while(mThreadAliveFlag) {
					// 取出任务
					AsynTask task = pickTask();
					
					// 任务管理
					if (task == null) {
						// 队列中没有任务，执行线程准备进入休眠
						mUnderSleeping = true;
					}else{
						// 切换到高速处理，执行队列任务
						mUnderSleeping = false;
					}
					
					// 执行任务
					if (task != null) {
						task.run(mEnvironment);
					}
					
					// 执行线程休眠
					if (mUnderSleeping) {
						Thread.sleep(mSleepTime);
					}else{
						Thread.sleep(mTurboTime);
					}

				}
			}catch(InterruptedException exp) {
				// 线程执行完毕
			}catch(Exception exp) {
				// 线程异常退出
				exp.printStackTrace();
			}catch(Error err) {
				// 线程异常退出
				err.printStackTrace();
			}finally{

				
				MLog.d("aaa",Thread.currentThread().getId()  + "===End===");
				disposeThread();
			}

		}
		
		public void disposeThread() {
			// 执行回收任务
			if (mDisposeTask != null) {

				MLog.d("aaa", "disposeThread===mEnvironment==="+mEnvironment);
				mDisposeTask.disposeRun(mEnvironment);
			}
		}
		
		/**
		 * 设置环境变量
		 * @param environment
		 */
		protected void setEnvironment(Object environment) {
			mEnvironment = environment;
		}
		
		/**
		 * 获取环境变量
		 * @return
		 */
		protected Object getEnvironment() {
			return mEnvironment;
		}
	}
	
}
