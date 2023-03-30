import java.util.ArrayList;
import java.util.Comparator;

public class Post extends Content{
    private double latitude;
    private double longitude;
    private double lastKnownDist;
    private int radius;
    private ArrayList<String> tags = new ArrayList<String>();

    public Post(int id, String username, String text, ArrayList<String> tags, int radius, Double lat, Double lon, Long posttime,int likes){
        this.id = id;
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

    public void updateDistance(double lat, double lon){
        double R = 6371.0; // Earth's radius in kilometers
    
        double dLat = Math.toRadians(lat - this.latitude);
        double dLon = Math.toRadians(lon - this.longitude);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(lat)) *
               Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        this.lastKnownDist = (int)(distance * 1000);
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

