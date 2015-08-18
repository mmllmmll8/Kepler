package com.example.kepler.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;

import com.example.kepler.object.LBSInfo;
import com.example.kepler.object.POI_Info;
import com.example.kepler.tools.LBS_table_SQL;
import com.example.kepler.tools.POI_table_SQL;

public class mycallback implements Callback{
	
		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			Bundle bundle = arg0.getData();
			LBSInfo lbs = null;
			try {	
				lbs = new LBSInfo();
				if("record".equalsIgnoreCase((String) bundle.get("type"))){
					JSONObject jrecord = new JSONObject((String)bundle.get("record"));
					lbs.accuracy = jrecord.getDouble("accuracy");
					lbs.lat = jrecord.getDouble("lat");
					lbs.lng = jrecord.getDouble("lng");
					lbs.lbsid = jrecord.getString("lbsid");
					LBS_table_SQL.add(lbs);
				}
				else{
					JSONArray jpois = new JSONArray((String)bundle.get("pois"));
					if(jpois.length()>0){
						for(int i = 0;i<jpois.length();i++){
							JSONObject jpoi = jpois.getJSONObject(i);
							POI_Info poi = new POI_Info();
							poi.lbsid = jpoi.getString("lbsid");
							poi.id = jpoi.getString("id");
							poi.lat = jpoi.getDouble("lat");
							poi.lon = jpoi.getDouble("lng");
							poi.name = jpoi.getString("name");
							poi.Type = jpoi.getString("type");
							POI_table_SQL.add(poi);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		
}
