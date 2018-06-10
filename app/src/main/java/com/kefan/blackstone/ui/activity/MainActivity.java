package com.kefan.blackstone.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.JsonUtil.JsonResolverSpeciesDetailedSave;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.HandlerConstant;
import com.kefan.blackstone.database.Amphibia;
import com.kefan.blackstone.database.Bird;
import com.kefan.blackstone.database.Insect;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.database.Reptiles;
import com.kefan.blackstone.database.Species;
import com.kefan.blackstone.ui.fragment.GuideFragment;
import com.kefan.blackstone.ui.fragment.HomeFragment;
import com.kefan.blackstone.ui.fragment.SettingFragment;
import com.kefan.blackstone.ui.fragment.TeamFragment;
import com.kefan.blackstone.ui.fragment.TestingFragment;
import com.kefan.blackstone.util.UserSharePreferenceUtil;
import com.kefan.blackstone.widget.HeaderBar;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kefan.blackstone.BlackStoneApplication.getContext;


public class MainActivity extends AutoLayoutActivity implements View.OnClickListener {

    private String TAG = "MainActivity";
    private int textColor = Color.argb(100, 74, 144, 226);
    private String SpeciesDetailedUrl = APIManager.BASE_URL + "v1/species/";
    private String getCategoryURL = APIManager.BASE_URL + "v1/species/categories";
    private RequestQueue requestQueue;//请求队列


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile = "isLogin";



    public static List<List<Record>> records;

    private long exitTime = 0;

    private final int CHOOSE_PICTURE=1;
    private final int CHOOSE_PICTURE_RESULT=2;
    private final int TAKE_PHOTO=3;
    private final int TAKE_PHOTO_RESULT=4;

    private String choosePicturePath;//图片全路径
    private Uri choosePictureUri;

    private String takePhotoSavePath;//保存路径
    private String takePhotoSaveName;//图pian名
    private String takePhotoPath;//图片全路径
    private Uri takePhotoimageUri;

    @BindView(R.id.drawer_main_activity)
    DrawerLayout drawerLayout;


    @BindView(R.id.headerbar_main_activity)
    public HeaderBar headerBar;

    @BindView(R.id.iv_home_header_view_main_activity)
    ImageView home;

    @BindView(R.id.iv_icon_header_view_main_activity)
    ImageView icon;

    @BindView(R.id.tv_signup_header_view_main_activity)
    TextView signUp;

    @BindView(R.id.tv_signin_header_view_main_activity)
    TextView signIn;


    @BindView(R.id.ll_guide_main_activity)
    LinearLayout guide;

    @BindView(R.id.ll_add_record_main_activity)
    LinearLayout addRecord;

    @BindView(R.id.ll_testing_main_activity)
    LinearLayout testing;

    @BindView(R.id.ll_collection_main_activity)
    LinearLayout collection;

    @BindView(R.id.ll_observe_record_main_activity)
    LinearLayout observeRcord;

    @BindView(R.id.ll_setting_main_activity)
    LinearLayout setting;

    @BindView(R.id.ll_team_main_activity)
    LinearLayout team;

    @BindView(R.id.ll_combine_signinup_header_view_main_activity)
    LinearLayout combine;


    private HomeFragment homeFragment;

    private GuideFragment guideFragment;

    private AddRecordFragment addRecordFragment;

    private TestingFragment testingFragment;

    private CollectionHomeFragment collectionHomeFragment;

    private ObserveRecordFragment observeRecordFragment;

    private SettingFragment settingFragment;

