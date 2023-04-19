package com.example.whereruapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.whereruapp.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    ArrayList<LatLng> markerCoords = new ArrayList<LatLng>();
    ArrayList<Integer> markerIds = new ArrayList<Integer>();
    Double userlatitude = 0.0;
    Double userlongitude = 0.0;
    String username;
    Integer radius;
    int zoom = 21;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        radius = extras.getInt("radius");
        userlatitude = extras.getDouble("latitude");
        userlongitude = extras.getDouble("longitude");
        if(radius == 0){
            //if coming from login screen default radius is 50 meters
            radius = 50;
            userlatitude = 0.0;
            userlongitude = 0.0;
        }
        System.out.println("\n\n\nlat"+userlatitude+"lon"+userlongitude);

        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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

        //get nearby posts from
        LatLng userlocation = new LatLng(userlatitude, userlongitude);
        markerCoords.add(userlocation);
        markerIds.add(markerIds.size()+1);

        mMap.addMarker(new MarkerOptions().position(userlocation).title("0").icon(BitmapFromVector(getApplicationContext(), R.drawable.baseline_person_pin_circle_24)));

        getPosts(userlatitude,userlongitude,radius);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(userlocation));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userlocation)
                .zoom(zoom)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                int markerid = Integer.parseInt(m.getTitle());
                if (markerid != 0) {
                    //go to a post view that shows this posts data
                    Intent intent = new Intent(MapsActivity.this, ExpandedPostActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("radius", radius);
                    intent.putExtra("latitude", userlatitude);
                    intent.putExtra("longitude", userlongitude);
                    intent.putExtra("postid", markerid);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public void getPosts(Double latitude, Double longitude, int radius){
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);

        HashMap<String,Object> args = new HashMap<String,Object>();
        args.put("latitude",latitude);
        args.put("longitude",longitude);
        args.put("radius",radius);

        Call<List<RetroPost>> call = service.getNearby(args);
        call.enqueue(new Callback<List<RetroPost>>() {
            @Override
            public void onResponse(Call<List<RetroPost>> call, Response<List<RetroPost>> response) {
                if(response.isSuccessful()) {
                    List<RetroPost> nearbyPosts = response.body();
                    for (int x = 0; x < nearbyPosts.size() ; x++) {
                        addPostMarkers(nearbyPosts.get(x).id,nearbyPosts.get(x).latitude,nearbyPosts.get(x).longitude);
                    }
                }
                else {
                    try {
                        System.out.println(response.errorBody().string());
                    }
                    catch(Exception e){}
                }
            }
            @Override
            public void onFailure(Call<List<RetroPost>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void addPostMarkers(int id,double latitude, double longitude){
        LatLng postlocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(postlocation).title(""+id));
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            //userlatitude = location.getLatitude();
                            //userlongitude = location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
    public void scroll(View view){
        Intent intent = new Intent(MapsActivity.this, ScrollingActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("radius",radius);
        intent.putExtra("latitude",userlatitude);
        intent.putExtra("longitude",userlongitude);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    private BitmapDescriptor
    BitmapFromVector(Context context, int vectorResId)
    {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(
                context, vectorResId);

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable.setBounds(
                0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void zoomIn(View v){
        LatLng userlocation = new LatLng(userlatitude, userlongitude);
        if (zoom < 21){
            zoom+=2;
        }
        if (zoom >21){
            zoom = 21;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userlocation)
                .zoom(zoom)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void zoomOut(View v){
        LatLng userlocation = new LatLng(userlatitude, userlongitude);
        if (zoom > 2){
            zoom-=2;
        }
        if (zoom <2){
            zoom = 2;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userlocation)
                .zoom(zoom)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
