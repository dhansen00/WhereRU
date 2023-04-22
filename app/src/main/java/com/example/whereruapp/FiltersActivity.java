package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * A menu where the user can set the filters from what the views can be viewed
 */
public class FiltersActivity extends AppCompatActivity {
    private String username;
    private int radius;
    private double userlatitude;
    private double userlongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the variables passed over from the previous screen
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        this.username = extras.getString("username");
        this.radius = extras.getInt("radius");
        this.userlatitude = extras.getDouble("latitude");
        this.userlongitude = extras.getDouble("longitude");

        //create the display
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_filters);

    }

    /**
     * Takes the user to the scroll view
     * @param view the view of the scrollview button pressed
     */
    public void toScroll(View view){
        //get the variables provided by the user
        TextView userRadius = (TextView) this.findViewById(R.id.distanceText);
        CharSequence text = userRadius.getText();
        String s = text.toString();
        int givenRadius = Integer.parseInt(s);

        if (1000 < this.radius || givenRadius < 0){
            //do not allow for a distance less than 0 or greater than 1000
            TextView invalidView = (TextView)findViewById(R.id.invalidDistance);
            invalidView.setVisibility(View.VISIBLE);
        }
        else{
            //set the variables to be passed onto the next screen
            this.radius = givenRadius;
            Intent intent = new Intent(FiltersActivity.this,ScrollingActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("radius",radius);
            intent.putExtra("latitude",userlatitude);
            intent.putExtra("longitude",userlongitude);
            startActivity(intent);
        }
    }
}