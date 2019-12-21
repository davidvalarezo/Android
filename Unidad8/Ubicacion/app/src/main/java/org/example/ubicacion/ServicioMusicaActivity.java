package org.example.ubicacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ServicioMusicaActivity extends AppCompatActivity implements View.OnClickListener{
    private Button detener, map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_musica);

        detener = findViewById(R.id.boton_detener);
        detener.setOnClickListener(this);

        map = findViewById(R.id.boton_map);
        map.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.boton_detener:
                servicioMusicaDetener(null);
                break;
            case R.id.boton_map:
                lanzarMapa(null);
                break;
        }
    }

    public void servicioMusicaDetener(View view) {
        stopService(new Intent(getBaseContext(),
                ServicioMusica.class));
    }

    public void lanzarMapa(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
