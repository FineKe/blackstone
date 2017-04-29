package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDataLoadProvider;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import database.Species;

/**
 * Created by MY SHIP on 2017/3/18.
 */

public class SpeciesFragment extends Fragment{
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private  ListView listView_speciesClass;



    private BootstrapEditText searchView;
    private List<String> listName;
    private String[] Type={"bird","amphibia","reptiles","insect"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.from(container.getContext()).inflate(R.layout.species,null,false);

        //searchView= (BootstrapEditText) view.findViewById(R.id.searchView);
        listView_speciesClass= (ListView) view.findViewById(R.id.listView_speciesClass);
        listView_speciesClass.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,listName));
        listView_speciesClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Species> speciesType=new ArrayList<Species>();
                speciesType=DataSupport.where("speciesType = ?",Type[position]).find(Species.class);//从数据库中获取相应position的物种数据粗放到speciesType
                fragmentManager=getActivity().getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();//开启一个事列
                SpeciesContentFragment speciesContentFragment=new SpeciesContentFragment();
                speciesContentFragment.setList(speciesType);//设置物种数据
                transaction.replace(R.id.frame_layout,speciesContentFragment).addToBackStack(null).commit();//切换到相应的fragment
            }
        });

       //showAlert(container.getContext());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listName=new ArrayList<>();
        listName.add("鸟类");
        listName.add("两栖类");
        listName.add("爬行类");
        listName.add("昆虫类");


    }



}
