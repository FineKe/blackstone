package com.kefan.blackstone.ui.activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;

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

import java.util.HashMap;
import java.util.Map;

import database.Record;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadIntentService extends IntentService {
    private String upLoadRecordURL = "http://api.blackstone.ebirdnote.cn/v1/record/new";
    private RequestQueue requestQueue;
    private double lat;
    private double lon;
    private Long milliseconds;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";
    private String token;

    private JsonObjectRequest upLoadRequest;
    String TAG="UploadIntentService";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.kefan.blackstone.action.FOO";
    private static final String ACTION_BAZ = "com.kefan.blackstone.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.kefan.blackstone.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.kefan.blackstone.extra.PARAM2";

    public UploadIntentService() {
        super("UploadIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UploadIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UploadIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        lat=intent.getDoubleExtra("lat",0.0);
        lon=intent.getDoubleExtra("lon",0.0);
        milliseconds=intent.getLongExtra("milliseconds",0);
        Log.d(TAG, "onHandleIntent: "+lat+":"+lon);
        Log.d(TAG, "onHandleIntent: "+intent.getLongExtra("milliseconds",0));
        initData();


    }



    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void initData()
    {
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        userInformationSharedPreferences=getSharedPreferences(userInformation,MODE_PRIVATE);
        token=userInformationSharedPreferences.getString("token","");
        long ex=userInformationSharedPreferences.getLong("expireAt",0);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("time",milliseconds);
            jsonObject.put("lat",lat);
            jsonObject.put("lon",lon);
            jsonObject.put("addToObservedList",true);
            jsonObject.put("observationPalName","");
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<3;i++)
            {
                for(Record record: MainActivity.records.get(i))
                {
                    if(record.isRemarkIsNull()==false&&record.isChecked())
                    {
                        JSONObject js=new JSONObject();
                        js.put("speciesId",record.getSpeciesId());
                        js.put("remark",record.getRemark());
                        jsonArray.put(js);
                    }
                }
            }
            jsonObject.put("notes",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        upLoadRequest=new JsonObjectRequest(Request.Method.POST, upLoadRecordURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        System.out.println(code);
                    }else
                    {
                        String message=jsonObject.getString("message");
                        System.out.println(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("token",token);
                return headers;
            }
        };

        requestQueue.add(upLoadRequest);

    }



}
