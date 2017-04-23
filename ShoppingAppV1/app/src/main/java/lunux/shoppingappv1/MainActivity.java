package lunux.shoppingappv1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String searchString;
    EditText searchItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchItem = (EditText)findViewById(R.id.SearchText);
        ImageButton searchButton = (ImageButton)findViewById(R.id.SearchButton);
        searchButton.setOnClickListener(this);
    }
    public void onClick(View v){
        searchString = searchItem.getText().toString();
        if(searchString.equalsIgnoreCase("")){
            Context context = getApplicationContext();
            CharSequence text = "Nothing to Search";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
        else if(searchString.length() > 40){
            Context context = getApplicationContext();
            CharSequence text = "Search item is too long!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
            searchItem.setText("");
        }
        else{
            Intent searchSet = new Intent(MainActivity.this, SearchResultActivity.class);
            Bundle container = new Bundle();
            //RadioGroup stores = (RadioGroup)findViewById(R.id.storeList);
            int Store =  ((RadioGroup) findViewById(R.id.storeList)).getCheckedRadioButtonId();
            RadioGroup b=(RadioGroup) findViewById((R.id.storeList));
            RadioButton a=(RadioButton)findViewById(b.getCheckedRadioButtonId());
            String storeName = a.getText().toString();
            container.putString("storeName", a.getText().toString());
            container.putString("searchItem", searchItem.getText().toString());

            searchSet.putExtras(container);

//            Toast.makeText(MainActivity.this,
//                    a.getText(), Toast.LENGTH_SHORT).show();

            startActivity(searchSet);
        }
    }
}
