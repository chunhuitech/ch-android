package com.chunhuitech.reader.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chunhuitech.reader.entity.BaseResult;
import com.chunhuitech.reader.entity.CommRecord;
import com.chunhuitech.reader.entity.CommRecordBo;
import com.chunhuitech.reader.entity.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Response;

public class DBLocalQuery {
    public static int MAX_DATA_SIZE = 1000;
    private Context mContext;
    private SQLiteDatabase mDb;//本地数据库;

    public DBLocalQuery(){

    }

    public void init(Context context) {
        mContext = context;
        if(mDb==null){
//			DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
            DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(mContext);
            dataBaseHelper.createDataBase();
            mDb = dataBaseHelper.getReadableDatabase();
        }
    }

    public Result<CommRecordBo> getBookPages(String classId, Integer currentPageString ){
        //Response<BaseResult> response = new Response<BaseResult>();
//        Map<String, Object> data = new HashMap<String, Object>();
//        BaseResult baseResult = new BaseResult();
//        baseResult.setCode(200);
//        baseResult.setData(data);

        CommRecordBo modelResult = new CommRecordBo();

        List<CommRecord> modelList = new ArrayList<CommRecord>();
        Cursor cursor = mDb.query(true, "comm_record",
                null, "class_id=?",
                new String[] { classId }, null, null, "sort_num asc",
                String.valueOf(currentPageString) + "," + String.valueOf(MAX_DATA_SIZE) , null);
        while (cursor.moveToNext()) {
            CommRecord commRecord = new CommRecord();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            commRecord.setId(id);
            String title = cursor.getString(cursor.getColumnIndex("title"));
            commRecord.setTitle(title);
            String label = cursor.getString(cursor.getColumnIndex("label"));
            commRecord.setLabel(label);
            // relative_path file_size file_type content_html content_plain status modify_time create_time
            commRecord.setModifyTime(cursor.getLong(cursor.getColumnIndex("modify_time")));
            modelList.add(commRecord);
        }
        if (cursor != null) cursor.close();


        modelResult.setDataList(modelList);
        long maxTime = 0;
        for (CommRecord commRecord : modelList) {
            maxTime = maxTime >= commRecord.getModifyTime() ? maxTime : commRecord.getModifyTime();
        }
        modelResult.setLastModTime(maxTime);
        return new Result<>(modelResult);
    }
}
