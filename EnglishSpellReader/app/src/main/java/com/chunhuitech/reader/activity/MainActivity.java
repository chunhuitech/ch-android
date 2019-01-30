package com.chunhuitech.reader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.R;
import com.chunhuitech.reader.adapter.ListViewAdapter;
import com.chunhuitech.reader.callback.ActivityLifecycleCallback;
import com.chunhuitech.reader.callback.ILoadDataCallback;
import com.chunhuitech.reader.entity.BaseResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView mListChildren;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.book_local);
                    mListChildren.setVisibility(View.INVISIBLE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_know:
                    mTextMessage.setVisibility(View.INVISIBLE);
                    mListChildren.setVisibility(View.VISIBLE);
                    initListView(1100);
                    return true;
                case R.id.navigation_teaching:
                    mTextMessage.setVisibility(View.INVISIBLE);
                    mListChildren.setVisibility(View.VISIBLE);
                    initListView(1200);
                    return true;
                case R.id.navigation_notifications:
                    mListChildren.setVisibility(View.INVISIBLE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.book_my);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 系统初始化
        App app = App.instanceApp();

        // 注册监听
        getApplication().registerActivityLifecycleCallbacks(new ActivityLifecycleCallback(app));

        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        mListChildren = (ListView) findViewById(R.id.listChildren);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initListView(int id) {
        App.instanceApp().getDataService().getChildren(id, new ILoadDataCallback() {
            @Override
            public void loadFinish(BaseResult data) {
                List<HashMap<String, Object>> listData = (List<HashMap<String, Object>>)data.getData().get("dataList");
                ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext(), R.layout.list_item, listData);
                mListChildren.setAdapter(listViewAdapter);

                mListChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int itemId = (int)mListChildren.getAdapter().getItemId(position);
                        Map<String, Object> data = (Map<String, Object>)mListChildren.getAdapter().getItem(position);
                        if ((int)Float.parseFloat(data.get("leaf").toString()) == 1) {
                            Intent startIntent = new Intent(MainActivity.this, LoadBookActivity.class);
                            startIntent.putExtra("bookCnName", data.get("cnName").toString());
                            startIntent.putExtra("page", 1);
                            App.instanceApp().getBookInfo().setBookId(data.get("id").toString());
                            startActivity(startIntent);
                        } else {
                            initListView(itemId);
                        }
                    }
                });
            }
        });
    }

}
