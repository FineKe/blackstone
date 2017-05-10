package com.hdu.myship.blackstone;

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
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import JsonUtil.JsonResolverSpeciesDetailed;
import database.Amphibia;
import database.Bird;
import database.Insect;
import database.Reptiles;

public class SpeciesDeatailedActivity extends AutoLayoutActivity implements View.OnClickListener{
    private String getSpeciesDetailedURL="http://api.blackstone.ebirdnote.cn/v1/species/";
    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    private int singal;
    private String speciesType;
    private ViewPager viewPager;
    private ArrayList<View> views;
    private Object speciesDetailed;
    private MyViewPagerAdapter adapter;
    String TAG="TEST";
    private String speciesTypeChineseName;
    private String SpeciesChineseName;
    private TextView speciesClassName;
    private TextView speciesName;

    private ImageButton actionBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        singal=getIntent().getIntExtra("singal",1);
        speciesType=getIntent().getStringExtra("speciesType");
        speciesTypeChineseName=getIntent().getStringExtra("speciesTypeChineseName");
        setContentView(R.layout.activity_species_deatailed);
        Log.d(TAG, "onCreate: "+singal);
        initViews();
        initData();

    }

    private void initData() {


        requestQueue= Volley.newRequestQueue(this);


    }

    private void initViews() {
        adapter=new MyViewPagerAdapter();
        viewPager = (ViewPager) findViewById(R.id.species_detailed_viewpager);
        speciesClassName= (TextView) findViewById(R.id.species_detailed_title_bar_textView_speciesClassName);
        speciesName= (TextView) findViewById(R.id.species_detailed_title_bar_textView_speciesNameAndOrder);
        actionBack= (ImageButton) findViewById(R.id.species_detailed_title_bar_img_btn_back);
        speciesClassName.setText(speciesTypeChineseName);
        speciesName.setText(SpeciesChineseName);
        viewPager.setAdapter(adapter);
        views=new ArrayList<>();
        switch (speciesType)
        {
            case "reptiles":speciesDetailed= DataSupport.find(Reptiles.class,singal);
                for(String picture:((Reptiles)speciesDetailed).getImgs())
                {
                    views.add(createView(picture));
                }
                break;
            case "bird":;speciesDetailed=DataSupport.find(Bird.class,singal);
                for(String picture:((Bird)speciesDetailed).getImgs())
                {
                    views.add(createView(picture));
                }
                break;
            case "amphibia":speciesDetailed=DataSupport.find(Amphibia.class,singal);
                for(String picture:((Amphibia)speciesDetailed).getImgs())
                {
                    views.add(createView(picture));
                }
                break;
            case "insect":;speciesDetailed=DataSupport.find(Insect.class,singal);
                for(String picture:((Insect)speciesDetailed).getImgs())
                {
                    views.add(createView(picture));
                }
                break;
        }
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

    private View createView(String picture)
    {
        ImageView imgView=new ImageView(this);
        imgView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(picture+"?imageslim").placeholder(R.mipmap.loading).into(imgView);
        return imgView;
    }
}
