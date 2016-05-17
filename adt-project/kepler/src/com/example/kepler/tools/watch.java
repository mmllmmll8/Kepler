package com.example.kepler.tools;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

public class watch {

	private static final String PACKAGE = "com.example.dameonservice/";
	private String mMonitoredService = "";
	private volatile boolean bHeartBreak = false;
	private Context mContext;
	private boolean mRunning = true;

	public void createAppMonitor(String userId)
	{
		if( !createwatch(userId) )
		{
			Log.e("Watcher", "<<Monitor created failed>>");
		}
	}

	public watch( Context context)
	{
		mContext = context;
	}

	private int isServiceRunning()
	{
		ActivityManager am=(ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>)am.getRunningServices(1024); 
		for( int i = 0; i < runningService.size(); ++i )
		{
			if( mMonitoredService.equals(runningService.get(i).service.getClassName().toString() ))
			{
				return 1;
			}
		}
		return 0;
	}
	
	private native boolean createwatch(String userId);
	
	private native boolean connectToMonitor();

	private native int sendMsgToMonitor(String msg);
	static 
	{
		System.loadLibrary("com_example_kepler_tools_watch");
	}

}
