import java.sql.*;

public class Account{

    public static void createAccount(String givenUsername, String givenPassword){
        String psql = "SELECT username FROM logins WHERE username LIKE '" + givenUsername + "';";
        ResultSet r = Database.query(psql);
        String res1 = null;
        try{
            while (r.next()){
                res1 = r.getString(1);
                break;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        if (res1 == null){
            //Account can be created
            String[] tableCols = {"username","password"};
            String[] values = {givenUsername,givenPassword};
            String table = "logins";
            try{
                Database.insertString(table,tableCols,values);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("Account created successfully.");

            //add account to other tables
        }
        else{   //The username is taken and therefore an account cannot be created
            System.out.print(givenUsername + " is already taken.");
        }
        
    }

    public void signIn(){
        //Sign in
    }

    public void getLocation(){
        //Get location
    }

    public void makePost(){
        //Make post
    }

    public void getRecentPosts(){
        //Get recent posts
    }

    public void getLikedPosts(){
        //Get liked posts
    }

    public void makeComment(){
        //Make comment
    }

    public static void main(String [] args){
        createAccount("Jose","joseiscool");
    }
}