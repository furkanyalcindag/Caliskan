package com.example.furkan.lojistikson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity {

    private EditText plaka,sifre;
    private Button btnSubmit;
    private Button btnGiris;
    //  private ProgressDialog pDialog;
    private JSONObject json;
    private int success=0;
    private HTTPURLConnection service;
    private String strname ="", strMobile ="",strAddress="";
    //Initialize webservice URL
    private String path = "http://www.sri-ako.com/insert.php";
    boolean servis = false;

    public static final String USER_NAME = "USER_NAME";

    public static final String PASSWORD = "PASSWORD";

    private static final String LOGIN_URL = "http://www.sri-ako.com/login.php";

   private String password,user;


    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor; //preferences editor nesnesi referansı .prefernces nesnesine veri ekleyip cıkarmak için




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        plaka= (EditText) findViewById(R.id.etName);
        sifre= (EditText) findViewById(R.id.etMobile);

       // btnSubmit= (Button) findViewById(R.id.btnSubmit);

        btnGiris = (Button) findViewById(R.id.button);


        //Initialize HTTPURLConnection class object
        service=new HTTPURLConnection();








        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


OnLogin();
            }
        });


    }

    private void OnLogin() {
        String username = plaka.getText().toString();
        String password = sifre.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);



        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Giriş Yapılıyor...");
        progressDialog.show();


    }




    public void startService(View view){
        Intent intent = new Intent(this,GPSTracker.class);
        startService(intent);
    }
    public void stopService(View view){
        Intent intent = new Intent(this,GPSTracker.class);
        stopService(intent);
    }

    /*private class PostDataTOServer extends AsyncTask<Void, Void, Void> {

        String response = "";
        //Create hashmap Object to send parameters to web service
        HashMap<String, String> postDataParams;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            postDataParams=new HashMap<String, String>();
            postDataParams.put("lat", "1212121");
            postDataParams.put("long", "2121212121");
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(success==1) {
                Toast.makeText(getApplicationContext(), "Employee Added successfully..!", Toast.LENGTH_LONG).show();
            }
        }
    }*/




    private void login(){
        String username = plaka.getText().toString().trim();
        String password = sifre.getText().toString().trim();
        userLogin(username,password);
    }

    private void userLogin(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    System.out.println("başarılı");
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);

                    intent.putExtra(USER_NAME,username);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
                    System.out.println("mösnsndömsndmösndösnmös");
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username,password);
    }




}
