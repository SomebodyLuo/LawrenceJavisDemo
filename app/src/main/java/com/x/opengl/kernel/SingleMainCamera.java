package com.x.opengl.kernel;

import com.x.components.node.CameraView;

public class SingleMainCamera extends Camera{

	//-------以下为单眼---------
	private CameraView 		SingleEyeAgentView 			= new CameraView();
	private float[] mPosition ;
	public SingleMainCamera() {
		mPosition = new float[]{0, 0,EngineConstanst.REFERENCE_SCREEN_HEIGHT};
		SingleEyeAgentView.setTranslate(mPosition[0],mPosition[1],mPosition[2]);
	}
	@Override
	public void resetEye() {
		
	}
	@Override
	public void initViewPort() {

		SingleEyeAgentView.draw();
		super.setViewPort(0, 0, EngineConstanst.SCREEN_WIDTH, EngineConstanst.SCREEN_HEIGHT);
	}

	@Override
	public void updateWorldViewMatrix() {
		
		SingleEyeAgentView.updateWorldViewMatrix();
		
		
	}

	@Override
	public void updateUiViewMatrix() {
		SingleEyeAgentView.updateUiViewMatrix();
	}
	@Override
	public void updateUiGravityViewMatrix() {
		SingleEyeAgentView.updateUiGraivityViewMatrix();
	}
	@Override
	public void setEyeMatrix(float[] matrix) {

		SingleEyeAgentView.setGyroscopeMatrix(matrix);
			
	}
	public float[] getEyePosition() {
		return SingleEyeAgentView.getEyePosition();
//			return new float[]{mTransform.Position.X,mTransform.Position.Y,mTransform.Position.Z};
	}

}
