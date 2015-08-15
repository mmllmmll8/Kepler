package com.example.kepler.LBS;

import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;

public class getpoi {
	public static void start(final String id,LatLonPoint point ,Context context,final Callback callback){
		Query query = new Query("", "", "");
        query.setPageSize(20);
		PoiSearch poisearch = new PoiSearch(context, query);
		poisearch.setOnPoiSearchListener(new OnPoiSearchListener(){
			@Override
			public void onPoiSearched(PoiResult arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1 == 0){
					if (arg0 != null&&arg0.getQuery()!=null) {
						List<PoiItem> poiItems = arg0.getPois();
						Iterator itr = poiItems.iterator();
						JSONArray jarray = new JSONArray();
						while (itr.hasNext()) {
							JSONObject jobject = new JSONObject();
							PoiItem nextObj = (PoiItem) itr.next();
							try {
								jobject.put("lat", String.valueOf(nextObj.getLatLonPoint().getLatitude()));
								jobject.put("lng", String.valueOf(nextObj.getLatLonPoint().getLongitude()));
								jobject.put("name", String.valueOf(nextObj.getAdName()));
								jobject.put("id", String.valueOf(nextObj.getPoiId()));
								jobject.put("type", String.valueOf(nextObj.getTypeDes()));
								jobject.put("lbsid", id);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							jarray.put(jobject);
	   					}
						Message message = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("type", "pois");
						bundle.putString("pois",jarray.toString());
						message.setData(bundle);
						callback.handleMessage(message);
					}
				}
			}

			@Override
			public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
	});
	poisearch.setBound(new SearchBound(point, (int) 2000));
	poisearch.searchPOIAsyn();
}
}
