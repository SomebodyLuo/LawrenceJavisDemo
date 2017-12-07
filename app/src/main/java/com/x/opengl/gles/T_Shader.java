package com.x.opengl.gles;

import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.Mesh;

public abstract class  T_Shader {

	protected abstract String getVetexShell();
	protected abstract String getFragShell();
	protected abstract String getVetexString();
	protected abstract String getFragString();

	protected abstract void init() ;
	protected abstract void goRender(Mesh mesh, Material mMaterial,  float[] selfModelViewMatrix);
//	protected abstract void goMaterial(Material material )  ;
//	protected abstract void goDrawableMaterial(MaterialGroup materialGroup, Material material,float te_2d);


}
