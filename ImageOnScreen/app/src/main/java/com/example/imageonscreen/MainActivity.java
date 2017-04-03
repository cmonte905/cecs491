package com.example.imageonscreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.io.InputStream;
import java.net.URL;

/*https://android--code.blogspot.com/2015/08/android-imageview-set-image-from-url.html*/
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the widgets reference from XML layout
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        final ImageView iv = (ImageView) findViewById(R.id.iv);

        //the URL used is subject to change
        final String imgURL  = "https://image.freepik.com/free-vector/android-boot-logo_634639.jpg";

        /*
            You need the following permission in your app AndroidManifest.xml file.
                <uses-permission android:name="android.permission.INTERNET" />
         */
        new DownLoadImageTask(iv).execute(imgURL);
    }

    /*
    AsyncTask enables proper and easy use of the UI thread. This class
    allows to perform background operations and publish results on the UI
    thread without having to manipulate threads and/or handlers.
     */

    /*
    final AsyncTask<Params, Progress, Result>
        execute(Params... params)
            Executes the task with the specified parameters.
    */
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
        doInBackground(Params... params)
            Override this method to perform a computation on a background thread.
        */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                //  decodeStream(InputStream is) - Decode an input stream into a bitmap.
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        //   onPostExecute(Result result) - Runs on the UI thread after doInBackground(Params...).
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}