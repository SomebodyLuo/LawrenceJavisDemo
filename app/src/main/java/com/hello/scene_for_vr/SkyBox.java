package com.hello.scene_for_vr;

import com.x.Director;
import com.x.components.node.View;
import com.x.components.node.ViewGroup;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.ObjDrawable;
import com.x.opengl.kernel.SkyDrawable;

/**
 * Created by pc on 2017/12/11.
 */

public class SkyBox {
    private View mBox;
    public View getLayer() {
        return mBox;
    }
    public SkyBox() {


        SkyDrawable skyDrawable = Director.getInstance().sResourcer.generateSkyBox();
        mBox = new View(skyDrawable);
//        mBox.setWidth(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//        mBox.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//        mBox.setThickness(EngineConstanst.REFERENCE_SCREEN_HEIGHT);
//		mSphere.setScale(2, 2, 2);

//        mBox.setTranslate(0, 0, EngineConstanst.REFERENCE_SCREEN_HEIGHT/2);
        mBox.setFocusable(false);
        mBox.setTouchAble(false);
        mBox.setCullFrontFace(true);
    }
}