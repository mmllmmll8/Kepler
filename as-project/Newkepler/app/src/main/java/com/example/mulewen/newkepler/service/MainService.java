package com.example.mulewen.newkepler.service;


import com.example.mulewen.newkepler.LBS.baidu;
import com.example.mulewen.newkepler.LBS.gaode;
import com.example.mulewen.newkepler.LBS.tencent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service{
	baidu baidu_server;
	gaode gaode_server;
	tencent tencent_server;
	
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
