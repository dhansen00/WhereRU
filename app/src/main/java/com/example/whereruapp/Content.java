package com.example.whereruapp;//import java.lang.Math;
import java.util.ArrayList;
import java.util.Comparator;

public class Content{
    protected int id;
    protected String text;
    protected String username;
    protected Long posttime;   
    protected int likes;
    protected int dislikes;
    protected Boolean isPost;

    public Boolean isPost(){
        return this.isPost;
    }

    public int getId(){
        return this.id;
    }

    public String getContent(){
        return this.text;
    }

    public String getUsername(){
        return this.username;
    }

    public Long getPostTime(){
        return this.posttime;
    }

    public int getLikes(){
        return this.likes;
    }

    public int getDislikes(){
        return this.dislikes;
    }

    public void like(){}
    public void dislike(){}
    public void updateDistance(double lat, double lon){}
    public double getDistance(){
        return 0.0;
    }
    public ArrayList<String> getTags(){
        return new ArrayList<String>();
    }
}

class ContentTimeComparator implements Comparator<Content>{
    @Override
    public int compare(Content c1, Content c2){
        return (int)(c1.getPostTime() - c2.getPostTime());
    }
}

class ContentLikeComparator implements Comparator<Content>{
    @Override
    public int compare(Content c1, Content c2){
        return c2.getLikes() - c1.getLikes();
    }
}