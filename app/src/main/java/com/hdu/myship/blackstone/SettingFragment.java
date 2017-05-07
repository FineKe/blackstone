package com.hdu.myship.blackstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by MY SHIP on 2017/3/18.
 */

public class SettingFragment extends Fragment implements View.OnClickListener{
    private LinearLayout tab_account;
    private LinearLayout tab_copyright;
    private LinearLayout tab_team;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.setting,container,false);
        tab_account= (LinearLayout) view.findViewById(R.id.setting_linearlayout_account_and_security);
        tab_copyright= (LinearLayout) view.findViewById(R.id.setting_linearlayout_copyright);
        tab_team= (LinearLayout) view.findViewById(R.id.setting_linearlayout_developing_team);

        tab_account.setOnClickListener(this);
        tab_copyright.setOnClickListener(this);
        tab_team.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.setting_linearlayout_account_and_security:
                startActivity(new Intent(getContext(),AccountAndSecurityActivity.class));
                break;

            case R.id.setting_linearlayout_copyright:
                startActivity(new Intent(getContext(),CopyRightActivity.class));
                break;

            case R.id.setting_linearlayout_developing_team:
                startActivity(new Intent(getContext(),MakeTeamActivity.class));
                break;
        }
    }
}
