package com.hdu.myship.blackstone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by MY SHIP on 2017/3/18.
 * 添加记录fragment
 */

public class AddRecordFragment extends Fragment{
    private ExpandableListView expandableListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_record,container,false);
        String[][]child={{"1","2","3","4"},{"1","2","3","4"},{"1","2","3","4"},{"1","2","3","4"}};
        expandableListView= (ExpandableListView) view.findViewById(R.id.add_record_expandListView);
        expandableListView.setAdapter(new MyExpandAdapter(child));
        return view;
    }

    class MyExpandAdapter extends BaseExpandableListAdapter
    {   String father[]={"鸟类","两栖类","爬行类","昆虫类"};
        String child[][];

        public MyExpandAdapter(String[][] child) {
            this.child = child;
        }

        @Override
        public int getGroupCount() {
            return father.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return child[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return father[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView speciesClassChineseName;
            if (convertView==null)
            {
                convertView = getLayoutInflater(null).inflate(R.layout.expand_list_view_father,null);
            }
            speciesClassChineseName= (TextView) convertView.findViewById(R.id.expand_lisetView_father_speciesClassChineseName);
            speciesClassChineseName.setText(father[groupPosition]);
            return convertView ;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView speciesChineseName;
            if (convertView==null)
            {
                convertView=getLayoutInflater(null).inflate(R.layout.expand_list_view_child,null);
            }
            speciesChineseName= (TextView) convertView.findViewById(R.id.expand_listView_child_speciesChineseName);
            speciesChineseName.setText(child[groupPosition][childPosition]);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
