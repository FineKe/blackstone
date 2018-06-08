package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JavaBean.APIManager;
import ShapeUtil.GlideRoundTransform;
import database.Species;

public class MyCollectionsTwoActivity extends BaseActivity implements View.OnClickListener{
    private final int LOAD_DATA_OK=0;
    private final int JUMP_OK=1;
    private String dealPicure="?imageView2/0/w/400/h/400";//本平台的图片采用了cdn分发，为了节省流量，采用七牛云的api处理接口
    private String TAG="MyCollectionsTwoActivity";
    private LinearLayout actionBack;
    private RecyclerView recyclerView;
    private TextView title;
    private int position;
    private SliderBar sliderBar;
   // private MyAdapter adapter;
    private int HEAD=0;
    private int ITEM=1;

    private String getCollectionURL= APIManager.BASE_URL +"v1/species/collection/";
    private RequestQueue requestQueue;
    private JsonObjectRequest getCollectionRequest;
    public  List<MyCollectionsActivity.SpeciesClass> speciesClassList;
    private List<String> indexList;

    private SpeciesContentAdapter speciesContentAdapter;
    private List<Species> speciesList;
    private List<Integer> positionList;
    private List<SpeciesClassActivity.Result> resultList;
    private int oldSize;
    private int nowSize;

    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_collections_two);
        initData();
        initViews();
    }

    private void initData() {
        position=getIntent().getIntExtra("position",0);//取出position
        List<Species> specieslist=MyCollectionsActivity.speciesClassList.get(position).getList();//这里从上一个activity中取list
        oldSize=MyCollectionsActivity.speciesClassList.size();
        speciesList=new ArrayList<>();
        resultList=new ArrayList<>();
        indexList=new ArrayList<>();
        positionList=new ArrayList<>();
        speciesClassList=new ArrayList<>();
        for(Species species:specieslist)
        {
            System.out.println(species.getSingal());
            speciesList.add(DataSupport.where("singal=?",species.getSingal()+"").find(Species.class).get(0));
        }
        for(Species species:speciesList)
        {
            System.out.println(species.toString());
        }
        createIndexlist(speciesList);

    }

    private void initViews() {
        actionBack= (LinearLayout) findViewById(R.id.activity_my_collection_two_linear_layout_action_back);
        recyclerView= (RecyclerView) findViewById(R.id.activity_my_collection_two_recycler_view);
        title= (TextView) findViewById(R.id.activity_my_collection_text_view_title);
        sliderBar= (SliderBar) findViewById(R.id.activity_my_collection_two_slider_bar);
        sliderBar.setData(indexList,positionList);

        linearLayoutManager=new LinearLayoutManager(this);

        speciesContentAdapter=new SpeciesContentAdapter(this,resultList);
        recyclerView.setAdapter(speciesContentAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sliderBar.setCharacterListener(new SliderBar.CharacterClickListener() {
            @Override
            public void clickCharacter(View view) {
                linearLayoutManager.scrollToPositionWithOffset((Integer) view.getTag(),0);
            }
        });
        speciesContentAdapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
            @Override
            public void onItemClick(View view, Species data) {
                Intent intent=new Intent(MyCollectionsTwoActivity.this,SpeciesDeatailedActivity.class);
                intent.putExtra("singal",data.getSingal());
                intent.putExtra("speciesType",data.getSpeciesType());
                startActivityForResult(intent,1);
            }
        });

        actionBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_my_collection_two_linear_layout_action_back:
                actionBack();
                break;
        }
    }

    private void actionBack() {
        finish();
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder>implements View.OnClickListener
   {    List<Species> speciesList;
        Context context;
        onItemClickLisenter onItemClickLisenter=null;
       public MyAdapter(Context context,List<Species> speciesList) {
           this.speciesList = speciesList;
           this.context = context;
       }

       @Override
       public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
           View view = LayoutInflater.from(context).inflate(R.layout.species_content_recycler_view_item, null, false);
           view.setOnClickListener(this);
           MyHolder holder = new MyHolder(view);
           return holder;
       }

       @Override
       public void onBindViewHolder(MyHolder myHolder, int i) {
           myHolder.tv_englishName.setText(speciesList.get(i).getLatinName());
           Glide.with(context).load(speciesList.get(i).getMainPhoto()+dealPicure).placeholder(R.mipmap.loading_small).transform(new GlideRoundTransform(context,8)).into(myHolder.imageView);
           myHolder.tv_chineseName.setText(speciesList.get(i).getChineseName());
           myHolder.itemView.setTag(speciesList.get(i).getEnglishName());
       }

       @Override
       public int getItemCount() {
           return speciesList.size();
       }

       @Override
       public void onClick(View v) {
            if(onItemClickLisenter!=null)
            {
                onItemClickLisenter.onItemClick(v,(Species)v.getTag());
            }
       }

       public void setOnRecyclerViewItemClickeListener(onItemClickLisenter listener)
       {
           this.onItemClickLisenter=listener;
       }

   }

    class MyHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_latinName,tv_chineseName,tv_englishName;
        public RoundedImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView= (RoundedImageView) itemView.findViewById(R.id.species_picture);
            tv_chineseName= (TextView) itemView.findViewById(R.id.tv_chineseName);
            tv_latinName= (TextView) itemView.findViewById(R.id.tv_latinName);
            tv_englishName= (TextView) itemView.findViewById(R.id.tv_englishName);


        }
    }
    public interface onItemClickLisenter
    {
        void onItemClick(View view, Species data);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getCollection();
    }

    public void createIndexlist(List<Species> speciesList) {
        if (speciesList.size() > 1) {
            for (int i = 0; i < speciesList.size() - 1; i++) {
                if (i == 0) {
                    indexList.add(speciesList.get(0).getOrder().substring(0, 1));
                }
                if (!speciesList.get(i).getOrder().equals(speciesList.get(i + 1).getOrder())) {
                    indexList.add(speciesList.get(i + 1).getOrder().substring(0, 1));
                    System.out.println(speciesList.get(i).getOrder() + ":" + speciesList.get(i + 1).getOrder());

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
                            SpeciesClassActivity.Result result = new SpeciesClassActivity.Result();
                            result.setViewType(HEAD);
                            result.setLatinHead(speciesList.get(i).getLatinOrder());
                            result.setHead(speciesList.get(i).getOrder());
                            resultList.add(result);
                            positionList.add(j);
                            j++;
                        }
                        SpeciesClassActivity.Result result = new SpeciesClassActivity.Result();
                        result.setViewType(ITEM);
                        result.setSpecies(speciesList.get(i));
                        resultList.add(result);
                        j++;
                        k++;
                    }
                }
            }
        }else
        {
            SpeciesClassActivity.Result result = new SpeciesClassActivity.Result();
            result.setViewType(HEAD);
            result.setLatinHead(speciesList.get(0).getLatinOrder());
            result.setHead(speciesList.get(0).getOrder());
            positionList.add(0);
            resultList.add(result);

            SpeciesClassActivity.Result result1 = new SpeciesClassActivity.Result();
            result1.setViewType(ITEM);
            result1.setSpecies(speciesList.get(0));
            resultList.add(result1);
            indexList.add(speciesList.get(0).getOrder().substring(0, 1));
        }
    }
    public void getCollection()
    {   speciesClassList.clear();
        requestQueue= Volley.newRequestQueue(this);
        UpdateToken updateToken=new UpdateToken(this);
        updateToken.updateToken();
        final UserInformationUtil userInformationUtil=new UserInformationUtil(this);
        getCollectionRequest=new JsonObjectRequest(Request.Method.GET, getCollectionURL + userInformationUtil.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        List<Species> amphibia=new ArrayList<>();
                        List<Species> reptiles=new ArrayList<>();
                        List<Species> bird=new ArrayList<>();
                        List<Species> insect=new ArrayList<>();

                        int acount=0;
                        int bcount=0;
                        int rcount=0;
                        int icount=0;
                        JSONArray data=jsonObject.getJSONArray("data");
                        for(int i=0;i<data.length();i++)
                        {
                            JSONObject object=data.getJSONObject(i);
                            switch (object.getString("speciesType"))
                            {
                                case "amphibia":
                                    acount++;
                                    Species aspecies=new Species();
                                    aspecies.setSingal(object.getInt("id"));
                                    amphibia.add(aspecies);
                                    break;
                                case "reptiles":
                                    rcount++;
                                    Species rspecies=new Species();
                                    rspecies.setSingal(object.getInt("id"));
                                    reptiles.add(rspecies);
                                    break;
                                case "bird":
                                    bcount++;
                                    Species bspecies=new Species();
                                    bspecies.setSingal(object.getInt("id"));
                                    bird.add(bspecies);
                                    break;
                                case "insect":
                                    icount++;
                                    Species ispecies=new Species();
                                    ispecies.setSingal(object.getInt("id"));
                                    insect.add(ispecies);
                                    break;
                            }
                        }
                        if(acount!=0)
                        {
                            MyCollectionsActivity.SpeciesClass a=new MyCollectionsActivity.SpeciesClass("amphibia",amphibia,acount);
                            a.setName("两栖类");
                            speciesClassList.add(a);
                        }
                        if(rcount!=0)
                        {
                            MyCollectionsActivity.SpeciesClass r=new MyCollectionsActivity.SpeciesClass("reptiles",reptiles,rcount);
                            r.setName("爬行类");
                            speciesClassList.add(r);
                        }
                        if(bcount!=0)
                        {
                            MyCollectionsActivity.SpeciesClass b=new MyCollectionsActivity.SpeciesClass("bird",bird,bcount);
                            b.setName("鸟类");
                            speciesClassList.add(b);
                        }
                        Log.d("zx", "onResponse: "+icount);
                        if(icount!=0)
                        {
                            Log.d("zx", "onResponse: "+icount);
                            MyCollectionsActivity.SpeciesClass in=new MyCollectionsActivity.SpeciesClass("insect",insect,icount);
                            Log.d("zx", "onResponse: "+icount);
                            in.setName("昆虫目");
                            speciesClassList.add(in);
                        }
                        nowSize=speciesClassList.size();
                        if(oldSize>nowSize)
                        {
                            finish();
                        }else
                        {
                            Log.d("list", "onResponse: "+speciesClassList.size());
                            Message message=new Message();
                            message.what=LOAD_DATA_OK;
                            handler.sendMessage(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyCollectionsTwoActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> hearder=new HashMap<>();
                hearder.put("token",userInformationUtil.getToken());
                return hearder;
            }
        };
        requestQueue.add(getCollectionRequest);
    }

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case LOAD_DATA_OK:
                    speciesList.clear();
                    List<Species> specieslist=speciesClassList.get(position).getList();
                    for(Species species:specieslist)
                    {
                        System.out.println(species.getSingal());
                        speciesList.add(DataSupport.where("singal=?",species.getSingal()+"").find(Species.class).get(0));
                    }
                    for(Species species:speciesList)
                    {
                        System.out.println(species.toString());
                    }

                    resultList.clear();
                    positionList.clear();
                    indexList.clear();
                    createIndexlist(speciesList);
                    speciesContentAdapter.notifyDataSetChanged();
                    sliderBar.setData(indexList,positionList);
                    break;
            }
        }
    };
}