    private TeamFragment teamFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        initData();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();//初始化控件
        initEvents();//添加逻辑事件控制
    }

    private void initData() {
        sharedPreferences = getSharedPreferences(isLoginedFile, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int size = (DataSupport.findAll(Bird.class)).size() + (DataSupport.findAll(Amphibia.class)).size() + (DataSupport.findAll(Insect.class)).size() + (DataSupport.findAll(Reptiles.class)).size();
        System.out.println("size" + size + ":" + (DataSupport.findAll(Species.class)).size());
        if (size != (DataSupport.findAll(Species.class)).size()) {
            DataSupport.deleteAll(Bird.class);
            DataSupport.deleteAll(Amphibia.class);
            DataSupport.deleteAll(Reptiles.class);
            DataSupport.deleteAll(Insect.class);
            DataSupport.deleteAll(Record.class);
            requestQueue = Volley.newRequestQueue(this);//创建请求队列
            List<Species> speciesList = DataSupport.findAll(Species.class);
            for (Species s : speciesList) {
                JsonObjectRequest speciesDetailedRequest = new JsonObjectRequest(Request.Method.GET, SpeciesDetailedUrl + s.getSingal(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int code = jsonObject.getInt("code");
                            if (code == 88) {
                                JsonResolverSpeciesDetailedSave speciesDetailed = new JsonResolverSpeciesDetailedSave(jsonObject);
                                speciesDetailed.ResolveSpeciesDetailed();
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
                requestQueue.add(speciesDetailedRequest);
            }

            for (Species species : DataSupport.findAll(Species.class)) {
                Record record = new Record(species.getChineseName(), species.getSingal(), species.getSpeciesType());
                record.save();
                System.out.println(species.getSingal());
            }

        }

        updateUserInformation(this);
    }

    public void updateUserInformation(Context context) {
        IsLoginUtil loginUtil = new IsLoginUtil(context);
        if (loginUtil.getLogined()) {
            UserInformationUtil informationUtil = new UserInformationUtil(context);
            informationUtil.updateUserInfomation();
        }
    }

    private void initView() {

        if (UserSharePreferenceUtil.getUser(this).isIslogined()) {
            combine.setVisibility(View.INVISIBLE);
            combine.setClickable(false);
        }else {
            combine.setVisibility(View.VISIBLE);
            combine.setClickable(true);
        }

        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content_main_activity, homeFragment)
                .commit();


    }

    private void initEvents() {

        headerBar.getLeftPart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT, true);
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                LoginDialog.getLoginDialog(MainActivity.this,handler).show();

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                alterPicture();
            }
        });


        home.setOnClickListener(this);
        guide.setOnClickListener(this);
        addRecord.setOnClickListener(this);
        testing.setOnClickListener(this);
        collection.setOnClickListener(this);
        observeRcord.setOnClickListener(this);
        setting.setOnClickListener(this);
        team.setOnClickListener(this);
    }


    /**
     * 侧滑栏点击 切换fragment
     * @param v
     */
    @Override
    public void onClick(View v) {

        //关闭侧滑栏
        closeDrawer();

        switch (v.getId()) {

            //首页点击切换
            case R.id.iv_home_header_view_main_activity:

                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, homeFragment)
                        .commit();
                break;
            //指南点击切换
            case R.id.ll_guide_main_activity:

                if (guideFragment == null) {
                    guideFragment = new GuideFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, guideFragment)
                        .commit();

                break;

            //添加记录切换
            case R.id.ll_add_record_main_activity:

                if (addRecordFragment == null) {
                    addRecordFragment = new AddRecordFragment();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, addRecordFragment)
                        .commit();

                break;

            //小测试切换
            case R.id.ll_testing_main_activity:

                if (testingFragment == null) {
                    testingFragment = new TestingFragment();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, testingFragment)
                        .commit();

                break;

            //收藏切换
            case R.id.ll_collection_main_activity:

                if (collectionHomeFragment == null) {

                    collectionHomeFragment = new CollectionHomeFragment();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, collectionHomeFragment)
                        .commit();

                break;

            //观察记录切换
            case R.id.ll_observe_record_main_activity:

                if (observeRecordFragment == null) {
                    observeRecordFragment = new ObserveRecordFragment();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, observeRecordFragment)
                        .commit();

                break;

            //切换到设置
            case R.id.ll_setting_main_activity:

                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, settingFragment)
                        .commit();

                break;

            //团队信息
            case R.id.ll_team_main_activity:

                if (teamFragment == null) {
                    teamFragment = new TeamFragment();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, teamFragment)
                        .commit();

                break;

        }
    }

    public void createBasicRecords() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Record> birdRecord = DataSupport.where("speciesType=?", "bird").find(Record.class);
                List<Record> amphibiaRecord = DataSupport.where("speciesType=?", "amphibia").find(Record.class);
                List<Record> reptilesRecord = DataSupport.where("speciesType=?", "reptiles").find(Record.class);
                List<Record> insectRecord = DataSupport.where("speciesType=?", "insect").find(Record.class);
                records.add(birdRecord);
                records.add(amphibiaRecord);
                records.add(reptilesRecord);
                records.add(insectRecord);
            }
        }).start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }

        /**
         * 主要是实现各种fragment的切换
         * @param v
         */
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        moveTaskToBack(true);
    }


    //关闭侧滑栏
    private void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT, true);
    }

    private void alterPicture() {

        final TextView deletePicture;
        TextView choosePicture;
        final TextView takePhoto;
        TextView actionCancel;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display;
        display = windowManager.getDefaultDisplay();
        final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.view_actionsheet, null);
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
                Glide.with(getContext()).load(R.mipmap.person_center).into(icon);

            }
        });

        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                choosePictures(dialog);

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
        if (!file.exists()) {
            file.mkdirs();
        }
        takePhotoSavePath=Environment.getExternalStorageDirectory()+"/ClipHeadPhoto/cache/";
        takePhotoSaveName =System.currentTimeMillis()+ ".png";
        //photoSaveName =String.valueOf(System.currentTimeMillis()) + ".png";
        Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        takePhotoimageUri = Uri.fromFile(new File(takePhotoSavePath,takePhotoSaveName));
        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoimageUri);
        startActivityForResult(openCameraIntent, TAKE_PHOTO);
    }

    public void choosePictures(Dialog dialog)
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
        Map<String,String> map=new HashMap<>();
        map.put("avatar",null);
        JSONObject jsonObject=new JSONObject(map);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, APIManager.UPLOAD_IMAGE_URL, jsonObject, new Response.Listener<JSONObject>() {
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
                    Cursor cursor = getContentResolver().query(choosePictureUri, proj, null, null, null);
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
                    icon.setImageBitmap(getLoacalBitmap(temppath));
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
                    icon.setImageBitmap(getLoacalBitmap(tempath));
                }
                break;

        }
    }


    public Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerConstant.LOGIN_SUCCESS:
                    combine.setVisibility(View.INVISIBLE);
                    combine.setClickable(false);

            }
        }
    };
}
