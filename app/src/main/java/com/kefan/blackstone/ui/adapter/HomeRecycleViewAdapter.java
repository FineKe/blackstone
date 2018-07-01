package com.kefan.blackstone.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.IntentFieldConstant;
import com.kefan.blackstone.model.SpeciesClass;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.ui.activity.SpeciesClassActivity;
import com.kefan.blackstone.vo.MainVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/15 下午10:15
 */
public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.ViewHolder> {

    private Context context;

    private List<MainVo.CategoriesBean> categoriesBeans;

    public HomeRecycleViewAdapter(Context context, List<MainVo.CategoriesBean> categoriesBeans) {
        this.context = context;
        this.categoriesBeans = categoriesBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_recycle_view_home_fragment,null,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MainVo.CategoriesBean categoriesBean=categoriesBeans.get(position);

        //加载头像
        Glide.with(context).load(categoriesBean.getImg()).into(holder.icon);

        //显示文本
        holder.speciesName.setText(categoriesBean.getName());

        final int pos=position;

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(context, SpeciesClassActivity.class);
                    intent.putExtra(IntentFieldConstant.SPEICES_CLASS_ID, pos);
                    intent.putExtra(IntentFieldConstant.SPECIES_TYPE, categoriesBean.getSpeciesType());
                    context.startActivity(intent);
                ((AppCompatActivity) context).overridePendingTransition(R.anim.in,R.anim.in);


            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesBeans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon_item_view_recycle_view_home_fragment)
        public ImageView icon;

        @BindView(R.id.tv_species_name_item_view_recycle_view_home_fragment)
        public TextView speciesName;

        @BindView(R.id.ll_species_item_view_recycle_view_home_fragment)
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
