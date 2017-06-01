package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.DrmInitData;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MY SHIP on 2017/5/7.
 */

public class LoginedFragment extends Fragment implements View.OnClickListener{
    private String getUpLoadTokenURL="http://api.blackstone.ebirdnote.cn/v1/upload/token";
    private String upLoadImageURL="http://api.blackstone.ebirdnote.cn/v1/user/avatar";

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.logined_fragment,null);
        myCollections= (ImageView) view.findViewById(R.id.logined_fragment_my_collections);
        myRecords= (ImageView) view.findViewById(R.id.logined_fragment_my_records);
        picture= (ImageView) view.findViewById(R.id.logined_fragment_image_view_picture);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.logined_fragment_my_collections:
                startActivity(new Intent(getContext(),MyCollectionsActivity.class));
                getActivity(). overridePendingTransition(R.anim.in,R.anim.out);
                break;

            case R.id.logined_fragment_my_records:
                startActivity(new Intent(getContext(),MyRecordsActivity.class));
                getActivity(). overridePendingTransition(R.anim.in,R.anim.out);
                break;

            case R.id.logined_fragment_image_view_picture:
                alterPicture();
                break;
        }
    }

    private void alterPicture() {

        TextView deletePicture;
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
            }
        });

        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Intent choosePictureIntent=new Intent(Intent.ACTION_GET_CONTENT);
        choosePictureIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        dialog.dismiss();
        startActivityForResult(choosePictureIntent,CHOOSE_PICTURE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: "+requestCode);
        switch (requestCode) {
            case CHOOSE_PICTURE://相册
                System.out.println("1");
                choosePictureUri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor =getActivity().managedQuery(choosePictureUri, proj, null, null,null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                choosePicturePath = cursor.getString(column_index);// 图片在的路径
                Intent intent3=new Intent(getContext(), ClipActivity.class);
                intent3.putExtra("path", choosePicturePath);
                startActivityForResult(intent3, CHOOSE_PICTURE_RESULT);
                break;
            case CHOOSE_PICTURE_RESULT:
                Toast.makeText(getContext(), "111111", Toast.LENGTH_SHORT).show();
                System.out.println("2");
                final String temppath = data.getStringExtra("path");
                picture.setImageBitmap(getLoacalBitmap(temppath));
                Log.d(TAG, "getLoacalBitmap: "+"null");

                break;
            case TAKE_PHOTO:
                takePhotoPath=takePhotoSavePath+takePhotoSaveName;
                takePhotoimageUri = Uri.fromFile(new File(takePhotoPath));
                Intent intent2=new Intent(getContext(), ClipActivity.class);
                intent2.putExtra("path", takePhotoPath);
                startActivityForResult(intent2, TAKE_PHOTO_RESULT);
                break;

            case TAKE_PHOTO_RESULT:
                final String tempath = data.getStringExtra("path");
                picture.setImageBitmap(getLoacalBitmap(tempath));

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
}
