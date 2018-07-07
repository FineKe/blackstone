package com.kefan.blackstone.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.database.Species;
import com.kefan.blackstone.widget.SliderBar;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeciesClassActivity extends AutoLayoutActivity implements View.OnClickListener {
    private int HEAD = 0;
    private int ITEM = 1;
    private String getSpeciesDetailedURL = APIManager.BASE_URL + "v1/species/";
    private RequestQueue requestQueue;
    private static final String TAG = "SpeciesClassActivity";
    private LinearLayout actionBack;
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
    private String typeTitle[] = {"两栖类", "爬行类", "鸟类", "昆虫","兽类"};
    private List<String> indexList;

    private SharedPreferences sortingSharedPreferences;
    private SharedPreferences.Editor sortingEditor;
    private String sortingValueFile = "FamilyOROrder";
    private boolean sortingByOrder = true;
    private boolean sourceFromPick = false;
    private int w;
    private int h;
    private int height;
    private int width;
    private int statusBarHeight;
    private boolean titleBarIsHide = false;

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
        actionBack = (LinearLayout) findViewById(R.id.species_class_title_bar_linear_layout_actionBack);
        alertMenu = (ImageButton) findViewById(R.id.species_class_imageButton_alert_menu);
        alertPick = (ImageButton) findViewById(R.id.species_class_imageButton_alert_pick);

        speciesClassName = (TextView) findViewById(R.id.species_class_title_bar_species_class_name);

        speciesContent = (RecyclerView) findViewById(R.id.species_class_recyclerView_speciesContent);

        sliderBar = (SliderBar) findViewById(R.id.species_class_sliderBar);

        viewInclude = findViewById(R.id.species_class_view_include_head);

        speciesClassName.setText(typeTitle[position]);
        linearLayoutManager = new LinearLayoutManager(this);
        speciesContent.setLayoutManager(linearLayoutManager);
        speciesContent.setHasFixedSize(true);


        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android"); //状态栏高度
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }




        if (position==3) {
            alertMenu.setClickable(false);

        }

        height = statusBarHeight;
        width = WindowManager.LayoutParams.MATCH_PARENT;
    }

    private void initData() {
        sortingSharedPreferences = getSharedPreferences(sortingValueFile, MODE_PRIVATE);
        sortingEditor = sortingSharedPreferences.edit();
        sortingByOrder = sortingSharedPreferences.getBoolean("sortingWay", true);
        requestQueue = Volley.newRequestQueue(this);
        indexList = new ArrayList<>();
        positionList = new ArrayList<>();
        resultList = new ArrayList<>();

        position = getIntent().getIntExtra("position", 0);
        speciesList = DataSupport.where("speciesType=?", getIntent().getStringExtra("speciesType")).find(Species.class);

        if (position == 3) {

            insectCreateIndexList();
            return;
        }

        createIndexList(speciesList);
    }



    private void insectCreateIndexList() {

        for (int i = 0; i < speciesList.size(); i++) {
            indexList.add(speciesList.get(i).getOrder().substring(0, 1));
            positionList.add(i*2);
            Result head = new Result();
            head.setHead(speciesList.get(i).getOrder());
            head.setViewType(HEAD);
            head.setLatinHead(speciesList.get(i).getLatinOrder());

            Result item=new Result();
            item.setViewType(ITEM);
            item.setSpecies(speciesList.get(i));
            resultList.add(head);
            resultList.add(item);
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
                    result.setLatinHead(speciesList.get(i + 1).getLatinOrder());
                    System.out.println(speciesList.get(i + 1).getOrder());
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

            for (Result result : resultList) {

                System.out.println(result.toString());

            }
        } else {//按科

            for (int i = 0; i < speciesList.size() - 1; i++) {
                if (i == 0) {
                    indexList.add(speciesList.get(0).getFamily().substring(0, 1));
                }
                if (!speciesList.get(i).getFamily().equals(speciesList.get(i + 1).getFamily())) {
                    indexList.add(speciesList.get(i + 1).getFamily().substring(0, 1));
                    System.out.println(speciesList.get(i).getFamily() + ":" + speciesList.get(i + 1).getFamily());

                } else {

                }
            }
            for (String s : indexList) {
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

    private void createIndexListc(List<Species> speciesList) {
        if (sortingByOrder) {//按目
//            if(speciesList.size()>1) {
            for (int i = 0; i < speciesList.size() - 1; i++) {
                if (i == 0) {
                    indexList.add(speciesList.get(0).getOrder().substring(0, 1));
                }
                if (!speciesList.get(i).getOrder().equals(speciesList.get(i + 1).getOrder())) {
                    indexList.add(speciesList.get(i + 1).getOrder().substring(0, 1));
                    System.out.println(speciesList.get(i).getOrder() + ":" + speciesList.get(i + 1).getOrder());

                } else {

                }
            }
            for (String s : indexList) {
                System.out.println(s);
            }
            int j = 0;
            for (String s : indexList) {
                int k = 0;
                for (int i = 0; i < speciesList.size(); i++) {
                    if (s.equals(speciesList.get(i).getOrder().substring(0, 1))) {
                        if (k == 0) {
                            Result result = new Result();
                            result.setViewType(HEAD);
                            result.setLatinHead(speciesList.get(i).getLatinOrder());
                            result.setHead(speciesList.get(i).getOrder());
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
        } else {//按科
            for (int i = 0; i < speciesList.size() - 1; i++) {
                if (i == 0) {
                    indexList.add(speciesList.get(0).getFamily().substring(0, 1));
                }
                if (!speciesList.get(i).getFamily().equals(speciesList.get(i + 1).getFamily())) {
                    indexList.add(speciesList.get(i + 1).getFamily().substring(0, 1));
                    System.out.println(speciesList.get(i).getFamily() + ":" + speciesList.get(i + 1).getFamily());

                } else {

                }
            }
            for (String s : indexList) {
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
                            result.setHead(speciesList.get(i).getChineseAbbr());
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


    private void sortByAlpha() {
        for (int i = 0; i < speciesList.size() - 1; i++) {
            if (i == 0) {
                indexList.add(speciesList.get(0).getChineseAbbr().substring(0, 1));
            }

            for (int i1 = 0; i1 < indexList.size(); i1++) {

                if (speciesList.get(i + 1).getChineseAbbr().substring(0, 1).equals(indexList.get(i1))) {
                    break;
                }

                if (i1 == indexList.size()-1) {
                    indexList.add(speciesList.get(i+1).getChineseAbbr().substring(0,1));
                }

            }


        }

        sortAlpha(indexList);

        int j = 0;
        int m=0;
        for (String s : indexList) {
            int k = 0;
            for (int i = 0; i < speciesList.size(); i++) {
                if (s.equals(speciesList.get(i).getChineseAbbr().substring(0, 1))) {
                    if (k == 0) {
                        Result result = new Result();
                        result.setViewType(HEAD);
//                        result.setLatinHead(speciesList.get(i).getLatinFamily());
                        result.setHead(indexList.get(m).toUpperCase());
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
            m++;
        }

        for (int i = 0; i < indexList.size(); i++) {

            indexList.set(i,indexList.get(i).toUpperCase());

        }
    }


    /**
     * 对字母排序
     */
    private void sortAlpha(List<String> list) {

        for (int i = 0; i < list.size(); i++) {

            for (int j=0;j<list.size()-i-1;j++) {

                if (list.get(j).compareTo(list.get(j + 1)) >0) {
                    String temp = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,temp);
                }

            }

        }


    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        alertMenu.setOnClickListener(this);
        alertPick.setOnClickListener(this);

        adapter = new SpeciesContentAdapter(this, resultList);
        speciesContent.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
            @Override
            public void onItemClick(View view, Species data) {//添加点击事件
                Log.d(TAG, "onItemClick: " + data.getSingal());
                Intent intent = new Intent(SpeciesClassActivity.this, SpeciesDeatailedActivity.class);
                intent.putExtra("singal", data.getSingal());
                intent.putExtra("speciesType", data.getSpeciesType());
//                loadDeatailed(getApplicationContext(),data.getSpeciesType(),data.getSingal());
                //intent.putExtra("speciesTypeChineseName",typeTitle[position]);
                startActivity(intent);
            }
        });

        sliderBar.setData(indexList, positionList);//索引栏设置数据
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
                linearLayoutManager.scrollToPositionWithOffset((Integer) view.getTag(), 0);
            }
        });
    }

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.species_class_imageButton_alert_menu:
                if (position == 3) {
                    return;
                }
                showAlertMenu(this);
                break;

            case R.id.species_class_imageButton_alert_pick:
                if (position == 0) {
                    showAmphibiaPickDialog();
                } else if (position == 2) {
                    showBirdPickDialog();
                } else if (position == 1) {
                    showReptilesPickDialog();
                }
                //else {showInsectPickDialog();}

                break;
            case R.id.species_class_title_bar_linear_layout_actionBack:
                actionBack();
                break;
        }
    }

    public void showBirdPickDialog() {
        hideTitleBar();
        mydialog_bird dialog = new mydialog_bird(this, R.style.customDialog, width, height);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showAmphibiaPickDialog() {
        hideTitleBar();
        mydialog_amphibia dialog = new mydialog_amphibia(this, R.style.customDialog, width, height);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showReptilesPickDialog() {
        hideTitleBar();
        mydialog_retile dialog = new mydialog_retile(this, R.style.customDialog, width, height);

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showInsectPickDialog() {
        hideTitleBar();
        mydialog_insect dialog = new mydialog_insect(this, R.style.customDialog, width, height);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void actionBack() {
        this.finish();
    }

    private void showAlertMenu(final Context context) {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display;
        display = windowManager.getDefaultDisplay();
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View alertMenuView = LayoutInflater.from(context).inflate(R.layout.alert_menu, null);
        TextView button2 = (TextView) alertMenuView.findViewById(R.id.browse_by_section);
        TextView button1 = (TextView) alertMenuView.findViewById(R.id.browse_by_order);
        TextView alpha = (TextView) alertMenuView.findViewById(R.id.browse_by_alpha);

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
            public void onClick(View v) {//按目
                dialog.dismiss();
                if (sourceFromPick == false) {
                    if (sortingByOrder != true) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sortingByOrder = true;
                                sortingEditor.putBoolean("sortingWay", true).apply();
                                if (position <= 2||position>3) {
                                    speciesList = DataSupport.where("speciesType=?", getIntent().getStringExtra("speciesType")).find(Species.class);
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();

                                    createIndexList(speciesList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                        }
                                    });
                                }
                            }
                        }).start();

                    }
                } else {
                    if (sortingByOrder != true) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sortingByOrder = true;
                                sortingEditor.putBoolean("sortingWay", true).apply();
                                if (position <= 2||position>3) {
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();
                                    createIndexListc(speciesList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                        }
                                    });
                                }
                            }
                        }).start();

                    }

                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {//按科浏览
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (sourceFromPick == false) {
                    if (sortingByOrder != false) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sortingByOrder = false;
                                sortingEditor.putBoolean("sortingWay", false).apply();
                                if (position <= 2||position>3) {
                                    speciesList = DataSupport.where("speciesType=?", getIntent().getStringExtra("speciesType")).find(Species.class);
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();
                                    createIndexList(speciesList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                        }
                                    });
                                } else {
                                    speciesList = DataSupport.where("chineseName=?", getIntent().getStringExtra("speciesClassName")).find(Species.class);
                                }
                            }
                        }).start();

                    }
                } else {
                    if (sortingByOrder != false) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sortingByOrder = false;
                                sortingEditor.putBoolean("sortingWay", false).apply();
                                if (position <= 2||position>3) {
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();
                                    createIndexListc(speciesList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                        }
                                    });
                                }
                            }
                        }).start();

                    }

                }

            }
        });

        alpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                if (position <= 2||position>3) {
                                    speciesList = DataSupport.where("speciesType=?", getIntent().getStringExtra("speciesType")).find(Species.class);
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();
                                    sortByAlpha();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                        }
                                    });
                                } else {
                                    speciesList = DataSupport.where("chineseName=?", getIntent().getStringExtra("speciesClassName")).find(Species.class);
                                }
                            }
                        }).start();
            }
        });

    }


    public void resetPick(List<CheckBox> checkBoxList) {
        for (CheckBox checkBox : checkBoxList) {
            checkBox.setChecked(false);
        }

//    {
//        if (sortingByOrder != false) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    sortingByOrder = false;
////                    sortingEditor.putBoolean("sortingWay", false).apply();
//                    if (position <= 2) {
//                        speciesList = DataSupport.where("speciesType=?", getIntent().getStringExtra("speciesType")).find(Species.class);
//                        resultList.clear();
//                        positionList.clear();
//                        indexList.clear();
//                        createIndexList(speciesList);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                                sliderBar.setData(indexList, positionList);
//                            }
//                        });
//                    } else {
//                        speciesList = DataSupport.where("chineseName=?", getIntent().getStringExtra("speciesClassName")).find(Species.class);
//                    }
//                }
//            }).start();
//
//        }else {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                sortingByOrder = true;
////                sortingEditor.putBoolean("sortingWay", true).apply();
//                if (position <= 2) {
//                    speciesList = DataSupport.where("speciesType=?", getIntent().getStringExtra("speciesType")).find(Species.class);
//                    resultList.clear();
//                    positionList.clear();
//                    indexList.clear();
//                    createIndexList(speciesList);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter.notifyDataSetChanged();
//                            sliderBar.setData(indexList, positionList);
//                        }
//                    });
//                }
//            }
//        }).start();
//
//    }
    }

    static class Result {
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


        @Override
        public String toString() {
            return "Result{" +
                    "viewType=" + viewType +
                    ", head='" + head + '\'' +
                    ", latinHead='" + latinHead + '\'' +
                    ", species=" + species +
                    '}';
        }
    }

    public class mydialog_bird extends Dialog implements CompoundButton.OnCheckedChangeListener {
        boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false, flag5 = false;//
        List<String> shape = new ArrayList<>();//筛选字段的集合，用于将属性装该集合
        List<String> habit = new ArrayList<>();
        List<String> tone = new ArrayList<>();
        List<String> tailShape = new ArrayList<>();
        private Context context;
        private int width, height;

        public mydialog_bird(@NonNull Context context, @StyleRes int themeResId, int width, int height) {
            super(context, themeResId);
            this.context = context;
            this.width = width;
            this.height = height;
            init();
        }
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//        }

        @Override
        public void onBackPressed() {

            if (titleBarIsHide == true) {
                showTitleBar();
                dismiss();
            } else {
                super.onBackPressed();
                dismiss();
            }
        }

        public void show() {
            super.show();
            Window dialogWindow = getWindow();
            WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            dialogWindow.setGravity(Gravity.BOTTOM);//设置对话框位置
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            params.alpha = 0.8f;
            params.width = metric.widthPixels;
            params.height = metric.heightPixels - height;
            dialogWindow.setAttributes(params);
        }

        private void init() {


            View v = getLayoutInflater().inflate(R.layout.dialog_pick_bird, null);
            int[] shapeId = {R.id.dialog_pick_bird_shape_1, R.id.dialog_pick_bird_shape_2, R.id.dialog_pick_bird_shape_3};

            int[] toneId = {R.id.dialog_pick_bird_tone_1, R.id.dialog_pick_bird_tone_2, R.id.dialog_pick_bird_tone_3, R.id.dialog_pick_bird_tone_4,
                    R.id.dialog_pick_bird_tone_5, R.id.dialog_pick_bird_tone_6, R.id.dialog_pick_bird_tone_7, R.id.dialog_pick_bird_tone_8,
                    R.id.dialog_pick_bird_tone_9, R.id.dialog_pick_bird_tone_10, R.id.dialog_pick_bird_tone_11, R.id.dialog_pick_bird_tone_12,
                    R.id.dialog_pick_bird_tone_13, R.id.dialog_pick_bird_tone_14, R.id.dialog_pick_bird_tone_15, R.id.dialog_pick_bird_tone_16,
                    R.id.dialog_pick_bird_tone_17, R.id.dialog_pick_bird_tone_18, R.id.dialog_pick_bird_tone_19, R.id.dialog_pick_bird_tone_20,
                    R.id.dialog_pick_bird_tone_21, R.id.dialog_pick_bird_tone_22, R.id.dialog_pick_bird_tone_23, R.id.dialog_pick_bird_tone_24,
                    R.id.dialog_pick_bird_tone_25, R.id.dialog_pick_bird_tone_26,};

            int[] habitatId = {R.id.dialog_pick_bird_habit_1, R.id.dialog_pick_bird_habit_2, R.id.dialog_pick_bird_habit_3, R.id.dialog_pick_bird_habit_4,
                    R.id.dialog_pick_bird_habit_5, R.id.dialog_pick_bird_habit_6, R.id.dialog_pick_bird_habit_7, R.id.dialog_pick_bird_habit_8,
                    R.id.dialog_pick_bird_habit_9, R.id.dialog_pick_bird_habit_10, R.id.dialog_pick_bird_habit_11, R.id.dialog_pick_bird_habit_12};

            int[] tail_shapeId = {R.id.dialog_pick_bird_tail_shape_1, R.id.dialog_pick_bird_tail_shape_2, R.id.dialog_pick_bird_tail_shape_3, R.id.dialog_pick_bird_tail_shape_4,
                    R.id.dialog_pick_bird_tail_shape_5, R.id.dialog_pick_bird_tail_shape_6, R.id.dialog_pick_bird_tail_shape_7, R.id.dialog_pick_bird_tail_shape_8,
                    R.id.dialog_pick_bird_tail_shape_9, R.id.dialog_pick_bird_tail_shape_10, R.id.dialog_pick_bird_tail_shape_11, R.id.dialog_pick_bird_tail_shape_12,
                    R.id.dialog_pick_bird_tail_shape_13, R.id.dialog_pick_bird_tail_shape_14};
            final List<CheckBox> checkBoxList = new ArrayList<>();

            for (int i : shapeId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : toneId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : habitatId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : tail_shapeId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            LinearLayout actionBack = (LinearLayout) v.findViewById(R.id.dialog_pick_bird_linear_layout_action_back);
            TextView sure = (TextView) v.findViewById(R.id.dialog_pick_bird_text_view_sure);
            final ImageView resetPick = (ImageView) v.findViewById(R.id.dialog_pick_bird_image_view_reset_pick);

            resetPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sourceFromPick = false;
//                    dismiss();
//                    showTitleBar();
                    resetPick(checkBoxList);
                }
            });


            actionBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    showTitleBar();
                }
            });

            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://api.blackstone.ebirdnote.cn/v1/species/query/";//特征检索请求url
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());//创建一个请求队列
                    JSONArray shapeJsonArray = new JSONArray(shape);//将集合生成jSONArray
                    JSONArray toneJsonArray = new JSONArray(tone);
                    JSONArray habitJsonArray = new JSONArray(habit);
                    JSONArray tail_shapeJsonArray = new JSONArray(tailShape);
                    Map<String, Object> map = new HashMap<>();//建一个map
                    if (tail_shapeJsonArray.length() != 0)//判断是否为空，不为空则将其装入map中
                        map.put("tail_shape", tail_shapeJsonArray);//map中的"tail_shape"必须从返回接口里拿，以key-value的形式存入map
                    if (habitJsonArray.length() != 0)
                        map.put("habitat", habitJsonArray);
                    if (shapeJsonArray.length() != 0)
                        map.put("shape", shapeJsonArray);
                    if (toneJsonArray.length() != 0)
                        map.put("tone", toneJsonArray);
                    JSONObject jsonObject = new JSONObject(map);//生成一个json对象
