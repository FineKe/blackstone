package com.kefan.blackstone.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.kefan.blackstone.BlackStoneApplication;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.data.req.RecordReq;
import com.kefan.blackstone.database.Note;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.model.NoteCount;
import com.kefan.blackstone.service.RecordService;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.RecordServiceImpl;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.fragment.BaseFragment;
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.vo.RecordVo;
import com.kefan.blackstone.widget.HeaderBar;
import com.kefan.blackstone.widget.ItemRemoveRecordRecycleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;

public class ObserveRecordFragment extends BaseFragment {

    public static final int LOAD_NET_RECORD = 2;

    private static final String TAG = ObserveRecordFragment.class.getName();

    public static final int LOAD_RECORDS_COMPLETE = 1;

    private HeaderBar headerBar;


    @BindView(R.id.item_remove_recycler_view_observe_record_fragment)
    ItemRemoveRecordRecycleView itemRemoveRecordRecycleView;

    private UserService userService;

    private RecordService recordService;

    private ItemRemoveRcordAdapter itemRemoveRcordAdapter;

    private List<Record> records;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private LoadRecordRunner loadRecordRunner = new LoadRecordRunner();

    @Override
    public int setLayout() {
        return R.layout.fragment_observe_record;
    }

    @Override
    public void initView() {

        headerBar = ((MainActivity) getActivity()).headerBar;
        headerBar.getCenterTextView().setText("我的观察记录");
        headerBar.getRightPart().setVisibility(View.GONE);


        itemRemoveRecordRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        itemRemoveRecordRecycleView.setAdapter(itemRemoveRcordAdapter);

    }

    @Override
    protected void initData() {

        userService = new UserServiceImpl();
        recordService = new RecordServiceImpl();

        records = new ArrayList<>();
        itemRemoveRcordAdapter = new ItemRemoveRcordAdapter(getContext(), records);

        //加载数据
        loadRecords();
    }

    @Override
    public void initEvent() {

        itemRemoveRecordRecycleView.setOnItemClickListener(new OnItemRemoveRecordListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getContext(), MyRecordTwoActivity.class);
                intent.putExtra("recordId", records.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(final int position) {




                final Record record = records.get(position);

                //先删除服务器山的记录
                recordService.deleteRecord(userService.getToken(), record.getNetId() + "", new BaseResponseListener(Object.class) {
                    @Override
                    protected void onSuccess(Object data) {

                        //成功再删除本地记录
                        if (record.delete() > 0) {
                            records.remove(position);
                            ToastUtil.showToast(getContext(), "删除成功");
                        }

                        itemRemoveRcordAdapter.notifyDataSetChanged();

                    }

                    @Override
                    protected void onFailed(int code, String message) {
                        ToastUtil.showToast(getContext(),message);
                    }
                }, new BaseErrorListener(getContext()) {
                    @Override
                    protected void onError(VolleyError volleyError) {

                    }
                });

            }

            @Override
            public void onUploadClick(int position) {

                final Record record = records.get(position);

                RecordReq recordReq = copyFromRecord(record);

                recordService.uploadRecord(userService.getToken(), recordReq, new BaseResponseListener(Object.class) {
                    @Override
                    protected void onSuccess(Object data) {

                        ToastUtil.showToast(getContext(), "上传成功");

                        record.setAddToObservedList(true);

                        boolean result = record.save();

                        itemRemoveRcordAdapter.notifyDataSetChanged();

                        Log.d(TAG, "onSuccess: " + result);

                    }

                    @Override
                    protected void onFailed(int code, String message) {
                        ToastUtil.showToast(getContext(), "上传失败");
                        ToastUtil.showToast(getContext(), message);

                    }
                }, new BaseErrorListener(getContext()) {
                    @Override
                    protected void onError(VolleyError volleyError) {
                        super.onError(volleyError);
                    }
                });

            }
        });
    }

    @Override
    public void onDestroyView() {

        headerBar.getCenterTextView().setText("");
        headerBar.getRightPart().setVisibility(View.VISIBLE);

        super.onDestroyView();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_RECORDS_COMPLETE:

                    itemRemoveRcordAdapter.notifyDataSetChanged();

                    break;

                case LOAD_NET_RECORD:
                    ToastUtil.showToast(getContext(), "记录中心获取记录失败");
                    break;
            }
        }
    };

    private void loadRecords() {

        executor.submit(loadRecordRunner);

    }

    private class LoadRecordRunner implements Runnable {

        @Override
        public void run() {
            records.clear();
            records.addAll(recordService.list(userService.getUser().getId()));


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                    , APIManager.PULL_RECORDS + userService.getUser().getId(), null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    try {
                        if (jsonObject.getInt("code") == 88) {

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.length() > 0) {

                                Gson gson = new Gson();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    RecordVo recordVo = gson.fromJson(jsonArray.get(i).toString(), RecordVo.class);

                                    //将从网络获取的记录存储到本地


                                    //如果过本地存在 记录id不一样的 则创建
                                    if (recordService.findRecordByNetId(recordVo.getId()) == null) {

                                        records.add(createRecordFromNet(recordVo));

                                    }


                                }

                                Log.d(TAG, "onResponse: "+records.size());
                                handler.sendEmptyMessage(LOAD_RECORDS_COMPLETE);

                            }

                        } else {
                            handler.sendEmptyMessage(LOAD_NET_RECORD);
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

                    Map<String, String> header = new HashMap<>();
                    header.put("token", userService.getToken());

                    return header;
                }
            };

            BlackStoneApplication.getRequestQueue().add(jsonObjectRequest);

            handler.sendEmptyMessage(LOAD_RECORDS_COMPLETE);
        }
    }


    /**
     * @param record
     * @return
     */
    private RecordReq copyFromRecord(Record record) {

        return new RecordReq(record.getTime(), record.getLat(), record.getLon()
                , true, "", record.getNotes());

    }


    private Record createRecordFromNet(RecordVo recordVo) {

        Record record = new Record();

        record.setAddToObservedList(true);
        record.setUserId(recordVo.getUserId());
        record.setTime(recordVo.getTime());
        record.setNetId(recordVo.getId());
        record.setNotes(createNoteFromNet(recordVo.getNoteCounts()));

        record.save();

        return record;

    }

    private List<Note> createNoteFromNet(List<NoteCount> noteCounts) {

        List<Note> notes = new ArrayList<>();

        for (NoteCount noteCount : noteCounts) {

            Note note = new Note(0l, "", noteCount.getSpeciesType(), "", "");
            note.save();
            notes.add(note);
        }
        return notes;
    }


}
