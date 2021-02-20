package com.chunhuitech.reader.storage;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chunhuitech.reader.R;
import com.chunhuitech.reader.activity.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private String DB_PATH = "/data/data/com.chunhuitech.reader/databases/";
    private static final String DB_NAME = "chunhuidiandu.db";
    private static DataBaseHelper mDataBaseHelper = null;

    public  static  int VERSION = 15;
    private SQLiteDatabase myDataBase;
    private static  Context myContext;
    private int current_versionCode = 0;

    private MyLog log = new MyLog("db");

    public static synchronized DataBaseHelper getInstance(Context ctx){
        myContext = ctx;
        VERSION = getVersion(ctx);
        if(null == mDataBaseHelper){
            mDataBaseHelper = new DataBaseHelper(ctx,VERSION);
        }
        return mDataBaseHelper;
    }
    private static  int getVersion(Context ctx) {
        int version = 1;
        try {
            ApplicationInfo appInfo = ctx.getPackageManager()
                    .getApplicationInfo(ctx.getPackageName(),
                            PackageManager.GET_META_DATA);
            version= appInfo.metaData.getInt("dbLocalVersion");
        } catch (Exception e) {
            version = 1;
        }
        return version;
    }

    private DataBaseHelper(Context context,int version) {
        super(context, DB_NAME, null, version);
        this.myContext = context;
        //DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }

    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (dbExist && (current_versionCode >= VERSION)) {
        } else {
            try {
                copyDataBase();
                // to set db version need open database
                openDataBase();
                close();
            } catch (IOException e) {
                Log.e(DB_NAME, "Error copying database");
            }
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String databaseFilename = DB_PATH+DB_NAME;
            checkDB =SQLiteDatabase.openDatabase(databaseFilename, null,
                    SQLiteDatabase.OPEN_READONLY);
        }catch(SQLException e){

        }
        if(checkDB!=null){
            checkDB.close();
        }
        return checkDB !=null?true:false;
        /*
        SQLiteDatabase checkDB = null;
        try{
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS
                            | SQLiteDatabase.OPEN_READONLY);
            current_versionCode = checkDB.getVersion();
        }catch(Exception e){
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
        */
    }


    private void copyDataBase() throws IOException{
        String databaseFilenames =DB_PATH+DB_NAME;

        File dir = new File(DB_PATH);
        if(!dir.exists())//判断文件夹是否存在，不存在就新建一个
            dir.mkdir();
        FileOutputStream os = null;

        try{
            os = new FileOutputStream(databaseFilenames);//得到数据库文件的写入流
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        InputStream is = myContext.getResources().openRawResource(R.raw.chunhuidiandu);//得到数据库文件的数据流
        // 分配接收缓冲区
        byte[] buffer = new byte[8192];
        int count = 0;
        try{

            while((count=is.read(buffer))>0){

                os.write(buffer, 0, count);  // 读写文件
                os.flush();
            }
        }catch(IOException e){

        }
        try{
            is.close();
            os.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        /*
        File file = new File(DB_PATH);
        if(file.exists()){
            if(file.delete()){
                Log.d("","删除文件成功");
            }else{
                Log.d("","删除文件失败");
            }
        }

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        */
    }

    public void openDataBase() throws SQLException {
        String databaseFilename = DB_PATH+DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(databaseFilename, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS
                        | SQLiteDatabase.OPEN_READWRITE);
        myDataBase.setVersion(VERSION);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    class MyLog {
        private String TAG = "";
        private boolean isDebug = false;

        public MyLog(String tagName){
            TAG = tagName;
        }

        public void d(String msg){
            if(isDebug){
                Log.d(TAG, msg);
            }
        }
    }

}
