package com.kefan.blackstone.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kefan.blackstone.BaseActivity;
import com.kefan.blackstone.R;

public class ShowImageActivity extends BaseActivity {
    private Dialog dialog;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_image);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        Intent intent = getIntent();
        int im = intent.getIntExtra("title", 0);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageResource(im);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveImage(imageView);
                return true;
            }
        });
    }

    private void saveImage(final ImageView imageView) {
        TextView savePicture;
        TextView actionCancel;
        WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        Display display;
        display = windowManager.getDefaultDisplay();
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.guide_savepicture, null);
        view.setMinimumWidth(display.getWidth());
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.show();

        savePicture = (TextView) view.findViewById(R.id.dialog_guide_savepicture_text_view_save_picture);
        actionCancel = (TextView) view.findViewById(R.id.dialog_guide_savepicture_text_view_action_cancel);
        savePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
                Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "123", "456");
                Toast.makeText(ShowImageActivity.this, "已保存到系统相册", Toast.LENGTH_SHORT).show();
                imageView.setDrawingCacheEnabled(false);//关闭catch
            }
        });
        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}
