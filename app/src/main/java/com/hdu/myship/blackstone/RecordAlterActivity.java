package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.Record;
import database.Species;

import static com.hdu.myship.blackstone.MyRecordTwoActivity.noteList;

public class RecordAlterActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG="RecordAlterActivity";
    private static final int CREATE_OK = 1;
    private TextView date;
    private TextView save;
    private DatePicker datePicker;
    private ExpandableListView expandableListView;
    private LinearLayout actionBack;

    public static List<List<Record>> recordList;
    private MyExpandListViewAdapter adapter;

    private boolean datePickerShow = false;
    private Long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_record_alter);
        initData();
        initViews();
        initEvents();
    }

    private void initData() {
        time=getIntent().getLongExtra("time",0);
        recordList=new ArrayList<>();
        adapter=new MyExpandListViewAdapter(this,recordList);
        createBaseRecord();
    }

    private void initViews() {
        date= (TextView) findViewById(R.id.activity_record_alter_text_view_date);
        save= (TextView) findViewById(R.id.activity_record_alter_text_view_save);
        actionBack= (LinearLayout) findViewById(R.id.activity_record_alter_linear_layout_action_back);
        datePicker= (DatePicker) findViewById(R.id.activity_record_alter_datepicker);
        expandableListView= (ExpandableListView) findViewById(R.id.activity_record_alter_expandListView);
        expandableListView.setAdapter(adapter);

        Date da=new Date(getIntent().getLongExtra("time",0));
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        String mdate=format.format(da);
        Log.d(TAG, "onBindViewHolder:"+da.toString() );
        date.setText(mdate.substring(0,4)+"年"+mdate.substring(5,7)+"月"+mdate.substring(8,10)+"日");
        datePicker.init(Integer.parseInt(mdate.substring(0, 4)), Integer.parseInt(mdate.substring(5, 7))-1, Integer.parseInt(mdate.substring(8, 10)), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
            }
        });
    }

    private void initEvents() {
        date.setOnClickListener(this);
        actionBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_record_alter_text_view_date:
                showDataPicker();
                break;
            case R.id.activity_record_alter_linear_layout_action_back:
                actionBack();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
    }

    private void actionBack() {
        this.finish();
    }

    private void showDataPicker() {
        if (datePickerShow == false) {
            datePicker.setVisibility(View.VISIBLE);
            datePickerShow = true;
        } else {
            datePicker.setVisibility(View.GONE);
            datePickerShow = false;
        }
    }

    class MyExpandListViewAdapter extends BaseExpandableListAdapter {
        private String[] groups = {"鸟类", "两栖类", "爬行类", "昆虫"};
        private List<List<Record>> records;
        private Context context;
        private LayoutInflater layoutInflater;

        public MyExpandListViewAdapter(Context context,List<List<Record>> records) {
            this.context=context;
            this.records = records;
            layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return records.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return records.get(groupPosition).get(childPosition);
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
                convertView = layoutInflater.inflate(R.layout.expand_list_view_father, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.expand_lisetView_father_speciesClassChineseName);
            ImageView im = (ImageView) convertView.findViewById(R.id.expand_lisetView_father_image_view_arrow);
            if (isExpanded) {
                im.setRotation(90);
            } else

            {
                im.setRotation(0);
            }
            textView.setText(groups[groupPosition]);
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.expand_list_view_child, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.expand_listView_child_speciesChineseName);
            textView.setText(records.get(groupPosition).get(childPosition).getChineseName());
            final ImageView addNotes = (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_addNotes);
            final ImageView collection = (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_collection);
            if (!records.get(groupPosition).get(childPosition).isChecked()) {
                collection.setImageResource(R.mipmap.select_normal);
            } else {
                collection.setImageResource(R.mipmap.select_pressed);
            }

            if (records.get(groupPosition).get(childPosition).isRemarkIsNull()) {
                addNotes.setImageResource(R.mipmap.pen_normal);
            } else {
                addNotes.setImageResource(R.mipmap.pen_pressed);
            }

            addNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // GROUPPOSITION = groupPosition;
                    //CHILDPOSITION = childPosition;
                    startActivityForResult(new Intent(context, AddNotesActivity.class)
                            .putExtra("groupPosition", groupPosition).putExtra("childPosition", childPosition), 1);
                }
            });


            collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!records.get(groupPosition).get(childPosition).isChecked()) {
                        records.get(groupPosition).get(childPosition).setChecked(true);
                        records.get(groupPosition).get(childPosition).save();
                        collection.setImageResource(R.mipmap.select_pressed);
                    } else {
                        records.get(groupPosition).get(childPosition).setChecked(false);
                        records.get(groupPosition).get(childPosition).save();
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
    }


    public void createBaseRecord()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataSupport.deleteAll(Record.class);
                List<Species> list=DataSupport.findAll(Species.class);
                for(Species species:list)
                {
                    Record record=new Record(species.getChineseName(),species.getSingal(),species.getSpeciesType());
                    for(List<MyRecordTwoActivity.Note> notes:noteList)
                    {
                        for(MyRecordTwoActivity.Note note:notes)
                        {
                            if(note.getSpeciesId()==species.getSingal())
                            {
                                System.out.println(note.getSpeciesId());
                                record.setRemarkIsNull(false);
                                record.setChecked(true);
                                record.setRemark(note.getRemark());
                                System.out.println(note.getRemark());
                            }
                        }
                    }
                    record.save();
                }

                List<Record>birdRecord = DataSupport.where("speciesType=?", "bird").find(Record.class);
                List<Record>amphibiaRecord = DataSupport.where("speciesType=?", "amphibia").find(Record.class);
                List<Record>reptilesRecord = DataSupport.where("speciesType=?", "reptiles").find(Record.class);
                List<Record>insectRecord = DataSupport.where("speciesType=?", "insect").find(Record.class);

                System.out.println("ssssssssssssssssss");
                recordList.add(birdRecord);
                recordList.add(amphibiaRecord);
                recordList.add(reptilesRecord);
                recordList.add(insectRecord);

                Message message=new Message();
                message.what=CREATE_OK;
                handler.sendMessage(message);
            }
        }).start();


    }

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case CREATE_OK:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
