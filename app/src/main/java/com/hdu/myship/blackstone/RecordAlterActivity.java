package com.hdu.myship.blackstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JavaBean.APIManager;
import database.AlterRecord;
import database.Species;

import static com.hdu.myship.blackstone.BlackStoneApplication.getContext;

public class RecordAlterActivity extends BaseActivity implements View.OnClickListener{
    private String alterRecordURL= APIManager.rootDoname+"v1/record/edit";
    private String TAG="RecordAlterActivity";
    private static final int CREATE_OK = 1;
    private TextView date;
    private TextView save;
    private DatePicker datePicker;
    private ExpandableListView expandableListView;
    private LinearLayout actionBack;

    public static List<List<AlterRecord>> recordList;
    private MyExpandListViewAdapter adapter;
    private ProgressDialog progressDialog;
    private boolean datePickerShow = false;
    private Long time;

    private int year_;
    private int month_;
    private int day_;
    private Long millisecond;


    private List<List<MyRecordTwoActivity.Note>> noteList;
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
        noteList=MyRecordTwoActivity.noteList;
        adapter=new MyExpandListViewAdapter(this,recordList);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("请稍后...");
        progressDialog.setCancelable(false);
        createBaseRecord();
    }



    private void initViews() {
        date= (TextView) findViewById(R.id.activity_record_alter_text_view_date);
        save= (TextView) findViewById(R.id.activity_record_alter_text_view_save);
        actionBack= (LinearLayout) findViewById(R.id.activity_record_alter_linear_layout_action_back);
        datePicker= (DatePicker) findViewById(R.id.activity_record_alter_datepicker);

        expandableListView= (ExpandableListView) findViewById(R.id.activity_record_alter_expandListView);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);

        Date da=new Date(getIntent().getLongExtra("time",0));
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        String mdate=format.format(da);
        Log.d(TAG, "onBindViewHolder:"+da.toString() );
        date.setText(mdate.substring(0,4)+"年"+mdate.substring(5,7)+"月"+mdate.substring(8,10)+"日");
        year_=Integer.parseInt(mdate.substring(0, 4));
        month_=Integer.parseInt(mdate.substring(5, 7));
        day_=Integer.parseInt(mdate.substring(8, 10));
        datePicker.init(Integer.parseInt(mdate.substring(0, 4)), Integer.parseInt(mdate.substring(5, 7))-1, Integer.parseInt(mdate.substring(8, 10)), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                year_=year;
                month_=monthOfYear;
                day_=dayOfMonth;
                date.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
            }
        });
    }

    private void initEvents() {
        date.setOnClickListener(this);
        actionBack.setOnClickListener(this);
        save.setOnClickListener(this);
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
            case R.id.activity_record_alter_text_view_save:
                save();
                break;
        }
    }

    private void save() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Long second = format.parse(year_ + "-" + month_ + "-" + day_).getTime();
            System.out.println(second);
            millisecond = second;
            Log.d(TAG, "save: "+millisecond);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        upLoadData(millisecond);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();

    }

    public void resetRecords() {//重置记录

        for(List<AlterRecord> records:recordList)
        {
            for(AlterRecord record:records)
            {
                record.setChecked(false);
                record.setRemarkIsNull(true);
                record.setRemark("");
            }
        }
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
        private String[] groups = {"昆虫", "两栖类", "爬行类", "鸟类"};
        private List<List<AlterRecord>> records;
        private Context context;
        private LayoutInflater layoutInflater;

        public MyExpandListViewAdapter(Context context,List<List<AlterRecord>> records) {
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
                    startActivityForResult(new Intent(context, AlterNotesActivity.class)
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
    {   progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataSupport.deleteAll(AlterRecord.class);
                List<Species> list=DataSupport.findAll(Species.class);
                for(Species species:list)
                {
                    AlterRecord record=new AlterRecord(species.getChineseName(),species.getSingal(),species.getSpeciesType());
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

                List<AlterRecord>birdRecord = DataSupport.where("speciesType=?", "bird").find(AlterRecord.class);
                List<AlterRecord>amphibiaRecord = DataSupport.where("speciesType=?", "amphibia").find(AlterRecord.class);
                List<AlterRecord>reptilesRecord = DataSupport.where("speciesType=?", "reptiles").find(AlterRecord.class);
                List<AlterRecord>insectRecord = DataSupport.where("speciesType=?", "insect").find(AlterRecord.class);

//                System.out.println("ssssssssssssssssss");
                recordList.add(insectRecord);
                recordList.add(amphibiaRecord);
                recordList.add(reptilesRecord);
                recordList.add(birdRecord);

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
                    progressDialog.dismiss();
                    break;
            }
        }
    };


    private void upLoadData(Long milliseconds) {
       RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",getIntent().getIntExtra("recordId",0) );
            jsonObject.put("time",milliseconds);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i <4; i++) {
                for (AlterRecord record :recordList.get(i)) {
                    if (record.isChecked()) {
                        JSONObject js = new JSONObject();
                        js.put("speciesId", record.getSpeciesId());
                        js.put("remark", record.getRemark());
                        jsonArray.put(js);
                    }
                }
            }
            jsonObject.put("notes", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, alterRecordURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code = jsonObject.getInt("code");
                    if (code == 88) {
                        System.out.println(code);
                        Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                        resetRecords();
                    } else {
                        String message = jsonObject.getString("message");
                        System.out.println(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                UpdateToken updateToken=new UpdateToken(getContext());
                updateToken.updateToken();
                UserInformationUtil userInformationUtil=new UserInformationUtil(getContext());
                headers.put("token", userInformationUtil.getToken());
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
