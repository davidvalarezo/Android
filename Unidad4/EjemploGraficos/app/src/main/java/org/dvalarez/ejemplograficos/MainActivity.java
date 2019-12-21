package org.dvalarez.ejemplograficos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private TextView texto;
    private EjemploView ejemploView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texto = findViewById(R.id.textView);
        //setContentView(new EjemploView(this));
        //setContentView(new EjemploView(this));
        ejemploView = findViewById(R.id.ejemploView) ;

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animacion);
        texto.startAnimation(animation);
        ejemploView.startAnimation(animation);
        ImageView v = findViewById(R.id.imageView);
        v.setVisibility();

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
        /*Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
        finish();*/

    }


}
