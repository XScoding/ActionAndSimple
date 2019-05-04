package com.xs.demo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.xs.action.Scheduler;
import com.xs.simple.Call;
import com.xs.simple.Callback;
import com.xs.simple.Simple;
import com.xs.simple.SimpleResponse;
import com.xs.simple.ext.ActionCallAdapterFactory;
import com.xs.simple.ext.GsonConverterFactory;
import com.xs.simple.ext.SimpleAction;
import com.xs.simplehttp.api.CookieJar;
import com.xs.simplehttp.api.Interceptor;
import com.xs.simplehttp.api.Request;
import com.xs.simplehttp.api.Response;
import com.xs.simplehttp.api.SimpleHttp;
import com.xs.simplehttp.core.HttpException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String baseUrl = "https://test.com.cn/";

    private Map<String, String> cookiesCache;

    private Button mFrist;
    private Button mSecond;
    private TestService service;
    private Button mThird;
    private Button mFour;

    private void assignViews() {
        mFrist = (Button) findViewById(R.id.frist);
        mSecond = (Button) findViewById(R.id.second);
        mThird = (Button) findViewById(R.id.third);
        mFour = (Button) findViewById(R.id.four);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        mFrist.setOnClickListener(this);
        mSecond.setOnClickListener(this);
        mThird.setOnClickListener(this);
        mFour.setOnClickListener(this);
        initSimple();
    }

    private void initSimple() {
        SimpleHttp.Builder httpBuilder = new SimpleHttp.Builder();
        httpBuilder.setCookieJar(new CookieJar() {
            @Override
            public void save(String url, Map<String, String> cookies) {
                if (cookies != null && cookies.size() > 0) {
                    cookiesCache = cookies;
                    Log.e("xssss","cookie save == " + cookiesCache.toString());
                }
            }

            @Override
            public Map<String, String> load(String url) {
                if (cookiesCache != null)
                    Log.e("xssss","cookie load == " + cookiesCache.toString());
                return cookiesCache;
            }
        });
        httpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) {
                Request request = chain.request();
                request.getParams().put("appVersion", "appVersion");
                request.getParams().put("deviceId", "deviceId");
                return chain.proceed(request);
            }
        });
        httpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) {
                Request request = chain.request();
                Log.e("xssss","intercept == " + request.getParams().toString());
                Response response = chain.proceed(request);
                String responseString = response.getResponseString();
                Log.e("xssss","intercept == " + responseString);
                return response;
            }
        });
        SimpleHttp simpleHttp = httpBuilder.build();
        Simple.Builder builder = new Simple.Builder(simpleHttp,baseUrl);
        builder.addConverter(GsonConverterFactory.create());
        builder.addCallAdapter(new ActionCallAdapterFactory());
        Simple simple = builder.build();
        service = simple.create(TestService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frist:
                login();
                break;
            case R.id.second:
                info();
                break;
            case R.id.third:
                userInfo();
            case R.id.four:
                loginAction();
                break;
        }
    }

    private void userInfo() {
        Call<String> call = service.userInfo();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(SimpleResponse<String> response) {
                String body = response.body();
                Log.e("xssss",body);
            }

            @Override
            public void onFailure(HttpException e) {
                Log.e("xssss",e.getMessage());
            }
        });
    }

    private void info() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                Call<String> call = service.info(map);
                SimpleResponse<String> response = call.execute();
                if (response.isSuccessful()) {
                    String body = response.body();
                    Log.e("xssss",body);
                } else {
                    String message = response.getError().getMessage();
                    Log.e("xssss",message);
                }
            }
        }).start();
    }

    private void login() {
        Map<String, Object> map = new HashMap<>();
        map.put("vCode","123456");
        map.put("phone","13111111111");
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1","this is header");
        Call<LBean> call = service.login(map,"header2",headers);
        call.enqueue(new Callback<LBean>() {
            @Override
            public void onResponse(SimpleResponse<LBean> response) {
                LBean lBean = response.body();
                Gson gson = new Gson();
                String json = gson.toJson(lBean);
                Log.e("xssss",json);
            }

            @Override
            public void onFailure(HttpException e) {
                Log.e("xssss",e.getMessage());
            }
        });
    }


    private void loginAction() {
        Map<String, Object> map = new HashMap<>();
        map.put("vCode","123456");
        map.put("phone","13111111111");
        SimpleAction<LBean> action = service.login(map);
        action.onAction(Scheduler.IO)
                .onExecute(Scheduler.MAIN)
                .exec(new com.xs.action.Call<LBean>() {
                    @Override
                    public void success(LBean lBean) {
                        Gson gson = new Gson();
                        String json = gson.toJson(lBean);
                        Log.e("xssss",json);
                    }

                    @Override
                    public void fail(Exception e) {
                        Log.e("xssss",e.getMessage());
                    }
                });
    }
}