//                    Toast.makeText(SpeciesClassActivity.this,jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    JsonRequest request = new JsonObjectRequest(Request.Method.POST, url + "bird", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {//
                            try {
                                int code = jsonObject.getInt("code");
                                if (code == 88) {
                                    speciesList.clear();
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        Species species = new Species();
                                        species.setSingal(data.getJSONObject(i).getInt("id"));
                                        species.setChineseName(data.getJSONObject(i).getString("chineseName"));
                                        species.setOrder(data.getJSONObject(i).getString("order"));
                                        species.setFamily(data.getJSONObject(i).getString("family"));
                                        species.setMainPhoto(data.getJSONObject(i).getString("mainPhoto"));
                                        species.setLatinName(data.getJSONObject(i).getString("latinName"));
                                        species.setEnglishName(data.getJSONObject(i).getString("englishName"));
                                        species.setSpeciesType(data.getJSONObject(i).getString("speciesType"));
                                        speciesList.add(species);
                                    }
                                    //createIndexList(speciesList);
                                    sourceFromPick = true;
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();
                                    createIndexListc(speciesList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                            showTitleBar();
                                        }
                                    });
                                    dismiss();
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(SpeciesClassActivity.this, message, Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    showTitleBar();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(SpeciesClassActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            dismiss();
                            showTitleBar();
                        }
                    });
                    requestQueue.add(request);
                }
            });

            setContentView(v);
            TextView t1 = (TextView) v.findViewById(R.id.t1);
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l1 = (LinearLayout) findViewById(R.id.l1);
                    if (flag1 == true) {
                        l1.setVisibility(View.VISIBLE);
                        flag1 = false;
                    } else if (flag1 == false) {
                        l1.setVisibility(View.GONE);
                        flag1 = true;
                    }
                }
            });
            TextView t2 = (TextView) v.findViewById(R.id.t2);
            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l2 = (LinearLayout) findViewById(R.id.l2);
                    if (flag2 == true) {
                        l2.setVisibility(View.VISIBLE);
                        flag2 = false;
                    } else if (flag2 == false) {
                        l2.setVisibility(View.GONE);
                        flag2 = true;
                    }
                }
            });
            TextView t3 = (TextView) v.findViewById(R.id.t3);
            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l3 = (LinearLayout) findViewById(R.id.l3);
                    if (flag3 == true) {
                        l3.setVisibility(View.VISIBLE);
                        flag3 = false;
                    } else if (flag3 == false) {
                        l3.setVisibility(View.GONE);
                        flag3 = true;
                    }
                }
            });
            TextView t4 = (TextView) v.findViewById(R.id.t4);
            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l4 = (LinearLayout) findViewById(R.id.l4);
                    if (flag4 == true) {
                        l4.setVisibility(View.VISIBLE);
                        flag4 = false;
                    } else if (flag4 == false) {
                        l4.setVisibility(View.GONE);
                        flag4 = true;
                    }
                }
            });
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch ((String) buttonView.getTag())//根据标记可以知道按钮属于哪个筛选类别
            {
                case "shape":
                    if (isChecked) {
                        shape.add(buttonView.getText().toString());
                    } else {
                        shape.remove(buttonView.getText().toString());
                    }
                    break;
                case "tone":
                    if (isChecked) {
                        tone.add(buttonView.getText().toString());
                    } else {
                        tone.remove(buttonView.getText().toString());
                    }
                    break;
                case "habit":
                    if (isChecked) {
                        habit.add(buttonView.getText().toString());
                    } else {
                        habit.remove(buttonView.getText().toString());
                    }
                    break;
                case "tail_shape":
                    if (isChecked) {
                        tailShape.add(buttonView.getText().toString());
                    } else {
                        tailShape.remove(buttonView.getText().toString());
                    }
                    break;
            }

        }
    }

    public class mydialog_amphibia extends Dialog implements CompoundButton.OnCheckedChangeListener {
        boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false;
        List<String> fil_web = new ArrayList<>();//筛选字段的集合，用于将属性装该集合
        List<String> fil_major_color = new ArrayList<>();
        List<String> fil_vocal_sac = new ArrayList<>();
        List<String> fil_biotope = new ArrayList<>();
        private Context context;
        private int width, height;

        public mydialog_amphibia(@NonNull Context context, @StyleRes int themeResId, int width, int height) {
            super(context, themeResId);
            this.context = context;
            this.width = width;
            this.height = height;
            init();
        }

        //        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//        }
        @Override
        public void onBackPressed() {

            if (titleBarIsHide == true) {
                showTitleBar();
                dismiss();
            } else {
                super.onBackPressed();
                dismiss();
            }
        }

        public void show() {
            super.show();
            Window dialogWindow = getWindow();
            WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            params.alpha = 0.8f;
            dialogWindow.setGravity(Gravity.BOTTOM);//设置对话框位置
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            params.width = metric.widthPixels;
            params.height = metric.heightPixels - height;
            dialogWindow.setAttributes(params);
        }

        private void init() {
            int[] webId = {R.id.dialog_pick_amphibia_web_1, R.id.dialog_pick_amphibia_web_2, R.id.dialog_pick_amphibia_web_3,
                    R.id.dialog_pick_amphibia_web_4, R.id.dialog_pick_amphibia_web_5, R.id.dialog_pick_amphibia_web_6,
                    R.id.dialog_pick_amphibia_web_7, R.id.dialog_pick_amphibia_web_8, R.id.dialog_pick_amphibia_web_9,
                    R.id.dialog_pick_amphibia_web_10, R.id.dialog_pick_amphibia_web_11, R.id.dialog_pick_amphibia_web_12,
                    R.id.dialog_pick_amphibia_web_13, R.id.dialog_pick_amphibia_web_14};


            int[] major_colorId = {R.id.dialog_pick_amphibia_major_color_1, R.id.dialog_pick_amphibia_major_color_2, R.id.dialog_pick_amphibia_major_color_3,
                    R.id.dialog_pick_amphibia_major_color_4, R.id.dialog_pick_amphibia_major_color_5, R.id.dialog_pick_amphibia_major_color_6,
                    R.id.dialog_pick_amphibia_major_color_7, R.id.dialog_pick_amphibia_major_color_8, R.id.dialog_pick_amphibia_major_color_9,
                    R.id.dialog_pick_amphibia_major_color_10, R.id.dialog_pick_amphibia_major_color_11, R.id.dialog_pick_amphibia_major_color_12,
                    R.id.dialog_pick_amphibia_major_color_13, R.id.dialog_pick_amphibia_major_color_14, R.id.dialog_pick_amphibia_major_color_15,
                    R.id.dialog_pick_amphibia_major_color_16};


            int[] sacId = {R.id.dialog_pick_amphibia_sac_1, R.id.dialog_pick_amphibia_sac_2, R.id.dialog_pick_amphibia_sac_3};


            int[] habitId = {R.id.dialog_pick_amphibia_habit_1, R.id.dialog_pick_amphibia_habit_2, R.id.dialog_pick_amphibia_habit_3,
                    R.id.dialog_pick_amphibia_habit_4, R.id.dialog_pick_amphibia_habit_5, R.id.dialog_pick_amphibia_habit_6,
                    R.id.dialog_pick_amphibia_habit_7, R.id.dialog_pick_amphibia_habit_8, R.id.dialog_pick_amphibia_habit_9,
                    R.id.dialog_pick_amphibia_habit_10, R.id.dialog_pick_amphibia_habit_11};

            final List<CheckBox> checkBoxList = new ArrayList<>();

            View v = getLayoutInflater().inflate(R.layout.dialog_pick_amphibia, null);

            for (int i : webId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : major_colorId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : sacId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : habitId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }
            ImageView resetPick = (ImageView) v.findViewById(R.id.dialog_pick_amphibia_image_view_reset_pick);
            LinearLayout actionBack = (LinearLayout) v.findViewById(R.id.dialog_pick_amphibia_linear_layout_action_back);
            TextView sure = (TextView) v.findViewById(R.id.dialog_pick_amphibia_text_view_sure);
            setContentView(v);
            TextView t1 = (TextView) v.findViewById(R.id.t1);
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l1 = (LinearLayout) findViewById(R.id.l1);
                    if (flag1 == true) {
                        l1.setVisibility(View.VISIBLE);
                        flag1 = false;
                    } else if (flag1 == false) {
                        l1.setVisibility(View.GONE);
                        flag1 = true;
                    }
                }
            });
            TextView t2 = (TextView) v.findViewById(R.id.t2);
            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l2 = (LinearLayout) findViewById(R.id.l2);
                    if (flag2 == true) {
                        l2.setVisibility(View.VISIBLE);
                        flag2 = false;
                    } else if (flag2 == false) {
                        l2.setVisibility(View.GONE);
                        flag2 = true;
                    }
                }
            });
            TextView t3 = (TextView) v.findViewById(R.id.t3);
            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l3 = (LinearLayout) findViewById(R.id.l3);
                    if (flag3 == true) {
                        l3.setVisibility(View.VISIBLE);
                        flag3 = false;
                    } else if (flag3 == false) {
                        l3.setVisibility(View.GONE);
                        flag3 = true;
                    }
                }
            });
            TextView t4 = (TextView) v.findViewById(R.id.t4);
            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l4 = (LinearLayout) findViewById(R.id.l4);
                    if (flag4 == true) {
                        l4.setVisibility(View.VISIBLE);
                        flag4 = false;
                    } else if (flag4 == false) {
                        l4.setVisibility(View.GONE);
                        flag4 = true;
                    }
                }
            });
            resetPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sourceFromPick = false;
