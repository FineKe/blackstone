package com.kefan.blackstone.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/5 下午10:46
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayout(), null, false);
        ButterKnife.bind(this, view);

        initView();
        initEvent();

        return view;
    }

    public abstract int setLayout();

    public void initView() {

    }

    public void initEvent() {

    }
}
