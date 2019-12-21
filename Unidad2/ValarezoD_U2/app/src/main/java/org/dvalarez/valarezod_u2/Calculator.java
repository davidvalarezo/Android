package org.dvalarez.valarezod_u2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Iterator;

public class Calculator extends Fragment implements View.OnClickListener{

    public ArrayList<Button> Botones = new ArrayList<Button>();
    public ArrayList<Float> numeros = new ArrayList<Float>();
    public ArrayList<String> operacion = new ArrayList<String>();
    public TextView pant_out, pant_in;

    public String cap = "", pIn="", pOut, aux="";
    public double operando1,operando2, res =0;
    public double numInMemory = 0; /* Ayuda hacer acumulativa la operacion */
    public int tipoOperacion = 0; /* 1 suma; 2 resta; 3 multiplicacion; 4 division*/
    public int numSum = 0,numMul = 0,numDiv = 0,numRes = 0;;
    public boolean isOperacion = false, isWritting = false;

    static final double PTS = 166.386;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_calculator, container, false);

        pant_in = v.findViewById(R.id.t_in);
        pant_out= v.findViewById(R.id.t_out);
        Log.d("Calculator","Entramos en onCreateView");

        Botones.add(0,(Button)v.findViewById(R.id.b_0));
        Botones.add(1,(Button)v.findViewById(R.id.b_1));
        Botones.add(2,(Button)v.findViewById(R.id.b_2));
        Botones.add(3,(Button)v.findViewById(R.id.b_3));
        Botones.add(4,(Button)v.findViewById(R.id.b_4));
        Botones.add(5,(Button)v.findViewById(R.id.b_5));
        Botones.add(6,(Button)v.findViewById(R.id.b_6));
        Botones.add(7,(Button)v.findViewById(R.id.b_7));
        Botones.add(8,(Button)v.findViewById(R.id.b_8));
        Botones.add(9,(Button)v.findViewById(R.id.b_9));
        Botones.add(10,(Button)v.findViewById(R.id.bpoint));
        Botones.add(11,(Button)v.findViewById(R.id.b_M));
        Botones.add(12,(Button)v.findViewById(R.id.b_plas));
        Botones.add(13,(Button)v.findViewById(R.id.b_less));
        Botones.add(14,(Button)v.findViewById(R.id.b_mul));
        Botones.add(15,(Button)v.findViewById(R.id.b_div));
        Botones.add(16,(Button)v.findViewById(R.id.b_equal));
        Botones.add(17,(Button)v.findViewById(R.id.b_pts));
        Botones.add(18,(Button)v.findViewById(R.id.b_D));
        Botones.add(19,(Button)v.findViewById(R.id.b_C));






        for(int i=0; i<=19; i++){
            Botones.get(i).setOnClickListener(this);
            Botones.get(i).setTag(i);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        /* Los tags del 0-9 son teclas númericas*/
        int b_numbers = (int)v.getTag();

        if(b_numbers>=0 && b_numbers<10){ /*Ingresando números*/
            if(!isOperacion && (!isWritting) ){
                clear();
            }

            /**Esto es para no concatenar el 0 al inicio*/
            if(!(cap.equals("")&& b_numbers ==0)){

                cap = cap+""+b_numbers;
                mostrarPantalla(b_numbers);
            }
            isOperacion = false;
            isWritting = true;

        }else{
            aux = aux+cap;
            // "."
            if(b_numbers == 10){
                punto();
            }
            // boton M = 11(modificar)
            if(b_numbers == 11){
                modificar(v);
            }
            // suma = 12
            if(b_numbers == 12){
                sumar();
            }
            // resta = 13
            if(b_numbers == 13){
                restar();
            }
            // multiplicacion = 14
            if(b_numbers == 14){
                multiplicar();
            }
            // dividir = 15
            if(b_numbers == 15){
                dividir();
            }
            // equals  = 16
            if(b_numbers == 16){

                    equals();

            }
            // conversor a Pts
            if (b_numbers == 17){
                cambioMoneda (17);
            }// conversor a €
            if (b_numbers == 18){
                cambioMoneda (18);
            }
            // Clear 19
            if(b_numbers == 19){
                clear();
            }

        }


    }

    public double pecetas (double op1){
        return res = op1 * PTS;
    }
    public double euros (double op1){ return res = op1 / PTS; }

    public void punto (){
        if(cap.equals("")){
            cap = 0+".";
        }else {
            cap = cap+".";
        }
    }

    public void cambioMoneda (int num){
        try{
            operando1 = 0.0;
            if(!cap.equals("")){
                operando1 = Double.parseDouble(cap);
            }
            if(num == 18){
                res = euros (operando1);
                mostrarPantalla(18);
            }else{
                res = pecetas (operando1);
                mostrarPantalla(17);
            }
        }catch (NumberFormatException nfe){ }
        cap = "";//pant.setText("");


    }

    public void sumar(){
        if(!aux.equals("")&&!aux.equals("-") && (!isOperacion)){
            aux = aux+"+";
            try{
                if(!cap.equals("")){
                    //operando1 = Double.parseDouble(cap);
                    numeros.add(Float.parseFloat(cap));
                    operacion.add("+");
                }

                //res = suma(res,operando1);
                //pIn = pIn+operando1+ "+"; //Encadenando todo lo que ingresa el usuario más el signo "+"

            }catch (NumberFormatException nfe){ }
            cap = "";
            numSum++;
            //tipoOperacion = 1; //Setting operation type for suma
            mostrarPantalla(12);
        }else{
            mostrarPantalla(20);
        }
        isOperacion = true;
        isWritting = false;

    }

    public void restar(){
        aux = aux+"-";
        try{
            if(!cap.equals("")){
                //operando1 = Double.parseDouble(cap);
                numeros.add(Float.parseFloat(cap));
                operacion.add("-");
            }
            //res = resta(res,operando1);
            //pIn = pIn+"-"+operando1; //Encadenando todo lo que ingresa el usuario más el signo "+"

        }catch (NumberFormatException nfe){ }
        cap = "";
        numRes++;
        //tipoOperacion = 2; //Setting operation type for suma
        mostrarPantalla(13);
        isOperacion = true;
    }
    public void multiplicar(){
        if(!aux.equals("")&&!aux.equals("-") && (!isOperacion)){
            aux = aux+"*";
            try{
                if(!cap.equals("")){
                    //operando1 = Double.parseDouble(cap);
                    numeros.add(Float.parseFloat(cap));
                    operacion.add("*");
                }

            }catch (NumberFormatException nfe){ }
            cap = "";
            numMul++;
            mostrarPantalla(14);
        }else{
            mostrarPantalla(20);
        }
        isOperacion = true;
        isWritting = false;
    }
    public void dividir(){
        if(!aux.equals("")&&!aux.equals("-")  && (!isOperacion)){
            aux = aux+"/";
            try{
                if(!cap.equals("")){
                    //operando1 = Double.parseDouble(cap);
                    numeros.add(Float.parseFloat(cap));
                    operacion.add("/");
                }

            }catch (NumberFormatException nfe){ }
            cap = "";
            numDiv++;
            mostrarPantalla(15);
        }else{
            mostrarPantalla(20);
        }
        isOperacion = true;
        isWritting = false;
    }

    public void equals (){
        try{
            if(!cap.equals("")){
            //operando2 = Double.parseDouble(cap);
            numeros.add(Float.parseFloat(cap));
                leerNumeros();
             }
        }catch (NumberFormatException nfe){ }
        cap = "";



        pOut = res+"";
        aux = res+"";
        mostrarPantalla(16);
        res = 0; pIn = "";
        isOperacion = false;
        numRes =0; numSum=0; numMul=0; numDiv=0;
        isWritting = false;
    }

    public void clear (){
        //operando1 = 0.0;
        //operando2 = 0.0;
        res=0;
        //tipoOperacion = 0;
        pIn = "";
        pOut = "0.0";
        aux = "";
        numRes =0; numSum=0; numMul=0; numDiv=0;
        operacion.clear();
        numeros.clear();
        isOperacion = false;
        mostrarPantalla(19);
        isWritting = true;
    }

    public void modificar (View v){
        if(!cap.equals("")){
           cap = (String)cap.subSequence(0,cap.length()-1);
        }
        mostrarPantalla(11);
    }

    public void leerNumeros (){

        try{

            if(!numeros.isEmpty() && !operacion.isEmpty()){

                if(numMul!=0){
                    for(int i= 0; i< operacion.size(); i++){
                        if(operacion.get(i).equals("*")){
                            Float num = numeros.get(i) * numeros.get(i+1);
                            numeros.set(i,num);

                            if(numeros.size()==2 ){
                                numeros.remove(i+1);
                                operacion.clear();
                                break;
                            }else{
                                numeros.remove(i+1);
                                operacion.remove(i);
                            }

                        }
                    }if(numeros.size()==2&& operacion.get(0).equals("*")){
                        numeros.set(0,numeros.get(0) * numeros.get(1)) ;
                        numeros.remove(1);
                        operacion.clear();
                    }

                }

                if(numDiv!=0){
                    for(int i= 0; i< operacion.size(); i++){
                        if(operacion.get(i).equals("/")){
                            Float num = numeros.get(i) / numeros.get(i+1);
                            numeros.set(i,num);

                            if(numeros.size()==2 ){
                                numeros.remove(i+1);
                                operacion.clear();
                                break;
                            }
                            else{
                                numeros.remove(i+1);
                                operacion.remove(i);
                            }

                        }
                    }
                    if(numeros.size()==2&& operacion.get(0).equals("/")){
                        numeros.set(0,numeros.get(0) / numeros.get(1)) ;
                        numeros.remove(1);
                        operacion.clear();
                    }

                }
                if(numSum!=0){
                    for(int i= 0; i< operacion.size(); i++){
                        if(operacion.get(i).equals("+")){
                            Float num = numeros.get(i) + numeros.get(i+1);
                            numeros.set(i,num);

                            if(numeros.size()==2 ){
                                numeros.remove(i+1);
                                operacion.clear();
                                break;
                            }
                            else{
                                numeros.remove(i+1);
                                operacion.remove(i);
                            }

                        }
                    }
                    if(numeros.size()==2&& operacion.get(0).equals("+")){
                        numeros.set(0,numeros.get(0) + numeros.get(1)) ;
                        numeros.remove(1);
                        operacion.clear();
                    }

                }
                if(numRes!=0){
                    for(int i= 0; i< operacion.size(); i++){
                        if(operacion.get(i).equals("-")){
                            Float num = numeros.get(i) - numeros.get(i+1);
                            numeros.set(i,num);

                            if(numeros.size()==2 ){
                                numeros.remove(i+1);
                                operacion.clear();
                                break;
                            }
                            else{
                                numeros.remove(i+1);
                                operacion.remove(i);
                            }

                        }
                    }
                    if(numeros.size()==2&& operacion.get(0).equals("-")){
                        numeros.set(0,numeros.get(0) - numeros.get(1)) ;
                        numeros.remove(1);
                        operacion.clear();
                    }

                }
                res = numeros.get(0);
            }else{
                if(!numeros.isEmpty()){
                    res = numeros.get(0);
                }
            }

        }catch (NumberFormatException nfe){

        }


    }


    public void mostrarPantalla(int bn){

        switch (bn){

        case 12: // suma
            //numOperandos++;
            pant_out.setText(pOut);
            pant_in.setText(aux);
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
            break;
        case 13: // resta
            //numOperandos++;
            pant_out.setText(pOut);
            pant_in.setText(aux);
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
            break;
        case 14: // mul
            //numOperandos++;
            pant_out.setText(pOut);
            pant_in.setText(aux);
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
            break;
        case 15: // div
            //numOperandos++;
            pant_out.setText(pOut);
            pant_in.setText(aux);
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
            break;
        case 16:// =
            pant_out.setText(pOut);
            pant_in.setText(pIn);
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
            break;

        case 17:// € a Pts
            //numOperandos++;
            pant_in.setText(operando1+" €");
            pant_out.setText(res+" Pts");
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/

            break;
        case 18:// Pts a €
            //numOperandos++;
            pant_in.setText(operando1+" Pts");
            pant_out.setText(res+" €");
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/

            break;

        case 19:// C
            pant_in.setText("");
            pant_out.setText("0.0");
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
            break;
        case 20:// Error
            //pant_in.setText("");
            pant_out.setText("Error");
            cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
        break;
        default: /* Ingreso de números*/
            pant_out.setText(cap);
            break;
        }
    }
}
