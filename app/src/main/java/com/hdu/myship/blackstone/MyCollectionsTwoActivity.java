package com.hdu.myship.blackstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import database.Species;

public class MyCollectionsTwoActivity extends AppCompatActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_collections_two);
        initVies();
    }

    private void initVies() {
        position=getIntent().getIntExtra("position",0);//取出position
        List<Species> speciesList=MyCollectionsActivity.list.get(position).getList();//这里从上一个activity中取list
        System.out.println(speciesList.get(0).getSingal()+":"+speciesList.get(0).getChineseName()+":"+speciesList.get(0).getLatinName());//示列 物种的id，名字，拉丁文名字，通过get方法，可获取所有信息
    }
}
