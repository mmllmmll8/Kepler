package com.example.kepler.lbs;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler.Callback;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class baidu {
	
	private LocationClient mLocationClient = null;
	private int ScanSpan = 1000*60;
	private Context context = null;
	Callback callback;
	SharedPreferences share = null;
	public BDLocationListener myListener = new BDLocationListener() {
		
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
	        Editor edit = share.edit();
	        JSONObject baidu = new JSONObject();
	        try {
	        	baidu.put("lat", String.valueOf(String.valueOf(arg0.getLatitude())));
	        	baidu.put("lng", String.valueOf(String.valueOf(arg0.getLongitude())));
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	        }
			edit.putString("baidu", baidu.toString());
			edit.commit();
		}
	};
	
	public baidu(Context a,Callback callback) {
		this.context = a;
		this.callback = callback;
		share = a.getSharedPreferences("exam", 0);
	    mLocationClient = new LocationClient(a.getApplicationContext());     //����LocationClient��
	    LocationClientOption loc = new LocationClientOption();
	    loc.setScanSpan(ScanSpan);
	    loc.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
	    mLocationClient.setLocOption(loc);
	    mLocationClient.registerLocationListener(myListener);    //ע���������
	}
	
	public void start()
	{
		mLocationClient.start();
	}
	
	public void stop()
	{
		mLocationClient.stop();
	}
	
}
