package com.kefan.blackstone.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.activity.GuideTabOneActivity;
import com.kefan.blackstone.ui.activity.GuideTableFourActivity;
import com.kefan.blackstone.ui.activity.GuideTableThreeActivity;
import com.kefan.blackstone.ui.activity.GuideTableTwoActivity;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.ui.fragment.BaseFragment;
import com.kefan.blackstone.widget.HeaderBar;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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


    private HeaderBar headerBar;

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

        headerBar= ((MainActivity) getActivity()).headerBar;

        headerBar.getCenterTextView().setText("指南");
        headerBar.getRightPart().setVisibility(View.GONE);

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
    public void onDestroyView() {
        headerBar.getCenterTextView().setText("");
        headerBar.getRightPart().setVisibility(View.VISIBLE);
        super.onDestroyView();
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
