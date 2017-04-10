package lunux.shoppingappv1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class CompareSimilarItems extends AppCompatActivity {
    ProgressBar progressBar;
    TextView OriginalItem;
    String searchString;
    static final String WALMART_API_URL = "http://api.walmartlabs.com/v1/search?query=";
    static final String WALMART_API_REPSONSE = "&format=json&apiKey=bue3xb9zsg69nawfue55zxd7";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_similar_items);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        Intent received = getIntent();
        Bundle receives = received.getExtras();
        searchString = receives.getString("searchString");
        OriginalItem = (TextView)findViewById(R.id.OrigItem);
        OriginalItem.setText(receives.getString("itemName"));
        new RetrieveFeedTask().execute();
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
//            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here
            String urlString="";
            if(searchString.split(" ").length > 1){
                for(int i=0; i<searchString.split(" ").length;i++){
                    urlString+=searchString.split(" ")[i]+"+";
                }
            }
            String walmartURL = WALMART_API_URL + urlString + WALMART_API_REPSONSE;
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

                final JSONArray items = object.getJSONArray("items");
                //JSONArray item = object.getJSONArray("Item");

                //Going through the items in the json array
                ArrayList<String> itemNames = new ArrayList<String>();//Arraylist for item names
                ArrayList<String> itemPrices = new ArrayList<String>();//Arraylist for item prices
                final ArrayList<String> bigImages = new ArrayList<String>();//URL images for items
                final ArrayList<String> itemDesc = new ArrayList<String>();//URL images for item descriptions
                ArrayList<String> productURLs = new ArrayList<String>();
                for (int i = 0; i < items.length(); i++) {
                    itemNames.add(i,items.getJSONObject(i).getString("name").toString());
                    double newValue= Double.parseDouble(items.getJSONObject(i).getString("salePrice").toString());
                    Random random= new Random();
                    newValue = newValue + (random.nextInt(6)-3);
                    if(newValue < 0)
                        newValue = 0;
                    itemPrices.add(i,Double.toString(newValue));
                    productURLs.add(i,items.getJSONObject(i).getString("thumbnailImage").toString());
                }
                ListView list;
                final String[] listItem= new String[itemNames.size()];
                //String[] listPrice= new String[itemPrices.size()];
                final String[] imageLinks= new String[productURLs.size()];
                for(int i=0; i < itemNames.size();i++){
                    listItem[i]=itemNames.get(i) + " \n$" + itemPrices.get(i) ;
                }
                for(int i=0; i < productURLs.size();i++){
                    imageLinks[i]=productURLs.get(i);
                }

                CustomList adapter = new CustomList(CompareSimilarItems.this, listItem, imageLinks);
                list = (ListView) findViewById(R.id.compareItemList);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Intent viewThisItem = new Intent(SearchResultActivity.this, viewItemActivity.class);
//                        Bundle container = new Bundle();
//                        container.putString("itemName" , listItem[+position]);
//                        container.putString("itemURL" , bigImages.get(+position));
//                        container.putString("description", itemDesc.get(+position));
//                        viewThisItem.putExtras(container);
//                        startActivity(viewThisItem);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
