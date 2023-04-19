package com.example.whereruapp;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("/login/{username}/{password}")
    Call<List<RetroLogin>> getLogin (@Path("username") String username,@Path("password") String password);
    @POST("/getNearby")
    Call<List<RetroPost>> getNearby (@Body HashMap<String,Object> args);
    @GET("/getUserPosts/{username}")
    Call<List<RetroPost>> getUserPosts (@Path("username") String username);
    @POST("/insertPost")
    Call<String> insertPost(@Body HashMap<String,Object> args);
    @POST("/insertComment")
    Call<String> insertComment(@Body HashMap<String,Object> args);
    @GET("/likePost/{postid}")
    Call<String> likePost (@Path("postid") int postid);
    @GET("/dislikePost/{postid}")
    Call<String> dislikePost (@Path("postid") int postid);
    @GET("/likeComment/{commentid}")
    Call<String> likeComment (@Path("commentid") int commentid);
    @GET("/dislikeComment/{commentid}")
    Call<String> dislikeComment (@Path("commentid") int commentid);
    @GET("/getPost/{postid}")
    Call<List<RetroPost>> getPost(@Path("postid") int postid);
    @GET("/getPostComments/{postid}")
    Call<List<RetroComment>> getComments(@Path("postid") int postid);
    @POST("/createAccount/{username}/{password}")
    Call<String> createAccount(@Path("username") String username, @Path("password") String password);

}
