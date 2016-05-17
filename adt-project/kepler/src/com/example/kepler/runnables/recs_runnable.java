package com.example.kepler.runnables;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.example.kepler.framework.Datacenter;
import com.example.kepler.lbs.newgetpoi;
import com.example.kepler.object.REC_Info;
import com.example.kepler.sql.Sql_Tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mulewen on 16/2/12.
 */
public class recs_runnable implements Runnable{

    Context context = null;
    String content = "";
    ArrayList<REC_Info> nrecinfos = null;
    Sql_Tool recSql_Tool = null;
    public recs_runnable(Context context){
        this.context = context;
        recSql_Tool = new Sql_Tool("recinfo",context,REC_Info.class);
    }

    @Override
    public void run() {

        // TODO Auto-generated method stub
        String content = recSql_Tool.getcontent();
        if(content==""){
            Log.e("records", "void");
        }else{
            Log.e("records", "sending");
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost post = new HttpPost("http://121.42.136.178:8080/kepler-server/SendData");
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("data", content));
                formparams.add(new BasicNameValuePair("type", "record"));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,"utf-8");
                entity.setContentType("application/x-www-form-urlencoded;charset=utf-8");
                post.setEntity(entity);
                HttpResponse response=httpClient.execute(post);
                if(response.getStatusLine().getStatusCode()==200)
                {
                	recSql_Tool.clean();
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
