package com.example.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class CustomList extends ArrayAdapter<String> {
   private final Activity context;
   private final String[] web;
   //private final Integer[] imageId;
   private final String[] imageURL;

   public CustomList(Activity context, String[] web, String[] imageURL) {
   //public CustomList(Activity context, String[] web, Integer[] imageId) {
      super(context, R.layout.list_single, web);
      this.context = context;
      this.web = web;
      //this.imageId = imageId;
      this.imageURL = imageURL;
   }

   @Override
   public View getView(int position, View view, ViewGroup parent) {
      LayoutInflater inflater = context.getLayoutInflater();
      View rowView = inflater.inflate(R.layout.list_single, null, true);
      TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

      ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
      txtTitle.setText(web[position]);
      new DownloadImageTask(imageView).execute(imageURL[position]);
      //imageView.setImageResource(imageId[position]);
      return rowView;
   }

   private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
      ImageView imageView;

      public DownloadImageTask(ImageView imageView) {
         this.imageView = imageView;
      }

      //doInBackground(Params... params)
      //  Override this method to perform a computation on a background thread.
      protected Bitmap doInBackground(String... urls) {
         String urlOfImage = urls[0];
         Bitmap logo = null;
         try {
            InputStream is = new URL(urlOfImage).openStream();
            //  decodeStream(InputStream is) - Decode an input stream into a bitmap.
            logo = BitmapFactory.decodeStream(is);
         } catch (Exception e) { // Catch the download exception
            e.printStackTrace();
         }
         return logo;
      }

      //  onPostExecute(Result result) - Runs on the UI thread after doInBackground(Params...).
      protected void onPostExecute(Bitmap result) {
         imageView.setImageBitmap(result);
      }
   }
}
