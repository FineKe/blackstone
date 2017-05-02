package com.hdu.myship.blackstone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by MY SHIP on 2017/3/18.
 * 指南fragment
 */

public class GuideFragment extends Fragment implements View.OnClickListener{
    private LinearLayout tab1;
    private LinearLayout tab2;
    private LinearLayout tab3;
    private LinearLayout tab4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.guide,container,false);

        tab1= (LinearLayout) view.findViewById(R.id.guideFragment_linearlayout_tab1);
        tab2= (LinearLayout) view.findViewById(R.id.guideFragment_linearlayout_tab2);
        tab3= (LinearLayout) view.findViewById(R.id.guideFragment_linearlayout_tab3);
        tab4= (LinearLayout) view.findViewById(R.id.guideFragment_linearlayout_tab4);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.guideFragment_linearlayout_tab1:
                Toast.makeText(getContext(), "tab1", Toast.LENGTH_SHORT).show();
                break;

            case R.id.guideFragment_linearlayout_tab2:
                Toast.makeText(getContext(), "tab2", Toast.LENGTH_SHORT).show();
                break;

            case R.id.guideFragment_linearlayout_tab3:
                Toast.makeText(getContext(), "tab3", Toast.LENGTH_SHORT).show();
                break;

            case R.id.guideFragment_linearlayout_tab4:
                Toast.makeText(getContext(), "tab4", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
