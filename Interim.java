import java.util.ArrayList;
import java.util.Scanner;

public class Interim {
    public static void main(String[] args) { 
        System.out.println("Log in-- Username:");
        Scanner scanner = new Scanner(System.in);

        // "username"
        String givenUsername = scanner.nextLine();


        // "password"
        System.out.println("Log in-- Password:");
        String givenPassword = scanner.nextLine();

        User account = Account.signIn(givenUsername, givenPassword);
        User p1 = Account.createAccount("Bernie Sandpaper", "Society");
        User p2 = Account.createAccount("Lochness Monster", "ThaBends");
        System.out.println(account);
        
        ViewableContent vc = new ViewableContent(account);
        ViewableContent vc1 = new ViewableContent(p1);
        ViewableContent vc2 = new ViewableContent(p2);

        String text = "Not feeling my greatest. Will listen to the Clown Polka (by Joe Mosti)";
        String text2 = "There's a dollar coin under the stool!";
        String text3 = "What a beautiful turtle here!";

        boolean y =  vc1.makePost(text, 4, null);
        boolean y2 = vc2.makePost(text2, 3, null);
        boolean y3 = vc2.makePost(text3, 5, null);
        System.out.println(y + " " + y2 + " " + y3);
        

        ArrayList<Content> currList = vc.showContent();
        
        scanner.close();

        for (int i = 0; i < currList.size(); i++){
            System.out.println(currList.get(i));
        }

    }
}
