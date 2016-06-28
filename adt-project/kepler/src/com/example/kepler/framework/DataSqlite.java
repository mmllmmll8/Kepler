package com.example.kepler.framework;
//封装sqlite,可以不使用
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mulewen on 16/1/30.
 */
public class DataSqlite {
    //保存大量格式化数据
    private HashMap<String,SQLiteDatabase> tables = null;

    public DataSqlite(Context context,ArrayList<HashMap<String,String>> dbs){
        //初始化建表工作
        //recsql、lbssql
        //初始化信息有名字和创建数据表的sql语句
        tables = new HashMap<String, SQLiteDatabase>();
        for (HashMap<String,String> db: dbs) {
            String dbname = db.get("name");
            String sql = db.get("creattable");
            if(sql!=""&&sql!=""){
                SQLiteDatabase a =  context.openOrCreateDatabase(dbname+".db",context.MODE_PRIVATE,null);
                a.execSQL(sql);
                tables.put(dbname,a);
            }
        }
    }

    public void savedata(ArrayList<Object> datamap,String table_name,String db_name){
        Object a = datamap.get(0);
        //构建insert sql语句
        String sql = "insert into "+table_name+"(";
        String sql2 = "values(";
        Field[] fields = a.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {

            String name = fields[i].getName();
            sql += name + ",";
            sql2 += "?,";
        }
        sql = sql.substring(0,sql.length()-1)+")";
        sql2 = sql2.substring(0,sql2.length()-1)+")";

        SQLiteDatabase db = this.tables.get(db_name);
        SQLiteStatement stat = db.compileStatement(sql+sql2);
        //批量提交数据
        for(Iterator iter = datamap.iterator(); iter.hasNext();){
            Object object = iter.next();
            for (int i = 0;i<fields.length;i++){
                String value = "";
                try{
                    value = (String) fields[i].get(object);
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
                stat.bindString(i+1, value);
            }
            stat.executeInsert();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

}
//    private String Creatrec = "CREATE TABLE IF NOT EXISTS rec" +
//                             "(userid VARCHAR(32) PRIMARY KEY NOT NULL," +
//                             " date    TEXT       NOT NULL" +
//                             " lbsinfo TEXT       NOT NULL" +
//                             " poiinfo TEXT       NOT NULL)";
//
//    private String Creatlbs = "CREATE TABLE IF NOT EXISTS rec" +
//                              "(userid VARCHAR(32) PRIMARY KEY NOT NULL," +
//                              " date    TEXT       NOT NULL" +
//                              " lbsinfo TEXT       NOT NULL";
