package com.example.edward.orthography;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ayuda extends Fragment {


    public ayuda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View hview = inflater.inflate(R.layout.fragment_ayuda, container, false);

        TextView txt11 = (TextView) hview.findViewById(R.id.txt11);
        TextView txt22 = (TextView) hview.findViewById(R.id.txt22);
        TextView txt33 = (TextView) hview.findViewById(R.id.txt33);
        TextView txt44 = (TextView) hview.findViewById(R.id.txt44);
        TextView txt55 = (TextView) hview.findViewById(R.id.txt55);

        txt11.setText("");
        txt22.setText("");
        txt33.setText("");
        txt44.setText("");
        txt55.setText("");

        return hview;
    }

}
