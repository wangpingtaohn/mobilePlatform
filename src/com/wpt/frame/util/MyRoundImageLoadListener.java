package com.wpt.frame.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MyRoundImageLoadListener implements ImageLoadingListener{

	private int mType;
	
	private int mPixels;
	
	public static final int ROUND = 0;
	
	public static final int ROUND_CONNER = 1;
	
	public MyRoundImageLoadListener(int type) {
		this.mType = type;
	}
	public MyRoundImageLoadListener(int type, int pixels) {
		this.mType = type;
		this.mPixels = pixels;
	}
	@Override
	public void onLoadingStarted(String imageUri, View view) {
		
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (loadedImage != null) {
			ImageView imageView = (ImageView) view;
			if (ROUND == mType) {
				loadedImage = BitmapUtil.toRound(loadedImage);
			} else if (ROUND_CONNER == mType) {
				loadedImage = BitmapUtil.toRoundCorner(loadedImage, mPixels);
			}
			imageView.setImageBitmap(loadedImage);
		}
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		
	}

}
