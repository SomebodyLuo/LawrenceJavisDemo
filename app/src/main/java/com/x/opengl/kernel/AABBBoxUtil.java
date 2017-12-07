package com.x.opengl.kernel;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

/**
 * 	查找一系列点的最小闭合多边形
 *
 */
public class AABBBoxUtil {

	public static ArrayList<PointF> caculateMinPolygon(ArrayList<PointF> list) {

		List<PointF> pointList = list;
		ArrayList<PointF> findList = new ArrayList<PointF>();
		
		PointF pStart = new PointF(Float.MAX_VALUE,Float.MAX_VALUE);
		//寻找一个起始点，默认选择x值最小的一个，同时消除重复点
		for (int i = 0; i < pointList.size(); i++) {
			PointF p = pointList.get(i);
			if(p.x < pStart.x){
				pStart = p;
			}
			
			for (int j = i+1; j < pointList.size(); j++) {
				PointF cp = pointList.get(j);
				if(cp.x == p.x && cp.y == p.y){
					pointList.remove(cp);
				}
			}
		}
		findList.add(pStart);
		
		//寻找从起始点开始后的第一个合法点
		
		List<PointF> tryPootList = new ArrayList<PointF>();
		tryPootList.addAll(pointList);
		boolean mJumpToOut = false;//全部循环终止条件
		while (tryPootList.size() > 0) {
			for (int i = 0; i < tryPootList.size(); i++) {
				PointF  tryPoint = tryPootList.get(i);
				PointF lastLegalPointF = findList.get(findList.size() - 1);//获取上一次的合法点
				
				List<PointF> loopList = new ArrayList<PointF>();
				loopList.addAll(pointList);
				loopList.remove(lastLegalPointF);
				loopList.remove(tryPoint); //构建一个可供查找的新的列表
				
				boolean check = true;//默认值为通过了检验
				for (int j = 0; j < loopList.size(); j++) {
					PointF checkPointF = loopList.get(j);//获取检验点
					Vector2 v0 = new Vector2(lastLegalPointF,tryPoint);//构建从上一次合法点到tryPoint的向量;
					Vector2 v1 = new Vector2(tryPoint, checkPointF);//构建从tryPoint到检验点的向量
//					D2Vector v2 = new D2Vector(checkPointF, lastLegalPointF);//再构建从检验点到合法点的向量;
					if(!Vector2.isCWcross(v0,v1)){//按顺时针检索所有符合条件的点
						check = false;
						break;
					}
				}
				
				if(check){
					if(findList.indexOf(tryPoint) >= 0){
						mJumpToOut = true; //表明新找到tryPoint又是在mFindList里面的，说明，循环已经找了一个循环
						break;
					}
					findList.add(tryPoint);//通过检查
				}
			}
			tryPootList.clear();
			tryPootList.addAll(pointList);
			if(mJumpToOut){
				break;
			}
		}
		
		return findList;
	}

}
