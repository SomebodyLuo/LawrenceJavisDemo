package com.hello.scene_for_vr;

import android.util.Log;

import com.x.Director;
import com.x.components.node.View;
import com.x.components.node.ViewGroup;
import com.x.opengl.kernel.EngineConstanst;
import com.x.opengl.kernel.ObjDrawable;

/**
 * Created by Administrator on 2017/12/4.
 */

public class MyWorldLayer {

    private final ViewGroup viewGroup;

    public MyWorldLayer(){

        ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/enviroment.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/teaport.obj");
//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/TankWorld.obj");
        //		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadObjModel("models/MyCube.obj");

//		ObjDrawable objDrawable = Director.getInstance().sResourcer.loadModelByAssimpAPI(Director.getInstance().getContext(),new String[]{"models/MyModel/rectangle.FBX","models/MyModel/20150805095213114.jpg"});

         viewGroup = new ViewGroup();
        View  cube = new MyTransView(objDrawable);
        cube.setWidth(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
        cube.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
        cube.setThickness(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
        cube.setTranslate(0, -EngineConstanst.REFERENCE_SCREEN_HEIGHT*2f, 0);
        cube.setScale(0.5f, 0.5f, 0.5f);
            cube.setFocusable(false);
            cube.setTouchAble(false);
     //   viewGroup.addChild(cube);

//        for (int i = 0 ; i < 1 ; i++){
//
//            float angel = i /10f* 360;
//
//            float x = (float) (4000*Math.sin(angel));
//            float y = -500;
//            float z = (float) (4000*Math.cos(angel));
//
//            View  cube = new MyTransView(objDrawable);
//            cube.setWidth(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
//            cube.setHeight(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
//            cube.setThickness(EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f);
//            cube.setTranslate(0, -EngineConstanst.REFERENCE_SCREEN_HEIGHT*0.5f, 0);
//            //cube.setCullFrontFace(true);
//            cube.setScale(0.5f, 0.5f, 0.5f);
////		cube.setRotate(0, 10, 0);
//
//            cube.setTranslate(x, y, z);
//            cube.setFocusable(false);
//            cube.setTouchAble(false);
//
//            viewGroup.addChild(cube);
//        }

    }
    public View getLayer() {
        return viewGroup;
    }
}
