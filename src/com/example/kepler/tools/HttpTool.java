package com.example.kepler.tools;

import java.io.IOException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class HttpTool {
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static OkHttpClient httpclient = null;
	
	public static String post(String url, String json) throws IOException {
		if(httpclient==null){
		   httpclient = new OkHttpClient();
		}
		RequestBody body = RequestBody.create(JSON,json);
		Request request = new Request.Builder()
		   .url(url)
		   .post(body)
		   .build();
		Response response = httpclient.newCall(request).execute();
		return response.body().string();
	}
	
	public static String get(String url) throws IOException {
		if(httpclient==null){
     	   httpclient = new OkHttpClient();
    	}
	    Request request = new Request.Builder()
	        .url(url)
	        .get()
	        .build();
	    Response response = httpclient.newCall(request).execute();
	    return response.body().string();
	}
}
