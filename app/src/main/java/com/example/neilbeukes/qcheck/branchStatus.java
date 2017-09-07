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

import com.google.gson.Gson;

public class BranchStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_status);

        final BranchInfo branchInfo = new Gson().fromJson(getIntent().getStringExtra("branchInfo"), BranchInfo.class);
        //Log.w("branchInfo", getIntent().getStringExtra("branchInfo"));

        TextView tvAdress = (TextView) findViewById(R.id.tvBranchAdress);
        TextView tvBranchMame = (TextView) findViewById(R.id.tvBranchName);
        TextView tvStatus = (TextView) findViewById(R.id.tvBranchStatus);
        TextView tvTime = (TextView) findViewById(R.id.tvBusinessHours);
        ImageView ivBranch = (ImageView) findViewById(R.id.ivBranchImage);

        switch (branchInfo.getName()){
            case "North branch":
                ivBranch.setBackgroundResource(R.drawable.northbranch);
                break;
            case "West branch":
                ivBranch.setBackgroundResource(R.drawable.westbranch);
                break;
            case "South branch":
                ivBranch.setBackgroundResource(R.drawable.southbranch);
                break;
        }

        tvAdress.setText(branchInfo.getAdress());
        tvBranchMame.setText(branchInfo.getName());
        tvStatus.setText(branchInfo.getName() + " is currently " + branchInfo.getStatus());

        Button btnViewQ = (Button) findViewById(R.id.btnViewQ);
        Button btnViewMap = (Button) findViewById(R.id.btnViewMap);

        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(BranchStatus.this, MapsActivity.class);
                i.putExtra("branch", branchInfo.getName());
                startActivity(i);
            }
        });

        btnViewQ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(BranchStatus.this, QStatus.class);
                i.putExtra("branch", branchInfo.getName());
                startActivity(i);
            }
        });



    }
}
