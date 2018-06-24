package com.kefan.blackstone.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.kefan.blackstone.BlackStoneApplication;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.model.Rank;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.adapter.RankAdapter;
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.widget.HeaderBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RankActivity extends BaseActivity {


    @BindView(R.id.headerbar_rank_activity)
    HeaderBar headerbar;

    @BindView(R.id.recycle_view_rank_activity)
    RecyclerView recycleView;

    @BindView(R.id.v_spilt_line_rank_activity)
    View view;

    @BindView(R.id.tv_utime_rank_activity)
    TextView utime;

    private List<Rank> rankList;

    private RankAdapter rankAdapter;

    private UserService userService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_rank;
    }

    @Override
    public void initData() {

        userService = new UserServiceImpl();
        rankList = new ArrayList<>();
        rankAdapter = new RankAdapter(this, rankList);
        loadRankData();

    }

    @Override
    public void initView() {

        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        recycleView.setAdapter(rankAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Date date = new Date();



        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        utime.setText("更新时间: " + year + "年" + (month+1) + "月" + day + "日 " + date.getHours() + ":" + date.getMinutes());

    }

    @Override
    public void initEvent() {

        headerbar.getLeftPart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadRankData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                APIManager.RANK_LIST, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if (jsonObject.getInt("code") == 88) {
                        rankList.clear();
                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {

                            Gson gson = new Gson();

                            Rank rank = gson.fromJson(data.get(i).toString(), Rank.class);

                            rankList.add(rank);
                        }

                        //如果当前用户是排名第一 或者没有数据的话 影藏分割线
                        if (rankList.size() == 0 || rankList.get(0).getUser().getId() == userService.getUser().getId()) {
                            view.setVisibility(View.INVISIBLE);
                        }
                        rankAdapter.notifyDataSetChanged();

                    } else {

                        ToastUtil.showToast(RankActivity.this, jsonObject.getString("message"));

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

                Map<String, String> map = new HashMap<>();
                map.put("token", userService.getToken());
                return map;
            }
        };

        BlackStoneApplication.getRequestQueue().add(jsonObjectRequest);

    }


}
