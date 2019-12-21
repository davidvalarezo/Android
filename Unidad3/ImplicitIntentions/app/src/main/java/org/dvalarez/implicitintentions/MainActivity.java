package org.dvalarez.implicitintentions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Button bWeb, bFoto, bMap, bMail, bTel, bWeb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bWeb = (Button) findViewById(R.id.bWeb);
        bWeb2 = (Button) findViewById(R.id.bWeb2);
        bFoto = (Button) findViewById(R.id.bFoto);
        bMap = (Button) findViewById(R.id.bMap);
        bMail = (Button) findViewById(R.id.bMail);
        bTel = (Button) findViewById(R.id.bTelf);

        bWeb.setOnClickListener(this);
        bWeb2.setOnClickListener(this);
        bFoto.setOnClickListener(this);
        bMail.setOnClickListener(this);
        bMap.setOnClickListener(this);
        bTel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bWeb:
                verPgWeb(null);
                break;
            case R.id.bFoto:
                tomarFoto(null);
                break;
            case R.id.bMail:
                mandarCorreo(null);
                break;
            case R.id.bMap:
                googleMaps(null);
                break;
            case R.id.bTelf:
                llamarTelefono(null);
                break;
            case R.id.bWeb2:
                webSearch(null);
                break;
        }

    }

    public void verPgWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.androidcurso.com/"));
        startActivity(intent);
    }
    public void llamarTelefono(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:962849347"));
        startActivity(intent);
    }
    public void googleMaps(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:41.656313,-0.877351"));
        startActivity(intent);
    }
    public void tomarFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }
    public void mandarCorreo(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "asunto");
        intent.putExtra(Intent.EXTRA_TEXT, "texto del correo");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"david.v.13@hotmail.es"});
        startActivity(intent);
    }

    public void webSearch(View view) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH,
                Uri.parse("http://www.androidcurso.com/"));
        //intent.setType("");
        startActivity(intent);

    }


}
