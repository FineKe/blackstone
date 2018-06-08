package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kefan.blackstone.BaseActivity;
import com.kefan.blackstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JavaBean.APIManager;
import database.Species;

public class MyCollectionsActivity extends BaseActivity {
    private final int LOAD_DATA_OK=0;
    private final int JUMP_OK=1;
    private String getCollectionURL= APIManager.BASE_URL +"v1/species/collection/";
    private RequestQueue requestQueue;
    private JsonObjectRequest getCollectionRequest;
    private ListView listView;
    private LinearLayout actionBack;
    private MyAdapter listAdapter;
    public static List<SpeciesClass> speciesClassList;//该list声明为静态，是为了与下一级界面共享该数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_collections);
        initData();
        initViews();
        initEvents();
    }

    private void initData() {
        speciesClassList=new ArrayList<>();
        listAdapter=new MyAdapter(this,speciesClassList);
        getCollection();
    }


    private void initViews() {
        listView= (ListView) findViewById(R.id.activity_my_collection_list_view);
//        listView.setDivider(getDrawable(R.drawable.line));
        listView.setDivider(getResources().getDrawable(R.drawable.line));
        actionBack= (LinearLayout) findViewById(R.id.activity_my_collection_linear_layout_action_back);
        listView.setAdapter(listAdapter);

        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initEvents() {

    }
    public class MyAdapter extends BaseAdapter {
        private Context context;
        private List<SpeciesClass> list;
        private LayoutInflater layoutInflater;
        public MyAdapter(Context context,List<SpeciesClass> list) {
            this.list = list;
            this.context=context;
            layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView=layoutInflater.inflate(R.layout.activity_my_collection_list_view_item,null);
            LinearLayout item= (LinearLayout) convertView.findViewById(R.id.activity_my_collection_list_view_item_linear_layout_item);
            TextView speciesClassName= (TextView) convertView.findViewById(R.id.activity_my_collection_list_view_item_text_view_species_class_name);
            TextView speciesClassCount= (TextView) convertView.findViewById(R.id.activity_my_collection_list_view_item_text_view_species_count);
            speciesClassCount.setText("共"+list.get(position).getCount()+"种");
            speciesClassName.setText(list.get(position).getName());
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(context,MyCollectionsTwoActivity.class).putExtra("position",position),JUMP_OK);//跳转到下一级界面，并夹带参数position
                }
            });

            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                speciesClassList.clear();
                getCollection();
                listAdapter.notifyDataSetChanged();


    }

    /**
     *
     */
    public static class SpeciesClass
    {
        private int count;//多少种
        private String name;//名字，这里我没有赋值
        private String speciesType;
        private List<Species> list;//这个是主要数据，下一级界面用的就是这个list

        public SpeciesClass(String speciesType,List<Species> list,int count) {
            this.speciesType = speciesType;
            this.list = list;
            this.count=count;

        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpeciesType() {
            return speciesType;
        }

        public void setSpeciesType(String speciesType) {
            this.speciesType = speciesType;
        }

        public List<Species> getList() {
            return list;
        }

        public void setList(List<Species> list) {
            this.list = list;
        }
    }

    public void getCollection()
    {
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
                                    Log.d("sssssssssssss", "onResponse: "+icount);
                                    Species ispecies=new Species();
                                    ispecies.setSingal(object.getInt("id"));
                                    insect.add(ispecies);
                                    break;
                            }
                        }
                        if(acount!=0)
                        {
                            SpeciesClass a=new SpeciesClass("amphibia",amphibia,acount);
                            a.setName("两栖类");
                            speciesClassList.add(a);
                        }
                        if(rcount!=0)
                        {
                            SpeciesClass r=new SpeciesClass("reptiles",reptiles,rcount);
                            r.setName("爬行类");
                            speciesClassList.add(r);
                        }
                        if(bcount!=0)
                        {
                            SpeciesClass b=new SpeciesClass("bird",bird,bcount);
                            b.setName("鸟类");
                            speciesClassList.add(b);
                        }
                        Log.d("zx", "onResponse: "+icount);
                        if(icount!=0)
                        {
                            Log.d("zx", "onResponse: "+icount);
                            SpeciesClass in=new SpeciesClass("insect",insect,icount);
                            Log.d("zx", "onResponse: "+icount);
                            in.setName("昆虫目");
                            speciesClassList.add(in);
                        }
                        Log.d("list", "onResponse: "+speciesClassList.size());
                        Message message=new Message();
                        message.what=LOAD_DATA_OK;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyCollectionsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                    listAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
