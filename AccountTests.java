
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class AccountTests {

    User account = Account.createAccount("username", "password");

    @Test
    public void CreateAccount() {
        assertEquals("username", account.getUsername());
    }

}