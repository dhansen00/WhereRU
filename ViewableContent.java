import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

public class ViewableContent{
    private int viewRadius;
    private String sortMethod;
    private ArrayList<Content> content = new ArrayList<Content>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private User user;


    public ArrayList<Content> showContent(){
        return this.content;
    }

    public void updateRadius(int newRadius){
        this.viewRadius = newRadius;
        this.updateContent();
    }

    public boolean makePost(String text, int radius, ArrayList<String> tags){
        Date currentDate = new Date();
        Long longdate = currentDate.getTime();
        Double[] loc = user.getLocation();
        try{
            Database.insertPost(user.getUsername(), text, longdate, loc, radius, tags);
            updateContent();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        //return post object?
        return false;
    }

    public void updateContent(){
        Double[] pos = user.getLocation();
        Database.getNearbyPosts(pos[0], pos[1], viewRadius);
    }

    public void updateSort(String newSort){
        this.sortMethod = newSort;
        this.sort();
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
                // to be implemented
            default:
                throw new IllegalArgumentException("Invalid sorting method");
        }  
    }
    public static void main(String[] args){
        Date date = new Date();
        long longdate = date.getTime();
        System.out.println(longdate);
    }
}