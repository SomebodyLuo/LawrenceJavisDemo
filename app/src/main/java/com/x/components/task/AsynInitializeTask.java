package com.x.components.task;

/**
 * AsynTaskExecutor的初始化任务
 * @date   2014-04-03 11:48:36
 */
public interface AsynInitializeTask{

	/**
	 * 完成AsynTaskExecutor执行线程的初始化工作
	 * 如果有需要保存执行线程生命周期的环境，请保存为一个Object并返回
	 * @return
	 */
	Object initializedRun();
	
}
