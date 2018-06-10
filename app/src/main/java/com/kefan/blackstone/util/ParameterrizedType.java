package com.kefan.blackstone.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 下午3:12
 */
public class ParameterrizedType implements ParameterizedType{

    private final Class raw;
    private final Type[] args;

    public ParameterrizedType(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }


    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }


    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

}
