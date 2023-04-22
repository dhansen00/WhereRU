package com.example.whereruapp;

import java.util.ArrayList;

/**
 * A class that represents a post in our database
 */
public class Post extends Content{
    private double latitude;
    private double longitude;
    private int radius;
    private ArrayList<String> tags = new ArrayList<String>();

    /**
     * Constructor for the post class
     * @param i         the id of the post
     * @param s         the username that is posting the post
     * @param s1        the text that is being posted
     * @param list      An ArrayList of tags
     * @param i1        the radius of the post
     * @param lat       the latitude of the post
     * @param lon       the longitude of the post
     * @param aLong     the time of the post in milliseconds since epoch
     * @param i2        the like count
     */
    public Post(int i, String s, String s1, ArrayList<String> list, int i1, Double lat, Double lon, Long aLong,int i2){
        this.id = i;
        this.username = s;
        this.text = s1;
        this.tags = list;
        this.radius = i1;
        this.latitude = lat;
        this.longitude = lon;
        this.posttime = aLong;
        this.likes = i2;
    }
}

