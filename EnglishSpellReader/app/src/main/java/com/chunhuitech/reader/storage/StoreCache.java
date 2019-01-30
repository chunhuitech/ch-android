package com.chunhuitech.reader.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class StoreCache {

    private String baseFilePath;

    public StoreCache(Context context) {
        initCache(context);
    }

    private void initCache(Context context) {
        File baseFile = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (baseFile != null) {
            baseFilePath = baseFile.getAbsolutePath();
        }
    }

    public boolean isExistRes(String bookId, String resId) {
        if (baseFilePath != null) {
            String fileName = bookId + File.separator + "res" + File.separator + resId;
            fileName = baseFilePath + File.separator + fileName;
            File resFile = new File(fileName);
            return resFile.exists();
        }
        return false;
    }

    public File getResFile(String bookId, String resId) {
        if (baseFilePath != null) {
            String fileName = bookId + File.separator + "res" + File.separator + resId;
            fileName = baseFilePath + File.separator + fileName;
            return new File(fileName);
        }
        return null;
    }

    public File getPageImage(String bookId, String pageId) {
        if (baseFilePath != null) {
            String fileName = bookId + File.separator + "page" + File.separator + pageId;
            fileName = baseFilePath + File.separator + fileName;
            return new File(fileName);
        }
        return null;
    }

    public Bitmap getPageImage(String bookId, String page, String imageUrl) {
        File pageFile = getPageImage(bookId, page);
        if (!pageFile.exists()) {
            try {
                URL picUrl = new URL(imageUrl);
                Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());

                if(!pageFile.getParentFile().exists()) {
                    pageFile.getParentFile().mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(pageFile);
                pngBM.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return pngBM;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return BitmapFactory.decodeFile(pageFile.getAbsolutePath());
        }
        return null;
    }


}
