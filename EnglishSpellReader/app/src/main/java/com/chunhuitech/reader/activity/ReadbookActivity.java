package com.chunhuitech.reader.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.chunhuitech.reader.R;

public class ReadbookActivity extends AppCompatActivity {

    String bookCnName;
    String bookId;
    int startPage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readbook);

        bookCnName = getIntent().getStringExtra("bookCnName");
        bookId = getIntent().getStringExtra("bookId");
        startPage = getIntent().getIntExtra("page", 0);

    }
}
