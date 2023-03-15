import java.lang.Math;

public class Content{
    private static String text;
    private static String username;
    private static int posttime;   
    private int likes;
    private int dislikes;


    public String getContent(){
        return self.text;
    }

    public String getUsername(){
        return self.username;
    }

    public int getPostTime(){
        return self.posttime;
    }

    public int getLikes(){
        return self.likes;
    }

    public int getDislikes(){
        return self.dislikes;
    }

    public void like(){

    }

    public void dislike(){

    }
}

public Class ContentTimeComparator implements Comparator<Content>{
    public int compare(Content c1, Content c2){
        return c1.getPostTime() - c2.getPostTime();
    }
}

public Class ContentLikeComparator implements Comparator<Content>{
    public int compare(Content c1, Content c2){
        return c1.getLikes() - c2.getLikes();
    }
}