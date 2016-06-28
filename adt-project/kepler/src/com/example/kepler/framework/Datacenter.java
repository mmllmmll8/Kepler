package com.example.kepler.framework;
//封装shareprefrence,可以不使用
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by mulewen on 16/1/30.
 */
public class Datacenter {

    private static Datacenter datacenter = null;
    private Datashare datashare = null;//使用方法，传入字符串数组，数组保存的是share的名字
    private DataSqlite dbs = null;//使用方法，传入哈希数组，哈希中存储数据库名，和建表所需的sql语句
    public static Datacenter getDatacenter(Context context){
        if(datacenter==null){
            datacenter = new Datacenter(context);
        }
        return datacenter;
    }

    private Datacenter(Context context){
        ArrayList<String> names = new ArrayList<String>();
        names.add("exam");
        datashare = new Datashare(names,context);
    }

    public Datashare getshared(){
        return datashare;
    }
}
