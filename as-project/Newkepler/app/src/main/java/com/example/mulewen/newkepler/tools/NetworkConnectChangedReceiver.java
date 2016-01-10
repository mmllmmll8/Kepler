package com.example.kepler.tools;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkConnectChangedReceiver extends BroadcastReceiver{
	Thread thread ;
    static boolean laststate = true;
    SharedPreferences share = null;
    String content = null;
    @SuppressLint("NewApi") @Override
    public void onReceive(final Context context, Intent intent) {
        share = context.getSharedPreferences("exam", 0);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        content = share.getString("nrecs", "");
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (content!=""&&WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                State state = networkInfo.getState();
                boolean isConnected = state == State.CONNECTED;// 当然，这边可以更精确的确定状态
                
                if(isConnected&&!laststate){
                    Log.e("receiver", "11111"); 
                    connectServer(context); 
                }else {
                    Log.e("receiver", "222222");
                }
                laststate = isConnected;
            }
        }
    }
    final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
            	Log.e("haha", "ahahahah");
                try {
                	JSONArray info = new JSONArray(content);
                	JSONObject haha = new JSONObject();
//                	JSONArray info = new JSONArray();
//                	haha.put("userid", "caoma");
//                	haha.put("date", "cnima");
//                	haha.put("poiinfo", "caonima");
//                	haha.put("lbsinfo", "caonima");
                	info.put(haha);
                	JSONObject jsonobject = new JSONObject();
                	jsonobject.put("type","add");
                	JSONArray jarray = new JSONArray();
                	jsonobject.put("items",info);
                	String strUTF8 = jsonobject.toString();

                	HttpClient httpClient=new DefaultHttpClient();
                	httpClient.getParams().setParameter(AllClientPNames.HTTP_CONTENT_CHARSET, "utf-8");
                	//HttpPost post = new HttpPost("http://121.42.136.178:10086/RecordHandler.ashx");
                	HttpPost post = new HttpPost("http://192.168.1.143:8080/kepler_server/SendData");
                	StringEntity entity = new StringEntity(strUTF8);
                	entity.setContentEncoding("utf-8");
                	post.setEntity(entity);
                	//Log.e("haha4",strUTF8);
                    HttpResponse response=httpClient.execute(post);
                    if(response.getStatusLine().getStatusCode()==200)
                    {
                    	Editor editer = share.edit();
                    	editer.putString("nrecs", "");
                    	editer.commit();
                    }
                    Log.e("haha",String.valueOf(response.getStatusLine().getStatusCode()));
                } 
                catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                catch (IOException e) {
                        // TODO Auto-generated catch block
                	Log.e("haha", e.getMessage()); 
                        e.printStackTrace();
                }
            }
        };
    private void connectServer(Context context){
        List<NameValuePair> params=new ArrayList();
        //添加参数

        Log.v("@@@@@@@@@@@@@", "######################");
        HttpGet httpGet = new HttpGet("https://www.baidu.com/");
        HttpClient httpClient = new DefaultHttpClient();
        // 发送请求
        try
        {
        	
            HttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if(code==200)
            {
               Log.e("############LoginActivity","wifi!!!!!!!!!!!!!!!!!");
	           thread = new Thread(runnable);
	           thread.start();
	        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean isWifiConnected(Context context) {  
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
//JSONObject jobject = new JSONObject();
//jobject.put("date", "2015-11-15");
//jobject.put("userid", "tonypod");
//jobject.put("lbsinfo", "asdfasdf");
//jobject.put("poiinfo", "caoadsfasdfsdafasdfnima");
//jarray.put(info);
//String haha = URLDecoder.decode("%E5%95%86%E5%8A%A1%E4%BD%8F%E5%AE%85%3B%E4%BD%8F%E5%AE%85%E5%8C%BA%3B%E5%AE%BF%E8%88%8D","utf-8");
//Log.e("haha2",haha);
//strUTF8 = URLEncoder.encode(strUTF8,"utf-8");
//Log.e("haha3",strUTF8);
//URLEncoder.encode(strUTF8, "utf-8");
//Log.e("haha", strUTF8);
//HttpClient httpClient=new DefaultHttpClient();
//HttpPost httpPost=new HttpPost("http://121.42.136.178:10086/RecordHandler.ashx");
//httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//StringEntity stringentity = new StringEntity(strUTF8);
//stringentity.setContentEncoding("utf-8");
//httpPost.setEntity(stringentity);