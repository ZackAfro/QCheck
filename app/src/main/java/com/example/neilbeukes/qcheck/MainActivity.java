package com.example.neilbeukes.qcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MyBranchRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button btn = (Button)findViewById(R.id.btnMaps);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MapsActivity.class));
//            }
//        });

        ArrayList<BranchInfo> branches = new ArrayList<>();
        branches.add(new BranchInfo("North branch", "12 Commissioner street", "Busy"));
        branches.add(new BranchInfo("West branch", "77 Main street", "Quiet"));
        branches.add(new BranchInfo("South branch", "42 Orange street", "Normal"));

        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvBranches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyBranchRecycleViewAdapter(this, branches);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
