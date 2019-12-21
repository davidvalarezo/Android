package org.example.asteroides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bPlay, bSetting, bAbout, bScore;
    private TextView titulo;
    private ImageView icono;
    public static  Animation animation;
    public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titulo = findViewById(R.id.titleAsteroid);
        icono = findViewById(R.id.icono);

        bPlay = (Button) findViewById(R.id.b_play);
        bSetting = (Button) findViewById(R.id.b_setting);
        bAbout = (Button) findViewById(R.id.b_about);
        bScore = (Button) findViewById(R.id.b_score);

        bPlay.setOnClickListener(this);
        bSetting.setOnClickListener(this);
        bAbout.setOnClickListener(this);
        bScore.setOnClickListener(this);

        animation = AnimationUtils.loadAnimation(this, R.anim.giro_con_zoom);
        titulo.startAnimation(animation);
        Animation animButton1 = AnimationUtils.loadAnimation(this, R.anim.aparecer);
        bPlay.startAnimation(animButton1);
        Animation animIcon = AnimationUtils.loadAnimation(this, R.anim.animacion3);
        icono.startAnimation(animIcon);
        Animation animButton2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha);
        bSetting.startAnimation(animButton2);
        Animation animButton3 = AnimationUtils.loadAnimation(this, R.anim.animacion1);
        bAbout.startAnimation(animButton3);
        Animation animButton4 = AnimationUtils.loadAnimation(this, R.anim.animacion2);
        bScore.startAnimation(animButton4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_score:
                lanzarPuntuaciones(null);
                break;
            case R.id.b_about:
                bAbout.startAnimation(animation);
                lanzarAcercaDe(null);
                break;
            case R.id.b_play:
                lanzarJuego(null);
                break;
            case R.id.b_setting:
                lanzarPreferencias(null);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.menu_buscar){
            //lanzarBuscar(null);
            return true;
        }
        if (id == R.id.acercaDe){
            lanzarAcercaDe(null);
            return true;
        }
        /*if (id == R.id.salida){
            finish();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void lanzarPreferencias(View view) {
        Intent i = new Intent(this,PreferenciasActivity.class );
        startActivity(i);
    }

    public void lanzarAcercaDe (View view){
        Intent i = new Intent(this,AcercaDeActivity.class );
        startActivity(i);
    }

    public void lanzarJuego (View view){
        Intent i = new Intent(this,Juego.class );
        startActivity(i);
    }

    public void lanzarPuntuaciones(View view) {
        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
    }

    public void mostrarPreferencias(View view){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = "musica"+": " +
                pref.getBoolean(/*key:*/"musica",true)
                +", "+"graficos"+": " +
                pref.getString(/*key:*/"graficos","?")
                ///+", "+getString(R.string.title_multiplayer)+": " +
                ///pref.getBoolean(/*key:*/getString(R.string.title_multiplayer),true)
                ///+", "+getString(R.string.title_conection_type)+": " +
                //pref.getString(/*key:*/getString(R.string.title_conection_type),"?")
                ;
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
