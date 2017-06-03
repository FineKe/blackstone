package com.hdu.myship.blackstone;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import JsonUtil.JsonResolverSpeciesDetailed;
import ShapeUtil.GlideRoundTransform;
import database.Amphibia;
import database.Bird;
import database.Insect;
import database.Reptiles;

public class SpeciesDeatailedActivity extends AutoLayoutActivity implements View.OnClickListener {
    private String getSpeciesDetailedURL = "http://api.blackstone.ebirdnote.cn/v1/species/";
    private String collectionURL = "http://api.blackstone.ebirdnote.cn/v1/species/addToCollection";
    private String cancelCollectionURL = "http://api.blackstone.ebirdnote.cn/v1/species/collection/";
    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    private JsonObjectRequest collectionRequest;
    private JsonObjectRequest canelCollcetionRequest;

    private int singal;//物种id
    private String speciesType;
    private ViewPager viewPager;
    private ArrayList<View> views;
    private ArrayList<ImageView> pointers;//点
    private Object speciesDetaileds;
    private MyViewPagerAdapter adapter;
    String TAG = "TEST";
    private String speciesTypeChineseName;
    private String SpeciesChineseName;
    private TextView speciesClassName;
    private TextView speciesName;
    private ImageView copyRight;
    private LinearLayout tab_pointers;

    private boolean start = false;
    private int oldPosition = 0;
    private int currentIndex;
    private int a;
    private ScheduledExecutorService scheduledExecutorService;


    private TextView actionBackText;
    private TextView titleText;
    private ImageView collection;
    private TextView orderFamilyGenus;
    private TextView latinOrderFamily;
    private TextView latinGenus;
    private TextView englishName;

    private LinearLayout linearLayout;
    private LinearLayout actionBack;

    private boolean isCollected;

