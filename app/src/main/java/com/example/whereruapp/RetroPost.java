package com.example.whereruapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *A class that is used to interpret the output from RetroFit
 */
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

    /**
     * The constructor for the RetroPost class
     * @param inId          The id of the post
     * @param inAuthor      The username that made the post
     * @param inText        The text that the post contains
     * @param inRadius      The radius that the post is viewable from
     * @param inLikes       The like count of the post
     * @param inTag1        The first tag on the post
     * @param inTag2        The second tag on the post
     * @param inTag3        The third tag on the post
     * @param inTag4        The fourth tag on the post
     * @param inTag5        The fifth tag on the post
     * @param inLatitude    The latitude of the post
     * @param inLongitude   The longitude of the post
     * @param inTime        The time the post was made
     */
    public RetroPost(int inId, String inAuthor, String inText, int inRadius, int inLikes, String inTag1, String inTag2, String inTag3, String inTag4,String inTag5, Double inLatitude, Double inLongitude, long inTime){
        this.id = inId;
        this.author = inAuthor;
        this.text = inText;
        this.radius = inRadius;
        this.likes = inLikes;

        this.tag1 = inTag1;
        this.tag2 = inTag2;
        this.tag3 = inTag3;
        this.tag4 = inTag4;
        this.tag5 = inTag5;
        if (inTag1 != null){
            tags.add(inTag1);
            if(inTag2!=null){
                tags.add(inTag2);
                    if (inTag3!=null){
                        tags.add(inTag3);
                        if (inTag4!= null){
                            tags.add(inTag4);
                            if (inTag5!=null){
                                tags.add(inTag5);
                            }
                        }
                    }
            }
        }
        this.latitude = inLatitude;
        this.longitude = inLongitude;
        this.time = inTime;
    }

    /**
     * @return A Post with the same values/data as this
     */
    public Post toPost(){
        return new Post(this.id,this.author ,this.text, this.tags, this.radius, this.latitude, this.longitude, this.time, this.likes);
    }
}
