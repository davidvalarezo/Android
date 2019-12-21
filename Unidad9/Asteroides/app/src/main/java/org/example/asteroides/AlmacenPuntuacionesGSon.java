package org.example.asteroides;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AlmacenPuntuacionesGSon implements AlmacenPuntuaciones {
    private String string; //Almacena puntuaciones en formato JSON
    private Context context;
    private static String PREFERENCIAS_GSON = "puntuacionesGSon";
    private Gson gson = new Gson();
    private Type type = new TypeToken<List<Puntuacion>>() {}.getType();
    //private Type type = new TypeToken<Clase>() {}.getType();

    public AlmacenPuntuacionesGSon(Context context) {
        this.context = context;
        //guardarPuntuacion(45000,"Mi nombre", System.currentTimeMillis());
        //guardarPuntuacion(31000,"Otro nombre", System.currentTimeMillis());
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        string = leerString();
        ArrayList<Puntuacion> puntuaciones;
        if (string == null) {
            puntuaciones = new ArrayList<>();
        } else {
            puntuaciones = gson.fromJson(string, type);
        }
        puntuaciones.add(new Puntuacion(puntos, nombre, fecha));
        string = gson.toJson(puntuaciones, type);
        guardarString(string);

        /*string = leerString();
        Clase objeto;
        if (string == null) {
            objeto = new Clase();
        } else {
            objeto = gson.fromJson(string, type);
        }
        objeto.puntuaciones.add(new Puntuacion(puntos, nombre, fecha));
        //objeto.guardado = true;
        string = gson.toJson(objeto, type);
        guardarString(string);*/
    }

    @Override
    public List<String> listaPuntuaciones(int cantidad) {
        string = leerString();
        ArrayList<Puntuacion> puntuaciones;
        if (string == null) {
            puntuaciones = new ArrayList<>();
        } else {
            puntuaciones = gson.fromJson(string, type);
        }
        List<String> salida = new ArrayList<>();
        for (Puntuacion puntuacion : puntuaciones) {
            salida.add(puntuacion.getPuntos()+" "+puntuacion.getNombre());
        }
        return salida;

        /*string = leerString();
        Clase objeto;
        if (string == null) {
            objeto = new Clase();
        } else {
            objeto = gson.fromJson(string, type);
        }
        List<String> salida = new ArrayList<>();
        for (Puntuacion puntuacion : objeto.puntuaciones) {
            salida.add(puntuacion.getPuntos()+" "+puntuacion.getNombre());
        }
        return salida;*/
    }

    public void guardarString(String string){
        SharedPreferences preferencias =context.getSharedPreferences(
                PREFERENCIAS_GSON, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString(PREFERENCIAS_GSON, string);
        editor.apply();
    }

    public String leerString(){
        String result = null;
        SharedPreferences preferencias =context.getSharedPreferences( PREFERENCIAS_GSON, Context.MODE_PRIVATE);
        String s = preferencias.getString(PREFERENCIAS_GSON, "");
        if (!s.isEmpty()) {
            result = s;
        }
       return result;
    }

/*
    public class Clase {
        private ArrayList<Puntuacion> puntuaciones = new ArrayList<>();
        private boolean guardado;
    }*/
}
