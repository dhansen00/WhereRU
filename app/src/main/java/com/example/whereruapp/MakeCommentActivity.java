package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.Instant;
import java.util.EmptyStackException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A screen where the user can make a comment on the post displayed
 */
public class MakeCommentActivity extends AppCompatActivity {
    public int radius;
    public String username;
    public Double userlatitude;
    public Double userlongitude;
    public int postid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the values passed over from the previous screen
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        this.username = extras.getString("username");
        this.radius = extras.getInt("radius");
        this.userlatitude = extras.getDouble("latitude");
        this.userlongitude = extras.getDouble("longitude");
        this.postid = extras.getInt("postid");

        //create the display
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_comment);
    }

    /**
     * Posts a comment associated with the current post
     * @param view view of the post comment button
     */
    public void postComment(View view){
        //get the text in the comment text field
        EditText commentText = (EditText) this.findViewById(R.id.commentEditText);
        Editable text = commentText.getText();
        String commentString = text.toString();

        //verify that the comment is not an empty string
        if (commentString.isEmpty()){
            TextView invalidText = (TextView) this.findViewById(R.id.invalidText);
            invalidText.setVisibility(View.VISIBLE);
        }
        else{
            //Creates a HashMap for the objects to go in the body of the request
            HashMap<String,Object> args = new HashMap<String,Object>();
            args.put("postid", this.postid);
            args.put("text",commentString);
            args.put("author", this.username);
            long value = Instant.now().toEpochMilli();
            args.put("time", value);

            //perform a call to the db to post the comment
            GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<String> call = service.insertComment(args);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //send the user back to the ExpandedPostActivity
                    Intent intent = new Intent(MakeCommentActivity.this,ExpandedPostActivity.class);
                    intent.putExtra("username", MakeCommentActivity.this.username);
                    intent.putExtra("radius", MakeCommentActivity.this.radius);
                    intent.putExtra("latitude", MakeCommentActivity.this.userlatitude);
                    intent.putExtra("longitude", MakeCommentActivity.this.userlongitude);
                    intent.putExtra("postid", MakeCommentActivity.this.postid);
                    MakeCommentActivity.this.startActivity(intent);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    throw new EmptyStackException();
                }
            });
        }
    }
}