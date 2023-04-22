package com.example.whereruapp;//import java.lang.Math;

public class Content{
    protected int id = 0;
    protected String text = null;
    protected String username = null;
    protected Long posttime = null;
    protected int likes = 0;
    protected int dislikes = 0;

    /**
     * @return the id of this
     */
    public int getId(){
        return this.id;
    }

    /**
     * @return the text that this content displays
     */
    public String getContent(){
        return this.text;
    }

    /**
     * @return the username that posted this content
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * @return the time that the content was posted
     */
    public Long getPostTime(){
        return this.posttime;
    }

    /**
     * @return the likes that this content has obtained
     */
    public int getLikes(){
        return this.likes;
    }
}