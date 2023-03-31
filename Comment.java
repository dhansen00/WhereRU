public class Comment extends Content {
    private int postId;


    public Comment(int id, int postId, String username, String text, Long posttime, int likes, int dislikes){
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.text = text;
        this.posttime = posttime;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public int getPostId(){
        return this.postId;
    }
}
