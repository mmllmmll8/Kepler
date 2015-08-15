package com.example.kepler.service;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.kepler.object.LBSInfo;
import com.example.kepler.object.POI_Info;
import com.example.kepler.tools.HttpTool;
import com.example.kepler.tools.LBS_table_SQL;
import com.example.kepler.tools.POI_table_SQL;
import com.example.kepler.tools.object_to_json;

import android.os.Handler.Callback;
import android.os.Message;

public class WIFIcallback implements Callback{

	String username = null;
	public WIFIcallback(String username){
		this.username = username;
	}
	
	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		JSONObject jsonobject = new JSONObject();
		try {
			jsonobject.put("poi", POI_table_SQL.search());
			jsonobject.put("lbs", LBS_table_SQL.search());
			jsonobject.put("userid",username);
			//HttpTool.post("http://127.0.0.1/", jsonobject.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		  catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return false;
	}
}
