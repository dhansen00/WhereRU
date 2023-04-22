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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This activity displays the post that the user chose to expand
 */
public class ExpandedPostActivity extends AppCompatActivity {
    private ViewGroup mLinearLayout;
    private int radius;
    private String username;
    private Double userlatitude;
    private Double userlongitude;
    protected int postid;
    protected TextView postText;
    protected TextView postLikes;
    protected TextView postAuthor;
    protected TextView postTime ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_expanded_post);

        //get data that was passed over from the prevous screen
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        this.username = extras.getString("username");
        this.radius = extras.getInt("radius");
        this.userlatitude = extras.getDouble("latitude");
        this.userlongitude = extras.getDouble("longitude");
        this.postid = extras.getInt("postid");

        //assign the display views that will be needed
        this.postText = (TextView) this.findViewById(R.id.postText2);
        this.postLikes = (TextView) this.findViewById(R.id.likectr);
        this.postAuthor = (TextView) this.findViewById(R.id.usernameTextView);
        this.postTime = (TextView) this.findViewById(R.id.dateTime);

        //perfrom a call to the db to get all of the necessary data on this post from the db
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<List<RetroPost>> call = service.getPost(this.postid);
        call.enqueue(new Callback<List<RetroPost>>() {
            @Override
            public void onResponse(Call<List<RetroPost>> call, Response<List<RetroPost>> response) {
                if(response.isSuccessful()) {
                    List<RetroPost> retroPosts = response.body();
                    RetroPost currPost = retroPosts.get(0);
                    ExpandedPostActivity.this.postText.setText(currPost.text);
                    String text = Integer.toString(currPost.likes);
                    ExpandedPostActivity.this.postLikes.setText(text);
                    ExpandedPostActivity.this.postAuthor.setText(currPost.author);
                    String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(currPost.time));
                    ExpandedPostActivity.this.postTime.setText(date);
                }
                else{
                    throw new EmptyStackException();
                }
            }
            @Override
            public void onFailure(Call<List<RetroPost>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        //find the linear layout view that will contain the comments and retrieve them
        this.mLinearLayout = (ViewGroup) this.findViewById(R.id.commentList);
        this.getComments(this.postid);
    }

    /**
     * Gets the comments of a post
     * @param i the id of the post to get comments for
     */
    private void getComments(int i){
        //query the database for the comments associated with a post
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroComment>> call = service.getComments(i);
        call.enqueue(new Callback<List<RetroComment>>() {
            @Override
            public void onResponse(Call<List<RetroComment>> call, Response<List<RetroComment>> response) {
                if(response.isSuccessful()) {
                    //if successfully retrieve the comments then send them to be added
                    List<RetroComment> nearbyComments = response.body();
                    ExpandedPostActivity.this.addComments(nearbyComments);
                }
                else {
                    throw new EmptyStackException();
                }
            }
            @Override
            public void onFailure(Call<List<RetroComment>> call, Throwable t) {
                throw new EmptyStackException();
            }
        });
    }

    /**
     * @param retroComments A list of RetroComments obtained from getComments
     */
    private void addComments(List<RetroComment> retroComments){
        //convert the list of retrocomments to comments
        ArrayList<Comment> comments = new ArrayList<>();
        for(int i =0; i<retroComments.size();i++){
            RetroComment currRetro = retroComments.get(i);
            comments.add(currRetro.toComment());
        }

        comments.sort(new ContentTimeComparator());

        for(int i=0;i<comments.size();i++) {
            //get the info of the current comment
            Comment currComment = comments.get(i);
            String commentText = currComment.getContent();
            String author = currComment.getUsername();
            int likes = currComment.getLikes();
            Long time = currComment.getPostTime();
            int id = currComment.getPostId();

            //get the views of all of the components of a comment
            View layout2 = LayoutInflater.from(this).inflate(R.layout.scrollingcomment, mLinearLayout, false);
            TextView postTextView = (TextView) layout2.findViewById(R.id.postText);
            TextView postAuthorView = (TextView) layout2.findViewById(R.id.authorText);
            TextView likesView = (TextView) layout2.findViewById(R.id.likectr);
            Button likeButton = (Button) layout2.findViewById(R.id.likeButton);
            Button dislikeButton = (Button) layout2.findViewById(R.id.likeButton2);
            TextView dateTextView = (TextView) layout2.findViewById(R.id.dateTime);

            //set the values of the views of the comment
            postTextView.setText(commentText);
            postAuthorView.setText(author);
            Integer bigLikes = likes;
            likesView.setText(bigLikes.toString());
            String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(time));
            dateTextView.setText(date);

            //add the comment to the display
            layout2.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layout2.setTag(id);
            this.mLinearLayout.addView(layout2);
        }
    }

    /**
     * Allows the user to like the comment from pressing the like button
     * @param view the view of the like button pressed
     */
    public void likeComment(View view){
        //get the data needed to update the likes of the comment
        View commentLayout = (View) view.getParent().getParent().getParent();
        Object tag = commentLayout.getTag();
        String s = tag.toString();
        int commentId = Integer.parseInt(s);

        //perform a call to the db
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<String> call = service.likeComment(commentId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //upon confirmation update the like count locally
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
     * @param view the view of the dislike button pressed
     */
    public void dislikeComment(View view){
        //get the data needed to update the likes of the comment
        View commentLayout = (View) view.getParent().getParent().getParent();
        Object tag = commentLayout.getTag();
        String s = tag.toString();
        int commentId = Integer.parseInt(s);

        //perform a call to the db to update the like count
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<String> call = service.dislikeComment(commentId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //upon confirmation of the like count being updated, update the count locally
                LinearLayout x = (LinearLayout) view.getParent();
                int count = x.getChildCount();
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
     * Likes the post that is currently expanded
     * @param view the view of the like button that was pressed
     */
    public void likeThisPost(View view){
        //perform a call to the db to update the like count
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<String> call = service.likePost(this.postid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //upon confirmation of the like count being updated, update the count locally
                LinearLayout x = (LinearLayout) view.getParent();
                TextView likeCount = (TextView) x.getChildAt(2);

                CharSequence likeCountText = likeCount.getText();
                String s = likeCountText.toString();
                int likes = Integer.parseInt(s);
                likes = likes + 1;
                String text = Integer.toString(likes);
                likeCount.setText(text);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                throw new EmptyStackException();
            }
        });
    }

    /**
     * Dislikes the post that is currently expanded
     * @param view the view of the dislike button being pressed
     */
    public void dislikeThisPost(View view){
        //perform a call to the db to update the like count
        Retrofit retrofitInstance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = retrofitInstance.create(GetDataService.class);
        Call<String> call = service.dislikePost(this.postid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //upon confirmation of the like count being updated, update the count locally
                LinearLayout x = (LinearLayout) view.getParent();
                TextView likeCount = (TextView) x.getChildAt(2);

                CharSequence text = likeCount.getText();
                String s = text.toString();
                int likes = Integer.parseInt(s);
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
     * Takes the user to the MakeCommentActivity
     * @param view  the view of the make comment button pressed
     */
    public void toMakeComment(View view){
        Intent intent = new Intent(ExpandedPostActivity.this,MakeCommentActivity.class);
        intent.putExtra("username", this.username);
        intent.putExtra("radius", this.radius);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        intent.putExtra("postid", this.postid);
        startActivity(intent);
    }
    /**
     * Takes the user to the scroll view
     * @param view the view of the expand button pressed
     */
    public void toScrollView(View view){
        Intent intent = new Intent(ExpandedPostActivity.this,ScrollingActivity.class);
        intent.putExtra("username", this.username);
        intent.putExtra("radius", this.radius);
        intent.putExtra("latitude", this.userlatitude);
        intent.putExtra("longitude", this.userlongitude);
        this.startActivity(intent);
    }
}