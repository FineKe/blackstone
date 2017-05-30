package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JsonUtil.JsonResolverSpeciesDetailed;
import database.Amphibia;
import database.Bird;
import database.Insect;
import database.Reptiles;
import database.Species;

public class SpeciesClassActivity extends AutoLayoutActivity implements View.OnClickListener{
    private String getSpeciesDetailedURL="http://api.blackstone.ebirdnote.cn/v1/species/";
    private RequestQueue requestQueue;
    private static final String TAG ="SpeciesClassActivity";
    private ImageButton actionBack;
    private ImageButton alertMenu;
    private ImageButton alertPick;

    private TextView speciesClassName;

    private RecyclerView speciesContent;

    private SliderBar sliderBar;

    private Dialog dialogAlertMenu;

    private List<Species> speciesList;

    private String SpeciesType;

    private int position;

    private LinearLayoutManager linearLayoutManager;
    private String typeTitle[]={"两栖类","爬行类","鸟类"};
    private List<String> indexList;

    private UserInformationUtil userInformation;
    private IsLoginUtil isLoginUtil;
    private UpdateToken updateToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
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
        if(position<=2)
        {
            speciesClassName.setText(typeTitle[position]);
        }else
        {
            speciesClassName.setText(getIntent().getStringExtra("speciesClassName"));
        }

        linearLayoutManager=new LinearLayoutManager(this);
        speciesContent.setLayoutManager(linearLayoutManager);
        speciesContent.setHasFixedSize(true);

    }

    private void initData() {
        requestQueue= Volley.newRequestQueue(this);
        position=getIntent().getIntExtra("position",0);
        if(position<=2)
        {
            speciesList=DataSupport.where("speciesType=?",getIntent().getStringExtra("speciesType")).find(Species.class);
        }else
        {
            speciesList=DataSupport.where("chineseName=?",getIntent().getStringExtra("speciesClassName")).find(Species.class);
        }

    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        alertMenu.setOnClickListener(this);
        alertPick.setOnClickListener(this);

        SpeciesContentAdapter adapter=new SpeciesContentAdapter(speciesList,this);
        speciesContent.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
            @Override
            public void onItemClick(View view, Species data) {//添加点击事件
                Log.d(TAG, "onItemClick: "+data.getSingal());
                Intent intent=new Intent(SpeciesClassActivity.this,SpeciesDeatailedActivity.class);
                intent.putExtra("singal",data.getSingal());
                intent.putExtra("speciesType",data.getSpeciesType());
//                loadDeatailed(getApplicationContext(),data.getSpeciesType(),data.getSingal());
                //intent.putExtra("speciesTypeChineseName",typeTitle[position]);
                startActivity(intent);
            }
        });

        sliderBar.setData(getResources().getStringArray(R.array.sorting_by_order));//索引栏设置数据
        sliderBar.setGravity(Gravity.CENTER_VERTICAL);//设置位置
        sliderBar.setCharacterListener(new SliderBar.CharacterClickListener() {
            @Override
            public void clickCharacter(int position) {
                linearLayoutManager.scrollToPositionWithOffset(position,0);
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
                showScreenDialog(this);
                break;
        }
    }

    private void showScreenDialog(Context context) {
        screenDialog dialog=new screenDialog(this,R.style.screen);
        dialog.setContentView(R.layout.screen);
        dialog.show();
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

    public class screenDialog extends Dialog
    {

        public screenDialog(@NonNull Context context, @StyleRes int themeResId) {
            super(context,android.R.style.Theme);
        }
    }
}
