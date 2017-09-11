package com.example.neilbeukes.qcheck;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements MyBranchRecycleViewAdapter.ItemClickListener {

    MyBranchRecycleViewAdapter adapter;
    private FusedLocationProviderClient mFusedLocationClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    ArrayList<BranchInfo> branchArray = new ArrayList<>();
    TextView tvBranchesFound;
    RecyclerView rvBranches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvBranches = (RecyclerView) findViewById(R.id.rvBranches);

        getLocation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.itemRefresh){
            findViewById(R.id.pbRefresh).setVisibility(View.VISIBLE);
            branchArray.clear();
            getLocation();
            populateBranches();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this,BranchStatus.class);
        intent.putExtra("branchInfo", (new Gson()).toJson(adapter.getItem(position)));
        startActivity(intent);
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    public void getLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (!mLocationPermissionGranted) {
                getLocationPermission();
            }

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                saveBrancesToArray(location);
                            }
                        }
                    });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void saveBrancesToArray(Location location){
        final Branches brances = new Branches();

        brances.checkForBranches(location, getApplicationContext(),new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {

                    JSONObject obj = new JSONObject(result);
                    JSONArray arrJson = obj.getJSONArray("results");

                    for (int i = 0; i < arrJson.length(); i++) {
                        JSONObject row = arrJson.getJSONObject(i);
                        try {
                            if (row.getString("name").toLowerCase().indexOf("atm".toLowerCase()) == -1 ) {
                                branchArray.add(new BranchInfo(row.getString("name"), row.getString("vicinity"),
                                        "Normal", row.getJSONObject("opening_hours").getBoolean("open_now"),
                                        row.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                        row.getJSONObject("geometry").getJSONObject("location").getDouble("lng")));

                                Log.w("Array List" + i + " : ", "new Branch Info Recorded");
                            }
                        }catch(Exception e){
                            Log.e("Array List" + i + " : ", e.toString());
                        }

                        populateBranches();
                    }
                } catch (Throwable t) {
                    Log.e("My App", t.toString());
                }

            }
        });
    }

    public void populateBranches(){

        rvBranches.setVisibility(View.INVISIBLE);
        tvBranchesFound = (TextView) findViewById(R.id.tvBranchFound);
        tvBranchesFound.setText("Locating nearest branches...");
        rvBranches.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyBranchRecycleViewAdapter(this, branchArray);
        adapter.setClickListener(this);
        findViewById(R.id.pbRefresh).setVisibility(View.GONE);
        rvBranches.setVisibility(View.VISIBLE);
        rvBranches.setAdapter(adapter);
        tvBranchesFound.setText("Here is the Absa branches near you :");

    }
}
