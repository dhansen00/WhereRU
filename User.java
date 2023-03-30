public class User {
    private String username = null;
    private int likes = 0;

    public User (String user,int internetPoints){
        username = user;
        likes = internetPoints;
    }
    /* 
    public void makePost(String text, Double[] location, int radius, String[] tags){
        Calendar cal = Calendar.getInstance();
        java.util.Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        int postTime = Integer.parseInt(dateFormat.format(date));
        try{
            Database.insertPost(username, text, postTime, location, radius, tags);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        //return post object?
    }
    */
    public String getUsername(){
        return new String(username);
    }

    public Double[] getLocation(){
        Double[] location = new Double[2];
        location[0] = 0.0;
        location[1] = 0.0;
        return location;
    }

    public int getLikes(){
        int ret = likes;
        return ret;
    }
}
