package com.example.kepler.tools;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Handler.Callback;
import android.os.Parcelable;


public class WIFIreceiver extends BroadcastReceiver {
    private Context context;
    private Callback callbackfunction;
    
    public WIFIreceiver(Callback a){
    	this.callbackfunction = a;
    }
    
    @Override
    public void onReceive(final Context context, Intent intent) {
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                State state = networkInfo.getState();
                boolean isConnected = state.equals(State.CONNECTED);// 当然，这边可以更精确的确定状态
                if (isConnected) {
                	JSONObject jsonobject = new JSONObject();
            		try {
            			jsonobject.put("poi", POI_table_SQL.search());
            			jsonobject.put("lbs", LBS_table_SQL.search());
            			//HttpTool.post("http://127.0.0.1/", jsonobject.toString());
            		} catch (JSONException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		} 
//            		  catch (IOException e) {
//            			// TODO Auto-generated catch block
//            			e.printStackTrace();
//            		}
                } else {
                	
                }
            }
        }
    }
}