package com.example.neilbeukes.qcheck;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.neilbeukes.qcheck.R.id.btnRequestTicket;

public class QueryActivity extends AppCompatActivity {

    String selectedName;
    String timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        getSupportActionBar().setTitle("");

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour >= 12)
            timeText = "Good Afternoon.";
        else
            timeText = "Good Morning.";

        TextView tvHeading = (TextView) findViewById(R.id.tvHeading);
        tvHeading.setText(timeText);

        ImageView ivQuery = (ImageView) findViewById(R.id.ivEnquire);
        ImageView ivTeller= (ImageView) findViewById(R.id.ivTeller);
        ImageView ivConsultants = (ImageView) findViewById(R.id.ivConsultant);

        ivQuery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToMain("Enquiries");
            }
        });

        ivTeller.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToMain("Tellers");
            }
        });

        ivConsultants.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToMain("Consultants");
            }
        });
    }

    public void goToMain(String selectedName){
        Intent intent = new Intent(QueryActivity.this,MainActivity.class);
        intent.putExtra("Query", selectedName);
        startActivity(intent);
    }
}
