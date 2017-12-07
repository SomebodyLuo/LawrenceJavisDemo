package com.x.opengl.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 工具类
 * 
 * @date
 */
@SuppressLint("NewApi")
public class Utils {

	public static Bitmap scaleBitmapWithProportion(Bitmap src, int destWidth,
			int destHeight, boolean filter) {
		int srcWidth = 0, srcHeight = 0;
		int transWidth = 0, transHeight = 0, transXpos = 0, transYpos = 0;
		Bitmap result = null;

		if (src == null) {
			return null;
		}

		srcWidth = src.getWidth();
		srcHeight = src.getHeight();

		// 计算目标宽高
		if ((srcWidth - destWidth) != (srcHeight - destHeight)) {
			if ((srcWidth - destWidth) > (srcHeight - destHeight)) {
				// 缩放至srcWidth == destWidth
				transWidth = destWidth;
				transHeight = (int) ((float) srcHeight * ((float) destWidth / (float) srcWidth));
			} else {
				// 缩放至srcHeight == destHeight
				transHeight = destHeight;
				transWidth = (int) ((float) srcWidth * ((float) destHeight / (float) srcHeight));
			}
		} else {
			// 无需缩放及剪裁
			return src;
		}

		// 缩放
		try {
			result = Bitmap.createScaledBitmap(src, transWidth, transHeight,
					filter);
			src.recycle();
		} catch (OutOfMemoryError exp) {
			return null;
		} catch (Exception exp) {
			// Do nothing
		}

		return result;
	}

	/**
	 * 按比例剪裁缩放图片
	 * 
	 * @param src
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap cutBitmapWithProportion(Bitmap src, int destWidth,
			int destHeight, boolean filter) {
		int srcWidth = 0, srcHeight = 0;
		int transWidth = 0, transHeight = 0, transXpos = 0, transYpos = 0;
		Bitmap result = null;

		if (src == null) {
			return null;
		}

		srcWidth = src.getWidth();
		srcHeight = src.getHeight();

		// 计算缩放后的宽高
		if ((srcWidth - destWidth) != (srcHeight - destHeight)) {
			if ((srcWidth - destWidth) > (srcHeight - destHeight)) {
				// 缩放至srcHeight == destHeight
				transHeight = destHeight;
				transWidth = (int) ((float) srcWidth * ((float) destHeight / (float) srcHeight));
			} else {
				// 缩放至srcWidth == destWidth
				transWidth = destWidth;
				transHeight = (int) ((float) srcHeight * ((float) destWidth / (float) srcWidth));
			}
		} else {
			// 无需缩放及剪裁
			return src;
		}

		// 缩放
		try {
			result = Bitmap.createScaledBitmap(src, transWidth, transHeight,
					filter);
			src.recycle();
		} catch (OutOfMemoryError exp) {
			return null;
		} catch (Exception exp) {
			// Do nothing
		}

		// 计算剪裁的坐标
		if (result != null) {
			srcWidth = result.getWidth();
			srcHeight = result.getHeight();
			if (srcWidth == destWidth && srcHeight == destHeight) {
				// 无需剪裁
				return result;
			} else {
				transWidth = destWidth;
				transHeight = destHeight;
				transXpos = (int) ((float) (srcWidth - destWidth) / 2.0f);
				transYpos = (int) ((float) (srcHeight - destHeight) / 2.0f);
				transXpos = transXpos < 0 ? 0 : transXpos;
				transYpos = transYpos < 0 ? 0 : transYpos;
				transWidth = transWidth > srcWidth ? srcWidth : transWidth;
				srcHeight = transHeight > srcHeight ? srcHeight : transHeight;
			}
		}

		// 剪裁
		src = result;
		result = Bitmap.createBitmap(src, transXpos, transYpos, transWidth,
				transHeight);
		try {
			src.recycle();
		} catch (Exception exp) {
			// Do nothing
		}

		return result;
	}

	/**
	 * 截取字符串的前length个字符，并转换为char数组
	 * 
	 * @param inputString
	 * @param length
	 * @return
	 */
	public static char[] trimToChars(String inputString, int length) {
		int orgStringLength = 0;
		char[] result = null;
		char[] inputStringChars = null;

		if (length > 0) {
			result = new char[length];
		} else {
			return null;
		}

		if (inputString != null && !inputString.isEmpty()) {
			orgStringLength = inputString.length();

			inputStringChars = inputString.toCharArray();

			for (int i = 0, j = 0; i < length && j < orgStringLength; i++, j++) {
				result[i] = inputStringChars[j];
			}
		}

		return result;
	}

