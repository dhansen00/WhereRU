package com.example.whereruapp;

import com.google.gson.annotations.SerializedName;

public class RetroComment {
    @SerializedName("id")
    public int id;
    @SerializedName("postid")
    public int postid;
    @SerializedName("text")
    public String text;
    @SerializedName("likes")
    public int likes;
    @SerializedName("author")
    public String author;
    @SerializedName("time")
    public long time;

    public RetroComment(int id,int postid, String author, String text, int likes, long time){
        this.id = id;
        this.postid = postid;
        this.author = author;
        this.text = text;
        this.likes = likes;
    }
}
