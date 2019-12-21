package org.dvalarez.valarezod_u2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class Calculadora extends Fragment implements View.OnClickListener{
    public Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bC, b_equal, b_Pts, b_Plas;
    public TextView pant;
    public double operando1, operando2, res, memory =0.0;
    public int ope, wr=0;
    static final double pts = 166.386;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_calculadora, container, false);
        pant = rootView.findViewById(R.id.pantalla);
        Log.d("Calculadora","onCreateView mensaje");
        b1 = rootView.findViewById(R.id.b1);
        b2 = rootView.findViewById(R.id.b2);
        b3 = rootView.findViewById(R.id.b3);
        b4 = rootView.findViewById(R.id.b4);
        b5 = rootView.findViewById(R.id.b5);
        b6 = rootView.findViewById(R.id.b6);
        b7 = rootView.findViewById(R.id.b7);
        b8 = rootView.findViewById(R.id.b8);
        b9 = rootView.findViewById(R.id.b9);
        b0 = rootView.findViewById(R.id.b0);
        bC = rootView.findViewById(R.id.bC);
        b_equal = rootView.findViewById(R.id.bequal);
        b_Pts = rootView.findViewById(R.id.bPts);
        b_Plas = rootView.findViewById(R.id.bplas);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b0.setOnClickListener(this);
        bC.setOnClickListener(this);
        b_equal.setOnClickListener(this);
        b_Plas.setOnClickListener(this);
        b_Pts.setOnClickListener(this);

        return rootView;

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b0:
                bt0 (null);
                break;
            case R.id.b1:
                bt1 (null);
                break;
            case R.id.b2:
                bt2 (null);
                break;
            case R.id.b3:
                bt3 (null);
                break;
            case R.id.b4:
                bt4 (null);
                break;
            case R.id.b5:
                bt5 (null);
                break;
            case R.id.b6:
                bt6 (null);
                break;
            case R.id.b7:
                bt7 (null);
                break;
            case R.id.b8:
                bt8 (null);
                break;
            case R.id.b9:
                bt9 (null);
                break;
            case R.id.bC:
                clear (null);
                break;
            case R.id.bequal:
                igual(null);
                break;
            case R.id.bplas:
                suma (null);
                break;
            case R.id.bPts:
                conversorPts(null);
                break;
        }
    }
    /* Funciones para todos los botones*/
    public void bt0 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"0";
        pant.setText(cap);
        wr=0;
    }
    public void bt1 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"1";
        pant.setText(cap);
        wr=0;
    }
    public void bt2 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"2";
        pant.setText(cap);
        wr=0;
    }
    public void bt3 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"3";
        pant.setText(cap);
        wr=0;
    }
    public void bt4 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"4";
        pant.setText(cap);
        wr=0;
    }
    public void bt5 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"5";
        pant.setText(cap);
        wr=0;
    }
    public void bt6 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"6";
        pant.setText(cap);
        wr=0;
    }
    public void bt7 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"7";
        pant.setText(cap);
        wr=0;
    }
    public void bt8 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"8";
        pant.setText(cap);
        wr=0;
    }
    public void bt9 (View v){
        if(ope==0 && wr==1)pant.setText("");
        String cap = pant.getText().toString();
        cap = cap+"9";
        pant.setText(cap);
        wr=0;
    }


    public void suma(View view){
        try{
            String aux1 = pant.getText().toString();
            operando1 = memory + Float.parseFloat(aux1);
            memory = operando1;
        }catch (NumberFormatException nfe){

        }
        pant.setText("");
        ope=1;
        wr = 1;
    }

    public void igual(View view){
        try{
            String aux2 = pant.getText().toString();
            operando2 = Float.parseFloat(aux2);
        }catch (NumberFormatException nfe){

        }
        pant.setText("");

        if(ope==1){
            res = operando1 + operando2;
        }else{
            //Toast.makeText(this.getActivity(), R.string.error_cal, Toast.LENGTH_SHORT).show();
            res = operando2;
        }
        pant.setText(""+res);
        operando1 = res;
        memory = 0.0;
        res = 0.0;
        /* Se acabo la operación*/
        ope = 0;
        wr = 1;
    }

    public void conversorPts(View view){
        try{
            String aux1 = pant.getText().toString();
            operando1 = Float.parseFloat(aux1);
        }catch (NumberFormatException nfe){

        }
        if(ope==1){
            operando1 =0;
        }
        res = operando1* pts;
        pant.setText(""+res+" Pts.");
        /* Se acabo la operación*/
        operando1 = 0.0;
        memory = 0.0;
        res = 0.0;
        ope = 0;
        wr = 1;
    }
    public void clear (View v){
        pant.setText("");
        operando2= 0.0;
        operando1=0.0;
        res =0.0;
        ope = 0;
        wr = 0;
        memory = 0;
    }
}
