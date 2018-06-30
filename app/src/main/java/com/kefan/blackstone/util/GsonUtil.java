package com.kefan.blackstone.util;

import com.google.gson.Gson;
import com.kefan.blackstone.data.response.BaseResponse;

import java.lang.reflect.Type;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/15 上午10:05
 */
public class GsonUtil {

    public static BaseResponse fromJson(String json,Class clazz) {

        Gson  gson = new Gson();
        Type type = type(BaseResponse.class,clazz);
        return gson.fromJson(json,type);
    }



    private static ParameterrizedType type(final Class clzz,final Type ...args) {


        return new ParameterrizedType(clzz,args){

            @Override
            public Type getRawType() {
                return clzz;
            }

            @Override
            public Type[] getActualTypeArguments() {

                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
