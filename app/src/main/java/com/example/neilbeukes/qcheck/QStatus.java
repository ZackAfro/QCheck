package com.example.neilbeukes.qcheck;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import android.widget.TextView;

import java.util.Date;

import static android.R.attr.minHeight;

public class QStatus extends AppCompatActivity {

    boolean notify = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qstatus);
        getSupportActionBar().setTitle("");

        SharedPreferences prefs = getSharedPreferences("TICKET", MODE_PRIVATE);
        int ticketName = prefs.getInt("number", 0);
        int ticketId = prefs.getInt("id", 0);
        Log.w("TicketInfo", "ID: " + ticketId + ",Number: " + ticketName);

        TextView tvTicketHeader = (TextView) findViewById(R.id.tvTicketHeader);
        tvTicketHeader.setText("Ticket Number: " + ticketName);

        TextView tvTicket = (TextView) findViewById(R.id.tvTicketInfo);
        tvTicket.setText("Your Position in the Queue: ");

        TextView tvPosition = (TextView) findViewById(R.id.tvQPosition);

        startChecks(tvPosition, ticketId);

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

        Button btnCancelTicket = (Button) findViewById(R.id.btnCancel);

        btnCancelTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                throwCancelDialog();
            }
        });

        final CardView cvBringWith = (CardView) findViewById(R.id.cvBringWith);
    }g

    public void updatePosition(final TextView tvposition, final int id){
            final VolleyClient volleyClient = new VolleyClient();
                String url = "http://qshack-001-site1.htempurl.com/WebService/GetQueue.asmx/GetPositionInQueue?ticketId=" + id;
                volleyClient.sendVolley(url, getApplicationContext(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            int position = Character.getNumericValue(result.charAt(result.length() - 7));
                            tvposition.setText(position + "");
                            if ((position == 2) && (notify)) {
                            sendNotification();
                            }
                        } catch (Throwable t) {
                            Log.e("My App", t.toString());
                        }
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

            try {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.logo_bigger)
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
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    Handler handler = new Handler();
    int delay = 3000;
    public void startChecks(final TextView tv, final int ticketId){

        handler.postDelayed(new Runnable(){
            public void run(){
                updatePosition(tv, ticketId);
                handler.postDelayed(this, delay);
            }
        }, delay);

    }
}
