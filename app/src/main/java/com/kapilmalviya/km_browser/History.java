package com.kapilmalviya.km_browser;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    RecyclerView recycler ;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recycler = (RecyclerView)findViewById(R.id.Myrecycler);
        try{
            adapter = new MyAdapter(getBaseContext(),new MainActivity().listhistory);
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        }catch (Exception e){
            Toast.makeText(History.this, "ERROR:" + e, Toast.LENGTH_SHORT).show();
        }

    }


}
