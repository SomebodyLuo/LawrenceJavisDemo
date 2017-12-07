package com.x.opengl.kernel;

import com.x.components.node.CameraView;

public class EngineInfoCamera extends Camera {

	//-------以下为引擎信息的眼睛-------
	private CameraView 		EngineInfoEyeAgentView 		= new CameraView();
	public EngineInfoCamera() {
		EngineInfoEyeAgentView.setTranslate(0, 0,EngineConstanst.REFERENCE_SCREEN_HEIGHT);
	}
	@Override
	public void resetEye() {

	}

	@Override
	public void initViewPort() {
		EngineInfoEyeAgentView.draw();
		super.setViewPort(0, 0, EngineConstanst.SCREEN_WIDTH, EngineConstanst.SCREEN_HEIGHT);
	}

	@Override
	public void updateWorldViewMatrix() {

	}

	@Override
	public void updateUiViewMatrix() {
		EngineInfoEyeAgentView.updateUiViewMatrix();
	}
	
	@Override
	public void updateUiGravityViewMatrix() {
		
	}


	@Override
	public void setEyeMatrix(float[] matrix) {
		
	}

}
