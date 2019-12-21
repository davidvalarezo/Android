package org.example.comunicacionactividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button bVerificar;
    private TextView tName, tResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tName = (TextView) findViewById(R.id.name);
        tResult = (TextView) findViewById(R.id.resultado);
        bVerificar = (Button) findViewById(R.id.b_verificar);
        bVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificar(null);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0001 && resultCode==RESULT_OK){
            String result = data.getExtras().getString("resultado");
            tResult.setText("Resultado: "+result);

        }

    }

    public void verificar(View v) {
        Intent i = new Intent(this, Resultado.class);
        String nombre = tName.getText().toString();
        i.putExtra("name", nombre);
        startActivityForResult(i,0001);
        /*Bundle extras = getIntent().getExtras();
        String name = extras.getString(null);*/
    }
}
