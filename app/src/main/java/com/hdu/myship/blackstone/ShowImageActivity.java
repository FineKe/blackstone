package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_image);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        Intent intent=getIntent();
        int im=intent.getIntExtra("title",0);
        final ImageView imageView= (ImageView) findViewById(R.id.image);
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
    private void Dialog(Context context){
        final String[] x=new String[]{"保存","取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(x, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(ShowImageActivity.this,x[which],Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();

    }
    private void saveImage(ImageView imageView){
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"123","456");
        Toast.makeText(ShowImageActivity.this, "图片已保存到"+getContentResolver().toString(), Toast.LENGTH_SHORT).show();
        imageView.setDrawingCacheEnabled(false);//关闭catch
    }


}
