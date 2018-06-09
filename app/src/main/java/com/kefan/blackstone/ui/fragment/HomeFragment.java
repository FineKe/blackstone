package com.kefan.blackstone.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.widget.HeaderBar;
import com.kefan.blackstone.widget.PrecentCricleView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/7 上午12:01
 */
public class HomeFragment extends BaseFragment {

    private int[] bannerUrl = {R.drawable.banner_b1_home, R.drawable.banner_b2_home,
            R.drawable.banner_b3_home, R.drawable.banner_b4_home};

    @BindView(R.id.banner_home_fragment)
    Banner banner;

    @BindView(R.id.precent_view_correct_rate_home_fragment)
    PrecentCricleView correctRate;

    @BindView(R.id.precent_view_this_score_home_fragment)
    PrecentCricleView thisScore;

    @BindView(R.id.precent_view_last_score_home_fragment)
    PrecentCricleView lastScore;

    private HeaderBar headerBar;

    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {

        //初始化 banner
        List<Integer> images = new ArrayList<>();

        for (int i : bannerUrl) {
            images.add(i);
        }

        banner.setImages(images).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load((Integer) path).into(imageView);
            }
        }).start();

        correctRate.setSweepAngle(0.98F * 360);
        lastScore.setText("100");
        thisScore.setText("67");

        headerBar= ((MainActivity) getActivity()).headerBar;

        headerBar.getCenterTextView().setText("黑石顶生物多样性野外实习");

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onDestroyView() {
        headerBar.getCenterTextView().setText("");
        super.onDestroyView();
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
}
