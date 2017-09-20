package com.example.neilbeukes.qcheck;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.content.Context;

public class QStatus extends AppCompatActivity {

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qstatus);
        getSupportActionBar().setTitle(getIntent().getStringExtra("branch"));

        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.toggleButton);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked){
                    notify = true;
                    Log.w("Toggle", "Checked");
                }else
                {
                    notify = false;
                    Log.w("Toggle", "Unchecked");
                }
            }
        });

        sendNotification();

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

    public void sendNotification() {


        try
        {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle("Q's")
                            .setContentText("You are second in the Queue. Tap for more information");

            Intent resultIntent = new Intent(this, QStatus.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            int mNotificationId = 001;

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }

    }
}
