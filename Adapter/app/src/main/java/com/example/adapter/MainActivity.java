package com.example.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {
   ListView list;
   String[] web = {"PAWN", "ROOK", "KNIGHT", "BISHOP", "QUEEN", "KING"};
   /*Integer[] imageId = {R.drawable.pawn, R.drawable.rook, R.drawable.knight,
                        R.drawable.bishop, R.drawable.queen, R.drawable.king};*/

   String[] imageURL = {
      "https://www.iconexperience.com/_img/o_collection_png/green_dark_grey/512x512/plain/chess_piece_pawn.png",
      "https://www.iconexperience.com/_img/o_collection_png/green_dark_grey/512x512/plain/chess_piece_rook.png",
      "https://www.iconexperience.com/_img/o_collection_png/green_dark_grey/512x512/plain/chess_piece_knight.png",
      "https://www.iconexperience.com/_img/o_collection_png/green_dark_grey/512x512/plain/chess_piece_bishop.png",
      "https://www.iconexperience.com/_img/o_collection_png/green_dark_grey/512x512/plain/chess_piece_queen.png",
      "https://www.iconexperience.com/_img/o_collection_png/green_dark_grey/512x512/plain/chess_piece_king.png"
   };

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //CustomList adapter = new CustomList(MainActivity.this, web, imageId);
      CustomList adapter = new CustomList(MainActivity.this, web, imageURL);
      list = (ListView) findViewById(R.id.list);
      list.setAdapter(adapter);
      list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, "You Clicked on the " + web[+position],
                           Toast.LENGTH_SHORT).show();
         }
      });
   }
}