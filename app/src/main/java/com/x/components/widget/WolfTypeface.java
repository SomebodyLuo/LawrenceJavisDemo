package com.x.components.widget;

import java.util.HashMap;

import com.x.Director;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

public class WolfTypeface {

	public static final int SIMPLIFIEDSTYLE = 0;
	public static final int HELSTYLE = 1;
	public static final int HELIGHTSTYLE = 2;
	public static final int UNIVIECSTYLE = 3;
	public static final int IOSSTYLE = 4;
	public static final int AGENCYR = 5;
	public static final int HANDGOTN = 6;
	public static final int GOTHAM = 7;
	private static HashMap<Integer, Typeface> TARTOTTYPEFACE = new HashMap<Integer, Typeface>();
	
	public static Typeface getTypeface(int typefacId)
	{
		Typeface typeface = TARTOTTYPEFACE.get(typefacId);
		if (typeface != null){
			return typeface;
		};
		if(IOSSTYLE == typefacId ){
			 typeface = Typeface.createFromAsset(Director.getInstance().getContext().getAssets(),
					 "fonts/ios.ttc");
		}
		if(typeface != null){
			TARTOTTYPEFACE.put(typefacId, typeface);
		}
		return typeface;
	}

}
