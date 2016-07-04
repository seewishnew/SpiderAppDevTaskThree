package com.example.vishnu.spidertaskthree;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    public static final int INTERNET_PERMISSION_REQUEST_CODE = 1;
    public static final String LOG_TAG = "MainActivity";
    private boolean isSearchOpen = false;

    private boolean permissionGiven = true;
    public static final String permissions[] = {
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkPermissions();

        if(permissionGiven){

            Log.d(LOG_TAG, "Permission was given");

            OMDbInterfaceService omDbInterfaceService = new OMDbInterfaceService();

            omDbInterfaceService.getData("Frozen");

        }

        Log.d(LOG_TAG, "Permission was not given");

    }

    private void checkPermissions() {
        //if(Build.VERSION.SDK_INT>=23)
        {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                    PackageManager.PERMISSION_GRANTED) {

                permissionGiven = false;

                ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        INTERNET_PERMISSION_REQUEST_CODE
                );

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == INTERNET_PERMISSION_REQUEST_CODE){
            if(grantResults.length>0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                permissionGiven = false;



                    new AlertDialog.Builder(this)
                            .setTitle("Allow app to use internet?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("You need to allow this app to access internet" +
                                    " to use this app")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exitApp();
                                }
                            })
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(
                                            MainActivity.this,
                                            permissions,
                                            INTERNET_PERMISSION_REQUEST_CODE

                                    );
                                }
                            });



            }

            else
                permissionGiven = true;
        }

    }

    public void exitApp(){
        finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.action_search){

            if(isSearchOpen){



            }

        }

        return super.onOptionsItemSelected(item);
    }
}
