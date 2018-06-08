package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
import com.kefan.blackstone.BaseActivity;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecordTwoActivity extends BaseActivity implements View.OnClickListener{
    private String getRecordDeatailedURL= APIManager.BASE_URL +"v1/record/";
    private RequestQueue requestQueue;
    private JsonObjectRequest getRcordDeatailedRquest;
    private String TAG="MyRecordTwoActivity";

    private TextView date;
    private TextView alter;
    private LinearLayout actionBack;
    private ExpandableListView expandableListView;
    public static List<List<Note>> noteList;
    private List<String>father;
    private RecordDeatailedAdapter recordDeatailedAdapter;
    private  int recordId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_record_two);
        initData();
        initViews();
        initEvents();
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        alter.setOnClickListener(this);
    }

    private void initViews() {
        date= (TextView) findViewById(R.id.activity_myrecord_two_text_view_date);
        alter= (TextView) findViewById(R.id.activity_myrecord_two_text_view_alter);
        actionBack= (LinearLayout) findViewById(R.id.activity_myrecord_two_linear_layout_action_back);
        Date da=new Date(getIntent().getLongExtra("time",0));
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        String mdate=format.format(da);
        Log.d(TAG, "onBindViewHolder:"+da.toString() );
        date.setText(mdate.substring(0,4)+"年"+mdate.substring(5,7)+"月"+mdate.substring(8,10)+"日");
        expandableListView= (ExpandableListView) findViewById(R.id.activity_myrecord_two_expand_list_view);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(recordDeatailedAdapter);
    }

    private void initData() {
        recordId=getIntent().getIntExtra("recordId",0);
        requestQueue= Volley.newRequestQueue(this);
        noteList=new ArrayList<>();
        father=new ArrayList<>();
        recordDeatailedAdapter=new RecordDeatailedAdapter(this, (ArrayList<String>) father,noteList);
        Log.d(TAG, "initData: "+recordId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_myrecord_two_text_view_alter:
                alter();
                break;
            case R.id.activity_myrecord_two_linear_layout_action_back:
                actionBack();
                break;
        }
    }