//                    dismiss();
//                    showTitleBar();
                    resetPick(checkBoxList);
                }
            });

            actionBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    showTitleBar();
                }
            });

            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://api.blackstone.ebirdnote.cn/v1/species/query/";//特征检索请求url
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());//创建一个请求队列
                    JSONArray webJsonArray = new JSONArray(fil_web);//将集合生成jSONArray
                    JSONArray majorColorJsonArray = new JSONArray(fil_major_color);
                    JSONArray biotpeJsonArray = new JSONArray(fil_biotope);
                    JSONArray socJsonArray = new JSONArray(fil_vocal_sac);
                    Map<String, Object> map = new HashMap<>();//建一个map
                    if (socJsonArray.length() != 0)//判断是否为空，不为空则将其装入map中
                        map.put("fil_vocal_sac", socJsonArray);//map中的"tail_shape"必须从返回接口里拿，以key-value的形式存入map
                    if (biotpeJsonArray.length() != 0)
                        map.put("fil_biotope", biotpeJsonArray);
                    if (webJsonArray.length() != 0)
                        map.put("fil_web", webJsonArray);
                    if (majorColorJsonArray.length() != 0)
                        map.put("fil_major_color", majorColorJsonArray);
                    JSONObject jsonObject = new JSONObject(map);//生成一个json对象
