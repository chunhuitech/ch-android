package com.chunhuitech.reader.service;

import android.os.AsyncTask;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.callback.IDownloadCallback;
import com.chunhuitech.reader.storage.StoreCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class DownloadService {

    private DataService dataService;
    private StoreCache storeCache;

    public DownloadService(DataService dataService, StoreCache storeCache) {
        this.dataService = dataService;
        this.storeCache = storeCache;
    }

    public void downLoadRes(Map<String, Object> resObject, String savePath) {

    }

    public void beginDownloadResource(String bookId, List<Map<String, Object>> listResource, String filePath) {
        //for (Map<String, Object> resObj : listResource) {
        if (filePath == null && listResource != null && listResource.size() > 0){
            Map<String, Object> resObj = listResource.get(0);
            String id = resObj.get("id").toString();
            final File resFile = storeCache.getResFile(bookId, id);
            filePath = resFile.getAbsolutePath();
            String path1 = resFile.getPath();
        }
        for (int i = 0; i < listResource.size(); i++) {
            Map<String, Object> resObj = listResource.get(i);
            String id = resObj.get("id").toString();
            final String url = resObj.get("relativePath").toString();
            final File resFile = storeCache.getResFile(bookId, id);
            if(resFile.getAbsolutePath().equals(filePath)) {
                if (!resFile.exists()) {
                    App.instanceApp().getMessageService().showWaitingDialog();
                    new AsyncTask<Void, Long, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            dataService.downloadResource(url, new IDownloadCallback() {
                                @Override
                                public void loadFinish(ResponseBody responseBody) {
                                    if (responseBody != null)  {
                                        InputStream inputStream = null;
                                        OutputStream outputStream = null;
                                        try {
                                            if(!resFile.getParentFile().exists()) {
                                                resFile.getParentFile().mkdirs();
                                            }

                                            byte[] fileReader = new byte[4096];
                                            long fileSize = responseBody.contentLength();
                                            long fileSizeDownloaded = 0;
                                            inputStream = responseBody.byteStream();
                                            outputStream = new FileOutputStream(resFile);
                                            while (true) {
                                                int read = inputStream.read(fileReader);
                                                if (read == -1) {
                                                    break;
                                                }
                                                outputStream.write(fileReader, 0, read);
                                                fileSizeDownloaded += read;
                                            }
                                            outputStream.flush();
                                            App.instanceApp().getMessageService().hideWaitingDialog();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } finally {
                                            if (inputStream != null) {
                                                try {
                                                    inputStream.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if (outputStream != null) {
                                                try {
                                                    outputStream.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            return null;
                        }
                    }.execute();
                }
            }

        }
    }

}
