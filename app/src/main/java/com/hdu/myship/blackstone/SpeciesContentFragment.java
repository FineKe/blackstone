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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import JsonUtil.JsonResolverList;
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

    private Dialog alertMenu;
    private boolean flag=false;

    private ImageButton alertMenuImg;
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
        alertMenuImg= (ImageButton) view.findViewById(R.id.img_alert_menu);
        species_content_recyclerView = (RecyclerView) view.findViewById(R.id.spcies_content_recyclerView);//绑定组件
        sliderBar= (SliderBar) view.findViewById(R.id.silde);
        species_content_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        species_content_recyclerView.setHasFixedSize(true);//设置固定高度，提高效率
        SpeciesContentAdapter adapter=new SpeciesContentAdapter(list,getContext());//创建一个适配器，并设置数据
//        species_content_recyclerView.addOnItemTouchListener();
        species_content_recyclerView.setAdapter(adapter);//给recyclerview设置适配器
        adapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
            @Override
            public void onItemClick(View view, Species data) {//添加点击事件
                Toast.makeText(getContext(),data.getChineseName(),Toast.LENGTH_SHORT).show();
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
        alertMenuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==false)
                {
                    flag=true;
                    showAlert(getContext());
                }else
                {
                    flag=false;
                    dismissAlert();
                }
            }
        });
        return view;
    }

    private void showAlert(Context context)
    {
        alertMenu=new Dialog(context);
        View  alertMenuView=LayoutInflater.from(context).inflate(R.layout.alert_menu,null);
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
        alertMenu.show();
    }

    private void dismissAlert()
    {
        alertMenu.dismiss();
    }

}
