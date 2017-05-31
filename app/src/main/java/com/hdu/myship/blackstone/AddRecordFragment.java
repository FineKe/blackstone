package com.hdu.myship.blackstone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
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
import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Record;
import database.Species;

import static android.content.Context.MODE_PRIVATE;
import static com.hdu.myship.blackstone.R.mipmap.right_arrow;

/**
 * Created by MY SHIP on 2017/3/18.
 * 添加记录fragment
 */

public class AddRecordFragment extends Fragment implements View.OnClickListener {
    private String loginURL = "http://api.blackstone.ebirdnote.cn/v1/user/login";
    private String upLoadRecordURL = "http://api.blackstone.ebirdnote.cn/v1/record/new";
    private RequestQueue requestQueue;
    private JsonObjectRequest upLoadRequest;
    private ExpandableListView expandableListView;
    private List<List<Species>> speciesList;

    private List<List<Record>> records;
    private List<Record> birdRecord;
    private List<Record> amphibiaRecord;
    private List<Record> reptilesRecord;
    private List<Record> insectRecord;

    private String TAG = "AddRecordFragment";

    private LocationManager locationManager;
    private Location location;
    private String privoder;

    private TextView save;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile = "isLogin";
    private Boolean isLogined;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation = "UesrInformation";

    private EditText iuputAccount;
    private EditText inputPassword;


    private TextView loginForget;
    private TextView Ok;
    private TextView errorLoginForget;
    private TextView inputAgain;

    private TextView textViewDate;
    private ImageView actionCancel;

    private ImageView showPassword;

    private boolean isShowed = false;

    private boolean datePickerShow = false;

    private Calendar calendar;
    private DatePicker datePicker;

    private SharedPreferences createBasicRecordsSharedPreferences;
    private SharedPreferences.Editor createBasicRecordsEditor;
    private String createBasicRecordsFile = "RecordsFileADT";

    private int year, month, day;
    private long millisecond;

    private MyExpandListViewAdapter myExpandListViewAdapter;

    private int GROUPPOSITION;
    private int CHILDPOSITION;

    private List<UpdateRecordPosition> updateRecordPositionList;

    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_record, container, false);
        textViewDate = (TextView) view.findViewById(R.id.add_record_titleBar_textView_date);
        datePicker = (DatePicker) view.findViewById(R.id.add_record_datepicker);
        expandableListView = (ExpandableListView) view.findViewById(R.id.add_record_expandListView);
        expandableListView.setAdapter(myExpandListViewAdapter);
        //createBasicRecords();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        textViewDate.setText(year + "年" + (month + 1) + "月" + day + "日");
        month++;
        datePicker.init(year, month - 1, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year_, int monthOfYear, int dayOfMonth) {
                year = year_;
                month = monthOfYear + 1;
                day = dayOfMonth;
                textViewDate.setText(year + "年" + (month) + "月" + day + "日");
            }
        });


