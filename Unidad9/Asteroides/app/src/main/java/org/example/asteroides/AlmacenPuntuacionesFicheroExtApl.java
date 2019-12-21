package org.example.asteroides;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.example.asteroides.MainActivity.pref;

public class AlmacenPuntuacionesFicheroExtApl implements AlmacenPuntuaciones {
    //private static String FICHERO = Environment. getExternalStorageDirectory() + "/puntuaciones.txt";
    private Context context;
    //private static final int SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE = 0;
    //File path;
    File file;
    File[] pathList;

    public AlmacenPuntuacionesFicheroExtApl(Context context) {
        this.context = context;
        //path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        pathList = context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
        file = crearDirectorio();
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        String stadoSD = Environment.getExternalStorageState();
        if (!stadoSD.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "No puedo escribir en la memoria externa", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            FileOutputStream f = new FileOutputStream(file, true);
            String texto = puntos + " " + nombre + "\n";
            f.write(texto.getBytes());
            f.close();

        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }

    }

    @Override
    public List<String> listaPuntuaciones(int cantidad) {
        List<String> result = new ArrayList<String>();
        String stadoSD = Environment.getExternalStorageState();
        if (!stadoSD.equals(Environment.MEDIA_MOUNTED) && !stadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(context, "No puedo leer en la memoria externa", Toast.LENGTH_SHORT).show();
            return result;
        }

        try {
            FileInputStream f = new FileInputStream(file);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(f));
            int n = 0;
            String linea;
            do {
                linea = entrada.readLine();
                if (linea != null) {
                    result.add(linea);
                    n++;
                }
            } while (n < cantidad && linea != null);
            f.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        return result;
    }

    /*boolean hasExternalStoragePrivateFile() {
        path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (path != null) {
            file = new File(path, "puntuaciones.txt");
            return file.exists();
        }
        return false;
    }*/

    public File crearDirectorio(){
        if(pathList.length > 1 &&
                pref.getString("extDirPath", "0").equals("1")){ //Phone
            file = new File(pathList[1], "puntuaciones.txt");
            Toast.makeText(context, "SDCard1", Toast.LENGTH_SHORT).show();
        }
        else{
            if(pathList.length == 1){
                Toast.makeText(context, "No existe la segunda partición de memoria externa", Toast.LENGTH_SHORT).show();
            }
            file = new File(pathList[0], "puntuaciones.txt"); //Card Extraible
            Toast.makeText(context, "SDCard0", Toast.LENGTH_SHORT).show();
        }
        return file;
    }



}
