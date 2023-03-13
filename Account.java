import java.sql.*;

public class Account{

    public User createAccount(String givenUsername, String givenPassword){
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

            return createUser(givenUsername);
        }
        else{   //The username is taken and therefore an account cannot be created
            System.out.print(givenUsername + " is already taken.");
            return null;
        }
        
    }

    public User signIn(String givenUsername,String givenPassword){
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
        }

        if (password == null || !password.equals(givenPassword)){
            System.out.println("Sign in Unsuccessfull.");
            return null;
        }
        else{
            System.out.println("Sign in successfull.");
            return createUser(givenUsername);
        }

    }

    private User createUser(String givenUsername){
        User u = new User();
        u.username = givenUsername;
        return u;
    }

}