//        millisecond=millisecondDate.getTime();//获得毫秒数

        textViewDate.setOnClickListener(new View.OnClickListener() {
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
        save = (TextView) view.findViewById(R.id.add_record_titleBar_textView_save);
        initEvents();

        expandableListView.setGroupIndicator(null);
        return view;
    }

    private void initEvents() {
        save.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        myExpandListViewAdapter = new MyExpandListViewAdapter(records);
    }


    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            List<String> list = locationManager.getProviders(true);
            if (list.contains(LocationManager.GPS_PROVIDER)) {
                privoder = LocationManager.GPS_PROVIDER;
            } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
                privoder = LocationManager.NETWORK_PROVIDER;
            } else {
                Toast.makeText(getContext(), "请检查网络", Toast.LENGTH_SHORT).show();
            }

            location = locationManager.getLastKnownLocation(privoder);
            locationManager.requestLocationUpdates(privoder, 100, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location_) {
                    location = location_;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

    }


    private void initData() {
        createBasicRecordsSharedPreferences = getActivity().getSharedPreferences(createBasicRecordsFile, MODE_PRIVATE);
        createBasicRecordsEditor = createBasicRecordsSharedPreferences.edit();

        userInformationSharedPreferences = getActivity().getSharedPreferences(userInformation, MODE_PRIVATE);
        userInformationEditor = userInformationSharedPreferences.edit();

        requestQueue = Volley.newRequestQueue(getContext());
        boolean isCreateBasicRecords = createBasicRecordsSharedPreferences.getBoolean("isCreated", false);
        records = new ArrayList<>();
        birdRecord = new ArrayList<>();
        amphibiaRecord = new ArrayList<>();
        reptilesRecord = new ArrayList<>();
        insectRecord = new ArrayList<>();
        createBasicRecordsEditor.putBoolean("isCreated", true).apply();
        records = MainActivity.records;

        updateRecordPositionList = new ArrayList<>();

        sharedPreferences = getActivity().getSharedPreferences(isLoginedFile, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isLogined = sharedPreferences.getBoolean("islogined", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record_titleBar_textView_save:
                if (updateRecordPositionList.isEmpty()) {
                    Toast.makeText(getContext(), "你还没有添加记录", Toast.LENGTH_SHORT).show();
                } else {
                    save();
                }

                break;
        }
    }

    /**
     * 保存
     */
    private void save() {
        getLocation();
        isLogined = sharedPreferences.getBoolean("islogined", false);
        if (isLogined == false) {//判断是否登录了
            showLoginDialog();//如果没有则弹出登录框
        } else {

            if (location != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Long second = format.parse(year + "-" + month + "-" + day).getTime();
                    System.out.println(second);
                    millisecond = second;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                        Intent intent=new Intent(getContext(),UploadIntentService.class);
//                        intent.putExtra("lat",location.getLatitude());
//                        intent.putExtra("lon",location.getLongitude());
//                        intent.putExtra("milliseconds",millisecond);
//                        getContext().startService(intent);
                DecimalFormat d = new DecimalFormat("0.0000");
                upLoadData(millisecond, Double.parseDouble(d.format(location.getLatitude())), Double.parseDouble(d.format(location.getLongitude())));
            } else {
                Toast.makeText(getContext(), "获取位置失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class MyExpandListViewAdapter extends BaseExpandableListAdapter {
        private String[] groups = {"鸟类", "两栖类", "爬行类", "昆虫"};
        private List<List<Record>> records;


        public MyExpandListViewAdapter(List<List<Record>> records) {
            this.records = records;
        }


        public MyExpandListViewAdapter() {

        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return records.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return records.get(groupPosition).get(childPosition);
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
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater(null).inflate(R.layout.expand_list_view_father, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.expand_lisetView_father_speciesClassChineseName);
            ImageView im = (ImageView) convertView.findViewById(R.id.expand_lisetView_father_image_view_arrow);
            if (isExpanded) {
                im.setRotation(90);
            } else

            {
                im.setRotation(0);
            }
            textView.setText(groups[groupPosition]);
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater(null).inflate(R.layout.expand_list_view_child, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.expand_listView_child_speciesChineseName);
            textView.setText(records.get(groupPosition).get(childPosition).getChineseName());
            final ImageView addNotes = (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_addNotes);
            final ImageView collection = (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_collection);
            if (!records.get(groupPosition).get(childPosition).isChecked()) {
                collection.setImageResource(R.mipmap.select_normal);
            } else {
                collection.setImageResource(R.mipmap.select_pressed);
            }

            if (records.get(groupPosition).get(childPosition).isRemarkIsNull()) {
                addNotes.setImageResource(R.mipmap.pen_normal);
            } else {
                addNotes.setImageResource(R.mipmap.pen_pressed);
            }

            addNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GROUPPOSITION = groupPosition;
                    CHILDPOSITION = childPosition;
                    startActivityForResult(new Intent(getContext(), AddNotesActivity.class)
                            .putExtra("groupPosition", groupPosition).putExtra("childPosition", childPosition), 1);
                }
            });


            collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!records.get(groupPosition).get(childPosition).isChecked()) {
                        records.get(groupPosition).get(childPosition).setChecked(true);
                        records.get(groupPosition).get(childPosition).save();
                        collection.setImageResource(R.mipmap.select_pressed);
                    } else {
                        records.get(groupPosition).get(childPosition).setChecked(false);
                        records.get(groupPosition).get(childPosition).save();
                        collection.setImageResource(R.mipmap.select_normal);
                    }


                }
            });


            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Boolean isNull=data.getBooleanExtra("isNull",true);
//        if(!isNull)
//        {
//            records.get(GROUPPOSITION).get(CHILDPOSITION).setRemarkIsNull(false);
//            records.get(GROUPPOSITION).get(CHILDPOSITION).setRemark(data.getStringExtra("Remark"));
        myExpandListViewAdapter.notifyDataSetChanged();
