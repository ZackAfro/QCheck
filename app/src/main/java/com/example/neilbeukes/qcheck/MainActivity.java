package com.example.neilbeukes.qcheck;

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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyBranchRecycleViewAdapter.ItemClickListener {

    MyBranchRecycleViewAdapter adapter;
    private FusedLocationProviderClient mFusedLocationClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    ArrayList<BranchInfo> branchArray = new ArrayList<>();
    TextView tvBranchesFound;
    RecyclerView rvBranches;
    String selectedQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Qs");

        selectedQuery = getIntent().getStringExtra("Query");
        Log.w("Oi", selectedQuery);

        tvBranchesFound =  (TextView) findViewById(R.id.tvBranchFound);
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
        intent.putExtra("Query", selectedQuery);
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

    //global
    int cs = 0;
    public void calculateTravelDistance(Location location){
        for (int counter = 0; counter < branchArray.size(); counter++) {
            final VolleyClient volleyClient = new VolleyClient();
            tvBranchesFound.setText("Locating nearest branches...");
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + location.getLatitude() +
                    "," + location.getLongitude() + "&destinations=" + branchArray.get(counter).getGeoLat() + "," + branchArray.get(counter).getGeoLng()
                    + "&key=AIzaSyCTuW4GcvCWRXRCS1wzrYUhzKJOu4ru-jg";
            final int fCounter = counter;
            volleyClient.sendVolley(url, getApplicationContext(), new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject obj = new JSONObject(result).getJSONArray("rows").getJSONObject(0)
                                .getJSONArray("elements").getJSONObject(0);
                        //Saving distance
                        branchArray.get(fCounter).setDistanceText(obj.getJSONObject("distance").getString("text"));
                        branchArray.get(fCounter).setDistanceKm(obj.getJSONObject("distance").getInt("value"));
                        //saving travel time
                        branchArray.get(fCounter).setTimeString(obj.getJSONObject("duration").getString("text"));
                        branchArray.get(fCounter).setTimeSeconds(obj.getJSONObject("duration").getInt("value"));
                        cs++;
                        if (cs==branchArray.size()) {
                            populateBranches();
                        }

                    } catch (Throwable t) {
                        Log.e("My App", t.toString());
                    }
                }
            });
        }
    }

    public void saveBrancesToArray(final Location location){
        final VolleyClient volleyClient = new VolleyClient();
        tvBranchesFound.setText("Locating nearest branches...");
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.getLatitude() +
                "," + location.getLongitude()+ "&radius=6000&type=bank&keyword=absa%20branch&key=AIzaSyCTuW4GcvCWRXRCS1wzrYUhzKJOu4ru-jg";
        volleyClient.sendVolley(url, getApplicationContext(),new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {

                    JSONObject obj = new JSONObject(result);
                    JSONArray arrJson = obj.getJSONArray("results");
                    for (int i = 0; i < arrJson.length(); i++) {
                        JSONObject row = arrJson.getJSONObject(i);
                        try {
                            if ((row.getString("name").toLowerCase().indexOf("atm".toLowerCase()) == -1) && (row.getString("name").toLowerCase().indexOf("bank".toLowerCase()) == -1)) {
                                branchArray.add(new BranchInfo(row.getString("name"), row.getString("vicinity"),
                                        "", row.getJSONObject("opening_hours").getBoolean("open_now"),
                                        row.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                        row.getJSONObject("geometry").getJSONObject("location").getDouble("lng"), row.getString("id")));
                            }
                        }catch(Exception e){
                        }
                    }
                } catch (Throwable t) {
                    Log.e("My App", t.toString());
                }
                calculateTravelDistance(location);
            }
        });
    }

    public void populateBranches(){
        rvBranches.setVisibility(View.INVISIBLE);
        tvBranchesFound = (TextView) findViewById(R.id.tvBranchFound);
        rvBranches.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyBranchRecycleViewAdapter(this, branchArray);
        adapter.setClickListener(this);
        findViewById(R.id.pbRefresh).setVisibility(View.GONE);
        rvBranches.setVisibility(View.VISIBLE);
        rvBranches.setAdapter(adapter);
        tvBranchesFound.setText("Select a branch:");

    }
}
