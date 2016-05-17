package com.example.kepler.runnables;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.example.kepler.object.REC_Info;
import com.example.kepler.sql.Sql_Tool;

import android.content.Context;

/**
 * Created by mulewen on 16/2/12.
 */
public class nrecs_runnable implements Runnable{

    Context context = null;
    ArrayList<REC_Info> nrecinfos = null;
    Sql_Tool nrecSql_Tool = null;
    public nrecs_runnable(Context context){
        this.context = context;
        nrecSql_Tool = new Sql_Tool("nrecinfo",context,REC_Info.class);
    }

    @Override
    public void run() {

        // TODO Auto-generated method stub
    	String content = nrecSql_Tool.getcontent();

        if(content==""){
            Log.e("nrecords", "void");
        }else{
            Log.e("nrecords", "sending");
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost post = new HttpPost("http://121.42.136.178:8080/kepler-server/SendData");
                //HttpPost post = new HttpPost("http://192.168.2.1:8080/kepler-server/SendData");
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("data", content));
                formparams.add(new BasicNameValuePair("type", "nrecord"));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,"utf-8");
                entity.setContentType("application/x-www-form-urlencoded;charset=utf-8");
                post.setEntity(entity);
                HttpResponse response=httpClient.execute(post);
                if(response.getStatusLine().getStatusCode()==200)
                {
                	nrecSql_Tool.clean();
                }
                Log.e("haha",String.valueOf(response.getStatusLine().getStatusCode()));
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("haha", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