//                    Toast.makeText(SpeciesClassActivity.this,jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    JsonRequest request = new JsonObjectRequest(Request.Method.POST, url + "amphibia", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {//
                            try {
                                int code = jsonObject.getInt("code");
                                if (code == 88) {
                                    speciesList.clear();
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        Species species = new Species();
                                        species.setSingal(data.getJSONObject(i).getInt("id"));
                                        species.setChineseName(data.getJSONObject(i).getString("chineseName"));
                                        species.setOrder(data.getJSONObject(i).getString("order"));
                                        species.setFamily(data.getJSONObject(i).getString("family"));
                                        species.setMainPhoto(data.getJSONObject(i).getString("mainPhoto"));
                                        species.setLatinName(data.getJSONObject(i).getString("latinName"));
                                        //species.setEnglishName(data.getJSONObject(i).getString("englishName"));
                                        species.setSpeciesType(data.getJSONObject(i).getString("speciesType"));
                                        speciesList.add(species);
                                    }
                                    //createIndexList(speciesList);
                                    sourceFromPick = true;
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();
                                    createIndexListc(speciesList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                            showTitleBar();
                                        }
                                    });
                                    dismiss();
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(SpeciesClassActivity.this, message, Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    showTitleBar();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(SpeciesClassActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                            dismiss();
                            showTitleBar();
                        }
                    });
                    requestQueue.add(request);
                }
            });
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch ((String) buttonView.getTag()) {
                case "web":
                    if (isChecked) {
                        fil_web.add(buttonView.getText().toString());
                    } else {
                        fil_web.remove(buttonView.getText().toString());
                    }
                    break;

                case "majorColor":
                    if (isChecked) {
                        fil_major_color.add(buttonView.getText().toString());
                    } else {
                        fil_major_color.remove(buttonView.getText().toString());
                    }
                    break;

                case "sac":
                    if (isChecked) {
                        fil_vocal_sac.add(buttonView.getText().toString());
                    } else {
                        fil_vocal_sac.remove(buttonView.getText().toString());
                    }
                    break;

                case "habit":
                    if (isChecked) {
                        fil_biotope.add(buttonView.getText().toString());
                    } else {
                        fil_biotope.remove(buttonView.getText().toString());
                    }
                    break;
            }

        }
    }

    public class mydialog_retile extends Dialog implements CompoundButton.OnCheckedChangeListener {
        boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false, flag5 = false;
        List<String> shapeList = new ArrayList<>();
        List<String> majorList = new ArrayList<>();
        List<String> biotopeList = new ArrayList<>();
        List<String> microBiotopeList = new ArrayList<>();
        List<String> bigscaleList = new ArrayList<>();
        private Context context;
        private int width, height;

        public mydialog_retile(@NonNull Context context, @StyleRes int themeResId, int width, int height) {
            super(context, themeResId);
            this.context = context;
            this.width = width;
            this.height = height;
            init();
        }
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//        }

        @Override
        public void onBackPressed() {

            if (titleBarIsHide == true) {
                showTitleBar();
                dismiss();
            } else {
                super.onBackPressed();
                dismiss();
            }
        }

        public void show() {
            super.show();
            Window dialogWindow = getWindow();
            WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            params.alpha = 0.8f;
            dialogWindow.setGravity(Gravity.BOTTOM);//设置对话框位置
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            params.width = metric.widthPixels;
            params.height = metric.heightPixels - height;
            dialogWindow.setAttributes(params);
        }

        private void init() {
            int[] shape = {R.id.dialog_pick_reptiles_fil_shape_1, R.id.dialog_pick_reptiles_fil_shape_2, R.id.dialog_pick_reptiles_fil_shape_3};//形状

            int[] majorColor = {R.id.dialog_pick_reptiles_fil_major_color_1, R.id.dialog_pick_reptiles_fil_major_color_2, R.id.dialog_pick_reptiles_fil_major_color_3,//主色
                    R.id.dialog_pick_reptiles_fil_major_color_4, R.id.dialog_pick_reptiles_fil_major_color_5, R.id.dialog_pick_reptiles_fil_major_color_6,
                    R.id.dialog_pick_reptiles_fil_major_color_7, R.id.dialog_pick_reptiles_fil_major_color_8, R.id.dialog_pick_reptiles_fil_major_color_9,
                    R.id.dialog_pick_reptiles_fil_major_color_10, R.id.dialog_pick_reptiles_fil_major_color_11, R.id.dialog_pick_reptiles_fil_major_color_12,
                    R.id.dialog_pick_reptiles_fil_major_color_13, R.id.dialog_pick_reptiles_fil_major_color_14, R.id.dialog_pick_reptiles_fil_major_color_15,
                    R.id.dialog_pick_reptiles_fil_major_color_16, R.id.dialog_pick_reptiles_fil_major_color_17, R.id.dialog_pick_reptiles_fil_major_color_18,
                    R.id.dialog_pick_reptiles_fil_major_color_19, R.id.dialog_pick_reptiles_fil_major_color_20, R.id.dialog_pick_reptiles_fil_major_color_21,
                    R.id.dialog_pick_reptiles_fil_major_color_22, R.id.dialog_pick_reptiles_fil_major_color_23, R.id.dialog_pick_reptiles_fil_major_color_24,
                    R.id.dialog_pick_reptiles_fil_major_color_25, R.id.dialog_pick_reptiles_fil_major_color_26, R.id.dialog_pick_reptiles_fil_major_color_27,
                    R.id.dialog_pick_reptiles_fil_major_color_28, R.id.dialog_pick_reptiles_fil_major_color_29};

            final int[] biotope = {R.id.dialog_pick_reptiles_fil_biotope_1, R.id.dialog_pick_reptiles_fil_biotope_2, R.id.dialog_pick_reptiles_fil_biotope_3,//栖息地
                    R.id.dialog_pick_reptiles_fil_biotope_4, R.id.dialog_pick_reptiles_fil_biotope_5, R.id.dialog_pick_reptiles_fil_biotope_6,
                    R.id.dialog_pick_reptiles_fil_biotope_7, R.id.dialog_pick_reptiles_fil_biotope_8, R.id.dialog_pick_reptiles_fil_biotope_9,
                    R.id.dialog_pick_reptiles_fil_biotope_10, R.id.dialog_pick_reptiles_fil_biotope_11, R.id.dialog_pick_reptiles_fil_biotope_12,
                    R.id.dialog_pick_reptiles_fil_biotope_13};

            int[] microBiotope = {R.id.dialog_pick_reptiles_fil_micro_biotope_1, R.id.dialog_pick_reptiles_fil_micro_biotope_2, R.id.dialog_pick_reptiles_fil_micro_biotope_3,//生境
                    R.id.dialog_pick_reptiles_fil_micro_biotope_4, R.id.dialog_pick_reptiles_fil_micro_biotope_5, R.id.dialog_pick_reptiles_fil_micro_biotope_6,
                    R.id.dialog_pick_reptiles_fil_micro_biotope_7, R.id.dialog_pick_reptiles_fil_micro_biotope_8, R.id.dialog_pick_reptiles_fil_micro_biotope_9,
                    R.id.dialog_pick_reptiles_fil_micro_biotope_10, R.id.dialog_pick_reptiles_fil_micro_biotope_11, R.id.dialog_pick_reptiles_fil_micro_biotope_12,
                    R.id.dialog_pick_reptiles_fil_micro_biotope_13, R.id.dialog_pick_reptiles_fil_micro_biotope_14};

            int[] bigscale = {R.id.dialog_pick_reptiles_fil_has_bigscale_1, R.id.dialog_pick_reptiles_fil_has_bigscale_2};

            final List<CheckBox> checkBoxList = new ArrayList<>();

            View v = getLayoutInflater().inflate(R.layout.dialog_pick_reptiles, null);
            setContentView(v);
            for (int i : shape) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : majorColor) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : biotope) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : microBiotope) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }

            for (int i : bigscale) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
                checkBoxList.add((CheckBox) v.findViewById(i));
            }
            ImageView resetPick = (ImageView) v.findViewById(R.id.dialog_pick_reptiles_image_view_reset_pick);
            LinearLayout actionBack = (LinearLayout) v.findViewById(R.id.dialog_pick_reptiles_linear_layout_action_back);
            TextView sure = (TextView) v.findViewById(R.id.dialog_pick_reptiles_text_view_sure);

            resetPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sourceFromPick = false;
                    // dismiss();
