package com.binx.fireline.ui.activity;

/**
 * Created by james on 1/6/17.
 * Map activity which accepts an ArrayList and will plot all or just one
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binx.fireline.R;
import com.binx.fireline.adapter.IncidentImageAdapter;
import com.binx.fireline.model.Incident;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
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

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        for(Incident incident: incidentList){
            IncidentImageAdapter incidentImageAdapter = new IncidentImageAdapter();
            LatLng pos = new LatLng(incident.getLatitude(), incident.getLongitude());
            MarkerOptions marker = new MarkerOptions()
                    .position(pos)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(incidentImageAdapter.getIncidentTypeImageId(incident.getIncidentType()),75,75)))
                    .anchor(0.5f, 0.5f)
                    .title(incident.getIncidentType())
                    .snippet(incident.getStatus() + "\n"
                            + incident.getResponseDate() + "\n"
                            + incident.getBlock() + " " + incident.getAddress() + ", " + incident.getCity());

            mMap.addMarker(marker);
            builder.include(pos);
        }

        bounds = builder.build();

        if (incidentList.size() == 1) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(incidentList.get(0).getLatitude(), incidentList.get(0).getLongitude()), 14));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 500, 500, 5));
        }
    }

    public Bitmap resizeMapIcons(int iconId,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), iconId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}