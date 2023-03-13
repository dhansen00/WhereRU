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
            System.out.println("Getting remote connection with connection string from environment variables.");
            Connection con = DriverManager.getConnection(jdbcUrl);
            System.out.println("Remote connection successful.");
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

    public static void insertInt(String table,String[] tableCols,int[] values) throws Exception{
        //sets up psql code to be in the form "INSERT INTO table(col1,...) VALUES (?,...)"
        String psql = "INSERT INTO " + table + "(";
        for (int i = 0; i < tableCols.length;i++){
            if (i != 0){
                psql = psql + ",";
            }
            psql += tableCols[i];
        }
        psql = psql + ") VALUES (";
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
            st.setInt(i+1, values[i]);        
        }
        st.executeUpdate();
    }

    public static void insertString(String table,String[] tableCols,String[] values) throws Exception{
        //sets up psql code to be in the form "INSERT INTO table(col1,...) VALUES (?,...)"
        String psql = "INSERT INTO " + table + "(";
        for (int i = 0; i < tableCols.length;i++){
            if (i != 0){
                psql = psql + ",";
            }
            psql += tableCols[i];
        }
        psql = psql + ") VALUES (";
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
    }
    /* 
    public static void main(String[] args){
        String[] tableCols = {"username","password"};
        String[] values = {"coolguy69","12345"};
        String table = "logins";
        try{
            insertString(table,tableCols,values);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        String psql = "SELECT * FROM logins";
        ResultSet rs = query(psql);
        System.out.println("username | password");
        try{
            while (rs.next()){
                    String res1 = rs.getString(1);
                    String res2 = rs.getString(2);
                    
                    System.out.println(res1 + '|' + res2);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    */
}
