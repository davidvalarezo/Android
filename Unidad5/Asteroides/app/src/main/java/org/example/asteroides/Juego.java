package org.example.asteroides;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class Juego extends Activity {
    private VistaJuego vistaJuego;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);
        vistaJuego = findViewById(R.id.VistaJuego);
    }
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
    }
    @Override protected void onDestroy() {
        vistaJuego.getThread().detener();
        super.onDestroy();
    }
}
