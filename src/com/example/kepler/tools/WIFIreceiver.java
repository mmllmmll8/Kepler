package com.example.kepler.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;


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
                	callbackfunction.handleMessage(null);
                } else {
                	
                }
            }
        }
    }
}