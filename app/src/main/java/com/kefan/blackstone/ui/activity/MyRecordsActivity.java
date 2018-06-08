package com.kefan.blackstone.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kefan.blackstone.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JavaBean.APIManager;
public class MyRecordsActivity extends BaseActivity {
    private String getRecordListURL= APIManager.BASE_URL +"v1/record/user/";
    private String removeRecordListURL=APIManager.BASE_URL +"v1/record/";
    private RequestQueue requestQueue;
    private JsonObjectRequest getRecordListRequest;
    private JsonObjectRequest deleteRecordListRequest;
    private ItemRemoveRecordRecycle removeRecordRecycleView;
    private List<Record> recordList;
    private ItemRemoveRcordAdapter itemRemoveRcordAdapter;
    private LinearLayout actionBack;
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
        UpdateToken updateToken=new UpdateToken(this);
        updateToken.updateToken();
    }

    private void initData() {
        userInformationSharedPreferences=getSharedPreferences(userInformation, MODE_PRIVATE);
        token=userInformationSharedPreferences.getString("token","");//从用户信息文件中从读取token
        userId=userInformationSharedPreferences.getLong("id",0);//从用户信息文件中读取用户id不是手机号码
        requestQueue= Volley.newRequestQueue(this);
        recordList=new ArrayList<>();
    }

    private void initView() {
        actionBack= (LinearLayout) findViewById(R.id.activity_my_record_linear_layout_action_back);
        removeRecordRecycleView= (ItemRemoveRecordRecycle) findViewById(R.id.activity_my_records_item_remove_recycler_view);
        removeRecordRecycleView.setLayoutManager(new LinearLayoutManager(this));

        itemRemoveRcordAdapter=new ItemRemoveRcordAdapter(this,recordList);
        removeRecordRecycleView.setAdapter(itemRemoveRcordAdapter);
        removeRecordRecycleView.setOnItemClickListener(new OnItemRemoveRecord() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(MyRecordsActivity.this,MyRecordTwoActivity.class);
                intent.putExtra("recordId",recordList.get(position).getId());
                intent.putExtra("time",recordList.get(position).getTime());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                itemRemoveRcordAdapter.removeItem(position);
                removeRecordList(position);
            }
        });

        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void removeRecordList(final int position) {
       Record record= recordList.get(position);
        final UserInformationUtil userInformationUtil=new UserInformationUtil(this);
        deleteRecordListRequest=new JsonObjectRequest(Request.Method.DELETE, removeRecordListURL + record.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        recordList.remove(position);
                        itemRemoveRcordAdapter.notifyDataSetChanged();
                    }else
                    {
                        String message=jsonObject.getString("message");
                        Toast.makeText(MyRecordsActivity.this,message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyRecordsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                UpdateToken updateToken=new UpdateToken(MyRecordsActivity.this);
                updateToken.updateToken();
                Map<String,String>headers=new HashMap<>();
                headers.put("token",userInformationUtil.getToken());
                return  headers;
            }
        };
        requestQueue.add(deleteRecordListRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordList.clear();
        initRecordList();
    }

    private void initRecordList()
    {
        UserInformationUtil userInformationUtil=new UserInformationUtil(this);
        getRecordListRequest = new JsonObjectRequest(Request.Method.GET, getRecordListURL + userInformationUtil.getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int code = jsonObject.getInt("code");
                            if (code == 88) {
                                JSONArray data=jsonObject.getJSONArray("data");
                                for(int i=0;i<data.length();i++)
                                {
                                    Record record=new Record();
                                    record.setId(data.getJSONObject(i).getInt("id"));
                                    record.setUserId(data.getJSONObject(i).getInt("userId"));
                                    if(data.getJSONObject(i).has("time"))
                                    {
                                        record.setTime(data.getJSONObject(i).getLong("time"));
                                    }
                                    else
                                    {
                                        record.setTime(0l);
                                    }
                                    ArrayList<NoteCounts> noteCountses=new ArrayList<>();
                                    JSONArray notecounts=data.getJSONObject(i).getJSONArray("noteCounts");
                                    for(int j=0;j<notecounts.length();j++)
                                    {
                                        NoteCounts noteC=new NoteCounts();
                                        noteC.setSpeciesType(notecounts.getJSONObject(j).getString("speciesType"));
                                        noteC.setCount(notecounts.getJSONObject(j).getInt("count"));
                                        noteCountses.add(noteC);
                                    }
                                    record.setNoteCountses(noteCountses);
                                    recordList.add(record);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        initView();
                                        itemRemoveRcordAdapter.notifyDataSetChanged();
                                    }
                                });

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
                UpdateToken updateToken=new UpdateToken(MyRecordsActivity.this);
                updateToken.updateToken();
                Map<String,String> headers=new HashMap<>();
                headers.put("token",userInformationSharedPreferences.getString("token",""));
                return headers;
            }
        };

        requestQueue.add(getRecordListRequest);
    }

    public class Record {
        private int id;//记录id
        private int userId;//用户id
        private Long time;//时间：毫秒数
        private List<NoteCounts> noteCountses;

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

        public List<NoteCounts> getNoteCountses() {
            return noteCountses;
        }

        public void setNoteCountses(List<NoteCounts> noteCountses) {
            this.noteCountses = noteCountses;
        }
    }
    public class NoteCounts
    {
        String speciesType;
        int count;

        public String getSpeciesType() {
            return speciesType;
        }

        public void setSpeciesType(String speciesType) {
            this.speciesType = speciesType;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
