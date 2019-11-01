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
 * @author zyunl
 */
public class MusicPlayer {

    private static final Logger LOGGER = CQSPLogger.getLogger(MusicPlayer.class.getName());

    private OggClip clip;
    private JSONArray musicArray;

    private boolean toPlay = true;

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
            Thread t = new Thread(() -> {
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
                }
            }
            );
            t.setName("musicplayer");
            t.start();
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
        clip.stop();
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

    public boolean isPlaying() {
        return toPlay;
    }
}
