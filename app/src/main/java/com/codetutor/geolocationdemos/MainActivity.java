package com.codetutor.geolocationdemos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetutor.geolocationdemos.Registration.DriverRegistration;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import 	android.location.LocationManager;
import android.app.AlertDialog;
import android.content.DialogInterface;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    Button buttonLogin, buttonRegistration;
    EditText editTextUsername, editTextPassword =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonRegistration = (Button)findViewById(R.id.buttonRegistration);
        editTextUsername =(EditText) findViewById(R.id.editTextUsername);
        editTextPassword =(EditText) findViewById(R.id.editTextPassword);

        Log.d(TAG, "Opencv "+OpenCVLoader.initDebug());


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextUsername !=null && editTextPassword !=null){
                    if(editTextPassword.getText().toString().equals("") || editTextUsername.getText().toString().equals("")){
                        Toast.makeText(MainActivity.this,"fill the  U/P",Toast.LENGTH_LONG).show();
                    }else{
                        if(editTextPassword.getText().toString().equals("password") && editTextUsername.getText().toString().equals("username")){
                            Toast.makeText(MainActivity.this,"correct password",Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(MainActivity.this,   MapsActivity.class);
                            startActivity(myIntent);
                        }else{
                            Toast.makeText(MainActivity.this,"wrong password",Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });

        buttonRegistration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,   DriverRegistration.class);
                startActivity(myIntent);
            }
        });




    }




}
