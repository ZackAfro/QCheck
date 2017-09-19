package com.example.neilbeukes.qcheck;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class QStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qstatus);
        getSupportActionBar().setTitle(getIntent().getStringExtra("branch"));

        Button btnCancelTicket = (Button) findViewById(R.id.btnCancel);

        btnCancelTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                throwCancelDialog();
            }
        });
    }
    public void throwCancelDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Ticket cancel confirmation")
                .setMessage("Are you sure you want to Delete this ticket?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

}
