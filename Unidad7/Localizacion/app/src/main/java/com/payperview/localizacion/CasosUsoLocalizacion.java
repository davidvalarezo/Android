package com.payperview.localizacion;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

public class CasosUsoLocalizacion {
    private static final String TAG = "MisLugares";
    private Activity actividad; private int codigoPermiso;
    private LocationManager manejadorLoc;
    private Location mejorLoc;
    /*private GeoPunto posicionActual;
    private AdaptadorLugares adaptador;*/

    public CasosUsoLocalizacion(Activity actividad, int codigoPermiso) {
        this.actividad = actividad;
        this.codigoPermiso = codigoPermiso;
        manejadorLoc = (LocationManager)actividad.getSystemService(Context.LOCATION_SERVICE);
        //posicionActual = ((Application) actividad.getApplication()).posicionActual;
        //adaptador = ((Aplicacion) actividad.getApplication()).adaptador;
        ultimaLocalizazion();
    }

    public boolean hayPermisoLocalizacion() {
        return (ActivityCompat.checkSelfPermission( actividad, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    void ultimaLocalizazion(){
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation( LocationManager.GPS_PROVIDER));
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
               // actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation( LocationManager.NETWORK_PROVIDER));
            }
        } else {
            //solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso localización no puedo mostrar la distancia"+ " a los lugares.",
             //       codigoPermiso, actividad);
                  }
    }
}
