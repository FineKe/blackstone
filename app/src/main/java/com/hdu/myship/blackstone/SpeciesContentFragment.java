package com.hdu.myship.blackstone;

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
import android.widget.ArrayAdapter;
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
 */

public class SpeciesContentFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RecyclerView species_content_recyclerView;
    private SliderBar sliderBar;
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
        View view = inflater.inflate(R.layout.species_content, container, false);
        species_content_recyclerView = (RecyclerView) view.findViewById(R.id.spcies_content_recyclerView);
        sliderBar= (SliderBar) view.findViewById(R.id.silde);
        species_content_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        species_content_recyclerView.setHasFixedSize(true);
        SpeciesContentAdapter adapter=new SpeciesContentAdapter(list,getContext());
//        species_content_recyclerView.addOnItemTouchListener();
        species_content_recyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickeListener(new SpeciesContentAdapter.OnRecyclerViewItemClickeListener() {
            @Override
            public void onItemClick(View view, Species data) {
                Toast.makeText(getContext(),data.getChineseName(),Toast.LENGTH_SHORT).show();
            }
        });
//        species_content_recyclerView.addItemDecoration(new SpeciesItemDecoration(getContext()));
        sliderBar.setData(getResources().getStringArray(R.array.sorting_by_order));
        sliderBar.setGravity(Gravity.CENTER_VERTICAL);
        sliderBar.setCharacterListener(new SliderBar.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clickArrow() {

            }
        });
        return view;
    }


}
