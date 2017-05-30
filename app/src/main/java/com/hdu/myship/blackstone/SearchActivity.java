package com.hdu.myship.blackstone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.media.DrmInitData;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Species;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private String searchURL="http://api.blackstone.ebirdnote.cn/v1/species/search";
    private RequestQueue requestQueue;
    private JsonObjectRequest searchRequest;
    private EditText input;
    private TextView cancel;
    private LinearLayout history;
    private ListView listView;
    private MyListViewAdapter myListViewAdapter;
    private List<Species> myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        initData();
        initViews();
        initEvents();

    }

    private void initData() {
        requestQueue= Volley.newRequestQueue(this);
        myList=new ArrayList<>();
        myListViewAdapter=new MyListViewAdapter(myList);
    }

    private void initEvents() {
        cancel.setOnClickListener(this);
    }


    private void initViews() {
        input= (EditText) findViewById(R.id.activity_search_view_edit_text_input);
        cancel= (TextView) findViewById(R.id.activity_search_view_text_view_cancel);
        history= (LinearLayout) findViewById(R.id.activity_search_view_linear_layout_history);
        listView= (ListView) findViewById(R.id.activity_search_view_list_view);
        listView.setAdapter(myListViewAdapter);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                history.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAdapter(input.getText().toString());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Species species=myList.get(position);
                Intent intent=new Intent(SearchActivity.this,SpeciesDeatailedActivity.class);
                intent.putExtra("singal",species.getSingal());
                intent.putExtra("speciesType",species.getSpeciesType());
                //intent.putExtra("speciesTypeChineseName",typeTitle[position]);
                startActivity(intent);
            }
        });
    }

    private void updateAdapter(String s) {
        if(s.equals(""))
        {
            myListViewAdapter.list.clear();
            myListViewAdapter.notifyDataSetChanged();
            history.setVisibility(View.VISIBLE);
        }else
        {
            Map<String,String> map=new HashMap<>();
            map.put("key",input.getText().toString());
            JSONObject jsonObject=new JSONObject(map);
            searchRequest=new JsonObjectRequest(Request.Method.POST, searchURL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code=jsonObject.getInt("code");
                        if(code==88)
                        {   myList.clear();
                            JSONArray data=jsonObject.getJSONArray("data");
                            for(int i=0;i<data.length();i++)
                            {
                                Species species= new Species();
                                species.setSingal(data.getJSONObject(i).getInt("id"));
                                species.setChineseName(data.getJSONObject(i).getString("chineseName"));
                                species.setLatinName(data.getJSONObject(i).getString("latinName"));
                                species.setOrder(data.getJSONObject(i).getString("order"));
                                if(data.getJSONObject(i).has("family"))
                                {
                                    species.setFamily(data.getJSONObject(i).getString("family"));
                                }
                                else
                                {
                                    species.setFamily("");
                                }
                                species.setSpeciesType(data.getJSONObject(i).getString("speciesType"));
                                species.setMainPhoto(data.getJSONObject(i).getString("mainPhoto"));
                                myList.add(species);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myListViewAdapter.list=myList;
                                    myListViewAdapter.notifyDataSetChanged();
                                }
                            });
                        }else
                        {
                            String message=jsonObject.getString("message");
                            Toast.makeText(SearchActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SearchActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(searchRequest);
           // myList= DataSupport.where("chineseName like ?","%"+s+"%").find(Species.class);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_search_view_text_view_cancel:
                this.finish();
                break;
        }
    }
    public class MyListViewAdapter extends BaseAdapter
    {
        List<Species> list;

        public MyListViewAdapter(List<Species> list) {
            this.list = list;
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
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView= LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_view_reseult_item,null);
            TextView textView= (TextView) convertView.findViewById(R.id.search_view_result_item_text_view);
            textView.setText(list.get(position).getChineseName());
            return convertView;
        }
    }
}
