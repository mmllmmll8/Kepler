package com.example.kepler.framework;

import android.content.Context;
import android.content.IntentSender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kepler.object.LBSInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mulewen on 16/2/13.
 */
public class LBS_info_mid implements Middledata{

    Context context = null;
    ArrayList<LBSInfo> lbsInfos = null;
    boolean ischanged = false;
    static LBS_info_mid lbs_info_mid = null;

    public static LBS_info_mid getlbsinfomid(Context context){
        if(lbs_info_mid==null){
            lbs_info_mid = new LBS_info_mid(context);
        }
        return lbs_info_mid;
    }

    public ArrayList<LBSInfo> getLbsInfos(){
        return this.lbsInfos;
    }

    private LBS_info_mid(Context context){
        this.context = context;
        lbsInfos = new ArrayList<LBSInfo>();
        ini();
    }

    @Override
    public void update() {
        if (ischanged){
            JSONArray jlbss = new JSONArray();
            for (LBSInfo lbs:lbsInfos
                    ) {
                JSONObject jlbs = new JSONObject();
                try {
                    jlbs.put("userid",lbs.userid);
                    jlbs.put("date",lbs.date);
                    jlbs.put("lat",lbs.lat);
                    jlbs.put("lng",lbs.lng);
                    jlbs.put("accuracy",lbs.accuracy);
                    jlbss.put(jlbs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayList<HashMap> update = new ArrayList<HashMap>();
            HashMap<String,String> hasmap = new HashMap<String, String>();
            hasmap.put("name","lbss");
            hasmap.put("value",jlbss.toString());
            update.add(hasmap);
            Datacenter.getDatacenter(context).getshared().Savedata(update,"exam");
            ischanged = false;
        }
    }

    public void addlbss(LBSInfo nrec){
        ischanged = true;
        if(lbsInfos==null) {
            lbsInfos = new ArrayList<LBSInfo>();
        }
        lbsInfos.add(nrec);
    }

    @Override
    public void ini() {
        Datacenter datacenter = Datacenter.getDatacenter(this.context);
        Datashare datashare = datacenter.getshared();
        String content = datashare.getstring("exam","lbss");
        try {
            JSONArray jlbsinfos = new JSONArray(content);
            lbsInfos = new ArrayList<LBSInfo>();
            for (int i = 0;i<jlbsinfos.length();i++){
                LBSInfo lbsInfo = new LBSInfo();
                lbsInfo.lat = jlbsinfos.getJSONObject(i).getDouble("lat");
                lbsInfo.lng = jlbsinfos.getJSONObject(i).getDouble("lng");
                lbsInfo.accuracy = jlbsinfos.getJSONObject(i).getDouble("accuracy");
                lbsInfo.date = jlbsinfos.getJSONObject(i).getString("date");
                lbsInfo.userid = jlbsinfos.getJSONObject(i).getString("userid");
                lbsInfos.add(lbsInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
