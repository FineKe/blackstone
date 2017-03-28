package com.hdu.myship.blackstone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import database.Species;

/**
 * Created by MY SHIP on 2017/3/24.
 */

public class SpeciesContentAdapter extends RecyclerView.Adapter<SpeciesContentAdapter.MyHolder> {
    private String dealPicure="?imageView2/1/w/200/h/200";
    private  List<Species>  list=new ArrayList<>();
    private Context context;
    public SpeciesContentAdapter(List<Species> list, Context context ) {
        this.list=list;
        this.context=context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyHolder holder=new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.species_content_recycler_view_item,parent,false));
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.species_content_recycler_view_item,parent,false);
        MyHolder holder=new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        Species species=list.get(position);
        Glide.with(context).load(species.getMainPhoto()+dealPicure).placeholder(R.drawable.loading).crossFade().override(100,100).fitCenter().into(holder.im);
//        Glide.with(context).load(species.getMainPhoto()).into(holder.im);
        holder.tv_latinName.setText(species.getLatinName());
        holder.tv_chineseName.setText(species.getChineseName());
        holder.tv_mo.setText(species.getFamily());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_latinName,tv_chineseName,tv_mo;
        private ImageView im;
        public MyHolder(View itemView) {
            super(itemView);
            im= (ImageView) itemView.findViewById(R.id.species_picture);
            tv_chineseName= (TextView) itemView.findViewById(R.id.tv_chineseName);
            tv_latinName= (TextView) itemView.findViewById(R.id.tv_latinName);
            tv_mo= (TextView) itemView.findViewById(R.id.tv_mo);
        }
    }


}
