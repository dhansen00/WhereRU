package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A screen that allows the user to create a post
 */
public class MakePostActivity extends AppCompatActivity {
    public String username;
    public int radius;
    public Double userlatitude;
    public Double userlongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the variables passed in from the previous screen
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        this.username = extras.getString("username");
        this.radius = extras.getInt("radius");
        this.userlatitude = extras.getDouble("latitude");
        this.userlongitude = extras.getDouble("longitude");

        //create the display
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_make_post);
    }

    /**
     * Creates a post with the entries in the text fields
     * @param view the view of the post button that was pressed
     */
    public void postEntries(View view){
        //get the Edit Text views of the ones on the display
        EditText postText = (EditText) this.findViewById(R.id.postEditText);
        EditText radiusText = (EditText) this.findViewById(R.id.radiusEditText);
        EditText tag1 = (EditText) this.findViewById(R.id.tagEditText1);
        EditText tag2 = (EditText) this.findViewById(R.id.tagEditText2);
        EditText tag3 = (EditText) this.findViewById(R.id.tagEditText3);
        EditText tag4 = (EditText) this.findViewById(R.id.tagEditText4);
        EditText tag5 = (EditText) this.findViewById(R.id.tagEditText5);

        //sets up the variables to be passed into the arguments of the query to the db
        Editable text = postText.getText();
        String postString = text.toString();
        Editable text1 = radiusText.getText();
        String s = text1.toString();
        int radiusValue = Integer.parseInt(s);

        ArrayList<String> tags = new ArrayList<String>();
        Editable text2 = tag1.getText();
        String s1 = text2.toString();
        Editable text3 = tag2.getText();
        String s2 = text3.toString();
        Editable text4 = tag3.getText();
        String s3 = text4.toString();
        Editable text5 = tag4.getText();
        String s4 = text5.toString();
        Editable text6 = tag5.getText();
        String s5 = text6.toString();
        tags.add(s1);
        tags.add(s2);
        tags.add(s3);
        tags.add(s4);
        tags.add(s5);

        if (1000 < radiusValue || 0 > radiusValue){
            //do not allow for a distance less than 0 or greater than 1000
            TextView invalidView = (TextView) this.findViewById(R.id.invalidDistance);
            invalidView.setVisibility(View.VISIBLE);
        }
        else {
            //set up the variables to go into the query body
            HashMap<String,Object> args = new HashMap<String,Object>();
            args.put("username", this.username);
            args.put("text",postString);
            args.put("radius",radiusValue);
            //add all values into the tags array
            int added = 1;
            for(int i = 0;i<tags.size();i++){
                String s6 = tags.get(i);
                if(!s6.isEmpty()){
                    String value = tags.get(i);
                    args.put("tag"+added, value);
                    added+=1;
                }
            }
            for(int i = added;i<6;i++){
                args.put("tag"+i,null);
            }
            args.put("lat", this.userlatitude);
            args.put("lon", this.userlongitude);
            long value = Instant.now().toEpochMilli();
            args.put("time", value);

            //perform the call to the database
            Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
            GetDataService service = retrofitInstance.create(GetDataService.class);
            Call<String> call = service.insertPost(args);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //upon confirmation that the post was posted send the user to the ScrollingActivity
                    MakePostActivity.this.toScroll();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    throw new EmptyStackException();
                }
            });
        }
    }

    /**
     * A method that cancels the post
     * @param view the view of the cancel button being pressed
     */
    public void cancelPost(View view){
        this.toScroll();
    }

    private void toScroll(){
        Intent intent = new Intent(MakePostActivity.this, ScrollingActivity.class);
        intent.putExtra("username", this.username);
        intent.putExtra("radius", this.radius);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }
}