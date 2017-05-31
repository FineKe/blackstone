package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.litepal.crud.DataSupport;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import database.Species;

public class MyCollectionsActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter listAdapter;
    public static List<SpeciesClass> list;//该list声明为静态，是为了与下一级界面共享该数据
    private String[] speciesType={"amphibia","reptiles","bird","insect"};
    private String[] speciesTypeChineseName={"两栖类","爬行类","鸟类","昆虫"};

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
        list=new ArrayList<>();
        for(String s:speciesType)
        {
            List<Species> temp=DataSupport.where("speciesType=?",s).find(Species.class);
            list.add(new SpeciesClass(s,temp));
        }

        listAdapter=new MyAdapter(this,list);
    }

    private void initViews() {
        listView= (ListView) findViewById(R.id.activity_my_collection_list_view);
        listView.setAdapter(listAdapter);


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

            speciesClassName.setText(speciesTypeChineseName[position]);
            speciesClassCount.setText("共"+list.get(position).getCount()+"种");

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context,MyCollectionsTwoActivity.class).putExtra("position",position));//跳转到下一级界面，并夹带参数position
                }
            });

            return convertView;
        }
    }

    /**
     *
     */
    public class SpeciesClass
    {
        private int count;//多少种
        private String name;//名字，这里我没有赋值
        private String speciesType;
        private List<Species> list;//这个是主要数据，下一级界面用的就是这个list

        public SpeciesClass(String speciesType,List<Species> list) {
            this.speciesType = speciesType;
            this.list = list;
            count=list.size();

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

}
