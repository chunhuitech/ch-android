package com.chunhuitech.reader.callback;

import okhttp3.ResponseBody;

public interface IDownloadCallback {
    void loadFinish(ResponseBody responseBody);
}
