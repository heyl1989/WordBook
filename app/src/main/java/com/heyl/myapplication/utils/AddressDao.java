package com.heyl.myapplication.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;

public class AddressDao {
	
	public static String queryAddress(String num,Context context){
		File file =  new File(context.getFilesDir(), "dictionary.db");
		String location="";
		if(!file.exists()){
			Toast.makeText(context,"字典加载失败,请退出程序重新进入",Toast.LENGTH_SHORT).show();
		}else{
			//打开数据库
			//file:///android_asset/address.db  打开本地文件的操作，不能打开assets目录下的数据库，可以打开html文件
			//一般都是将数据库从assets目录下拷贝到手机目录中，通过打开手机目录，打开数据库
			SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
			Cursor cursor = openDatabase.rawQuery("select * from t_words where english=?",new String[]{num});
			if(cursor.moveToFirst()) {
				location = cursor.getString(cursor.getColumnIndex("chinese"));
			}
			cursor.close();
			openDatabase.close();
		}
		return location;
	}
}
