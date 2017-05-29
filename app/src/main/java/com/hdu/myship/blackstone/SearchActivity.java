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

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import database.Species;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
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
            myList= DataSupport.where("chineseName like ?","%"+s+"%").find(Species.class);
            myListViewAdapter.list=myList;
            myListViewAdapter.notifyDataSetChanged();
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
