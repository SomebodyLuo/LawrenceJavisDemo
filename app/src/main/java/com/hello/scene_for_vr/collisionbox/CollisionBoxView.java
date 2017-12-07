package com.hello.scene_for_vr.collisionbox;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CollisionBoxView extends View{

	
	private ArrayList<PointF>  list;
	private Paint mPaint;
	private int mCenterX;
	private int mCenterY;

	public CollisionBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mPaint = new Paint();
		this.mPaint .setColor(Color.RED);
		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setStrokeWidth(5);
		this.mPaint.setTextSize(70);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		mCenterX =( right - left)/2;
		mCenterY = (bottom - top)/2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(list == null){
			return;
		}
		
//		String pointString = ""; 
		for (int i = 0; i < list.size() ; i++) {
			PointF pstart = list.get(i);
			PointF pstop = list.get((i+1)%list.size());
			
//			pointString += pstart+" --> ";
//			canvas.drawLine(mCenterX+pstart.x, mCenterY+pstart.y, mCenterX+pstop.x, mCenterY+pstop.y, mPaint);
			mPaint.setColor(Color.RED);
			canvas.drawLine(pstart.x, pstart.y, pstop.x, pstop.y, mPaint);
			mPaint.setColor(Color.WHITE);
			canvas.drawText(""+i, pstart.x, pstart.y, mPaint);
		}
//		canvas.drawLine(0,0,600,600, mPaint);
//		canvas.drawCircle(mCenterX, mCenterY, 200, mPaint);
//		Log.d("ming", "list.size) "+pointString);
		
	}

	public void update(Object obj) {
		list = (ArrayList<PointF>) obj;
		postInvalidate();
	}
}
