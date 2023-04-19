package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {
    int radius;
    private ViewGroup mLinearLayout;
    String username;
    Double userlatitude;
    Double userlongitude;
    HashMap<Integer,Integer> androidIdtoPostId = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        radius = extras.getInt("radius");
        userlatitude = extras.getDouble("latitude");
        userlongitude = extras.getDouble("longitude");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mLinearLayout = (ViewGroup) findViewById(R.id.postList);
        getPosts();
    }
    public void getPosts(){
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<List<RetroPost>> call = service.getUserPosts(username);
        call.enqueue(new Callback<List<RetroPost>>() {
            @Override
            public void onResponse(Call<List<RetroPost>> call, Response<List<RetroPost>> response) {
                if(response.isSuccessful()) {
                    List<RetroPost> nearbyPosts = response.body();
                    for (int x = 0; x < nearbyPosts.size() ; x++) {
                        addPost(nearbyPosts.get(x).text, nearbyPosts.get(x).author, nearbyPosts.get(x).time,nearbyPosts.get(x).likes,nearbyPosts.get(x).id);
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
    public void addPost(String postText,String author,long time,int likes, int id){
        View layout2 = LayoutInflater.from(this).inflate(R.layout.scrollingpost, mLinearLayout, false);
        TextView postTextView = (TextView) layout2.findViewById(R.id.postText);
        TextView postAuthorView = (TextView) layout2.findViewById(R.id.authorText);
        TextView likesView = (TextView) layout2.findViewById(R.id.likectr);
        Button likeButton = (Button) layout2.findViewById(R.id.likeButton);
        Button dislikeButton = (Button) layout2.findViewById(R.id.likeButton2);
        TextView dateTextView = (TextView) layout2.findViewById(R.id.dateTime);

        postTextView.setText(postText);
        postAuthorView.setText(author);
        Integer bigLikes = likes;
        likesView.setText(bigLikes.toString());
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (time));
        dateTextView.setText(date);

        layout2.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout2.setTag(id);
        mLinearLayout.addView(layout2);
    }
    public void logout(View v){
        startActivity(new Intent(AccountActivity.this, MainActivity.class));
    }
    public void toMapView(View v){
        Intent intent = new Intent(AccountActivity.this, MapsActivity.class);
        intent.putExtra("radius", radius);
        intent.putExtra("username",username);
        intent.putExtra("latitude",userlatitude);
        intent.putExtra("longitude",userlongitude);
        startActivity(intent);
    }
    public void toScrollView(View view){
        Intent intent = new Intent(AccountActivity.this, ScrollingActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("radius",radius);
        intent.putExtra("latitude",userlatitude);
        intent.putExtra("longitude",userlongitude);
        startActivity(intent);
    }
    public void likePost(View v){
        View postLayout = (View) v.getParent().getParent().getParent();
        int postId = Integer.parseInt(postLayout.getTag().toString());
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<String> call = service.likePost(postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LinearLayout x = (LinearLayout) v.getParent();
                int count = x.getChildCount();
                TextView likeCount = (TextView) x.getChildAt(2);

                int likes = Integer.parseInt(likeCount.getText().toString());
                likes = likes + 1;
                likeCount.setText(Integer.toString(likes));
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void dislikePost(View v){
        View postLayout = (View) v.getParent().getParent().getParent();
        int postId = Integer.parseInt(postLayout.getTag().toString());
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<String> call = service.dislikePost(postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LinearLayout x = (LinearLayout) v.getParent();
                int count = x.getChildCount();
                TextView likeCount = (TextView) x.getChildAt(2);

                int likes = Integer.parseInt(likeCount.getText().toString());
                likes = likes - 1;
                likeCount.setText(Integer.toString(likes));
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void expandPost(View v){
        View x = (View) v.getParent().getParent().getParent();
        int postid = Integer.parseInt(x.getTag().toString());

        Intent intent = new Intent(AccountActivity.this,ExpandedPostActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("radius",radius);
        intent.putExtra("latitude",userlatitude);
        intent.putExtra("longitude",userlongitude);
        intent.putExtra("postid",postid);
        startActivity(intent);
    }

}