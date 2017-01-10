package com.binx.fireline.ui.activity;

/**
 * Created by james on 1/6/17.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.binx.fireline.R;
import com.binx.fireline.model.Incident;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double lat;
    private Double lon;
    private ArrayList<Incident> incidentList;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        incidentList = (ArrayList<Incident>)intent.getSerializableExtra("Incidents");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        builder = new LatLngBounds.Builder();

        for(Incident incident: incidentList){
//            Log.i("MapsActivity", "lat: " + lat + " lon: " + lon);
            LatLng pos = new LatLng(incident.getLatitude(), incident.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pos).title(incident.getIncidentType()));
            builder.include(pos);
        }

        bounds = builder.build();

        if (incidentList.size() == 1) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(incidentList.get(0).getLatitude(), incidentList.get(0).getLongitude()), 14));
        } else {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.2655587,-119.0464405), 10));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 500, 500, 5));
        }
    }
}