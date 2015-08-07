package com.example.kepler.tools;

import com.example.kepler.object.POI_Info;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

public class POI_table_SQL {
	
	static SQLiteDatabase db;
	
	public static void init(Context context)
	{
		db = SQLiteDatabase.openOrCreateDatabase(
				Environment.getExternalStorageDirectory()+"/POI_table.db3",
				null);
	}
	
	public static void add(POI_Info poi)
	{
		try
		{
			db.execSQL("insert into poi values(?,?,?,?,?)",
					new String[]{
						  poi.id,
						  String.valueOf(poi.lat),
						  String.valueOf(poi.lon),
						  poi.name,
						  poi.Type
					}
			);
//					+ poi.id+ ","
//					+ String.valueOf(poi.lat)+ ","
//					+ String.valueOf(poi.lon)+ ","
//					+ poi.name+ ","
//					+ poi.Type+ ","
//					+ "1400"+ ")");
			Cursor cursor = db.rawQuery("", null);
		}
		catch(SQLiteException e)
		{
			db.execSQL("create table if not exists poi"
					  + "(id varchar(25) primary key,"
					  + " latitude double,"
					  + " longitude double,"
					  + " name varchar(25),"
					  + " type varchar(25)");
//			db.execSQL("alter table poi_table.poi add id int primary key");
//			db.execSQL("alter table poi_table.poi add latitude double");
//			db.execSQL("alter table poi_table.poi add longitude double");
//			db.execSQL("alter table poi_table.poi add name varchar(25)");
//			db.execSQL("alter table poi_table.poi add type varchar(25)");
//			db.execSQL("alter table poi_table.poi add score int");
		}
	}
	public static void change_score(String id , int score)
	{
		ContentValues values = new ContentValues();
		values.put("score", score);
		
		try
		{
			db.update("poi", values, "id=?", new String[]{id});
		}
		catch(SQLiteException e)
		{
			
		}
	}
	public static int search_score(String id)
	{
		try
		{
			Cursor cursor = db.rawQuery("select * from poi where id= ?",new String[]{id});
			if(cursor.moveToNext())
			{
				return cursor.getInt(cursor.getColumnIndex("score"));				
			}
			else
			{
				return -1;
			}
		}
		catch(SQLiteException e)
		{
			return -1;
		}
	}
}
