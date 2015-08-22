package com.example.kepler.tools;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kepler.object.LBSInfo;
import com.example.kepler.object.POI_Info;
public class object_to_json {
	public static String poijson(ArrayList<POI_Info> a){
		JSONArray jsonarray = new JSONArray();
		for(POI_Info b:a){
			JSONObject jobject = new JSONObject();
			try {
				jobject.put("id", b.id);
				jobject.put("lbsid", b.lbsid);
				jobject.put("lat",b.lat);
				jobject.put("lon",b.lon);
				jobject.put("name",b.name);
				jobject.put("userid",b.userid);
				jobject.put("date",b.date);
				jobject.put("type",b.Type);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonarray.put(jobject);
		}
		return jsonarray.toString();
	}
	
	public static String lbsjson(ArrayList<LBSInfo> a){
		JSONArray jsonarray = new JSONArray();
		for(LBSInfo b:a){
			JSONObject jobject = new JSONObject();
			try {
				jobject.put("id", b.lbsid);
				jobject.put("lat",b.lat);
				jobject.put("lon",b.lng);
				jobject.put("date",b.date);
				jobject.put("accuracy",b.accuracy);
				jobject.put("userid",b.userid);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonarray.put(jobject);
		}
		return jsonarray.toString();
	}
}
