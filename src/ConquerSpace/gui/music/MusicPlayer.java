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
package ConquerSpace.gui.music;

import ConquerSpace.util.CQSPLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.easyogg.OggClip;

/**
 *
 * @author EhWhoAmI
 */
public class MusicPlayer {

    private static final Logger LOGGER = CQSPLogger.getLogger(MusicPlayer.class.getName());

    private OggClip clip;
    private JSONArray musicArray;

    private boolean toPlay = true;

    Thread musicThread;
    boolean toStop = false;

    public MusicPlayer() {
        FileInputStream fis = null;
        try {
            //Load music
            File f = new File("assets/music/music_list.json");
            fis = new FileInputStream(f);
            byte[] data = new byte[(int) f.length()];
            fis.read(data);
            fis.close();
            String text = new String(data);
            musicArray = new JSONArray(text);
            musicThread = new Thread(() -> {
                for (;;) {
                    if (toPlay) {
                        try {
                            int i = (int) (Math.random() * musicArray.length());
                            JSONObject obj = musicArray.getJSONObject(i);
                            clip = new OggClip(new FileInputStream("assets/music/" + obj.getString("file")));
                            //if (toPlay) {
                            clip.play();
                            //}
                            //Thread.sleep(500);

                            int length = obj.getInt("length");
                            for (int index = 0; index < length; index++) {
                                if (!toPlay) {
                                    break;
                                }
                                Thread.sleep(1000);
                            }
                            //if (toPlay) {
                            clip.stop();
                            clip.close();
                            //}
                        } catch (IOException ex) {
                            LOGGER.error(ex);
                        } catch (InterruptedException ex) {
                            LOGGER.error(ex);
                        } catch (NullPointerException npe) {
                            //Ignore...
                        }
                    }
                    if (toStop) {
                        break;
                    }
                }
            }
            );
            musicThread.setName("musicplayer");
        } catch (FileNotFoundException ex) {
            LOGGER.warn("No Music!", ex);
        } catch (IOException ex) {
            LOGGER.warn("No Music!", ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {

            }
        }
    }

    public void stopMusic() {
        if (clip != null) {
            clip.stop();
        }
        toStop = true;
    }

    public void clean() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void setToPlay(boolean toPlay) {
        this.toPlay = toPlay;
    }

    public void playMusic() {
        this.toPlay = true;
        musicThread.start();
    }

    public boolean isPlaying() {
        return toPlay;
    }
}
