package com.kefan.blackstone.ui.fragment;

import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kefan.blackstone.R;

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


    @Override
    public int setLayout() {
        return R.layout.fragment_execute_test;
    }


}
