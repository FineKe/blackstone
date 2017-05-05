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
    private Object object;
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
        views=new ArrayList<>();
        initData();
        initViews();

    }

    private void initData() {

        adapter=new MyViewPagerAdapter();
        requestQueue= Volley.newRequestQueue(this);
        request=new JsonObjectRequest(Request.Method.GET, getSpeciesDetailedURL + singal, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JsonResolverSpeciesDetailed detailed=new JsonResolverSpeciesDetailed();
                switch (speciesType) {
                    case "reptiles":
                        object=detailed.ResolveRepties(jsonObject);
                        final Reptiles re=detailed.ResolveRepties(jsonObject);
                        for(String picture:re.getImgs())
                        {
                            views.add(createView(picture));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SpeciesChineseName=re.getChineseName();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        break;

                    case "bird":
//                        object=detailed.ResolveBird(jsonObject);
                        final Bird bi=detailed.ResolveBird(jsonObject);
                        for(String picture:bi.getImgs())
                        {
                            Log.d(TAG, "onResponse: bird"+picture);
                            views.add(createView(picture));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SpeciesChineseName=bi.getChineseName();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        break;

                    case "amphibia":
                        object=detailed.ResolveAmphibia(jsonObject);
                        final Amphibia am=detailed.ResolveAmphibia(jsonObject);
                        for(String picture:am.getImgs())
                        {
                            Log.d(TAG, "onResponse: am"+picture);
                            views.add(createView(picture));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SpeciesChineseName=am.getChineseName();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        break;

                    case "insect":
                        object=detailed.ResolveInsect(jsonObject);
                        final Insect in=detailed.ResolveInsect(jsonObject);
                        for(String picture:in.getImgs())
                        {
                            Log.d(TAG, "onResponse: in"+picture);
                            views.add(createView(picture));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SpeciesChineseName=in.getChineseName();
                                    adapter.notifyDataSetChanged();

                                }
                            });
                        }
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SpeciesDeatailedActivity.this,"请求异常",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);

    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.species_detailed_viewpager);
        speciesClassName= (TextView) findViewById(R.id.species_detailed_title_bar_textView_speciesClassName);
        speciesName= (TextView) findViewById(R.id.species_detailed_title_bar_textView_speciesNameAndOrder);
        actionBack= (ImageButton) findViewById(R.id.species_detailed_title_bar_img_btn_back);
        speciesClassName.setText(speciesTypeChineseName);
        speciesName.setText(SpeciesChineseName);
        viewPager.setAdapter(adapter);
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
