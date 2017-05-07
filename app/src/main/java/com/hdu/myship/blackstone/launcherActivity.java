package com.hdu.myship.blackstone;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.logging.LogRecord;

import JsonUtil.JsonResolverList;
import database.Species;

public class launcherActivity extends AutoLayoutActivity {
    String TAG="tag";
    private String getSpeciesListURL="http://api.blackstone.ebirdnote.cn/v1/species/list";//物种清单获取接口
    private String getSpeciesDetailedURL="http://api.blackstone.ebirdnote.cn/v1/species/";//物种详情接口
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

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

    public void initData() {
        DataSupport.deleteAll(Species.class);
        requestQueue=Volley.newRequestQueue(this);
        JsonObjectRequest getSpeciesListRequest=new JsonObjectRequest(Request.Method.GET, getSpeciesListURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        JSONObject data=jsonObject.getJSONObject("data");
                        JsonResolverList resolve=new JsonResolverList(data);
                        resolve.Resolve();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(getSpeciesListRequest);

    }


}
