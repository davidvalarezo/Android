package org.example.ejemplogooglemaps;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static org.example.ejemplogooglemaps.ServicioCirculoPolar.localizacionCP;

public class ActividadVerMapa extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap mapaCP;
    private LatLng CP;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaCP);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(localizacionCP != null){
            CP = new LatLng(localizacionCP.getLatitude(), localizacionCP.getLongitude());
        }else{
            CP = new LatLng(39.481106, -0.340987);
        }
        mapaCP = googleMap;
        mapaCP.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapaCP.getUiSettings().setZoomControlsEnabled(false);
        mapaCP.moveCamera(CameraUpdateFactory.newLatLngZoom(CP, 15));
        mapaCP.addMarker(new MarkerOptions()
                .position(CP)
                .title("CP")
                .snippet("Circulo Polar")
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f)); mapaCP.setOnMapClickListener(this);
                if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) { mapaCP.setMyLocationEnabled(true);
                    mapaCP.getUiSettings().setCompassEnabled(true); }
    }

    public void moveCamera(View view) { mapaCP.moveCamera(CameraUpdateFactory.newLatLng(CP)); }
    public void animateCamera(View view) { mapaCP.animateCamera(CameraUpdateFactory.newLatLng(CP)); }
    public void addMarker(View view) { mapaCP.addMarker(new MarkerOptions().position( mapaCP.getCameraPosition().target)); }
    @Override public void onMapClick(LatLng puntoPulsado) {
        mapaCP.addMarker(new MarkerOptions()
                .position(puntoPulsado)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); }
}
