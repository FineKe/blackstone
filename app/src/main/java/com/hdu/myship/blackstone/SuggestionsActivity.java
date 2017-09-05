package com.hdu.myship.blackstone;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import JavaBean.APIManager;

public class SuggestionsActivity extends BaseActivity implements View.OnClickListener{
    private String feedBackURL= APIManager.rootDoname+"v1/feedback/new";
    private RequestQueue requestQueue;
    private JsonObjectRequest suggestionRequest;
    private UserInformationUtil userInformation;
    private LinearLayout actionBack;

    private TextView send;

    private EditText write;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_suggestions);

        initDatas();
        initViews();
        initEvents();
        UpdateToken updateToken=new UpdateToken(this);
        updateToken.updateToken();
    }

    private void initDatas() {
        userInformation=new UserInformationUtil(this);

    }

    private void initViews() {
        actionBack= (LinearLayout) findViewById(R.id.activity_suggestion_linear_layout_action_back);
        send= (TextView) findViewById(R.id.activity_suggestion_text_view_send);
        write= (EditText) findViewById(R.id.activity_suggestion_edit_text_write_suggestions);
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_suggestion_linear_layout_action_back:
                actionBack();
                break;

            case R.id.activity_suggestion_text_view_send:
                send();
                break;
        }
    }

    private void actionBack() {
        finish();
       // overridePendingTransition(R.anim.in,R.anim.out);
    }

    private void send() {
        requestQueue= Volley.newRequestQueue(this);
        final String token=userInformation.getToken();
        if(token.equals(""))
        {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        }else
        {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("content",write.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            suggestionRequest=new JsonObjectRequest(Request.Method.POST, feedBackURL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code=jsonObject.getInt("code");
                        if(code==88)
                        {
                            Toast.makeText(SuggestionsActivity.this, "感谢你的建议", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            String message=jsonObject.getString("message");
                            Toast.makeText(SuggestionsActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SuggestionsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    UpdateToken updateToken=new UpdateToken(SuggestionsActivity.this);
                    updateToken.updateToken();
                    Map<String,String> headers=new HashMap<>();
                    headers.put("token",token);
                    return headers;
                }
            };
            requestQueue.add(suggestionRequest);
        }


    }
}
