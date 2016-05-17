package com.example.kepler.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import android.media.MediaPlayer;
import java.util.ArrayList;
import com.example.kepler.R;
import com.example.kepler.framework.Datacenter;
import com.example.kepler.lbs.baidu;
import com.example.kepler.lbs.gaode;
import com.example.kepler.object.REC_Info;
import com.example.kepler.sql.Sql_Tool;
import com.example.kepler.tools.NetworkConnectChangedReceiver;
import com.example.kepler.tools.watch;

public class MainService extends Service implements MediaPlayer.OnCompletionListener{
	baidu baidu_server;
	gaode gaode_server;
	MediaPlayer player;
	watch awatch;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.e("caonima", "service binder");
//		awatch = new watch(getApplicationContext());
//		awatch.createAppMonitor(Datacenter.getDatacenter(getApplicationContext()).getshared().getstring("exam", "id"));
//		player = MediaPlayer.create(this, R.raw.empty);  
//	    player.setOnCompletionListener(this);	
//	    player.setLooping(true);
//	    player.start(); 
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("service", "service create");
		mycallback callback = new mycallback();
		init(this.getApplicationContext(),callback);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		awatch = new watch(getApplicationContext());
		awatch.createAppMonitor(Datacenter.getDatacenter(getApplicationContext()).getshared().getstring("exam", "id"));
		Log.e("service", "service start");
		player = MediaPlayer.create(this, R.raw.empty);  
	    player.setOnCompletionListener(this);	
	    player.setLooping(true);
	    player.start();
	    Log.e("mediaplayer", "start");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		player.stop();
		Log.e("service", "service stop");
	}

	private void init(final Context context,mycallback callback){
		Log.e("service","service init");
		
		NetworkConnectChangedReceiver.init(context);
		NetworkConnectChangedReceiver.connectServer();
		
		gaode_server = new gaode(context,callback);
		//填充wifi触发的runnable
		final String rec_sql = "CREATE TABLE IF NOT EXISTS `recinfo` (" +
					"`date`    TEXT NOT NULL," +
					"`userid`  TEXT NOT NULL," +
					"`lbsinfo` TEXT," +
					"`poiinfo` TEXT," +
					"PRIMARY KEY (`date`, `userid`));";
		
		final String nrec_sql = "CREATE TABLE IF NOT EXISTS `nrecinfo` (" +
					"`date`    TEXT NOT NULL," +
					"`userid`  TEXT NOT NULL," +
					"`lbsinfo` TEXT," +
					"`poiinfo` TEXT," +
					"PRIMARY KEY (`date`, `userid`));";
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 new Sql_Tool("recinfo", rec_sql, getApplicationContext(),REC_Info.class);
				 new Sql_Tool("nrecinfo", nrec_sql, getApplicationContext(),REC_Info.class);
			}
		}).start();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		Log.e("mediaplayer", "complete");
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) @Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		Intent intent0 = new Intent(this,MainService.class);
		startService(intent0);  
	}
}
//		Notification notification = new Notification(
//				R.drawable.ic_launcher,
//				"wf update service is running",
//                System.currentTimeMillis());  
//
//		PendingIntent pintent=PendingIntent.getService(this, 0, intent, 0);  
//
//        notification.setLatestEventInfo(
//        		this, 
//        		"WF Update Service", 
//        		"wf update service is running！", 
//        		pintent);  
//
//        //让该service前台运行，避免手机休眠时系统自动杀掉该服务  
//        //如果 id 为 0 ，那么状态栏的 notification 将不会显示。  
//
//        startForeground(1, notification); 