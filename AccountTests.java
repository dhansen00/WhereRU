import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;


public class AccountTests {

    User account = Account.createAccount("username", "password");

    /*@Test
    public void CreateAccount() {
        assertEquals("username", account.getUsername());
    }*/

    @Test
    public void login() {
        User login_test = Account.signIn("Jose","joseiscool");
        assert(login_test.getUsername().equals("Jose"));
        System.out.print("Username: " + login_test.getUsername() + "\nLikes: " + login_test.getLikes());
    }

    @Test
    public void testNearbyPosts() {
        User login_test = Account.signIn("please","work");
        ViewableContent views = new ViewableContent(login_test);
        ArrayList<String> tags = new ArrayList<String>();
        //views.makePost("Middle of the ocean", 100, tags);
        views.updateRadius(50);
        //shown_content = views.showContent();

    }



}