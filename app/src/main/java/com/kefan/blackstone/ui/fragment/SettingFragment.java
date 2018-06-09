package com.kefan.blackstone.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.activity.AccountAndSecurityActivity;
import com.kefan.blackstone.ui.activity.CopyRightActivity;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.ui.activity.SuggestionsActivity;
import com.kefan.blackstone.widget.HeaderBar;

/**
 * Created by MY SHIP on 2017/3/18.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    private LinearLayout tab_account;
    private LinearLayout tab_copyright;
    private LinearLayout tab_suggestions;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile = "isLogin";
    private Boolean isLogined;

    private HeaderBar headerBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);
        tab_account = (LinearLayout) view.findViewById(R.id.setting_linearlayout_account_and_security);
        tab_copyright = (LinearLayout) view.findViewById(R.id.setting_linearlayout_copyright);
        tab_suggestions = (LinearLayout) view.findViewById(R.id.setting_linearlayout_suggestions);

        tab_account.setOnClickListener(this);
        tab_copyright.setOnClickListener(this);
        tab_suggestions.setOnClickListener(this);

        headerBar= ((MainActivity) getActivity()).headerBar;
        headerBar.getCenterTextView().setText("设置");
        headerBar.getRightPart().setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        sharedPreferences = getActivity().getSharedPreferences(isLoginedFile, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    @Override
    public void onDestroyView() {

        headerBar.getCenterTextView().setText("");
        headerBar.getRightPart().setVisibility(View.VISIBLE);

        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_linearlayout_account_and_security:
                isLogined = sharedPreferences.getBoolean("islogined", false);
                if (isLogined) {
                    startActivity(new Intent(getContext(), AccountAndSecurityActivity.class));
//                    getActivity().overridePendingTransition(R.anim.in,0);
                } else {
                    Toast.makeText(getContext(), "你还未登录", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.setting_linearlayout_copyright:
                startActivity(new Intent(getContext(), CopyRightActivity.class));
//                getActivity().overridePendingTransition(R.anim.in,R.anim.out);
                break;


            case R.id.setting_linearlayout_suggestions:
                startActivity(new Intent(getContext(), SuggestionsActivity.class));
//                getActivity().overridePendingTransition(R.anim.in,R.anim.out);
                break;
        }
    }
}
