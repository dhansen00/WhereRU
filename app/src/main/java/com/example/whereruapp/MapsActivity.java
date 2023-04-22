package com.example.whereruapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private int PERMISSION_ID = 44;
    public FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    //hard coded because getting location data is not easily testable
    public Double userlatitude = 42.730857718961154;
    public Double userlongitude = -73.68255985949537;
    public String username;
    public Integer radius;
    int zoom = 15;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the variables passed in from the previous screen
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        this.username = extras.getString("username");
        this.radius = extras.getInt("radius");
        if(0 == this.radius){
            //if coming from login screen default radius is 50 meters
            this.radius = 50;
            //hard coded because getting location data is not easily testable
            this.userlatitude = 42.730857718961154;
            this.userlongitude = -73.68255985949537;
        }

        //Create the map
        super.onCreate(savedInstanceState);
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        this.getLastLocation();

        LayoutInflater layoutInflater = this.getLayoutInflater();
        this.binding = ActivityMapsBinding.inflate(layoutInflater);
        RelativeLayout root = this.binding.getRoot();
        this.setContentView(root);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
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
        this.mMap = googleMap;

        //get the current location
        this.getLastLocation();
        LatLng userlocation = new LatLng(this.userlatitude, this.userlongitude);

        //add a marker that shows the users current location
        MarkerOptions position = new MarkerOptions().position(userlocation);
        MarkerOptions title = position.title("0");
        Context applicationContext = this.getApplicationContext();
        BitmapDescriptor iconDescriptor = this.BitmapFromVector(applicationContext, R.drawable.baseline_person_pin_circle_24);
        MarkerOptions icon = title.icon(iconDescriptor);
        this.mMap.addMarker(icon);

        this.getPosts(this.userlatitude, this.userlongitude, this.radius);

        //move the camera to focus on the current post
        CameraUpdate update = CameraUpdateFactory.newLatLng(userlocation);
        this.mMap.moveCamera(update);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userlocation)
                .zoom(zoom)
                .bearing(0)
                .tilt(45)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        this.mMap.animateCamera(cameraUpdate);

        //when clicking on a post go to an expanded post
        this.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int markerid = Integer.parseInt(marker.getTitle());
                if (0 != markerid) {
                    //go to a post view that shows this posts data
                    Intent intent = new Intent(MapsActivity.this, ExpandedPostActivity.class);
                    intent.putExtra("username", MapsActivity.this.username);
                    intent.putExtra("radius", MapsActivity.this.radius);
                    intent.putExtra("latitude", MapsActivity.this.userlatitude);
                    intent.putExtra("longitude", MapsActivity.this.userlongitude);
                    intent.putExtra("postid", markerid);
                    MapsActivity.this.startActivity(intent);
                }
                return true;
            }
        });
    }

    /**
     * Gets the posts that are near the users location
     * @param latitude  the current latitude to search from
     * @param longitude the current longitude to search from
     * @param r    the current radius to search
     */
    private void getPosts(Double latitude, Double longitude, int r){
        //set up the args for the database query
        HashMap<String,Object> args = new HashMap<String,Object>();
        args.put("latitude",latitude);
        args.put("longitude",longitude);
        args.put("radius",r);

        //call to the db for posts
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<List<RetroPost>> call = service.getNearby(args);
        call.enqueue(new Callback<List<RetroPost>>() {
            @Override
            public void onResponse(Call<List<RetroPost>> call, Response<List<RetroPost>> response) {
                if(response.isSuccessful()) {
                    //upon receipt of the response from the database add the posts to the display
                    List<RetroPost> nearbyPosts = response.body();
                    for (int x = 0; x < nearbyPosts.size() ; x++) {
                        RetroPost retroPost = nearbyPosts.get(x);
                        MapsActivity.this.addPostMarkers(retroPost.id, retroPost.latitude, retroPost.longitude);
                    }
                }
                else {
                    throw new EmptyStackException();
                }
            }
            @Override
            public void onFailure(Call<List<RetroPost>> call, Throwable t) {
                throw new EmptyStackException();
            }
        });
    }

    /**
     * Adds a post as a marker on the map
     * @param id            the id of the post
     * @param latitude      the latitude of the post
     * @param longitude     the longitude of the post
     */
    private void addPostMarkers(int id,double latitude, double longitude){
        LatLng postlocation = new LatLng(latitude, longitude);
        MarkerOptions position = new MarkerOptions().position(postlocation);
        String value = String.valueOf(id);
        MarkerOptions title = position.title(value);
        this.mMap.addMarker(title);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (this.checkPermissions()) {
            // check if location is enabled
            if (this.isLocationEnabled()) {
                // getting last
                // location from
                // FusedLocationClient
                // object
                this.mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            MapsActivity.this.requestNewLocationData();
                        } else {
                            //this is where you would set the user location using gps
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
            this.requestPermissions();
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
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
                this.getLastLocation();
            }
        }
    }

    /**
     * Sends the user to the scroll view
     * @param view view of the scroll view button
     */
    public void scroll(View view){
        Intent intent = new Intent(MapsActivity.this, ScrollingActivity.class);
        intent.putExtra("username", this.username);
        intent.putExtra("radius", this.radius);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (this.checkPermissions()) {
            this.getLastLocation();
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

    /**
     * Zooms in the view of the user around their location by a unit of 2
     * @param view the view of the zoom in button
     */
    public void zoomIn(View view){
        LatLng userlocation = new LatLng(this.userlatitude, this.userlongitude);
        if (21 > this.zoom){
            this.zoom +=2;
        }
        if (21 < this.zoom){
            this.zoom = 21;
        }
        //move the camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userlocation)
                .zoom(this.zoom)
                .bearing((float) 0)
                .tilt(45.0F)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        this.mMap.animateCamera(cameraUpdate);
    }

    /**
     * Zooms out the view of the user around their location by a unit of 2
     * @param view
     */
    public void zoomOut(View view){
        LatLng userlocation = new LatLng(this.userlatitude, this.userlongitude);
        if (2 < this.zoom){
            this.zoom -=2;
        }
        if (2 > this.zoom){
            this.zoom = 2;
        }
        //move the camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userlocation)
                .zoom(this.zoom)
                .bearing((float) 0)
                .tilt(45.0F)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        this.mMap.animateCamera(cameraUpdate);
    }
}
