package com.hdu.myship.blackstone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDataLoadProvider;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import ShapeUtil.GlideRoundTransform;
import database.Species;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by MY SHIP on 2017/3/24.
 */

public class SpeciesContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private String dealPicure="?imageView2/0/w/400/h/400";//本平台的图片采用了cdn分发，为了节省流量，采用七牛云的api处理接口
    private  List<Species>  list;
  //  private  List<String> indexList;
    //private  List<result> resultList;
    private int HEAD=0;
    private int ITEM=1;
    private Context context;

    private OnRecyclerViewItemClickeListener onRecyclerViewItemClickeListener=null;
    public SpeciesContentAdapter(List<Species> list, Context context ) {
        this.list=list;
        //this.indexList=indexList;
        this.context=context;
        //resultList=hanleList();
    }

   /* private List<result>hanleList()
    {
        List<result> resultList=new ArrayList<>();
        int count;
        for(String index:indexList)
        {
            result mHresu=new result();
            mHresu.setViewType(HEAD);
            mHresu.setHead(index);
            resultList.add(mHresu);
            for(Species species:list)
            {
                if(species.getOrder().substring(0,1).equals(index))
                {
                    result r=new result();
                    r.setViewType(ITEM);
                    r.setSpecies(species);
                    resultList.add(r);
                }
            }
        }
        for(result l:resultList)
        {
            System.out.println(l.getHead());
        }

        return resultList;
    }*/



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* if(viewType==HEAD)
        {
            return new HeadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.species_content_recycler_view_head,parent,false));
        }

        else{*/
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.species_content_recycler_view_item, parent, false);
            view.setOnClickListener(this);
            MyHolder holder = new MyHolder(view);
            return holder;
       // }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if(holder instanceof HeadHolder)
//        {
//            HeadHolder headHolder= (HeadHolder) holder;
//            headHolder.orderOrFamilyChinaeseName.setText(resultList.get(position).getHead()+"形目");
//            //headHolder.orderOrFamilyLatinName.setText(resultList.get(position+1).getSpecies().getLatinName());
//        }else if(holder instanceof MyHolder)
//        {
            MyHolder myHolder= (MyHolder) holder;
            myHolder.tv_englishName.setText(list.get(position).getLatinName());
            Glide.with(context).load(list.get(position).getMainPhoto()+dealPicure).placeholder(R.mipmap.mainphoto_loading).transform(new GlideRoundTransform(context,8)).into(myHolder.imageView);
            myHolder.tv_chineseName.setText(list.get(position).getChineseName());
            myHolder.itemView.setTag(list.get(position));
       // }

    }

//   @Override
//    public void onBindViewHolder(final MyHolder holder, int position) {
//
//        Species species=list.get(position);//从数据库中读取当前位置的物种信息
//        Glide.with(context).load(species.getMainPhoto()+dealPicure).placeholder(R.mipmap.mainphoto_loading).transform(new GlideRoundTransform(context,8)).into(holder.imageView);//使用glide框架加载图片到imageview
//        holder.tv_latinName.setText(species.getLatinName());
//        holder.tv_chineseName.setText(species.getChineseName());
//        holder.tv_englishName.setText(species.getFamily());
//        holder.itemView.setTag(list.get(position));
//
//
//    }

    @Override
    public int getItemCount() {
        return list.size();
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

    class HeadHolder extends RecyclerView.ViewHolder
    {
        private TextView orderOrFamilyChinaeseName;
        private TextView orderOrFamilyLatinName;
        public HeadHolder(View itemView) {
            super(itemView);
            orderOrFamilyChinaeseName= (TextView) itemView.findViewById(R.id.species_content_recyclerView_head_orderOrFamily_chineseName);
            orderOrFamilyLatinName= (TextView) itemView.findViewById(R.id.species_content_recyclerView_head_orderOrFamily_latinName);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static interface OnRecyclerViewItemClickeListener
    {
        void onItemClick(View view, result data);
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClickeListener != null) {
            //注意这里使用getTag方法获取数据
            onRecyclerViewItemClickeListener.onItemClick(v,(result) v.getTag());
        }
    }

    public void setOnRecyclerViewItemClickeListener(OnRecyclerViewItemClickeListener listener)
    {
        this.onRecyclerViewItemClickeListener=listener;
    }

    class result
    {
        int viewType;
        String head;
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

        public Species getSpecies() {
            return species;
        }

        public void setSpecies(Species species) {
            this.species = species;
        }
    }
}
