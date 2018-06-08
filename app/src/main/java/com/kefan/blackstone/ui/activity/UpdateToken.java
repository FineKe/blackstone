package com.kefan.blackstone.ui.activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import JavaBean.APIManager;

/**
 * Created by MY SHIP on 2017/5/30.
 * 检测token是否已经过期，过期将自动更新
 */

public class UpdateToken {
    private String updateURL= APIManager.BASE_URL +"v1/user/login";
    private String TAG="UpdateToken";
    private UserInformationUtil information;
    private Context context;

    public UpdateToken(Context context) {
        this.context = context;
        information = new UserInformationUtil(context);
    }

    public boolean isOutOfDate() {
        Log.d(TAG, "isOutOfDate: "+information.getExpireAt()+":"+System.currentTimeMillis());
        return information.getExpireAt() <= System.currentTimeMillis();
    }

    public void updateToken()
    {
        System.out.println("current time"+System.currentTimeMillis()+"expireAt"+information.getExpireAt());
        if (isOutOfDate())
        {
            RequestQueue requestQueue= Volley.newRequestQueue(context);
            Map<String,String> map=new HashMap<>();
            map.put("username",information.getUserName());
            map.put("pwd",information.getUserPwd());
            JSONObject jsonObject=new JSONObject(map);
            JsonObjectRequest updateRquest=new JsonObjectRequest(Request.Method.POST, updateURL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code=jsonObject.getInt("code");
                        if(code==88)
                        {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONObject user=data.getJSONObject("user");
                            information.setId(user.getLong("id"));
                            information.setUserName(user.getString("mobile"));
                            information.setStudentId(user.getString("studentId"));
                            information.setName(user.getString("name"));
                            information.setGender(user.getString("gender"));
                            information.setToken(data.getString("token"));
                            information.setExpireAt(data.getLong("expireAt"));
                            if(user.has("avatar"))
                            {
                                information.setAvatar(user.getString("avatar"));
                            }
                        }
                        else
                        {
                            String message=jsonObject.getString("message");
                            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(updateRquest);
        }
    }

}
