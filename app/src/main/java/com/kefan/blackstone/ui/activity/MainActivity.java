package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.fragment.HomeFragment;
import com.kefan.blackstone.ui.fragment.TestingFragment;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

import JavaBean.APIManager;
import JsonUtil.JsonResolverSpeciesDetailedSave;
import butterknife.BindView;
import butterknife.ButterKnife;
import database.Amphibia;
import database.Bird;
import database.Insect;
import database.Record;
import database.Reptiles;
import database.Species;
import widget.HeaderBar;

;

public class MainActivity extends AutoLayoutActivity implements View.OnClickListener {


    private String TAG = "MainActivity";
    private int textColor = Color.argb(100, 74, 144, 226);
    private String SpeciesDetailedUrl = APIManager.BASE_URL + "v1/species/";
    private String getCategoryURL = APIManager.BASE_URL + "v1/species/categories";
    private RequestQueue requestQueue;//请求队列


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile = "isLogin";
    private Boolean isLogined;


    private TextView top_title;//头部
    public int test;

    public static List<List<Record>> records;

    private long exitTime = 0;

    @BindView(R.id.drawer_main_activity)
    DrawerLayout drawerLayout;

    @BindView(R.id.headerbar_main_activity)
    HeaderBar headerBar;

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

    private HomeFragment homeFragment;

    private GuideFragment guideFragment;

    private TestingFragment testingFragment;


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

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT, true);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content_main_activity, homeFragment)
                        .commit();
                headerBar.getCenterTextView().setText(R.string.title_home_page);
                headerBar.getRightImageView().setVisibility(View.VISIBLE);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                LoginDialog.getLoginDialog(MainActivity.this).show();

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        guide.setOnClickListener(this);
        addRecord.setOnClickListener(this);
        testing.setOnClickListener(this);
        collection.setOnClickListener(this);
        observeRcord.setOnClickListener(this);
        setting.setOnClickListener(this);
        team.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        closeDrawer();

        switch (v.getId()) {
            case R.id.ll_guide_main_activity:
                if (guideFragment == null) {
                    guideFragment = new GuideFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content_main_activity, guideFragment).commit();
                headerBar.getCenterTextView().setText("指南");
                headerBar.getRightImageView().setVisibility(View.INVISIBLE);

                break;

            case R.id.ll_add_record_main_activity:
                startActivity(new Intent(this, AddRecordActivity.class));

                break;

            case R.id.ll_testing_main_activity:

                if (testingFragment == null) {
                    testingFragment = new TestingFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content_main_activity, testingFragment).commit();
                headerBar.getCenterTextView().setText("小测试");
                headerBar.getRightImageView().setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_collection_main_activity:
                startActivity(new Intent(this, MyCollectionsActivity.class));
                break;
            case R.id.ll_observe_record_main_activity:
                startActivity(new Intent(this, MyRecordsActivity.class));
                break;
            case R.id.ll_setting_main_activity:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.ll_team_main_activity:
                startActivity(new Intent(this, MakeTeamActivity.class));
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        moveTaskToBack(true);
    }


    private void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT, true);
    }
}
