package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakePostActivity extends AppCompatActivity {
    String username;
    int radius;
    Double userlatitude;
    Double userlongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        radius = extras.getInt("radius");
        userlatitude = extras.getDouble("latitude");
        userlongitude = extras.getDouble("longitude");

        System.out.println("lat"+userlatitude+"lon"+userlongitude);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);
    }

    public void postEntries(View v){
        EditText postText = (EditText) findViewById(R.id.postEditText);
        EditText radiusText = (EditText) findViewById(R.id.radiusEditText);
        EditText tag1 = (EditText) findViewById(R.id.tagEditText1);
        EditText tag2 = (EditText) findViewById(R.id.tagEditText2);
        EditText tag3 = (EditText) findViewById(R.id.tagEditText3);
        EditText tag4 = (EditText) findViewById(R.id.tagEditText4);
        EditText tag5 = (EditText) findViewById(R.id.tagEditText5);

        String postString = postText.getText().toString();
        int radiusValue = Integer.parseInt(radiusText.getText().toString());
        ArrayList<String> tags = new ArrayList<String>();
        tags.add(tag1.getText().toString());
        tags.add(tag2.getText().toString());
        tags.add(tag3.getText().toString());
        tags.add(tag4.getText().toString());
        tags.add(tag5.getText().toString());

        if (radius > 1000 || radius < 0 || postString.equals("")){
            //error
        }
        else {
            GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
            HashMap<String,Object> args = new HashMap<String,Object>();
            args.put("username",username);
            args.put("text",postString);
            args.put("radius",radiusValue);
            int added = 1;
            for(int i = 0;i<tags.size();i++){
                if(!tags.get(i).equals("")){
                    args.put("tag"+added,tags.get(i));
                    added+=1;
                }
            }
            for(int i = added;i<6;i++){
                args.put("tag"+i,null);
                i++;
            }
            args.put("lat",userlatitude);
            args.put("lon",userlongitude);
            args.put("time",Instant.now().toEpochMilli());

            Call<String> call = service.insertPost(args);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Intent intent = new Intent(MakePostActivity.this, ScrollingActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("radius", radius);
                    intent.putExtra("latitude",userlatitude);
                    intent.putExtra("longitude",userlongitude);
                    startActivity(intent);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
    public void cancelPost(View v){
        Intent intent = new Intent(MakePostActivity.this, ScrollingActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("radius",radius);
        intent.putExtra("latitude",userlatitude);
        intent.putExtra("longitude",userlongitude);
        startActivity(intent);
    }
}