    private UserInformationUtil userInformation;
    private IsLoginUtil isLoginUtil;
    private UpdateToken updateToken;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        singal = getIntent().getIntExtra("singal", 1);
        speciesType = getIntent().getStringExtra("speciesType");
        //speciesTypeChineseName=getIntent().getStringExtra("speciesTypeChineseName");
        setContentView(R.layout.activity_species_deatailed);
        Log.d(TAG, "onCreate: " + singal);
        initData();
        initViews();
        initEvents();


    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        collection.setOnClickListener(this);
        collection.setOnClickListener(this);
    }

    private void initData() {

        views = new ArrayList<>();
        pointers = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        userInformation = new UserInformationUtil(this);
        isLoginUtil = new IsLoginUtil(this);
        updateToken = new UpdateToken(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        tab_pointers = (LinearLayout) findViewById(R.id.activity_species_deatailed_linear_layout_pointers);
        copyRight = (ImageView) findViewById(R.id.activity_species_deatailed_image_view_copy_right);
        viewPager = (ViewPager) findViewById(R.id.species_detailed_viewpager);

        actionBack = (LinearLayout) findViewById(R.id.species_deatailed_title_bar_linear_layout_action_back);
        actionBackText = (TextView) findViewById(R.id.species_detailed_title_bar_textView_action_back_text);
        titleText = (TextView) findViewById(R.id.species_detailed_title_bar_textView_title);
        collection = (ImageView) findViewById(R.id.species_detailed_title_bar_image_view_collection);

        orderFamilyGenus = (TextView) findViewById(R.id.activity_species_deatailed_text_view_order_family_genus);
        latinOrderFamily = (TextView) findViewById(R.id.activity_species_deatailed_text_view_latin_order_family);
        latinGenus = (TextView) findViewById(R.id.activity_species_deatailed_text_view_latin_genus);
        englishName = (TextView) findViewById(R.id.activity_species_deatailed_text_view_english_name);
        linearLayout = (LinearLayout) findViewById(R.id.species_deatailed_linear_layout);
        loadDeatailed(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createAllView() {
        switch (speciesType) {
            case "reptiles":
                Reptiles reptiles = (Reptiles) speciesDetaileds;
                isCollected = reptiles.isCollected();
                if (reptiles.isCollected()) {
                    collection.setImageResource(R.mipmap.heart_pressed);
                } else {
                    collection.setImageResource(R.mipmap.heart_normal);
                }
                createReptilesView(reptiles);
                for (String picture : (reptiles).getImgs()) {
                    views.add(createView(picture));
                    pointers.add(createPointer());
                }
                break;
            case "bird":
                ;
                Bird bird = (Bird) speciesDetaileds;
                isCollected = bird.isCollected();
                if (bird.isCollected()) {
                    collection.setImageResource(R.mipmap.heart_pressed);
                } else {
                    collection.setImageResource(R.mipmap.heart_normal);
                }
                createBirdView((Bird) speciesDetaileds);
                for (String picture : ((Bird) speciesDetaileds).getImgs()) {
                    views.add(createView(picture));
                    pointers.add(createPointer());
                }
                break;
            case "amphibia":
                Amphibia amphibia = (Amphibia) speciesDetaileds;
                isCollected = amphibia.isCollected();
                if (amphibia.isCollected()) {
                    collection.setImageResource(R.mipmap.heart_pressed);
                } else {
                    collection.setImageResource(R.mipmap.heart_normal);
                }
                createAmphibiaView((Amphibia) speciesDetaileds);
                for (String picture : ((Amphibia) speciesDetaileds).getImgs()) {
                    views.add(createView(picture));
                    pointers.add(createPointer());
                }
                break;
            case "insect":
                ;
                Insect insect = (Insect) speciesDetaileds;
                isCollected = insect.isCollected();
                if (insect.isCollected()) {
                    collection.setImageResource(R.mipmap.heart_pressed);
                } else {
                    collection.setImageResource(R.mipmap.heart_normal);
                }
                createInsectView((Insect) speciesDetaileds);
                for (String picture : ((Insect) speciesDetaileds).getImgs()) {
                    views.add(createView(picture));
                    pointers.add(createPointer());
                }
                break;

        }
        for (ImageView imageView : pointers) {
            tab_pointers.addView(imageView);
        }

        currentIndex = 0;
        pointers.get(currentIndex).setImageResource(R.mipmap.pointer_pressed);
        adapter = new MyViewPagerAdapter();
        viewPager.setAdapter(adapter);

        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 3, 3, TimeUnit.SECONDS);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                a = position;
                setCurrentPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createInsectView(Insect speciesDetailed) {
        speciesDetailed.setViewTables();
        actionBackText.setText(speciesDetailed.getChineseName());
        titleText.setText("#" + speciesDetailed.getSingal() + speciesDetailed.getChineseName());
        orderFamilyGenus.setText(speciesDetailed.getOrder());
        latinOrderFamily.setText(speciesDetailed.getOrderLatin());
        latinGenus.setText(speciesDetailed.getLatinName());//生物圈这么干的，latinname
//        englishName.setText(speciesDetaileds.getEnglishName());
        englishName.setVisibility(View.GONE);
        String[] tables = getResources().getStringArray(R.array.insectTablesName);
        int i = 0;
        for (String s : speciesDetailed.getViewTables()) {
            if (!s.equals("")) {
                LinearLayout tableView = new LinearLayout(this);
                tableView.setGravity(Gravity.CENTER_VERTICAL);
                tableView.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(this);
                title.setText(tables[i]);
                title.setTextColor(getResources().getColor(R.color.mycolor));
                TextView content = new TextView(this);
                content.setText(s);
                tableView.addView(title);
                tableView.addView(content);
                View view1 = linearLayout.getChildAt(1);
                tableView.setPadding(view1.getPaddingLeft(), 0, view1.getPaddingRight(), 0);
                View view = LayoutInflater.from(this).inflate(R.layout.split_line, linearLayout);
                linearLayout.addView(tableView);
            }
            i++;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.split_line, linearLayout);
    }

    private void createAmphibiaView(Amphibia speciesDetailed) {
        speciesDetailed.setViewTables();
        actionBackText.setText("两栖类");
        titleText.setText("#" + speciesDetailed.getSingal() + speciesDetailed.getChineseName());
        orderFamilyGenus.setText(speciesDetailed.getOrder() + ">" + speciesDetailed.getFamily() + ">" + speciesDetailed.getGenus());
        latinOrderFamily.setText(speciesDetailed.getOrderLatin() + ">" + speciesDetailed.getFamilyLatin() + ">");
        latinGenus.setText(speciesDetailed.getGenusLatin());//生物圈这么干的，latinname
        englishName.setText(speciesDetailed.getLatinName());
//        englishName.setVisibility(View.GONE)
        String[] tables = getResources().getStringArray(R.array.aphibiaTablesName);
        int i = 0;
        for (String s : speciesDetailed.getViewTables()) {
            if (!s.equals("")) {
                LinearLayout tableView = new LinearLayout(this);
                tableView.setGravity(Gravity.CENTER_VERTICAL);
                tableView.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(this);
                title.setText(tables[i]);
                title.setTextColor(getResources().getColor(R.color.mycolor));
                TextView content = new TextView(this);
                content.setText(speciesDetailed.getViewTables().get(i));
                tableView.addView(title);
                tableView.addView(content);

                View view1 = linearLayout.getChildAt(1);
                tableView.setPadding(view1.getPaddingLeft(), 0, view1.getPaddingRight(), 0);
                View view = LayoutInflater.from(this).inflate(R.layout.split_line, linearLayout);
                linearLayout.addView(tableView);
                System.out.println(tables[i] + ":");
            }
            i++;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.split_line, linearLayout);

    }

    private void createReptilesView(Reptiles speciesDetailed) {
        speciesDetailed.setViewTables();
        actionBackText.setText("爬行类");
        titleText.setText("#" + speciesDetailed.getSingal() + speciesDetailed.getChineseName());
        orderFamilyGenus.setText(speciesDetailed.getOrder() + ">" + speciesDetailed.getFamily() + ">" + speciesDetailed.getGenus());
        latinOrderFamily.setText(speciesDetailed.getOrderLatin() + ">" + speciesDetailed.getFamilyLatin() + ">");
        latinGenus.setText(speciesDetailed.getGenusLatin());//生物圈这么干的，latinname
        englishName.setText(speciesDetailed.getLatinName());
//        englishName.setVisibility(View.GONE);
        String[] tables = getResources().getStringArray(R.array.reptilesTblesName);
        int i = 0;
        for (String s : speciesDetailed.getViewTables()) {
            if (!s.equals("")) {
                LinearLayout tableView = new LinearLayout(this);
                tableView.setGravity(Gravity.CENTER_VERTICAL);
                tableView.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(this);
                title.setText(tables[i]);
                title.setTextColor(getResources().getColor(R.color.mycolor));
                TextView content = new TextView(this);
                content.setText(speciesDetailed.getViewTables().get(i));
                tableView.addView(title);
                tableView.addView(content);
                View view1 = linearLayout.getChildAt(1);
                tableView.setPadding(view1.getPaddingLeft(), 0, view1.getPaddingRight(), 0);
                View view = LayoutInflater.from(this).inflate(R.layout.split_line, linearLayout);
                linearLayout.addView(tableView);
                System.out.println(tables[i] + ":");
            }
            i++;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.split_line, linearLayout);
    }

    private void createBirdView(final Bird speciesDetailed) {
        speciesDetailed.setViewTables();
        actionBackText.setText("鸟类");
        titleText.setText("#" + speciesDetailed.getSingal() + speciesDetailed.getChineseName());
        orderFamilyGenus.setText(speciesDetailed.getOrder() + ">" + speciesDetailed.getFamily() + ">" + speciesDetailed.getChineseName() + "属");
        latinOrderFamily.setText(speciesDetailed.getOrderLatin() + ">" + speciesDetailed.getFamilyLatin() + ">");
        latinGenus.setText(speciesDetailed.getGenusLatin());//生物圈这么干的，latinname
        englishName.setText(speciesDetailed.getEnglishName());
        String[] tables = getResources().getStringArray(R.array.birdTablesName);
        int i = 0;
        for (String s : speciesDetailed.getViewTables()) {
            if (!s.equals("")) {
                LinearLayout tableView = new LinearLayout(this);
                tableView.setGravity(Gravity.CENTER_VERTICAL);
                tableView.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(this);
                title.setText(tables[i]);
                title.setTextColor(getResources().getColor(R.color.mycolor));
                TextView content = new TextView(this);
                content.setText(speciesDetailed.getViewTables().get(i));
                tableView.addView(title);
                tableView.addView(content);

                View view1 = linearLayout.getChildAt(1);
                tableView.setPadding(view1.getPaddingLeft(), 0, view1.getPaddingRight(), 0);
                View view = LayoutInflater.from(this).inflate(R.layout.split_line, linearLayout);
                linearLayout.addView(tableView);
                System.out.println(tables[i] + ":");
            }
            i++;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.split_line, linearLayout);


        View v = LayoutInflater.from(this).inflate(R.layout.bird_audio_picture, linearLayout);
        ImageView audioPicture = (ImageView) v.findViewById(R.id.bird_audio_picture_image_view_audio_picture);
        final ImageView playAudio = (ImageView) v.findViewById(R.id.bird_audio_picture_image_view_play_audio);
        audioPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(speciesDetailed.getAudioPicture()).placeholder(R.mipmap.loading_big).into(audioPicture);
        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayAudio audio = new PlayAudio(speciesDetailed.getAudio(), SpeciesDeatailedActivity.this);

            }
        });
        //linearLayout.addView(v);
    }


    private void setCurrentPoint(int position) {
        pointers.get(oldPosition).setImageResource(R.mipmap.pointer_normal);
        pointers.get(position).setImageResource(R.mipmap.pointer_pressed);
        currentIndex = position;
        oldPosition = position;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.species_deatailed_title_bar_linear_layout_action_back:
                actionBack();
                break;

            case R.id.species_detailed_title_bar_image_view_collection:
                if (isLoginUtil.getLogined())//判断是否登录
                {
                    updateToken.updateToken();
                    Log.d(TAG, "onClick: "+isCollected);
                    if (!isCollected) {
                        addCollection(singal);
                    } else {
                        cancelCollection(singal);
                    }
                } else {
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void collection() {
        addCollection(singal);
    }

    private void actionBack() {
        this.finish();
    }

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private View createView(String picture) {
        RoundedImageView imgView = new RoundedImageView(this);
        imgView.setBackground(getDrawable(R.drawable.view_pager_background));
        imgView.setCornerRadius(6);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(picture + "?imageslim").placeholder(R.mipmap.loading_big).transform(new GlideRoundTransform(this, 6)).into(imgView);
        return imgView;
    }

    private ImageView createPointer() {
        ImageView pointer = new ImageView(this);
        pointer.setImageResource(R.mipmap.pointer_normal);
        pointer.setAdjustViewBounds(true);
        pointer.setPadding(8, 0, 8, 0);
        return pointer;
    }

    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            if (start == true) {
                currentIndex = (currentIndex + 1) % views.size();
                //更新界面
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //设置当前页面
            viewPager.setCurrentItem(currentIndex);
        }
    };

    public void addCollection(int speciesId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("speciesId", speciesId);
        JSONObject object = new JSONObject(map);
        collectionRequest = new JsonObjectRequest(Request.Method.POST, collectionURL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code = jsonObject.getInt("code");
                    if (code == 88) {
                        collection.setImageResource(R.mipmap.heart_pressed);
                        isCollected = true;
                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(SpeciesDeatailedActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SpeciesDeatailedActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                UserInformationUtil userInformationUtil = new UserInformationUtil(SpeciesDeatailedActivity.this);
                headers.put("token", userInformationUtil.getToken());
                return headers;
            }
        };
        requestQueue.add(collectionRequest);
    }

    public void cancelCollection(int speciesId) {
        canelCollcetionRequest = new JsonObjectRequest(Request.Method.DELETE, cancelCollectionURL+singal,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code = jsonObject.getInt("code");
                    if (code == 88) {
                        collection.setImageResource(R.mipmap.heart_normal);
                        isCollected = false;
                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(SpeciesDeatailedActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SpeciesDeatailedActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                UserInformationUtil userInformationUtil = new UserInformationUtil(SpeciesDeatailedActivity.this);
                headers.put("token", userInformationUtil.getToken());
                return headers;
            }
        };
        requestQueue.add(canelCollcetionRequest);
    }

    public void loadDeatailed(Context context) {
        if (isLoginUtil.getLogined())//如果登录了
        {
            updateToken.updateToken();//判断token是否过期，过期则更新
            JsonObjectRequest speciesDetailedRequest = new JsonObjectRequest(Request.Method.GET, getSpeciesDetailedURL + singal, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code = jsonObject.getInt("code");
                        if (code == 88) {
                            JsonResolverSpeciesDetailed speciesDetailed = new JsonResolverSpeciesDetailed(jsonObject);
                            speciesDetailed.ResolveSpeciesDetailed();
                            speciesDetaileds = speciesDetailed.getResultObject();
                            runOnUiThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    createAllView();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SpeciesDeatailedActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("token", userInformation.getToken());
                    return headers;
                }
            };
            requestQueue.add(speciesDetailedRequest);
        } else {
            JsonObjectRequest speciesDetailedRequest = new JsonObjectRequest(Request.Method.GET, getSpeciesDetailedURL + singal, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code = jsonObject.getInt("code");
                        if (code == 88) {
                            final JsonResolverSpeciesDetailed speciesDetailed = new JsonResolverSpeciesDetailed(jsonObject);
                            speciesDetailed.ResolveSpeciesDetailed();
                            speciesDetaileds = speciesDetailed.getResultObject();
                            runOnUiThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    createAllView();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SpeciesDeatailedActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(speciesDetailedRequest);
        }

    }
}
