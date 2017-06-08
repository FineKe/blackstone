package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
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
                    result.setHead(speciesList.get(i).getOrder());
                    result.setViewType(HEAD);
                    resultList.add(result);
                }

                if (!speciesList.get(i).getOrder().equals(speciesList.get(i + 1).getOrder())) {
                    indexList.add(speciesList.get(i + 1).getOrder().substring(0, 1));
                    positionList.add(j);
                    Result result = new Result();
                    result.setHead(speciesList.get(i + 1).getOrder());
                    result.setViewType(HEAD);

                    System.out.println(speciesList.get(i).getOrder());
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

    class Result
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
}
