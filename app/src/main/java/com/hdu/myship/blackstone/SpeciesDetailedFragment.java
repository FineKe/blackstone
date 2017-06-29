package com.hdu.myship.blackstone;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import JavaBean.APIManager;
import database.Amphibia;

/**
 * Created by MY SHIP on 2017/4/28.
 */

public class SpeciesDetailedFragment extends Fragment {
    private String getSpeciesClassesURL= APIManager.rootDoname+"v1/species/categories";
    private String TAG="SpeciesDetailedFragment";


    private int singl;

    private ViewPager viewPager;

    private ArrayList<View> views;

    private int currentIndex;
    private int oldPosition=0;

    private boolean start=true;

    public SpeciesDetailedFragment() {
    }

    private ScheduledExecutorService scheduledExecutorService;
    public static final SpeciesDetailedFragment newInstance(int singl)
    {
        SpeciesDetailedFragment fragment=new SpeciesDetailedFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("singl",singl);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.singl=getArguments().getInt("singl");
        views=new ArrayList<>();
        scheduledExecutorService= Executors.newSingleThreadScheduledExecutor();
        List<Amphibia> speciesDetaileds=new ArrayList<>();
        speciesDetaileds=DataSupport.where("singl=?",""+singl).find(Amphibia.class);


        Amphibia sp=speciesDetaileds.get(0);
       for(String picture:sp.getImgs())
        {
            views.add(createView(picture));
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getContext(),R.layout.species_detailed,null);
        viewPager= (ViewPager) view.findViewById(R.id.viewpager);
        //scheduledExecutorService.scheduleAtFixedRate(new ViewPagerTask(),3,3, TimeUnit.SECONDS);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new MyViewPagerAdapter());
        return view;

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

    class ViewPagerTask implements Runnable
    {

        @Override
        public void run() {
            if(start==true)
            {
                currentIndex=(currentIndex+1)%views.size();
                handler.obtainMessage().sendToTarget();;
            }
        }


    }
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            //设置当前页面
            viewPager.setCurrentItem(currentIndex);
        }
    };
    private View createView(String picture)
    {
        ImageView imgView=new ImageView(getContext());
        imgView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getContext()).load(picture).placeholder(R.mipmap.loading_small).into(imgView);
        return imgView;
    }

}
