package com.example.whereruapp;

/**
 * A class that extends content that represents a comment in the db
 */
public class Comment extends Content {
    private int postId;

    /**
     * Constructor for the comment class
     * @param id        the comment id of the comment
     * @param postId    the postid associated with the comment
     * @param username  the username that posted the comment
     * @param text      the text that the comment contains
     * @param posttime  the time the comment was made
     * @param likes     the amount of likes the comment has received
     */
    public Comment(int id, int postId, String username, String text, Long posttime, int likes){
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.text = text;
        this.posttime = posttime;
        this.likes = likes;
    }

    /**
     * @return this comments post id
     */
    public int getPostId(){
        return this.postId;
    }

}
