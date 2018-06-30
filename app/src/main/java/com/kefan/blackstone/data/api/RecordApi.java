package com.kefan.blackstone.data.api;

import android.content.Context;

import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.data.req.AlterRecordReq;
import com.kefan.blackstone.data.req.RecordReq;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.util.JsonUtil;
import com.qiniu.android.utils.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/22 下午9:46
 */
public class RecordApi extends BaseApi {

    public RecordApi(Context context) {
        super(context);
    }

    /**
     * 上传记录
     * @param token
     * @param recordReq
     * @param listener
     * @param errorListener
     * @return
     */
    public JsonObjectRequest uploadRecord(String token, RecordReq recordReq, BaseResponseListener listener, BaseErrorListener errorListener) {

        if (token != null && token.length() > 0) {

            Map<String,String> header=new HashMap<>();

            header.put("token",token);

            return baseJsonObjectPostRequestWithHeader(context, APIManager.UPLOAD_RECORD, JsonUtil.convertObject(recordReq),listener,errorListener,header);
        }

        return null;
    }


    /**
     * 删除记录
     * @param token
     * @param recordId
     * @param listener
     * @param errorListener
     * @return
     */
    public JsonObjectRequest deleteRecord(String token,String recordId,BaseResponseListener listener,BaseErrorListener errorListener) {

        if (token != null && token.length() > 0) {

            Map<String,String> header=new HashMap<>();

            header.put("token",token);

            return baseJsonObjectDeleteRequestWithHeader(context, APIManager.DELETE_RECORD+recordId,null,listener,errorListener,header);
        }
        return null;

    }

    /**
     * 获取记录列表
     * @param token
     * @param userId
     * @param listener
     * @param errorListener
     * @return
     */
    public JsonObjectRequest getRecords(String token,String userId,BaseResponseListener listener,BaseErrorListener errorListener) {

        if (token != null && token.length() > 0) {

            Map<String,String> header=new HashMap<>();

            header.put("token",token);

            return baseJsonObjectGetRequestWithHeader(context,APIManager.PULL_RECORDS+userId,listener,errorListener,header);
        }
        return null;
    }

    /**
     * 修改记录
     * @param token
     * @param alterRecordReq
     * @param listener
     * @param errorListener
     * @return
     */
    public JsonObjectRequest alterRecord(String token, AlterRecordReq alterRecordReq,BaseResponseListener listener,BaseErrorListener errorListener) {

        if (token != null && token.length() > 0) {

            Map<String,String> header=new HashMap<>();

            header.put("token",token);

            return baseJsonObjectPostRequestWithHeader(context,APIManager.ALTER_RECORD,JsonUtil.convertObject(alterRecordReq),listener,errorListener,header);
        }
        return null;
    }

    /**
     * 获取记录详情
     * @param token
     * @param recordId
     * @param listener
     * @param errorListener
     * @return
     */
    public JsonObjectRequest getRecordDetailed(String token,Long recordId,BaseResponseListener listener,BaseErrorListener errorListener) {

        if (token != null && token.length() > 0) {

            Map<String, String> header = new HashMap<>();

            header.put("token", token);

            return baseJsonObjectGetRequestWithHeader(context, APIManager.RECORD_DETAILED + recordId, listener, errorListener, header);
        }

        return null;

    }
}
