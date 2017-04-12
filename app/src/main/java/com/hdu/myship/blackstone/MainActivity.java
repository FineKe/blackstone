package com.hdu.myship.blackstone;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Set;

import JsonUtil.JsonResolverList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private int textColor=Color.argb(100,74,144,226);
    private String getSpeciesList_url="http://api.blackstone.ebirdnote.cn/v1/species/list";//物种清单获取接口
    private RequestQueue requestQueue;//请求队列

    private FragmentManager fragmentManager;//fragment 管理者
    private FragmentTransaction transaction;//开启一个事列

    private SpeciesFragment speciesFragment;//创建一个speciesFragment
    private GuideFragment guideFragment;//创建guideFragment
    private AddRecordFragment addRecordFragment;//创建addRecordFragment
    private PersonalCenterFragment personalCenterFragment;//创建personalCenterFragment
    private SettingFragment settingFragment;//创建settingFragment

    /**
     * 底部菜单选项
     */
    private LinearLayout tab_species;
    private LinearLayout tab_guide;
    private LinearLayout tab_add_record;
    private LinearLayout tab_personal_center;
    private LinearLayout tab_setting;

    private TextView top_title;//头部
    /**
     * 以下5个textview仅仅只是为展示，以后用界面替换掉
     */
    private TextView textView_species;
    private TextView textView_guide;
    private TextView textView_add_record;
    private TextView textView_personal_center;
    private TextView textView_setting;
    /**
     * 以下5个imagebutton是底部菜单栏的图片按钮
     */
    private ImageButton imageButton_species;
    private ImageButton imageButton_guide;
    private ImageButton imageButton_add_record;
    private ImageButton imageButton_personal_center;
    private ImageButton imageButton_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initView();//初始化控件
        initEvents();//添加逻辑事件控制
        initData();//从网络中加载数据

    }

    private void initData() {//加载物种清单数据
        JsonObjectRequest getSpeciesList=new JsonObjectRequest(Request.Method.GET, getSpeciesList_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code;
                JSONObject data;
                try {
                    code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        data=jsonObject.getJSONObject("data");
                        JsonResolverList jsonResolverList=new JsonResolverList(data);
                        jsonResolverList.Resolve();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(getSpeciesList);

    }

    private void initView() {
        fragmentManager=getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();//开启一个事物
        requestQueue= Volley.newRequestQueue(this);//创建请求队列
        tab_species= (LinearLayout) findViewById(R.id.tab_species);
        tab_guide= (LinearLayout) findViewById(R.id.tab_guide);
        tab_add_record= (LinearLayout) findViewById(R.id.tab_add_record);
        tab_personal_center= (LinearLayout) findViewById(R.id.tab_personal_cneter);
        tab_setting= (LinearLayout) findViewById(R.id.tab_setting);

        top_title= (TextView) findViewById(R.id.textView_top_title);

        textView_species= (TextView) findViewById(R.id.textView_species);
        textView_guide= (TextView) findViewById(R.id.textView_guide);
        textView_add_record= (TextView) findViewById(R.id.textView_add_record);
        textView_personal_center= (TextView) findViewById(R.id.textView_personal_center);
        textView_setting= (TextView) findViewById(R.id.textView_setting);

        imageButton_species= (ImageButton) findViewById(R.id.imageButton_species);
        imageButton_guide= (ImageButton) findViewById(R.id.imageButton_guide);
        imageButton_add_record= (ImageButton) findViewById(R.id.imageButton_add_record);
        imageButton_personal_center= (ImageButton) findViewById(R.id.imageButton_personal_center);
        imageButton_setting= (ImageButton) findViewById(R.id.imageButton_setting);

    }

    private void initEvents()
    {   top_title.setText("物种");
        transaction.replace(R.id.frame_layout,new SpeciesFragment()).commit();
        tab_guide.setOnClickListener(this);
        tab_setting.setOnClickListener(this);
        tab_species.setOnClickListener(this);
        tab_personal_center.setOnClickListener(this);
        tab_add_record.setOnClickListener(this);

    }

    private void resetColor()//重置颜色
    {
        textView_guide.setTextColor(Color.BLACK);
        textView_species.setTextColor(Color.BLACK);
        textView_add_record.setTextColor(Color.BLACK);
        textView_personal_center.setTextColor(Color.BLACK);
        textView_setting.setTextColor(Color.BLACK);
    }

    /**
     * 主要是实现各种fragment的切换
     * @param v
     */
    @Override
    public void onClick(View v) {
        transaction=fragmentManager.beginTransaction();
        resetColor();
        switch (v.getId())
        {
            case R.id.tab_species://切换到物种界面
                if(speciesFragment==null)
                {
                    speciesFragment=new SpeciesFragment();
                    transaction.replace(R.id.frame_layout,speciesFragment);//用该fragment替换activity_main.xml的frame_layout来实现界面的切换
                }else
                {
                    transaction.replace(R.id.frame_layout,speciesFragment);
                }
                top_title.setText("物种");
                break;
            case R.id.tab_guide://切换到指南界面
                if(guideFragment==null)
                {
                    guideFragment=new GuideFragment();
                    transaction.replace(R.id.frame_layout,guideFragment);
                }else
                {
                    transaction.replace(R.id.frame_layout,guideFragment);
                }
                top_title.setText("指南");
                break;
            case R.id.tab_add_record://切换到添加记录界面
                if(addRecordFragment==null)
                {
                    addRecordFragment=new AddRecordFragment();
                    transaction.replace(R.id.frame_layout,addRecordFragment);
                }else
                {
                    transaction.replace(R.id.frame_layout,addRecordFragment);
                }
                top_title.setText("添加记录");
                break;
            case R.id.tab_personal_cneter://切换到个人中心界面
                if(personalCenterFragment==null)
                {
                    personalCenterFragment=new PersonalCenterFragment();
                    transaction.replace(R.id.frame_layout,personalCenterFragment);
                }else
                {
                    transaction.replace(R.id.frame_layout,personalCenterFragment);
                }
                top_title.setText("个人中心");
                break;
            case R.id.tab_setting://切换到设置界面
                if(settingFragment==null)
                {
                    settingFragment=new SettingFragment();
                    transaction.replace(R.id.frame_layout,settingFragment);
                }else
                {
                    transaction.replace(R.id.frame_layout,settingFragment);
                }
                top_title.setText("设置");
                break;
        }
        transaction.commit();//提交，必须commit以下，否则切换不会执行
    }



}
