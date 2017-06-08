package com.hdu.myship.blackstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import database.Species;

public class MyCollectionsTwoActivity extends AppCompatActivity {
    private String TAG="MyCollectionsTwoActivity";
    private LinearLayout actionBack;
    private RecyclerView recyclerView;
    private TextView title;
    private List<Species> speciesList;
    private int position;
    private SpeciesContentAdapter adapter;
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
        speciesList=new ArrayList<>();
        for(Species species:specieslist)
        {
            speciesList.add(DataSupport.where("singal=?",species.getSingal()+"").find(Species.class).get(0));
        }
        adapter=new SpeciesContentAdapter(this,null);
    }

    private void initViews() {
        actionBack= (LinearLayout) findViewById(R.id.activity_my_collection_two_linear_layout_action_back);
        recyclerView= (RecyclerView) findViewById(R.id.activity_my_collection_two_recycler_view);
        title= (TextView) findViewById(R.id.activity_my_collection_text_view_title);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
            @Override
            public void onItemClick(View view, Species data) {//添加点击事件

                Intent intent=new Intent(MyCollectionsTwoActivity.this,SpeciesDeatailedActivity.class);
                intent.putExtra("singal",data.getSingal());
                intent.putExtra("speciesType",data.getSpeciesType());
//                loadDeatailed(getApplicationContext(),data.getSpeciesType(),data.getSingal());
                //intent.putExtra("speciesTypeChineseName",typeTitle[position]);
                startActivity(intent);
            }
        });
    }
}
