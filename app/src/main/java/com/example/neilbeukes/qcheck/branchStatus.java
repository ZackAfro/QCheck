package com.example.neilbeukes.qcheck;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.data;
import static android.R.attr.defaultValue;
import static android.R.attr.value;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static java.security.AccessController.getContext;

public class BranchStatus extends AppCompatActivity implements OnMapReadyCallback {

        BranchInfo branchInfo;
        String selectedQuery;
        int ticketId;
        int ticketNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_status);

        branchInfo = new Gson().fromJson(getIntent().getStringExtra("branchInfo"), BranchInfo.class);

        getSupportActionBar().setTitle("");
        selectedQuery = getIntent().getStringExtra("Query");
        TextView tvBranchEnq = (TextView) findViewById(R.id.tvBranchEnq);
        TextView tvTime = (TextView) findViewById(R.id.tvBusinessHours);
        TextView tvQtime = (TextView) findViewById(R.id.tvQTime);
        TextView tvQLength = (TextView) findViewById(R.id.tvQLength);
        MapView mapView = (MapView) findViewById(R.id.map_view);
        Button btnRequestTicket = (Button) findViewById(R.id.btnRequestTicket);

        setOpen(branchInfo.isOpen(), tvTime);
        tvBranchEnq.setText(selectedQuery);
        tvQLength.setText(Html.fromHtml("Number of people in the Queue: <b>" + branchInfo.getQueueLength() + "</b>"));
        tvQtime.setText(Html.fromHtml("Estimated waiting time: <b>" + branchInfo.getQueueLength() * 5 + " mins</b>"));

        btnRequestTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                throwAlertDialog();
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
        map.setMapType(MAP_TYPE_HYBRID);
    }

    public void setOpen(boolean isOpen, TextView tv){
        if (isOpen)
            tv.setText(Html.fromHtml(branchInfo.getName() +  " is Currently : <b>Open</b>"));
        else
            tv.setText(Html.fromHtml(branchInfo.getName() + " is Currently : <b>Closed</b>"));
    }

    public void throwAlertDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Ticket request confirmation")
                .setMessage("Are you sure you would like to book a ticket in the " + selectedQuery + " Queue at " + branchInfo.getName() + " ?");
                builder.setPositiveButton("Request Ticket", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                            final VolleyClient volleyClient = new VolleyClient();
                            String url ="http://qshack-001-site1.htempurl.com/WebService/GetQueue.asmx/CreateTicket?" +
                                    "GoogleId=" + branchInfo.getId() + "&FunctionType=" + selectedQuery;
                            volleyClient.sendVolley(url, getApplicationContext(),new VolleyCallback(){
                                @Override
                                public void onSuccess(String result) {
                                    try {
                                        String jsonstring = result.substring(result.indexOf("{"), result.indexOf("}") + 1);
                                        Log.w("Returned", jsonstring);
                                        JSONObject obj = new JSONObject(jsonstring);
                                        ticketId = obj.getInt("value");
                                        ticketNumber = obj.getInt("data");


                                        SharedPreferences.Editor editor = getSharedPreferences("TICKET", MODE_PRIVATE).edit();
                                        editor.putInt("id", ticketId);
                                        editor.putInt("number", ticketNumber);
                                        editor.apply();

                                        Intent i = new Intent();
                                        i.setClass(BranchStatus.this, QStatus.class);
                                        i.putExtra("branch", branchInfo.getName());
                                        //i.putExtra("cellNum", input.getText().toString());
                                        startActivity(i);

                                        Log.w("Created", ticketId + "" + ticketNumber);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                         });

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_menu_info_details)
                .show();
    }
}
