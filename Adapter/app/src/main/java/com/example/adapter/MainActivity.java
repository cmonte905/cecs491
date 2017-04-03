package com.example.adapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {
    ListView list;
    String[] web = {"PAWN", "ROOK", "KNIGHT", "BISHOP", "QUEEN", "KING"};
    Integer[] imageId = {R.drawable.pawn, R.drawable.rook, R.drawable.knight,
                         R.drawable.bishop, R.drawable.queen, R.drawable.king};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomList adapter = new CustomList(MainActivity.this, web, imageId);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " + web[+position],
                               Toast.LENGTH_SHORT).show();
            }
        });
    }
}
