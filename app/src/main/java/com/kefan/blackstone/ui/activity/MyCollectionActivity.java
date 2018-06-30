package com.kefan.blackstone.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.kefan.blackstone.R;
import com.kefan.blackstone.widget.HeaderBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCollectionActivity extends BaseActivity {


    @BindView(R.id.headerbar_my_collection_activity)
    HeaderBar headerbar;
    @BindView(R.id.recycle_view_my_collection_activity)
    RecyclerView recycleView;


    @Override
    protected int setLayout() {
        return R.layout.activity_my_collection;
    }




    @Override
    public void initView() {

    }
}
