package ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hdu.myship.blackstone.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vo.SlideMenuVO;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/5 上午12:08
 */
public class SlideMenuAdapter extends BaseAdapter{

    private List<SlideMenuVO> menus;


    public SlideMenuAdapter(List<SlideMenuVO> menus) {
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int i) {
        return menus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_item_view_slide_menu, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) view.getTag();
            viewHolder.title.setText(menus.get(i).getTitle());
            Glide.with(viewGroup.getContext()).load(menus.get(i).getDrawable()).into(viewHolder.icon);
        }

        return view;
    }

    public static class ViewHolder {

        @BindView(R.id.iv_icon_item_view_slide_menu_main_activity)
        ImageView icon;

        @BindView(R.id.tv_title_item_view_slide_menu_main_activity)
        TextView title;

        public ViewHolder(View item) {
            ButterKnife.bind(this,item);
        }
    }

}
