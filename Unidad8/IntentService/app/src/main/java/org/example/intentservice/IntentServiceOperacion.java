package org.example.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import static org.example.intentservice.MainActivity.*;

public class IntentServiceOperacion extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IntentServiceOperacion() {
        super("Nombre de mi servicio");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        double n = intent.getExtras().getDouble("numero");
        SystemClock.sleep(5000);
        //MainActivity.salida.append(n*n + "\n");
/*********************************************************************************/
        Intent i = new Intent();
        i.setAction(MainActivity.ReceptorOperacion.ACTION_RESP);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.putExtra("resultado", n*n);
        sendBroadcast(i);
/**************************************************************************************/
    }
}
