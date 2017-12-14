package com.x.opengl.kernel;


/**
 * 1.存储用到的一些常量,某些常量经常参与运算，对程序效率造成损失，故写在此处
 * 2.存储一些基本的配置信息
 * 
 * 要求在Director的onSurfaceChange完成后,这里面的变量都有了赋值,防止报空指针
 *
 */
public class EngineConstanst {

	
	// 是否有按键音
	public static boolean ISSOUND = true;
	public static final float BOX_LENGTH = 2048 * 10;//用户参考屏幕宽度

	public static final float REFERENCE_SCREEN_WIDTH = 1920;//用户参考屏幕宽度
	public static final float REFERENCE_SCREEN_HEIGHT = 1080;//用户参考屏幕高度
	public static final float INITIAL_PIX_REFERENCE = 1f/1080;//初始值引用
	public static final float INITIAL_ENGINE_SCREEN_REFERENCE = 1080;//初始值引用

	//基本像素点长度参照,因为我们开发环境是1920x1080,故暂时用这个1080作为参考基准；即，如果你想设置全屏高度,应该使用该屏幕高度引用
	public static float SCREEN_WIDTH = 1; //屏幕的实际宽度，<不建议用户使用>
	public static float SCREEN_HEIGHT = 1; //屏幕的实际高度，<不建议用户使用>
	
	public static float PIX_REFERENCE = INITIAL_PIX_REFERENCE; 
	public static float ENGINE_SCREEN_REFERENCE = INITIAL_ENGINE_SCREEN_REFERENCE;//引擎屏幕引用
	
	public static float REFERENCE_CLICK_TINGLE_ERROR = 160;//点击时候，用户手指移动误差范围
	public static float REFERENCE_LIST_MOVE_TINGLE = 160;//列表移动时候，手指移动范围超过该值则不在往下传递事件
	public static float REFERENCE_MOVE_ATTRACT = 30;//主卡片触发吸引的到附近卡片的最小值

	
	//这里是为了适应多个分辨率而抽象出的像素点概念，同时我们这里假设像素点之间的距离为0
//	public static float	PIX_WIDTH = PIX_REFERENCE; //一个像素点代表的立方体的宽，
//	public static float	PIX_HEIGHT = PIX_REFERENCE; //一个像素点代表的立方体的高。
//	public static float	PIX_THICKNESS = PIX_REFERENCE; //一个像素点代表的立方体的厚度，也可以理解为深度，即z轴向值。
	
}
