package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.DrmInitData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MY SHIP on 2017/5/7.
 */

public class LoginedFragment extends Fragment implements View.OnClickListener{

    private ImageView myCollections;
    private ImageView myRecords;
    private TextView name;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.logined_fragment,null);
        myCollections= (ImageView) view.findViewById(R.id.logined_fragment_my_collections);
        myRecords= (ImageView) view.findViewById(R.id.logined_fragment_my_records);

        name= (TextView) view.findViewById(R.id.logined_frgment_textView_name);

        myCollections.setOnClickListener(this);
        myRecords.setOnClickListener(this);
        name.setText(userInformationSharedPreferences.getString("name",""));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        userInformationSharedPreferences=getActivity().getSharedPreferences(userInformation,Context.MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.logined_fragment_my_collections:
                startActivity(new Intent(getContext(),MyCollectionsActivity.class));
                break;

            case R.id.logined_fragment_my_records:
                startActivity(new Intent(getContext(),MyRecordsActivity.class));
                break;
        }
    }
}
