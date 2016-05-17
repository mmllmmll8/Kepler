package com.example.kepler.tools;

import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.kepler.runnables.lbss_runnable;
import com.example.kepler.runnables.nrecs_runnable;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;


public class NetworkConnectChangedReceiver extends BroadcastReceiver{
    static boolean laststate = true;
    static ArrayList<Runnable> runnables = null;

    public static void init(Context context){
    	if(runnables==null){
    		runnables = new ArrayList<Runnable>();
    		Runnable runnable1 = new nrecs_runnable(context);
    		Runnable runnable2 = new lbss_runnable(context);
    		runnables.add(runnable1);
    		runnables.add(runnable2);    		
    	}
    }
    
    public static void setrunnables(ArrayList<Runnable> runnable){
        runnables = runnable;
    }

    @SuppressLint("NewApi") @Override
    public void onReceive(final Context context, Intent intent) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
       
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                State state = networkInfo.getState();
                boolean isConnected = state == State.CONNECTED;// 当然，这边可以更精确的确定状态
                if(isConnected&&!laststate){
                    Log.e("receiver", "111111");
                    init(context);
                    new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							connectServer();
						}
					}).start();
                }else {
                    Log.e("receiver", "222222");
                }
                laststate = isConnected;
            }
        }
    }

    
    
    public static void connectServer(){
        Log.e("@@@@@@@@@@@@@", "######################");
        
        // 发送请求
        
    	new Thread(new Runnable() {
			public void run() {
				try
				{
					HttpGet httpGet = new HttpGet("http://121.42.136.178:8080/kepler-server/SendData");
					HttpClient httpClient = new DefaultHttpClient();
					HttpResponse response = httpClient.execute(httpGet);
		            int code = response.getStatusLine().getStatusCode();
		            if(code==200)
		            {
		                Log.e("########LoginActivity","wifi!!!!!!!!!");
		                for (Runnable a:runnables
		                     ) {
		                    Thread thread = new Thread(a);
		                    thread.start();
		                }
		            }
				}
		        catch (Exception e)
		        {
		            e.printStackTrace();
		        }
			}
		}).start();
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager  
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
            if (mWiFiNetworkInfo != null) {  
                return mWiFiNetworkInfo.isAvailable();  
            }  
        }  
        return false;  
    }

}
