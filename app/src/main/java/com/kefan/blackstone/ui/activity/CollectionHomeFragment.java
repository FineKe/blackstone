package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.database.Species;
import com.kefan.blackstone.model.SpeciesClass;
import com.kefan.blackstone.ui.adapter.CollectionHomeAdapter;
import com.kefan.blackstone.ui.fragment.BaseFragment;
import com.kefan.blackstone.util.NetWorkUtil;
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.widget.HeaderBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CollectionHomeFragment extends BaseFragment {
    private final int LOAD_DATA_OK = 0;
    private final int JUMP_OK = 1;



    private String getCollectionURL = APIManager.BASE_URL + "v1/species/collection/";
    private RequestQueue requestQueue;
    private JsonObjectRequest getCollectionRequest;


    public static List<SpeciesClass> speciesClassList;//该list声明为静态，是为了与下一级界面共享该数据


    private HeaderBar headerBar;

    @BindView(R.id.recycle_view_collection_home_fragment)
    RecyclerView recyclerView;

    @BindView(R.id.rfl_collection_home_fragment)
    PullRefreshLayout pullRefreshLayout;

    private CollectionHomeAdapter adapter;



    @Override
    public int setLayout() {
        return R.layout.fragment_home_collection;
    }

    @Override
    protected void initData() {

        speciesClassList=new ArrayList<>();
        adapter=new CollectionHomeAdapter(getContext(),speciesClassList);
    }

    @Override
    public void initView() {

        headerBar= ((MainActivity) getActivity()).headerBar;
        headerBar.getCenterTextView().setText("我的收藏");
        headerBar.getRightPart().setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        getCollection();
    }

    @Override
    public void initEvent() {

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getCollection();

            }
        });

    }

    @Override
    public void onDestroyView() {

        headerBar.getCenterTextView().setText("");
        headerBar.getRightPart().setVisibility(View.VISIBLE);

        super.onDestroyView();
    }


    public void getCollection()
    {

        if (!NetWorkUtil.isConnected(getContext())) {

            ToastUtil.showToast(getContext(),"网络未连接");

            handler.sendEmptyMessage(LOAD_DATA_OK);

            return;
        }

        requestQueue= Volley.newRequestQueue(getContext());
        UpdateToken updateToken=new UpdateToken(getContext());
        updateToken.updateToken();

        speciesClassList.clear();

        final UserInformationUtil userInformationUtil=new UserInformationUtil(getContext());
        getCollectionRequest=new JsonObjectRequest(Request.Method.GET, getCollectionURL + userInformationUtil.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        List<Species> amphibia=new ArrayList<>();
                        List<Species> reptiles=new ArrayList<>();
                        List<Species> bird=new ArrayList<>();
                        List<Species> insect=new ArrayList<>();

                        int acount=0;
                        int bcount=0;
                        int rcount=0;
                        int icount=0;
                        JSONArray data=jsonObject.getJSONArray("data");
                        for(int i=0;i<data.length();i++)
                        {
                            JSONObject object=data.getJSONObject(i);
                            switch (object.getString("speciesType"))
                            {
                                case "amphibia":
                                    acount++;
                                    Species aspecies=new Species();
                                    aspecies.setSingal(object.getInt("id"));
                                    amphibia.add(aspecies);
                                    break;
                                case "reptiles":
                                    rcount++;
                                    Species rspecies=new Species();
                                    rspecies.setSingal(object.getInt("id"));
                                    reptiles.add(rspecies);
                                    break;
                                case "bird":
                                    bcount++;
                                    Species bspecies=new Species();
                                    bspecies.setSingal(object.getInt("id"));
                                    bird.add(bspecies);
                                    break;
                                case "insect":
                                    icount++;
                                    Log.d("sssssssssssss", "onResponse: "+icount);
                                    Species ispecies=new Species();
                                    ispecies.setSingal(object.getInt("id"));
                                    insect.add(ispecies);
                                    break;
                            }
                        }
                        if(acount!=0)
                        {
                            SpeciesClass a=new SpeciesClass("amphibia",amphibia,acount);
                            a.setName("两栖类");
                            speciesClassList.add(a);
                        }
                        if(rcount!=0)
                        {
                            SpeciesClass r=new SpeciesClass("reptiles",reptiles,rcount);
                            r.setName("爬行类");
                            speciesClassList.add(r);
                        }
                        if(bcount!=0)
                        {
                            SpeciesClass b=new SpeciesClass("bird",bird,bcount);
                            b.setName("鸟类");
                            speciesClassList.add(b);
                        }
                        Log.d("zx", "onResponse: "+icount);
                        if(icount!=0)
                        {
                            Log.d("zx", "onResponse: "+icount);
                            SpeciesClass in=new SpeciesClass("insect",insect,icount);
                            Log.d("zx", "onResponse: "+icount);
                            in.setName("昆虫目");
                            speciesClassList.add(in);
                        }
                        Log.d("list", "onResponse: "+speciesClassList.size());
                        Message message=new Message();
                        message.what=LOAD_DATA_OK;
                        handler.sendMessage(message);
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> hearder=new HashMap<>();
                hearder.put("token",userInformationUtil.getToken());
                return hearder;
            }
        };
        requestQueue.add(getCollectionRequest);
    }

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case LOAD_DATA_OK:


                    adapter.notifyDataSetChanged();


                    pullRefreshLayout.setRefreshing(false);

                    break;
            }
        }
    };
}
