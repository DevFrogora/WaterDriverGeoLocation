package com.codetutor.geolocationdemos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter   implements GoogleMap.InfoWindowAdapter  {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.snippet);
        TextView tvcontact= (TextView) view.findViewById(R.id.contact);
        TextView tvtext= (TextView) view.findViewById(R.id.test);
        ImageButton btnAdd = (ImageButton) view.findViewById(R.id.addBtn);


        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());
        tvcontact.setText("Contact : +919954997214");


        return view;
    }

    public void click(View v) {
        Toast.makeText(context, "clickMe", Toast.LENGTH_SHORT).show();
    }
}