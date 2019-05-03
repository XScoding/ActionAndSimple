package com.xs.simple.ext;

import com.google.gson.Gson;

import com.xs.simple.Converter;
import com.xs.simple.util.TypeUtil;
import com.xs.simplehttp.api.Response;

import java.lang.reflect.Type;

/**
 * Gson ConverterFactory
 *
 * Created by xs code on 2019/3/27.
 */

public class GsonConverterFactory extends Converter.Factory {

    private final Gson gson;

    public static GsonConverterFactory create() {
        return create(new Gson());
    }

    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

    private GsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<Response, ?> responseConverter(final Type type) {
        if (!TypeUtil.typeClass(type,Void.class)) {
            return new Converter<Response, Object>() {
                @Override
                public Object convert(Response response) {
                    String string = response.getResponseString();
                    return gson.fromJson(string,type);
                }
            };
        }
        return null;
    }

    @Override
    public Converter<?, String> requestConverter(Type type) {

        return new Converter<Object, String>() {
            @Override
            public String convert(Object o) {
                return gson.toJson(o);
            }
        };
    }
}
