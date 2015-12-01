package com.example.kepler.LBS;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

public class getpoi {
	public static void start(final String date,double error,LatLonPoint point ,String city,final Context context){
		Query query = new Query("", "141200", city);
        query.setPageSize(30);
		PoiSearch poisearch = new PoiSearch(context, query);
		poisearch.setOnPoiSearchListener(new OnPoiSearchListener(){
			@Override
			public void onPoiSearched(PoiResult arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1 == 0){
					if (arg0 != null&&arg0.getQuery()!=null) {
						List<PoiItem> poiItems = arg0.getPois();
						if(poiItems!=null){
							Iterator itr = poiItems.iterator();
							SharedPreferences share = context.getSharedPreferences("exam",context.MODE_PRIVATE);
							String records = share.getString("records", "");
							JSONArray recs = null;
							if(records==""){ 
								recs = new JSONArray();
							}else{
								try {
									recs = new JSONArray(records);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							//poiinfo
							JSONArray jarray = new JSONArray();
							while (itr.hasNext()) {
								JSONObject jobject = new JSONObject();
								PoiItem nextObj = (PoiItem) itr.next();
								try {
									String value = String.valueOf(nextObj.getLatLonPoint().getLatitude());
									jobject.put("lat", value);
									value = String.valueOf(nextObj.getLatLonPoint().getLongitude());
									jobject.put("lng", value);
									try {
										value = URLEncoder.encode(String.valueOf(nextObj.getTitle()),"utf-8");
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									jobject.put("name", value);
									value = String.valueOf(nextObj.getPoiId());
									jobject.put("id", value);
									try {
										value = URLEncoder.encode(String.valueOf(nextObj.getTypeDes()),"utf-8");
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									jobject.put("type", value);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.e("poi",jobject.toString());
								jarray.put(jobject);
							}
							//lbsinfo
							JSONArray lbss = new JSONArray();
							
							try {
								String gaode = share.getString("gaode", "");
								lbss.put(new JSONObject(gaode));
								String baidu = share.getString("baidu", "");
								lbss.put(new JSONObject(baidu));
								String tencent = share.getString("tencent", "");
								lbss.put(new JSONObject(tencent));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							JSONObject rec = new JSONObject();
							try {
								rec.put("poiinfo", jarray);
								rec.put("lbsinfo", lbss.toString());
								rec.put("date", date);
								String userid = null;
								try {
									userid = URLEncoder.encode(share.getString("id", ""),"utf-8");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								rec.put("userid",userid);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							recs.put(rec);
							Log.e("rec", recs.toString());
							Editor edit = share.edit();
							edit.putString("records", recs.toString());
							edit.commit();
							Shownotification(context);
						}
						
//						Message message = new Message();
//						Bundle bundle = new Bundle();
//						bundle.putString("type", "pois");
//						bundle.putString("pois",jarray.toString());
//						message.setData(bundle);
//						callback.handleMessage(message);
					}
				}
			}

			
	});
	poisearch.setBound(new SearchBound(point, (int) error));
	poisearch.searchPOIAsyn();
}
	
	public static void Shownotification(Context context) {
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		 int icon = com.example.kepler.R.drawable.ic_launcher; //通知图标
		 CharSequence tickerText = "Notice from Kepler"; //状态栏显示的通知文本提示
		 long when = System.currentTimeMillis(); //通知产生的时间，会在通知信息里显示
		 //用上面的属性初始化 Nofification
		 Notification notification = new Notification(icon,tickerText,when);
	     CharSequence contentTitle = "New location!"; //通知栏标题
		 CharSequence contentText = "New sites needed to be signed!"; //通知栏内容
		 Intent notificationIntent = new Intent(context,List.class); //点击该通知后要跳转的Activity
		 PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);
		 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		 mNotificationManager.notify(0,notification);// TODO Auto-generated method stub
	}
}
