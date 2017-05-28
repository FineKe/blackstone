package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
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

/**
 * Created by MY SHIP on 2017/5/25.
 */

public class UserInformationUtil {
    private SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Context context;
    private String fileName;
    private int id;
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

    public int getId() {
        return sharedPreferences.getInt("id",0);
    }

    public void setId(int id) {
        this.id = id;
        editor.putInt("id",id);
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
}
