package com.example.kepler.lbs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import com.example.kepler.framework.Datacenter;
import com.example.kepler.framework.Datashare;
import com.example.kepler.object.LBSInfo;
import com.example.kepler.object.POI_Info;
import com.example.kepler.object.REC_Info;
import com.example.kepler.runnables.lbss_runnable;
import com.example.kepler.sql.Sql_Tool;

import android.content.Context;
import android.util.Log;

public class getpoi {
	private static Sql_Tool rec_sql_Tool = null;
	private static Sql_Tool nrec_sql_Tool = null;
	public static void start(
			final String date,
			double error,
			LatLonPoint point ,
			String city,
			final Context context){
		Query query = new Query("",
				"050000|060000|070000|080000|090000|100000|120200|190400|" +
				"110000|120000|130000|140000|150000|200000|190000|141200|" +
				"050100|050200|050300|050400|050500|050600|050700|140500|" +
				"050800|050900|060100|060200|060300|060400|060600|060700|190403|" +
				"060800|080100|080200|080300|080500|080600|090100|120300|120303", city);
		Log.e("getpoi","start");
		query.setPageSize(30);
		if(rec_sql_Tool==null){
			rec_sql_Tool = new Sql_Tool("recinfo",context,REC_Info.class);
			//nrec_sql_Tool = new Sql_Tool("nrecinfo",context,REC_Info.class);			
		}
		PoiSearch poisearch = new PoiSearch(context, query);
		poisearch.setOnPoiSearchListener(new OnPoiSearchListener(){
			@Override
			public void onPoiSearched(PoiResult arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1 == 0){
					if (arg0 != null&&arg0.getQuery()!=null) {
						List<PoiItem> poiItems = arg0.getPois();
						REC_Info rec_info = new REC_Info();
						Datacenter datacenter = Datacenter.getDatacenter(context);
						Datashare share = datacenter.getshared();
						if(poiItems!=null){
							//生成预存的rec数据
							Iterator itr = poiItems.iterator();
							JSONArray pois = new JSONArray();
							//poiinfo
							//填充poi信息
							while (itr.hasNext()) {
								POI_Info po = new POI_Info();
								JSONObject poi = new JSONObject();
								PoiItem nextObj = (PoiItem) itr.next();
								try {
									poi.put("lat", nextObj.getLatLonPoint().getLatitude());
									poi.put("lng", nextObj.getLatLonPoint().getLongitude());
									poi.put("id", String.valueOf(nextObj.getPoiId()));
									poi.put("name", URLEncoder.encode(String.valueOf(nextObj.getTitle()),"utf-8"));
									poi.put("type", URLEncoder.encode(String.valueOf(nextObj.getTypeDes()),"utf-8"));
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}catch (JSONException e) {
										// TODO Auto-generated catch block
									e.printStackTrace();
								}
								pois.put(poi);
							}
							rec_info.poiinfo = pois.toString();
							
							//lbsinfo
							//填充lbs信息
							JSONArray lbss = new JSONArray();
							ArrayList<String> names = new ArrayList<String>();

							names.add("gaode");
							names.add("baidu");
							names.add("tencent");

							for (String name : names){
								String lbsinfo = share.getstring("exam", name);
								if(!lbsinfo.equalsIgnoreCase("")){
									try {
										lbss.put(new JSONObject(lbsinfo));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							rec_info.lbsinfo = lbss.toString();
						}
						
						rec_info.date = date;
						try {
							rec_info.userid =URLEncoder.encode(share.getstring("exam", "id"),"utf-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.e("rec and nrec", "get");
						rec_sql_Tool.addsql(rec_info);
						nrec_sql_Tool.addsql(rec_info);
					}
				}
			}
		});
		poisearch.setBound(new SearchBound(point, (int) error));
		poisearch.searchPOIAsyn();
	}
}
