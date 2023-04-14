public class Comment extends Content {
    private int postId;


    public Comment(int id, int postId, String username, String text, Long posttime, int likes){
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.text = text;
        this.posttime = posttime;
        this.likes = likes;
        this.isPost = false;
    } // constructor

    public int getPostId(){
        return this.postId;
    }

    @Override
    public void like(){
        try{
            if(Database.likeComment(this.username, this.id)){
                this.likes += 1;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }     
    } // implements comment specific like behavior

    @Override
    public void dislike(){
        try{
            if(Database.dislikeComment(this.username, this.id)){
                this.likes -= 1;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    } // implements comment specific dislike behavior
}
