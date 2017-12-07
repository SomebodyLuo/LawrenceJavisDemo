package com.x.components.task;

/**
 * 异步任务
 * @date   2014-04-01 20:47:17
 */
public interface AsynTask {
	
	void run(Object envirnment);
	
	void onCanceled(Object envirnment);
	
}
