package com.example.neilbeukes.qcheck;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QueryActivity extends AppCompatActivity {

    String selectedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        final Spinner dropdown = (Spinner)findViewById(R.id.enquiriesSpinnner);
        String[] items = new String[]{"General Enquiries", "Tellers", "Consultants"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Button button= (Button) findViewById(R.id.btnContinue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain(selectedName);
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

//                ((TextView) dropdown.getChildAt(0)).setTextColor(Color.WHITE);
//                ((TextView) dropdown.getChildAt(0)).setTextSize(14);

                switch (position){
                    case 0:
                        selectedName = "General Enquiries";
                        break;
                    case 1:
                        selectedName = "Consultants";
                        break;
                    case 2:
                        selectedName = "Tellers";
                        break;
                    default:
                        selectedName = "No value selected";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void goToMain(String selectedName){
        Intent intent = new Intent(QueryActivity.this,MainActivity.class);
        intent.putExtra("Query", selectedName);
        startActivity(intent);
    }
}
