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

    /**
     * Attempts to create an account for the user
     * @param username  the username for account
     * @param password  the password for account
     * @return  A confirmation string
     */
    @POST("/createAccount/{username}/{password}")
    Call<String> createAccount(@Path("username") String username, @Path("password") String password);

    /**
     * Attempts to login the user
     * @param username the username for login
     * @param password the password for login
     * @return  A list of retroLogin, size should be 1 however
     */
    @GET("/login/{username}/{password}")
    Call<List<RetroLogin>> getLogin (@Path("username") String username,@Path("password") String password);

    /**
     * Gets the nearby posts from a current location
     * @param args a list of arguments for the query should contain mappings for latitude, longitude, and radius
     * @return  A list of RetroPost that are within the radius
     */
    @POST("/getNearby")
    Call<List<RetroPost>> getNearby (@Body HashMap<String,Object> args);

    /**
     * Gets the posts that were posted by a single user
     * @param username the username of the user to get the posts for
     * @return A list of RetroPost that were posted by a user
     */
    @GET("/getUserPosts/{username}")
    Call<List<RetroPost>> getUserPosts (@Path("username") String username);

    /**
     * Creates and posts a post
     * @param args args a list of arguments for the query should contain mappings for username, text, radius, tag1, tag2, tag3, tag4, tag5, lat, lon, and time
     * @return  A confirmation string
     */
    @POST("/insertPost")
    Call<String> insertPost(@Body HashMap<String,Object> args);

    /**
     * Creates and posts a comment
     * @param args args a list of arguments for the query should contain mappings for postid, text, author, and time
     * @return  A confirmation string
     */
    @POST("/insertComment")
    Call<String> insertComment(@Body HashMap<String,Object> args);

    /**
     * Likes a post by updating its like counter
     * @param postid    the postid of the post to be liked
     * @return          A confirmation string
     */
    @GET("/likePost/{postid}")
    Call<String> likePost (@Path("postid") int postid);

    /**
     * Dislikes a post by updating its like counter
     * @param postid    the postid of the post to be disliked
     * @return          A confirmation string
     */
    @GET("/dislikePost/{postid}")
    Call<String> dislikePost (@Path("postid") int postid);

    /**
     * Likes a comment by updating its like counter
     * @param commentid     the commentid of the post to be liked
     * @return              A confirmation string
     */
    @GET("/likeComment/{commentid}")
    Call<String> likeComment (@Path("commentid") int commentid);

    /**
     * Dislikes a comment by updating its like counter
     * @param commentid     the commentid of the post to be disliked
     * @return              A confirmation string
     */
    @GET("/dislikeComment/{commentid}")
    Call<String> dislikeComment (@Path("commentid") int commentid);

    /**
     * Gets the post specified by the postid
     * @param postid    The postid of the post to be retrieved
     * @return          A list of RetroPost, Size will be 1 however
     */
    @GET("/getPost/{postid}")
    Call<List<RetroPost>> getPost(@Path("postid") int postid);

    /**
     * Gets the comments associated with the postid
     * @param postid    The postid of the post whose comments will be retrieved
     * @return          A list of RetroComment
     */
    @GET("/getPostComments/{postid}")
    Call<List<RetroComment>> getComments(@Path("postid") int postid);
}