//                    showTitleBar();
                    resetPick(checkBoxList);
                }
            });
            actionBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    showTitleBar();
                }
            });

            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://api.blackstone.ebirdnote.cn/v1/species/query/";//特征检索请求url
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());//创建一个请求队列
                    JSONArray shapeJsonArray = new JSONArray(shapeList);//将集合生成jSONArray
                    JSONArray majorColorJsonArray = new JSONArray(majorList);
                    JSONArray biotpeJsonArray = new JSONArray(biotopeList);
                    JSONArray microBiotopeJsonArray = new JSONArray(microBiotopeList);
                    JSONArray bigscaleJsonArray = new JSONArray(bigscaleList);
                    Map<String, Object> map = new HashMap<>();//建一个map
                    if (shapeJsonArray.length() != 0)//判断是否为空，不为空则将其装入map中
                        map.put("fil_shape", shapeJsonArray);//map中的"tail_shape"必须从返回接口里拿，以key-value的形式存入map
                    if (majorColorJsonArray.length() != 0)
                        map.put("fil_major_color", majorColorJsonArray);
                    if (microBiotopeJsonArray.length() != 0)
                        map.put("fil_micro_biotope", microBiotopeJsonArray);
                    if (biotpeJsonArray.length() != 0)
                        map.put("fil_biotope", biotpeJsonArray);
                    if (bigscaleJsonArray.length() != 0)
                        map.put("fil_has_bigscale", bigscaleJsonArray);
                    JSONObject jsonObject = new JSONObject(map);//生成一个json对象
