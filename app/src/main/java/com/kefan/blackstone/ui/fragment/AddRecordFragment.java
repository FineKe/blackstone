package com.kefan.blackstone.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.BlackStoneApplication;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.AddNoteCommon;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.data.req.RecordReq;
import com.kefan.blackstone.database.Note;
import com.kefan.blackstone.database.NoteTemplate;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.listener.AlertPromptOnClickListener;
import com.kefan.blackstone.listener.addNoteOnclickListener;
import com.kefan.blackstone.service.RecordService;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.RecordServiceImpl;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.activity.AddNotesActivity;
import com.kefan.blackstone.ui.activity.MainActivity;
import com.kefan.blackstone.ui.adapter.AddRecordExpandAdapter;
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.vo.UpLoadRecordVo;
import com.kefan.blackstone.widget.AlertPrompt;
import com.kefan.blackstone.widget.HeaderBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import ch.ielse.view.SwitchView;

/**
 * Created by MY SHIP on 2017/3/18.
 * 添加记录fragment
 */

public class AddRecordFragment extends BaseFragment {


    public static final int LOAD_NODE_TEMPLATE_COMPLETE = 1;


    private String TAG = "AddRecordFragment";


    private boolean datePickerShow = false;

    private Calendar calendar;


    private int year, month, day;


    private List<String> group;

    private List<List<NoteTemplate>> noteTemplateList;

    private AMapLocationClient mapLocationClient;

    private AMapLocationListener locationListener;


    private HeaderBar headerBar;


    @BindView(R.id.add_record_titleBar_textView_date)
    TextView date;

    @BindView(R.id.add_record_datepicker)
    DatePicker datePicker;

    @BindView(R.id.tv_upload_add_record_fragment)
    TextView upload;

    @BindView(R.id.add_record_expandListView)
    ExpandableListView expandListView;

    @BindView(R.id.sv_upload_add_record_fragment)
    SwitchView switchUpload;


    private UserService userService;
    private RecordService recordService;

    private AddRecordExpandAdapter adapter;


    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    //位置
    private int groupPosition;
    private int childPosition;


    private Double lat=0.0;
    private Double lon = 0.0;

    @Override
    public int setLayout() {
        return R.layout.fragment_add_record;
    }


