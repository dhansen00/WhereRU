import java.util.ArrayList;
import java.util.Comparator;

//generic class, to be extended into post and comment
public class Content{
    protected int id;
    protected String text;
    protected String username;
    protected Long posttime;   
    protected int likes;
    protected Boolean isPost;

    public Boolean isPost(){
        return this.isPost;
    }

    public int getId(){
        return this.id;
    }

    public String getContent(){
        return this.text;
    }

    public String getUsername(){
        return this.username;
    }

    public Long getPostTime(){
        return this.posttime;
    }

    public int getLikes(){
        return this.likes;
    }

    public void like(){}
    public void dislike(){}
    public void updateDistance(double lat, double lon){}
    public double getDistance(){
        return 0.0;
    }
    public ArrayList<String> getTags(){
        return new ArrayList<String>();
    }
}

//comparator for sorting content by time
class ContentTimeComparator implements Comparator<Content>{
    @Override
    public int compare(Content c1, Content c2){
        return (int)(c1.getPostTime() - c2.getPostTime());
    }
}

//comparator for sorting content by likes
class ContentLikeComparator implements Comparator<Content>{
    @Override
    public int compare(Content c1, Content c2){
        return c2.getLikes() - c1.getLikes();
    }
}