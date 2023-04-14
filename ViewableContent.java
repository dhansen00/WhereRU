import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

//Aggregator for posts and comments
public class ViewableContent{
    private int viewRadius;
    private String sortMethod;
    //private ArrayList<Content> content = new ArrayList<Content>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<Integer> commentDisplay = new ArrayList<Integer>();
    private User user;

    //constructor, takes in valid user
    public ViewableContent(User user){
        this.user = user;
        this.sortMethod = "distance";
        this.viewRadius = 50;
    }

    //formats all content visible to the user and returns it as an ArrayList
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
            }// adding comments after posts that need them
        }// adding all visible posts
        return shownContent;
    }

    public void updateRadius(int newRadius){
        this.viewRadius = newRadius;
        this.updateContent();
    } // changing the view radius and updating the valid content

    public void showComment(int postid){
        this.commentDisplay.add(postid);
    } // setting the comment flag for a post to visible

    public void hideComment(int postid){
        this.commentDisplay.remove(this.commentDisplay.indexOf(postid));
    } // setting the comment flag for a post to invisible

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
    } // formats input data, adds date and location, then adds the new post to the database

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
    } // formats input data, adds date, then adds the new comment to the database

    public void updateContent(){
        Double[] pos = user.getLocation();
        this.posts = Database.getNearbyPosts(pos[0], pos[1], this.viewRadius);
        this.sort();
    } // queries database for all posts visible to the user

    public void updateSort(String newSort){
        this.sortMethod = newSort;
        this.sort();
    } // changes the sorting method

    public void updateDistances(){
        Double[] loc = this.user.getLocation();
        for(int i = 0; i < this.posts.size(); i++){
            this.posts.get(i).updateDistance(loc[0], loc[1]);
        }
    } // updates the location of all posts to match current location

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
    } // sorts post accordign to correct method

}