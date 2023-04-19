package com.example.whereruapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetroPost {
    @SerializedName("id")
    public int id;
    @SerializedName("author")
    public String author;
    @SerializedName("text")
    public String text;
    @SerializedName("radius")
    public int radius;
    @SerializedName("likes")
    public int likes;
    @SerializedName("tag1")
    private String tag1;
    @SerializedName("tag2")
    private String tag2;
    @SerializedName("tag3")
    private String tag3;
    @SerializedName("tag4")
    private String tag4;
    @SerializedName("tag5")
    private String tag5;
    @SerializedName("latitude")
    public Double latitude;
    @SerializedName("longitude")
    public Double longitude;
    @SerializedName("time")
    public long time;
    ArrayList<String> tags = new ArrayList<String>();
    public RetroPost(int id, String author, String text, int radius, int likes, String tag1, String tag2, String tag3, String tag4,String tag5, Double latitude, Double longitude, long time){
        this.id = id;
        this.author = author;
        this.text = text;
        this.radius = radius;
        this.likes = likes;

        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
        this.tag5 = tag5;
        if (tag1 != null){
            tags.add(tag1);
            if(tag2!=null){
                tags.add(tag2);
                    if (tag3!=null){
                        tags.add(tag3);
                        if (tag4!= null){
                            tags.add(tag4);
                            if (tag5!=null){
                                tags.add(tag5);
                            }
                        }
                    }
            }
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Post toPost(){
        return new Post(this.id,this.author ,this.text, this.tags, this.radius, this.latitude, this.longitude, this.time, this.likes);
    }
}
