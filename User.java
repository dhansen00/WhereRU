public class User {
    private String username = null;
    private int likes = 0;

    public User (String user,int internetPoints){
        username = user;
        likes = internetPoints;
    } // constructor

    public String getUsername(){
        return new String(username);
    }

    public Double[] getLocation(){
        Double[] location = new Double[2];
        location[0] = 0.0;
        location[1] = 0.0;
        //location[0] = 38.897957;
        //location[1] = -77.03656;
        return location;
    } // hardcoding user location for testing

    public int getLikes(){
        int ret = likes;
        return ret;
    }
}
