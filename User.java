public class User {
    public String username = null;

    public User (String user){
        username = user;
    }
    public void makePost(String text, Double[] location, int radius, String[] tags){
        //get current time
        //Database.insertPost(username, text, "TIME", location, radius, tags);
    }
    public void makeComment(){
        //Make comment
    }

    public void getLocation(){
        //Get location
    }

    public void getRecentPosts(){
        //Get recent posts
    }
    
    public void getLikedPosts(){
        //Get liked posts
    }
    
}
