package com.kefan.blackstone.service.impl;

import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.data.api.RecordApi;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.data.req.AlterRecordReq;
import com.kefan.blackstone.data.req.RecordReq;
import com.kefan.blackstone.data.response.BaseResponse;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.service.RecordService;
import com.qiniu.android.utils.Json;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/18 下午7:47
 */
public class RecordServiceImpl extends BaseServiceImpl implements RecordService {


    private RecordApi recordApi;

    public RecordServiceImpl() {

        recordApi=new RecordApi(context);

    }

    @Override
    public boolean saveRecord(Record record) {
        return record.save();
    }

    @Override
    public List<Record> list(Long userId) {
        return DataSupport.where("userId=?",String.valueOf(userId)).order("time").find(Record.class,true);
    }

    @Override
    public Record findRecordByNetId(Long netId) {
        return DataSupport.where("netId=?",String.valueOf(netId)).findFirst(Record.class,true);
    }

    @Override
    public boolean updateRecord(Record record) {
        return record.save();
    }

    @Override
    public Record findRecordById(Long id) {
        return DataSupport.where("id=?",String.valueOf(id)).findFirst(Record.class,true);
    }

    @Override
    public void uploadRecord(String token, RecordReq recordReq, BaseResponseListener baseResponseListener, BaseErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest=recordApi.uploadRecord(token,recordReq,baseResponseListener,errorListener);

        if (checkRequest(context, jsonObjectRequest)) {

            requestQueue.add(jsonObjectRequest);

        }

    }

    public void deleteRecord(String token, String recordId, BaseResponseListener listener,BaseErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = recordApi.deleteRecord(token,recordId,listener,errorListener);

        if (checkRequest(context, jsonObjectRequest)) {

            requestQueue.add(jsonObjectRequest);

        }
    }

    @Override
    public void getRecords(String token, String userId, BaseResponseListener listener, BaseErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest=recordApi.getRecords(token,userId,listener,errorListener);

        if (checkRequest(context, jsonObjectRequest)) {

            requestQueue.add(jsonObjectRequest);

        }
    }

    @Override
    public void alterRecord(String token, AlterRecordReq alterRecordReq, BaseResponseListener listener, BaseErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest=recordApi.alterRecord(token,alterRecordReq,listener,errorListener);

        if (checkRequest(context, jsonObjectRequest)) {

            requestQueue.add(jsonObjectRequest);

        }
    }

    @Override
    public void getRecordDetailed(String token, Long recordId, BaseResponseListener listener, BaseErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest=recordApi.getRecordDetailed(token,recordId,listener,errorListener);

        if (checkRequest(context, jsonObjectRequest)) {

            requestQueue.add(jsonObjectRequest);
        }
    }


}
