import java.sql.*;

public class Account{

    public static User createAccount(String givenUsername, String givenPassword){
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
            String[] values = {givenUsername,givenPassword};
            try{
                Database.insertLogin(values);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("Account created successfully.");

            return new User(givenUsername,0);
        }
        else{   //The username is taken and therefore an account cannot be created
            System.out.print(givenUsername + " is already taken.");
            return null;
        }
        
    }

    public static User signIn(String givenUsername,String givenPassword){
        String psql = "SELECT password FROM logins WHERE username LIKE '" + givenUsername + "';";
        ResultSet r = Database.query(psql);
        String password = null;
        try{
            while (r.next()){
                password = r.getString(1);
                break;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        if (password == null || !password.equals(givenPassword)){
            System.out.println("Sign in Unsuccessfull.");
            return null;
        }
        System.out.println("Sign in successfull.");
        r = Database.query("SELECT likes FROM \"accountLikes\" WHERE username = '" + givenUsername + "';");
        int likes = 0;
        try{
            r.next();
            likes = r.getInt(1);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return new User(givenUsername,likes);

    }
}