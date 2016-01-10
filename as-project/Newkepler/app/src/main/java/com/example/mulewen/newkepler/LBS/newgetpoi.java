package com.example.mulewen.newkepler.LBS;
import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.NearbySearch.NearbyListener;
import com.amap.api.services.nearby.NearbySearch.NearbyQuery;

public class newgetpoi {
	public static void start(
			final String date,
			double error,
			LatLonPoint point ,
			String city,
			final Context context){
		NearbyQuery query = new NearbyQuery();
		query.setCenterPoint(point);
		query.setRadius((int)error);
		NearbySearch nearbysearch = NearbySearch.getInstance(context);
		nearbysearch.addNearbyListener(new NearbyListener() {
			
			@Override
			public void onUserInfoCleared(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onNearbyInfoUploaded(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onNearbyInfoSearched(NearbySearchResult arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1==0){
					
				}
			}
		});
		nearbysearch.searchNearbyInfoAsyn(query);
		try {
			nearbysearch.searchNearbyInfo(query);
		} catch (AMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
