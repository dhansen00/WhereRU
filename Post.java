import java.util.ArrayList;
import java.util.Comparator;

public class Post extends Content{
    private double latitude;
    private double longitude;
    private double lastKnownDist;
    private int radius;
    private ArrayList<String> tags = new ArrayList<String>();

    public Post(String username, String text, ArrayList<String> tags, int radius, double lat, double lon, int posttime,int likes){
        this.username = username;
        this.text = text;
        this.tags = tags;
        this.radius = radius;
        this.latitude = lat;
        this.longitude = lon;
        this.posttime = posttime;
        this.likes = 0;
        this.dislikes = 0;
        this.lastKnownDist = 0;
    }

    public double getDistance(){
        return this.lastKnownDist;
    }

    public int getRadius(){
        return this.radius;
    }

    public ArrayList<String> getTags(){
        return this.tags;
    }

    public double updateDistance(double lat, double lon){
        this.lastKnownDist =
        Math.acos(Math.sin(lat)*Math.sin(latitude)+Math.cos(lat)*Math.cos(latitude)*Math.cos(longitude-lon))*6371000;
        return this.lastKnownDist;
    }

    public void like(){
        try{
            if(Database.likePost(this.username, this.id)){
                this.likes += 1;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }     
    }

    public void dislike(){
        try{
            if(Database.dislikePost(this.username, this.id)){
                this.dislikes += 1;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}

class PostDistanceComparator implements Comparator<Post>{
    @Override
    public int compare(Post post1, Post post2){
        return (int) (post1.getDistance() - post2.getDistance());
    }
}

