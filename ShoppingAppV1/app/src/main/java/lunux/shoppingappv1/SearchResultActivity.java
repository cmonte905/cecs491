package lunux.shoppingappv1;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SearchResultActivity extends AppCompatActivity {
    ProgressBar progressBar;
    String searchString;
    String storeShow;
    static final String WALMART_API_URL = "http://api.walmartlabs.com/v1/search?query=";
    static final String WALMART_API_REPSONSE = "&format=json&apiKey=bue3xb9zsg69nawfue55zxd7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Intent searchResults= getIntent();
        Bundle searchResult = searchResults.getExtras();
        searchString  = searchResult.getString("searchItem");
        storeShow = searchResult.getString("storeName");
        TextView searching = (TextView)findViewById(R.id.searchItem);
        TextView storeList = (TextView)findViewById(R.id.storeString);
        searching.setText("Searching for:"+searchString);
        storeList.setText("Searching store(s):" + storeShow);
        new RetrieveFeedTask().execute();
        Button toMap = (Button)findViewById(R.id.mapButton);
        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMap = new Intent(SearchResultActivity.this, MapsActivity.class);
                startActivity(toMap);
            }
        });
    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here
            String urlString;
            urlString=searchString.replace(" ", "+");
            String walmartURL = WALMART_API_URL + urlString + WALMART_API_REPSONSE;

            try {
                URL url = new URL(walmartURL);
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
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                String requestID = object.getString("query");
                System.out.println(requestID + "\n\n\n\n");

                int likelihood = object.getInt("totalResults");
                System.out.println(likelihood + "\n\n\n\n");

                final JSONArray items = object.getJSONArray("items");

                //Going through the items in the json array
                final ArrayList<String> nameItem = new ArrayList<String>();//Arraylist for item names
                final ArrayList<String> itemPrices = new ArrayList<String>();//Arraylist for item prices
                final ArrayList<String> bigImages = new ArrayList<String>();//URL images for larger picture of item
                final ArrayList<String> itemDesc = new ArrayList<String>();//URL images for item descriptions
                ArrayList<String> productURLs = new ArrayList<String>();//URL images for thumbnails
                for (int i = 0; i < items.length(); i++) {//Stores each item to the arraylist
                    nameItem.add(i,items.getJSONObject(i).getString("name").toString());
                    itemPrices.add(i,items.getJSONObject(i).getString("salePrice").toString());
                    productURLs.add(i,items.getJSONObject(i).getString("thumbnailImage").toString());
                    bigImages.add(i,items.getJSONObject(i).getString("mediumImage").toString());
                    itemDesc.add(i,items.getJSONObject(i).optString("shortDescription", "longDescription").toString());
                }
                ListView list;
                //Creates an array for listview adapter.
                final String[] imageLinkList= new String[productURLs.size()];
                final String[] itemPriceList= new String[productURLs.size()];
                final String[] itemNameList= new String[productURLs.size()];

                for(int i=0; i < productURLs.size();i++){//Convert the arraylist to an array
                    imageLinkList[i] = productURLs.get(i);
                    itemPriceList[i] = itemPrices.get(i);
                    itemNameList[i] = nameItem.get(i);
                }

                CustomList adapter = new CustomList(SearchResultActivity.this, itemNameList, itemPriceList, imageLinkList);//Creates listview of the objects
                list = (ListView) findViewById(R.id.list);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent viewThisItem = new Intent(SearchResultActivity.this, viewItemActivity.class);
                        Bundle container = new Bundle();
                        container.putString("searchString" , searchString);
                        container.putString("itemName" , nameItem.get(+position) +"\n$" +itemPrices.get(+position));
                        container.putString("itemURL" , bigImages.get(+position));
                        container.putString("description", itemDesc.get(+position));
                        viewThisItem.putExtras(container);
                        startActivity(viewThisItem);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
