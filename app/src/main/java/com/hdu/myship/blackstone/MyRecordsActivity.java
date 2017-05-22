package com.hdu.myship.blackstone;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyRecordsActivity extends AppCompatActivity {
    private String RecordListURL="http://api.blackstone.ebirdnote.cn/v1/record/personalStat/";//记录列表接口
    private RequestQueue requestQueue;
    private JsonObjectRequest getRecordListRequest;
    private ItemRemoveRecordRecycle removeRecordRecycleView;
    private List<Record> recordList;
    private ItemRemoveRcordAdapter itemRemoveRcordAdapter;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";
    private String token;
    private Long userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_records);
        initData();
        initView();
    }

    private void initData() {
        userInformationSharedPreferences=getSharedPreferences(userInformation, MODE_PRIVATE);
        token=userInformationSharedPreferences.getString("token","");//从用户信息文件中从读取token
        userId=userInformationSharedPreferences.getLong("id",0);//从用户信息文件中读取用户手机号
        requestQueue= Volley.newRequestQueue(this);
        recordList=new ArrayList<>();
        initRecordList();
        itemRemoveRcordAdapter=new ItemRemoveRcordAdapter(recordList);
    }

    private void initView() {
        removeRecordRecycleView= (ItemRemoveRecordRecycle) findViewById(R.id.activity_my_records_item_remove_recycler_view);
        removeRecordRecycleView.setLayoutManager(new LinearLayoutManager(this));
        removeRecordRecycleView.setAdapter(itemRemoveRcordAdapter);
        removeRecordRecycleView.setOnItemClickListener(new OnItemRemoveRecord() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                itemRemoveRcordAdapter.removeItem(position);
            }
        });
    }

    private void initRecordList()
    {

        getRecordListRequest = new JsonObjectRequest(Request.Method.GET, RecordListURL + userId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int code = jsonObject.getInt("code");
                            if (code == 88) {
                                int id;
                                int userId;
                                Long time;//时间：毫秒数
                                double lat;
                                double lon;
                                String observationPalName;//观察伙伴
                                int noteCount;//笔记数量
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    id = data.getJSONObject(i).getInt("id");
                                    userId = data.getJSONObject(i).getInt("userId");
                                    time=data.getJSONObject(i).getLong("time");
                                    lat=data.getJSONObject(i).getDouble("lat");
                                    lon=data.getJSONObject(i).getDouble("lon");
                                    noteCount=data.getJSONObject(i).getInt("noteCount");
                                    Record record=new Record(id,userId,time,lat,lon,noteCount);
                                    recordList.add(record);
                                }

                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        requestQueue.add(getRecordListRequest);
    }

    public class Record {
        private int id;
        private int userId;
        private Long time;//时间：毫秒数
        private double lat;
        private double lon;
        private String observationPalName;//观察伙伴
        private int noteCount;//笔记数量

        public Record(int id, int userId, Long time, double lat, double lon,int noteCount) {
            this.id = id;
            this.userId = userId;
            this.time = time;
            this.lat = lat;
            this.lon = lon;
            this.noteCount = noteCount;
        }

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

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
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

        public String getObservationPalName() {
            return observationPalName;
        }

        public void setObservationPalName(String observationPalName) {
            this.observationPalName = observationPalName;
        }

        public int getNoteCount() {
            return noteCount;
        }

        public void setNoteCount(int noteCount) {
            this.noteCount = noteCount;
        }
    }

}
