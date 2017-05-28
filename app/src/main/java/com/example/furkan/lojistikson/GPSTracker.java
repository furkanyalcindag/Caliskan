package com.example.furkan.lojistikson;

/**
 * Created by furkan on 26.03.2017.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

import android.os.Handler;


import org.json.JSONException;
import org.json.JSONObject;


public class GPSTracker extends Service implements LocationListener {


    private ProgressDialog pDialog;
    private JSONObject json;
    private int success = 0;
    private HTTPURLConnection service;
    private String strname = "", strMobile = "", strAddress = "";
    //Initialize webservice URL
    private String path = "http://www.sri-ako.com/insert.php";

    private double longs;
    private double lat;
    private  TelephonyManager telephonyManager;
    private String imeiNumber=gpsActivity.getIds() ;



    //private static String url_create_product = "http://www.sri-ako.com/insert.php";


    // temporary string to show the parsed response
    private Context mContext;
    private String jsonResponse;
    GPSTracker gpsTracker;
    public double x;
    public double y;
    Handler handler = new Handler() {

        public void publish(LogRecord record) {

        }


        public void flush() {

        }


        public void close() throws SecurityException {

        }
    };

    public GPSTracker() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        gpsTracker = new GPSTracker(mContext);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                service = new HTTPURLConnection();
                // new PostDataTOServer.execute();
                track(gpsTracker);

                handler.postDelayed(this, 30000);
            }
        };
        handler.post(runnable);
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "service destroyed", Toast.LENGTH_LONG).show();
    }


    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }


    public void insert() {
        System.out.println("Merhaba Buradayım");
    }

    public void track(GPSTracker gps) {
        System.out.println("Merhaba Buradayım");
        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            System.out.println("Merhaba Buradayım");
            // new PostDataTOServer().execute();

            lat = gps.getLatitude();
            longs = gps.getLongitude();
          //  telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
           // imeiNumber = telephonyManager.getDeviceId().toString();

            new PostDataTOServer().execute();
            // \n is for new line

            Toast.makeText(getApplicationContext(), "Your Location is -  \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            insert();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
            Toast.makeText(getApplicationContext(), "Lütfen GPS'inizi açın", Toast.LENGTH_LONG).show();
            System.out.println("gps açık değil");
        }


    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {


                    try {

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {
                        //dialogGPS(this.getContext()); // lets the user know there is a problem with the gps
                    }


                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                    try {


                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    System.out.println(latitude + " asas" + longitude);
                                }
                            }
                            else{
                                System.out.println("gps kapalı");
                            }
                        }

                    }
                    catch (SecurityException e) {
                        //dialogGPS(this.getContext()); // lets the user know there is a problem with the gps
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class PostDataTOServer extends AsyncTask<Void, Void, Void> {

        String response = "";
        //Create hashmap Object to send parameters to web service
        HashMap<String, String> postDataParams;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //    pDialog.setMessage("Please wait...");
            //  pDialog.setCancelable(false);
            // pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            postDataParams=new HashMap<String, String>();
            postDataParams.put("lat", String.valueOf(lat));
            postDataParams.put("long", String.valueOf(longs));
            postDataParams.put("imei",imeiNumber);
            //  postDataParams.put("address", strAddress);
            //Call ServerData() method to call webservice and store result in response
            response= service.ServerData(path,postDataParams);
            try {
                json = new JSONObject(response);
                //Get Values from JSONobject
                System.out.println("success=" + json.get("success"));
                success = json.getInt("success");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           /* if (pDialog.isShowing())
                pDialog.dismiss();*/
            if(success==1) {
                Toast.makeText(getApplicationContext(), "Employee Added successfully..!", Toast.LENGTH_LONG).show();
            }
        }
    }




}

