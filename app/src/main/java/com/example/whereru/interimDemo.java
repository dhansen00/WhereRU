package com.example.whereru;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class interimDemo {
    

    public static void main(String args[]){
        Scanner reader = new Scanner(System.in);
        User user = null;

        while(user == null){
            System.out.println("Welcome to WhereRU! Do you want to create an account or login?");
            System.out.print("Enter c for account creation, or l for login: ");
            String option = reader.nextLine();
            if(option.equals("c")){
                System.out.print("Please enter the username for your new account: ");
                String username = reader.nextLine();
                System.out.print("Please enter the password for your new account: ");
                String password = reader.nextLine();
                user = Account.createAccount(username, password);
            }else if (option.equals("l")){
                System.out.print("Please enter your username: ");
                String username = reader.nextLine();
                System.out.print("Please enter your password: ");
                String password = reader.nextLine();
                user = Account.signIn(username, password);          
            }
            if(user == null){
                System.out.println("Something went wrong, please try again.\n");
            }else{
                System.out.println("Welcome, " + user.getUsername() + "!\n");
            }
        }

        ViewableContent content = new ViewableContent(user);

        while(true){
            ArrayList<Content> shown = content.showContent();
            for(int i = 0; i < shown.size(); i++){
                Content c = shown.get(i);
                if(c.isPost()){
                    System.out.println();
                    Double[] loc = user.getLocation();
                    c.updateDistance(loc[0], loc[1]);
                    System.out.println("Item: "+(i+1));
                    System.out.println(c.getUsername() + " posted:");
                    System.out.println("     "+c.getContent());
                    System.out.print("Distance: "+c.getDistance()+"m     Likes: "+c.getLikes());
                    if(c.getTags().size() != 0){
                        System.out.print("     Tags: " + c.getTags().get(0));
                        for(int j = 1; j < c.getTags().size(); j++){
                            System.out.print(", "+c.getTags().get(j));
                        }
                        System.out.println();
                    }
                }else{
                    System.out.println("     Item: "+(i+1));
                    System.out.println("     "+c.getUsername()+" commented:");
                    System.out.println("          "+c.getContent());
                    System.out.println("     Likes: "+c.getLikes());
                }
            }
            System.out.println("\nEnter s for showing comments, h for hiding comments, r for viewing radius, l to like, d to dislike,");
            System.out.print("c to comment, or p to post, followed by the appropriate item number or value: ");
            String choice = reader.nextLine();
            Character character = choice.charAt(0);
            int value = Integer.parseInt(choice.substring(1));
            switch(character){
                case 's':
                    content.showComment(shown.get(value-1).getId());
                    break;
                case 'h':
                    content.hideComment(shown.get(value-1).getId());
                    break;
                case 'r':
                    content.updateRadius(value);
                    break;
                case 'l':
                    shown.get(value-1).like();
                    break;
                case 'd':
                    shown.get(value-1).dislike();
                    break;
                case 'c':
                    System.out.print("Enter the text of your comment: ");
                    String text = reader.nextLine();
                    content.makeComment(shown.get(value-1).getId(), text);
                    break;
                case 'p':
                    System.out.print("Enter the text of your post: ");
                    String newtext = reader.nextLine();
                    System.out.print("Enter your tags (if any) separated by commas: ");
                    String tags = reader.nextLine();
                    ArrayList<String> procTags = new ArrayList<String>();
                    Collections.addAll(procTags, tags.split(","));

                    System.out.print("Enter the visible radius of your post: ");
                    String rad = reader.nextLine();
                    int radius = Integer.parseInt(rad);

                    content.makePost(newtext, radius, procTags);
                    break;
                default:
                    System.out.println("Not a valid command!");
            }    
            System.out.println("\n\n\n\n\n");
        }
    }
}
