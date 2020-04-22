/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.util;

import ConquerSpace.ConquerSpace;
import static ConquerSpace.ConquerSpace.localeMessages;
import ConquerSpace.Globals;
import static ConquerSpace.gui.game.DebugStatsWindow.byteCountToDisplaySize;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 * The one class I hope no one sees.
 *
 * @author EhWhoAmI
 */
public class ExceptionHandling {

    /**
     * Takes in a string and exception. Has a message box for the user and makes
     * a crash dump.
     *
     * @param what Your own message.
     * @param ex Exception that caused it.
     */
    public static void ExceptionMessageBox(String what, Throwable ex) {
        int exit = 1;
        String header = getHeaderText(what);

        exit = JOptionPane.showConfirmDialog(null,
                String.format(localeMessages.getMessage("errorhandlingheader"),
                        ConquerSpace.VERSION,
                        ConquerSpace.VERSION)
                + "\n\n" + what + "\n\nDo you want to quit the game?",
                ex.getClass().getName() + ": " + ex.getMessage(),
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);            //Create dump file
        writeErrorLog(ex, header);

        if (exit == 0) {
            System.exit(1);
        }
    }

    private static void writeErrorLog(Throwable ex, String header) {

        PrintWriter writer = null;
        Runtime runtime = Runtime.getRuntime();
        
        File file = new File("crashlog.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new PrintWriter(file);
            writer.println(ex.getClass().getName() + ": " + ex.getMessage());
            writer.println();

            writer.println("Conquer Space version: " + ConquerSpace.VERSION);
            //Checksums:
            if (ConquerSpace.codeChecksum != null) {
                writer.println("Code checksum: " + ConquerSpace.codeChecksum);
            } else {
                writer.println("Code checksum: not generated yet");
            }

            if (ConquerSpace.assetChecksum != null) {
                writer.println("Asset checksum: " + ConquerSpace.assetChecksum);
            } else {
                writer.println("Asset checksum: not generated yet");
            }
            
            if(Globals.universe != null) {
                writer.println("Universe seed: " + Globals.universe.getSeed());
            }
            
            writer.println("Java version: " + System.getProperty("java.version") + " running on " + System.getProperty("os.name"));
            writer.println("Threads running: " + Thread.getAllStackTraces().size());
            writer.println("Memory used: " + byteCountToDisplaySize(runtime.totalMemory() - runtime.freeMemory()) + "/" + byteCountToDisplaySize(runtime.totalMemory()));

            writer.println();
            writer.print(header.replace("\n", System.getProperty("line.separator")));
            writer.print("\n\n Stack trace: \n\n".replace("\n", System.getProperty("line.separator")));
            ex.printStackTrace(writer);
            writer.println();
            writer.close();

            if (ConquerSpace.DEBUG) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex1) {
            //Wat. Problem happens in a problem.......
        } catch (IOException ex1) {
            //Same as above. Just exit it later... DAMMIT
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static String getHeaderText(String what) {
        String header = "Something has gone wrong with Conquer Space\n"
                + "We are sorry for the inconvinence.\n"
                + "Restarting could help.\n"
                + "Here's some information for the developers.\n\n"
                + "Conquer Space v " + ConquerSpace.VERSION.toString() + "\n"
                + "Build: " + ConquerSpace.BUILD_NUMBER + "\n\n" + what;
        return what;
    }

}