    @Override
    protected void initData() {

        userService = new UserServiceImpl();
        recordService = new RecordServiceImpl();

        group = new ArrayList<>();
        noteTemplateList = new ArrayList<>();
        adapter = new AddRecordExpandAdapter(group, noteTemplateList);

        mapLocationClient=new AMapLocationClient(getContext());
        locationListener=new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                lat=aMapLocation.getLatitude();
                lon=aMapLocation.getLongitude();

            }
        };

        createNoteTemplate();

    }

    @Override
    public void initView() {

        //设置headerbar
        headerBar = ((MainActivity) getActivity()).headerBar;
        headerBar.getCenterTextView().setText("添加记录");
        headerBar.getRightImageView().setVisibility(View.GONE);
        headerBar.getRightTextView().setText("保存");


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date.setText(year + "年" + (month + 1) + "月" + day + "日");
        month++;


        expandListView.setAdapter(adapter);

        //取消自带的指示箭头
        expandListView.setGroupIndicator(null);


    }

    @Override
    public void initEvent() {

        switchUpload.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                headerBar.getRightTextView().setText("发表");
                view.toggleSwitch(true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                headerBar.getRightTextView().setText("保存");
                view.toggleSwitch(false);
            }
        });


        datePicker.init(year, month - 1, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year_, int monthOfYear, int dayOfMonth) {
                year = year_;
                month = monthOfYear + 1;
                day = dayOfMonth;
                date.setText(year + "年" + (month) + "月" + day + "日");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePickerShow == false) {
                    datePicker.setVisibility(View.VISIBLE);
                    datePickerShow = true;
                } else {
                    datePicker.setVisibility(View.GONE);
                    datePickerShow = false;
                }
            }
        });

        headerBar.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //判断用户是否登录了
                if (!userService.isLogined()) {
                    ToastUtil.showToast(getContext(), "未登录");
                    return;
                }

                //如果过开启上传
                if (switchUpload.isOpened()) {

                    //弹出提示窗
                    showAlertPrompt();
                } else {

                    //否则直接保存到本地
                    if (saveToLocal()) {

                        Toast.makeText(getContext(), "保存本地成功", Toast.LENGTH_SHORT).show();

                        //重置一下 模板
                        createNoteTemplate();
                    } else {

                        Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();

                    }


                }


            }
        });

        adapter.setAddNoteOnclickListener(new addNoteOnclickListener() {
            @Override
            public void onClick(View view, int groupPos, int childPos, NoteTemplate noteTemplate) {


                //记录位置
                childPosition = childPos;
                groupPosition = groupPos;

                startActivityForResult(new Intent(getContext(), AddNotesActivity.class), AddNoteCommon.REQUEST_START_ADD_NOTE_ACTIVITY_CODE);

            }
        });


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        } else {
            mapLocationClient.setLocationListener(locationListener);
            mapLocationClient.stopLocation();
        }
    }

    @Override
    public void onDestroyView() {

        //还原初始headerbar
        headerBar.getCenterTextView().setText("");
        headerBar.getRightImageView().setVisibility(View.VISIBLE);
        headerBar.getRightTextView().setText("");

        super.onDestroyView();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            int j = 0;
            for (int i = 0; i < grantResults.length; i++) {

                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showToast(getContext(), "权限拒绝");
                } else {
                    j++;
                }

            }

            if (j == permissions.length) {

                mapLocationClient.setLocationListener(locationListener);
                mapLocationClient.stopLocation();

            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mapLocationClient.stopLocation();
        mapLocationClient.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AddNoteCommon.REQUEST_START_ADD_NOTE_ACTIVITY_CODE) {
            if (!data.getBooleanExtra(AddNoteCommon.ISNULL, true)) {

                adapter.setRemark(groupPosition, childPosition, data.getStringExtra(AddNoteCommon.REMARK));

            }
        }

    }




    private void createNoteTemplate() {

        //如果 笔记模板还不存在 则创建
        if (DataSupport.count(NoteTemplate.class) <= 0) {
            Log.d(TAG, "createNoteTemplate: " + "网络获取");
            executorService.submit(new Runnable() {
                @Override
                public void run() {


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, APIManager.SPECIES_LIST_URL, null
                            , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {


                            try {

                                if (jsonObject.getInt("code") == 88) {
                                    JSONObject data = jsonObject.getJSONObject("data");

                                    Iterator<String> keys = data.keys();

                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONArray jsonArray = data.getJSONArray(key);

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject object = (JSONObject) jsonArray.get(i);

                                            System.out.println(object.toString());
                                            NoteTemplate noteTemplate = new NoteTemplate(object.getLong("id")
                                                    , object.getString("chineseName"), false
                                                    , object.getString("speciesType"), null
                                                    , object.has("family") ? object.getString("family") : object.getString("order"));
                                            noteTemplate.save();

                                        }

                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            group.clear();
                            noteTemplateList.clear();

                            group.add("鸟类");
                            group.add("昆虫");
                            group.add("两栖类");
                            group.add("爬行类");
                            group.add("兽类");


                            List<NoteTemplate> bird = DataSupport.where("speciesType=?", "bird").find(NoteTemplate.class);
                            List<NoteTemplate> insect = DataSupport.where("speciesType=?", "insect").find(NoteTemplate.class);
                            List<NoteTemplate> amphibia = DataSupport.where("speciesType=?", "amphibia").find(NoteTemplate.class);
                            List<NoteTemplate> reptiles = DataSupport.where("speciesType =?", "reptiles").find(NoteTemplate.class);
                            List<NoteTemplate> mamal = DataSupport.where("speciesType =?", "mamal").find(NoteTemplate.class);


                            noteTemplateList.add(bird);
                            noteTemplateList.add(insect);
                            noteTemplateList.add(amphibia);
                            noteTemplateList.add(reptiles);
                            noteTemplateList.add(mamal);

                            handler.sendEmptyMessage(LOAD_NODE_TEMPLATE_COMPLETE);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });

                    BlackStoneApplication.getRequestQueue().add(jsonObjectRequest);

                }
            });


        } else {

            Log.d(TAG, "createNoteTemplate: " + "本地获取");
            group.clear();
            noteTemplateList.clear();

            group.add("鸟类");
            group.add("昆虫");
            group.add("两栖类");
            group.add("爬行类");
            group.add("兽类");

            List<NoteTemplate> bird = DataSupport.where("speciesType=?", "bird").find(NoteTemplate.class);
            List<NoteTemplate> insect = DataSupport.where("speciesType=?", "insect").find(NoteTemplate.class);
            List<NoteTemplate> amphibia = DataSupport.where("speciesType=?", "amphibia").find(NoteTemplate.class);
            List<NoteTemplate> reptiles = DataSupport.where("speciesType=?", "reptiles").find(NoteTemplate.class);
            List<NoteTemplate> mamal = DataSupport.where("speciesType =?", "mamal").find(NoteTemplate.class);

            noteTemplateList.add(bird);
            noteTemplateList.add(insect);
            noteTemplateList.add(amphibia);
            noteTemplateList.add(reptiles);
            noteTemplateList.add(mamal);

            handler.sendEmptyMessage(LOAD_NODE_TEMPLATE_COMPLETE);
        }

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case LOAD_NODE_TEMPLATE_COMPLETE:

                    //刷新adapter
                    adapter.notifyDataSetChanged();

                    break;
            }
        }

    };

    /**
     * 记录记录
     *
     * @return
     */
    private Record createRecord() {

        List<Note> notes = new ArrayList<>();

        Record record = new Record();

        for (List<NoteTemplate> noteTemplates : noteTemplateList) {

            for (NoteTemplate noteTemplate : noteTemplates) {

                if (noteTemplate.isChekced()) {

                    Note note = copyFromTemplate(noteTemplate);
                    note.save();
                    notes.add(note);

                }

            }

        }

        record.setUserId(userService.getUser().getId());
        record.setTime(getTime());
        record.setAddToObservedList(false);
        record.setLat(0.0);
        record.setLon(0.0);
        record.setNotes(notes);

        record.setLat(lat);
        record.setLon(lon);

        return record;
    }

    /**
     * 保存至本地
     * @return
     */
    public boolean saveToLocal() {

        return createRecord().save();

    }

    private void uploadRecord() {

        final Record record = createRecord();

        RecordReq recordReq = copyFromRecord(record);

        recordReq.setAddToObservedList(true);

        recordService.uploadRecord(userService.getToken(), recordReq, new BaseResponseListener<UpLoadRecordVo>(UpLoadRecordVo.class) {

            @Override
            protected void onSuccess(UpLoadRecordVo data) {
                ToastUtil.showToast(getContext(),"上传成功");
                //设置已经上传
                record.setAddToObservedList(true);
                //设置服务器返回的记录id
                record.setNetId(data.getId());
                //保存至本地
                record.save();

                //重置模板
                createNoteTemplate();
            }

            @Override
            protected void onFailed(int code, String message) {
                ToastUtil.showToast(getContext(),message);

                //保存到本地
                record.save();

                //重置模板
                createNoteTemplate();

                ToastUtil.showToast(getContext(),"记录已保存至本地");
            }
        }, new BaseErrorListener(getContext()) {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });

    }


    /**
     * 从笔记模板产生note 对象
     *
     * @param noteTemplate
     * @return
     */
    private Note copyFromTemplate(NoteTemplate noteTemplate) {

        return new Note(noteTemplate.getSpeciesId(),0l, noteTemplate.getRemark()
                , noteTemplate.getSpeciesType(), noteTemplate.getFamily(), noteTemplate.getSpeciesName());
    }

    /**
     * @param record
     * @return
     */
    private RecordReq copyFromRecord(Record record) {

        return new RecordReq(record.getTime(), record.getLat(), record.getLon()
                , record.getAddToObservedList(), "", record.getNotes());

    }

    /**
     * 获取时间
     *
     * @return
     */
    private Long getTime() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Long second = format.parse(year + "-" + month + "-" + day).getTime();

            return second;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0l;

    }


    /**
     * 弹窗提示
     */
    private void showAlertPrompt() {


        final AlertPrompt alertPrompt = AlertPrompt.create(getContext());

        //显示，不然 设置时会空指针
        alertPrompt.show();

        //设置提示 标题、内容
        alertPrompt.setTitle("上传记录");
        alertPrompt.setContent("是否将该记录上传到记录中心");

        //设置监听
        alertPrompt.setOnClickListener("取消", "确定", new AlertPromptOnClickListener() {

            @Override
            public void leftOnClick(View view) {
                alertPrompt.dismiss();
            }

            @Override
            public void rightOnClick(View view) {

                uploadRecord();

                alertPrompt.dismiss();
            }
        });
    }

}
