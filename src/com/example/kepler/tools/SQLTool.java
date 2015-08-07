package com.example.kepler.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLTool {
	private static SQLiteDatabase db;
	
	public static void creat_table(String name,Context context){
		db = SQLiteDatabase.openOrCreateDatabase(
				context.getFilesDir().toString()+"/"+name,
				null);
	}
	
	
}
