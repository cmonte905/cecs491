package lunux.shoppingappv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class viewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Intent searchResults= getIntent();
        Bundle searchResult = searchResults.getExtras();
        String itemName = searchResult.getString("itemName");
        String itemURL = searchResult.getString("itemURL");
        TextView productName = (TextView)findViewById(R.id.specificItem);
        productName.setText(itemName);
    }
}
