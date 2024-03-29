package com.codetutor.geolocationdemos;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetutor.geolocationdemos.Utils.RequestTask;
import com.codetutor.geolocationdemos.socket.MessageSender;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;

    private static final int UPDATE_INTERVAL = 5000; // 5 seconds

    FusedLocationProviderClient locationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private Location currentLocation;

    private String locationAddress;
    private boolean isAddressRequested;
    private AddressResultReceiver addressResultReceiver;

    private static final String ADDRESS_REQUEST_KEY = "address-request";
    private static final String LOCATION_ADDRESS_KEY = "location-address";

    private TextView textViewAddress;
    private TextView textViewLocation;
    private EditText editTextSocket;
    private Button buttonGetAddress;
    private Button buttonSocket;

    private int LOCATION_PERMISSION = 100;
    MessageSender messageSender= null;


    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        textViewAddress = (TextView)findViewById(R.id.textViewAddress);
        textViewLocation = (TextView)findViewById(R.id.textViewLocation);
        editTextSocket = (EditText) findViewById(R.id.editTextSocket);

        buttonGetAddress = (Button)findViewById(R.id.buttonGetAddress);
        buttonSocket = (Button)findViewById(R.id.buttonSocket);
        //textViewLocation

        buttonGetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAddressRequested = true;
                getAddress(currentLocation);
            }
        });

        buttonSocket.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(currentLocation != null ){
                    messageSender = new MessageSender();
                    messageSender.execute(currentLocation.getLatitude() + ""+currentLocation.getLongitude());
                }else{
                    Toast.makeText(MapsActivity.this, "No current location ", Toast.LENGTH_LONG).show();
                }

            }
        });

        String locationPLaceURI="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=26.1783,91.8284&radius=1000&key=AIzaSyDjkylEyifmJ_mZG2MQL_CPmY7ezFWIRvw";
        new RequestTask().httpCall(locationPLaceURI, MapsActivity.this);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if(locationAvailability.isLocationAvailable()){
                    Log.i(TAG,"Location is available");
                }else {
                    Log.i(TAG,"Location is unavailable");
                }
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.i(TAG,"Location result is available");
            }
        };

        updateValuesFromBundle(savedInstanceState);
        startGettingLocation();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.atharv);
        int height = 120;
        int width = 80;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.atharv);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//        mMarker = googleMap.addMarker(markerOptions);

        // Add a marker in Sydney and move the camera
        textViewLocation.setText(currentLocation.getLatitude() + ""+currentLocation.getLongitude());
        LatLng currentPlace = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(currentPlace)
                .title("Atharv")
                .snippet("Thinking of finding some thing...")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
//        mMap.addMarker(new MarkerOptions().position(currentPlace).title("Marker in Sydney"));
         marker = mMap.addMarker(markerOptions);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPlace));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 15.0f));

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));


    }
    public void doChangeTitle(String title) {
        marker.setTitle(title);
    }

    private void startGettingLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            locationProviderClient.requestLocationUpdates(locationRequest,locationCallback, MapsActivity.this.getMainLooper());
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);

                }
            });

            locationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "Exception while getting the location: "+e.getMessage());
                }
            });


        }else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(MapsActivity.this, "Permission needed", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION);
            }
        }
    }

    private void stopLocationRequests(){
        locationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationRequests();
    }

    private class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode==Constants.SUCCESS_RESULT){
                locationAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                textViewAddress.setText(locationAddress);
                isAddressRequested = false;
                textViewAddress.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }else {
                locationAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                textViewAddress.setText(locationAddress);
                textViewAddress.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private void getAddress(Location location){
        if(!Geocoder.isPresent()){
            Toast.makeText(this, "Geocoder not present", Toast.LENGTH_LONG).show();
        }else {
            if(isAddressRequested){
                startAddressFetcherService();
            }
        }
    }

    private void startAddressFetcherService(){
        Intent intent = new Intent(this,AddressFetcherService.class );
        addressResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, addressResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, currentLocation);
        startService(intent);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(ADDRESS_REQUEST_KEY)) {
                isAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUEST_KEY);
            }
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                locationAddress = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                textViewAddress.setText(locationAddress);
                Log.i(TAG, "Address: "+ locationAddress);
            }
        }
    }
}