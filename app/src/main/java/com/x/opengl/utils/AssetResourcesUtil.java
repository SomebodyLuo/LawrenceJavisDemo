package com.x.opengl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class AssetResourcesUtil {
	public static Bitmap getBitmap(Context context,String picName){
		Bitmap bitmap = null;
		try {
			BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), context.getAssets().open(picName));
			bitmap = bitmapDrawable.getBitmap();
		} catch (Exception e) {
			bitmap = null;
		}
		return   bitmap;
	}

}
