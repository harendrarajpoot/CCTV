package com.app.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.Strings;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class Utils {
	public static String getUUID() {

		Date date = new Date();
		long time = date.getTime();
		return "Bill-" + time;
	}

	public static JSONArray getJsonArrayFromString(String data) {
		JSONArray jsonArray=null;
	try {
		 jsonArray=new JSONArray(data);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return jsonArray;
	}
	
	public static Map<String, String>getMapFromJson(String data)
	{
		if(data!=null)
		{
			return new  Gson().fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
			
		}
		return new HashMap();
	}
}
