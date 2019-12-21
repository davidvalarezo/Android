package org.dvalarez.valarezod_u2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Boton extends Fragment implements View.OnClickListener {
    private Button b_imagen, b_add0;
    private EditText entrada;
    private TextView salida; /*log;*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.boton, container, false);

        b_imagen = (Button) rootView.findViewById(R.id.b_imagen);
        b_add0 = (Button) rootView.findViewById(R.id.button0);
        entrada = (EditText) rootView.findViewById(R.id.entrada);
        salida = (TextView) rootView.findViewById(R.id.salida);

        b_imagen.setOnClickListener(this);
        b_add0.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_imagen:
                sePulsa(null);
                break;
            case R.id.button0:
                sePulsa0(v);
                break;
        }
    }

    public void sePulsa(View view){
        //Toast.makeText(this.getActivity(), "Pulsado", Toast.LENGTH_SHORT).show();

        if(entrada.getText().toString().equals("")){
            Toast.makeText(this.getActivity(), R.string.In, Toast.LENGTH_SHORT).show();
            //entrada.setText("0");
        }
        else{
        salida.setText(String.valueOf(Float.parseFloat(
                entrada.getText().toString()) * 2.0));
        }
    }

    public void sePulsa0(View view){
        //entrada.setText(entrada.getText()+"0");
        entrada.setText(entrada.getText()+(String)view.getTag());
    }
}
