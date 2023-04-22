package com.example.whereruapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This class handles the scroll view of the app
 */
public class ScrollingActivity extends AppCompatActivity {
    private ViewGroup mLinearLayout;
    int radius;
    public String username;
    public Double userlatitude;
    public Double userlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the variables that were passed in from the previous screen
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        this.username = extras.getString("username");
        this.radius = extras.getInt("radius");
        this.userlatitude = extras.getDouble("latitude");
        this.userlongitude = extras.getDouble("longitude");

        //create the display
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_scrolling);
        this.mLinearLayout = (ViewGroup) this.findViewById(R.id.postList);
        this.getPosts(this.userlatitude, this.userlongitude, this.radius);
    }

    /**
     * Add the post to the scrolling feed
     * @param postText  the text to be displayed on the post
     * @param author    the author of the post
     * @param time      the time of posting
     * @param likes     the like count of the post
     * @param id        the id of the post
     */
    public void addPost(String postText,String author,long time,int likes, int id){
        //get the values of the views that will be modified
        View layout2 = LayoutInflater.from(this).inflate(R.layout.scrollingpost, this.mLinearLayout, false);
        TextView postTextView = (TextView) layout2.findViewById(R.id.postText);
        TextView postAuthorView = (TextView) layout2.findViewById(R.id.authorText);
        TextView likesView = (TextView) layout2.findViewById(R.id.likectr);
        TextView dateTextView = (TextView) layout2.findViewById(R.id.dateTime);

        //set the text in the views with the current post values
        postTextView.setText(postText);
        postAuthorView.setText(author);
        String text = Integer.toString(likes);
        likesView.setText(text);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = simpleDateFormat.format(new java.util.Date (time));
        dateTextView.setText(date);

        //add the post to the scrolling feed
        layout2.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout2.setTag(id);
        this.mLinearLayout.addView(layout2);
    }

    /**
     * Get the nearby posts from the database
     * @param latitude      the current latitude of the user
     * @param longitude     the current longitude of the user
     * @param r1        the r1 to look for posts in
     */
    public void getPosts(Double latitude, Double longitude, int r1){
        //Create the arguments to be used in the query
        HashMap<String,Object> args = new HashMap<String,Object>();
        args.put("latitude",latitude);
        args.put("longitude",longitude);
        args.put("radius",r1);

        //Perform the query to get the posts within the area
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<List<RetroPost>> call = service.getNearby(args);
        call.enqueue(new Callback<List<RetroPost>>() {
            @Override
            public void onResponse(Call<List<RetroPost>> call, Response<List<RetroPost>> response) {
                if(response.isSuccessful()) {
                    //upon receipt of the nearby posts add them to the screen
                    List<RetroPost> nearbyPosts = response.body();
                    for (int x = 0; x < nearbyPosts.size() ; x++) {
                        RetroPost retroPost = nearbyPosts.get(x);
                        ScrollingActivity.this.addPost(retroPost.text, retroPost.author, retroPost.time, retroPost.likes, retroPost.id);
                    }
                }
                else {
                    throw new EmptyStackException();
                }
            }
            @Override
            public void onFailure(Call<List<RetroPost>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Likes the post that is associated with the like button
     * @param view the view of the like button that was pressed
     */
    public void likePost(View view){
        //get the post id of the post
        View postLayout = (View) view.getParent().getParent().getParent();
        Object tag = postLayout.getTag();
        String s = tag.toString();
        int postId = Integer.parseInt(s);

        //perform the call for liking the post
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<String> call = service.likePost(postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //upon receipt of confirmation update the like count
                LinearLayout x = (LinearLayout) view.getParent();
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
     * Dislikes the post that is associated with the dislike button
     * @param view the view of the dislike button pressed
     */
    public void dislikePost(View view){
        //get the post id of the post
        View postLayout = (View) view.getParent().getParent().getParent();
        Object tag = postLayout.getTag();
        String s = tag.toString();
        int postId = Integer.parseInt(s);

        //perform the call for disliking the post
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<String> call = service.dislikePost(postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //upon receipt of confirmation update the like count
                LinearLayout x = (LinearLayout) view.getParent();
                TextView likeCount = (TextView) x.getChildAt(2);

                CharSequence text = likeCount.getText();
                String s1 = text.toString();
                int likes = Integer.parseInt(s1);
                likes = likes - 1;
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
     * Expands the post associated with the expand button
     * @param view the view of the expand button pressed
     */
    public void expandPost(View view){
        View x = (View) view.getParent().getParent().getParent();
        Object tag = x.getTag();
        String s = tag.toString();
        int postid = Integer.parseInt(s);

        Intent intent = new Intent(ScrollingActivity.this,ExpandedPostActivity.class);
        intent.putExtra("username", this.username);
        intent.putExtra("radius", this.radius);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        intent.putExtra("postid",postid);
        this.startActivity(intent);
    }

    /**
     * Takes the user to the FiltersActivity
     * @param view the view of the filters button pressed
     */
    public void setFilters(View view){
        Intent intent = new Intent(ScrollingActivity.this, FiltersActivity.class);
        intent.putExtra("radius", this.radius);
        intent.putExtra("username", this.username);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }

    /**
     * Takes the user to the map view
     * @param view the view of the map view button pressed
     */
    public void toMapView(View view){
        Intent intent = new Intent(ScrollingActivity.this, MapsActivity.class);
        intent.putExtra("radius", this.radius);
        intent.putExtra("username", this.username);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }

    /**
     * Takes the user to the make post view
     * @param view the view of the make post button
     */
    public void makePost(View view){
        Intent intent = new Intent(ScrollingActivity.this,MakePostActivity.class);
        intent.putExtra("username", this.username);
        intent.putExtra("radius", this.radius);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }

    /**
     * Takes the user to their account page
     * @param view the view of the account button
     */
    public void toAccount(View view){
        Intent intent = new Intent(ScrollingActivity.this,AccountActivity.class);
        intent.putExtra("username", this.username);
        intent.putExtra("radius", this.radius);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }
}
