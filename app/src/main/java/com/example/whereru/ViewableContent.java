package com.example.whereru;

import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

public class ViewableContent{
    private int viewRadius;
    private String sortMethod;
    private ArrayList<Content> content = new ArrayList<Content>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<Integer> commentDisplay = new ArrayList<Integer>();
    private User user;

    public ViewableContent(User user){
        this.user = user;
        this.sortMethod = "distance";
        this.viewRadius = 50;
    }

    public ArrayList<Content> showContent(){
        this.updateContent();
        ArrayList<Content> shownContent = new ArrayList<Content>();
        for(int i = 0; i < this.posts.size(); i++){
            shownContent.add(this.posts.get(i));
            if(commentDisplay.contains(this.posts.get(i).getId())){
                try {
                    ArrayList<Comment> comments = Database.getComments(this.posts.get(i).getId());
                    for(int j = 0; j < comments.size(); j++){
                        shownContent.add(comments.get(j));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
            }
        }
        return shownContent;
    }

    public void updateRadius(int newRadius){
        this.viewRadius = newRadius;
        this.updateContent();
    }

    public void showComment(int postid){
        this.commentDisplay.add(postid);
    }

    public void hideComment(int postid){
        this.commentDisplay.remove(this.commentDisplay.indexOf(postid));
    }

    public boolean makePost(String text, int radius, ArrayList<String> tags){
        Date currentDate = new Date();
        Long longdate = currentDate.getTime();
        Double[] loc = user.getLocation();
        try{
            Database.insertPost(user.getUsername(), text, longdate, loc, radius, tags);
            updateContent();
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean makeComment(int postId, String text){
        Date currentDate = new Date();
        Long longdate = currentDate.getTime();
        try {
            Database.insertComment(user.getUsername(), postId, text, longdate);
            updateContent();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void updateContent(){
        Double[] pos = user.getLocation();
        this.posts = Database.getNearbyPosts(pos[0], pos[1], this.viewRadius);
        this.sort();
    }

    public void updateSort(String newSort){
        this.sortMethod = newSort;
        this.sort();
    }

    public void updateDistances(){
        Double[] loc = this.user.getLocation();
        for(int i = 0; i < this.posts.size(); i++){
            this.posts.get(i).updateDistance(loc[0], loc[1]);
        }
    }

    private void sort(){
        switch (this.sortMethod) {
            case "time":
                Collections.sort(this.posts, new ContentTimeComparator());
                break;
            case "likes":
                Collections.sort(this.posts, new ContentLikeComparator());
                break;
            case "distance":
                this.updateDistances();
                Collections.sort(this.posts, new PostDistanceComparator());
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting method");
        }  
    }
    /*
    public static void main(String[] args){
        Date date = new Date();
        long longdate = date.getTime();
        System.out.println(longdate);
        User user = Account.signIn("test1", "test1");
        System.out.println(user.getUsername());
        ViewableContent content = new ViewableContent(user);
        ArrayList<Content> shown = content.showContent();
        for(int i = 0; i < shown.size(); i++){
            if(!shown.get(i).isPost()){
                System.out.println(shown.get(i).getLikes());
                shown.get(i).dislike();
                System.out.println(shown.get(i).getLikes());
            }
            System.out.println(shown.get(i).getContent());
        }
    }
    */
}