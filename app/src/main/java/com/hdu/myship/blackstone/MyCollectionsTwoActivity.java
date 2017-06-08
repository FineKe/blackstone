package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import ShapeUtil.GlideRoundTransform;
import database.Species;

public class MyCollectionsTwoActivity extends AppCompatActivity {
    private String dealPicure="?imageView2/0/w/400/h/400";//本平台的图片采用了cdn分发，为了节省流量，采用七牛云的api处理接口
    private String TAG="MyCollectionsTwoActivity";
    private LinearLayout actionBack;
    private RecyclerView recyclerView;
    private TextView title;
    private List<Species> speciesList;
    private int position;
    private MyAdapter adapter;

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

    }

    private void initViews() {
        actionBack= (LinearLayout) findViewById(R.id.activity_my_collection_two_linear_layout_action_back);
        recyclerView= (RecyclerView) findViewById(R.id.activity_my_collection_two_recycler_view);
        title= (TextView) findViewById(R.id.activity_my_collection_text_view_title);
        adapter=new MyAdapter(this,speciesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter.setOnRecyclerViewItemClickeListener(new onItemClickLisenter() {
            @Override
            public void onItemClick(View view, Species data) {
                Intent intent=new Intent(MyCollectionsTwoActivity.this,SpeciesDeatailedActivity.class);
                intent.putExtra("singal",data.getSingal());
                intent.putExtra("speciesType",data.getSpeciesType());
                startActivity(intent);
            }
        });
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
           View view = LayoutInflater.from(context).inflate(R.layout.species_content_recycler_view_item, viewGroup, false);
           view.setOnClickListener(this);
           MyHolder holder = new MyHolder(viewGroup);
           return holder;
       }

       @Override
       public void onBindViewHolder(MyHolder myHolder, int i) {
           myHolder.tv_englishName.setText(speciesList.get(i).getLatinName());
           Glide.with(context).load(speciesList.get(i).getMainPhoto()+dealPicure).placeholder(R.mipmap.loading_small).transform(new GlideRoundTransform(context,8)).into(myHolder.imageView);
           myHolder.tv_chineseName.setText(speciesList.get(i).getChineseName());
           myHolder.itemView.setTag(speciesList.get(i));
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
        // private ImageView imageView;
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
}
