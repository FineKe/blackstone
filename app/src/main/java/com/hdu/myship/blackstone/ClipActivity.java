package com.hdu.myship.blackstone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.hdu.myship.blackstone.MyApplication.getContext;

public class ClipActivity extends Activity {
    private String getUpLoadTokenURL="http://api.blackstone.ebirdnote.cn/v1/upload/token";
    private String upLoadImageURL="http://api.blackstone.ebirdnote.cn/v1/user/avatar";
    private String TAG="ClipActivity";
    private ClipImageLayout mClipImageLayout;
    private String path;
    private ProgressDialog loadingDialog;

    private final int DEAL_PICTURE_OK=7;
    private final int GET_UPLOAD_TOKEN_OVER=5;
    private final int GET_IMAGE_KEY_OK=6;
    private final int UPLOAD_IMAGE_OK=8;

    private String uploadtoken;
    private String imageKey;
//    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipimage);
        //这步必须要加
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog=new ProgressDialog(this);
        loadingDialog.setTitle("请稍后...");
        path=getIntent().getStringExtra("path");
        if(TextUtils.isEmpty(path)||!(new File(path).exists())){
            Toast.makeText(this, "图片加载失败",Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap= ImageTools.convertToBitmap(path, 600,600);
        if(bitmap==null){
            Toast.makeText(this, "图片加载失败",Toast.LENGTH_SHORT).show();
            return;
        }
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.setBitmap(bitmap);
        ((Button)findViewById(R.id.id_action_clip)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = mClipImageLayout.clip();
                        path= Environment.getExternalStorageDirectory()+"/ClipHeadPhoto/cache/"+System.currentTimeMillis()+ ".png";
                        ImageTools.savePhotoToSDCard(bitmap,path);
                        Message message=new Message();
                        message.what=DEAL_PICTURE_OK;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    public void result()
    {
        loadingDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("path",path);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void getUpLoadTokenRequest()//得到上传图片的token
    {
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, getUpLoadTokenURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {

                        JSONObject data=jsonObject.getJSONObject("data");
                        uploadtoken=data.getString("token");
                        Log.d(TAG, "onResponse: "+uploadtoken);
                        Message message=new Message();
                        message.what=GET_UPLOAD_TOKEN_OVER;
                        handler.sendMessage(message);
                    }
                    else
                    {
                        String message=jsonObject.getString("message");
                        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "请求异常", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>headers=new HashMap<>();
                UpdateToken updateToken=new UpdateToken(getContext());
                updateToken.updateToken();
                UserInformationUtil userInformationUtil=new UserInformationUtil(getContext());
                headers.put("token",userInformationUtil.getToken());
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void upLoadImage()
    {
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        Map<String,String>map=new HashMap<>();
        map.put("avatar",imageKey);

        JSONObject jsonObject=new JSONObject(map);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, upLoadImageURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        Log.d(TAG, "onResponse:上传成功 ");
                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        Message message=new Message();
                        message.what=UPLOAD_IMAGE_OK;
                        handler.sendMessage(message);
                    }
                    else
                    {
                        String message=jsonObject.getString("message");
                        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "请求异常", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>headers=new HashMap<>();
                UpdateToken updateToken=new UpdateToken(getContext());
                updateToken.updateToken();
                UserInformationUtil userInformationUtil=new UserInformationUtil(getContext());
                userInformationUtil.setAvatar("http://img.blackstone.ebirdnote.cn/"+imageKey);
                headers.put("token",userInformationUtil.getToken());
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void getImageKey(String path)
    {
        UploadManager uploadManager=new UploadManager();
        uploadManager.put(path, null, uploadtoken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if(info.isOK())
                {
                    try {
                        imageKey=response.getString("key");
                        Log.d(TAG, "complete: "+imageKey);
                        Message message=new Message();
                        message.what=GET_IMAGE_KEY_OK;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "获取key异常", Toast.LENGTH_SHORT).show();
                }
            }
        },null);

    }

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_UPLOAD_TOKEN_OVER:
                    getImageKey(path);
                    break;
                case GET_IMAGE_KEY_OK:
                    upLoadImage();
                    break;
                case DEAL_PICTURE_OK:
                    getUpLoadTokenRequest();
                    break;
                case UPLOAD_IMAGE_OK:
                    result();
                    break;
            }
        }
    };

}