	public static byte[] trimToCPPCharBytes(String inputString, int length) {
		int inputStringLength = 0;
		byte[] inputStringBytes = null;
		byte[] result = null;

		if (length > 0) {
			result = new byte[length];
		} else {
			return null;
		}

		if (inputString != null && !inputString.isEmpty()) {
			inputStringBytes = inputString.getBytes(Charset.forName("utf8"));
			inputStringLength = inputStringBytes.length;

			for (int i = 0, j = 0; i < length && j < inputStringLength; i++, j++) {
				result[i] = inputStringBytes[j];
			}
		}

		return result;
	}

	public static float clampAngle(float angle, float minAngle, float maxAngle) {
		angle %= 360;
		if (angle < minAngle)
			angle = minAngle;
		else if (angle > maxAngle)
			angle = maxAngle;
		return angle;
	}

	/**
	 * 拼接文字图片
	 * 
	 * @param number
	 * @return
	 */
	public static Drawable mosaicNumberDrawable(Drawable number) {
		return mosaicNumberDrawable(number, null);
	}

	/**
	 * 拼接文字图片
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static Drawable mosaicNumberDrawable(Drawable left, Drawable right) {
		Bitmap bitmap = null;
		Canvas canvas = null;

		int imageWidth = 0;
		int imageHeight = 0;
		int leftWidth = 0;
		int leftHeight = 0;
		int rightWidth = 0;
		int rightHeight = 0;

		if (left != null) {
			imageWidth = leftWidth = left.getIntrinsicWidth();
			imageHeight = leftHeight = left.getIntrinsicHeight();
			left.setBounds(0, 0, leftWidth, leftHeight);
		}
		if (right != null) {
			imageWidth += rightWidth = right.getIntrinsicWidth();
			imageHeight = imageHeight > right.getIntrinsicHeight() ? imageHeight
					: (rightHeight = right.getIntrinsicHeight());
			right.setBounds(0, 0, rightWidth, rightHeight);
		}

		bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Config.ARGB_8888);
		canvas = new Canvas(bitmap);

		// 拼接图片
		if (left != null) {
			canvas.save();
			canvas.translate(0, (imageHeight - leftHeight) / 2.0f);
			left.draw(canvas);
			canvas.restore();
		}

		if (right != null) {
			canvas.save();
			canvas.translate(leftWidth, (imageHeight - rightHeight) / 2.0f);
			right.draw(canvas);
			canvas.restore();
		}

		return new BitmapDrawable(bitmap);
	}

	public static ArrayList<Integer> makePrimeNumbers(ArrayList<Integer> list,
			int fromNumber, int toNumber) {
		list.clear();
		for (int i = fromNumber; i <= toNumber; i++) {
			int theta = (int) Math.sqrt(i + 1);
			boolean isPrime = true;
			for (int j = 2; j <= theta; j++) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
			}

			if (isPrime) {
				list.add(i);
			}
		}

		return list;
	}

	public static ArrayList<Integer> makeRandCompositeNumbers(
			ArrayList<Integer> list, int fromNumber, int toNumber) {
		list.clear();

		for (int i = fromNumber; i <= toNumber; i++) {
			int theta = (int) Math.sqrt(i + 1);
			for (int j = 2; j <= theta; j++) {
				if (i % j == 0) {
					list.add(i);
					break;
				}
			}
		}

		// 打乱顺序
		int[] randSerial = randomSerial(list.size());
		ArrayList<Integer> randList = new ArrayList<Integer>(list);
		int size = randSerial.length;
		for (int i = 0; i < size; i++) {
			randList.set(randSerial[i], list.get(i));
		}

		list.clear();

		return randList;
	}

	/**
	 * 因式分解
	 * 
	 * @param factoredNumber
	 * @return
	 */
	public static int[] factor(int factoredNumber) {
		boolean keepRunning = true;

		List<Integer> factors = new ArrayList<Integer>();

		while (keepRunning) {
			for (int i = 2; i < factoredNumber / 2 + 1; i++) {
				if (factoredNumber % i == 0) {
					factors.add(i);
					factoredNumber /= i;

					if (isPrimeNumber(factoredNumber)) {
						keepRunning = false;
						factors.add(factoredNumber);
					}

					break;
				}
			}
		}

		int loopSize = factors.size();
		int[] result = new int[loopSize];
		for (int i = 0; i < loopSize; i++) {
			result[i] = factors.get(i);
		}

		return result;
	}

