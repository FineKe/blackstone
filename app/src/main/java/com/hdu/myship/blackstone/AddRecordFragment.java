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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import database.Species;

/**
 * Created by MY SHIP on 2017/3/18.
 * 添加记录fragment
 */

public class AddRecordFragment extends Fragment implements View.OnClickListener{
    private String loginURL="http://api.blackstone.ebirdnote.cn/v1/user/login";
    private RequestQueue requestQueue;
    private ExpandableListView expandableListView;
    private List<List<Species>> speciesList;
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
    private boolean isCollected=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_record,container,false);
        initData();
        expandableListView= (ExpandableListView) view.findViewById(R.id.add_record_expandListView);
        expandableListView.setAdapter(new MyExpandListViewAdapter(speciesList));
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
        requestQueue=Volley.newRequestQueue(getContext());
        List<Species> bird=DataSupport.where("speciesType=?","bird").find(Species.class);
        List<Species> reptiles=DataSupport.where("speciesType=?","reptiles").find(Species.class);
        List<Species> amphibia=DataSupport.where("speciesType=?","amphibia").find(Species.class);
        List<Species> insect=DataSupport.where("speciesType=?","insect").find(Species.class);
        for(Species s:bird)
        {
            Log.d(TAG, "initData: "+s.getChineseName());
        }
        speciesList=new ArrayList<>();
        speciesList.add(bird);
        speciesList.add(amphibia);
        speciesList.add(reptiles);
        speciesList.add(insect);

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

    private void save() {
        if(isLogined==false) {
            showLoginDialog();
        }
    }

    class MyExpandListViewAdapter extends BaseExpandableListAdapter
    {   private String [] groups={"鸟类","两栖类","爬行类","昆虫"};
        List<List<Species>> list;

        public MyExpandListViewAdapter(List<List<Species>> list) {
            this.list = list;
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return list.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return list.get(groupPosition).get(childPosition);
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
        public View getChildView(int groupPosition, int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView==null)
            {
                convertView=getLayoutInflater(null).inflate(R.layout.expand_list_view_child,null);
            }

            TextView textView= (TextView) convertView.findViewById(R.id.expand_listView_child_speciesChineseName);
            textView.setText(list.get(groupPosition).get(childPosition).getChineseName());
            final ImageView addNotes= (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_addNotes);
            final ImageView collection= (ImageView) convertView.findViewById(R.id.expand_list_view_child_imageView_collection);

            addNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),AddNotesActivity.class));
                }
            });

            collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCollected==false)
                    {
                        isCollected =true;
                        collection.setImageResource(R.mipmap.select_pressed);
                    }
                    else
                    {
                        isCollected=false;
                        collection.setImageResource(R.mipmap.select_normal);
                    }
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public void showLoginDialog()
    {
        final LoginDialog loginDialog=new LoginDialog(getContext(),R.style.LoginDialog,R.layout.login_dialog);
        loginDialog.show();
        iuputAccount= (EditText) loginDialog.findViewById(R.id.login_dialog_editText_account);
        inputPassword= (EditText) loginDialog.findViewById(R.id.login_dialog_editText_password);
        loginForget= (TextView) loginDialog.findViewById(R.id.login_dialog_textView_forget);
        Ok= (TextView) loginDialog.findViewById(R.id.login_dialog_textView_Ok);
        actionCancel= (ImageView) loginDialog.findViewById(R.id.login_dialog_imageView_actionCancel);
        showPassword= (ImageView) loginDialog.findViewById(R.id.login_dialog_imageView_showPassword);



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
            public void onClick(View v) {
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
                                editor.putBoolean("islogined",true).apply();

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

        loginForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void showErrorDialog()
    {
        final LoginDialog errorDialog=new LoginDialog(getContext(),R.style.LoginDialog,R.layout.error_login_dialog);
        errorDialog.show();
        errorLoginForget= (TextView) errorDialog.findViewById(R.id.error_login_dialog_forget);
        inputAgain= (TextView) errorDialog.findViewById(R.id.error_login_dialog_inputAgain);

        errorLoginForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        inputAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
                errorDialog.dismiss();
            }
        });
    }
}
