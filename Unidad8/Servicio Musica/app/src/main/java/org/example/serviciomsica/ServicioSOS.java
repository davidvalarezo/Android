package org.example.serviciomsica;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ServicioSOS extends Service {
    private NotificationManager notificationManager;
    static final String CANAL_ID = "SOS";
    static final int NOTIFICACION_ID = 2;
    MediaPlayer reproductor;

    @Override public void onCreate() {
        Toast.makeText(this,"Servicio SOS creado", Toast.LENGTH_SHORT).show();
        reproductor = MediaPlayer.create(this, R.raw.morat);
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        sosAnuncio();
        Toast.makeText(this,"Servicio SOS arrancado "+ idArranque, Toast.LENGTH_SHORT).show();
        reproductor.start();
        return START_STICKY;
    }

    @Override public void onDestroy() {
        Toast.makeText(this,"Servicio SOS detenido", Toast.LENGTH_SHORT).show();
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


    public void sosAnuncio(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel =
                    new NotificationChannel( CANAL_ID, "Mis Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 100, 300, 100});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Título")
                        .setContentText("«¡SOCORRO!»")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                android.R.drawable.ic_media_play))
                        .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                        .setContentInfo("más info")
                        .setTicker("Texto en barra de estado")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setDefaults(Notification.DEFAULT_VIBRATE);

        PendingIntent intencionPendiente = PendingIntent
                .getActivity( this, 0,
                        new Intent(this, MainActivity.class), 0);
        notificacion.setContentIntent(intencionPendiente);

        //notificationManager.notify(NOTIFICACION_ID, notificacion.build());
        startForeground(NOTIFICACION_ID, notificacion.build());

    }
}
