package com.kefan.blackstone.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.widget.HeaderBar;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/8 上午10:35
 */
public class ExecuteTestFragment extends BaseFragment {


    @BindView(R.id.iv_icon_execute_test_fragment)
    ImageView icon;

    @BindView(R.id.rg_anser_group_execute_test_fragment)
    RadioGroup answerGroup;

    @BindView(R.id.rb_anser_a_execute_test_fragment)
    RadioButton answerA;
    @BindView(R.id.rb_anser_b_execute_test_fragment)
    RadioButton answerB;
    @BindView(R.id.rb_anser_c_execute_test_fragment)
    RadioButton answerC;
    @BindView(R.id.rb_anser_d_execute_test_fragment)
    RadioButton answerD;

    @BindView(R.id.tv_answer_result_execute_test_fragment)
    TextView result;

    @BindView(R.id.ll_answer_result_and_next_execute_test_fragment)
    RelativeLayout next;


    private HeaderBar headerBar;

    private TextView title;

    private ImageView heartOne;

    private ImageView heartTwo;

    private ImageView heartThree;

    private TextView rightText;

    @Override
    public void initView() {

        Logger.i(">>");

        //标题栏设置
        headerBar = headerBar==null? ((MainActivity) getActivity()).headerBar:headerBar;
        title = title==null?headerBar.getCenterTextView():title;
        heartOne = heartOne==null?new ImageView(getContext()):heartOne;
        heartTwo = heartTwo==null?new ImageView(getContext()):heartTwo;
        heartThree = heartThree==null?new ImageView(getContext()):heartThree;

        rightText = rightText==null?headerBar.getRightTextView():rightText;

        heartOne.setImageDrawable(getResources().getDrawable(R.mipmap.heart_pressed));
        heartTwo.setImageDrawable(getResources().getDrawable(R.mipmap.heart_pressed));
        heartThree.setImageDrawable(getResources().getDrawable(R.mipmap.heart_pressed));



        headerBar.getCenterPart().removeView(title);

        headerBar.getRightTextView().setVisibility(View.VISIBLE);

        headerBar.getCenterPart().addView(heartOne);
        headerBar.getCenterPart().addView(heartTwo);
        headerBar.getCenterPart().addView(heartThree);

        //设置间距
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) heartTwo.getLayoutParams();
        layoutParams.setMargins(32,0,32,0);

        rightText.setText("0");
        rightText.setTextSize(18);

        headerBar.getRightImageView().setVisibility(View.GONE);

        Glide.with(this).load(R.drawable.testing_bg).into(icon);

    }

    @Override
    public int setLayout() {
        return R.layout.fragment_execute_test;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Logger.i(">>");

        //恢复标题栏
        headerBar.getCenterPart().removeView(heartOne);
        headerBar.getCenterPart().removeView(heartTwo);
        headerBar.getCenterPart().removeView(heartThree);
        headerBar.getCenterPart().addView(title);

        headerBar.getRightTextView().setText("");
        headerBar.getRightTextView().setVisibility(View.GONE);
        headerBar.getRightImageView().setVisibility(View.VISIBLE);
    }
}
