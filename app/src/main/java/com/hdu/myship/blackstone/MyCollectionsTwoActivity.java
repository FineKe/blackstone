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
   // private MyAdapter adapter;
    private int HEAD=0;
    private int ITEM=1;

    private List<String> indexList;
    private List<SpeciesClassActivity.Result>resultList;
    private SpeciesContentAdapter speciesContentAdapter;
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
        resultList=new ArrayList<>();
        indexList=new ArrayList<>();
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
        //adapter=new MyAdapter(this,speciesList);
       // recyclerView.setAdapter(adapter);
        speciesContentAdapter=new SpeciesContentAdapter(this,resultList);
        recyclerView.setAdapter(speciesContentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
//        //adapter.setOnRecyclerViewItemClickeListener(new onItemClickLisenter() {
//            @Override
//            public void onItemClick(View view, Species data) {
//                Intent intent=new Intent(MyCollectionsTwoActivity.this,SpeciesDeatailedActivity.class);
//                intent.putExtra("singal",data.getSingal());
//                intent.putExtra("speciesType",data.getSpeciesType());
//                startActivity(intent);
//            }
//        });
        speciesContentAdapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
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

    public void createIndexlist(List<Species> speciesList) {
        /*
        if (speciesList.size() > 1) {
            for (int i = 0, j = 0; i < speciesList.size() - 1; i++, j++) {
                if (i == 0) {
                    SpeciesClassActivity.Result result = new SpeciesClassActivity.Result();
                    result.setHead(speciesList.get(0).getOrder());
                    result.setViewType(HEAD);
                    result.setLatinHead(speciesList.get(0).getLatinOrder());
                    resultList.add(result);
                }

                if (!speciesList.get(i).getOrder().equals(speciesList.get(i + 1).getOrder())) {
                    SpeciesClassActivity.Result result = new SpeciesClassActivity.Result();
                    result.setHead(speciesList.get(i + 1).getOrder());
                    result.setViewType(HEAD);
                    result.setLatinHead(speciesList.get(i + 1).getLatinOrder());
                    System.out.println(speciesList.get(i + 1).getOrder());
                    SpeciesClassActivity.Result result_ = new SpeciesClassActivity.Result();
                    result_.setViewType(ITEM);
                    result_.setSpecies(speciesList.get(i));
                    resultList.add(result_);
                    resultList.add(result);
                } else {

                    SpeciesClassActivity.Result result = new SpeciesClassActivity.Result();
                    result.setViewType(ITEM);
                    result.setSpecies(speciesList.get(i));
                    resultList.add(result);
                }

            }

        }
        else
        {
            for (int i = 0, j = 0; i < speciesList.size(); i++, j++) {
                SpeciesClassActivity.Result result=new SpeciesClassActivity.Result();
                result.setHead(speciesList.get(i).getOrder());
                result.setViewType(HEAD);
                result.setLatinHead(speciesList.get(i).getLatinOrder());
                resultList.add(result);
                SpeciesClassActivity.Result result1=new SpeciesClassActivity.Result();
                result1.setViewType(ITEM);
                result1.setSpecies(speciesList.get(i));
                resultList.add(result1);
            }
        }*/

        if (speciesList.size() > 1) {
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
                            SpeciesClassActivity.Result result = new SpeciesClassActivity.Result();
                            result.setViewType(HEAD);
                            result.setLatinHead(speciesList.get(i).getLatinOrder());
                            result.setHead(speciesList.get(i).getOrder());
                            resultList.add(result);
//                            positionList.add(j);
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
            resultList.add(result);

            SpeciesClassActivity.Result result1 = new SpeciesClassActivity.Result();
            result1.setViewType(ITEM);
            result1.setSpecies(speciesList.get(0));
            resultList.add(result1);
        }
    }
}
