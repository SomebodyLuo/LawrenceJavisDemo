package com.x.opengl.kernel;

import java.util.ArrayList;

import android.graphics.PointF;
import android.util.Log;

import com.x.Director;

public class SkyDrawable extends Drawable{

    public Mesh[] mMeshes = null;//专为加载Obj格式的文件而用个的临时变量
    public MaterialGroup mMaterials = null;//专为加载Obj格式的文件而用个的临时变量
    private boolean mCullFrontFlag  = false;


    public void setCullFrontFace(boolean b) {
        mCullFrontFlag =b;
    }


    public SkyDrawable(int depth) {
        super(depth);
    }

    /**
     * 由于三维模型一个单位和我们这里的一个单位并不对应，但是导出模型时又无法确定我们这边的EngineConstanst.PIX_REFERENCE的具体值，所以导出模型时只能将模型缩小为3dmax里面的默认的一个单位
     * 而后续的乘法（ * EngineConstanst.PIX_REFERENCE）由如下代码实现
     *
     * （EngineConstanst.PIX_REFERENCE的值是跟屏幕相关的），故这里采用在代码中乘以我们算出的EngineConstanst.PIX_REFERENCE值，
     */
    @Override
    public void setWidth(float f) {
        super.setWidth(f*EngineConstanst.PIX_REFERENCE);
    }
    @Override
    public void setHeight(float f) {
        super.setHeight(f*EngineConstanst.PIX_REFERENCE);
    }
    @Override
    public void setThickness(float f) {
        super.setThickness(f*EngineConstanst.PIX_REFERENCE);
    }

    public void draw( ) {


//        if(mCullFrontFlag){
//            Director.sGLESVersion.openCullFrontFace();
//        }else{
//            Director.sGLESVersion.openCullBackFace();
//        }

        Director.sGLESVersion.closeDepthTest();

        Director.sGLESVersion.pushMatrix();

        onAnimation();
        onDrawbleTransform();

			Log.d("ming", "lallalallala========================mMeshes == " + mMeshes.length );
			Log.d("ming", "lallalallala========================mMaterials == " + mMaterials.size());
//			Log.d("ming", "lallalallala========================mMaterials =="+mMaterials.getMaterial(0).Name);
//			for (int j = 0; j < mMaterials.size(); j++) {
//				Material gj = mMaterials.getMaterial(j);
//				Log.d("ming", "materialgroup["+j+"] name = "+gj.Name );
//			}




        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (mMeshes[i].isEnabled()) {
                    onRender(mMeshes[4 * i + j], mMaterials.getMaterial(i));
                }
            }
        }


        onRayCast();

        Director.sGLESVersion.popMatrix();
//        Director.sGLESVersion.closeDepthTest();
//        Director.sGLESVersion.closeCullFace();

    }
    private void onRender(Mesh mesh, Material material)
    {
        Director.sGLESVersion.onRender(mesh, material, null, true);
    }



}
