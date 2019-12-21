package org.example.serviciomsica;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class ServicioMusica extends Service {
    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;
    MediaPlayer reproductor;


    @Override public void onCreate() {
        Toast.makeText(this,"Servicio creado", Toast.LENGTH_SHORT).show();
        reproductor = MediaPlayer.create(this, R.raw.audio);
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel( CANAL_ID, "Mis Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
        notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Título")
                        .setContentText("Texto de la notificación.")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                android.R.drawable.ic_media_play))
                        .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                        .setContentInfo("más info")
                        .setTicker("Texto en barra de estado")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setDefaults(Notification.DEFAULT_VIBRATE);
        PendingIntent intencionPendiente = PendingIntent
                .getActivity( this, 0,
                        new Intent(this, MainActivity.class), 0);
        notificacion.setContentIntent(intencionPendiente);
        startForeground(NOTIFICACION_ID, notificacion.build());
        notificationManager.notify(NOTIFICACION_ID, notificacion.build());
        /*NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setContentIntent(pendingIntent); //Required on Gingerbread and below*/

        Toast.makeText(this,"Servicio arrancado "+ idArranque, Toast.LENGTH_SHORT).show();
        reproductor.start();
        return START_STICKY;
    }
    @Override public void onDestroy() {
        Toast.makeText(this,"Servicio detenido", Toast.LENGTH_SHORT).show();
        reproductor.stop();
        reproductor.release();
        stopForeground( false);
        notificationManager.cancel(NOTIFICACION_ID);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
