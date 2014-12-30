package com.crazyfish.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description jsonÊý¾Ý½âÎö
 * @author kamal
 * @time 2014.12.22
 * @version1.0
 */
public class JsonCodec {
	public JsonCodec(){}
	public static List<Map<String,Object>> deJson(String data,String paraType){
		List<Map<String,Object>> lmap = null;
		try {
			JSONArray ja = new JSONArray(data);
			JSONObject user = null;
			String[] types = paraType.split(",");
			lmap = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < ja.length();i ++){
				user = (JSONObject)ja.getJSONObject(i);
				Map<String,Object> map = new HashMap<String,Object>();;
				for(int j = 0;j < types.length;j ++){
					final Object p1 =  user.get(types[j]);
					map.put(types[j],p1);
				}
				lmap.add(map);
			}
			return lmap;
		} catch (JSONException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return lmap;
	}
}
