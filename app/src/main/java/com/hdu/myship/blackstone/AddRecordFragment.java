package com.hdu.myship.blackstone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Record;
import database.Species;

import static com.hdu.myship.blackstone.R.mipmap.right_arrow;

/**
 * Created by MY SHIP on 2017/3/18.
 * 添加记录fragment
 */

public class AddRecordFragment extends Fragment implements View.OnClickListener {
    private String loginURL = "http://api.blackstone.ebirdnote.cn/v1/user/login";

    private RequestQueue requestQueue;
    private ExpandableListView expandableListView;
    private List<List<Species>> speciesList;
    private List<List<Record>> records;
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
    private String userInformation="UesrInformation";

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

    private boolean datePickerShow=false;

    private Calendar calendar;
    private DatePicker datePicker;

    private SharedPreferences createBasicRecordsSharedPreferences;
    private SharedPreferences.Editor createBasicRecordsEditor;
    private String createBasicRecordsFile = "RecordsFileADT";

    private int year,month,day;
    private long millisecond;

    private int fatherPosition;
    private int childPosition_;

    private MyExpandListViewAdapter myExpandListViewAdapter;


    private View mview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_record, container, false);
       // initData();
        textViewDate= (TextView) view.findViewById(R.id.add_record_titleBar_textView_date);
        datePicker= (DatePicker) view.findViewById(R.id.add_record_datepicker);
        expandableListView = (ExpandableListView) view.findViewById(R.id.add_record_expandListView);
        expandableListView.setAdapter(myExpandListViewAdapter);
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month++;
        textViewDate.setText(year+"年"+month+"月"+day+"日");

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year_, int monthOfYear, int dayOfMonth) {
                year=year_;
                month=monthOfYear;
                day=dayOfMonth;
                textViewDate.setText(year+"年"+month+"月"+day+"日");
            }
        });

        Date millisecondDate=new Date(year,month,day);
        millisecond=millisecondDate.getTime();//获得毫秒数

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datePickerShow==false){
                    datePicker.setVisibility(View.VISIBLE);
                    datePickerShow=true;
                }
                else {
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
        myExpandListViewAdapter=new MyExpandListViewAdapter(records);




    }


    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }else
        {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            List<String> list = locationManager.getProviders(true);
            if (list.contains(LocationManager.GPS_PROVIDER)) {
                privoder = LocationManager.GPS_PROVIDER;
            } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
                privoder = LocationManager.NETWORK_PROVIDER;
            } else {
                Toast.makeText(getContext(), "请检查网络", Toast.LENGTH_SHORT).show();
            }

            location= locationManager.getLastKnownLocation(privoder);
            locationManager.requestLocationUpdates(privoder, 100, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location_) {
                    location=location_;
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
        createBasicRecordsSharedPreferences=getActivity().getSharedPreferences(createBasicRecordsFile,Context.MODE_PRIVATE);
        createBasicRecordsEditor=createBasicRecordsSharedPreferences.edit();

        userInformationSharedPreferences=getActivity().getSharedPreferences(userInformation,Context.MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();

        requestQueue=Volley.newRequestQueue(getContext());
        boolean isCreateBasicRecords=createBasicRecordsSharedPreferences.getBoolean("isCreated",false);
        records=new ArrayList<>();
      if(isCreateBasicRecords==false)
        {

        }

        createBasicRecords();
        createBasicRecordsEditor.putBoolean("isCreated",true).apply();




        sharedPreferences=getActivity().getSharedPreferences(isLoginedFile, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        isLogined=sharedPreferences.getBoolean("islogined",false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_record_titleBar_textView_save:save();
                break;
        }
    }

    /**
     * 保存
     */
    private void save() {
        isLogined=sharedPreferences.getBoolean("islogined",false);
        getLocation();
        if(isLogined==false) {//判断是否登录了
            showLoginDialog();//如果没有则弹出登录框

            if(location!=null)
            {
                System.out.println(location.toString());
                Intent intent=new Intent(getContext(),UploadIntentService.class);
                intent.putExtra("lat",location.getLatitude());
                intent.putExtra("lon",location.getLongitude());
                intent.putExtra("milliseconds",millisecond);
                getContext().startService(intent);
            }else {
                Toast.makeText(getContext(), "获取位置失败", Toast.LENGTH_SHORT).show();
            }

        }else{

            if(location!=null)
            {

                Intent intent=new Intent(getContext(),UploadIntentService.class);
                intent.putExtra("lat",location.getLatitude());
                intent.putExtra("lon",location.getLongitude());
                intent.putExtra("milliseconds",millisecond);
                getContext().startService(intent);
            }else {
                Toast.makeText(getContext(), "获取位置失败", Toast.LENGTH_SHORT).show();
            }
        }
        /**
         * 开启一个服务用与上传记录
         */



        //createBasicRecords();//重置一下
    }



    class MyExpandListViewAdapter extends BaseExpandableListAdapter
    {   private String [] groups={"鸟类","两栖类","爬行类","昆虫"};
        private List<List<Record>> records;

        public MyExpandListViewAdapter( List<List<Record>>  records) {
            this.records = records;
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
            if (convertView==null)
            {
                convertView =getLayoutInflater(null).inflate(R.layout.expand_list_view_father,null);
            }

            TextView textView= (TextView)convertView.findViewById(R.id.expand_lisetView_father_speciesClassChineseName);
            ImageView im= (ImageView) convertView.findViewById(R.id.expand_lisetView_father_image_view_arrow);
            if(isExpanded)
            {
                im.setRotation(90);
            }else

            {
                im.setRotation(0);
            }
            textView.setText(groups[groupPosition]);
            return convertView ;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView==null)
            {
                convertView=getLayoutInflater(null).inflate(R.layout.expand_list_view_child,null);
            }

            TextView textView= (TextView) convertView.findViewById(R.id.expand_listView_child_speciesChineseName);
            textView.setText(records.get(groupPosition).get(childPosition).getChineseName());
            final ImageView addNotes= (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_addNotes);
            final ImageView collection= (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_collection);
            if(!records.get(groupPosition).get(childPosition).isChecked())
            {
                collection.setImageResource(R.mipmap.select_normal);
            }else
            {
                collection.setImageResource(R.mipmap.select_pressed);
            }

            if(records.get(groupPosition).get(childPosition).isRemarkIsNull())
            {
                addNotes.setImageResource(R.mipmap.pen_normal);
            }else
            {
                addNotes.setImageResource(R.mipmap.pen_pressed);
            }

               addNotes.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       fatherPosition=groupPosition;
                       childPosition_=childPosition;

                      startActivityForResult(new Intent(getContext(),AddNotesActivity.class).putExtra("speciesId",records.get(groupPosition).get(childPosition).getSpeciesId()),1);
                   }
               });


            collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!records.get(groupPosition).get(childPosition).isChecked())
                    {
                        records.get(groupPosition).get(childPosition).setChecked(true);
                        records.get(groupPosition).get(childPosition).save();
                        collection.setImageResource(R.mipmap.select_pressed);
                    }else
                    {
                        records.get(groupPosition).get(childPosition).setChecked(false);
                        records.get(groupPosition).get(childPosition).save();
                        collection.setImageResource(R.mipmap.select_normal);
                    }
                }
            });


            mview=convertView;
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
        Toast.makeText(getContext(),"sdasdasdasdas",Toast.LENGTH_SHORT).show();
        ImageView pen= (ImageView) mview.findViewById(R.id.expand_list_view_child_imageView_addNotes);
        pen.setImageResource(R.mipmap.pen_pressed);
        myExpandListViewAdapter.notifyDataSetChanged();
    }

    /**
     * 登录对话框
     */
    public void showLoginDialog()
    {
        final LoginDialog loginDialog=new LoginDialog(getContext(),R.style.LoginDialog,R.layout.login_dialog);
        loginDialog.setCancelable(false);
        loginDialog.show();
        /**
         * 绑定控件
         */
        iuputAccount= (EditText) loginDialog.findViewById(R.id.update_phone_numbe_dialog_editText_account);
        inputPassword= (EditText) loginDialog.findViewById(R.id.update_phone_number_dialog_editText_code);
        loginForget= (TextView) loginDialog.findViewById(R.id.update_phone_number_text_view_get_code);
        Ok= (TextView) loginDialog.findViewById(R.id.update_phone_number_dialog_textView_Ok);
        actionCancel= (ImageView) loginDialog.findViewById(R.id.login_dialog_imageView_actionCancel);
        showPassword= (ImageView) loginDialog.findViewById(R.id.login_dialog_imageView_showPassword);


        //设置密码的密码可见性
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowed)
                {
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.mipmap.see);
                }
                else
                {
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.mipmap.no_see);
                }
                isShowed=!isShowed;
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
                Map<String,String> loginMap=new HashMap<String, String>();
                loginMap.put("username",iuputAccount.getText().toString());
                loginMap.put("pwd",inputPassword.getText().toString());
                JSONObject loginJsonObject=new JSONObject(loginMap);
                requestQueue=Volley.newRequestQueue(getContext());
                JsonObjectRequest loginRequest=new JsonObjectRequest(Request.Method.POST, loginURL, loginJsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int code=jsonObject.getInt("code");
                            if(code==88)
                            {
                                loginDialog.dismiss();

                                editor.putBoolean("islogined",true).apply();//向文件中写入已经登录true
                                JSONObject data=jsonObject.getJSONObject("data");
                                JSONObject user=data.getJSONObject("user");
                                userInformationEditor.putLong("id",user.getLong("id"));
                                userInformationEditor.putString("mobile",user.getString("mobile"));
                                userInformationEditor.putString("studentId",user.getString("studentId"));
                                userInformationEditor.putString("name",user.getString("name"));
                                userInformationEditor.putString("gender",user.getString("gender"));
                                userInformationEditor.putString("mail",user.getString("mail"));
                                userInformationEditor.putString("token",data.getString("token"));
                                userInformationEditor.putLong("expireAt",data.getLong("expireAt"));
                                userInformationEditor.commit();
                            }
                            else
                            {

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
                        Toast.makeText(getContext(),"请求异常",Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(loginRequest);
            }
        });

        loginForget.setOnClickListener(new View.OnClickListener() {//忘记密码处理逻辑
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ForgetPasswordActivity.class));
            }
        });
    }

    /**
     * 登录错误对话框
     */
    public void showErrorDialog()
    {
        final LoginDialog errorDialog=new LoginDialog(getContext(),R.style.LoginDialog,R.layout.error_login_dialog);//创建一个错误dialog
        errorDialog.show();//显示dialog
        /**
         * 绑定控件
         */
        errorLoginForget= (TextView) errorDialog.findViewById(R.id.error_login_dialog_forget);
        inputAgain= (TextView) errorDialog.findViewById(R.id.error_login_dialog_inputAgain);

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
    private void createBasicRecords()
    {
        /**
         * 先将物种分类
         */
        List<Species> bird=DataSupport.where("speciesType=?","bird").find(Species.class);
        List<Species> reptiles=DataSupport.where("speciesType=?","reptiles").find(Species.class);
        List<Species> amphibia=DataSupport.where("speciesType=?","amphibia").find(Species.class);
        List<Species> insect=DataSupport.where("speciesType=?","insect").find(Species.class);

        /*speciesList=new ArrayList<>();
        speciesList.add(bird);
        speciesList.add(amphibia);
        speciesList.add(reptiles);
        speciesList.add(insect);
        */

        /**
         * 创建对应的list
         */
        List<Record> birdRecord=new ArrayList<>();
        List<Record> reptilesRecord=new ArrayList<>();
        List<Record> amphibiaRecord=new ArrayList<>();
        List<Record> insectRecord=new ArrayList<>();

        /**
         * 填充数据
         */
        for(Species species:bird)
        {
            Record record=new Record();
            record.setChineseName(species.getChineseName());
            record.setSpeciesId(species.getId());
            record.save();
            birdRecord.add(record);
        }

        for(Species species:reptiles)
        {
            Record record=new Record();
            record.setChineseName(species.getChineseName());
            record.setSpeciesId(species.getId());
            record.save();
            reptilesRecord.add(record);
        }

        for(Species species:amphibia)
        {
            Record record=new Record();
            record.setChineseName(species.getChineseName());
            record.setSpeciesId(species.getId());
            record.save();
            amphibiaRecord.add(record);
        }

        for(Species species:insect)
        {
            Record record=new Record();
            record.setChineseName(species.getChineseName());
            record.setSpeciesId(species.getId());
            record.save();
            insectRecord.add(record);
        }

        /**
         * 将list添加至recordList
         */

        records.add(birdRecord);
        records.add(amphibiaRecord);
        records.add(reptilesRecord);
        records.add(insectRecord);
    }


}
