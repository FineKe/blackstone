package com.kefan.blackstone.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kefan.blackstone.R;
import com.kefan.blackstone.widget.HeaderBar;

import butterknife.BindView;

public class RankActivity extends BaseActivity {


    @BindView(R.id.headerbar_rank_activity)
    HeaderBar headerbar;

    @BindView(R.id.recycle_view_rank_activity)
    RecyclerView recycleView;

    @Override
    protected int setLayout() {
        return R.layout.activity_rank;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {

        headerbar.getLeftPart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
