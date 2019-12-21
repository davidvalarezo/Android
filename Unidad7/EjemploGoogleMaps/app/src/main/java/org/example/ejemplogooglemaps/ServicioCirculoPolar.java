package org.example.ejemplogooglemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class ServicioCirculoPolar extends Service implements LocationListener {

    private NotificationManager notificationManager;
    static final String CANAL_ID_CP = "Circulo Polar";
    static final int NOTIFICACION_ID_CP = 101;
    NotificationCompat.Builder notificacion;
    static final double latitudCirculoP =66.55;



    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    static final long TIEMPO_MIN = 10 * 1000; // 10 segundos
    static final long DISTANCIA_MIN = 5; // 5 metros
    private static final long DOS_MINUTOS = 2 * 60 * 1000;
    LocationManager manejadorLoc;
    static public Location localizacionCP;
    public boolean isCPolar = false;

    @Override
    public void onCreate() {
        manejadorLoc = (LocationManager) getSystemService(LOCATION_SERVICE);
        ultimaLocalizazion();
        System.out.println("Circulo Polar");
        Log.d("CP","Servicio Circulo Polar");

    }
    @Override
    public int onStartCommand(Intent intencion, int flags, int idArran){
        Toast.makeText(this,"ServicioCirculoPolar arrancado "+ idArran, Toast.LENGTH_SHORT).show();

        if(!isCPolar) {
            return START_STICKY;
        }
        else {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel =
                        new NotificationChannel( CANAL_ID_CP, "Mis Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Descripcion del canal");
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificacion =
                    new NotificationCompat.Builder(this, CANAL_ID_CP)
                            .setSmallIcon(R.mipmap.ic_map)
                            .setContentTitle("SCP")
                            .setContentText("Servicio de Circulo Polar.")
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                    R.drawable.ic_oso_polar) )                    /*android.R.drawable.ic_media_play*/
                            .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                            .setDefaults(Notification.DEFAULT_VIBRATE);
            PendingIntent intencionPendiente = PendingIntent
                    .getActivity( this, 0,
                            new Intent(this, ActividadVerMapa.class), 0);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notificacion.setContentIntent(intencionPendiente);
            //notificationManager.notify(NOTIFICACION_ID, notificacion.build());
            startForeground(NOTIFICACION_ID_CP, notificacion.build());

            //Toast.makeText(this,"ServicioCirculoPolar arrancado "+ idArran, Toast.LENGTH_SHORT).show();
            return START_STICKY;
        }


    }
    @Override
    public void onDestroy() {
        Toast.makeText(this,"Servicio detenido", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(NOTIFICACION_ID_CP);
        stopForeground( false);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        actualizaMejorLocaliz( location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        activarProveedores();
    }

    @Override
    public void onProviderEnabled(String provider) {
        activarProveedores();
    }

    @Override
    public void onProviderDisabled(String provider) {
        activarProveedores();
    }



    public boolean hayPermisoLocalizacion() {
        return (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("MissingPermission")
    void ultimaLocalizazion(){
        if (hayPermisoLocalizacion()) {

            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER));
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER));
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void activarProveedores() {
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manejadorLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        2 * TIEMPO_MIN,DISTANCIA_MIN , this);
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                manejadorLoc.requestLocationUpdates(LocationManager
                        .NETWORK_PROVIDER, TIEMPO_MIN, 2 * DISTANCIA_MIN, this);
            }
        }
    }

    private void actualizaMejorLocaliz(Location localiz) {
        if (localiz != null && (localizacionCP == null
                || localiz.getAccuracy() < 2*localizacionCP.getAccuracy()
                || localiz.getTime() - localizacionCP.getTime() > DOS_MINUTOS)) {
            localizacionCP = localiz;

            if(localizacionCP != null){
                if(localizacionCP.getLatitude()>latitudCirculoP){
                    isCPolar = true;
                }
            }
        }
    }

    /**onCreate(): registra un LocationListener
     onLocationChanged(): si lat>66.55 lanzamos notificación asociada a ActividadVerMapa.**/
}
