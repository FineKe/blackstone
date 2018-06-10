package com.kefan.blackstone.util;

import com.google.gson.Gson;
import com.kefan.blackstone.data.response.BaseResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @note: json 工具
 * @author: 柯帆
 * @date: 2018/6/10 下午12:02
 */
public class JsonUtil {

    /**
     * 将对象转换成JSON 对象
     * @param o
     * @return
     */
    public static JSONObject convertObject(Object o) {

        JSONObject jsonObject=null;
        Gson gson=new Gson();

        try {
             jsonObject= new JSONObject(gson.toJson(o));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    /**
     * 转化成string
     * @param o
     * @return
     */
    public static String jsonToString(Object o) {

        return new Gson().toJson(o);
    }

    public static <T> BaseResponse<T> fromJsonObject(String reader, Class<T> clazz) {
        Type type = new ParameterrizedType(BaseResponse.class, new Class[]{clazz});
        return new Gson().fromJson(reader, type);
    }

    public static <T> BaseResponse<List<T>> fromJsonArray(String reader, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterrizedType(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterrizedType(BaseResponse.class, new Type[]{listType});
        return new Gson().fromJson(reader, type);
    }

}
