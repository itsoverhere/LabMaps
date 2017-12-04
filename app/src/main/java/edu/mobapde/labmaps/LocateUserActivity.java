package edu.mobapde.labmaps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class LocateUserActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int REQUEST_LOCATION = 0;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private Marker userMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_user);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // this part is just to view the other activity
        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MapsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        client = LocationServices.getFusedLocationProviderClient(getBaseContext());
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

        drawGokongweiBuilding();
        getContinuousLocationUpdates();
    }

    private void drawGokongweiBuilding() {
        mMap.addPolygon(new PolygonOptions().add(
                new LatLng(14.566268, 120.992677),
                new LatLng(14.566488, 120.993202),
                new LatLng(14.566244, 120.993312),
                new LatLng(14.566054, 120.992788),
                new LatLng(14.566268, 120.992677)
               ).fillColor(Color.argb(150, 150, 150, 150))
                .strokeWidth(3f)
        );
    }

    public boolean checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else{
            return true;
        }
    }

    public void getContinuousLocationUpdates() {
        if(checkLocationPermission()) {
            LocationRequest locationRequest
                    = LocationRequest.create()
                    .setInterval(1000)
                    .setFastestInterval(1000);
            client.requestLocationUpdates(locationRequest, locationCallback, null);
        }else{
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION
            );
        }
    }

    LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location currentLocation = locationResult.getLastLocation();
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            if(userMarker!=null) {
                userMarker.remove();
            }
            userMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f));
            mMap.setBuildingsEnabled(true);

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.i("MapsActivity", "User clicked on marker at " + marker.getPosition().toString());
                    return false;
                }
            });
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getContinuousLocationUpdates();
            }
        }
    }
}
