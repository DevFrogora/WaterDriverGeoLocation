package com.codetutor.geolocationdemos.Registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.codetutor.geolocationdemos.R;
import com.codetutor.geolocationdemos.model.DriverRegistrationModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DriverRegistration2 extends AppCompatActivity {

    private static final String TAG = DriverRegistration2.class.getSimpleName();

    ImageView imageView = null;
    Button buttonCameraCapture  ,buttonCompleteRegistration= null;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int REQUEST_IMAGE_CAPTURE =103;
    Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration2);

        bundle = getIntent().getExtras();

        imageView = (ImageView) findViewById(R.id.imageView);
        buttonCameraCapture = (Button) findViewById(R.id.buttonCameraCapture);
        buttonCompleteRegistration =(Button) findViewById(R.id.buttonCompleteRegistration);

        buttonCameraCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DriverRegistration2.this,"Camera Capture ",Toast.LENGTH_SHORT).show();
                askCameraPermission();
            }


        });

        buttonCompleteRegistration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

//                System.out.println(gson.toJson(driverRegistrationModel));
                //gson.toJsonTree(driverRegistrationModel)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        DriverRegistrationModel driverRegistrationModel = gson.fromJson(getIntent().getStringExtra("driverRegistrationModel"), DriverRegistrationModel.class);
                        driverRegistrationModel.setImageBase64(base64String);
                        Log.i(TAG,gson.toJson(driverRegistrationModel));
                        new RegistrationRequest().createRequest(driverRegistrationModel,DriverRegistration2.this);
                    }
                }).start();

//                writeFileExternalStorage(gson.toJson(driverRegistrationModel));

            }
        });
    }

    private void createJsonFile(String jsonString) throws IOException {
        File file = getFilesDir();
        FileOutputStream fileOutputStream =openFileOutput("driverRegistration.txt", Context.MODE_PRIVATE);
        fileOutputStream.write(jsonString.getBytes());
        Toast.makeText(DriverRegistration2.this,file.toString(),Toast.LENGTH_SHORT).show();
        Log.i(TAG,file.toString());
    }

    public void writeFileExternalStorage(String jsonString) {

        //Text of the Document
        String textToWrite = jsonString;

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

            //If it isn't mounted - we can't write into it.
            return;
        }

        //Create a new file that points to the root directory, with the given name:
        File file = new File(getExternalFilesDir(null), "driverRegistration.txt");

        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, false);

            outputStream.write(textToWrite.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG,file.getAbsolutePath());
    }


    public String imageFileToByte(File file){

        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 50, baos);
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(DriverRegistration2.this,"Camera Permission is Required to Use Camera ",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera() {
        Toast.makeText(DriverRegistration2.this,"Camera Open Request ",Toast.LENGTH_SHORT).show();
        Intent camera = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);

    }

    String base64String = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
//               Bitmap image  = (Bitmap) data.getExtras().get("data");
                File image = new File(currentPhotoPath);
                 base64String = imageFileToByte(image);
                imageView.setImageURI(Uri.fromFile(image));
                Log.i(TAG,currentPhotoPath);
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}
