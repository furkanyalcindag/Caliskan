package com.example.furkan.lojistikson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class gpsActivity extends AppCompatActivity {
    boolean servis = false;
   private static String ids;
    Button gps ;

    public static String getIds() {
        return ids;
    }

    public static void setIds(String ids) {
        gpsActivity.ids = ids;
    }

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        gps = (Button) findViewById(R.id.buttonGps);
        setIds(getIntent().getStringExtra("id"));
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!servis){
                    Intent i = new Intent(gpsActivity.this,GPSTracker.class);
                    i.putExtra("id",getIntent().getStringExtra("id"));
                    startService(i);

                    System.out.println("sdsdsdsdssds");
                    servis = true;
                }
                else{
                    Intent i = new Intent(gpsActivity.this,GPSTracker.class);
                    stopService(i);
                    servis = false;
                }


            }
        });

    }
}
