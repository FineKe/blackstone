package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ui.fragment.BaseFragment;
import widget.HeaderBar;

/**
 * Created by MY SHIP on 2017/3/18.
 * 指南fragment
 */

public class GuideFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.banner_guide_activity)
    Banner banner;

    @BindView(R.id.guideFragment_linearlayout_tab1)
    LinearLayout tabone;

    @BindView(R.id.guideFragment_linearlayout_tab2)
    LinearLayout tabtwo;

    @BindView(R.id.guideFragment_linearlayout_tab3)
    LinearLayout tabthree;

    @BindView(R.id.guideFragment_linearlayout_tab4)
    LinearLayout tabfour;



    @Override
    public int setLayout() {
        return R.layout.guide;
    }

    @Override
    public void initView() {

        List<Integer> images=new ArrayList<>(5);
        images.add(R.drawable.banner_b1_guide_activity);
        images.add(R.drawable.banner_b2_guide_activity);
        images.add(R.drawable.banner_b3_guide_activity);
        images.add(R.drawable.banner_b4_guide_activity);
        images.add(R.drawable.banner_b5_guide_activity);

        banner.setImages(images).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load((Integer)path).into(imageView);
            }
        }).start();
    }

    @Override
    public void initEvent() {
        tabone.setOnClickListener(this);
        tabtwo.setOnClickListener(this);
        tabthree.setOnClickListener(this);
        tabfour.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.guideFragment_linearlayout_tab1:
                startActivity(new Intent(getContext(),GuideTabOneActivity.class));
                break;

            case R.id.guideFragment_linearlayout_tab2:
                startActivity(new Intent(getContext(),GuideTableTwoActivity.class));
                break;

            case R.id.guideFragment_linearlayout_tab3:
                startActivity(new Intent(getContext(),GuideTableThreeActivity.class));
                break;

            case R.id.guideFragment_linearlayout_tab4:
                startActivity(new Intent(getContext(),GuideTableFourActivity.class));
                break;
        }
    }
}
