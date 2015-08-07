package com.example.kepler.tools;

import java.sql.Date;

import com.example.kepler.object.POI_Info;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class record_SQL {
    static SQLiteDatabase db;
	
	public static void init(Context context)
	{
		db = SQLiteDatabase.openOrCreateDatabase(
				context.getFilesDir().toString()+"/record.db3",
				null);
	}
	
	public static void add(POI_Info poi)
	{
		try
		{
			db.execSQL("insert into record_table value(?,?,?,?,?)",
						new String[]{
							poi.id,
							null,
							String.valueOf(poi.lat),
							String.valueOf(poi.lon),
							poi.Type});
			Cursor cursor = db.rawQuery("", null);
		}
		catch(SQLiteException e)
		{
			db.execSQL("create table record_table"
					  + "(id integer"
					  + " CreatedTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))"
					  + " latitude double"
					  + " longitude double"
					  + " type varchar(25)"
					  + " primary key(id,CreatedTime))");
		}
	}
}
