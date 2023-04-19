package com.example.whereruapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static User signIn(String givenUsername, String givenPassword){
        try {
            URL url = new URL("http://localhost:3300/login/"+givenUsername+"/"+givenPassword);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            System.out.println("adsfafdsaf");
            conn.setRequestProperty("Accept", "application/json");
            System.out.println("adsfafdsaf");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            System.out.println("HERE");
            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            String resString = result.toString();
            if (resString.equals("[]")){
                return null;
            }


            conn.disconnect();

        } catch (MalformedURLException e) {
            System.out.println("malformed");
            return null;
        } catch (IOException e) {
            System.out.println("ioexcept");
            e.printStackTrace();
            return null;
        }

        System.out.println("Sign in successfull.");
        ResultSet r = Database.query("SELECT likes FROM \"accountLikes\" WHERE username = '" + givenUsername + "';");
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

    public static void main(String[] args) {
        User test = signIn("Jose","joseiscol");
        if (test == null){
            System.out.println(":(");
        }
        System.out.println(":)");
    }
}