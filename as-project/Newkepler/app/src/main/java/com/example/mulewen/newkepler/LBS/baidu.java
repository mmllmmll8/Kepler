package com.example.mulewen.newkepler.LBS;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler.Callback;
import android.os.Bundle;
import android.os.Message;
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
			JSONObject job = new JSONObject();
	        try {
				job.put("lat", String.valueOf(arg0.getLatitude()));
				job.put("lng", String.valueOf(arg0.getLongitude()));
				job.put("accuracy", String.valueOf(arg0.getRadius()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        Editor edit = share.edit();
	        JSONObject gaode = new JSONObject();
	        try {
				gaode.put("lat", String.valueOf(String.valueOf(arg0.getLatitude())));
				gaode.put("lng", String.valueOf(String.valueOf(arg0.getLongitude())));
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			edit.putString("baidu", gaode.toString());
			edit.commit();
//	        Message message = new Message();
//	        Bundle bundle = new Bundle();
//	        bundle.putString("type", "record");
//	        bundle.putString("record", job.toString());
//	        message.setData(bundle);
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
