package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FiltersActivity extends AppCompatActivity {
    String username;
    int radius;
    double userlatitude;
    double userlongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        radius = extras.getInt("radius");
        userlatitude = extras.getDouble("latitude");
        userlongitude = extras.getDouble("longitude");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

    }

    public void toScroll(View v){
        TextView userRadius = (TextView)findViewById(R.id.distanceText);
        radius = Integer.parseInt(userRadius.getText().toString());
        if (radius > 1000 || radius < 0){
            TextView invalidView = (TextView)findViewById(R.id.invalidDistance);
            invalidView.setVisibility(View.VISIBLE);
        }
        else{
            Intent intent = new Intent(FiltersActivity.this,ScrollingActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("radius",radius);
            intent.putExtra("latitude",userlatitude);
            intent.putExtra("longitude",userlongitude);
            startActivity(intent);
        }
    }
}