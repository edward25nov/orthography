package com.example.edward.orthography;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AmigosFragment extends Fragment {


    public AmigosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_amigos,container,false);
        Button nuevaPartida = (Button) view.findViewById(R.id.btnNew);

        nuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //http://stackoverflow.com/questions/36483010/onclick-button-event-in-fragment
            //esta fue la que funciono :)
            // http://stackoverflow.com/questions/27728439/implementing-onclicklistener-for-a-fragment-on-android
             Toast.makeText(getActivity(), "Hello World", Toast.LENGTH_LONG).show();
            // startActivity(new Intent(getActivity(),Splash.class));
            }
        });


        return  view;

        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_amigos, container, false);
    }



}


