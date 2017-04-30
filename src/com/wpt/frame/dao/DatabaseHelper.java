package com.wpt.frame.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * @author liujun SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能
 *         第一，getReadableDatabase
 *         ()、getWritableDatabase()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作
 *         第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "shediao.db";
	private final static int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		// 必须通过super调用父类当中的构造函数
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
