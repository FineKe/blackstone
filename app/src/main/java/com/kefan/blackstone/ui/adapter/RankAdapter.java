package com.kefan.blackstone.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ShapeUtil.GlideRoundTransform;
import com.kefan.blackstone.model.Rank;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/24 下午1:55
 */
public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<Rank> rankList;

    private UserService userService;

    private Context context;

    public RankAdapter(Context context,List<Rank> rankList) {

        this.context = context;
        this.rankList = rankList;
        userService=new UserServiceImpl();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_recycle_view_rank_activity,null,false);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Rank rank = rankList.get(position);

        holder.rank.setText(rank.getRank()+"");

        //如果头像地址不为null 则加载头像
        if (rank.getUser().getAvatar() != null) {

            Glide.with(context).load(rank.getUser().getAvatar()).transform(new GlideRoundTransform(context,50)).into(holder.icon);

        }

        holder.username.setText(rank.getUser().getName());

        holder.score.setText("答题"+rank.getTotalCount()+"道");

        BigDecimal bigDecimal=BigDecimal.valueOf(rank.getRatio()*100);
        float ratio = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();

        holder.ratio.setText(ratio+"正确率");

        //如果是自己
        if (userService.getUser().getId() == rank.getUser().getId()) {
            holder.splitLine.setVisibility(View.INVISIBLE);
            holder.root.setBackgroundColor(context.getResources().getColor(R.color.global_bg_clolor));
            holder.rank.setTextColor(Color.WHITE);
            holder.username.setTextColor(Color.WHITE);
            holder.score.setTextColor(Color.WHITE);
            holder.ratio.setTextColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_root_item_view_recycle_view_rank_activity)
        RelativeLayout root;
        @BindView(R.id.tv_rank_item_view_recycle_view_rank_activity)
        TextView rank;
        @BindView(R.id.iv_icon_item_view_recycle_view_rank_activity)
        ImageView icon;
        @BindView(R.id.tv_username_item_view_recycle_view_rank_activity)
        TextView username;
        @BindView(R.id.tv_score_item_view_recycle_view_rank_activity)
        TextView score;
        @BindView(R.id.tv_ratio_item_view_recycle_view_rank_activity)
        TextView ratio;
        @BindView(R.id.v_spilt_line_item_view_recycle_view_rank_activity)
        View splitLine;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

