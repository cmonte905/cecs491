package com.example.cemo.testproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText itemSearch;
    TextView responseView;
    ProgressBar progressBar;
    ListView itemList;

    //Search items
    String searchString;

    //Macy's related things
    //static final String WALMART_API_KEY = "bue3xb9zsg69nawfue55zxd7";
    //static final String AMAZON_API_KEY = "";
    static final String WALMART_API_URL = "http://api.walmartlabs.com/v1/search?query=";
    //static final String AMAZON_API_URL = "http://webservices.amazon.com/onca/xml?" + "Service=AWSECommerceService&Operation=ItemSearch&";
    static final String WALMART_API_REPSONSE = "&format=json&apiKey=bue3xb9zsg69nawfue55zxd7";
    //the second half of the URL is from a sample in the documentation
    /*static final String AMAZON_API_RESPONSE = "AWSAccessKeyId=[Access Key ID]&AssociateTag=[Associate ID]&SearchIndex=Apparel&" +
                                                "MaximumPrice=5000Keywords=Shirt&Timestamp=[YYYY-MM-DDThh:mm:ssZ]&Signature=[Request Signature]"; */
    //maximum price is for purpose of finding an item of equal or lower price

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = (TextView) findViewById(R.id.leftResponseView);
        itemSearch = (EditText) findViewById(R.id.itemSearch);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button queryButton = (Button) findViewById(R.id.queryButton);

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = itemSearch.getText().toString();
                System.out.println("Search string by user: " + searchString);
                new RetrieveFeedTask().execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here
            String walmartURL = WALMART_API_URL + searchString + WALMART_API_REPSONSE;
            //String amazomURL = AMAZON_API_URL + searchString + AMAZOn_API_RESPONSE;

            try {
                URL url = new URL(walmartURL);
                //URL url = new URL(amazonURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);

            try {
                   //String xml = "<Query>" + response + "</Query>";
                   //JSONObject object = XML.toJSONObject(response);
                   //object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                String requestID = object.getString("query");
                   //int totalProducts = object.getInt("TotalResults");
                   //System.out.println("Total results: " + totalProducts + "\n\n\n\n");
                System.out.println(requestID + "\n\n\n\n");

                int likelihood = object.getInt("totalResults");
                System.out.println(likelihood + "\n\n\n\n");

                JSONArray items = object.getJSONArray("items");
                   //JSONArray item = object.getJSONArray("Item");

                //Going through the items in the json array
                for (int i = 0; i < items.length(); i++) {
                    String itemName = items.getJSONObject(i).getString("name").toString() + "\n";
                    String itemPrice = items.getJSONObject(i).getString("salePrice").toString() + "\n";
                    String productUrl = items.getJSONObject(i).getString("productUrl").toString() + "\n\n";
                    System.out.println(itemName); responseView.append(itemName);
                    System.out.println(itemPrice); responseView.append(itemPrice);
                    System.out.println(productUrl); responseView.append(productUrl);
                }
                    /*
                    if (items.length() > 0) {
                       for (int i = 0; i < item.length(); i++) {
                          String title = item(i).getJSONObject("ItemAttributes").getString("Title") + "\n";
                          System.out.println(title); responseView.append(title);
                       }
                    }
                    else {
                       System.out.println("No items matched the price."); responseView.append("No items matched the price.";
                    }
                    */
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void onListItemClick(ListView l, View v, int position, long id) {
            // Do something when a list item is clicked
        }
    }
}