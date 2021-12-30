package com.codetutor.geolocationdemos.Registration;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.codetutor.geolocationdemos.R;
import com.codetutor.geolocationdemos.model.DriverRegistrationModel;
import com.google.gson.Gson;


public class DriverRegistration extends AppCompatActivity {
    private static final String TAG = DriverRegistration.class.getSimpleName();


    EditText editTextName ,editTextVehicleNumber ,editTextViewAge ,editTextViewCarryWater ,editTextEmail ,editTextConfirmEmail = null;

    Button  buttonRegistration = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextVehicleNumber = (EditText) findViewById(R.id.editTextVehicleNumber);
        editTextViewAge = (EditText) findViewById(R.id.editTextViewAge);
        editTextViewCarryWater = (EditText) findViewById(R.id.editTextViewCarryWater);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextConfirmEmail = (EditText) findViewById(R.id.editTextConfirmEmail);

        testing();

        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DriverRegistration.this,"Camera Capture ",Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(DriverRegistration.this,   DriverRegistration2.class);
                String name = editTextName.getText().toString();
                String vehicleNumber = editTextVehicleNumber.getText().toString();
                int age = Integer.parseInt(editTextViewAge.getText().toString());
                int carryWater = Integer.parseInt(editTextViewCarryWater.getText().toString());
                String email = editTextEmail.getText().toString();

                DriverRegistrationModel driverRegistrationModel = new DriverRegistrationModel();
                driverRegistrationModel.setName(name);
                driverRegistrationModel.setAge(age);
                driverRegistrationModel.setCarryWater(carryWater);
                driverRegistrationModel.setVehicleNumber(vehicleNumber);
                driverRegistrationModel.setEmail(email);

                Gson gson = new Gson();
                String json = gson.toJson(driverRegistrationModel);
                myIntent.putExtra("driverRegistrationModel", json);
                startActivity(myIntent);

            }


        });



    }

    void testing(){
        editTextName.setText("Ruhit Rai");
        editTextVehicleNumber.setText("AS 01 ED 6798");
        editTextViewAge.setText("23");
        editTextViewCarryWater.setText("500");
        editTextEmail.setText("abc@gmail.com");
        editTextConfirmEmail.setText("abc@gmail.com");

    }


}
