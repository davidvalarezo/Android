package com.payperview.usarpermiso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void verVideo (View view){
        Intent i = new Intent();
        i.setClassName("com.payperview.servicios",
                "com.payperview.servicios.VerVideo");
        startActivity(i);
    }
}
