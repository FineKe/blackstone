package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private int HEAD=0;
    private int ITEM=1;
    private String getSpeciesDetailedURL="http://api.blackstone.ebirdnote.cn/v1/species/";
    private RequestQueue requestQueue;
    private static final String TAG ="SpeciesClassActivity";
    private ImageButton actionBack;
    private ImageButton alertMenu;
    private ImageButton alertPick;

    private SpeciesContentAdapter adapter;
    private TextView speciesClassName;

    private RecyclerView speciesContent;

    private SliderBar sliderBar;

    private Dialog dialogAlertMenu;

    private View viewInclude;
    private List<Species> speciesList;
    private List<Integer> positionList;
    private List<Result> resultList;
    private String SpeciesType;

    private int position;

    private LinearLayoutManager linearLayoutManager;
    private String typeTitle[]={"两栖类","爬行类","鸟类"};
    private List<String> indexList;

    private UserInformationUtil userInformation;
    private IsLoginUtil isLoginUtil;
    private UpdateToken updateToken;


    private SharedPreferences sortingSharedPreferences;
    private SharedPreferences.Editor sortingEditor;
    private String sortingValueFile="FamilyOROrder";
    private boolean sortingByOrder=true;

    private int w;
    private int h;
    private int height;
    private int width;
    private int statusBarHeight;
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

        viewInclude=findViewById(R.id.species_class_view_include_head);
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

