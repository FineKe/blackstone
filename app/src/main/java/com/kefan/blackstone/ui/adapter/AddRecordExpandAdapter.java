package com.kefan.blackstone.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kefan.blackstone.R;
import com.kefan.blackstone.database.NoteTemplate;
import com.kefan.blackstone.listener.addNoteOnclickListener;

import java.util.List;

public class AddRecordExpandAdapter extends BaseExpandableListAdapter {


    private List<String> group;


    private List<List<NoteTemplate>> noteTemplates;

    private addNoteOnclickListener addNoteListener;

    public AddRecordExpandAdapter(List<String> group, List<List<NoteTemplate>> noteTemplates) {
        this.group = group;
        this.noteTemplates = noteTemplates;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return noteTemplates.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return noteTemplates.get(groupPosition).get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expand_list_view_father, null, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.expand_lisetView_father_speciesClassChineseName);
        ImageView im = (ImageView) convertView.findViewById(R.id.expand_lisetView_father_image_view_arrow);

        textView.setText(group.get(groupPosition));

        if (isExpanded) {
            im.setRotation(90);
        } else

        {
            im.setRotation(0);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expand_list_view_child, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.expand_listView_child_speciesChineseName);

        final ImageView addNotes = (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_addNotes);

        final ImageView collection = (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_collection);

        textView.setText(noteTemplates.get(groupPosition).get(childPosition).getSpeciesName());
        if (!noteTemplates.get(groupPosition).get(childPosition).isChekced()) {
            collection.setImageResource(R.mipmap.select_normal);
        } else {
            collection.setImageResource(R.mipmap.select_pressed);
        }

        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addNoteListener != null) {
                    addNoteListener.onClick(v,groupPosition,childPosition,noteTemplates.get(groupPosition).get(childPosition));
                }
            }
        });


        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noteTemplates.get(groupPosition).get(childPosition).isChekced()) {

                    noteTemplates.get(groupPosition).get(childPosition).setChekced(true);
                    collection.setImageResource(R.mipmap.select_pressed);

                } else {

                    noteTemplates.get(groupPosition).get(childPosition).setChekced(false);
                    collection.setImageResource(R.mipmap.select_normal);

                }


            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return false;

    }

    public void setAddNoteOnclickListener(addNoteOnclickListener listener) {

        this.addNoteListener=listener;

    }

    /**
     * 设置笔记 并且自动勾选
     * @param groupPos
     * @param childPos
     * @param remark
     */
    public void setRemark(int groupPos,int childPos,String remark) {

        NoteTemplate noteTemplate=noteTemplates.get(groupPos).get(childPos);
        noteTemplate.setRemark(remark);
        noteTemplate.setChekced(true);
        this.notifyDataSetChanged();
    }



}
