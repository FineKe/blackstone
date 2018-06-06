package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import JavaBean.APIManager;
import database.SpeciesClasses;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MY SHIP on 2017/3/18.
 */

public class SpeciesFragment extends Fragment{
    private final int OK=1;
    private String getCategoryURL= APIManager.BASE_URL +"v1/species/categories";
    private StickyListHeadersListView speciesClassListView;
    private List<SpeciesClasses>speciesClassesList;
    private StickyListViewAdapter stickyListViewAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String categoryFile="category";
    private String speciesType[]={"amphibia","reptiles","bird"};
    private boolean isLoadedCategory=false;
    boolean flag=true;
    private ImageView searchView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.species,container,false);
        speciesClassListView= (StickyListHeadersListView) view.findViewById(R.id.StickyListHeadersListView_species_list_view);
        searchView= (ImageView) view.findViewById(R.id.species_title_image_view_search_view);
        speciesClassesList=new ArrayList<>();
        stickyListViewAdapter=new StickyListViewAdapter(speciesClassesList,getContext());
        speciesClassListView.setAdapter(stickyListViewAdapter);
        speciesClassListView.setDivider(null);
        speciesClassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<=2)
                {
                    startActivity(new Intent(getContext(),SpeciesClassActivity.class).putExtra("speciesType",speciesType[position]).putExtra("position",position));
                }else
                {
                    startActivity(new Intent(getContext(),SpeciesClassActivity.class).putExtra("speciesClassName",speciesClassesList.get(position).getClassesName()).putExtra("position",position));
                }
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),SearchActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(DataSupport.findAll(SpeciesClasses.class).size()==0)
        {
            createClassList();
        }
        else
        {
            speciesClassesList.clear();
            speciesClassesList.addAll(DataSupport.findAll(SpeciesClasses.class));
            stickyListViewAdapter.notifyDataSetChanged();
        }
    }

    private void createClassList() {

//        /**
//         * 将R.mipmap.species的图片ID封装到数组
//         */
//        int []speciesPicturesId={R.mipmap.species1,R.mipmap.species2,R.mipmap.species3,R.mipmap.species4,R.mipmap.species5,R.mipmap.species6,
//                R.mipmap.species7,R.mipmap.species8,R.mipmap.species9,R.mipmap.species10,R.mipmap.species11,R.mipmap.species12,
//                R.mipmap.species13,R.mipmap.species14,R.mipmap.species15,R.mipmap.species16,R.mipmap.species17,R.mipmap.species18,R.mipmap.species19};
//        int i=0;
//        for(String string:getResources().getStringArray(R.array.vertebrate))//创建脊椎动物类列表
//        {
//
//            SpeciesClasses speciesClasses=new SpeciesClasses(0,"脊椎动物",string,speciesPicturesId[i]);
//            speciesClassesList.add(speciesClasses);
//            i++;
//        }
//
//        for(String string:getResources().getStringArray(R.array.invertebrate))//创建无脊椎动物类列表
//        {
//
//            SpeciesClasses speciesClasses=new SpeciesClasses(1,"无脊椎动物",string,speciesPicturesId[i]);
//            speciesClassesList.add(speciesClasses);
//            i++;
//        }
            GetCategory();

    }

    private class StickyListViewAdapter extends BaseAdapter implements StickyListHeadersAdapter
    {
        private List<SpeciesClasses> list;
        private LayoutInflater inflater;

        public StickyListViewAdapter(List<SpeciesClasses> list, Context context) {
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            convertView=inflater.inflate(R.layout.species_classes_list_view_header,parent,false);
            TextView title= (TextView) convertView.findViewById(R.id.species_classes_header_text_view_classes_title);
            title.setText(list.get(position).getTitle());
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            return list.get(position).getFlag();
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=inflater.inflate(R.layout.species_classes_list_view_item,parent,false);
            ImageView picture= (ImageView) convertView.findViewById(R.id.species_classes_item_image_view_class_picture);
            TextView className= (TextView) convertView.findViewById(R.id.species_classes_item_text_view_class_name);
            Glide.with(getContext()).load(list.get(position).getMainPhoto()).into(picture);
            className.setText(list.get(position).getClassesName());
            View line=convertView.findViewById(R.id.fragment_species_recycler_view_item_line);
            if(position==2)
            {
                line.setVisibility(View.GONE);
            }
            return convertView;
        }
    }


    public void GetCategory()
    {   speciesClassesList.clear();
        DataSupport.deleteAll(SpeciesClasses.class);
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, getCategoryURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        JSONObject data=jsonObject.getJSONObject("data");
                        JSONArray invertebrate=data.getJSONArray("无脊椎动物");
                        JSONArray vertebrate=data.getJSONArray("脊椎动物");

                        for(int i=0;i<vertebrate.length();i++)
                        {   JSONObject object=vertebrate.getJSONObject(i);
                            SpeciesClasses speciesClasses=new SpeciesClasses();
                            speciesClasses.setFlag(0);
                            speciesClasses.setTitle("脊椎动物");
                            speciesClasses.setClassesName(object.getString("name"));
                            speciesClasses.setMainPhoto(object.getString("img"));
                            speciesClasses.save();
                            //speciesClassesList.add(speciesClasses);
                        }

                        for(int i=0;i<invertebrate.length();i++)
                        {   JSONObject object=invertebrate.getJSONObject(i);
                            SpeciesClasses speciesClasses=new SpeciesClasses();
                            speciesClasses.setFlag(1);
                            speciesClasses.setTitle("无脊椎动物");
                            speciesClasses.setClassesName(object.getString("name"));
                            speciesClasses.setMainPhoto(object.getString("img"));
                            speciesClasses.save();
                            //speciesClassesList.add(speciesClasses);
                        }
                        Message message=new Message();
                        message.what=OK;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case OK:
                    speciesClassesList.addAll(DataSupport.findAll(SpeciesClasses.class));
                    stickyListViewAdapter.notifyDataSetChanged();
            }
        }
    };
}
