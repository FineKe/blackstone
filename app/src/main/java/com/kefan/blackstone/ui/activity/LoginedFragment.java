package com.kefan.blackstone.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.bumptech.glide.Glide;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MY SHIP on 2017/5/7.
 */

public class LoginedFragment extends Fragment implements View.OnClickListener{
    private String getUpLoadTokenURL= APIManager.BASE_URL +"v1/upload/token";
    private String upLoadImageURL=APIManager.BASE_URL +"v1/user/avatar";
    private String updateURL=APIManager.BASE_URL +"v1/user/login";

    private String TAG="LoginedFragment";
    private final int CHOOSE_PICTURE=1;
    private final int CHOOSE_PICTURE_RESULT=2;
    private final int TAKE_PHOTO=3;
    private final int TAKE_PHOTO_RESULT=4;
    private final int GET_UPLOAD_TOKEN_OVER=5;
    private final int GET_IMAGE_KEY_OK=6;
    private ImageView myCollections;
    private ImageView myRecords;
    private ImageView picture;
    private TextView name;
    private Dialog dialog;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";


    private String choosePicturePath;//图片全路径
    private Uri choosePictureUri;

    private String takePhotoSavePath;//保存路径
    private String takePhotoSaveName;//图pian名
    private String takePhotoPath;//图片全路径
    private Uri takePhotoimageUri;

