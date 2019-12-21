package org.example.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Tab1 extends Fragment /*implements View.OnClickListener*/ {
    //private Button bPlay, bSetting, bAbout, bExit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1, container, false);
/*
        bPlay = rootView.findViewById(R.id.b_play);
        bSetting = rootView.findViewById(R.id.b_setting);
        bAbout = rootView.findViewById(R.id.b_about);
        bExit = rootView.findViewById(R.id.b_exit);

        bPlay.setOnClickListener(this);
        bSetting.setOnClickListener(this);
        bAbout.setOnClickListener(this);
        bExit.setOnClickListener(this);*/

        return rootView;
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_exit:
                getActivity().finish();
                break;
            case R.id.b_about:
                lanzarAcercaDe(null);
                break;
        }
    }

    public void lanzarAcercaDe (View view){
        Intent i = new Intent(this.getActivity(),AcercaDeActivity.class );
        startActivity(i);
    }*/
}