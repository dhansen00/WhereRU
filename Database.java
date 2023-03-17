import java.sql.*;

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

    public static void insertPost(String username, String text, int timeStamp, Double[] location, int radius, String[] tags) throws Exception{
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
        for (tagCtr = 0; tagCtr < tags.length;tagCtr++){
            st.setString(5+tagCtr+1,tags[tagCtr]);
        }
        for(;tagCtr < 5; tagCtr++){
            st.setString(tagCtr+5+1,null);
        }
        st.setDouble(11,location[0]);
        st.setDouble(12,location[1]);
        st.setInt(13,timeStamp); //timestamp should be of the format yyyyMMddhhmmss

        st.executeUpdate();
    }

    public static void insertLogin(String[] values) throws Exception{
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
        st.setString(1, values[0]);
        st.setInt(2, 0);
        st2.executeUpdate();
    }  
    
    public static void insertComment(){}

    public static void likePost(){}
    
    public static void dislikePost(){}

    public static void deleteComment(){}

    public static void deletePost(){}
}
