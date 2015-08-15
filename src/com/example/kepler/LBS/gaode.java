package com.example.kepler.LBS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.StrictMode;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;

public class gaode implements AMapLocationListener{
	
	public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
	LatLonPoint point;
	double Accuracy;
	String username = null;
	int time = 10000;
	int scantime = 1000;
	int fanwei = 50;
	Context context = null;
	HashMap<String, Object> map;
	public static ListView listview ;
	public static ArrayList<Map<String, Object>> lists;
	static SimpleAdapter adapter;
	Thread timer;
	PendingIntent mPendingIntent;
	protected LocationManagerProxy mLocationManagerProxy;
	Callback callback;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public gaode(Context activity,Callback callback)
	{
		this.context = activity;
		this.callback = callback;
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		init();
	}

	//list_to_json
	

	//初始化定位
	protected void init() {
	 
		mLocationManagerProxy = LocationManagerProxy.getInstance(this.context.getApplicationContext());
		
		//此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		//注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
		//在定位结束后，在合适的生命周期调用destroy()方法     
		//其中如果间隔时间为-1，则定位只定一次
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 
				scantime, 
				15, 
				this);
		mLocationManagerProxy.setGpsEnable(true);
	}

	//当位置发生变化或者定位间隔时间到时触发的方法
	//第一次定位之后加上电子围栏
	//触发事件时，如果移动， 回复经纬度，如果不动更新误差，查询poi，计算最可能点
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		
		if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
	        //获取位置信息
	        Double geoLat = amapLocation.getLatitude();
	        Double geoLng = amapLocation.getLongitude();
	        Accuracy = amapLocation.getAccuracy();
	        JSONObject job = new JSONObject();
	        try {
				job.put("lat", String.valueOf(geoLat));
				job.put("lng", String.valueOf(geoLng));
				job.put("accuracy", String.valueOf(Accuracy));
				Date now=new Date();
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
				getpoi.start(date, new LatLonPoint(geoLat, geoLng), context, callback);
				job.put("lbsid", date);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        Message message = new Message();
	        Bundle bundle = new Bundle();
	        bundle.putString("type", "record");
	        bundle.putString("record", job.toString());
	        message.setData(bundle);
	        synchronized (callback) {
				callback.handleMessage(message);
			}
	    }
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}


}
