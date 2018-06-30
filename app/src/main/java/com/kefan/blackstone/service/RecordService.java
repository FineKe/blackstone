package com.kefan.blackstone.service;

import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.data.req.AlterRecordReq;
import com.kefan.blackstone.data.req.RecordReq;
import com.kefan.blackstone.data.response.BaseResponse;
import com.kefan.blackstone.database.Record;

import java.util.List;

/**
 * @note: 记录service
 * @author: fine
 * @time: 2018/6/18 下午7:43
 */
public interface RecordService {

    /**
     * 保存 record
     * @param record
     * @return
     */
    public boolean saveRecord(Record record);


    /**
     * 获取记录列表
     * @param userId
     * @return
     */
    public List<Record> list(Long userId);

    /**
     * 通过远程记录id差找
     * @param netId
     * @return
     */
    public Record findRecordByNetId(Long netId);


    public boolean updateRecord(Record record);

    /**
     * 通过id 查找record
     * @param id
     * @return
     */
    public Record findRecordById(Long id);

    /**
     * 上传记录
     * @param token token
     * @param recordReq 记录
     * @param baseResponseListener
     * @param errorListener
     */
    public void uploadRecord(String token, RecordReq recordReq, BaseResponseListener baseResponseListener, BaseErrorListener errorListener);

    /**
     * 删除记录
     * @param token
     * @param recordId
     * @param baseResponseListener
     * @param errorListener
     */
    public void deleteRecord(String token,String recordId,BaseResponseListener baseResponseListener, BaseErrorListener errorListener);

    /**
     * 获取记录列表
     * @param token
     * @param userId
     * @param listener
     * @param errorListener
     */
    public void getRecords(String token,String userId,BaseResponseListener listener,BaseErrorListener errorListener);

    /**
     * 修改记录
     * @param token
     * @param alterRecordReq
     * @param listener
     * @param errorListener
     */
    public void alterRecord(String token, AlterRecordReq alterRecordReq,BaseResponseListener listener,BaseErrorListener errorListener);


    /**
     * 获取记录详情
     * @param token
     * @param recordId
     * @param listener
     * @param errorListener
     */
    public void getRecordDetailed(String token, Long recordId, BaseResponseListener listener, BaseErrorListener errorListener);
}
