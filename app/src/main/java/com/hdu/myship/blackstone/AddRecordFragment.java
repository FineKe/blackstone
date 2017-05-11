package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import database.Bird;
import database.Record;
import database.Species;

/**
 * Created by MY SHIP on 2017/3/18.
 * 添加记录fragment
 */

public class AddRecordFragment extends Fragment implements View.OnClickListener{
    private String loginURL="http://api.blackstone.ebirdnote.cn/v1/user/login";
    private String upLoadRecordURL="http://api.blackstone.ebirdnote.cn/v1/record/new";
    private RequestQueue requestQueue;
    private ExpandableListView expandableListView;
    private List<List<Species>> speciesList;
    private List<List<Record>> records;
    private String TAG="AddRecordFragment";

    private TextView save;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile="isLogin";
    private Boolean isLogined;

    private EditText iuputAccount;
    private EditText inputPassword;


    private TextView loginForget;
    private TextView Ok;
    private TextView errorLoginForget;
    private TextView inputAgain;
    private ImageView actionCancel;

    private ImageView showPassword;

    private boolean isShowed=false;

    private SharedPreferences createBasicRecordsSharedPreferences;
    private SharedPreferences.Editor createBasicRecordsEditor;
    private String createBasicRecordsFile="RecordsFileADT";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_record,container,false);
        initData();
        expandableListView= (ExpandableListView) view.findViewById(R.id.add_record_expandListView);
        expandableListView.setAdapter(new MyExpandListViewAdapter(records));
        save= (TextView) view.findViewById(R.id.add_record_titleBar_textView_save);
        initEvents();

        return view;
    }

    private void initEvents() {
        save.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initData() {
        createBasicRecordsSharedPreferences=getActivity().getSharedPreferences(createBasicRecordsFile,Context.MODE_PRIVATE);
        createBasicRecordsEditor=createBasicRecordsSharedPreferences.edit();
        requestQueue=Volley.newRequestQueue(getContext());
        boolean isCreateBasicRecords=createBasicRecordsSharedPreferences.getBoolean("isCreated",false);

        if(isCreateBasicRecords==false)
        {
            createBasicRecords();
            createBasicRecordsEditor.putBoolean("isCreated",true).apply();
        }



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
        if(isLogined==false) {//判断是否登录了
            showLoginDialog();//如果没有则弹出登录框
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
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView==null)
            {
                convertView =getLayoutInflater(null).inflate(R.layout.expand_list_view_father,null);
            }

            TextView textView= (TextView)convertView.findViewById(R.id.expand_lisetView_father_speciesClassChineseName);
            textView.setText(groups[groupPosition]);
            return convertView ;
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

            if(records.get(groupPosition).get(childPosition).isRemarkIsNull())//isRemarkNull默认为true
            {
                addNotes.setImageResource(R.mipmap.pen_normal);
            }else
            {
                addNotes.setImageResource(R.mipmap.pen_pressed);
            }


            addNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getActivity(),AddNotesActivity.class));//跳转到添加笔记界面
                }
            });

            collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!records.get(groupPosition).get(childPosition).isChecked())
                    {

                        collection.setImageResource(R.mipmap.select_pressed);
                        records.get(groupPosition).get(childPosition).setChecked(true);
                        boolean result=records.get(groupPosition).get(childPosition).isSaved();
                        Log.d(TAG, "onClick: "+result);
                    }
                    else
                    {
                        collection.setImageResource(R.mipmap.select_normal);
                        records.get(groupPosition).get(childPosition).setChecked(false);
                        boolean result=records.get(groupPosition).get(childPosition).save();
                        Log.d(TAG, "onClick: "+result);
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

    /**
     * 登录对话框
     */
    public void showLoginDialog()
    {
        final LoginDialog loginDialog=new LoginDialog(getContext(),R.style.LoginDialog,R.layout.login_dialog);
        loginDialog.show();
        /**
         * 绑定控件
         */
        iuputAccount= (EditText) loginDialog.findViewById(R.id.login_dialog_editText_account);
        inputPassword= (EditText) loginDialog.findViewById(R.id.login_dialog_editText_password);
        loginForget= (TextView) loginDialog.findViewById(R.id.login_dialog_textView_forget);
        Ok= (TextView) loginDialog.findViewById(R.id.login_dialog_textView_Ok);
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
        records=new ArrayList<>();
        records.add(birdRecord);
        records.add(amphibiaRecord);
        records.add(reptilesRecord);
        records.add(insectRecord);
    }
}