	/**
	 * 是否为素数
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isPrimeNumber(int number) {
		boolean result = true;

		for (int i = 2; i < number; i++) {
			if (number % i == 0) {
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * 产生随机序列
	 * 
	 * @param limit
	 * @return
	 */
	public static int[] randomSerial(int limit) {
		int[] result = new int[limit];
		for (int i = 0; i < limit; i++)
			result[i] = i;
		int w;
		Random rand = new Random();
		for (int i = limit - 1; i > 0; i--) {
			w = rand.nextInt(i);
			int t = result[i];
			result[i] = result[w];
			result[w] = t;
		}
		return result;
	}

	/**
	 * 转换为渐变倒影图
	 * 
	 * @param bitmap
	 * @param alpha
	 * @return
	 */
	public static Bitmap generateReflectedImage(Bitmap bitmap, int alpha) {
		// 图片与倒影间隔距离
		final int reflectionGap = 4;

		// 图片的宽度
		int width = bitmap.getWidth();
		// 图片的高度
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		// 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
		matrix.preScale(1, -1);
		// 创建反转后的图片Bitmap对象，图片高是原图的一半。
		Bitmap reflectionBitmap = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);
		// 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。
		Bitmap withReflectionBitmap = Bitmap.createBitmap(width, (height
				+ height / 2 + reflectionGap), Config.ARGB_8888);

		// 构造函数传入Bitmap对象，为了在图片上画图
		Canvas canvas = new Canvas(withReflectionBitmap);
		// 画原始图片
		canvas.drawBitmap(bitmap, 0, 0, null);

		// 画间隔矩形
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

		// 画倒影图片
		canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, null);

		// 实现倒影效果
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				withReflectionBitmap.getHeight(), 0x00FFFFFF | (alpha << 24),
				0x00FFFFFF, TileMode.MIRROR);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		// 覆盖效果
		canvas.drawRect(0, height, width, withReflectionBitmap.getHeight(),
				paint);

		return withReflectionBitmap;
	}

	/**
	 * 取能包含src的2的n次幂
	 * 
	 * @param src
	 * @return
	 */
	public static float roundTo2Pow(float src) {
		int pow = 1;
		float result = 2;

		do {
			result = (float) Math.pow(2, pow++);
		} while (result < src);

		return result;
	}

//	/**
//	 * 摄像机深度排序
//	 * 
//	 * @param actors
//	 * @param cameraPosition
//	 */
//	public static void sortByDepth(GameObject[] actors, Vector3 cameraPosition) {
//		float[] distanceList = new float[actors.length];
//		for (int i = 0; i < distanceList.length; i++) {
//			distanceList[i] = Vector3.distance(cameraPosition,
//					(Vector3) actors[i].Position);
//		}
//
//		qsort(distanceList, actors, 0, distanceList.length - 1, cameraPosition);
//
//	}
//
//	private static void qsort(float[] distanceList, GameObject[] actors,
//			int low, int high, Vector3 cameraPosition) {
//		int privot = 0;
//		if (low < high) {
//			privot = qsortPartion(distanceList, actors, low, high,
//					cameraPosition);
//			qsort(distanceList, actors, low, privot - 1, cameraPosition);
//			qsort(distanceList, actors, privot + 1, high, cameraPosition);
//		}
//	}
//
//	private static int qsortPartion(float[] distanceList, GameObject[] actors,
//			int low, int high, Vector3 cameraPosition) {
//		int key = low;
//		float keyValue = distanceList[key];
//		GameObject keyObject = actors[key];
//
//		distanceList[key] = distanceList[high];
//		actors[key] = actors[high];
//
//		distanceList[high] = keyValue;
//		actors[high] = keyObject;
//
//		int i = low - 1;
//		for (int j = low; j < high; j++) {
//			if (distanceList[j] <= distanceList[high]) {
//				keyValue = distanceList[i + 1];
//				keyObject = actors[i + 1];
//
//				distanceList[i + 1] = distanceList[j];
//				actors[i + 1] = actors[j];
//
//				distanceList[j] = keyValue;
//				actors[j] = keyObject;
//
//				i++;
//			}
//		}
//
//		keyValue = distanceList[high];
//		keyObject = actors[high];
//
//		distanceList[high] = distanceList[i + 1];
//		actors[high] = actors[i + 1];
//
//		distanceList[i + 1] = keyValue;
//		actors[i + 1] = keyObject;
//
//		return i + 1;
//	}

}
