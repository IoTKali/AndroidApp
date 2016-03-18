package com.iotkali.karport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindVehicleActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Button finish_button;
    private LatLng saved_location;
    private String latStr,lonStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_vehicle);
        getActionBar().hide();
        finish_button = (Button)findViewById(R.id.button_accept_save_location);
        sharedPref = getSharedPreferences("Location", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        saved_location = null;
        latStr = sharedPref.getString("Latitude", "N/A");
        lonStr = sharedPref.getString("Longitude","N/A");
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(Double.parseDouble(getString(R.string.latitude_value)),Double.parseDouble(getString(R.string.longitude_value)));
        if(!latStr.equals("N/A") && !lonStr.equals("N/A")){
            //mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latStr), Double.parseDouble(lonStr))).title(getString(R.string.default_vehicle_name)));
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latStr), Double.parseDouble(lonStr))).title(getString(R.string.default_vehicle_name)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.karport_map_pin)));
        }
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Tec"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(16).build()));

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
