package com.orientdata.lookforcustomers.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Dream on 16/5/29.
 */
public class GsonUtils<T> {

	// 两种方案
    // 第一种:方法泛型
	// 第二种:类泛型

	// 方法泛型使用补充
	public ArrayList<T> parseArray(String result, Class<?> clazz) {
		ArrayList<T> list = new ArrayList<T>();

		return null;
	}

	public static <T> List<T> getList(String result, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(result);
		Iterator it = el.getAsJsonArray().iterator();
		while (it.hasNext()) {
			JsonElement e = (JsonElement) it.next();
			T model = gson.fromJson(e, clazz);
			list.add(model);
		}
		return list;
	}

	/**
	 * 把一个map变成json字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String parseMapToJson(Map<?, ?> map) {
		try {
			Gson gson = new Gson();
			return gson.toJson(map);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 把一个json字符串变成对象
	 * 
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T parseJsonToBean(String json, Class<T> cls) {
		Gson gson = new Gson();
		T t = null;
		try {
			t = gson.fromJson(json, cls);
		} catch (Exception e) {
		}
		return t;

	}

	public static String getjson(String str) {
		String result = "";
		ArrayList<String> word = new ArrayList<String>();
		int m = 0, n = 0;
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '{') {
				if (count == 0) {
					m = i;
				}
				count++;
			}
			if (str.charAt(i) == '}') {
				count--;
				if (count == 0) {
					n = i;
					word.add(str.substring(m, n + 1));
				}
			}
		}
		for (String a : word) {
			result += a;
		}
		return result.trim();

	}

	/**
	 * 把json字符串变成map
	 * 
	 * @param json
	 * @return
	 */
	public static HashMap<String, Object> parseJsonToMap(String json) {
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<String, Object>>() {
		}.getType();
		HashMap<String, Object> map = null;
		try {
			map = gson.fromJson(json, type);
		} catch (Exception e) {
		}
		return map;
	}

	/**
	 * 把json字符串变成集合 params: new TypeToken<List<yourbean>>(){}.getType(),
	 * 
	 * @param json
	 * @param type
	 *            new TypeToken<List<yourbean>>(){}.getType()
	 * @return
	 */
	public static List<?> parseJsonToList(String json, Type type) {
		Gson gson = new Gson();
		List<?> list = gson.fromJson(json, type);
		return list;
	}

	/**
	 * 
	 * 获取json串中某个字段的值，注意，只能获取同一层级的value
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getFieldValue(String json, String key) {
		if (TextUtils.isEmpty(json))
			return null;
		if (!json.contains(key))
			return "";
		JSONObject jsonObject = null;
		String value = null;
		try {
			jsonObject = new JSONObject(json);
			value = jsonObject.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * hashMap 转化成表单字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String map2Form(HashMap<String, String> map) {
		StringBuilder stringBuilder = new StringBuilder();
		if (map == null) {
			return stringBuilder.toString();
		} else {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			return stringBuilder.substring(0, stringBuilder.length() - 1);
		}
	}

}