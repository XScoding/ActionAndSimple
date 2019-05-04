package com.xs.demo;


import com.xs.simple.Call;
import com.xs.simple.annotation.Field;
import com.xs.simple.annotation.FieldMap;
import com.xs.simple.annotation.FormPost;
import com.xs.simple.annotation.GET;
import com.xs.simple.annotation.Header;
import com.xs.simple.annotation.HeaderMap;
import com.xs.simple.annotation.Headers;
import com.xs.simple.annotation.POST;
import com.xs.simple.annotation.Path;
import com.xs.simple.ext.SimpleAction;

import java.util.Map;

/**
 * Created by xs code on 2019/3/11.
 */

public interface TestService {

    @POST("system/user/login/{path}")
    @FormPost
    Call<String> login(@Path("path") String path, @Field("phone") String phone, @Field("vCode") String vCode, @Field("num") int num);


    @POST("system/user/login/otp")
    @FormPost
    @Headers({"header3:header3"})
    Call<LBean> login(@FieldMap Map<String, Object> map, @Header("header2") String head, @HeaderMap Map<String, Object> headers);

    @POST("system/nologin/user/login/otp")
    @FormPost
    SimpleAction<LBean> login(@FieldMap Map<String, Object> map);



    @POST("system/homePage/info")
    @FormPost
    Call<String> info(@FieldMap Map<String, Object> map);

    @GET("system/user/userInfo")
    Call<String> userInfo();
}
