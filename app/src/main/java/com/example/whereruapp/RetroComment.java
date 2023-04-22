package com.example.whereruapp;

import com.google.gson.annotations.SerializedName;

/**
 * A class that is used to interpret the output from RetroFit
 */
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

    /**
     * Constructor for RetroComment
     * @param i     the id of the comment
     * @param i1    the postid associated with the comment
     * @param s     the username of the user making the comment
     * @param s1    the text being posted with the comment
     * @param i2    the like count of the comment
     * @param l     the time the comment was made
     */
    public RetroComment(int i,int i1, String s, String s1, int i2, long l){
        this.id = i;
        this.postid = i1;
        this.author = s;
        this.text = s1;
        this.likes = i2;
        this.time = l;
    }

    /**
     * @return A comment that contains the same data as the RetroComment
     */
    public Comment toComment(){
        return new Comment(this.id, this.postid, this.author, this.text, this.time, this.likes);
    }
}