//        }else
//        {
//            records.get(GROUPPOSITION).get(CHILDPOSITION).setRemarkIsNull(true);
//            myExpandListViewAdapter.notifyDataSetChanged();
//        }
        updateRecordPositionList.add(new UpdateRecordPosition(GROUPPOSITION, CHILDPOSITION));
        Log.d(TAG, "onActivityResult: " + GROUPPOSITION + ":" + CHILDPOSITION);
    }

    /**
     * 登录对话框
     */
    public void showLoginDialog() {
        final LoginDialog loginDialog = new LoginDialog(getContext(), R.style.LoginDialog, R.layout.login_dialog);
        loginDialog.setCancelable(false);
        loginDialog.show();
        /**
         * 绑定控件
         */
        iuputAccount = (EditText) loginDialog.findViewById(R.id.update_phone_numbe_dialog_editText_account);
        inputPassword = (EditText) loginDialog.findViewById(R.id.update_phone_number_dialog_editText_code);
        loginForget = (TextView) loginDialog.findViewById(R.id.update_phone_number_text_view_get_code);
        Ok = (TextView) loginDialog.findViewById(R.id.update_phone_number_dialog_textView_Ok);
        actionCancel = (ImageView) loginDialog.findViewById(R.id.login_dialog_imageView_actionCancel);
        showPassword = (ImageView) loginDialog.findViewById(R.id.login_dialog_imageView_showPassword);


        //设置密码的密码可见性
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowed) {
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.mipmap.see);
                } else {
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.mipmap.no_see);
                }
                isShowed = !isShowed;
                inputPassword.postInvalidate();
            }
        });

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//登录逻辑处理
                Map<String, String> loginMap = new HashMap<String, String>();
                loginMap.put("username", iuputAccount.getText().toString());
                loginMap.put("pwd", inputPassword.getText().toString());
                JSONObject loginJsonObject = new JSONObject(loginMap);
                requestQueue = Volley.newRequestQueue(getContext());
                JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, loginURL, loginJsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int code = jsonObject.getInt("code");
                            if (code == 88) {
                                loginDialog.dismiss();

                                editor.putBoolean("islogined", true).apply();//向文件中写入已经登录true
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONObject user = data.getJSONObject("user");
                                userInformationEditor.putLong("id", user.getLong("id"));
                                userInformationEditor.putString("mobile", user.getString("mobile"));
                                userInformationEditor.putString("studentId", user.getString("studentId"));
                                userInformationEditor.putString("name", user.getString("name"));
                                userInformationEditor.putString("gender", user.getString("gender"));
                                userInformationEditor.putString("mail", user.getString("mail"));
                                userInformationEditor.putString("token", data.getString("token"));
                                userInformationEditor.putLong("expireAt", data.getLong("expireAt"));
                                userInformationEditor.commit();
                            } else {

                                loginDialog.dismiss();
                                showErrorDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(), "请求异常", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(loginRequest);
            }
        });

        loginForget.setOnClickListener(new View.OnClickListener() {//忘记密码处理逻辑
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
            }
        });
    }


    /**
     * 登录错误对话框
     */
    public void showErrorDialog() {
        final LoginDialog errorDialog = new LoginDialog(getContext(), R.style.LoginDialog, R.layout.error_login_dialog);//创建一个错误dialog
        errorDialog.show();//显示dialog
        /**
         * 绑定控件
         */
        errorLoginForget = (TextView) errorDialog.findViewById(R.id.error_login_dialog_forget);
        inputAgain = (TextView) errorDialog.findViewById(R.id.error_login_dialog_inputAgain);

        /**
         * 设置点击事件
         */
        errorLoginForget.setOnClickListener(new View.OnClickListener() {//忘记密码处理逻辑
            @Override
            public void onClick(View v) {

            }
        });


        inputAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//重新输入逻辑
                showLoginDialog();//显示登录对话框
                errorDialog.dismiss();
            }
        });
    }


    /**
     * 生成用于基础记录的records
     */


    public void resetRecords() {
        int GROUP;
        int CHILD;
        for (UpdateRecordPosition updateRecordPosition : updateRecordPositionList) {
            GROUP = updateRecordPosition.getGroup();
            CHILD = updateRecordPosition.getChild();
            records.get(GROUP).get(CHILD).setRemark("");
            records.get(GROUP).get(CHILD).setRemarkIsNull(true);
            records.get(GROUP).get(CHILD).setChecked(false);
        }
        myExpandListViewAdapter.notifyDataSetChanged();
        updateRecordPositionList.clear();
    }

    private class UpdateRecordPosition {
        private int group;
        private int child;

        public UpdateRecordPosition(int group, int child) {
            this.group = group;
            this.child = child;
        }

        public int getGroup() {
            return group;
        }

        public void setGroup(int group) {
            this.group = group;
        }

        public int getChild() {
            return child;
        }

        public void setChild(int child) {
            this.child = child;
        }
    }

    private void upLoadData(Long milliseconds, double lat, double lon) {
        requestQueue = Volley.newRequestQueue(getContext());
        userInformationSharedPreferences = getActivity().getSharedPreferences(userInformation, MODE_PRIVATE);
        token = userInformationSharedPreferences.getString("token", "");
        long ex = userInformationSharedPreferences.getLong("expireAt", 0);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time", milliseconds);
            jsonObject.put("lat", lat);
            jsonObject.put("lon", lon);
            jsonObject.put("addToObservedList", true);
            jsonObject.put("observationPalName", "");
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i <4; i++) {
                for (Record record : MainActivity.records.get(i)) {
                    if (record.isRemarkIsNull() == false && record.isChecked()) {
                        JSONObject js = new JSONObject();
                        js.put("speciesId", record.getSpeciesId());
                        js.put("remark", record.getRemark());
                        jsonArray.put(js);
                    }
                }
            }
            jsonObject.put("notes", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        upLoadRequest = new JsonObjectRequest(Request.Method.POST, upLoadRecordURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code = jsonObject.getInt("code");
                    if (code == 88) {
                        System.out.println(code);
                        Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                        resetRecords();
                    } else {
                        String message = jsonObject.getString("message");
                        System.out.println(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "请求异常", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("token", token);
                return headers;
            }
        };

        requestQueue.add(upLoadRequest);

    }

}
