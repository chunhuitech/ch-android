package com.chunhuitech.reader.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.R;
import com.chunhuitech.reader.adapter.BookPageAdapter;
import com.chunhuitech.reader.callback.ILoadDataCallback;
import com.chunhuitech.reader.entity.BaseResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadBookActivity extends AppCompatActivity {

    String bookCnName;
    String bookId;
    int startPage ;
    ViewPager mcContainer;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        bookCnName = getIntent().getStringExtra("bookCnName");
        startPage = getIntent().getIntExtra("page", 0);
        bookId = App.instanceApp().getBookInfo().getBookId();

        setContentView(R.layout.activity_loadbook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(bookCnName);
        setSupportActionBar(toolbar);

        App.instanceApp().getDataService().getBookResource(bookId, new ILoadDataCallback() {
            @Override
            public void loadFinish(BaseResult data) {
                if (data != null) {
                    List<Map<String, Object>> listResource = (List<Map<String, Object>>) data.getData().get("dataList");
                    App.instanceApp().getBookInfo().setListResource(listResource);
                    App.instanceApp().getDownloadService().beginDownloadResource(bookId, listResource, null);
                }
            }
        });

        App.instanceApp().getDataService().getBookPages(bookId, new ILoadDataCallback() {
            @Override
            public void loadFinish(BaseResult data) {
                if (data != null) {
                    List<Map<String, Object>> listPages = (List<Map<String, Object>>) data.getData().get("dataList");
                    fragmentList = new ArrayList<>();
                    for(int i=0; i<listPages.size(); i++) {
                        fragmentList.add(new LoadBookActivityFragment());
                        //fragmentList.add(new LoadBookActivityFragment());
                    }
                    mcContainer.setAdapter(new BookPageAdapter(getSupportFragmentManager(), fragmentList, listPages, bookId));
                }
            }
        });
        mcContainer = findViewById(R.id.page_container);
    }

}
