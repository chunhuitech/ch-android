package com.chunhuitech.reader.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class BaseConverterFactory extends Converter.Factory {

    private static BaseConverterFactory factory;

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new MapResponseConverter(type);
    }

    public static BaseConverterFactory create() {
        if (factory == null) {
            factory = new BaseConverterFactory();
        }
        return factory;
    }
}
