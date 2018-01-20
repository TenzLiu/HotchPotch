package com.tenz.hotchpotch.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: TenzLiu
 * Date: 2018-01-18 15:13
 * Description: json格式转换工具
 */

public class JsonUtil {

    private static Gson sGson = new Gson();

    /**
     * 对象转json字符串
     * @param object
     * @return
     */
    public static <T> String toJson(T object){
        return sGson.toJson(object);
    }

    /**
     * 对象转json字符串
     * @param object
     * @return
     */
    public static <T> String toJson(T object, String key){
        Map map = new HashMap();
        map.put(key,object);
        return sGson.toJson(map);
    }

    /**
     * json字符串转实体对象
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T fromJsonToObject(String json, Class<T> cls){
        return sGson.fromJson(json,cls);
    }

    /**
     * json字符串转实体对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJsonToObject(String json, Type type){
        return sGson.fromJson(json,type);
    }

}
