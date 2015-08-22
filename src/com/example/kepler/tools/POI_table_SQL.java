package com.example.kepler.tools;

import java.io.File;
import java.util.ArrayList;

import com.example.kepler.object.LBSInfo;
import com.example.kepler.object.POI_Info;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

public class POI_table_SQL {
	
	static SQLiteDatabase db;
	static String username;
	public static void init(String userid,Context context)
	{
		username = userid;
		String SDCardPath = Environment.getExternalStorageDirectory().getPath();
		File dirFile = new File(SDCardPath+"/"+"tables");
		if (!dirFile.exists()) { 
			dirFile.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(
				dirFile+"/POI_table.db3",
				null);
		db.execSQL("create table if not exists poi"
				  + "(id varchar(25)," 
				  + " userid varchar(25)," 
				  + " lbsid varchar(25),"
				  + " latitude double,"
				  + " longitude double,"
				  + " name varchar(25),"
				  + " type varchar(25)," 
				  + " CreatedTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))," 
				  + " primary key(id,CreatedTime))");
	}
	
	public static void add(POI_Info poi)
	{
		try
		{
			db.execSQL("insert into poi values(?,?,?,?,?,?,?,datetime())",
				new String[]{
					  poi.id,
					  username,
					  poi.lbsid,
					  String.valueOf(poi.lat),
					  String.valueOf(poi.lon),
					  poi.name,
					  poi.Type
				}
			);
			//+ poi.id+ ","
			//+ String.valueOf(poi.lat)+ ","
			//+ String.valueOf(poi.lon)+ ","
			//+ poi.name+ ","
			//+ poi.Type+ ","
			//+ "1400"+ ")");
			//Cursor cursor = db.rawQuery("", null);
		}
		catch(SQLiteException e)
		{
			db.execSQL("create table if not exists poi"
					  + "(id varchar(25)," 
					  + " userid varchar(25)," 
					  + " lbsid varchar(25),"
					  + " latitude double,"
					  + " longitude double,"
					  + " name varchar(25),"
					  + " type varchar(25)," 
					  + " CreatedTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))," 
					  + " primary key(id,CreatedTime))");
					  //db.execSQL("alter table poi_table.poi add id int primary key");
					  //db.execSQL("alter table poi_table.poi add latitude double");
					  //db.execSQL("alter table poi_table.poi add longitude double");
					  //db.execSQL("alter table poi_table.poi add name varchar(25)");
					  //db.execSQL("alter table poi_table.poi add type varchar(25)");
					  //db.execSQL("alter table poi_table.poi add score int");
		}
	}
	
	public static ArrayList<POI_Info> search(){
		ArrayList<POI_Info> pois = new ArrayList<POI_Info>();
		try{
    		Cursor cursor = db.rawQuery("SELECT * FROM poi", null);
			while(cursor.moveToNext()){
				POI_Info poi = new POI_Info();
				poi.id = cursor.getString(0);
				poi.userid = cursor.getString(1);
				poi.lbsid = cursor.getString(2);
				poi.lat = cursor.getDouble(3);
				poi.lon = cursor.getDouble(4);
				poi.name = cursor.getString(5);
				poi.Type = cursor.getString(6);
				poi.date = cursor.getString(7);
				pois.add(poi);
			}		
			cursor.close();
		}
		catch(Exception e){
			System.out.print("haha");
		}
		return pois;
	}
}
