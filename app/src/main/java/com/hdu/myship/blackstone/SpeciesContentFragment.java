package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import database.Species;

/**
 * Created by MY SHIP on 2017/3/24.
 * 物种内容的fragment
 */

public class SpeciesContentFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RecyclerView species_content_recyclerView;
    private SliderBar sliderBar;

    private ImageButton img_btn_back;
    private ImageButton img_btn_alert_pick;
    private ImageButton img_btn_alert_menu;
    private Dialog alertMenu;
    private LinearLayout back_linearLayout;

    public List<Species> getList() {
        return list;
    }

    public void setList(List<Species> list) {
        this.list = list;
    }

    private List<Species> list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.species_content, container, false);//将布局映射到该fragment

        img_btn_alert_menu= (ImageButton) view.findViewById(R.id.img_btn_alert_menu);
        species_content_recyclerView = (RecyclerView) view.findViewById(R.id.species_class_recyclerView_speciesContent);//绑定组件
        sliderBar= (SliderBar) view.findViewById(R.id.silde);
        species_content_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        species_content_recyclerView.setHasFixedSize(true);//设置固定高度，提高效率
        SpeciesContentAdapter adapter=new SpeciesContentAdapter(list,getContext());//创建一个适配器，并设置数据

        species_content_recyclerView.setAdapter(adapter);//给recyclerview设置适配器
        adapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
            @Override
            public void onItemClick(View view, Species data) {//添加点击事件
                //transaction.replace(R.id.frame_layout,SpeciesDetailedFragment.newInstance(data.getSingl())).addToBackStack(null).commit();
                Toast.makeText(getContext(), data.getChineseName(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onItemClick: "+data.getSingal());
            }
        });
//        species_content_recyclerView.addItemDecoration(new SpeciesItemDecoration(getContext()));
        sliderBar.setData(getResources().getStringArray(R.array.sorting_by_order));//索引栏设置数据
        sliderBar.setGravity(Gravity.CENTER_VERTICAL);//设置位置
        sliderBar.setCharacterListener(new SliderBar.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {//添加监听事件
                Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
            }
        });
        img_btn_alert_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showAlert(getContext());
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getActivity().getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();
    }

    /**
     * 显示一个按目浏览和按科浏览的对话框
     * @param context
     */
    private void showAlert(final Context context)
    {
        alertMenu=new Dialog(context);
        View  alertMenuView=LayoutInflater.from(context).inflate(R.layout.alert_menu,null);
        Button button1= (Button) alertMenuView.findViewById(R.id.browse_by_section);
        Button button2= (Button) alertMenuView.findViewById(R.id.browse_by_order);

        alertMenu.setContentView(alertMenuView);
        Window window=alertMenu.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.x=0;
        layoutParams.y=0;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.alpha=9f;
        window.setAttributes(layoutParams);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"按目浏览",Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"按科浏览",Toast.LENGTH_SHORT).show();
            }
        });
        alertMenu.show();
    }

}
