//package org.example.ejemplogooglemaps;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static org.example.ejemplogooglemaps.MainActivity.PActual;
/*
public class ReceptorLlamadas extends BroadcastReceiver {
    public static final String ACTION_RESP = "android.intent.action.PHONE_STATE";
    //android.intent.action.PHONE_STATE

    @Override
    public void onReceive(Context context, Intent intent) {
        // Sacamos información de la intención
        String estado = "", numero = "";
        Bundle extras = intent.getExtras();
        if (extras != null) {

            estado = extras.getString(TelephonyManager.EXTRA_STATE);
            if (estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                numero = extras.getString( TelephonyManager.EXTRA_INCOMING_NUMBER);
                String info = estado + " " + numero;
                Log.d("ReceptorAnuncio", info + " intent=" + intent);

                // Creamos Notificación
                NotificationCompat.Builder notificacion = new NotificationCompat.Builder(context) .setContentTitle("Llamada entrante ")
                        .setContentText(info)
                        .setSmallIcon(R.mipmap.ic_map)
                        .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
                ((NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE)).notify(1,notificacion.build());

                    if(ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){
                        double latitude =  (PActual.latitude + (Math.random()*500 +1)/100000);
                        double longitude = (PActual.longitude + (Math.random()*500 +1)/100000);
                        new LatLng(latitude, longitude);
                        MainActivity.mMarkers.add(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("Numero")
                                .snippet(numero)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                    }else{
                        //Si no hay permiso de localizacion
                        MainActivity.mapa.addMarker(new MarkerOptions()
                                .position(PActual)
                                .title("Numero")
                                .snippet(numero)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    }



            }
        }


    }
}
*/