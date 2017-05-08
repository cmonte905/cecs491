package lunux.shoppingappv1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class viewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Intent searchResults= getIntent();
        Bundle searchResult = searchResults.getExtras();
        final String itemName = searchResult.getString("itemName");
        final String searchString = searchResult.getString("searchString");
        String itemURL = searchResult.getString("itemURL");
        String descriptions;

        //Does a check on the item description and gives sensible output
        if(searchResult.getString("description").equalsIgnoreCase("Longdescription") ||
                searchResult.getString("description").equalsIgnoreCase("short desciption is not available")){
            descriptions = "No description avavailable for this item";
        }
        else {
            //Converts the item description into a readable string format removing most HTML tags.
            descriptions = searchResult.getString("description");
            descriptions=Html.fromHtml(descriptions).toString();
            descriptions=descriptions.replaceAll("\\<.*?>","");
            //descriptions=Html.fromHtml(descriptions, Html.FROM_HTML_MODE_LEGACY , null, null).toString(); API 24 and above
        }

        //Sets the name and image of the items to the screen
        TextView productName = (TextView)findViewById(R.id.specificItem);
        productName.setText(itemName + "\n" + descriptions);
        new DownloadImageTask((ImageView) findViewById(R.id.specificImage))
                .execute(itemURL);

        //Button for finding store
        Button toMap = (Button)findViewById(R.id.findStores2);
        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMap = new Intent(viewItemActivity.this, MapsActivity.class);
                startActivity(toMap);
            }
        });
        //Button for comparing more items
        Button toCompare = (Button)findViewById(R.id.compareButton);
        toCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compareStuff = new Intent(viewItemActivity.this, CompareSimilarItems.class);
                Bundle compareItem = new Bundle();
                compareItem.putString("searchString" , searchString);
                compareItem.putString("itemName" , itemName);
                compareStuff.putExtras(compareItem);
                startActivity(compareStuff);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
