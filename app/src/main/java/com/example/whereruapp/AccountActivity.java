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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 *
 */
public class AccountActivity extends AppCompatActivity {
    private int radius = 0;
    private ViewGroup mLinearLayout = null;
    private String username = null;
    private Double userlatitude = null;
    private Double userlongitude = null;

    private ArrayList<Post> postsToSort = new ArrayList<>(50);

    //Creates an instance of the account screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();

        //set variables from previous screen
        this.username = extras.getString("username");
        this.radius = extras.getInt("radius");
        this.userlatitude = extras.getDouble("latitude");
        this.userlongitude = extras.getDouble("longitude");

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_account);

        //get the id for the linear layout that holds the posts
        this.mLinearLayout = (ViewGroup) this.findViewById(R.id.postList);
        this.getPosts();
    }
    //performs a call to the REST API and then passes the sorted posts to add posts to display
    private void getPosts(){
        //perform call to the API
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<List<RetroPost>> call = service.getUserPosts(this.username);
        call.enqueue(new Callback<List<RetroPost>>() {
            @Override
            public void onResponse(Call<List<RetroPost>> call, Response<List<RetroPost>> response) {
                if(response.isSuccessful()) {
                    List<RetroPost> nearbyPosts = response.body();
                    assert null != nearbyPosts;
                    AccountActivity.this.addPosts(nearbyPosts);
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
     * adds all the posts to the screen sorted by time
     * @param retroPosts A list of the retroposts received from the db to be displayed
     */
    private void addPosts(List<RetroPost> retroPosts){
        //convert retroposts to posts
        ArrayList<Post> posts = new ArrayList<>();
        for(int i = 0; i< retroPosts.size();i++) {
            RetroPost retroPost = retroPosts.get(i);
            Post currpost = retroPost.toPost();
            posts.add(currpost);
        }

        posts.sort(new ContentTimeComparator());

        //add the posts to the display
        for (int i = posts.size()-1; i>=0;i--){
            //get all the variables of the post
            Post currpost = posts.get(i);
            String postText = currpost.getContent();
            System.out.println(postText);
            String author = currpost.getUsername();
            int likes = currpost.getLikes();
            int id = currpost.getId();
            Long time = currpost.getPostTime();

            //get all of the display fields
            View layout2 = LayoutInflater.from(this).inflate(R.layout.scrollingpost, this.mLinearLayout, false);
            TextView postTextView = (TextView) layout2.findViewById(R.id.postText);
            TextView postAuthorView = (TextView) layout2.findViewById(R.id.authorText);
            TextView likesView = (TextView) layout2.findViewById(R.id.likectr);
            Button likeButton = (Button) layout2.findViewById(R.id.likeButton);
            Button dislikeButton = (Button) layout2.findViewById(R.id.likeButton2);
            TextView dateTextView = (TextView) layout2.findViewById(R.id.dateTime);

            //set display field text
            postTextView.setText(postText);
            postAuthorView.setText(author);
            String text = Integer.toString(likes);
            likesView.setText(text);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String date = simpleDateFormat.format(new java.util.Date(time));
            dateTextView.setText(date);

            layout2.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            layout2.setTag(id);
            this.mLinearLayout.addView(layout2);
        }
    }

    /**
     * logsout the user from the app, returns to the login page
     * @param view the view of the object that was clicked
     */
    public void logout(View view){
        this.startActivity(new Intent(AccountActivity.this, LoginActivity.class));
    }

    /**
     * Takes the user to the map view with all previous filters
     * @param view  the view of the object that was clicked
     */
    public void toMapView(View view){
        Intent intent = new Intent(AccountActivity.this, MapsActivity.class);
        intent.putExtra("radius", this.radius);
        intent.putExtra("username", this.username);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }

    /**
     * Takes the user to the scroll view with all previous filters
     * @param view  the view of the object that was clicked
     */
    public void toScrollView(View view){
        Intent intent = new Intent(AccountActivity.this, ScrollingActivity.class);
        intent.putExtra("radius", this.radius);
        intent.putExtra("username", this.username);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }

    /**
     * Likes the post and updates the like counter
     * @param view the view of the object that was clicked
     */
    public void likePost(View view){
        //gets the info of the linear layout of the post since the tag contains the postid
        View postLayout = (View) view.getParent().getParent().getParent();
        Object tag = postLayout.getTag();
        String s = tag.toString();
        int postId = Integer.parseInt(s);

        //performs call to the db
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<String> call = service.likePost(postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //upon receiving the confirmation that the like was updated then the post on the screen will update
                LinearLayout x = (LinearLayout) view.getParent();
                int count = x.getChildCount();
                TextView likeCount = (TextView) x.getChildAt(2);

                CharSequence text = likeCount.getText();
                String s1 = text.toString();
                int likes = Integer.parseInt(s1);
                likes = likes + 1;
                String text1 = Integer.toString(likes);
                likeCount.setText(text1);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                throw new EmptyStackException();
            }
        });
    }

    /**
     * Dislikes the post and updates the like counter
     * @param view the view of the object that was clicked
     */
    public void dislikePost(View view){
        //gets the info of the linear layout of the post since the tag contains the postid
        View postLayout = (View) view.getParent().getParent().getParent();
        Object tag = postLayout.getTag();
        String s = tag.toString();
        int postId = Integer.parseInt(s);

        //performs call to the db
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<String> call = service.dislikePost(postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //upon receiving the confirmation that the like was updated then the post on the screen will update
                LinearLayout x = (LinearLayout) view.getParent();
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


    /**
     * Expands the post and takes the user to the ExpandedPostActivity
     * @param view the view of the object that was clicked
     */
    public void expandPost(View view){
        View x = (View) view.getParent().getParent().getParent();
        int postid = Integer.parseInt(x.getTag().toString());

        Intent intent = new Intent(AccountActivity.this,ExpandedPostActivity.class);
        intent.putExtra("username", getUsername());
        intent.putExtra("radius", getRadius());
        intent.putExtra("latitude", getUserlatitude());
        intent.putExtra("longitude", getUserlongitude());
        intent.putExtra("postid",postid);
        startActivity(intent);
    }

    //GETTERS
    private int getRadius() {
        return this.radius;
    }
    private String getUsername() {
        return this.username;
    }
    private Double getUserlatitude() {
        return this.userlatitude;
    }
    private Double getUserlongitude() {
        return this.userlongitude;
    }
}