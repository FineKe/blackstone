package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
 * Created by MY SHIP on 2017/3/18.
 * 个人中心fragment
 */

public class PersonalCenterFragment extends Fragment implements View.OnClickListener{
    private String loginURL="http://api.blackstone.ebirdnote.cn/v1/user/login";
    private String updateURL="http://api.blackstone.ebirdnote.cn/v1/user/login";
    private RequestQueue requestQueue;

    private FragmentManager fragmentManager;//fragment 管理者
    private FragmentTransaction transaction;//开启一个事列

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile="isLogin";
    private Boolean isLogined;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";


    private Button login;
    private Button register;


    private EditText iuputAccount;
    private EditText inputPassword;


    private TextView loginForget;
    private TextView Ok;
    private TextView errorLoginForget;
    private TextView inputAgain;


    private ImageView showPassword;
    private ImageView actionCancel;
    private ImageView userPicture;

    private boolean isShowed=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.personal_center,container,false);

        login= (Button) view.findViewById(R.id.person_center_button_login);
        register= (Button) view.findViewById(R.id.person_center_button_register);

        userPicture= (ImageView) view.findViewById(R.id.person_center_imageView_uesrPicture);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        userPicture.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        fragmentManager=getActivity().getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();
        sharedPreferences=getContext().getSharedPreferences(isLoginedFile, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        userInformationSharedPreferences=getActivity().getSharedPreferences(userInformation,Context.MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.person_center_button_login:login();
                break;

            case R.id.person_center_button_register:
                register();
                break;

            case R.id.person_center_imageView_uesrPicture:
                break;
        }
    }

    public void login()
    {
        showLoginDialog();
    }


    public void register()
    {
        startActivity(new Intent(getActivity(),RegisterActivity.class));
    }

    public void showLoginDialog()
    {
        final LoginDialog loginDialog=new LoginDialog(getContext(),R.style.LoginDialog,R.layout.login_dialog);
        loginDialog.show();
        iuputAccount= (EditText) loginDialog.findViewById(R.id.update_phone_numbe_dialog_editText_account);
        inputPassword= (EditText) loginDialog.findViewById(R.id.update_phone_number_dialog_editText_code);
        loginForget= (TextView) loginDialog.findViewById(R.id.update_phone_number_text_view_get_code);
        Ok= (TextView) loginDialog.findViewById(R.id.update_phone_number_dialog_textView_Ok);
        actionCancel= (ImageView) loginDialog.findViewById(R.id.login_dialog_imageView_actionCancel);
        showPassword= (ImageView) loginDialog.findViewById(R.id.login_dialog_imageView_showPassword);



        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowed)
                {
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.mipmap.see);
                }
                else
                {
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.mipmap.no_see);
                }
                isShowed=!isShowed;
                inputPassword.setSelection(inputPassword.getText().length());
                inputPassword.postInvalidate();
            }
        });

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> loginMap=new HashMap<String, String>();
                loginMap.put("username",iuputAccount.getText().toString());
                loginMap.put("pwd",inputPassword.getText().toString());
                JSONObject loginJsonObject=new JSONObject(loginMap);
                requestQueue=Volley.newRequestQueue(getContext());
                JsonObjectRequest loginRequest=new JsonObjectRequest(Request.Method.POST, loginURL, loginJsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int code=jsonObject.getInt("code");
                            if(code==88)
                            {

                                loginDialog.dismiss();
                                editor.putBoolean("islogined",true).apply();
                                JSONObject data=jsonObject.getJSONObject("data");
                                JSONObject user=data.getJSONObject("user");
                                userInformationEditor.putLong("id",user.getLong("id"));
                                userInformationEditor.putString("mobile",user.getString("mobile"));
                                userInformationEditor.putString("studentId",user.getString("studentId"));
                                userInformationEditor.putString("name",user.getString("name"));
                                userInformationEditor.putString("gender",user.getString("gender"));
                                userInformationEditor.putString("token",data.getString("token"));
                                userInformationEditor.putLong("expireAt",data.getLong("expireAt"));
                                userInformationEditor.putString("password",inputPassword.getText().toString());
                                if(user.has("avatar"))
                                {
                                    userInformationEditor.putString("avatar",user.getString("avatar"));
                                }
                                else
                                {
                                    userInformationEditor.putString("avatar",null);
                                }
                              //  userInformationEditor.putString("avatar",data.getString("avatar"));
                                userInformationEditor.apply();
                                transaction.replace(R.id.frame_layout,new LoginedFragment()).commit();

                            }
                            else
                            {

                                loginDialog.dismiss();
                                showErrorDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(),"请求异常",Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(loginRequest);
            }
        });

        loginForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ForgetPasswordActivity.class));
            }
        });
    }


    public void showErrorDialog()
    {
        final LoginDialog errorDialog=new LoginDialog(getContext(),R.style.LoginDialog,R.layout.error_login_dialog);
        errorDialog.show();
        errorLoginForget= (TextView) errorDialog.findViewById(R.id.error_login_dialog_forget);
        inputAgain= (TextView) errorDialog.findViewById(R.id.error_login_dialog_inputAgain);

        errorLoginForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ForgetPasswordActivity.class));
            }
        });

        inputAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
                errorDialog.dismiss();
            }
        });
    }
}
