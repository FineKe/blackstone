package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class ResetPhoneActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView phone;
    private BootstrapButton update;

    private ImageButton actionBack;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";

    private String phoneNumber;
    private String phoneCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_phone);

        initData();
        initView();
        initEvents();
    }

    private void initData() {
        userInformationSharedPreferences=getSharedPreferences(userInformation,MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();

        phoneNumber=userInformationSharedPreferences.getString("mobile","");
        phoneCode=phoneNumber.substring(0,3)+"****"+phoneNumber.substring(7);
    }

    private void initView() {
        phone= (TextView) findViewById(R.id.activity_reset_phone_text_view_phone);

        update= (BootstrapButton) findViewById(R.id.activity_reset_phone_boot_strap_button_update_phone_number);

        actionBack= (ImageButton) findViewById(R.id.activity_reset_phone_image_button_action_back);

        phone.setText(phoneCode);
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_reset_phone_image_button_action_back:
                actionBack();
                break;

            case R.id.activity_reset_phone_boot_strap_button_update_phone_number:
                update();
                break;
        }
    }

    private void actionBack() {
        this.finish();
        overridePendingTransition(R.anim.in,R.anim.out);
    }

    private void update() {
        showUpdateDialog();
    }

    private void showUpdateDialog() {
        final updatePhoneNumberDialog updatePhoneNumberDialog=new updatePhoneNumberDialog(this,R.style.LoginDialog,R.layout.update_phone_number_dialog);
        updatePhoneNumberDialog.setCancelable(false);
        updatePhoneNumberDialog.show();

        ImageView actionCancel= (ImageView) updatePhoneNumberDialog.findViewById(R.id.update_phone_number_dialog_imageView_actionCancel);
        EditText inputPhone= (EditText) updatePhoneNumberDialog.findViewById(R.id.update_phone_numbe_dialog_editText_account);
        EditText inputCode= (EditText) updatePhoneNumberDialog.findViewById(R.id.update_phone_number_dialog_editText_code);
        TextView getCode= (TextView) updatePhoneNumberDialog.findViewById(R.id.update_phone_number_text_view_get_code);
        TextView Ok= (TextView) updatePhoneNumberDialog.findViewById(R.id.update_phone_number_dialog_textView_Ok);

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoneNumberDialog.dismiss();
            }
        });

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class updatePhoneNumberDialog extends Dialog {
        private Context context;
        private int resId;
        public updatePhoneNumberDialog(Context context, int resLayout) {
            this(context,0,0);
        }
        public updatePhoneNumberDialog(Context context, int themeResId, int resLayout) {
            super(context, themeResId);
            this.context = context;
            this.resId = resLayout;
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(resId);
        }
    }
}
