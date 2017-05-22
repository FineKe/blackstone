package com.hdu.myship.blackstone;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import JsonUtil.JsonResolverSpeciesDetailed;
import ShapeUtil.GlideRoundTransform;
import database.Amphibia;
import database.Bird;
import database.Insect;
import database.Reptiles;

import static com.hdu.myship.blackstone.R.color.bootstrap_brand_primary;

public class SpeciesDeatailedActivity extends AutoLayoutActivity implements View.OnClickListener{
    private String getSpeciesDetailedURL="http://api.blackstone.ebirdnote.cn/v1/species/";
    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    private int singal;//物种id
    private String speciesType;
    private ViewPager viewPager;
    private ArrayList<View> views;
    private ArrayList<ImageView> pointers;//点
    private Object speciesDetailed;
    private MyViewPagerAdapter adapter;
    String TAG="TEST";
    private String speciesTypeChineseName;
    private String SpeciesChineseName;
    private TextView speciesClassName;
    private TextView speciesName;
    private ImageView copyRight;
    private ImageButton actionBack;
    private LinearLayout tab_pointers;

    private boolean start=false;
    private int oldPosition=0;
    private int currentIndex;
    private int a;
    private ScheduledExecutorService scheduledExecutorService;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        singal=getIntent().getIntExtra("singal",1);
        speciesType=getIntent().getStringExtra("speciesType");
        //speciesTypeChineseName=getIntent().getStringExtra("speciesTypeChineseName");
        setContentView(R.layout.activity_species_deatailed);
        Log.d(TAG, "onCreate: "+singal);
        initData();
        initViews();


    }

    private void initData() {

        views=new ArrayList<>();
        pointers=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);
        scheduledExecutorService= Executors.newSingleThreadScheduledExecutor();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        tab_pointers= (LinearLayout) findViewById(R.id.activity_species_deatailed_linear_layout_pointers);
        copyRight= (ImageView) findViewById(R.id.activity_species_deatailed_image_view_copy_right);
        viewPager = (ViewPager) findViewById(R.id.species_detailed_viewpager);
        speciesClassName= (TextView) findViewById(R.id.species_detailed_title_bar_textView_speciesClassName);
        speciesName= (TextView) findViewById(R.id.species_detailed_title_bar_textView_speciesNameAndOrder);
        actionBack= (ImageButton) findViewById(R.id.species_detailed_title_bar_img_btn_back);
        speciesClassName.setText(speciesTypeChineseName);
        speciesName.setText(SpeciesChineseName);


        switch (speciesType)
        {
            case "reptiles":speciesDetailed=DataSupport.where("singal=?",singal+"").find(Reptiles.class).get(0);
                for(String picture:((Reptiles)speciesDetailed).getImgs())
                {
                    views.add(createView(picture));
                    pointers.add(createPointer());
                }
                break;
            case "bird":;speciesDetailed=DataSupport.where("singal=?",singal+"").find(Bird.class).get(0);
                for(String picture:((Bird)speciesDetailed).getImgs())
                {
                    views.add(createView(picture));
                    pointers.add(createPointer());
                }
                break;
            case "amphibia":speciesDetailed=DataSupport.where("singal=?",singal+"").find(Amphibia.class).get(0);
                for(String picture:((Amphibia)speciesDetailed).getImgs())
                {
                    views.add(createView(picture));
                    pointers.add(createPointer());
                }
                break;
            case "insect":;speciesDetailed=DataSupport.where("singal=?",singal+"").find(Insect.class).get(0);
                for(String picture:((Insect)speciesDetailed).getImgs())
                {
                    views.add(createView(picture));
                    pointers.add(createPointer());
                }
                break;

        }
        for(ImageView imageView:pointers)
        {
            tab_pointers.addView(imageView);
        }

        currentIndex=0;
        pointers.get(currentIndex).setImageResource(R.mipmap.pointer_pressed);
        adapter=new MyViewPagerAdapter();
        viewPager.setAdapter(adapter);

        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(),3,3, TimeUnit.SECONDS);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                a=position;
                setCurrentPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setCurrentPoint(int position){
        pointers.get(oldPosition).setImageResource(R.mipmap.pointer_normal);
        pointers.get(position).setImageResource(R.mipmap.pointer_pressed);
        currentIndex = position;
        oldPosition=position;
    }


    @Override
    public void onClick(View v) {

    }

    class MyViewPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v=views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private View createView(String picture)
    {
        RoundedImageView imgView=new RoundedImageView(this);
        imgView.setBackground(getDrawable(R.drawable.view_pager_background));
        imgView.setCornerRadius(6);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(picture+"?imageslim").placeholder(R.mipmap.loading_big).transform(new GlideRoundTransform(this,6)).into(imgView);
        return imgView;
    }

    private ImageView createPointer()
    {
        ImageView pointer=new ImageView(this);
        pointer.setImageResource(R.mipmap.pointer_normal);
        pointer.setAdjustViewBounds(true);
        pointer.setPadding(2,0,2,0);
        return pointer;
    }
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            if(start==true){
                currentIndex = (currentIndex +1) % views.size();
                //更新界面
                handler.obtainMessage().sendToTarget();
            }
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //设置当前页面
            viewPager.setCurrentItem(currentIndex);
        }
    };
}
