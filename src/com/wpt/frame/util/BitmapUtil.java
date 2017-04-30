package com.wpt.frame.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.PorterDuff.Mode;
import android.graphics.Matrix;

/**
 * @Desc: 图片工具类
 * @author wpt
 * @since 2013-6-27 下午3:07:26
 */
public class BitmapUtil {

	/**
	 * 图片旋转
	 * 
	 * @param bitmap
	 * @param angle
	 * @return
	 */
	public static Bitmap roateImg(Bitmap bitmap, int angle) {
		Matrix matrix = new Matrix();
		final int widthOrig = bitmap.getWidth();
		final int heightOrig = bitmap.getHeight();
		matrix.setRotate(angle);
		Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, widthOrig,
				heightOrig, matrix, true);
		return resizeBitmap;
	}

	/**
	 * @Desc: 图片缩放
	 * @author wpt
	 * @since 2013-6-27 下午3:30:28
	 */
	public static Bitmap getBitmap(byte[] bytes, int width, int height) {
		Bitmap _bitmap = null;
		Options _ops = new Options();
		_ops.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, _ops);

		_ops.inJustDecodeBounds = false;

		int scaleX = _ops.outWidth / width;
		int scaleY = _ops.outHeight / height;
		int scale = scaleX > scaleY ? scaleX : scaleY;
		_ops.inSampleSize = scale;
		_bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, _ops);
		return _bitmap;
	}

	/**
	 * 根据输入流， 缩小比获取位图对象
	 * 
	 * @param in
	 * @param scale
	 * @return
	 */
	public static Bitmap getBitmap(InputStream in, int scale) {
		Bitmap _bitmap = null;
		Options _ops = new Options();
		_ops.inSampleSize = scale;
		_bitmap = BitmapFactory.decodeStream(in, null, _ops);
		return _bitmap;
	}

	/**
	 * @Desc: 二进制流转为bitmap
	 * @author wpt
	 * @since 2013-6-27 下午3:30:59
	 */
	public static Bitmap getBitmapFromByte(byte[] temp) {
		if (temp != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * @Desc: bitmap转为二进制流
	 * @author wpt
	 * @since 2013-6-27 下午3:31:29
	 */
	public static byte[] getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 65, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * @Desc: 根据路径获取bitmap
	 * @author wpt
	 * @since 2013-6-27 下午3:45:28
	 */
	public static Bitmap readBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * @Desc: 根据res目录id获取bitmap
	 * @author wpt
	 * @since 2013-6-27 下午3:46:48
	 */
	public static Bitmap readResImage(Context context, int imageId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		InputStream in = context.getResources().openRawResource(imageId);
		return BitmapFactory.decodeStream(in, null, options);
	}

	/**
	 * 将位图保存到指定的路径
	 * 
	 * @param path
	 * @param bitmap
	 * @throws IOException
	 */
	public static void saveBitmap(String path, Bitmap bitmap)
			throws IOException {
		if (path != null && bitmap != null) {
			File _file = new File(path);
			// 如果文件夹不存在则创建一个新的文件
			if (!_file.exists()) {
				_file.getParentFile().mkdirs();
				_file.createNewFile();
			}
			// 创建输出流
			OutputStream write = new FileOutputStream(_file);
			// 获取文件名
			String fileName = _file.getName();
			// 取出文件的格式名
			String endName = fileName.substring(fileName.lastIndexOf(".") + 1);
			if ("png".equalsIgnoreCase(endName)) {
				// bitmap的压缩格式
				bitmap.compress(CompressFormat.PNG, 100, write);
			} else {
				bitmap.compress(CompressFormat.JPEG, 100, write);
			}
		}
	}
	
	/**
	 * 圆形图片
	 * @param bitmap
	 * @return bitmap
	 */
	public static Bitmap toRound(Bitmap bitmap) {
		if (bitmap != null) {
			byte[] bytes = getBitmapByte(bitmap);
			//使用图片先先进行压缩,以免outOfMemory
			bitmap = getBitmap(bytes, bitmap.getWidth(), bitmap.getHeight());
			int px = bitmap.getWidth() < bitmap.getHeight()?bitmap.getWidth():bitmap.getHeight();
			int width = px;
			int height = px;
			//创建输出bitmap,并保证是正方形的,才能切成圆形的
			Bitmap outBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(outBitmap);
			final int color =0xff424242;
			final Paint paint = new Paint();
			//根据原来的图片大小画一个矩形(正方形)
			final Rect rect = new Rect(0,0,px,px);
			final float roundPX = px/2;
			final float roundPY = px/2;
			final float radius = px / 2;
			paint.setAntiAlias(true);
			canvas.drawARGB(0,0,0,0);
			paint.setColor(color);
			//画圆
			canvas.drawCircle(roundPX, roundPY, radius, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			//将原来的bitmap画在圆形的画布上
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return outBitmap;
		}
		
		return null;

	}
	/**
	 * 圆角图片
	 * @param bitmap
	 * @param ratio
	 * @param pixels 度数,值越大圆角越大
	 * @return bitmap
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		if (bitmap != null) {
			byte[] bytes = getBitmapByte(bitmap);
			bitmap = getBitmap(bytes, bitmap.getWidth(), bitmap.getHeight());
			int px = bitmap.getWidth() < bitmap.getHeight()?bitmap.getWidth():bitmap.getHeight();
			int width = px;
			int height = px;
			Bitmap outBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(outBitmap);
			final int color =0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0,0,width,height);
			//图片显示区域
			final RectF rectF = new RectF(rect);
			final float radiusX = pixels;
			final float radiusY = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0,0,0,0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, radiusX, radiusY, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return outBitmap;
		}
		
		return null;
		
	}
}
