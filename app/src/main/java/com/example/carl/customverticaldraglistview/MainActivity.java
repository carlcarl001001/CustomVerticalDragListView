package com.example.carl.customverticaldraglistview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> mItems ;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list_view);
        mItems = new ArrayList<String>();
        for(int i=0;i<20;i++){
            mItems.add("i:"+i);
        }
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(MainActivity.this).
                        inflate(R.layout.item_lv,parent,false);
                TextView item = view.findViewById(R.id.item_tv);
                //String s= mItems.get(position);
                item.setText(mItems.get(position));
                return item;
            }
        });
    }
}














