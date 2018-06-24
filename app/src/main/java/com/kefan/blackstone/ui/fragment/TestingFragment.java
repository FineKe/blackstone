package com.kefan.blackstone.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.database.TestRecord;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.ui.activity.RankActivity;
import com.kefan.blackstone.widget.HeaderBar;
import com.kefan.blackstone.widget.PrecentCricleView;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import butterknife.BindView;


/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/8 上午12:42
 */
public class TestingFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.iv_icon_test_fragment)
    ImageView icon;

    @BindView(R.id.precent_view_correct_rate_testing_fragment)
    PrecentCricleView correctRate;

    @BindView(R.id.precent_view_this_score_testing_fragment)
    PrecentCricleView thisScore;

    @BindView(R.id.precent_view_last_score_testing_fragment)
    PrecentCricleView lastScore;

    @BindView(R.id.tv_start_testing_fragment)
    TextView start;

    @BindView(R.id.tv_rank_testing_fragment)
    TextView rank;

    private UserService userService;


    private ExecuteTestFragment executeTestFragment;

    private HeaderBar headerBar;

    @Override
    public int setLayout() {
        return R.layout.fragment_testing;
    }

    @Override
    protected void initData() {

        userService=new UserServiceImpl();

    }

    @Override
    public void initView() {


        Glide.with(this).load(R.drawable.testing_bg).into(icon);
        headerBar = ((MainActivity) getActivity()).headerBar;

        headerBar.getRightPart().setVisibility(View.GONE);
        headerBar.getCenterTextView().setText("小测试");

        List<TestRecord> testRecords = DataSupport.where("userId=?",String.valueOf(userService.getUser().getId())).order("time").find(TestRecord.class);

        //设置得分
        if (testRecords.size() >0) {
            int length = testRecords.size();
            //设置本次得分
            thisScore.setText(testRecords.get(length-1).getScore()+"");

            //设置历史得分
            if (length > 1) {
                lastScore.setText(testRecords.get(length-2).getScore()+"");
            }
        }

        float ratio = ((MainActivity) getActivity()).ratio;
        correctRate.setText(ratio+"%");
        correctRate.setSweepAngle(ratio*3.6f);

    }

    @Override
    public void initEvent() {

        start.setOnClickListener(this);
        rank.setOnClickListener(this);

    }

    @Override
    public void onDestroyView() {
        headerBar.getRightPart().setVisibility(View.VISIBLE);
        headerBar.getCenterTextView().setText("");
        super.onDestroyView();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_start_testing_fragment:


                executeTestFragment = new ExecuteTestFragment();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_content_main_activity, executeTestFragment).commit();

                break;
            case R.id.tv_rank_testing_fragment:

                startActivity(new Intent(getContext(), RankActivity.class));

                break;
        }

    }
}
