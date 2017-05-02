package com.hdu.myship.blackstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by MY SHIP on 2017/3/18.
 * 个人中心fragment
 */

public class PersonalCenterFragment extends Fragment implements View.OnClickListener{
    private Button login;
    private Button register;
    private ImageView userPicture;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.personal_center,container,false);

        login= (Button) view.findViewById(R.id.person_center_button_login);
        register= (Button) view.findViewById(R.id.person_center_button_register);

        userPicture= (ImageView) view.findViewById(R.id.person_center_imageView_uesrPicture);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        userPicture.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.person_center_button_login:
                break;

            case R.id.person_center_button_register:
                register();
                break;

            case R.id.person_center_imageView_uesrPicture:
                break;
        }
    }

    public void login()
    {

    }


    public void register()
    {
        startActivity(new Intent(getActivity(),RegisterActivity.class));
    }
}
