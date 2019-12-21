package org.example.asteroides;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlmacenPuntuacionesJSon implements AlmacenPuntuaciones {
    private String string; //Almacena puntuaciones en formato JSON
    private Context context;
    private static String PREFERENCIAS_GSON = "puntuacionesGSon";
    public AlmacenPuntuacionesJSon(Context context) {
        this.context = context;
        //guardarPuntuacion(45000,"Mi nombre", System.currentTimeMillis());
        //guardarPuntuacion(31000,"Otro nombre", System.currentTimeMillis());
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        string = leerString();
        List<Puntuacion> puntuaciones = leerJSon(string);
        puntuaciones.add(new Puntuacion(puntos, nombre, fecha));
        string = guardarJSon(puntuaciones);
        guardarString(string);
    }

    @Override
    public List<String> listaPuntuaciones(int cantidad) {
        //string = leerFichero();
        string = leerString();
        List<Puntuacion> puntuaciones = leerJSon(string);
        List<String> salida = new ArrayList<>();
        for (Puntuacion puntuacion: puntuaciones) {
            salida.add(puntuacion.getPuntos()+" "+puntuacion.getNombre());
        }
        return salida;
    }

    private String guardarJSon(List<Puntuacion> puntuaciones) {
        String string = "";
        try {
            JSONArray jsonArray = new JSONArray();
            for (Puntuacion puntuacion : puntuaciones) {
                JSONObject objeto = new JSONObject();
                objeto.put("puntos", puntuacion.getPuntos());
                objeto.put("nombre", puntuacion.getNombre());
                objeto.put("fecha", puntuacion.getFecha());
                jsonArray.put(objeto);
            }
            string = jsonArray.toString();
        } catch (JSONException e) { e.printStackTrace(); }
        return string;

    }

    private List<Puntuacion> leerJSon(String string) {
        List<Puntuacion> puntuaciones = new ArrayList<>();
        try { JSONArray json_array = new JSONArray(string);
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objeto = json_array.getJSONObject(i);
                puntuaciones.add(new Puntuacion(objeto.getInt("puntos"), objeto.getString("nombre"),
                        objeto.getLong("fecha")));
            }
        } catch (JSONException e) { e.printStackTrace(); }
        return puntuaciones;
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
}
