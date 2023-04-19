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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpandedPostActivity extends AppCompatActivity {
    private ViewGroup mLinearLayout;
    int radius;
    String username;
    Double userlatitude;
    Double userlongitude;
    int postid;

    Post curr = null;
    TextView postText;
    TextView postLikes;
    TextView postAuthor;
    TextView postTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_post);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        radius = extras.getInt("radius");
        userlatitude = extras.getDouble("latitude");
        userlongitude = extras.getDouble("longitude");
        postid = extras.getInt("postid");

        postText = (TextView) findViewById(R.id.postText2);
        postLikes = (TextView) findViewById(R.id.likectr);
        postAuthor = (TextView) findViewById(R.id.usernameTextView);
        postTime = (TextView) findViewById(R.id.dateTime);

        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroPost>> call = service.getPost(postid);
        call.enqueue(new Callback<List<RetroPost>>() {
            @Override
            public void onResponse(Call<List<RetroPost>> call, Response<List<RetroPost>> response) {
                if(response.isSuccessful()) {
                    RetroPost currPost = response.body().get(0);
                    postText.setText(currPost.text);
                    postLikes.setText(Integer.toString(currPost.likes));
                    postAuthor.setText(currPost.author);
                    String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(currPost.time));
                    postTime.setText(date);
                }
                else{
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
        mLinearLayout = (ViewGroup) findViewById(R.id.commentList);
        getComments(postid);
    }
    public void getComments(int postid){
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroComment>> call = service.getComments(postid);
        call.enqueue(new Callback<List<RetroComment>>() {
            @Override
            public void onResponse(Call<List<RetroComment>> call, Response<List<RetroComment>> response) {
                if(response.isSuccessful()) {
                    List<RetroComment> nearbyComments = response.body();
                    for (int x = 0; x < nearbyComments.size() ; x++) {
                        addComments(nearbyComments.get(x).text, nearbyComments.get(x).author, nearbyComments.get(x).time,nearbyComments.get(x).likes,nearbyComments.get(x).id);
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
            public void onFailure(Call<List<RetroComment>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void addComments(String postText,String author,long time,int likes, int id){
        View layout2 = LayoutInflater.from(this).inflate(R.layout.scrollingcomment, mLinearLayout, false);
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

    public void toScrollView(View v){
        Intent intent = new Intent(ExpandedPostActivity.this,ScrollingActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("radius",radius);
        intent.putExtra("latitude",userlatitude);
        intent.putExtra("longitude",userlongitude);
        startActivity(intent);
    }
    public void likeComment(View v){
        View commentLayout = (View) v.getParent().getParent().getParent();
        int commentId = Integer.parseInt(commentLayout.getTag().toString());
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<String> call = service.likeComment(commentId);
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
    public void dislikeComment(View v){
        View commentLayout = (View) v.getParent().getParent().getParent();
        int commentId = Integer.parseInt(commentLayout.getTag().toString());
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<String> call = service.dislikeComment(commentId);
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
    public void likeThisPost(View v){
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<String> call = service.likePost(postid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LinearLayout x = (LinearLayout) v.getParent();
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
    public void dislikeThisPost(View v){
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<String> call = service.dislikePost(postid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LinearLayout x = (LinearLayout) v.getParent();
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
    public void toMakeComment(View v){
        Intent intent = new Intent(ExpandedPostActivity.this,MakeCommentActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("radius",radius);
        intent.putExtra("latitude",userlatitude);
        intent.putExtra("longitude",userlongitude);
        intent.putExtra("postid",postid);
        startActivity(intent);
    }
}