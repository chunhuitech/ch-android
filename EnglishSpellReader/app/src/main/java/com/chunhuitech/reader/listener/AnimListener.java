package com.chunhuitech.reader.listener;

import android.animation.Animator;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.callback.ILoadDataCallback;
import com.chunhuitech.reader.entity.BaseResult;
import com.chunhuitech.reader.storage.StoreCache;

import java.io.File;
import java.util.List;
import java.util.Map;

public class AnimListener implements Animator.AnimatorListener {

    Map<String, Object> selectObj;
    String bookId;
    File mp3File;
    public AnimListener(Map<String, Object> selectObj) {
        this.selectObj = selectObj;
    }

//    @Override
//    public void onAnimationEnd(Animator animation, boolean isReverse) {
//        if (selectObj != null) {
//            String resourceId = selectObj.get("resourceId").toString();
//            String bookId = App.instanceApp().getBookInfo().getBookId();
//            StoreCache storeCache = App.instanceApp().getStoreCache();
//            File mp3File = storeCache.getResFile(bookId, resourceId);
//            if (mp3File.exists()) {
//                App.instanceApp().getSoundService().playMp3(mp3File, selectObj);
//            }
//        }
//    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (selectObj != null) {
            String resourceId = selectObj.get("resourceId").toString();
            bookId = App.instanceApp().getBookInfo().getBookId();
            StoreCache storeCache = App.instanceApp().getStoreCache();
            mp3File = storeCache.getResFile(bookId, resourceId);
            if (mp3File.exists()) {
                App.instanceApp().getSoundService().playMp3(mp3File, selectObj);
            } else{

                App.instanceApp().getDataService().getBookResource(bookId, new ILoadDataCallback() {
                    @Override
                    public void loadFinish(BaseResult data) {
                        if (data != null) {
                            List<Map<String, Object>> listResource = (List<Map<String, Object>>) data.getData().get("dataList");
                            App.instanceApp().getBookInfo().setListResource(listResource);
                            App.instanceApp().getDownloadService().beginDownloadResource(bookId, listResource, mp3File.getAbsolutePath());
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
