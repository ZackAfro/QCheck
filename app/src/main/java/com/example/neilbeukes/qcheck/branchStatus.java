package com.example.neilbeukes.qcheck;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;

public class BranchStatus extends AppCompatActivity implements OnMapReadyCallback {

        BranchInfo branchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_status);

        branchInfo = new Gson().fromJson(getIntent().getStringExtra("branchInfo"), BranchInfo.class);

        getSupportActionBar().setTitle(branchInfo.getName());

        TextView tvStatus = (TextView) findViewById(R.id.tvBranchStatus);
        TextView tvTime = (TextView) findViewById(R.id.tvBusinessHours);
        setOpen(branchInfo.isOpen(), tvTime);
        MapView mapView = (MapView) findViewById(R.id.map_view);

        tvStatus.setText(branchInfo.getName() + " is currently " + branchInfo.getStatus());

        Button btnViewQ = (Button) findViewById(R.id.btnViewQ);

        btnViewQ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(BranchStatus.this, QStatus.class);
                i.putExtra("branch", branchInfo.getName());
                startActivity(i);
            }
        });

        if (mapView != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map){
        MapsInitializer.initialize(getApplicationContext());
        map.addMarker(new MarkerOptions()
                .position(new LatLng(branchInfo.getGeoLat(),branchInfo.getGeoLng()))
                .title(branchInfo.getName()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(branchInfo.getGeoLat(),
                        branchInfo.getGeoLng()),16));
        map.setMapType(MAP_TYPE_SATELLITE);
    }

    public void setOpen(boolean isOpen, TextView tv){
        if (isOpen)
            tv.setText("This Branch is Currently : Open");
        else
            tv.setText("This Branch is Currently : Closed");
    }
}
