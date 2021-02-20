package com.chunhuitech.reader;

import android.app.Activity;
import android.content.Context;

import com.chunhuitech.reader.entity.BookInfo;
import com.chunhuitech.reader.service.DataService;
import com.chunhuitech.reader.service.DownloadService;
import com.chunhuitech.reader.service.MessageService;
import com.chunhuitech.reader.service.NetService;
import com.chunhuitech.reader.service.SoundService;
import com.chunhuitech.reader.storage.DBLocalQuery;
import com.chunhuitech.reader.storage.StoreCache;

// 应用类
public class                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              App {

    private NetService netService;
    private MessageService messageService;
    private Activity currentActivity;
    private DataService dataService;
    private StoreCache storeCache;
    private BookInfo bookInfo;
    private DownloadService downloadService;
    private SoundService soundService;
    private DBLocalQuery dbLocalQuery;

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
        this.downloadService = new DownloadService(this.dataService, this.getStoreCache());
    }

    public Context getContext() {
        if (currentActivity != null && !currentActivity.isFinishing()) {
            return currentActivity.getApplicationContext();
        }
        return null;
    }

    public SoundService getSoundService() {
        return this.soundService;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public NetService getNetService() {
        return netService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public DataService getDataService() {
        return dataService;
    }

    public StoreCache getStoreCache() {
        if (storeCache == null) {
            storeCache = new StoreCache(getContext());
        }
        return storeCache;
    }

    public DBLocalQuery getDBLocalQuery() {
        return dbLocalQuery;
    }

    private App() {
        this.netService = new NetService();
        this.messageService = new MessageService();
        this.dataService = new DataService();
        this.bookInfo = new BookInfo();
        this.soundService = new SoundService();
        this.dbLocalQuery = new DBLocalQuery();
    }

    private static App app = null;
    public static App instanceApp() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

}
