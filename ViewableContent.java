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
        /*
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("test");
        Long time = (long) 10000;
        this.posts.add(new Post(1,"alice","hi",tags, 50, (Double)0.0,(Double)0.0, time, 5));
        this.posts.add(new Post(2, "bob", "hey", tags, 40, (Double)0.0, (Double)0.0, (long)100000, 6));
        this.commentDisplay.add(1);
        this.posts.get(0).updateDistance((Double) 1.0, (Double)1.0);
        this.posts.get(1).updateDistance((Double)2.0,(Double)2.0);
        this.sort();
        this.updateContent();
        */
    }

    public ArrayList<Content> showContent(){
        this.updateContent();

        ArrayList<Content> shownContent = new ArrayList<Content>();
        for(int i = 0; i < this.posts.size(); i++){
            shownContent.add(this.posts.get(i));
            if(commentDisplay.contains(this.posts.get(i).getId())){
                //shownContent.add(new Comment(1, 1, "charlie", "sup", (long)10000, 5));
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
                Collections.sort(this.posts, new PostDistanceComparator());
                break;
                // to be implemented
            default:
                throw new IllegalArgumentException("Invalid sorting method");
        }  
    }
    public static void main(String[] args){
        Date date = new Date();
        long longdate = date.getTime();
        System.out.println(longdate);
        /*
        ViewableContent content = new ViewableContent();
        ArrayList<Content> shown = content.showContent();

        for(int i = 0; i < shown.size(); i++){
            System.out.println(shown.get(i).getContent());
        }
        */
    }
}