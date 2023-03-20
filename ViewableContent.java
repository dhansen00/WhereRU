import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewableContent{
    private String username;
    private int viewRadius;
    private String sortMethod;
    private Boolean commentViewable;
    private ArrayList<Content> content = new ArrayList<Content>();


    public ArrayList<Content> showContent(){
        return this.content;
    }

    public void updateContent(){

    }

    public void updateSort(String newSort){
        this.sortMethod = newSort;
        this.sort();
    }

    private void sort(){
        switch (this.sortMethod) {
            case "time":
                Collections.sort(this.content, new ContentTimeComparator());
                break;
            case "likes":
                Collections.sort(this.content, new ContentLikeComparator());
                break;
            case "distance":
                // to be implemented
            default:
                throw new IllegalArgumentException("Invalid sorting method");
        }  
    }

}