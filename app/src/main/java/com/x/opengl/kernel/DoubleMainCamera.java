package com.x.opengl.kernel;

import com.x.components.node.CameraView;

public class DoubleMainCamera extends Camera{

	//-------以下为左右眼------- 

	private CameraView 		LeftEyeAgentView 		= new CameraView();
	private CameraView 		RightEyeAgentView 	= new CameraView();
	private int i = 0 ;
	public DoubleMainCamera() {
		LeftEyeAgentView.setTranslate(-EngineConstanst.REFERENCE_SCREEN_WIDTH * 0.00f, 0,EngineConstanst.REFERENCE_SCREEN_HEIGHT);
		RightEyeAgentView.setTranslate(EngineConstanst.REFERENCE_SCREEN_WIDTH * 0.00f, 0,EngineConstanst.REFERENCE_SCREEN_HEIGHT);
	}
	@Override
	public void resetEye() {
		i = 0;
	}
	
	@Override
	public void initViewPort() {
		i ++;
		if(i == 1){
			LeftEyeAgentView.draw();
			super.setViewPort(0, 0, EngineConstanst.SCREEN_WIDTH/2, EngineConstanst.SCREEN_HEIGHT);
		}else{
			RightEyeAgentView.draw();
			super.setViewPort((int)EngineConstanst.SCREEN_WIDTH/2, 0, EngineConstanst.SCREEN_WIDTH/2, EngineConstanst.SCREEN_HEIGHT);
		}
		i %=2;
	}
	@Override
	public void updateWorldViewMatrix() {

		if(i == 1){
			LeftEyeAgentView.updateWorldViewMatrix();
		}else{
			RightEyeAgentView.updateWorldViewMatrix();
		}
	}
	@Override
	public void updateUiViewMatrix() {
		if(i == 1){
			LeftEyeAgentView.updateUiViewMatrix();
		}else{
			RightEyeAgentView.updateUiViewMatrix();
		}
	}
	@Override
	public void updateUiGravityViewMatrix() {
		
	}


	@Override
	public void setEyeMatrix(float[] matrix) {

		LeftEyeAgentView.setGyroscopeMatrix(matrix);
		RightEyeAgentView.setGyroscopeMatrix(matrix);
	}
	public float[] getEyePosition() {
		return RightEyeAgentView.getEyePosition();
	}


}
