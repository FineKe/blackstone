package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDataLoadProvider;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import JavaBean.SpeciesClasses;
import database.Species;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MY SHIP on 2017/3/18.
 */

public class SpeciesFragment extends Fragment{
    private StickyListHeadersListView speciesClassListView;
    private List<SpeciesClasses>speciesClassesList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.species,container,false);
        speciesClassListView= (StickyListHeadersListView) view.findViewById(R.id.StickyListHeadersListView_species_list_view);
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createClassList();
    }

    private void createClassList() {
        speciesClassesList=new ArrayList<>();
        for(String string:getResources().getStringArray(R.array.vertebrate))//创建脊椎动物类列表
        {
            SpeciesClasses speciesClasses=new SpeciesClasses(0,"脊椎动物",string);
            speciesClassesList.add(speciesClasses);
        }

        for(String string:getResources().getStringArray(R.array.invertebrate))//创建无脊椎动物类列表
        {
            SpeciesClasses speciesClasses=new SpeciesClasses(1,"无脊椎动物",string);
            speciesClassesList.add(speciesClasses);
        }
    }

    private class StickyListViewAdapter extends BaseAdapter implements StickyListHeadersAdapter
    {
        private List<SpeciesClasses> list;
        private LayoutInflater inflater;

        public StickyListViewAdapter(List<SpeciesClasses> list, Context context) {
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            convertView=inflater.inflate(R.layout.species_classes_list_view_header,parent,false);
            TextView title= (TextView) convertView.findViewById(R.id.species_classes_header_text_view_classes_title);
            title.setText(list.get(position).getTitle());
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            return list.get(position).getId();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=inflater.inflate(R.layout.species_classes_list_view_item,parent,false);
            ImageView picture= (ImageView) convertView.findViewById(R.id.species_classes_item_image_view_class_picture);
            TextView className= (TextView) convertView.findViewById(R.id.species_classes_item_text_view_class_name);

            className.setText(list.get(position).getClassName());
            return convertView;
        }
    }


}
