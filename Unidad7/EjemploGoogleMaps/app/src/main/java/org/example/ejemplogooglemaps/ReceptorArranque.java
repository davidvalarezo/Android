package org.example.ejemplogooglemaps;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ReceptorArranque extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServicioCirculoPolar.class));

        // Creamos Notificación
       /* NotificationCompat.Builder notificacion = new NotificationCompat.Builder(context) .setContentTitle("Reinicio del SO ")
                .setContentText("Arranque") .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, ActividadVerMapa.class), 0));
        ((NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE)).notify(0,notificacion.build());*/

    }
}
