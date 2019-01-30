package com.chunhuitech.reader.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class MapResponseConverter <T> implements Converter<ResponseBody, T> {

    private Type type;

    public MapResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(-1);
        baseResult.setData(new HashMap<String, Object>());
        try {
            JSONObject jsonObject = new JSONObject(value.string());
            baseResult.setCode(jsonObject.getInt("code"));
            convertChild(baseResult.getData(), jsonObject.getJSONObject("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (T)baseResult;
    }

    private void convertChild(Map<String, Object> map, JSONObject jsonObject) throws JSONException {
        Iterator<String> it = jsonObject.keys();
        while (it.hasNext()) {
            String key = it.next();
            if(key.endsWith("List")) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                List<Map<String, Object>> listMap = new ArrayList<>();
                map.put(key, listMap);
                for(int i=0; i<jsonArray.length(); i++) {
                    Map<String, Object> newMap = new HashMap<>();
                    listMap.add(newMap);
                    convertChild(newMap, jsonArray.getJSONObject(i));
                }
            } else if (key.endsWith("Time")) {
                map.put(key, jsonObject.getLong(key));
            } else {
                map.put(key, jsonObject.getString(key));
            }
        }
    }

}
