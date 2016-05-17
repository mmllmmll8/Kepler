package com.example.kepler.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Currency;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.kepler.lbs.newgetpoi;
import com.example.kepler.object.LBSInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Sql_Tool {
    private String table_name = null;
    private Context context = null;
    private static SQLiteDatabase db = null;
    private Field[] fields = null;
    
    public Sql_Tool(String table_name,final String sql,Context context,Object object){
    	this.table_name = table_name;
	 	this.context = context;
    	fields = ((Class<?>) object).getFields();
    	if(this.db==null){
    		this.db = context.openOrCreateDatabase("exam.db",this.context.MODE_MULTI_PROCESS,null);    		
    	}
    	// TODO Auto-generated method stub
		db.execSQL(sql);
    }
	
    public Sql_Tool(String table_name,Context context,Object object){
    	this.table_name = table_name;
    	this.context = context;
    	fields = ((Class<?>) object).getFields();
    	if(this.db==null){
    		this.db = context.openOrCreateDatabase("exam.db",this.context.MODE_MULTI_PROCESS,null);    		
    	}
    }
    
    public void clean(){
    	if(db!=null){
    		String sqlsString = "DELETE FROM " + table_name;
    		db.execSQL(sqlsString);
    	}
    }
    
    public String getcontent(){
    	if(db!=null){
    		String sqlsString = "SELECT * FROM " + table_name;
	    	Cursor cursor = db.rawQuery(sqlsString, null);
	    	JSONArray jsonArray = new JSONArray();
	    	while (cursor.moveToNext()) {  
	    		JSONObject jsonObject = new JSONObject();
	            for (int i = 0; i < fields.length; i++) {
					try {
						String nameString = fields[i].getName();
						String valueString = cursor.getString(cursor.getColumnIndex(nameString));
						jsonObject.put(nameString,valueString);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	            jsonArray.put(jsonObject);
	        } 
	    	return jsonArray.toString();
    	}
    	return "";
    }
    
	public void addsql(Object object){
		//数据库操作-mysql
		final ContentValues cv = new ContentValues();
		fields = object.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				cv.put(fields[i].getName(), String.valueOf(fields[i].get(object)));
				Log.e("addsql",fields[i].getName());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub	
				db.insert(table_name, null, cv);		
			}
		}).start();
	}
	
	public void close_connect(){
		db.close();
	}
}
//    public Sql_Tool(String table_name,String sql,Context context,Class<T> object){
//    	this.table_name = table_name;
//	 	this.context = context;
//    	fields = object.getClass().getFields();
//    	if(this.db==null){
//    		this.db = context.openOrCreateDatabase("exam.db",this.context.MODE_PRIVATE,null);    		
//    	}
//    	db.execSQL(sql);
//    }
//	
//    public Sql_Tool(String table_name,Context context,Class<T> object){
//    	this.table_name = table_name;
//    	this.context = context;
//    	fields = object.getClass().getFields();
//    	if(this.db==null){
//    		this.db = context.openOrCreateDatabase("exam.db",this.context.MODE_PRIVATE,null);    		
//    	}
//    }