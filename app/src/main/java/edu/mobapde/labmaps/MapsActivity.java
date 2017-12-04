package edu.mobapde.labmaps;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RecyclerView rvTravel;
    private TravelAdapter travelAdapter;
    private ArrayList<Travel> travelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // initialize RecyclerView
        rvTravel = findViewById(R.id.rv_travel);

        // initialize list of items in the "journey"
        travelArrayList = new ArrayList<Travel>();
        travelArrayList.add(new Travel("Andrew", 14.567069, 120.992662));
        travelArrayList.add(new Travel("Gokongwei", 14.566268, 120.992997));
        travelArrayList.add(new Travel("Henry Sy Sr. Hall", 14.564943, 120.993120));
        travelArrayList.add(new Travel("St. La Salle Hall", 14.564226, 120.993925));

        // create the adapter to populate the recyclerview
        travelAdapter = new TravelAdapter(travelArrayList, new TravelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Travel travel) {
                // when the user clicks on an item in the adapter, it will focus on that marker
                if(mMap != null){
                    LatLng current = new LatLng(travel.getLatitude(), travel.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 18.5f));
                }
            }
        });

        rvTravel.setAdapter(travelAdapter);
        rvTravel.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));

        mapFragment.getMapAsync(this);
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

        PolylineOptions polylineOptions = new PolylineOptions();
        for(Travel travel : travelArrayList){
            // create a marker for each item
            LatLng current = new LatLng(travel.getLatitude(), travel.getLongitude());
            mMap.addMarker(new MarkerOptions().position(current).title(travel.getPlace()));

            // connect the line to each travel/place's latlng
            polylineOptions.add(current);
        }
        // draw the line
        mMap.addPolyline(polylineOptions.color(Color.parseColor("#333333")));

        // move the camera to the first marker
        Travel firstTravel = travelArrayList.get(0);
        if(firstTravel!=null){
            LatLng first = new LatLng(firstTravel.getLatitude(), firstTravel.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(first, 18f));
        }

    }
}
