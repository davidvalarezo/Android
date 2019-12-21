package com.payperview.localizacion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    private CasosUsoLocalizacion usoLocalizacion;

    private View vista_principal;
    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION = 0;

    static final long TIEMPO_MIN = 10 * 1000; // 10 segundos
    static final long DISTANCIA_MIN = 5; // 5 metros
    static final String[] A = {"n/d", "preciso", "impreciso"};
    static final String[] P = {"n/d", "bajo", "medio", "alto"};
    static final String[] E = {"fuera de servicio", "temporalmente no disponible ", "disponible"};
    LocationManager manejadorLoc;
    String proveedor;
    TextView salida;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        salida = findViewById(R.id.salida);
        //manejadorLoc = (LocationManager) getSystemService(LOCATION_SERVICE);
        //log("Proveedores de localización: \n ");
        //muestraProveedores();
        /*Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejadorLoc.getBestProvider(criterio, true);
        log("Mejor proveedor: " + proveedor + "\n");
        log("Comenzamos con la última localización conocida:");*/
        //Location localizacion = manejadorLoc.getLastKnownLocation(proveedor);
        Location localizacion = getMyLocation(null);
        muestraLocaliz(localizacion);
    }

    // Métodos del ciclo de vida de la actividad
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        //manejadorLoc.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);
        //getMyLocation("Updates");

    }

    @Override
    protected void onPause() {
        super.onPause();
        manejadorLoc.removeUpdates(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private Location getMyLocation(String Updates) {
        manejadorLoc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /**/
        log("Proveedores de localización: \n ");
        muestraProveedores();
        /**/
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso"+ " administrar llamadas no puedo borrar llamadas del registro.",
                    SOLICITUD_PERMISO_ACCESS_FINE_LOCATION, this);
            //return TODO;
        }
        Location location = manejadorLoc.getLastKnownLocation(manejadorLoc.GPS_PROVIDER);
        if(location==null)
        {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            proveedor = manejadorLoc.getBestProvider(criteria, true);
            log("Mejor proveedor: " + proveedor + "\n");
            log("Comenzamos con la última localización conocida:");
            location=manejadorLoc.getLastKnownLocation(proveedor);
        }
        if(Updates.equals(Updates)) manejadorLoc.removeUpdates(this);
        return location;
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


    // Métodos de la interfaz LocationListener

    @Override
    public void onLocationChanged(Location location) {
        log("Nueva localización: ");
        muestraLocaliz(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        log("Cambia estado proveedor: " + proveedor + ", estado="
                + E[Math.max(0, status)] + ", extras=" + extras + "\n");
    }

    @Override
    public void onProviderEnabled(String provider) {
        log("Proveedor habilitado: " + proveedor + "\n");
    }

    @Override
    public void onProviderDisabled(String provider) {
        log("Proveedor deshabilitado: " + proveedor + "\n");
    }

    // Métodos para mostrar información
    private void log(String cadena) {
        salida.append(cadena + "\n");
    }

    private void muestraLocaliz(Location localizacion) {
        if (localizacion == null)
            log("Localización desconocida\n");
        else
            log(localizacion.toString() + "\n");
    }

    private void muestraProveedores() {
        log("Proveedores de localización: \n ");
        List<String> proveedores = manejadorLoc.getAllProviders();
        for (String proveedor : proveedores) {
            muestraProveedor(proveedor);
        }
    }

    private void muestraProveedor(String proveedor) {
        LocationProvider info = manejadorLoc.getProvider(proveedor);
        log("LocationProvider[ " + "getName=" + info.getName()
                + ", isProviderEnabled="
                + manejadorLoc.isProviderEnabled(proveedor) + ", getAccuracy="
                + A[Math.max(0, info.getAccuracy())] + ", getPowerRequirement="
                + P[Math.max(0, info.getPowerRequirement())]
                + ", hasMonetaryCost=" + info.hasMonetaryCost()
                + ", requiresCell=" + info.requiresCell()
                + ", requiresNetwork=" + info.requiresNetwork()
                + ", requiresSatellite=" + info.requiresSatellite()
                + ", supportsAltitude=" + info.supportsAltitude()
                + ", supportsBearing=" + info.supportsBearing()
                + ", supportsSpeed=" + info.supportsSpeed() + " ]\n");
    }
}
