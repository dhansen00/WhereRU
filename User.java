import java.text.*;
import java.util.*;
import java.sql.*;

public class User {
    public String username = null;

    public User (String user){
        username = user;
    }
    public void makePost(String text, Double[] location, int radius, String[] tags){
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HHmmss");
        int postTime = Integer.parseInt(dateFormat.format(date));
        try{
            Database.insertPost(username, text, postTime, location, radius, tags);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        //return post object?
    }
    public void makeComment(){
        //Make comment
    }

    public void getLocation(){
        //Get location
    }

    public void getRecentPosts(){
        ResultSet r = Database.query("SELECT * FROM posts WHERE author = '"+ username +"' ORDER BY posts.id DESC;");
        //return an array of posts up to x amount of posts
    }
    
    public void getLikedPosts(){
        //Get liked posts
    }
    
}
