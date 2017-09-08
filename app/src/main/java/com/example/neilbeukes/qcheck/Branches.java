package com.example.neilbeukes.qcheck;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Branches {

    public void checkForBranches(Location mLastKnownLocation, Context context, final VolleyCallback callback){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mLastKnownLocation.getLatitude() +
                "," + mLastKnownLocation.getLongitude()+ "&radius=6000&type=bank&keyword=absa%20branch&key=AIzaSyCTuW4GcvCWRXRCS1wzrYUhzKJOu4ru-jg";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("Volley Response", "Response Recieved");
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("Volley error response", "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
