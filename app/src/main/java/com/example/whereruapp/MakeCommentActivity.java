package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.Instant;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeCommentActivity extends AppCompatActivity {
    int radius;
    String username;
    Double userlatitude;
    Double userlongitude;
    int postid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        radius = extras.getInt("radius");
        userlatitude = extras.getDouble("latitude");
        userlongitude = extras.getDouble("longitude");
        postid = extras.getInt("postid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_comment);
    }

    public void postComment(View v){
        EditText commentText = (EditText) findViewById(R.id.commentEditText);
        String commentString = commentText.getText().toString();
        if (commentString.equals("")){
            TextView invalidText = (TextView) findViewById(R.id.invalidText);
            invalidText.setVisibility(View.VISIBLE);
        }
        else{
            System.out.println();
            GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
            HashMap<String,Object> args = new HashMap<String,Object>();
            args.put("postid",postid);
            args.put("text",commentString);
            args.put("author",username);
            args.put("time", Instant.now().toEpochMilli());
            Call<String> call = service.insertComment(args);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Intent intent = new Intent(MakeCommentActivity.this,ExpandedPostActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("radius",radius);
                    intent.putExtra("latitude",userlatitude);
                    intent.putExtra("longitude",userlongitude);
                    intent.putExtra("postid",postid);
                    startActivity(intent);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}