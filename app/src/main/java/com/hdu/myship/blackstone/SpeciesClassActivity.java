package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import database.Species;

public class SpeciesClassActivity extends AutoLayoutActivity implements View.OnClickListener{
    private ImageButton actionBack;
    private ImageButton alertMenu;
    private ImageButton alertPick;

    private TextView speciesClassName;

    private RecyclerView speciesContent;

    private SliderBar sliderBar;

    private Dialog dialogAlertMenu;

    private List<Species> species;

    private String SpeciesType;

    private int position;

    private String typeTitle[]={"鸟类","两栖类","爬行类","昆虫类"};
    private List<String> indexList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        SpeciesType=getIntent().getStringExtra("SpeciesType");
        position=getIntent().getIntExtra("position",0);
        setContentView(R.layout.activity_species_class);

        initData();
        initView();
        initEvents();
    }

    private void initView() {
        actionBack= (ImageButton) findViewById(R.id.species_class_imageButton_actionBack);
        alertMenu= (ImageButton) findViewById(R.id.species_class_imageButton_alert_menu);
        alertPick= (ImageButton) findViewById(R.id.species_class_imageButton_alert_pick);

        speciesClassName= (TextView) findViewById(R.id.species_class_textView_speciesClass_name);

        speciesContent= (RecyclerView) findViewById(R.id.species_class_recyclerView_speciesContent);

        sliderBar= (SliderBar) findViewById(R.id.species_class_sliderBar);

        speciesClassName.setText(typeTitle[position]);
        speciesContent.setLayoutManager(new LinearLayoutManager(this));
        speciesContent.setHasFixedSize(true);

    }

    private void initData() {
        species=new ArrayList<>();
        species=DataSupport.where("speciesType=?",SpeciesType).find(Species.class);
        indexList=new ArrayList<>();
        for(String s:getResources().getStringArray(R.array.sorting_by_order))
        {
            indexList.add(s);
        }

    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        alertMenu.setOnClickListener(this);
        alertPick.setOnClickListener(this);

        SpeciesContentAdapter adapter=new SpeciesContentAdapter(species,indexList,this);
        speciesContent.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
            @Override
            public void onItemClick(View view, SpeciesContentAdapter.result data) {//添加点击事件
                Intent intent=new Intent(SpeciesClassActivity.this,SpeciesDeatailedActivity.class);
                intent.putExtra("singal",data.getSpecies().getSingal());
                intent.putExtra("speciesType",data.getSpecies().getSpeciesType());
                intent.putExtra("speciesTypeChineseName",typeTitle[position]);
                startActivity(intent);
            }
        });

        sliderBar.setData(getResources().getStringArray(R.array.sorting_by_order));//索引栏设置数据
        sliderBar.setGravity(Gravity.CENTER_VERTICAL);//设置位置
        sliderBar.setCharacterListener(new SliderBar.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {//添加监听事件
                Toast.makeText(SpeciesClassActivity.this,"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.species_class_imageButton_actionBack:
                back();
                break;

            case R.id.species_class_imageButton_alert_menu:
                showAlertMenu(this);
                break;

            case R.id.species_class_imageButton_alert_pick:
                break;
        }
    }

    public void back()
    {
        this.finish();
    }

    private void showAlertMenu(final Context context)
    {
        dialogAlertMenu=new Dialog(context);
        View  alertMenuView= LayoutInflater.from(context).inflate(R.layout.alert_menu,null);
        Button button1= (Button) alertMenuView.findViewById(R.id.browse_by_section);
        Button button2= (Button) alertMenuView.findViewById(R.id.browse_by_order);

        dialogAlertMenu.setContentView(alertMenuView);
        Window window=dialogAlertMenu.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.x=0;
        layoutParams.y=0;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.alpha=9f;
        window.setAttributes(layoutParams);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"按目浏览",Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"按科浏览",Toast.LENGTH_SHORT).show();
            }
        });
        dialogAlertMenu.show();
    }
}