//                    Toast.makeText(SpeciesClassActivity.this,jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    JsonRequest request = new JsonObjectRequest(Request.Method.POST, url + "reptiles", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {//
                            try {
                                int code = jsonObject.getInt("code");
                                if (code == 88) {
                                    speciesList.clear();
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        Species species = new Species();
                                        species.setSingal(data.getJSONObject(i).getInt("id"));
                                        species.setChineseName(data.getJSONObject(i).getString("chineseName"));
                                        species.setOrder(data.getJSONObject(i).getString("order"));
                                        species.setFamily(data.getJSONObject(i).getString("family"));
                                        species.setMainPhoto(data.getJSONObject(i).getString("mainPhoto"));
                                        species.setLatinName(data.getJSONObject(i).getString("latinName"));
                                        //species.setEnglishName(data.getJSONObject(i).getString("englishName"));
                                        species.setSpeciesType(data.getJSONObject(i).getString("speciesType"));
                                        speciesList.add(species);
                                    }
                                    //createIndexList(speciesList);
                                    sourceFromPick = true;
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();
                                    createIndexListc(speciesList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                            showTitleBar();
                                        }
                                    });
                                    dismiss();
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(SpeciesClassActivity.this, message, Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    showTitleBar();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(SpeciesClassActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                            dismiss();
                            showTitleBar();
                        }
                    });
                    requestQueue.add(request);
                }
            });


            TextView t1 = (TextView) v.findViewById(R.id.t1);
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l1 = (LinearLayout) findViewById(R.id.l1);
                    if (flag1 == true) {
                        l1.setVisibility(View.VISIBLE);
                        flag1 = false;
                    } else if (flag1 == false) {
                        l1.setVisibility(View.GONE);
                        flag1 = true;
                    }
                }
            });
            TextView t2 = (TextView) v.findViewById(R.id.t2);
            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l2 = (LinearLayout) findViewById(R.id.l2);
                    if (flag2 == true) {
                        l2.setVisibility(View.VISIBLE);
                        flag2 = false;
                    } else if (flag2 == false) {
                        l2.setVisibility(View.GONE);
                        flag2 = true;
                    }
                }
            });
            TextView t3 = (TextView) v.findViewById(R.id.t3);
            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l3 = (LinearLayout) findViewById(R.id.l3);
                    if (flag3 == true) {
                        l3.setVisibility(View.VISIBLE);
                        flag3 = false;
                    } else if (flag3 == false) {
                        l3.setVisibility(View.GONE);
                        flag3 = true;
                    }
                }
            });
            TextView t4 = (TextView) v.findViewById(R.id.t4);
            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l4 = (LinearLayout) findViewById(R.id.l4);
                    if (flag4 == true) {
                        l4.setVisibility(View.VISIBLE);
                        flag4 = false;
                    } else if (flag4 == false) {
                        l4.setVisibility(View.GONE);
                        flag4 = true;
                    }
                }
            });
            TextView t5 = (TextView) v.findViewById(R.id.t5);
            t5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l5 = (LinearLayout) findViewById(R.id.l5);
                    if (flag5 == true) {
                        l5.setVisibility(View.VISIBLE);
                        flag5 = false;
                    } else if (flag5 == false) {
                        l5.setVisibility(View.GONE);
                        flag5 = true;
                    }
                }
            });
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch ((String) buttonView.getTag()) {
                case "fil_shape":
                    if (isChecked) {
                        shapeList.add(buttonView.getText().toString());
                    } else {
                        shapeList.remove(buttonView.getText().toString());
                    }
                    break;

                case "fil_micro_biotope":
                    if (isChecked) {
                        microBiotopeList.add(buttonView.getText().toString());
                    } else {
                        microBiotopeList.remove(buttonView.getText().toString());
                    }
                    break;

                case "fil_biotope":
                    if (isChecked) {
                        biotopeList.add(buttonView.getText().toString());
                    } else {
                        biotopeList.remove(buttonView.getText().toString());
                    }
                    break;

                case "fil_has_bigscale":
                    if (isChecked) {
                        bigscaleList.add(buttonView.getText().toString());
                    } else {
                        bigscaleList.remove(buttonView.getText().toString());
                    }
                    break;
                case "fil_major_color":
                    if (isChecked) {
                        majorList.add(buttonView.getText().toString());
                    } else {
                        majorList.remove(buttonView.getText().toString());
                    }
                    break;
            }
        }
    }

    public class mydialog_insect extends Dialog implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onBackPressed() {

            if (titleBarIsHide == true) {
                showTitleBar();
                dismiss();
            } else {
                super.onBackPressed();
                dismiss();
            }
        }

        boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false, flag5 = false;
        List<String> tentacle = new ArrayList<>();
        List<String> wing = new ArrayList<>();
        List<String> mouthparts = new ArrayList<>();
        List<String> life_cycle = new ArrayList<>();
        List<String> leg = new ArrayList<>();
        private Context context;
        private int width, height;

        public mydialog_insect(@NonNull Context context, @StyleRes int themeResId, int width, int height) {
            super(context, themeResId);
            this.context = context;
            this.width = width;
            this.height = height;
            init();
        }

        //        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//        }
        public void show() {
            super.show();
            Window dialogWindow = getWindow();
            WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            params.alpha = 0.8f;
            dialogWindow.setGravity(Gravity.BOTTOM);//设置对话框位置
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            params.width = metric.widthPixels;
            params.height = metric.heightPixels - height;
            dialogWindow.setAttributes(params);
        }

        private void init() {
            int[] tentacleId = {R.id.dialog_pick_insect_fil_tentacle_1, R.id.dialog_pick_insect_fil_tentacle_2, R.id.dialog_pick_insect_fil_tentacle_3,
                    R.id.dialog_pick_insect_fil_tentacle_4, R.id.dialog_pick_insect_fil_tentacle_5, R.id.dialog_pick_insect_fil_tentacle_6, R.id.dialog_pick_insect_fil_tentacle_7,
                    R.id.dialog_pick_insect_fil_tentacle_8, R.id.dialog_pick_insect_fil_tentacle_9};

            int[] wingId = {R.id.dialog_pick_insect_fil_wing_1, R.id.dialog_pick_insect_fil_wing_2, R.id.dialog_pick_insect_fil_wing_3, R.id.dialog_pick_insect_fil_wing_4,
                    R.id.dialog_pick_insect_fil_wing_5, R.id.dialog_pick_insect_fil_wing_6, R.id.dialog_pick_insect_fil_wing_7, R.id.dialog_pick_insect_fil_wing_8,
                    R.id.dialog_pick_insect_fil_wing_9, R.id.dialog_pick_insect_fil_wing_10};

            int[] mouthpartsId = {R.id.dialog_pick_insect_fil_mouthparts_1, R.id.dialog_pick_insect_fil_mouthparts_2, R.id.dialog_pick_insect_fil_mouthparts_3,
                    R.id.dialog_pick_insect_fil_mouthparts_4, R.id.dialog_pick_insect_fil_mouthparts_5, R.id.dialog_pick_insect_fil_mouthparts_6};

            int[] life_cycleId = {R.id.dialog_pick_insect_fil_life_cycle_1, R.id.dialog_pick_insect_fil_life_cycle_2, R.id.dialog_pick_insect_fil_life_cycle_3,
                    R.id.dialog_pick_insect_fil_life_cycle_4, R.id.dialog_pick_insect_fil_life_cycle_5};

            int[] legId = {R.id.dialog_pick_insect_fil_leg_1, R.id.dialog_pick_insect_fil_leg_2, R.id.dialog_pick_insect_fil_leg_3, R.id.dialog_pick_insect_fil_leg_4,
                    R.id.dialog_pick_insect_fil_leg_5};

            View v = getLayoutInflater().inflate(R.layout.dialog_pick_insect, null);
            setContentView(v);
            for (int i : tentacleId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
            }

            for (int i : wingId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
            }

            for (int i : mouthpartsId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
            }

            for (int i : life_cycleId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
            }

            for (int i : legId) {
                ((CheckBox) v.findViewById(i)).setOnCheckedChangeListener(this);
            }
            ImageView resetPick = (ImageView) v.findViewById(R.id.dialog_pick_insect_image_view_reset_pick);
            TextView sure = (TextView) v.findViewById(R.id.dialog_pick_insect_text_view_sure);
            LinearLayout actionBack = (LinearLayout) v.findViewById(R.id.dialog_pick_insect_linear_layout_action_back);


            resetPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sourceFromPick = false;
                    dismiss();
                    showTitleBar();
//                    resetPick();
                }
            });
            actionBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    showTitleBar();
                }
            });

            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://api.blackstone.ebirdnote.cn/v1/species/query/";//特征检索请求url
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());//创建一个请求队列
                    JSONArray tentacleJsonArray = new JSONArray(tentacle);//将集合生成jSONArray
                    JSONArray wingJsonArray = new JSONArray(wing);
                    JSONArray mouthpartsJsonArray = new JSONArray(mouthparts);
                    JSONArray lifeCycleJsonArray = new JSONArray(life_cycle);
                    JSONArray legJsonArray = new JSONArray(leg);
                    Map<String, Object> map = new HashMap<>();//建一个map
                    if (tentacleJsonArray.length() != 0)//判断是否为空，不为空则将其装入map中
                        map.put("fil_tentacle", tentacleJsonArray);//map中的"tail_shape"必须从返回接口里拿，以key-value的形式存入map
                    if (wingJsonArray.length() != 0)
                        map.put("fil_wing", wingJsonArray);
                    if (lifeCycleJsonArray.length() != 0)
                        map.put("fil_life_cycle", lifeCycleJsonArray);
                    if (mouthpartsJsonArray.length() != 0)
                        map.put("fil_mouthparts", mouthpartsJsonArray);
                    if (legJsonArray.length() != 0)
                        map.put("fil_leg", legJsonArray);
                    JSONObject jsonObject = new JSONObject(map);//生成一个json对象
                    Toast.makeText(SpeciesClassActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    JsonRequest request = new JsonObjectRequest(Request.Method.POST, url + "insect", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {//
                            try {
                                int code = jsonObject.getInt("code");
                                if (code == 88) {
                                    speciesList.clear();
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        Species species = new Species();
                                        species.setSingal(data.getJSONObject(i).getInt("id"));
                                        species.setChineseName(data.getJSONObject(i).getString("chineseName"));
                                        species.setOrder(data.getJSONObject(i).getString("order"));
                                        species.setFamily(data.getJSONObject(i).getString("family"));
                                        species.setMainPhoto(data.getJSONObject(i).getString("mainPhoto"));
                                        species.setLatinName(data.getJSONObject(i).getString("latinName"));
                                        //species.setEnglishName(data.getJSONObject(i).getString("englishName"));
                                        species.setSpeciesType(data.getJSONObject(i).getString("speciesType"));
                                        speciesList.add(species);
                                    }
                                    //createIndexList(speciesList);
                                    sourceFromPick = true;
                                    resultList.clear();
                                    positionList.clear();
                                    indexList.clear();
                                    createIndexListc(speciesList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                            sliderBar.setData(indexList, positionList);
                                            showTitleBar();
                                        }
                                    });
                                    dismiss();
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(SpeciesClassActivity.this, message, Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    showTitleBar();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(SpeciesClassActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                            dismiss();
                            showTitleBar();
                        }
                    });
                    requestQueue.add(request);
                }
            });

            TextView t1 = (TextView) v.findViewById(R.id.t1);
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l1 = (LinearLayout) findViewById(R.id.l1);
                    if (flag1 == true) {
                        l1.setVisibility(View.VISIBLE);
                        flag1 = false;
                    } else if (flag1 == false) {
                        l1.setVisibility(View.GONE);
                        flag1 = true;
                    }
                }
            });
            TextView t2 = (TextView) v.findViewById(R.id.t2);
            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l2 = (LinearLayout) findViewById(R.id.l2);
                    if (flag2 == true) {
                        l2.setVisibility(View.VISIBLE);
                        flag2 = false;
                    } else if (flag2 == false) {
                        l2.setVisibility(View.GONE);
                        flag2 = true;
                    }
                }
            });
            TextView t3 = (TextView) v.findViewById(R.id.t3);
            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l3 = (LinearLayout) findViewById(R.id.l3);
                    if (flag3 == true) {
                        l3.setVisibility(View.VISIBLE);
                        flag3 = false;
                    } else if (flag3 == false) {
                        l3.setVisibility(View.GONE);
                        flag3 = true;
                    }
                }
            });
            TextView t4 = (TextView) v.findViewById(R.id.t4);
            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l4 = (LinearLayout) findViewById(R.id.l4);
                    if (flag4 == true) {
                        l4.setVisibility(View.VISIBLE);
                        flag4 = false;
                    } else if (flag4 == false) {
                        l4.setVisibility(View.GONE);
                        flag4 = true;
                    }
                }
            });
            TextView t5 = (TextView) v.findViewById(R.id.t5);
            t5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout l5 = (LinearLayout) findViewById(R.id.l5);
                    if (flag5 == true) {
                        l5.setVisibility(View.VISIBLE);
                        flag5 = false;
                    } else if (flag5 == false) {
                        l5.setVisibility(View.GONE);
                        flag5 = true;
                    }
                }
            });
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch ((String) buttonView.getTag()) {
                case "fil_tentacle":
                    if (isChecked) {
                        tentacle.add(buttonView.getText().toString());
                    } else {
                        tentacle.remove(buttonView.getText().toString());
                    }
                    break;

                case "fil_wing":
                    if (isChecked) {
                        wing.add(buttonView.getText().toString());
                    } else {
                        wing.remove(buttonView.getText().toString());
                    }
                    break;

                case "fil_mouthparts":
                    if (isChecked) {
                        mouthparts.add(buttonView.getText().toString());
                    } else {
                        mouthparts.remove(buttonView.getText().toString());
                    }
                    break;

                case "fil_life_cycle":
                    if (isChecked) {
                        life_cycle.add(buttonView.getText().toString());
                    } else {
                        life_cycle.remove(buttonView.getText().toString());
                    }
                    break;

                case "fil_leg":
                    if (isChecked) {
                        leg.add(buttonView.getText().toString());
                    } else {
                        leg.remove(buttonView.getText().toString());
                    }
                    break;
            }
        }
    }

//    public void showBirdPick()
//    {
//        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display;
//        display = windowManager.getDefaultDisplay();
//        Dialog dialog = new Dialog(this,R.style.customDialog);
//        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pick_bird, null);
//        view.setMinimumWidth(display.getWidth());
//        dialog.setContentView(view);
//        dialog.setCancelable(false);
//        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.x = 0;
//        lp.y = 0;
//        dialogWindow.setAttributes(lp);
//        dialog.show();
//    }

    public void hideTitleBar()//隐藏物种界面头部的文字和功能图标
    {
        actionBack.setVisibility(View.INVISIBLE);
        speciesClassName.setVisibility(View.INVISIBLE);
        alertMenu.setVisibility(View.INVISIBLE);
        alertPick.setVisibility(View.INVISIBLE);
        titleBarIsHide = true;
    }

    public void showTitleBar()//显示物种界面头部的文字和功能图标
    {
        actionBack.setVisibility(View.VISIBLE);
        speciesClassName.setVisibility(View.VISIBLE);
        alertMenu.setVisibility(View.VISIBLE);
        alertPick.setVisibility(View.VISIBLE);
        titleBarIsHide = false;
    }

}
