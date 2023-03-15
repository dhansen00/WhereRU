public class ViewableContent{
    private static String username;
    private int viewRadius;
    private String sortMethod;
    private Bool commentViewable;
    private List<Content> content = new ArrayList<Content>();


    public ArrayList<Content> showContent(){
        return self.content;
    }

    public void updateContent(){

    }

    public void updateSort(String newSort){
        self.sortMethod = newSort;
        self.sort();
    }

    private void sort(){
        switch (self.sortMethod) {
            case "time":
                Collections.sort(self.content, new ContentTimeComparator());
                break;
            case "likes":
                Collections.sort(self.content, new ContentLikeComparator());
                break;
            case "distance":
                // to be implemented
            default:
                throw new IllegalAgumentException("Invalid sorting method");
        }  
    }

}