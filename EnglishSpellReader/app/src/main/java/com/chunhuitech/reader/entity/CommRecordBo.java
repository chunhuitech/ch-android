package com.chunhuitech.reader.entity;

import java.util.List;

public class CommRecordBo {
    List<CommRecord> dataList;
    long lastModTime;

    public List<CommRecord> getDataList() {
        return dataList;
    }

    public void setDataList(List<CommRecord> dataList) {
        this.dataList = dataList;
    }

    public long getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(long lastModTime) {
        this.lastModTime = lastModTime;
    }
}