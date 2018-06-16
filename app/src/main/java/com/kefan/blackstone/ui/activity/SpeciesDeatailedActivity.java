package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.JsonUtil.JsonResolverSpeciesDetailed;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ShapeUtil.GlideRoundTransform;
import com.kefan.blackstone.database.Amphibia;
import com.kefan.blackstone.database.Bird;
import com.kefan.blackstone.database.Insect;
import com.kefan.blackstone.database.Reptiles;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpeciesDeatailedActivity extends AutoLayoutActivity implements View.OnClickListener {
    private final int LOAD_AUDIO_OK = 8;
    private String getSpeciesDetailedURL = APIManager.BASE_URL + "v1/species/";
    private String collectionURL = APIManager.BASE_URL + "v1/species/addToCollection";
    private String cancelCollectionURL = APIManager.BASE_URL + "v1/species/collection/";
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

    private boolean isPlay = false;

    private MediaPlayer mediaPlayer = null;
    private ImageView playSound = null;

    private UserService userService;

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
    }

    private void initData() {

        userService=new UserServiceImpl();

        views = new ArrayList<>();
        pointers = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        userInformation = new UserInformationUtil(this);
        isLoginUtil = new IsLoginUtil(this);
        updateToken = new UpdateToken(this);


    }


    private void initViews() {
        tab_pointers = (LinearLayout) findViewById(R.id.activity_species_deatailed_linear_layout_pointers);
        copyRight = (ImageView) findViewById(R.id.activity_species_deatailed_image_view_copy_right);
        viewPager = (ViewPager) findViewById(R.id.species_detailed_viewpager);

        actionBack = (LinearLayout) findViewById(R.id.species_deatailed_title_bar_linear_layout_action_back);
        actionBackText = (TextView) findViewById(R.id.species_detailed_title_bar_textView_action_back_text);
        titleText = (TextView) findViewById(R.id.species_detailed_title_bar_textView_title);

        //收藏按钮
        collection = (ImageView) findViewById(R.id.species_detailed_title_bar_image_view_collection);

        orderFamilyGenus = (TextView) findViewById(R.id.activity_species_deatailed_text_view_order_family_genus);
        latinOrderFamily = (TextView) findViewById(R.id.activity_species_deatailed_text_view_latin_order_family);
        latinGenus = (TextView) findViewById(R.id.activity_species_deatailed_text_view_latin_genus);
        englishName = (TextView) findViewById(R.id.activity_species_deatailed_text_view_english_name);
        linearLayout = (LinearLayout) findViewById(R.id.species_deatailed_linear_layout);

        loadDeatailed(this);

    }


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
        titleText.setText(speciesDetailed.getChineseName());
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
                content.setLineSpacing(8, 1);
                tableView.addView(title);
                tableView.addView(content);
                View view1 = linearLayout.getChildAt(1);
                tableView.setPadding(view1.getPaddingLeft(), 10, view1.getPaddingRight(), 10);
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
        titleText.setText(speciesDetailed.getChineseName());
        orderFamilyGenus.setText(speciesDetailed.getOrder() + ">" + speciesDetailed.getFamily() + ">" + speciesDetailed.getGenus());
        latinOrderFamily.setText(speciesDetailed.getOrderLatin() + ">" + speciesDetailed.getFamilyLatin() + ">");
        latinGenus.setText(speciesDetailed.getLatinName());//生物圈这么干的，latinname
        //  englishName.setText(speciesDetailed.getLatinName());
        englishName.setVisibility(View.GONE);
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
                content.setLineSpacing(8, 1);
                tableView.addView(title);
                tableView.addView(content);

                View view1 = linearLayout.getChildAt(1);
                tableView.setPadding(view1.getPaddingLeft(), 10, view1.getPaddingRight(), 10);
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
        titleText.setText(speciesDetailed.getChineseName());
        orderFamilyGenus.setText(speciesDetailed.getOrder() + ">" + speciesDetailed.getFamily() + ">" + speciesDetailed.getGenus());
        latinOrderFamily.setText(speciesDetailed.getOrderLatin() + ">" + speciesDetailed.getFamilyLatin() + ">");
        latinGenus.setText(speciesDetailed.getLatinName());//生物圈这么干的，latinname
