package org.example.comunicacionactividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Resultado extends Activity {
    private TextView tMessage;
    //private Button bAceptar, bRechazar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultado);
        tMessage = (TextView) findViewById(R.id.message);
        /*bAceptar = (Button) findViewById(R.id.b_aceptar);
        bRechazar = (Button) findViewById(R.id.b_rechazar);*/

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        tMessage.setText("Hola "+name+"\n¿Aceptas las condiciones?");

    }

    public void evento (View v){
        Intent i = new Intent();
        String r = new String();
        switch (v.getId()){
            case R.id.b_aceptar:
                r = "Aceptado";
                break;

            case R.id.b_rechazar:
                r = "Rechazado";
                break;
        }
        i.putExtra("resultado", r);
        setResult(RESULT_OK,i);
        finish();
    }
}


