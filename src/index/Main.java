
package index;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Imaginatio
 */
public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        try {
            menu.menu();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
