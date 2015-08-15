package com.example.kepler.tools;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;

public class GetPhoneStateTool {
	public static String getphoneinfo(Context context){
		TelephonyManager telemamager =  (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber = telemamager.getLine1Number();
		String mtype = android.os.Build.MODEL; // 手机型号  
		String mtyb= android.os.Build.BRAND;//手机品牌  
		JSONObject phoneinfo = new JSONObject();
		try {
			phoneinfo.put("phonenumber", NativePhoneNumber);
			phoneinfo.put("phonetype", mtype);
			phoneinfo.put("phonebrand", mtyb);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return phoneinfo.toString();
	}
}
