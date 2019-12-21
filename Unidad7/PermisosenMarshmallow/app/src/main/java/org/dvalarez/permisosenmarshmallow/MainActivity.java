package org.dvalarez.permisosenmarshmallow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private View vista_principal;
    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vista_principal = findViewById(R.id.vista_principal);

        borrarLlamada();
    }

    void borrarLlamada() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission .WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            getContentResolver().delete(CallLog.Calls.CONTENT_URI, "number='555555555'", null);
            Snackbar.make(vista_principal, "Llamadas borradas del registro.", Snackbar.LENGTH_SHORT).show();
        }else {
            solicitarPermiso(Manifest.permission.WRITE_CALL_LOG, "Sin el permiso"+ " administrar llamadas no puedo borrar llamadas del registro.",
                    SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
        }

    }

    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode); }}).show();

        }else {
            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SOLICITUD_PERMISO_WRITE_CALL_LOG) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { borrarLlamada(); } else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la " + "acción", Toast.LENGTH_SHORT).show(); }
        }
    }


}
