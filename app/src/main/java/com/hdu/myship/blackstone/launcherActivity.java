package com.hdu.myship.blackstone;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import JsonUtil.JsonResolverList;
import database.Species;

public class launcherActivity extends AutoLayoutActivity {

    private String getSpeciesListURL="http://api.blackstone.ebirdnote.cn/v1/species/list";//物种清单获取接口
    private String getSpeciesDetailedURL="http://api.blackstone.ebirdnote.cn/v1/species/";//物种详情接口
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        requestQueue= Volley.newRequestQueue(this);
        initData();


        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(launcherActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        };
        timer.schedule(timerTask,1000);

    }

    private void initData() {
        //加载物种清单数据
        JsonObjectRequest getSpeciesList=new JsonObjectRequest(Request.Method.GET, getSpeciesListURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code;
                JSONObject data;
                try {
                    code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        data=jsonObject.getJSONObject("data");
                        JsonResolverList jsonResolverList=new JsonResolverList(data);
                        jsonResolverList.Resolve();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(launcherActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue.add(getSpeciesList);

    }
}
