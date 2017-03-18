package com.example.user.navigationdrawer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class FirstFragment extends Fragment {

    View myView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout, container, false);
        Button clicked = (Button)myView.findViewById(R.id.imageButton);
        clicked.setOnClickListener(new View.OnClickListener() { public void onClick(View v){
            EditText change = (EditText)myView.findViewById(R.id.editText);
            Bundle bundle = new Bundle();
            bundle.putString("search", change.getText().toString());
            SecondFragment frag2 = new SecondFragment();
            frag2.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, frag2).commit();
        }});
        return myView;
    }
}
