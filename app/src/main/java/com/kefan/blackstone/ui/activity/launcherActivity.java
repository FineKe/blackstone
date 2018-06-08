package com.kefan.blackstone.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.JsonUtil.JsonResolverList;
import com.kefan.blackstone.R;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class launcherActivity extends AutoLayoutActivity {
    String TAG="tag";
    private String getSpeciesListURL= APIManager.BASE_URL +"v1/species/list";//物种清单获取接口
    private String getSpeciesDetailedURL= APIManager.BASE_URL +"v1/species/";//物种详情接口
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String speciesListState="ListState";
    private ImageView imageView;
    private boolean isLoaded=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        imageView= (ImageView) findViewById(R.id.imageView);

        Glide.with(this).load(R.drawable.splash).into(imageView);
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
        timer.schedule(timerTask,3000);

    }

    public void initData() {
        sharedPreferences=getSharedPreferences(speciesListState,MODE_PRIVATE);
        editor=sharedPreferences.edit();
        isLoaded=sharedPreferences.getBoolean("isLoaded",false);
        if(isLoaded==false)
        {
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
                            editor.putBoolean("isLoaded",true).apply();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(launcherActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(getSpeciesListRequest);
        }
    }
}
