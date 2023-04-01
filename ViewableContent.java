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
        this.sortMethod = "time";
        this.updateContent();
    }

    public ArrayList<Content> showContent(){
        this.updateContent();
        
        ArrayList<Content> shownContent = new ArrayList<Content>();
        for(int i = 0; i < this.posts.size(); i++){
            shownContent.add(this.posts.get(i));
            if(commentDisplay.contains(this.posts.get(i).getId())){
                //retrieve and add relevent comments
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
        this.commentDisplay.remove(postid);
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