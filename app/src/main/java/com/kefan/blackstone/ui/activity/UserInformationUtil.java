package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
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
 * Created by MY SHIP on 2017/5/25.
 */

public class UserInformationUtil {
    private String updateURL= APIManager.BASE_URL +"v1/user/login";
    private SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Context context;
    private String fileName;
    private Long id;
    private String userName;//账户
    private String userPwd;//密码
    private String studentId;//学生id
    private String name;//学生姓名
    private String gender;//性别
    private String token;//token
    private String avatar;//头像信息
    private Long expireAt;//
    public UserInformationUtil(Context context) {
        this.context=context;
        fileName="UesrInformation";
        sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public Long getId() {
        return sharedPreferences.getLong("id",0);
    }

    public void setId(Long id) {
        this.id = id;
        editor.putLong("id",id);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("mobile","");
    }

    public void setUserName(String userName) {
        this.userName = userName;
        editor.putString("mobile",userName);
        editor.apply();
    }

    public String getUserPwd() {
        return sharedPreferences.getString("password","");
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
        editor.putString("password",userPwd);
        editor.apply();
    }

    public String getStudentId() {
        return sharedPreferences.getString("studentId","");
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
        editor.putString("studentId",studentId);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString("name","");
    }

    public void setName(String name) {
        this.name = name;
        editor.putString("name",name);
        editor.apply();
    }

    public String getGender() {
        return sharedPreferences.getString("gender","");
    }

    public void setGender(String gender) {
        this.gender = gender;
        editor.putString("gender",gender);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token","");
    }

    public void setToken(String token) {
        this.token = token;
        editor.putString("token",token);
        editor.apply();
    }

    public String getAvatar() {
        return sharedPreferences.getString("avatar","");
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        editor.putString("avatar",avatar);
        editor.apply();
    }

    public Long getExpireAt() {
        return sharedPreferences.getLong("expireAt",0);
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
        editor.putLong("expireAt",expireAt);
        editor.apply();
    }

    public void updateUserInfomation()
    {   final UserInformationUtil information=new UserInformationUtil(context);
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
