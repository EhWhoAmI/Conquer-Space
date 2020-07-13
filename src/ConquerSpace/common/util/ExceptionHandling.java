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
package ConquerSpace.common.util;

import ConquerSpace.ConquerSpace;
import ConquerSpace.Globals;
import static ConquerSpace.client.gui.game.DebugStatsWindow.byteCountToDisplaySize;
import com.alee.extended.layout.VerticalFlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.server.GameController;
import ConquerSpace.common.GameState;

/**
 * The one class I hope no one sees.
 *
 * @author EhWhoAmI
 */
public class ExceptionHandling {

    public static void ExceptionMessageBox(String what, Throwable ex) {
        ExceptionMessageBox(what, ex, null);
    }
    /**
     * Takes in a string and exception. Has a message box for the user and makes
     * a crash dump.
     *
     * @param what Your own message.
     * @param ex Exception that caused it.
     */
    public static void ExceptionMessageBox(String what, Throwable ex, GameState gameState) {
        int exit = 1;

        JPanel optionPanel = createOptionPanel(what, ex);

        exit = JOptionPane.showConfirmDialog(null,
                optionPanel,
                String.format(LOCALE_MESSAGES.getMessage("errorhandlingtitle"), ex.getClass().getName() ,ex.getMessage()),
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);//Create dump file

        writeErrorLog(ex, what, gameState);
        if (exit == 0) {
            System.exit(1);
        }
    }

    public static void FatalExceptionMessageBox(String what, Throwable ex) {
        FatalExceptionMessageBox(what, ex, null);
    }
    
    public static void FatalExceptionMessageBox(String what, Throwable ex, GameState gameState) {
        JPanel optionPanel = createOptionPanel(what, ex);

        JOptionPane.showMessageDialog(null,
                optionPanel,
                String.format(LOCALE_MESSAGES.getMessage("fatalhandlingtitle"), ex.getClass().getName() ,ex.getMessage()), JOptionPane.ERROR_MESSAGE);//Create dump file
        writeErrorLog(ex, what, gameState);
        System.exit(1);
    }

    private static JPanel createOptionPanel(String what, Throwable ex) {
        JPanel optionPanel = new JPanel(new VerticalFlowLayout());
        String text = String.format(LOCALE_MESSAGES.getMessage("errorhandlingheader"),
                ConquerSpace.VERSION,
                ConquerSpace.VERSION);
        String[] content = text.split("\n");
        for (String s : content) {
            optionPanel.add(new JLabel(s));
        }

        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        JTextArea area = new JTextArea(exceptionAsString);
        area.setRows(16);
        area.setEditable(false);
        optionPanel.add(new JScrollPane(area));
        return optionPanel;
    }
    
    private static void writeErrorLog(Throwable ex, String header) {
        writeErrorLog(ex, header, null);
    }
    
    private static void writeErrorLog(Throwable ex, String header, GameState gameState) {
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
            
            if (gameState != null && gameState.universe != null) {
                writer.println("Universe seed: " + gameState.universe.getSeed());
            }

            writer.println("Java version: " + System.getProperty("java.version") + " running on " + System.getProperty("os.name"));
            writer.println("Threads running: " + Thread.getAllStackTraces().size());
            writer.println("Memory used: " + byteCountToDisplaySize(runtime.totalMemory() - runtime.freeMemory()) + "/" + byteCountToDisplaySize(runtime.totalMemory()));

            writer.println();
            writer.print(header.replace("\n", System.getProperty("line.separator")));
            writer.print("\n\nStack trace: \n\n".replace("\n", System.getProperty("line.separator")));
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
}
