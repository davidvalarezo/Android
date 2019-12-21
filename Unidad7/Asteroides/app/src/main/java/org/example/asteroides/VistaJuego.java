package org.example.asteroides;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.example.asteroides.MainActivity.pref;
import static org.example.asteroides.PreferenciasFragment.valor;

public class VistaJuego extends View implements SensorEventListener {
    // //// ASTEROIDES //////
    private List<Grafico> asteroides; // Lista con los Asteroides
    private int numAsteroides = 5; // Número inicial de asteroides
    private int numFragmentos = 3; // Fragmentos en que se divide
    private Drawable drawableAsteroides[]= new Drawable[3];

    // //// NAVE //////
    private Grafico nave; // Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private double aceleracionNave; // aumento de velocidad
    private static final int MAX_VELOCIDAD_NAVE = 20;
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    // //// THREAD Y TIEMPO //////
    // Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;

    private float mX=0, mY=0;
    private boolean disparo=false;
    //Sensores
    private boolean hayValorInicial = false;
    private boolean hayValorInicialo = false;
    private float valorInicial;
    public final float[] gravity = new float[3];
    public final float[] orientation = new float[3];
    //
    // //// MISIL //////
    private Grafico misil;
    private ArrayList<Grafico> misiles;
    private static int PASO_VELOCIDAD_MISIL = 12;
    //private boolean misilActivo = false;
    //private int tiempoMisil;
    private ArrayList<Integer> tiempoMisiles;
    private Drawable drawableNave, drawableAsteroide, drawableMisil, drawablePlay, drawableInicio;
    private AnimationDrawable animacionMisil, animacion;
    //private SharedPreferences pref;

    Sensor accelerometerSensor;//orientationSensor, ;

    // //// MULTIMEDIA //////
    private SoundPool soundPool;
    private int idDisparo, idExplosion;
    //MediaPlayer mpDisparo, mpExplosion;

    private boolean inicia;
    private Grafico play, stop, numeros, inicio;


    public VistaJuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //this.setFocusable(true);
        //this.setOnKeyListener(this);
        //pref = PreferenceManager. getDefaultSharedPreferences(getContext());

        //mpDisparo = MediaPlayer.create(context, R.raw.disparo);
        //mpExplosion = MediaPlayer.create(context, R.raw.explosion);
        inicia =  true;
        createSoundPool();
        activarSensores();

        if(valor != numFragmentos){
            numFragmentos = valor;
        }

        if(pref.getString("graficos", "0").equals("0")){
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            /*Hacer aleatorio eltipo de asteroide*/
            int tipo = (int) (Math.random()*3 +1);
            drawableAsteroide = shapeGrafico("asteroides"+tipo);
            drawableNave = shapeGrafico("nave1");
            drawableMisil = shapeGrafico("misil");
            setBackgroundColor(Color.BLACK);

            for (int i=0; i<3; i++) {
                ShapeDrawable dAsteroide = shapeGrafico("asteroides"+i);
                dAsteroide.setIntrinsicWidth(50 - i * 14);
                dAsteroide.setIntrinsicHeight(50 - i * 14);
                drawableAsteroides[i] = dAsteroide;
            }
        }
        else if(pref.getString("graficos", "0").equals("2")){
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            drawableAsteroide = AppCompatResources.getDrawable(context, R.drawable.ic_asteroid1);
            drawableNave = AppCompatResources.getDrawable(context, R.drawable.ic_nave);
            drawableMisil = AppCompatResources.getDrawable(context, R.drawable.ic_misil1);
            animacionMisil = (AnimationDrawable) AppCompatResources.getDrawable(context, R.drawable.animation_misiles2);
            this.setBackgroundResource(R.drawable.fondo2);

            drawableAsteroides[0] = AppCompatResources.getDrawable(context, R.drawable.ic_asteroid1);
            drawableAsteroides[1] = AppCompatResources.getDrawable(context, R.drawable.ic_asteroid2);
            drawableAsteroides[2] = AppCompatResources.getDrawable(context, R.drawable.ic_asteroid3);
        }
        else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            drawableAsteroide = AppCompatResources.getDrawable(context, R.drawable.asteroide1);
            drawableNave = AppCompatResources.getDrawable(context, R.drawable.nave);
            drawableMisil = AppCompatResources.getDrawable(context, R.drawable.misil1);
            animacionMisil = (AnimationDrawable) AppCompatResources.getDrawable(context, R.drawable.animation_misiles);