//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        super.startActivityForResult(intent, requestCode);
//        noteList.clear();
//        father.clear();
//        loadingNotes();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        noteList.clear();
        father.clear();
        loadingNotes();
    }

    private void actionBack() {
        this.finish();
    }

    private void alter() {
        Intent intent=new Intent(this,RecordAlterActivity.class);
        intent.putExtra("time",getIntent().getLongExtra("time",0));
        intent.putExtra("recordId",recordId);
        startActivityForResult(intent,1);
    }

    public class RecordDeatailedAdapter extends BaseExpandableListAdapter
    {
        private List<String> fathers;
        private List<List<Note>> childs;
        private Context context;
        private LayoutInflater layoutInflater;

        public RecordDeatailedAdapter(Context context,ArrayList<String> fathers, List<List<Note>> childs ) {
            this.fathers = fathers;
            this.childs = childs;
            this.context = context;
            layoutInflater=LayoutInflater.from(context);
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
            convertView=layoutInflater.inflate(R.layout.activity_myrecord_two_expan_list_view_header,null);
            title= (TextView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_header_text_view_title);
            arrow= (ImageView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_header_image_view_arrow);
            title.setText(fathers.get(groupPosition));
            if (isExpanded)
            {
                arrow.setRotation(90);
            }else
            {
                arrow.setRotation(0);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView chineseName;
            TextView family;
            TextView remark;
            convertView=layoutInflater.inflate(R.layout.activity_myrecord_two_expand_list_view_item,null);
            chineseName= (TextView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_item_text_view_chineseName);
            family= (TextView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_item_text_view_family);
            remark= (TextView) convertView.findViewById(R.id.activity_myrecord_two_expand_list_view_item_text_view_remark);
            remark.setVisibility(View.VISIBLE);
            Note note=childs.get(groupPosition).get(childPosition);
            chineseName.setText(note.getChineseName());
            if(note.getFamily().equals(""))
            {
                family.setText(note.getChineseName());
            }
            else
            {
                family.setText(note.getFamily());
            }
            remark.setText(note.getRemark());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public void loadingNotes()
    {   final List<Note> amphibiaNotes=new ArrayList<>();
        final List<Note> reptilesNotes=new ArrayList<>();
        final List<Note> birdNotes=new ArrayList<>();
        final List<Note> insectNotes=new ArrayList<>();
        final UserInformationUtil userInformation=new UserInformationUtil(this);
        getRcordDeatailedRquest=new JsonObjectRequest(Request.Method.GET, getRecordDeatailedURL + recordId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int code=jsonObject.getInt("code");
                            if(code==88)
                            {
                                JSONObject data=jsonObject.getJSONObject("data");
                                JSONArray notes=data.getJSONArray("notes");
                                recordId=data.getInt("id");
                                for(int i=0;i<notes.length();i++)
                                {
                                    JSONObject object=notes.getJSONObject(i);
                                    JSONObject species=object.getJSONObject("species");
                                    String speciesType=species.getString("speciesType");
                                    switch (speciesType)
                                    {
                                        case "amphibia":
                                            Note note=new Note();
                                            note.setId(data.getInt("id"));
                                            note.setUserId(data.getInt("userId"));
                                            note.setTime(data.getLong("time"));
                                            note.setLat(data.getDouble("lat"));
                                            note.setLon(data.getDouble("lon"));
                                            note.setNoteId(object.getInt("id"));
                                            note.setChineseName(species.getString("chineseName"));
                                            note.setLatinName(species.getString("latinName"));
                                            if(species.has("family"))
                                            {
                                                note.setFamily(species.getString("family"));
                                            }
                                            else
                                            {
                                                note.setFamily("");
                                            }
                                            note.setRemark(object.getString("remark"));
                                            note.setSpeciesType(species.getString("speciesType"));
                                            note.setSpeciesId(species.getInt("id"));
                                            amphibiaNotes.add(note);
                                            break;
                                        case "reptiles":
                                            Note rnote=new Note();
                                            rnote.setId(data.getInt("id"));
                                            rnote.setUserId(data.getInt("userId"));
                                            rnote.setTime(data.getLong("time"));
                                            rnote.setLat(data.getDouble("lat"));
                                            rnote.setLon(data.getDouble("lon"));
                                            rnote.setNoteId(object.getInt("id"));
                                            rnote.setChineseName(species.getString("chineseName"));
                                            rnote.setLatinName(species.getString("latinName"));
                                            if(species.has("family"))
                                            {
                                                rnote.setFamily(species.getString("family"));
                                            }
                                            else
                                            {
                                                rnote.setFamily("");
                                            }
                                            rnote.setRemark(object.getString("remark"));
                                            rnote.setSpeciesType(species.getString("speciesType"));
                                            rnote.setSpeciesId(species.getInt("id"));
                                            reptilesNotes.add(rnote);
                                            break;
                                        case "bird":
                                            Note bnote=new Note();
                                            bnote.setId(data.getInt("id"));
                                            bnote.setUserId(data.getInt("userId"));
                                            bnote.setTime(data.getLong("time"));
                                            bnote.setLat(data.getDouble("lat"));
                                            bnote.setLon(data.getDouble("lon"));
                                            bnote.setNoteId(object.getInt("id"));
                                            bnote.setChineseName(species.getString("chineseName"));
                                            bnote.setLatinName(species.getString("latinName"));
                                            if(species.has("family"))
                                            {
                                                bnote.setFamily(species.getString("family"));
                                            }
                                            else
                                            {
                                                bnote.setFamily("");
                                            }
                                            bnote.setRemark(object.getString("remark"));
                                            bnote.setSpeciesType(species.getString("speciesType"));
                                            bnote.setSpeciesId(species.getInt("id"));
                                            birdNotes.add(bnote);
                                            break;
                                        case "insect":
                                            Note inote=new Note();
                                            inote.setId(data.getInt("id"));
                                            inote.setUserId(data.getInt("userId"));
                                            inote.setTime(data.getLong("time"));
                                            inote.setLat(data.getDouble("lat"));
                                            inote.setLon(data.getDouble("lon"));
                                            inote.setNoteId(object.getInt("id"));
                                            inote.setChineseName(species.getString("chineseName"));
                                            inote.setLatinName(species.getString("latinName"));
                                            if(species.has("family"))
                                            {
                                                inote.setFamily(species.getString("family"));
                                            }
                                            else
                                            {
                                                inote.setFamily("");
                                            }
                                            inote.setRemark(object.getString("remark"));
                                            inote.setSpeciesType(species.getString("speciesType"));
                                            inote.setSpeciesId(species.getInt("id"));
                                            insectNotes.add(inote);
                                            break;
                                    }
                                }

                                if(amphibiaNotes.size()>0)
                                {
                                    father.add("两栖类"+amphibiaNotes.size()+"种");
                                    noteList.add(amphibiaNotes);
                                }
                                if(reptilesNotes.size()>0)
                                {
                                    father.add("爬行类"+reptilesNotes.size()+"种");
                                    noteList.add(reptilesNotes);
                                }
                                if(birdNotes.size()>0)
                                {
                                    father.add("鸟类"+birdNotes.size()+"种");
                                    noteList.add(birdNotes);
                                }
                                if(insectNotes.size()>0)
                                {
                                    father.add("昆虫"+insectNotes.size()+"个目");
                                    noteList.add(insectNotes);
                                }
                                recordDeatailedAdapter.notifyDataSetChanged();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i=0; i<recordDeatailedAdapter.getGroupCount(); i++) {
                                            expandableListView.expandGroup(i);
                                        }
                                    }
                                });

                            }else
                            {
                                String message=jsonObject.getString("message");
                                Toast.makeText(MyRecordTwoActivity.this,message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyRecordTwoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                UpdateToken updateToken=new UpdateToken(MyRecordTwoActivity.this);
                updateToken.updateToken();
                Map<String,String> headers=new HashMap<>();
                headers.put("token",userInformation.getToken());
                return headers;
            }
        };
        requestQueue.add(getRcordDeatailedRquest);
    }



    public class Note
    {
        private int id;//记录id
        private int userId;//用户id
        private long time;
        private double lat;
        private double lon;
        private int noteId;//笔记id
        private int speciesId;//物种id
        private String chineseName;
        private String latinName;
        private String family;
        private String remark;//笔记内容
        private String speciesType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public int getNoteId() {
            return noteId;
        }

        public void setNoteId(int noteId) {
            this.noteId = noteId;
        }

        public int getSpeciesId() {
            return speciesId;
        }

        public void setSpeciesId(int speciesId) {
            this.speciesId = speciesId;
        }

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getLatinName() {
            return latinName;
        }

        public void setLatinName(String latinName) {
            this.latinName = latinName;
        }

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSpeciesType() {
            return speciesType;
        }

        public void setSpeciesType(String speciesType) {
            this.speciesType = speciesType;
        }
    }

}
