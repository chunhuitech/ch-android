package com.chunhuitech.reader.listener;

import android.animation.Animator;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.storage.StoreCache;

import java.io.File;
import java.util.Map;

public class AnimListener implements Animator.AnimatorListener {

    Map<String, Object> selectObj;
    public AnimListener(Map<String, Object> selectObj) {
        this.selectObj = selectObj;
    }

    @Override
    public void onAnimationEnd(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (selectObj != null) {
            String resourceId = selectObj.get("resourceId").toString();
            String bookId = App.instanceApp().getBookInfo().getBookId();
            StoreCache storeCache = App.instanceApp().getStoreCache();
            File mp3File = storeCache.getResFile(bookId, resourceId);
            if (mp3File.exists()) {
                App.instanceApp().getSoundService().playMp3(mp3File, selectObj);
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
