package org.example.ejemplogooglemaps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private View vista_principal;
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    static final long TIEMPO_MIN = 10 * 1000; // 10 segundos
    static final long DISTANCIA_MIN = 5; // 5 metros
    private static final long DOS_MINUTOS = 2 * 60 * 1000;
    LocationManager manejadorLoc;
    Location localizacion;
    private GoogleMap mapa;
    private final String TAG = "YO";
    static public LatLng PActual;
    static double latitude, longitude;

    static public ArrayList<MarkerOptions> mMarkers = new ArrayList<MarkerOptions>();
    SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vista_principal = findViewById(R.id.vista_principal);
        manejadorLoc = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        localizacion = getMyLocation();
        aplicacionUbicacion(localizacion);
/**************************************************************************************/
        IntentFilter filtro = new IntentFilter(ReceptorLlamadas.ACTION_RESP);
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(new ReceptorLlamadas(), filtro);

/**************************************************************************************/
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @SuppressLint("MissingPermission")
    public Location getMyLocation() {
        Location location = null;
        if (hayPermisoLocalizacion()) {
            location = manejadorLoc.getLastKnownLocation(manejadorLoc.GPS_PROVIDER);
            if(location==null)
            {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                location=manejadorLoc.getLastKnownLocation(
                        manejadorLoc.getBestProvider(criteria, true));
            }
            //miDireccion(location);

        }else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso de ubicación no se puede mostrar tu posición actual.",
                    SOLICITUD_PERMISO_LOCALIZACION, this);
        }
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
/********/

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
            } else  {
                solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                        "Sin el permiso localización no puedo mostrar la ubicación actual.", SOLICITUD_PERMISO_LOCALIZACION, this);
            }
        }else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso de ubicación no se puede mostrar tu posición actual.",
                    SOLICITUD_PERMISO_LOCALIZACION, this);
                    localizacion = getMyLocation();
                    actualizaMejorLocaliz(localizacion );
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode,
                                                     String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            permisoConcedido();
        }else{
            aplicacionUbicacion(localizacion );
        }


    }

    public void permisoConcedido() {
        ultimaLocalizazion();
        activarProveedores();
        moveCamera(null);
       // adaptador.notifyDataSetChanged();
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
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso localización no puedo mostrar la distancia"+
                            " a los lugares.", SOLICITUD_PERMISO_LOCALIZACION, this);
        }
    }



    private void actualizaMejorLocaliz(Location localiz) {
        if (localiz != null && (localizacion == null
                || localiz.getAccuracy() < 2*localizacion.getAccuracy()
                || localiz.getTime() - localizacion.getTime() > DOS_MINUTOS)) {
            localizacion = localiz;
            aplicacionUbicacion(localizacion);
        }
    }

    private void aplicacionUbicacion(Location localiz){
        if (localiz != null) {
            latitude = localiz.getLatitude();
            longitude = localiz.getLongitude();
        }else{
            latitude = 39.481106; longitude = -0.340987;
        }
        miDireccion(localiz);
        PActual = new LatLng(latitude, longitude);
    }

    private void miDireccion(Location location){
        String direccionText = "";
        if(location != null){
            if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (!list.isEmpty()) {
                        Address address = list.get(0);
                        direccionText= "Mi dirección es: " + address.getAddressLine(0);
                    }
                } catch (IOException e) {
                    direccionText= "latitude: "+location.getLatitude() +" longitude: "+location.getLongitude();
                    e.printStackTrace();
                }
            }
        }else{
            direccionText = "Se requiere permiso de ubicación para mostrar tu dirección";
        }

        Snackbar.make(vista_principal, direccionText , Snackbar.LENGTH_LONG).show();
    }

    @Override protected void onResume() {
        super.onResume();
        //addMarkerNumber(null);
        if (hayPermisoLocalizacion()) activarProveedores();
    }

    @Override protected void onPause() {
        super.onPause();
        if (hayPermisoLocalizacion()) manejadorLoc.removeUpdates(this);
    }


    @Override public void onLocationChanged(Location location) {
        actualizaMejorLocaliz(location);
        moveCamera(null);
        // adaptador.notifyDataSetChanged();
    }
    @Override public void onProviderDisabled(String proveedor) {
        activarProveedores();
    }
    @Override public void onProviderEnabled(String proveedor) {
        activarProveedores();
    }
    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        activarProveedores();
    }


    /**********************/
    private MarkerOptions makerOptions;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(PActual, 15));
        /*makerOptions = new MarkerOptions() .position(PActual) .title(TAG) .snippet("Mi posición actual")
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_compass)) .anchor(0.5f, 0.5f);
        mapa.addMarker(makerOptions);*/
        mapa.setOnMapClickListener(this);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
        }

    }

    public void moveCamera(View view) {
        mapa.clear();
        mapa.moveCamera(CameraUpdateFactory.newLatLng(PActual));
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(PActual, 15));
        makerOptions = new MarkerOptions() .position(PActual) .title(TAG) .snippet("Mi posición actual")
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_compass)) .anchor(0.5f, 0.5f);
        //mMarkers.add(makerOptions);
        mapa.addMarker(makerOptions);
        if(!mMarkers.isEmpty()){
            for (MarkerOptions item : mMarkers) {
                mapa.addMarker(item);
            }
        }else{
            String s = myPreferences.getString("Numero", "");
            if (!s.isEmpty()) {
                mMarkers.add(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Numero")
                        .snippet(s)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }
        }

    }
    public void animateCamera(View view) {
        mapa.animateCamera(CameraUpdateFactory.newLatLng(PActual));
        miDireccion(localizacion);
    }
    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position( mapa.getCameraPosition().target));
        if(!mMarkers.isEmpty()){
            for (MarkerOptions item : mMarkers) {
                mapa.addMarker(item);
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mapa.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

/********************************************************************************************************/
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
                        mMarkers.add(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("Numero")
                                .snippet(numero)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


                    }else{
                        //Si no hay permiso de localizacion
                        mapa.addMarker(new MarkerOptions()
                                .position(PActual)
                                .title("Numero")
                                .snippet(numero)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    }
                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    myEditor.putString("Numero", numero);
                    myEditor.commit();


                }
            }


        }
    }




}


