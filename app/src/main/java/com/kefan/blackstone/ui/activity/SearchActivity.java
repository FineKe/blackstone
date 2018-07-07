package com.kefan.blackstone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.kefan.blackstone.BaseActivity;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.database.HistoryRecord;
import com.kefan.blackstone.database.Species;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private String searchURL = APIManager.BASE_URL + "v1/species/search";
    private RequestQueue requestQueue;
    private JsonObjectRequest searchRequest;
    private EditText input;
    private TextView cancel;
    private LinearLayout history;
    private ListView listView;
    private RecyclerView listViewHistory;
    private MyListViewAdapter myListViewAdapter;
    private List<Species> myList;
    private List<HistoryRecord> historyRecordList;
    private HistorySearchAdapter historySearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        initData();
        initViews();
        initEvents();

    }

    private void initData() {
        requestQueue = Volley.newRequestQueue(this);
        myList = new ArrayList<>();
        myListViewAdapter = new MyListViewAdapter(myList);
        historyRecordList = DataSupport.limit(10).find(HistoryRecord.class);

        historySearchAdapter = new HistorySearchAdapter(historyRecordList);

    }

    private void initEvents() {
        cancel.setOnClickListener(this);
//        listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HistoryRecord historyRecord = (HistoryRecord) view.getTag();
//                Intent intent = new Intent(SearchActivity.this, SpeciesDeatailedActivity.class);
//                intent.putExtra("singal", historyRecord.getSingal());
//                intent.putExtra("speciesType", historyRecord.getSpeciesType());
//                startActivity(intent);
//            }
//        });
    }


    private void initViews() {
        input = (EditText) findViewById(R.id.activity_search_view_edit_text_input);
        cancel = (TextView) findViewById(R.id.activity_search_view_text_view_cancel);
        history = (LinearLayout) findViewById(R.id.activity_search_view_linear_layout_history);
        listView = (ListView) findViewById(R.id.activity_search_view_list_view);
        listViewHistory = (RecyclerView) findViewById(R.id.activity_search_list_view_history);
        listViewHistory.setLayoutManager(new GridLayoutManager(this, 3));
        listViewHistory.setAdapter(historySearchAdapter);
        listView.setAdapter(myListViewAdapter);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listView.setVisibility(View.VISIBLE);
                history.setVisibility(View.GONE);
                if (s.length() == 0) {
                    history.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAdapter(input.getText().toString());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Species species = myList.get(position);

                HistoryRecord historyRecord = new HistoryRecord();
                historyRecord.setSingal(species.getSingal());
                historyRecord.setChineseName(species.getChineseName());
                historyRecord.setSpeciesType(species.getSpeciesType());
                historyRecord.save();

                Intent intent = new Intent(SearchActivity.this, SpeciesDeatailedActivity.class);
                intent.putExtra("singal", species.getSingal());
                intent.putExtra("speciesType", species.getSpeciesType());
                startActivity(intent);
            }
        });


    }

    private void updateAdapter(String s) {
        if (s.equals("")) {
            myListViewAdapter.list.clear();
            myListViewAdapter.notifyDataSetChanged();
            history.setVisibility(View.VISIBLE);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("key", input.getText().toString());
            JSONObject jsonObject = new JSONObject(map);
            searchRequest = new JsonObjectRequest(Request.Method.POST, searchURL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code = jsonObject.getInt("code");
                        if (code == 88) {
                            myList.clear();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                Species species = new Species();
                                species.setSingal(data.getJSONObject(i).getInt("id"));
                                species.setChineseName(data.getJSONObject(i).getString("chineseName"));
                                species.setLatinName(data.getJSONObject(i).getString("latinName"));
                                species.setOrder(data.getJSONObject(i).getString("order"));
                                if (data.getJSONObject(i).has("family")) {
                                    species.setFamily(data.getJSONObject(i).getString("family"));
                                } else {
                                    species.setFamily("");
                                }
                                species.setSpeciesType(data.getJSONObject(i).getString("speciesType"));
                                species.setMainPhoto(data.getJSONObject(i).getString("mainPhoto"));
                                myList.add(species);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myListViewAdapter.list = myList;
                                    myListViewAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            String message = jsonObject.getString("message");
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
        switch (v.getId()) {
            case R.id.activity_search_view_text_view_cancel:
                this.finish();
                break;
        }
    }

    public class MyListViewAdapter extends BaseAdapter {
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
            convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_view_reseult_item, null);
            TextView textView = (TextView) convertView.findViewById(R.id.search_view_result_item_text_view);
            textView.setText(list.get(position).getChineseName());
            return convertView;
        }
    }

    public class HistoryRecordAdapter extends BaseAdapter {
        List<HistoryRecord> historyRecordList;

        public HistoryRecordAdapter(List<HistoryRecord> historyRecordList) {
            this.historyRecordList = historyRecordList;
        }

        @Override
        public int getCount() {
            return historyRecordList.size();
        }

        @Override
        public Object getItem(int position) {
            return historyRecordList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_view_history_item, null);
            TextView textView = (TextView) convertView.findViewById(R.id.search_view_history_item_text_view);
            textView.setText(historyRecordList.get(position).getChineseName());
            convertView.setTag(historyRecordList.get(position));
            return convertView;
        }
    }

    public class HistorySearchAdapter extends RecyclerView.Adapter<HistorySearchAdapter.ViewHolder> {

        List<HistoryRecord> historyRecordList;

        public HistorySearchAdapter(List<HistoryRecord> historyRecordList) {
            this.historyRecordList = historyRecordList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_history_item, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.textView.setText(historyRecordList.get(position).getChineseName());

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HistoryRecord historyRecord = historyRecordList.get(position);
                    Intent intent = new Intent(SearchActivity.this, SpeciesDeatailedActivity.class);
                    intent.putExtra("singal", historyRecord.getSingal());
                    intent.putExtra("speciesType", historyRecord.getSpeciesType());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return historyRecordList.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.search_view_history_item_text_view)
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