//        w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        viewInclude.measure(w,h);
//
//        width=viewInclude.getMeasuredWidth();
//        height=viewInclude.getMeasuredHeight();

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android"); //状态栏高度
        if (resourceId > 0) {
            statusBarHeight= getResources().getDimensionPixelSize(resourceId);
        }
        height=statusBarHeight;
        width= WindowManager.LayoutParams.MATCH_PARENT;
    }

    private void initData() {
        sortingSharedPreferences=getSharedPreferences(sortingValueFile,MODE_PRIVATE);
        sortingEditor=sortingSharedPreferences.edit();
        sortingByOrder=sortingSharedPreferences.getBoolean("sortingWay",true);
        requestQueue= Volley.newRequestQueue(this);
        indexList=new ArrayList<>();
        positionList=new ArrayList<>();
        resultList=new ArrayList<>();
        position=getIntent().getIntExtra("position",0);
        if(position<=2)
        {
            speciesList=DataSupport.where("speciesType=?",getIntent().getStringExtra("speciesType")).find(Species.class);
            createIndexList(speciesList);

        }else
        {
            speciesList=DataSupport.where("chineseName=?",getIntent().getStringExtra("speciesClassName")).find(Species.class);
            for (int i = 0, j = 0; i < speciesList.size(); i++, j++) {
                indexList.add(speciesList.get(i).getOrder().substring(0,1));
                positionList.add(i);
                Result result=new Result();
                result.setHead(speciesList.get(i).getOrder());
                result.setViewType(HEAD);
                result.setLatinHead(speciesList.get(i).getLatinOrder());
                resultList.add(result);

                Result result1=new Result();
                result1.setViewType(ITEM);
                result1.setSpecies(speciesList.get(i));
                resultList.add(result1);
            }
        }

    }

    private void createIndexList(List<Species> speciesList) {
        if (sortingByOrder) {

            for (int i = 0, j = 0; i < speciesList.size() - 1; i++, j++) {
                if (i == 0) {
                    indexList.add(speciesList.get(0).getOrder().substring(0, 1));
                    positionList.add(0);
                    Result result = new Result();
                    result.setHead(speciesList.get(0).getOrder());
                    result.setViewType(HEAD);
                    result.setLatinHead(speciesList.get(0).getLatinOrder());
                    resultList.add(result);
                }

                if (!speciesList.get(i).getOrder().equals(speciesList.get(i + 1).getOrder())) {
                    indexList.add(speciesList.get(i + 1).getOrder().substring(0, 1));
                    positionList.add(j);
                    Result result = new Result();
                    result.setHead(speciesList.get(i + 1).getOrder());
                    result.setViewType(HEAD);
                    result.setLatinHead(speciesList.get(i+1).getLatinOrder());
                    System.out.println(speciesList.get(i+1).getOrder());
                    Result result_ = new Result();
                    result_.setViewType(ITEM);
                    result_.setSpecies(speciesList.get(i));
                    resultList.add(result_);
                    resultList.add(result);
                    j++;
                } else {

                    Result result = new Result();
                    result.setViewType(ITEM);
                    result.setSpecies(speciesList.get(i));
                    resultList.add(result);
                }

            }
        } else {//按科

            for (int i = 0; i < speciesList.size() - 1; i++) {
                if (i == 0) {
                    indexList.add(speciesList.get(0).getFamily().substring(0, 1));
                }
                if (!speciesList.get(i).getFamily().equals(speciesList.get(i + 1).getFamily())) {
                    indexList.add(speciesList.get(i + 1).getFamily().substring(0, 1));
                    System.out.println(speciesList.get(i).getFamily()+":"+speciesList.get(i+1).getFamily());

                }else
                {

                }
            }
            for(String s:indexList)
            {
                System.out.println(s);
            }
            int j = 0;
            for (String s : indexList) {
                int k = 0;
                for (int i = 0; i < speciesList.size(); i++) {
                    if (s.equals(speciesList.get(i).getFamily().substring(0, 1))) {
                        if (k == 0) {
                            Result result = new Result();
                            result.setViewType(HEAD);
                            result.setLatinHead(speciesList.get(i).getLatinFamily());
                            result.setHead(speciesList.get(i).getFamily());
                            resultList.add(result);
                            positionList.add(j);
                            j++;
                        }
                        Result result = new Result();
                        result.setViewType(ITEM);
                        result.setSpecies(speciesList.get(i));
                        resultList.add(result);
                        j++;
                        k++;
                    }
                }
            }
        }
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        alertMenu.setOnClickListener(this);
        alertPick.setOnClickListener(this);

        adapter=new SpeciesContentAdapter(this,resultList);
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

        sliderBar.setData(indexList,positionList);//索引栏设置数据
        sliderBar.setGravity(Gravity.CENTER_VERTICAL);//设置位置
//        sliderBar.setCharacterListener(new SliderBar.CharacterClickListener() {
//            @Override
//            public void clickCharacter(int position) {
//                linearLayoutManager.scrollToPositionWithOffset(position,0);
//            }
//        });
        sliderBar.setCharacterListener(new SliderBar.CharacterClickListener() {
            @Override
            public void clickCharacter(View view) {
                linearLayoutManager.scrollToPositionWithOffset((Integer) view.getTag(),0);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
//                Toast.makeText(SpeciesClassActivity.this, "sssss", Toast.LENGTH_SHORT).show();
//                showBirdPick();
                mydialog_bird dialog=new mydialog_bird(this,R.style.customDialog,width,height);
                dialog.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
    }

    public void back()
    {
        this.finish();
    }

    private void showAlertMenu(final Context context)
    {
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display;
        display = windowManager.getDefaultDisplay();
        final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        View  alertMenuView= LayoutInflater.from(context).inflate(R.layout.alert_menu,null);
        TextView button2= (TextView) alertMenuView.findViewById(R.id.browse_by_section);
        TextView button1= (TextView) alertMenuView.findViewById(R.id.browse_by_order);

        alertMenuView.setMinimumWidth(display.getWidth());
        dialog.setContentView(alertMenuView);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.show();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(sortingByOrder!=true)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sortingByOrder=true;
                            sortingEditor.putBoolean("sortingWay",true).apply();
                            if(position<=2)
                            {
                                speciesList=DataSupport.where("speciesType=?",getIntent().getStringExtra("speciesType")).find(Species.class);
                                resultList.clear();
                                positionList.clear();
                                indexList.clear();
                                createIndexList(speciesList);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                        sliderBar.setData(indexList,positionList);
                                    }
                                });
                            }
                        }
                    }).start();

                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(sortingByOrder!=false)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sortingByOrder=false;
                            sortingEditor.putBoolean("sortingWay",false).apply();
                            if(position<=2)
                            {
                                speciesList=DataSupport.where("speciesType=?",getIntent().getStringExtra("speciesType")).find(Species.class);
                                resultList.clear();
                                positionList.clear();
                                indexList.clear();
                                createIndexList(speciesList);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                        sliderBar.setData(indexList,positionList);
                                    }
                                });
                            }else
                            {
                                speciesList=DataSupport.where("chineseName=?",getIntent().getStringExtra("speciesClassName")).find(Species.class);
                            }
                        }
                    }).start();

                }

            }
        });

    }

    static class Result
    {
        int viewType;
        String head;
        String latinHead;
        Species species;

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getLatinHead() {
            return latinHead;
        }

        public void setLatinHead(String latinHead) {
            this.latinHead = latinHead;
        }

        public Species getSpecies() {
            return species;
        }

        public void setSpecies(Species species) {
            this.species = species;
        }
    }

    public class mydialog_bird extends Dialog {
        boolean flag1=true,flag2=true,flag3=true,flag4=true,flag5=true;
        private Context context;
        private  int width,height;
        public mydialog_bird(@NonNull Context context, @StyleRes int themeResId,int width,int height) {
            super(context, themeResId);
            this.context=context;
            this.width=width;
            this.height=height;

        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            init();
        }
        public void show(){
            super.show();
            Window dialogWindow = getWindow();
            WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            dialogWindow.setGravity(Gravity.BOTTOM);//设置对话框位置
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            params.alpha=0.8f;
            params.width= metric.widthPixels;
            params.height=metric.heightPixels-height;
            dialogWindow.setAttributes(params);
        }
        private void init(){
            View v=getLayoutInflater().inflate(R.layout.dialog_pick_bird,null);
            setContentView(v);
            TextView t1= (TextView)v. findViewById(R.id.t1);
            t1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    LinearLayout l1= (LinearLayout)findViewById(R.id.l1);
                    if(flag1==true) {
                        l1.setVisibility(View.VISIBLE);
                        flag1=false;
                    }
                    else if (flag1==false){
                        l1.setVisibility(View.GONE);
                        flag1=true;
                    }
                }
            });
            TextView t2= (TextView)v. findViewById(R.id.t2);
            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l2= (LinearLayout)findViewById(R.id.l2);
                    if(flag2==true) {
                        l2.setVisibility(View.VISIBLE);
                        flag2=false;
                    }
                    else if (flag2==false){
                        l2.setVisibility(View.GONE);
                        flag2=true;
                    }
                }
            });
            TextView t3= (TextView)v. findViewById(R.id.t3);
            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l3= (LinearLayout)findViewById(R.id.l3);
                    if(flag3==true) {
                        l3.setVisibility(View.VISIBLE);
                        flag3=false;
                    }
                    else if (flag3==false){
                        l3.setVisibility(View.GONE);
                        flag3=true;
                    }
                }
            });
            TextView t4= (TextView)v. findViewById(R.id.t4);
            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l4= (LinearLayout)findViewById(R.id.l4);
                    if(flag4==true) {
                        l4.setVisibility(View.VISIBLE);
                        flag4=false;
                    }
                    else if (flag4==false){
                        l4.setVisibility(View.GONE);
                        flag4=true;
                    }
                }
            });
        }
    }

    public void showBirdPick()
    {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display;
        display = windowManager.getDefaultDisplay();
        Dialog dialog = new Dialog(this,R.style.customDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pick_bird, null);
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
    }
}
