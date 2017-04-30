package com.wpt.frame.util;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyImageLoaderUtil {

	private Context mContext;

	public MyImageLoaderUtil(Context context) {
		this.mContext = context;
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				mContext).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.enableLogging().build();
		ImageLoader.getInstance().init(configuration);
	}

	public void displayImage(String url, ImageView imageView) {

		DisplayImageOptions optionsImg = new DisplayImageOptions
				.Builder()
				.cacheInMemory()
				.cacheOnDisc()
				.build();

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(url, imageView, optionsImg);
	}

	public void displayImage(String url, ImageView imageView,
			int defaultImg) {
		int stubImageId = defaultImg;
		int failedImageId = defaultImg;
		int emptyImageId = defaultImg;

		DisplayImageOptions optionsImg = new DisplayImageOptions
				.Builder()
				.showStubImage(stubImageId)
				.showImageForEmptyUri(emptyImageId)
				.showImageOnFail(failedImageId)
				.cacheInMemory()
				.cacheOnDisc()
				.build();

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(url, imageView, optionsImg);
	}

	public void displayImage(String url, ImageView imageView,MyRoundImageLoadListener listener) {

		DisplayImageOptions optionsImg = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.build();

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(url, imageView, optionsImg, listener);
	}
	public void displayImage(String url, ImageView imageView,ImageLoadingListener listener) {
		
		DisplayImageOptions optionsImg = new DisplayImageOptions.Builder()
		.cacheInMemory().cacheOnDisc()
		.build();
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(url, imageView, optionsImg, listener);
	}
	public void displayImage(String url, ImageView imageView,
			int defaultImg, MyRoundImageLoadListener listener) {
		int stubImageId = defaultImg;
		int failedImageId = defaultImg;
		int emptyImageId = defaultImg;
		
		DisplayImageOptions optionsImg = new DisplayImageOptions.Builder()
		.showStubImage(stubImageId).showImageForEmptyUri(emptyImageId)
		.showImageOnFail(failedImageId).cacheInMemory().cacheOnDisc()
		.build();
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(url, imageView, optionsImg, listener);
	}
	public void loadImage(String url, ImageLoadingListener listener) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage(url, listener);
	}

	public void clearCache() {
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiscCache();
	}

	public String getCacheSize() {
		Log.d("file location", StorageUtils.getCacheDirectory(mContext)
				.getAbsolutePath());
		FileSize fileSize = new FileSize(
				StorageUtils.getCacheDirectory(mContext));
		return fileSize.toString();
	}
}