//        englishName.setText(speciesDetailed.getLatinName());
        englishName.setVisibility(View.GONE);
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
                content.setLineSpacing(8, 1);
                tableView.addView(title);
                tableView.addView(content);
                View view1 = linearLayout.getChildAt(1);
                tableView.setPadding(view1.getPaddingLeft(), 10, view1.getPaddingRight(), 10);
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
        titleText.setText(speciesDetailed.getChineseName());
        orderFamilyGenus.setText(speciesDetailed.getOrder() + ">" + speciesDetailed.getFamily() + ">" + speciesDetailed.getChineseName() + "属");
        latinOrderFamily.setText(speciesDetailed.getOrderLatin() + ">" + speciesDetailed.getFamilyLatin() + ">");
        latinGenus.setText(speciesDetailed.getLatinName());//生物圈这么干的，latinname
        englishName.setText(speciesDetailed.getEnglishName());
        String[] tables = getResources().getStringArray(R.array.birdTablesName);
        int i = 0;
        for (String s : speciesDetailed.getViewTables()) {
            if (!s.equals("")) {
                LinearLayout tableView = new LinearLayout(this);
                tableView.setGravity(Gravity.CENTER_VERTICAL);
                tableView.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(this);
                tableView.setGravity(Gravity.CENTER_VERTICAL);
                title.setText(tables[i]);
                title.setTextColor(getResources().getColor(R.color.mycolor));
                TextView content = new TextView(this);
                content.setText(speciesDetailed.getViewTables().get(i));
                content.setLineSpacing(8, 1);
                tableView.addView(title);
                tableView.addView(content);

                View view1 = linearLayout.getChildAt(1);
                tableView.setPadding(view1.getPaddingLeft(), 10, view1.getPaddingRight(), 10);
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
        playSound = playAudio;
        audioPicture.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(speciesDetailed.getAudioPicture()).placeholder(R.mipmap.loading_big).into(audioPicture);

        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay == false) {
                    mediaPlayer = new MediaPlayer();
                    isPlay = true;
                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/blackstone/audio/" + speciesDetailed.getSingal() + ".wav");
                    if (!file.exists()) {
                        download(speciesDetailed.getAudio(), speciesDetailed.getSingal() + ".wav");
                    } else {

                        String path = Environment.getExternalStorageDirectory().toString() + "/blackstone/audio/";

                        playAudio.setImageResource(R.mipmap.play_audio_pressed);
                        try {
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setDataSource(path + speciesDetailed.getSingal() + ".wav");
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (mp.isPlaying() == false) {
                                        playAudio.setImageResource(R.mipmap.play_audio_normal);
                                        isPlay = false;
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    isPlay = false;
                    mediaPlayer.stop();
                    playAudio.setImageResource(R.mipmap.play_audio_normal);
                }

            }
        });
    }


    public void playAudio(final MediaPlayer mediaPlayer, final int singal, final ImageView playAudio) {

        String path = Environment.getExternalStorageDirectory().toString() + "/blackstone/audio/";
        playAudio.setImageResource(R.mipmap.play_audio_pressed);
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(path + singal + ".wav");
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mp.isPlaying() == false) {
                        playAudio.setImageResource(R.mipmap.play_audio_normal);
                        isPlay = false;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
//        playAudio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isPlay==true)
//                {
//                    isPlay=false;
//                    mediaPlayer.stop();
//                    playAudio.setImageResource(R.mipmap.play_audio_normal);
//                }else
//                {
//                    isPlay=true;
//                }
//            }
//        });

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
                if (userService.isLogined())//判断是否登录
                {
                    updateToken.updateToken();
                    Log.d(TAG, "onClick: " + isCollected);
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

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private View createView(String picture) {
        RoundedImageView imgView = new RoundedImageView(this);
//        imgView.setBackground(getDrawable(R.drawable.view_pager_background));
        imgView.setCornerRadius(8);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(getApplicationContext()).load(picture + "?imageslim").placeholder(R.mipmap.loading_big).bitmapTransform(new GlideRoundTransform(this, 8)).into(imgView);
        return imgView;
    }

    private ImageView createPointer() {
        ImageView pointer = new ImageView(this);
        pointer.setImageResource(R.mipmap.pointer_normal);
        pointer.setAdjustViewBounds(true);
        pointer.setPadding(8, 0, 8, 0);
        return pointer;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 3, 3, TimeUnit.SECONDS);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(scheduledExecutorService!=null)
//        {
////            scheduledExecutorService.shutdown();
//            scheduledExecutorService.shutdown();
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }

    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            System.out.println(currentIndex);
            currentIndex = (currentIndex + 1) % views.size();
            //更新界面
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
        }
    }

    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            //设置当前页面
            switch (msg.what) {
                case 0:
                    viewPager.setCurrentItem(currentIndex);
                    break;
                case 1:
                    createAllView();
                    start = true;
                    System.out.println(1);
                    break;
                case LOAD_AUDIO_OK:
                    playAudio(mediaPlayer, singal, playSound);
                    break;
            }

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
                Toast.makeText(SpeciesDeatailedActivity.this, "请在有网络下查看", Toast.LENGTH_SHORT).show();
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
        canelCollcetionRequest = new JsonObjectRequest(Request.Method.DELETE, cancelCollectionURL + singal, null, new Response.Listener<JSONObject>() {
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
                Toast.makeText(SpeciesDeatailedActivity.this, "请在有网络下查看", Toast.LENGTH_SHORT).show();
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
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SpeciesDeatailedActivity.this, "请在有网络下查看", Toast.LENGTH_SHORT).show();
                    switch (speciesType) {
                        case "bird":
                            speciesDetaileds = (DataSupport.where("singal=?", "" + singal).find(Bird.class)).get(0);
                            Message messageb = new Message();
                            messageb.what = 1;
                            handler.sendMessage(messageb);
                            break;
                        case "amphibia":
                            speciesDetaileds = (DataSupport.where("singal=?", "" + singal).find(Amphibia.class)).get(0);
                            Message messagea = new Message();
                            messagea.what = 1;
                            handler.sendMessage(messagea);
                            break;
                        case "insect":
                            speciesDetaileds = (DataSupport.where("singal=?", "" + singal).find(Insect.class)).get(0);
                            Message messagei = new Message();
                            messagei.what = 1;
                            handler.sendMessage(messagei);
                            break;
                        case "reptiles":
                            speciesDetaileds = (DataSupport.where("singal=?", "" + singal).find(Reptiles.class)).get(0);
                            Message messages = new Message();
                            messages.what = 1;
                            handler.sendMessage(messages);
                            break;
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("token", userInformation.getToken());
                    return headers;
                }
            };
            speciesDetailedRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 1, 1.0f));
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
                    Toast.makeText(SpeciesDeatailedActivity.this, "请在有网络下查看", Toast.LENGTH_SHORT).show();
                    switch (speciesType) {
                        case "bird":
                            speciesDetaileds = (DataSupport.where("singal=?", "" + singal).find(Bird.class)).get(0);
                            Message messageb = new Message();
                            messageb.what = 1;
                            handler.sendMessage(messageb);
                            break;
                        case "amphibia":
                            speciesDetaileds = (DataSupport.where("singal=?", "" + singal).find(Amphibia.class)).get(0);
                            Message messagea = new Message();
                            messagea.what = 1;
                            handler.sendMessage(messagea);
                            break;
                        case "insect":
                            speciesDetaileds = (DataSupport.where("singal=?", "" + singal).find(Insect.class)).get(0);
                            Message messagei = new Message();
                            messagei.what = 1;
                            handler.sendMessage(messagei);
                            break;
                        case "reptiles":
                            speciesDetaileds = (DataSupport.where("singal=?", "" + singal).find(Reptiles.class)).get(0);
                            Message messages = new Message();
                            messages.what = 1;
                            handler.sendMessage(messages);
                            break;
                    }


                }
            });
            speciesDetailedRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 1, 1.0f));
            requestQueue.add(speciesDetailedRequest);
        }

    }

    @Override
    public void finish() {
        super.finish();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

    }

    public void download(final String path, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            FileUtils fileUtils = new FileUtils();
                            fileOutputStream = new FileOutputStream(fileUtils.createFile(fileName));//指定文件保存路径，代码看下一步
                            byte[] buf = new byte[1024];
                            int ch;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        Message message = new Message();
                        message.what = LOAD_AUDIO_OK;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public class FileUtils {
        private String path = Environment.getExternalStorageDirectory().toString() + "/blackstone/audio";

        public FileUtils() {
            File file = new File(path);
            /**
             *如果文件夹不存在就创建
             */
            if (!file.exists()) {
                file.mkdirs();
            }
        }

        /**
         * 创建一个文件
         *
         * @param FileName 文件名
         * @return
         */
        public File createFile(String FileName) {
            return new File(path, FileName);
        }
    }
}
