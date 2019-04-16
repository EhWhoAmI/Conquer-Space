package ConquerSpace.util;

import static ConquerSpace.ConquerSpace.localeMessages;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 * The one class I hope no one sees.
 * @author Zyun
 */
public class ExceptionHandling {
    /**
     * Takes in a string and exception. Has a message box for the user and makes
     * a crash dump.
     * @param what Your own message. 
     * @param ex Exception that caused it.
     */
    public static void ExceptionMessageBox(String what, Exception ex){
        PrintWriter writer = null;
        try {
            String header = "Something has gone wrong with Conquer Space\n"
                    + "We are sorry for the inconvinence.\n"
                    + "Restarting could help.\n"
                    + "Here's some information for the developers.\n\n"
                    + "Conquer Space v " + ConquerSpace.ConquerSpace.VERSION.toString() + "\n"
                    + "Build: " + ConquerSpace.ConquerSpace.BUILD_NUMBER + "\n\n" + what;
            JOptionPane.showMessageDialog(null, String.format(localeMessages.getMessage("errorhandlingheader"), ConquerSpace.ConquerSpace.VERSION, ConquerSpace.ConquerSpace.VERSION) + "\n\n" + what, ex.getClass().getName() + ": " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            //Create dump file
            File file = new File("crashlog.txt");
            if (!file.exists())file.createNewFile();
            writer = new PrintWriter(file);
            writer.println(ex.getClass().getName() + ": " + ex.getMessage());
            writer.println();
            writer.print(header.replace("\n", System.getProperty("line.separator")));
            writer.print("\n\n Stack trace: \n\n".replace("\n", System.getProperty("line.separator")));
            ex.printStackTrace(writer);
            writer.println();
            writer.close();
        } catch (FileNotFoundException ex1) {
            //Wat. Problem happens in a problem.......
        } catch (IOException ex1) {
            //Same as above. Just exit it later... DAMMIT
        } finally {
            writer.close();
        }
        
    }
    
    /**
     * Takes in a string and exception. Has a message box for the user and makes
     * a crash dump.
     * @param what Your own message. 
     * @param ex Exception that caused it.
     */
    public static void ExceptionMessageBox(String what, Throwable ex){
        PrintWriter writer = null;
        try {
            String header = "Something has gone wrong with Conquer Space\n"
                    + "We are sorry for the inconvinence.\n"
                    + "Restarting could help.\n"
                    + "Here's some information for the developers.\n\n"
                    + "Conquer Space v " + ConquerSpace.ConquerSpace.VERSION.toString() + "\n"
                    + "Build: " + ConquerSpace.ConquerSpace.BUILD_NUMBER + "\n\n" + what;
            JOptionPane.showMessageDialog(null, String.format(localeMessages.getMessage("errorhandlingheader"), ConquerSpace.ConquerSpace.VERSION, ConquerSpace.ConquerSpace.VERSION) + "\n\n" + what, ex.getClass().getName() + ": " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            //Create dump file
            File file = new File("crashlog.txt");
            if (!file.exists())file.createNewFile();
            writer = new PrintWriter(file);
            writer.println(ex.getClass().getName() + ": " + ex.getMessage());
            writer.println();
            writer.print(header.replace("\n", System.getProperty("line.separator")));
            writer.print("\n\n Stack trace: \n\n".replace("\n", System.getProperty("line.separator")));
            ex.printStackTrace(writer);
            writer.println();
            writer.close();
        } catch (FileNotFoundException ex1) {
            //Wat. Problem happens in a problem.......
        } catch (IOException ex1) {
            //Same as above. Just exit it later... DAMMIT
        } finally {
            writer.close();
        }
        
    }
}
