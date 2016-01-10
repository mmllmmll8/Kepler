package com.example.mulewen.newkepler.LBS;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler.Callback;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

public class tencent{
	
		int time = 60000;
		Context context = null;
		Callback callback;
		
	    public tencent(Context avtivity,final Callback callback)
	    {
	    	this.context = avtivity;
	    	this.callback = callback;
	    	TencentLocationRequest request = TencentLocationRequest.create();
	    	request.setInterval(time);
	    	TencentLocationManager locationManager = TencentLocationManager.getInstance(context.getApplicationContext());
	    	TencentLocationListener listener = new TencentLocationListener() {
				
				@Override
				public void onStatusUpdate(String arg0, int arg1, String arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLocationChanged(TencentLocation arg0, int arg1, String arg2) {
					// TODO Auto-generated method stub
					if (TencentLocation.ERROR_OK == arg1) {
						
						JSONObject job = new JSONObject();
				        try {
							job.put("lat", String.valueOf(arg0.getLatitude()));
							job.put("lng", String.valueOf(arg0.getLongitude()));
							job.put("accuracy", String.valueOf(arg0.getAccuracy()));
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
						
				       
				    } else {
				        // ��λʧ��
				    	Log.e("tencent", "localing error");
				    }
				}
			};
			
	    	int error = locationManager.requestLocationUpdates(request,listener);
	    	
	    }
}
