package com.example.kepler.tools;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;

public class json2object<T> {
	
	public ArrayList<T> handle(String content, Class<T> object) {//
		ArrayList<T> lbss = new ArrayList<T>();//新建LBS对象数组
		ObjectMapper objectMapper = new ObjectMapper();//新建对象映射对象
		Class<T> hehe;
		//传入的是jsonarray，转换为对象的array
		if(content!=""){
			try {				
				JSONArray jlbsarray  = new JSONArray(content);//json转为jsonarray对象
				for(int i = 0;i<jlbsarray.length();i++){//将jsonarray转换成LBS array  
					T lbsobject = objectMapper.readValue(jlbsarray.get(i).toString(), object);
					lbss.add(lbsobject);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//通过批量提交添加数据到数据库
		return lbss;
	}
}