            drawableAsteroides[0] = AppCompatResources.getDrawable(context, R.drawable.asteroide1);
            drawableAsteroides[1] = AppCompatResources.getDrawable(context, R.drawable.asteroide2);
            drawableAsteroides[2] = AppCompatResources.getDrawable(context, R.drawable.asteroide3);
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
        misiles = new ArrayList<Grafico>();
        tiempoMisiles  = new ArrayList<Integer>();

        drawablePlay =AppCompatResources.getDrawable(context, R.drawable.ic_stop);
        play = new Grafico(this, drawablePlay);

        //animacion = (AnimationDrawable) AppCompatResources.getDrawable(context, R.drawable.animation);
        //numeros = new Grafico(this, animacion);
        drawableInicio = AppCompatResources.getDrawable(context, R.drawable.ic_comet);
        inicio = new Grafico(this, drawableInicio);

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
        //if(thread.pausa){
        play.setCenX(50);
        play.setCenY(50);
        //}
        inicio.setCenX(ancho/2);
        inicio.setCenY(alto/2);


        ultimoProceso = System.currentTimeMillis();
        thread.start();

    }

    @Override
    protected void onDraw(Canvas canvas)  {
        super.onDraw(canvas);
        synchronized (asteroides) {
            for (Grafico asteroide: asteroides) {
                asteroide.dibujaGrafico(canvas);
            }
        }
        /* Dibujando la nave en el canvas */
        nave.dibujaGrafico(canvas);
        if(!misiles.isEmpty()){
            synchronized (misiles) {
                for(Grafico misil: misiles){
                    misil.dibujaGrafico(canvas);
                }
            }
        }
        //if(thread.pausa){
        play.dibujaGrafico(canvas);
        //}*/
        if(inicia){
            inicio.dibujaGrafico(canvas);
        }


    }

    protected void actualizaFisica ()  {
        float hora = System.currentTimeMillis();
        if(inicia){
            try {
                SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
                //if(!animacion.isRunning())System.out.println("no esta corriendo");
                //animacionNumeros();
                this.postInvalidate();

                ultimoProceso = System.currentTimeMillis();
                inicia = false;
            }catch (Exception e) {
            }
        }

        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return; // Salir si el período de proceso no se ha cumplido.
        }
        // Para una ejecución en tiempo real calculamos el factor de movimiento
        double factorMov = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
        // Actualizamos velocidad y dirección de la nave a partir de // giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * factorMov));
        double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * factorMov;
        double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * factorMov;
        // Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX,nIncY) <= MAX_VELOCIDAD_NAVE){
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementaPos(factorMov); // Actualizamos posición
        for (Grafico asteroide : asteroides) {
            asteroide.incrementaPos(factorMov);
        }

        // Actualizamos posición de misil
        if (!misiles.isEmpty()) {
            //int m =0;
            synchronized (misiles){
                for(int m=0; m< misiles.size(); m++){
                    misiles.get(m).incrementaPos(factorMov);
                    //tiempoMisil-=factorMov;
                    tiempoMisiles.set(m, (int) (tiempoMisiles.get(m)-factorMov));
                    if (tiempoMisiles.get(m) < 0) {
                        //misilActivo = false;
                        destruyeMisil(m);
                        m--;
                    } else {
                        for (int i = 0; i < asteroides.size(); i++)
                            if (misiles.get(m).verificaColision(asteroides.get(i))) {
                                destruyeAsteroide(i);
                                destruyeMisil(m);
                                m--;
                                break;
                            }
                    }
                   // m++;
                }
            }

        }//Fin de dibujar misil
    }

    private void destruyeAsteroide(int i) {
        int tam;
        if(asteroides.get(i).getDrawable()!=drawableAsteroides[2]){
            if(asteroides.get(i).getDrawable()==drawableAsteroides[1]){
                tam=2;
            } else {
                tam=1;
            }
            for(int n=0;n<numFragmentos;n++){
                Grafico asteroide = new Grafico(this,drawableAsteroides[tam]);
                asteroide.setCenX(asteroides.get(i).getCenX());
                asteroide.setCenY(asteroides.get(i).getCenY());
                asteroide.setIncX(Math.random()*7-2-tam);
                asteroide.setIncY(Math.random()*7-2-tam);
                asteroide.setAngulo((int)(Math.random()*360));
                asteroide.setRotacion((int)(Math.random()*8-4));
                asteroides.add(asteroide);
            }
        }

        synchronized (asteroides) {
            asteroides.remove(i);
            //mpExplosion.start();
            soundPool.play(idExplosion, 1, 1, 1, 0, 1);
            this.postInvalidate();
        }
    }
     private void destruyeMisil(int m) {
        synchronized (misiles) {
            tiempoMisiles.remove(m);
            misiles.remove(m);
            this.postInvalidate();
        }
    }

    private void activaMisil() {

        if(pref.getString("graficos", "0").equals("0")){
            misil = new Grafico(this, drawableMisil);
        }else{
            misil = new Grafico(this, animacionMisil);
            animacionMisil.setCallback(new Drawable.Callback() {

                @Override
                public void unscheduleDrawable(Drawable who, Runnable what) {
                    misil.getView().removeCallbacks(what);
                }

                @Override
                public void scheduleDrawable(Drawable who, Runnable what, long when) {
                    misil.getView().postDelayed(what, when - SystemClock.uptimeMillis());
                }

                @Override
                public void invalidateDrawable(Drawable who) {
                    misil.getView().postInvalidate();
                }
            });
        }

        misil.setCenX(nave.getCenX());
        misil.setCenY(nave.getCenY());
        misil.setAngulo(nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);

        synchronized (misiles) {
            misiles.add(misil);
            tiempoMisiles.add( (int) Math.min(this.getWidth() / Math.abs( misil. getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 4 );
            if(!pref.getString("graficos", "0").equals("0")){
                animacionMisil.start();
            }
            soundPool.play(idDisparo, 1, 1, 1, 0, 2);
        }
            //misilActivo = true;
        //mpDisparo.start();
    }

    private void animacionNumeros() {

            //numeros = new Grafico(this, animacion);
            animacion.setCallback(new Drawable.Callback() {

                @Override
                public void unscheduleDrawable(Drawable who, Runnable what) {
                    numeros.getView().removeCallbacks(what);
                }

                @Override
                public void scheduleDrawable(Drawable who, Runnable what, long when) {
                    numeros.getView().postDelayed(what, when - SystemClock.uptimeMillis());
                }

                @Override
                public void invalidateDrawable(Drawable who) {
                    numeros.getView().postInvalidate();
                }
            });
        //numeros.setCenX(300);
        //numeros.setCenY(300);
        animacion.start();
        //misilActivo = true;
    }


    public ShapeDrawable shapeGrafico(String shape){
        Path path = new Path();
        ShapeDrawable draw;
        if(shape.equals("misil")){
            draw = new ShapeDrawable(new RectShape());
            draw.getPaint().setColor(Color.WHITE);
            draw.getPaint().setStyle(Paint.Style.STROKE);
            draw.setIntrinsicWidth(15);
            draw.setIntrinsicHeight(3);
            //drawableMisil = dMisil;

        }else if(shape.equals("nave")){
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

    @Override
    public boolean onKeyDown (int codigoTecla, KeyEvent event){
        super.onKeyDown(codigoTecla, event);
        // Suponemos que vamos a procesar la pulsacion
        boolean procesada = true;
        switch (codigoTecla){
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = +PASO_ACELERACION_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave = -PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = +PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                activaMisil();
                break;
            default:
            // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return  procesada;
    }

    @Override public boolean onKeyUp(int codigoTecla, KeyEvent evento) {
        super.onKeyUp(codigoTecla, evento);
        // Suponemos que vamos a procesar la pulsación
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = 0;
                break;
            default:
            // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return procesada;
    }

    @Override
    synchronized  public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        if(event.getPointerCount()>1 || (x<70&&y<70)){
            //if(event.getDownTime()>500){
                if (thread.pausa){
                    if(MotionEvent.ACTION_POINTER_UP==6)
                        thread.reanudar();
                }else{
                    if(MotionEvent.ACTION_POINTER_DOWN==5){
                        thread.pausar();
                   /* play = new Grafico(this, drawablePlay);
                    play.setCenX(200/2);
                    play.setCenY(200/2);*/
                    }
                }
            //}

        }

        if(pref.getString("control", "0").equals("0")){
         x = event.getX();
         y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo=true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy<6 && dx>6){
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;
                } else if (dx<6 && dy>6){
                    aceleracionNave = Math.abs(Math.round((mY - y) / 25));
                    disparo = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                if (disparo && !thread.pausa){
                    activaMisil();
                }
                break;
        }
        mX=x; mY=y;
        }
        return true;
    }

    boolean estado = false;
    @Override
    public void onSensorChanged(SensorEvent event) {
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate
        // final float alpha = 0.8F;
        if(pref.getString("control", "0").equals("1")){
        float xo, yo, zo;
        float x =0, y =0 , z=0;

            if(event.sensor == accelerometerSensor){
                gravity[0] = event.values[0];
                gravity[1] = event.values[1];
                gravity[2] = event.values[2];

            if (!hayValorInicial){
                x = gravity[0] ;
                y = gravity[1] ;
                z = gravity[2] ;
                hayValorInicial = true;
            }
            float dy = Math.abs(gravity[1]-y);
            if(dy>0.5){
                giroNave=(int) (gravity[1]-y)/1 ;
            }
            float dz = Math.abs(gravity[2]-z);
            if(dz>8.5){
                disparo = true;
            }
            if(disparo){
                if(dz<6.0){
                    activaMisil();
                    disparo = false;
                }
            }
            if(dy<=1.0){

                float dx = Math.abs(gravity[0]-x);
                double velocidad =Math.hypot( nave.getIncX(),nave.getIncY());

                double a = nave.getIncY();
                double b = nave.getIncX();
                int angulo =  (b==0) ? 0: (int)(a/b);
                double auxvelocidad = angulo<0? -velocidad:velocidad;
                    int aux = (int)(4.5-dx);
                if(aux<0){
                    if(auxvelocidad<1 ){
                        aceleracionNave = 0;
                    }else{
                        aceleracionNave = -(PASO_ACELERACION_NAVE/2);

                    }
                }else if (aux>1){
                    aceleracionNave = (PASO_ACELERACION_NAVE/2);
                }else{
                    aceleracionNave = 0;
                }
                //System.out.println(aux);
            }
        }

        /*if(event.sensor == orientationSensor){
            xo = event.values[0];
            yo = event.values[1];
            zo = event.values[2];
            if (!hayValorInicialo){
                orientation[0] = xo;
                orientation[1] = yo;
                orientation[2] = zo;
                hayValorInicialo = true;
            }
            float dyo = Math.abs(yo-orientation[1]);
            if(dyo>8){
                int orientacion = ((int) (orientation[1] - yo )/3);
                giroNave=( orientacion > -3 && orientacion < 3)?0:orientacion;
            }

            if(Math.abs( gravity[1])<=1.5 && dyo >7){
                int aceleracion = ((int) (orientation[1] - yo )/3);
                //aceleracionNave= ( aceleracion > -2 && aceleracion < 2)?0:aceleracion;
            }
        }*/

        }
    }

    public void activarSensores(){
        // Sensores
        SensorManager mSensorManager = (SensorManager) this.getContext().getSystemService( Context.SENSOR_SERVICE);
        //List<Sensor> listSensorsO = mSensorManager.getSensorList( Sensor.TYPE_ORIENTATION);
        List<Sensor> listSensorsA = mSensorManager.getSensorList( Sensor.TYPE_ACCELEROMETER);
        if (/*!listSensorsO.isEmpty() && */!listSensorsA.isEmpty()) {
            //orientationSensor = listSensorsO.get(0);
            accelerometerSensor = listSensorsA.get(0);
            //mSensorManager.registerListener(this, orientationSensor,
            // SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this, accelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        /*****/
    }

    public void desactivarSensores(){
        // Sensores
        SensorManager mSensorManager = (SensorManager) this.getContext().getSystemService( Context.SENSOR_SERVICE);
        //List<Sensor> listSensorsO = mSensorManager.getSensorList( Sensor.TYPE_ORIENTATION);
        List<Sensor> listSensorsA = mSensorManager.getSensorList( Sensor.TYPE_ACCELEROMETER);
        if (/*!listSensorsO.isEmpty() && */!listSensorsA.isEmpty()) {
            //orientationSensor = listSensorsO.get(0);
            accelerometerSensor = listSensorsA.get(0);
            //mSensorManager.registerListener(this, orientationSensor,
            // SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.unregisterListener(this, accelerometerSensor);
        }
        /*****/
    }

    public void createSoundPool(){
        soundPool = new SoundPool( 5, AudioManager.STREAM_MUSIC , 0);
        idDisparo = soundPool.load(this.getContext(), R.raw.disparo, 0);
        idExplosion = soundPool.load(this.getContext(), R.raw.explosion, 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }

    class ThreadJuego extends Thread {
        private boolean pausa,corriendo;
        public synchronized void pausar() {
            pausa = true;
        }

        public synchronized void reanudar(){
            pausa = false;
            ultimoProceso = System.currentTimeMillis();
            notify();       // Desbloqueamos el Wait
        }

        public void detener() {
            corriendo = false;
            if (pausa) reanudar();
        }

        @Override
        public void run() {
            corriendo = true;
            while (corriendo) {
                actualizaFisica();
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        }catch (Exception e) {
                        }
                    }

                }
            }
        }

        /*public boolean isPausa() {
            return pausa;
        }*/
    }

    public ThreadJuego getThread() {
        return thread;
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
    }

    /*public boolean isInicia() {
        return inicia;
    }*/

    public void setInicia(boolean inicia) {
        this.inicia = inicia;
    }

    /*public Grafico getPlay() {
        return play;
    }*/

}
