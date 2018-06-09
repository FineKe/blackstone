package com.kefan.blackstone.ui.fragment;

import android.view.View;

import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.widget.HeaderBar;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/9 下午12:52
 */
public class TeamFragment extends BaseFragment{


    private HeaderBar headerBar;

    @Override
    public int setLayout() {
        return R.layout.fragment_team;
    }

    @Override
    public void initView() {
        super.initView();

        headerBar= ((MainActivity) getActivity()).headerBar;
        headerBar.getCenterTextView().setText("制作团队");
        headerBar.getRightPart().setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        headerBar.getCenterTextView().setText("");
        headerBar.getRightPart().setVisibility(View.VISIBLE);
        super.onDestroyView();
    }
}
