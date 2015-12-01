package com.example.kepler.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kepler.R;
import com.example.kepler.LBS.baidu;
import com.example.kepler.LBS.gaode;
import com.example.kepler.LBS.tencent;
import com.example.kepler.object.LBSInfo;
import com.example.kepler.object.POI_Info;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MainService extends Service{
	baidu baidu_server;
	gaode gaode_server;
	tencent tencent_server;
	String userid;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//Bundle bundle =  intent.getBundleExtra("username");
		Log.e("ok", "okokokokokok");
		SharedPreferences share = getSharedPreferences("exam",0);
		Editor editor = share.edit();
		editor.commit();
		mycallback callback = new mycallback();
		init(this.getApplicationContext(),callback);
		flags = START_STICKY;  
		return super.onStartCommand(intent, flags, startId);  
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		baidu_server.stop();
		Log.e("ok", "nonononono");
		SharedPreferences share = getSharedPreferences("exam",0);
		Editor editor = share.edit();
		editor.commit();
	}
	
	private void init(Context context,mycallback callback){
		baidu_server = new baidu(context,callback);
		baidu_server.start();
		tencent_server = new tencent(context,callback);
		gaode_server = new gaode(context,callback);
	}
}
