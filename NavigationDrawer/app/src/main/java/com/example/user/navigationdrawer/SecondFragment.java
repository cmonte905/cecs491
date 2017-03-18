package com.example.user.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SecondFragment extends Fragment{

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout, container, false);
        Bundle bundle = this.getArguments();

        if(bundle != null){
            String a = bundle.get("search").toString();
            TextView changes = (TextView)myView.findViewById(R.id.SecondLayout);
            changes.setText(a);
        }
        return myView;
    }
}
