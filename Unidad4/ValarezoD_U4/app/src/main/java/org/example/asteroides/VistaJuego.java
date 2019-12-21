package org.example.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.List;

public class VistaJuego extends View {
    // //// ASTEROIDES //////
    private List<Grafico> asteroides; // Lista con los Asteroides
    private int numAsteroides = 5; // Número inicial de asteroides
    private int numFragmentos = 3; // Fragmentos en que se divide

    // //// NAVE //////
    private Grafico nave; // Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private double aceleracionNave; // aumento de velocidad
    private static final int MAX_VELOCIDAD_NAVE = 20;
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    public VistaJuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide, drawableMisil;
        //drawableNave = AppCompatResources.getDrawable(context, R.drawable.astronave);
        //drawableAsteroide = AppCompatResources.getDrawable(context, R.drawable.asteroide1);
        SharedPreferences pref = PreferenceManager. getDefaultSharedPreferences(getContext());

        if(pref.getString("graficos", "0").equals("0")){
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            /*Hacer aleatorio eltipo de asteroide*/
            int tipo = (int) (Math.random()*3 +1);
            drawableAsteroide = shapeGrafico("asteroides"+tipo);
            drawableNave = shapeGrafico("nave1");
            setBackgroundColor(Color.BLACK);
        }
        else if(pref.getString("graficos", "0").equals("3")){
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            drawableAsteroide = AppCompatResources.getDrawable(context, R.drawable.ic_asteroid1);
            drawableNave = AppCompatResources.getDrawable(context, R.drawable.ic_nave);
            this.setBackgroundResource(R.drawable.fondo2);
        }
        else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            drawableAsteroide = AppCompatResources.getDrawable(context, R.drawable.asteroide1);
            drawableNave = AppCompatResources.getDrawable(context, R.drawable.nave);
        }

        /** Inicializacion de la lista de asteroides**/
        asteroides = new ArrayList<Grafico>();
        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }
        /** Inicializacion de nave**/
        nave = new Grafico(this, drawableNave);
    }


    @Override protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter){
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto.
        // centrando la nave en el canvas
        nave.setCenX(ancho/2);
        nave.setCenY(alto/2);

        for (Grafico asteroide: asteroides) {
            do {
                asteroide.setCenX((int) (Math.random()*ancho));
                asteroide.setCenY((int) (Math.random()*alto));
            } while(asteroide.distancia(nave) < (ancho+alto)/5 || verificaColisionGraficos(asteroide));
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafico asteroide: asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
        /* Dibujando la nave en el canvas */
        nave.dibujaGrafico(canvas);
    }

    public ShapeDrawable shapeGrafico(String shape){
        Path path = new Path();
        ShapeDrawable draw;
        if(shape.equals("nave")){
            path.moveTo((float) 0.0, (float) 0.0);
            path.lineTo((float) 1.0, (float) 0.5);
            path.lineTo((float) 0.0, (float) 1.0);
            path.lineTo((float) 0.0, (float) 0.0);
            draw = new ShapeDrawable( new PathShape(path, 1, 1));
            draw.getPaint().setColor(Color.WHITE);
            draw.getPaint().setStyle(Paint.Style.STROKE);
            draw.setIntrinsicWidth(50);
            draw.setIntrinsicHeight(40);

        }else if(shape.equals("nave1")){
            path.moveTo((float) 0.0, (float) 0.0);
            path.lineTo((float) 1.0, (float) 0.5);
            path.lineTo((float) 0.0, (float) 1.0);
            path.lineTo((float) 0.2, (float) 0.5);
            path.lineTo((float) 0.0, (float) 0.0);
            draw = new ShapeDrawable( new PathShape(path, 1, 1));
            draw.getPaint().setColor(Color.WHITE);
            draw.getPaint().setStyle(Paint.Style.STROKE);
            draw.setIntrinsicWidth(50);
            draw.setIntrinsicHeight(40);

        }else if(shape.equals("asteroides1")){
            path.moveTo((float) 0.1, (float) 0.0);
            path.lineTo((float) 0.3, (float) 0.0);
            path.lineTo((float) 0.6, (float) 0.1);
            path.lineTo((float) 0.7, (float) 0.0);
            path.lineTo((float) 0.8, (float) 0.2);
            path.lineTo((float) 1.1, (float) 0.4);
            path.lineTo((float) 1.1, (float) 0.7);
            path.lineTo((float) 0.9, (float) 0.9);
            path.lineTo((float) 0.9, (float) 1.0);
            path.lineTo((float) 0.7, (float) 1.1);
            path.lineTo((float) 0.6, (float) 1.0);
            path.lineTo((float) 0.5, (float) 1.1);
            path.lineTo((float) 0.2, (float) 1.0);
            path.lineTo((float) 0.0, (float) 0.8);
            path.lineTo((float) 0.0, (float) 0.7);
            path.lineTo((float) 0.1, (float) 0.6);
            path.lineTo((float) 0.0, (float) 0.3);
            path.lineTo((float) 0.1, (float) 0.0);
            draw = new ShapeDrawable( new PathShape(path, 1, 1));
            draw.getPaint().setColor(Color.WHITE);
            draw.getPaint().setStyle(Paint.Style.STROKE);
            draw.setIntrinsicWidth(120);
            draw.setIntrinsicHeight(120);

        }else if(shape.equals("asteroides2")){
            path.moveTo((float) 0.3, (float) 0.0);
            path.lineTo((float) 0.6, (float) 0.0);
            path.lineTo((float) 0.6, (float) 0.3);
            path.lineTo((float) 0.8, (float) 0.2);
            path.lineTo((float) 1.0, (float) 0.4);
            path.lineTo((float) 0.8, (float) 0.6);
            path.lineTo((float) 0.9, (float) 0.9);
            path.lineTo((float) 0.8, (float) 1.0);
            path.lineTo((float) 0.4, (float) 1.0);
            path.lineTo((float) 0.0, (float) 0.6);
            path.lineTo((float) 0.0, (float) 0.2);
            path.lineTo((float) 0.3, (float) 0.0);
            draw = new ShapeDrawable( new PathShape(path, 1, 1));
            draw.getPaint().setColor(Color.WHITE);
            draw.getPaint().setStyle(Paint.Style.STROKE);
            draw.setIntrinsicWidth(120);
            draw.setIntrinsicHeight(120);

    }
            else{  ///asteroides
            path.moveTo((float) 0.3, (float) 0.0);
            path.lineTo((float) 0.6, (float) 0.0);
            path.lineTo((float) 0.6, (float) 0.3);
            path.lineTo((float) 0.8, (float) 0.2);
            path.lineTo((float) 1.0, (float) 0.4);
            path.lineTo((float) 1.1, (float) 0.7);
            path.lineTo((float) 0.9, (float) 0.9);
            path.lineTo((float) 0.8, (float) 1.0);
            path.lineTo((float) 0.4, (float) 1.0);
            path.lineTo((float) 0.0, (float) 0.6);
            path.lineTo((float) 0.0, (float) 0.2);
            path.lineTo((float) 0.3, (float) 0.0);
            draw = new ShapeDrawable( new PathShape(path, 1, 1));
            draw.getPaint().setColor(Color.WHITE);
            draw.getPaint().setStyle(Paint.Style.STROKE);
            draw.setIntrinsicWidth(120);
            draw.setIntrinsicHeight(120);
        }
        return draw;
    }
    /* Verifica colision entre asteroides o cualquier clase de tipo grafico (nave)*/
    public boolean verificaColisionGraficos(Grafico g){
        boolean colision = false;
        for(Grafico asteroide: asteroides){
            if(asteroide.getCenX()!=0 && asteroide.getCenY()!=0){
                if(asteroide.equals(g)) break;
                colision = asteroide.verificaColision(g);
                if(colision) break;
            }else {
                break;
            }
        }
        return colision;
    }
}
