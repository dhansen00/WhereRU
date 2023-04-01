import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static Connection getRemoteConnection() {
        try {
            //have to install postgresql jdbc and add it to the workspace
            Class.forName("org.postgresql.Driver");

            String dbName = "postgres";
            String userName = "postgres";
            String password = "password1";
            String hostname = "whereru.cui0zqioehzd.us-east-2.rds.amazonaws.com";
            String port = "5432";
            String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
            //System.out.println("Getting remote connection with connection string from environment variables.");
            Connection con = DriverManager.getConnection(jdbcUrl);
            //System.out.println("Remote connection successful.");
            return con;
        }
        catch (ClassNotFoundException e) { 
            System.out.println(e.toString());
        }
        catch (SQLException e) { 
            System.out.println(e.toString());
        }
        return null;
    }

    public static ResultSet query(String psql){
        Connection conn = getRemoteConnection();
        try{
            Statement statement = conn.createStatement();
            return statement.executeQuery(psql);
        }
        catch (Exception e){
            return null;
        }
    }

    public static boolean insertPost(String username, String text, Long timeStamp, Double[] location, int radius, ArrayList<String> tags) throws Exception{
        //sets up psql code to be in the form "INSERT INTO table(col1,...) VALUES (?,...)"
        String psql = "INSERT INTO posts VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = getRemoteConnection();
        PreparedStatement st = conn.prepareStatement(psql);

        //code to get next available id
        ResultSet r = query("SELECT max(id) FROM posts;");
        int nextid = 888888;
        try{
            r.next();
            nextid = r.getInt(1);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        nextid++;
        st.setInt(1,nextid);
        st.setString(2,username);
        st.setString(3,text);
        st.setInt(4,radius);
        st.setInt(5,0);
        int tagCtr;
        for (tagCtr = 0; tagCtr < tags.size();tagCtr++){
            st.setString(5+tagCtr+1,tags.get(tagCtr));
        }
        for(;tagCtr < 5; tagCtr++){
            st.setString(tagCtr+5+1,null);
        }
        st.setDouble(11,location[0]);
        st.setDouble(12,location[1]);
        st.setLong(13,timeStamp); //timestamp should be of the format yyyyMMddhhmmss

        int inserted = st.executeUpdate();
        if (inserted == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean insertLogin(String[] values) throws Exception{
        //sets up psql code to be in the form "INSERT INTO table(col1,...) VALUES (?,...)"
        String psql = "INSERT INTO logins VALUES (";
        for (int i = 0; i < values.length; i++){
            if (i != 0){
                psql = psql + ",";
            }
            psql += "?";
        }
        psql = psql + ")";

        Connection conn = getRemoteConnection();
        PreparedStatement st = conn.prepareStatement(psql);
        for (int i = 0; i < values.length; i++){
            st.setString(i+1, values[i]);        
        }
        st.executeUpdate();

        //create an entry in accountLikes for the user
        PreparedStatement st2 = conn.prepareStatement("INSERT INTO \"accountLikes\" VALUES (?,?)");
        st2.setString(1, values[0]);
        st2.setInt(2, 0);
        int inserted = st2.executeUpdate();
        if (inserted == 1){
            return true;
        }
        else{
            return false;
        }
    }  
    
    public static boolean insertComment(String username,int postid,String text,Long timeStamp) throws Exception{
        String psql = "INSERT INTO comments VALUES (?,?,?,?,?,?)";

        //code to get next available id
        ResultSet r = query("SELECT max(id) FROM comments;");
        int nextid = 888888;
        try{
            r.next();
            nextid = r.getInt(1);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        nextid++;

        Connection conn = getRemoteConnection();
        PreparedStatement st = conn.prepareStatement(psql);

        st.setInt(1, nextid);
        st.setInt(2,postid);
        st.setString(3, text);
        st.setInt(4,0);
        st.setString(5, username);
        st.setLong(6, timeStamp);
        int inserted = st.executeUpdate();
        if (inserted != 1){
            return false;
        }
        return true;
    }

    public static boolean likePost(String authorUsername, int postid) throws Exception{
        Connection conn = getRemoteConnection();
        //increments the posts like ctr by 1
        PreparedStatement st = conn.prepareStatement("UPDATE posts SET likes = likes + 1 WHERE id = " + postid + " AND author = '" + authorUsername + "';");
        int changed = st.executeUpdate();
        if (changed != 1){ //if no rows are updated then return false *only one should be
            return false;
        }

        //increments the post authors like ctr by 1
        st = conn.prepareStatement("UPDATE \"accountLikes\" SET likes = likes + 1 WHERE username = '" + authorUsername + "';");
        changed = st.executeUpdate();
        if (changed != 1){ //if no rows are updated then return false *only one should be
            return false;
        }
        return true;
    }

    public static boolean likeComment(String authorUsername, int commentid) throws Exception{
        Connection conn = getRemoteConnection();
        //increments the comments like ctr by 1
        PreparedStatement st = conn.prepareStatement("UPDATE comments SET likes = likes + 1 WHERE id = " + commentid + " AND author = '" + authorUsername + "';");
        int changed = st.executeUpdate();
        if (changed != 1){ //if no rows are updated then return false *only one should be
            return false;
        }

        //increments the comment authors like ctr by 1
        st = conn.prepareStatement("UPDATE \"accountLikes\" SET likes = likes + 1 WHERE username = '" + authorUsername + "';");
        changed = st.executeUpdate();
        if (changed != 1){ //if no rows are updated then return false *only one should be
            return false;
        }
        return true;
    }

    public static boolean dislikePost(String authorUsername, int postid) throws Exception{
        Connection conn = getRemoteConnection();
        //increments the posts like ctr by 1
        PreparedStatement st = conn.prepareStatement("UPDATE posts SET likes = likes - 1 WHERE id = " + postid + " AND author = '" + authorUsername + "';");
        int changed = st.executeUpdate();
        if(changed != 1){ //if no rows are updated then return false *only one should be
            return false;
        }
        
        //increments the post authors like ctr by 1
        st = conn.prepareStatement("UPDATE \"accountLikes\" SET likes = likes - 1 WHERE username = '"+authorUsername+"';");
        changed = st.executeUpdate();
        if(changed != 1){ //if no rows are updated then return false *only one should be
            return false;
        }
        return true;
    }

    public static boolean dislikeComment(String authorUsername, int commentid) throws Exception{
        Connection conn = getRemoteConnection();
        //increments the comments like ctr by 1
        PreparedStatement st = conn.prepareStatement("UPDATE comments SET likes = likes - 1 WHERE id = " + commentid + " AND author = '" + authorUsername + "';");
        int changed = st.executeUpdate();
        if(changed != 1){
            return false;
        }

        //increments the comment authors like ctr by 1
        st = conn.prepareStatement("UPDATE \"accountLikes\" SET likes = likes - 1 WHERE username = '" + authorUsername + "';");
        changed = st.executeUpdate();
        if(changed != 1){
            return false;
        }
        return true;
    }

    public static boolean deleteComment(String authorUsername, int commentid) throws Exception{
        Connection conn = getRemoteConnection();
        
        //find out how many likes a comment has
        ResultSet r = query("SELECT likes FROM comments WHERE id = " + commentid + ";");
        r.next();
        int deletedLikes = r.getInt(1);
        System.out.println(deletedLikes);
        //remove those likes from the author
        PreparedStatement st = conn.prepareStatement("UPDATE \"accountLikes\" SET likes = likes - " + deletedLikes + " WHERE username = '" + authorUsername + "';");
        int changed = st.executeUpdate();
        if(changed != 1){
            return false;
        }

        //delete the comment
        st = conn.prepareStatement("DELETE FROM comments WHERE id = " + commentid + ";");
        changed = st.executeUpdate();
        if(changed != 1){
            return false;
        }
        return true;
    }

    public static boolean deletePost(String authorUsername, int postid) throws Exception{
        //find out how many likes a comment has
        ResultSet r = query("SELECT likes FROM posts WHERE id = " + postid + ";");
        r.next();
        int deletedLikes = r.getInt(1);

        Connection conn = getRemoteConnection();

        //delete the comment
        PreparedStatement st = conn.prepareStatement("DELETE FROM posts WHERE id = " + postid + " AND author = '" + authorUsername + "';");
        int changed = st.executeUpdate();
        if (changed != 1){
            return false;
        }

        //remove those likes from the author
        st = conn.prepareStatement("UPDATE \"accountLikes\" SET likes = likes - " + deletedLikes + " WHERE username = '" + authorUsername + "';");
        changed = st.executeUpdate();
        if (changed != 1){
            return false;
        }

        return true;
    }

    public static int getLikes(int postid) throws Exception{
        int likes;
        ResultSet r = query("SELECT likes FROM posts WHERE postid = " + postid + ";");
        r.next();
        likes = r.getInt(1);
        return likes;
    }

    public static ArrayList<Post> getNearbyPosts(Double currLatitude, Double currLongitude, int radius){
        // 1mile = 0.01831501831 degrees of longitude
        // 1mile = 0.01449275362 degrees of latitude

        //Collect all nearby posts within a box with the radius given
        Double mileToLat = 0.01449275362;
        Double mileToLon = 0.01831501831;
        Double maxLatitude = currLatitude + (mileToLat * radius);
        Double minLatitude = currLatitude - (mileToLat * radius);
        Double maxLongitude = currLongitude + (mileToLon * radius);
        Double minLongitude = currLongitude - (mileToLon * radius);
        ResultSet r = query("SELECT * FROM posts p WHERE p.latitude <= "+ maxLatitude + " AND p.latitude >= " + minLatitude + " AND p.longitude <= " + maxLongitude + " AND p.longitude >= " + minLongitude + " LIMIT 50;");
        ArrayList<Post> posts = new ArrayList<Post>();
        try {
            while (r.next()){
                int postid = r.getInt(1);
                String postAuthor = r.getString(2);
                String postText = r.getString(3);
                int postRadius = r.getInt(4);
                int postLikes = r.getInt(5);
                ArrayList<String> postTags = new ArrayList<String>();
                for (int i = 6; i < 11;i++){
                    String tag = r.getString(i);
                    if (tag != null){
                        postTags.add(tag);
                    }
                }
                Double postLatitude = r.getDouble(11);
                Double postLongitutde = r.getDouble(12);
                Long postTime = r.getLong(13);
                
                //Ensure post fits within radius and is not extraneous
                if (latLonDistance(currLatitude,currLongitude,postLatitude,postLongitutde) <= radius){
                    //Create post object and add to list
                    Post curr = new Post(postid, postAuthor, postText,postTags,postRadius,postLatitude,postLongitutde,postTime,postLikes);
                    posts.add(curr);
                }

                
            }
        } catch (Exception e) {
            return null;
        }

        return posts;
        
    }

    private static Double latLonDistance(Double lat1, Double lon1, Double lat2, Double lon2){
        //Haversine Formula
        double R = 6371.0; // Earth's radius in kilometers
    
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        
        //Convert km to miles
        distance = distance / 1.609;
        return distance;
    }

    public static ArrayList<Comment> getComments(int postid) throws Exception {
        ResultSet r = query("SELECT * FROM comments WHERE postid = " + postid + ";");

        ArrayList<Comment> comments = new ArrayList<Comment>();
        while(r.next()){
            int commentid = r.getInt(1);
            int parentid = postid;
            String text = r.getString(3);
            int likes = r.getInt(4);
            String author = r.getString(5);
            Long time = r.getLong(6);

            //create comment object
            Comment curr =  new Comment(commentid,parentid,author,text,time,likes);
            //add to list
            comments.add(curr);
        }
        return comments;
    }
}
