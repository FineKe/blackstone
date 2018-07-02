package com.kefan.blackstone.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.kefan.blackstone.database.Mamal;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.database.Reptiles;
import com.kefan.blackstone.database.Species;
import com.kefan.blackstone.receiver.LogoutReceiver;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.dialog.LoginDialog;
import com.kefan.blackstone.ui.fragment.AddRecordFragment;
import com.kefan.blackstone.ui.fragment.GuideFragment;
import com.kefan.blackstone.ui.fragment.HomeFragment;
import com.kefan.blackstone.ui.fragment.SettingFragment;
import com.kefan.blackstone.ui.fragment.TeamFragment;
import com.kefan.blackstone.ui.fragment.TestingFragment;
import com.kefan.blackstone.util.ToastUtil;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    private final int CHOOSE_PICTURE = 1;
    private final int CHOOSE_PICTURE_RESULT = 2;
    private final int TAKE_PHOTO = 3;
    private final int TAKE_PHOTO_RESULT = 4;

    private String choosePicturePath;//图片全路径
    private Uri choosePictureUri;

    private String takePhotoSavePath;//保存路径
    private String takePhotoSaveName;//图pian名
    private String takePhotoPath;//图片全路径
    private Uri takePhotoimageUri;

    public float ratio = 0f;

    @BindView(R.id.drawer_main_activity)
    DrawerLayout drawerLayout;


    @BindView(R.id.headerbar_main_activity)
    public HeaderBar headerBar;

    @BindView(R.id.iv_home_header_view_main_activity)
    public ImageView home;

    @BindView(R.id.iv_icon_header_view_main_activity)
    public ImageView icon;

    @BindView(R.id.tv_signup_header_view_main_activity)
    public TextView signUp;

    @BindView(R.id.tv_signin_header_view_main_activity)
    public TextView signIn;


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

    @BindView(R.id.tv_center_place_holer_header_view_main_activity)
    public TextView centerPlaceHolder;


    private UserService userService;

    private HomeFragment homeFragment;

    private GuideFragment guideFragment;

    private AddRecordFragment addRecordFragment;

    private TestingFragment testingFragment;

    private CollectionHomeFragment collectionHomeFragment;

    private ObserveRecordFragment observeRecordFragment;

    private SettingFragment settingFragment;

    private TeamFragment teamFragment;


    private LogoutReceiver logoutReceiver;


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

        userService = new UserServiceImpl();

        sharedPreferences = getSharedPreferences(isLoginedFile, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int size = (DataSupport.count(Bird.class)) + (DataSupport.count(Amphibia.class)) + (DataSupport.count(Insect.class)) + (DataSupport.count(Reptiles.class))+(DataSupport.count(Mamal.class));
        if (size != (DataSupport.count(Species.class))) {
            DataSupport.deleteAll(Bird.class);
            DataSupport.deleteAll(Amphibia.class);
            DataSupport.deleteAll(Reptiles.class);
            DataSupport.deleteAll(Insect.class);
            DataSupport.deleteAll(Record.class);
            DataSupport.deleteAll(Mamal.class);
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

        }

        updateUserInformation(this);

        logoutReceiver=new LogoutReceiver(this);
        IntentFilter filter=new IntentFilter("logout");
        registerReceiver(logoutReceiver,filter);

    }

    public void updateUserInformation(Context context) {
        IsLoginUtil loginUtil = new IsLoginUtil(context);
        if (loginUtil.getLogined()) {
            UserInformationUtil informationUtil = new UserInformationUtil(context);
            informationUtil.updateUserInfomation();
        }
    }

    private void initView() {

        initUserView();

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
                LoginDialog.getLoginDialog(MainActivity.this, handler).show();

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
     *
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

                if (!userService.isLogined()) {
                    ToastUtil.showToast(getContext(),"未登录");
                    return;
                }

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

                if (!userService.isLogined()) {
                    ToastUtil.showToast(getContext(),"未登录");
                    return;
                }

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

                if (!userService.isLogined()) {
                    ToastUtil.showToast(getContext(),"未登录");
                    return;
                }

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

                if (!userService.isLogined()) {
                    ToastUtil.showToast(getContext(),"未登录");
                    return;
                }

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
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
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

        deletePicture = (TextView) dialog.findViewById(R.id.dialog_change_picture_text_view_delete_picture);
        choosePicture = (TextView) dialog.findViewById(R.id.dialog_change_picture_text_view_choose_picture);
        takePhoto = (TextView) dialog.findViewById(R.id.dialog_change_picture_text_view_take_photo);
        actionCancel = (TextView) dialog.findViewById(R.id.dialog_change_picture_text_view_action_cancel);

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
        userService.getUser().setAvatar(null);
    }

    private void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "ClipHeadPhoto/cache");
        if (!file.exists()) {
            file.mkdirs();
        }
        takePhotoSavePath = Environment.getExternalStorageDirectory() + "/ClipHeadPhoto/cache/";
        takePhotoSaveName = System.currentTimeMillis() + ".png";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE,"image/png");
            values.put(MediaStore.Images.Media.DATA,new File(takePhotoSavePath, takePhotoSaveName).getAbsolutePath());
            takePhotoimageUri=getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

            Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoimageUri);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            takePhotoimageUri = Uri.fromFile(new File(takePhotoSavePath, takePhotoSaveName));
            openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoimageUri);
            startActivityForResult(openCameraIntent, TAKE_PHOTO);
        }

    }

    public void choosePictures(Dialog dialog) {
        Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        dialog.dismiss();
        startActivityForResult(choosePictureIntent, CHOOSE_PICTURE);
    }

    public void upLoadImage() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Map<String, String> map = new HashMap<>();
        map.put("avatar", null);
        JSONObject jsonObject = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, APIManager.UPLOAD_IMAGE_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code = jsonObject.getInt("code");
                    if (code == 88) {

                        ToastUtil.showToast(getContext(),"删除成功");

                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", userService.getToken());
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + requestCode);
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
                if (data != null) {
                    final String temppath = data.getStringExtra("path");
                    icon.setImageBitmap(getLoacalBitmap(temppath));
                    Log.d(TAG, "getLoacalBitmap: " + "null");

                }
                break;
            case TAKE_PHOTO:
                takePhotoPath = takePhotoSavePath + takePhotoSaveName;

                System.out.println(takePhotoPath);

                if (takePhotoPath != null) {
                    if (takePhotoimageUri != null) {
                        takePhotoimageUri = Uri.fromFile(new File(takePhotoPath));
                        if ((new File(takePhotoPath)).exists()) {
                            Intent intent = new Intent(getContext(), ClipActivity.class);
                            intent.putExtra("path", takePhotoPath);
                            startActivityForResult(intent, TAKE_PHOTO_RESULT);
                        }

                    }
                }
                break;

            case TAKE_PHOTO_RESULT:
                if (data != null) {
                    final String tempath = data.getStringExtra("path");
//                    icon.setImageBitmap(getLoacalBitmap(tempath));
                    Glide.with(this).load(tempath).into(icon);
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

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerConstant.LOGIN_SUCCESS:

                    initUserView();

                    break;


            }
        }
    };

    private void initUserView() {

        if (userService.isLogined()) {

            //加载头像
            Glide.with(this).load(userService.getUser().getAvatar()).into(icon);
            signIn.setVisibility(View.GONE);
            signUp.setVisibility(View.GONE);

            centerPlaceHolder.setText(userService.getUser().getName());

            combine.setClickable(false);
        } else {
            signIn.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.VISIBLE);

            centerPlaceHolder.setText("/");
            combine.setClickable(true);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(logoutReceiver);
    }
}
