package org.example.asteroides;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

public class Juego extends Activity {
    private VistaJuego vistaJuego;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        vistaJuego = findViewById(R.id.VistaJuego);

        //vistaJuego.getPlay().getView().setOnClickListener(this);
    }
    /*@Override
    public void onClick(View v) {
        if (vistaJuego.getThread().isPausa()){
            vistaJuego.getThread().reanudar();
        }else{
                vistaJuego.getThread().pausar();
        }

    }*/
    @Override protected void onPause() {
        vistaJuego.getThread().pausar();
        vistaJuego.desactivarSensores();
        vistaJuego.getSoundPool().release();
        vistaJuego.setSoundPool(null)  ;
        super.onPause();
    }
    @Override protected void onResume() {
        super.onResume();
        vistaJuego.getThread().reanudar();
        vistaJuego.activarSensores();
        vistaJuego.createSoundPool();

        vistaJuego.setInicia(true);
    }
    @Override protected void onDestroy() {
        vistaJuego.getThread().detener();
        super.onDestroy();
    }
}
