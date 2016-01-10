package com.example.mulewen.newkepler.LBS;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

public class gaode implements AMapLocationListener{
	
	public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
	LatLng point;
	double Accuracy;
	String username = null;
	int time = 10000;
	int scantime = 1000*60;
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
	boolean lasttime = true;
	SharedPreferences share = null;
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public gaode(Context activity,Callback callback)
	{
		this.context = activity;
		this.callback = callback;
		share = activity.getSharedPreferences("exam", 0);
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		init();
	}

	//list_to_json
	

	//��ʼ����λ
	protected void init() {
		Log.e("ok", "okokok");
		mLocationManagerProxy = LocationManagerProxy.getInstance(
				this.context.getApplicationContext());
		
		//�˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
		//ע�����ú��ʵĶ�λʱ��ļ���������ں���ʱ�����removeUpdates()������ȡ����λ����
		//�ڶ�λ�������ں��ʵ��������ڵ���destroy()����     
		//����������ʱ��Ϊ-1����λֻ��һ��
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 
				scantime, 
				80, 
				this);
		mLocationManagerProxy.setGpsEnable(true);
	}

	//��λ�÷����仯���߶�λ���ʱ�䵽ʱ�����ķ���
	//��һ�ζ�λ֮����ϵ���Χ��
	//�����¼�ʱ������ƶ��� �ظ���γ�ȣ����������������ѯpoi����������ܵ�
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		Log.e("ok", String.valueOf(amapLocation.getAMapException().getErrorCode()));
		if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
	        //��ȡλ����Ϣ
		
	        Double geoLat = amapLocation.getLatitude();
	        Double geoLng = amapLocation.getLongitude();
	        String city = amapLocation.getCityCode();
	        Editor edit = share.edit();
	        JSONObject gaode = new JSONObject();
	        try {
				gaode.put("lat", String.valueOf(geoLat));
				gaode.put("lng", String.valueOf(geoLng));
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			edit.putString("gaode", gaode.toString());
			edit.commit();
	        Log.e("latlng", String.valueOf(geoLat)+" "+ String.valueOf(geoLng));
	        LatLng nowll = new LatLng(geoLat, geoLng);
	        //Accuracy = amapLocation.getAccuracy();
	        Accuracy = amapLocation.getAccuracy();
	        Accuracy=Accuracy<100?100:Accuracy;
	        //�����ʲôʱ���ʺ���pois
	        //�����ζ�λû�г�����������ϴζ�λ�����˾���
	        if(point!=null){
	        	if((AMapUtils.calculateLineDistance(nowll,point)<=80)){
	        		if(lasttime){
	        			Log.e("latlng", "record");
			        	Date now=new Date();
						String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
						getpoi.start(
								date, 
								Accuracy,
								new LatLonPoint(geoLat, geoLng),
								city,
								context);
//                     new LatLonPoint(30.557973,104.001632)
//						newgetpoi.start(
//								date, 
//								Accuracy, 
//								new LatLonPoint(30.557605,104.003185), 
//								city, 
//								context);
						lasttime = false;
	        		}
		        }
	        	else{
	        		lasttime = true;
	        	}
	        }
			point = new LatLng(geoLat, geoLng);
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