    private String uploadtoken;
    private String imageKey;
    private String path;
    private UserInformationUtil userInformationUtil;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.logined_fragment,null);
        myCollections= (ImageView) view.findViewById(R.id.logined_fragment_my_collections);
        myRecords= (ImageView) view.findViewById(R.id.logined_fragment_my_records);
        picture= (ImageView) view.findViewById(R.id.logined_fragment_image_view_picture);

        if(userInformationUtil.getAvatar()!=null)
        {
            Glide.with(getContext()).load(userInformationUtil.getAvatar()).placeholder(R.mipmap.person_center).into(picture);
        }else
        {
            Glide.with(getContext()).load(R.mipmap.person_center).into(picture);
        }

        name= (TextView) view.findViewById(R.id.logined_frgment_textView_name);


        name.setText(userInformationSharedPreferences.getString("name",""));
        initEvents();
        return view;
    }

    private void initEvents() {
        myCollections.setOnClickListener(this);
        myRecords.setOnClickListener(this);
        picture.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        userInformationSharedPreferences=getActivity().getSharedPreferences(userInformation,Context.MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();
        userInformationUtil=new UserInformationUtil(getContext());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.logined_fragment_my_collections:
                startActivity(new Intent(getContext(),MyCollectionsActivity.class));
//                getActivity(). overridePendingTransition(R.anim.in,R.anim.out);
                break;

            case R.id.logined_fragment_my_records:
                startActivity(new Intent(getContext(),MyRecordsActivity.class));
//                getActivity(). overridePendingTransition(R.anim.in,R.anim.out);
                break;

            case R.id.logined_fragment_image_view_picture:
                alterPicture();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(userInformationUtil.getAvatar()!=null)
        {
            Glide.with(getContext()).load(userInformationUtil.getAvatar()).placeholder(R.mipmap.person_center).into(picture);
        }else
        {
            Glide.with(getContext()).load(R.mipmap.person_center).into(picture);
        }
    }

    private void alterPicture() {

        final TextView deletePicture;
        TextView choosePicture;
        final TextView takePhoto;
        TextView actionCancel;
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display;
        display = windowManager.getDefaultDisplay();
        dialog = new Dialog(getContext(),R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_actionsheet, null);
        view.setMinimumWidth(display.getWidth());
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.show();

        deletePicture= (TextView) dialog.findViewById(R.id.dialog_change_picture_text_view_delete_picture);
        choosePicture= (TextView) dialog.findViewById(R.id.dialog_change_picture_text_view_choose_picture);
        takePhoto= (TextView) dialog.findViewById(R.id.dialog_change_picture_text_view_take_photo);
        actionCancel= (TextView) dialog.findViewById(R.id.dialog_change_picture_text_view_action_cancel);

        deletePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deletePicture();
                Glide.with(getContext()).load(R.mipmap.person_center).into(picture);

            }
        });

        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                choosePictures();

            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                takePhoto();

            }
        });

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void deletePicture() {
        upLoadImage();
        UserInformationUtil userInformationUtil=new UserInformationUtil(getContext());
        userInformationUtil.setAvatar(null);
    }

    private void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(),"ClipHeadPhoto/cache");
        if (!file.exists())
            file.mkdirs();
        takePhotoSavePath=Environment.getExternalStorageDirectory()+"/ClipHeadPhoto/cache/";
        takePhotoSaveName =System.currentTimeMillis()+ ".png";
        //photoSaveName =String.valueOf(System.currentTimeMillis()) + ".png";
        Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        takePhotoimageUri = Uri.fromFile(new File(takePhotoSavePath,takePhotoSaveName));
        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoimageUri);
        startActivityForResult(openCameraIntent, TAKE_PHOTO);
    }

    public void choosePictures()
    {
        Intent choosePictureIntent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //choosePictureIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        dialog.dismiss();
        startActivityForResult(choosePictureIntent,CHOOSE_PICTURE);
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");//相片类型
//        startActivityForResult(intent, CHOOSE_PICTURE);
    }

    public void upLoadImage()
    {
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        Map<String,String>map=new HashMap<>();
        map.put("avatar",null);
        JSONObject jsonObject=new JSONObject(map);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, upLoadImageURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        Log.d(TAG, "onResponse:上传成功 ");
//                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
//                        Message message=new Message();
//                        message.what=UPLOAD_IMAGE_OK;
//                        handler.sendMessage(message);
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
                Toast.makeText(getContext(), "没有网络", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: "+requestCode);
        switch (requestCode) {
            case CHOOSE_PICTURE://相册
                System.out.println("1");
                if (data != null) {
                    choosePictureUri = data.getData();
                    choosePicturePath = choosePictureUri.getPath();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(choosePictureUri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    choosePicturePath = cursor.getString(column_index);// 图片在的路径
                    Intent intent3 = new Intent(getContext(), ClipActivity.class);
                    intent3.putExtra("path", choosePicturePath);
                    startActivityForResult(intent3, CHOOSE_PICTURE_RESULT);

                }

                break;
            case CHOOSE_PICTURE_RESULT:
//                Toast.makeText(getContext(), "111111", Toast.LENGTH_SHORT).show();
                System.out.println("2");
                if(data!=null)
                {
                    final String temppath = data.getStringExtra("path");
                    picture.setImageBitmap(getLoacalBitmap(temppath));
                    Log.d(TAG, "getLoacalBitmap: "+"null");

                }
                break;
            case TAKE_PHOTO:
                takePhotoPath = takePhotoSavePath + takePhotoSaveName;
                if (takePhotoPath!=null) {
                    if (takePhotoimageUri != null) {
                        takePhotoimageUri = Uri.fromFile(new File(takePhotoPath));
                        if((new File(takePhotoPath)).exists())
                        {
                            Intent intent2 = new Intent(getContext(), ClipActivity.class);
                            intent2.putExtra("path", takePhotoPath);
                            startActivityForResult(intent2, TAKE_PHOTO_RESULT);
                        }

                    }

                }
                break;

            case TAKE_PHOTO_RESULT:
                if(data!=null)
                {
                    final String tempath = data.getStringExtra("path");
                    picture.setImageBitmap(getLoacalBitmap(tempath));
                }
                break;

        }
    }

    public  Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void updateUserInformation(Context context)
    {

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        Map<String,String> map=new HashMap<>();
        map.put("username",userInformationUtil.getUserName());
        map.put("pwd",userInformationUtil.getUserPwd());
        JSONObject jsonObject=new JSONObject(map);
        JsonObjectRequest updateRquest=new JsonObjectRequest(Request.Method.POST, updateURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        JSONObject data=jsonObject.getJSONObject("data");
                        JSONObject user=jsonObject.getJSONObject("user");
                        userInformationUtil.setId(user.getLong("id"));
                        userInformationUtil.setUserName(user.getString("mobile"));
                        userInformationUtil.setStudentId(user.getString("studentId"));
                        userInformationUtil.setName(user.getString("name"));
                        userInformationUtil.setGender(user.getString("gender"));
                        userInformationUtil.setToken(data.getString("token"));
                        userInformationUtil.setExpireAt(data.getLong("expireAt"));
                        if(user.has("avatar"))
                        {
                            userInformationUtil.setAvatar(user.getString("avatar"));
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(userInformationUtil.getAvatar()!=null)
                                {
                                    Glide.with(getContext()).load(userInformationUtil.getAvatar()).placeholder(R.mipmap.person_center).into(picture);
                                }else
                                {
                                    Glide.with(getContext()).load(R.mipmap.person_center).into(picture);
                                }
                            }
                        });
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
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(updateRquest);
    }
}
