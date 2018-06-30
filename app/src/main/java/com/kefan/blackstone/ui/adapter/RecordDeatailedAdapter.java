package com.kefan.blackstone.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kefan.blackstone.R;
import com.kefan.blackstone.database.Note;

import java.util.List;

public class RecordDeatailedAdapter extends BaseExpandableListAdapter {

        private List<String> fathers;
        private List<List<Note>> childs;
        private Context context;
        private LayoutInflater layoutInflater;

        public RecordDeatailedAdapter(Context context, List<String> fathers, List<List<Note>> childs) {
            this.fathers = fathers;
            this.childs = childs;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return fathers.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childs.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return fathers.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childs.get(groupPosition).get(childPosition);
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
            TextView title;
            ImageView arrow;
            convertView = layoutInflater.inflate(R.layout.activity_myrecord_two_expan_list_view_header, null);
            title = (TextView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_header_text_view_title);
            arrow = (ImageView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_header_image_view_arrow);
            title.setText(fathers.get(groupPosition));
            if (isExpanded) {
                arrow.setRotation(90);
            } else {
                arrow.setRotation(0);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView chineseName;
            TextView family;
            TextView remark;
            convertView = layoutInflater.inflate(R.layout.activity_myrecord_two_expand_list_view_item, null);
            chineseName = (TextView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_item_text_view_chineseName);
            family = (TextView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_item_text_view_family);
            remark = (TextView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_item_text_view_remark);

            Note note = childs.get(groupPosition).get(childPosition);
            chineseName.setText(note.getChineseName());
            family.setText(note.getFamily());
            if (note.getRemark() != null) {

                remark.setText(note.getRemark());
                remark.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }