package com.example.kepler.lbs;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.StrictMode;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.example.kepler.framework.Datacenter;
import com.example.kepler.framework.Datashare;
import com.example.kepler.framework.LBS_info_mid;
import com.example.kepler.object.LBSInfo;
import com.example.kepler.sql.Sql_Tool;

public class gaode implements AMapLocationListener{

	LatLng point;
	double Accuracy;
	int scantime = 1000*10;
	float scanwide = 80;
	Context context = null;
	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	public AMapLocationClientOption mLocationOption = null;
	Callback callback;
	static boolean lasttime;
//	LBS_info_mid lbs_info_mid;
	Sql_Tool lbSql_Tool = null; 
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public gaode(Context activity,Callback callback,int time)
	{
		this.context = activity;
		this.callback = callback;
		this.lasttime = true;
		this.scantime = time;
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		init();
	}

	protected void init() {
		Log.e("gaode", "init");
		//初始化定位
		mLocationClient = new AMapLocationClient(context);
		//设置定位回调监听
		mLocationClient.setLocationListener(this);
		//初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(scantime);
		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//启动定位
		mLocationClient.startLocation();
//		lbs_info_mid = LBS_info_mid.getlbsinfomid(context);
		final String sql = "CREATE TABLE IF NOT EXISTS `lbsinfo`(" +
						   "`date`     TEXT NOT NULL," +
						   "`userid`   TEXT NOT NULL," +
						   "`lat`	   TEXT," +
						   "`lng` 	   TEXT," +
						   "`accuracy` TEXT," +
						   "PRIMARY    KEY (`date`, `userid`))";
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				lbSql_Tool = new Sql_Tool("lbsinfo", sql, context,LBSInfo.class);
			}
		}).start();
	}
	
	

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		//
		// TODO Auto-generated method stub
		Log.e("ok", String.valueOf(amapLocation.getErrorCode()));
		if(amapLocation != null && amapLocation.getErrorCode() == 0){

	        Double geoLat = amapLocation.getLatitude();
	        Double geoLng = amapLocation.getLongitude();
	        String city = amapLocation.getCityCode();
	        JSONObject gaode = new JSONObject();
	        try {
				gaode.put("lat", String.valueOf(geoLat));
				gaode.put("lng", String.valueOf(geoLng));
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Datashare datashare = Datacenter.getDatacenter(context).getshared();
			datashare.Savedata("gaode",gaode.toString(),"exam");
	        Log.e("now latlng", String.valueOf(geoLat)+" "+ String.valueOf(geoLng));
	        LatLng nowll = new LatLng(geoLat, geoLng);
			Date now=new Date();
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
			Log.e("last latlng", String.valueOf(datashare.getfloat("lat","exam"))+" "+ String.valueOf(datashare.getfloat("lng","exam")));
			point = new LatLng(datashare.getfloat("lat","exam"),datashare.getfloat("lng","exam"));
	        //point = new LatLng(0,0);
			Accuracy = amapLocation.getAccuracy();
	        double Accurac = Accuracy<100?100:Accuracy;
			if((AMapUtils.calculateLineDistance(nowll,point)<=120)){
				//没跳出地理围栏
				if(lasttime){
					getpoi.start(
						date,
						Accurac,
						new LatLonPoint(geoLat, geoLng),
						city,
						context);
					lasttime = false;
				}
			}else{
				Log.e("lasttime", "true");
				//跳出了地理围栏
				lasttime = true;
			}
			LBSInfo lbsInfo = new LBSInfo();
			final SharedPreferences share = context.getSharedPreferences("exam",0);
			lbsInfo.userid = share.getString("id", "");
			lbsInfo.date = date;
			lbsInfo.accuracy = Accuracy;
			lbsInfo.lng = geoLng;
			lbsInfo.lat = geoLat;
			lbSql_Tool.addsql(lbsInfo);
			Float lat = Float.valueOf(geoLat.toString());
			Float lng = Float.valueOf(geoLng.toString());
			datashare.Savedata("lat",lat,"exam");
			datashare.Savedata("lng",lng,"exam");
	    }
	}
}