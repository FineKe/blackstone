package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
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
import ui.adapter.SlideMenuAdapter;
import vo.SlideMenuVO;

public class MainActivity extends AutoLayoutActivity implements View.OnClickListener {


    private String TAG = "MainActivity";
    private int textColor = Color.argb(100, 74, 144, 226);
    private String SpeciesDetailedUrl = APIManager.rootDoname + "v1/species/";
    private String getCategoryURL = APIManager.rootDoname + "v1/species/categories";
    private RequestQueue requestQueue;//请求队列


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile = "isLogin";
    private Boolean isLogined;


    private TextView top_title;//头部
    public int test;

    public static List<List<Record>> records;

    private long exitTime = 0;

    @BindView(R.id.iv_home_header_view_main_activity)
    ImageView home;

    @BindView(R.id.iv_icon_header_view_main_activity)
    ImageView icon;

    @BindView(R.id.tv_signup_or_signup_header_view_main_activity)
    TextView signInUp;

    @BindView(R.id.lv_slide_menu_main_activity)
    ListView slideMenu;

    private int[] menusIcon = {R.drawable.guide, R.drawable.add_record,
            R.drawable.testing, R.drawable.my_collection,
            R.drawable.observe_record, R.drawable.setting,
            R.drawable.team_info};

    private List<SlideMenuVO>menuVOS=new ArrayList<>();

    private SlideMenuAdapter slideMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        initData();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
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

        String[] titles=getResources().getStringArray(R.array.slide_menu_title);
        for (int i = 0; i < titles.length; i++) {
            SlideMenuVO menuVO=new SlideMenuVO(menusIcon[i],titles[i]);
            menuVOS.add(menuVO);
        }

        slideMenuAdapter=new SlideMenuAdapter(menuVOS);

        slideMenu.setAdapter(slideMenuAdapter);

    }

    private void initEvents() {


    }


    @Override
    public void onClick(View v) {
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
}
