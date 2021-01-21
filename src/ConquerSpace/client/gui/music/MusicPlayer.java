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
package ConquerSpace.client.gui.music;

import ConquerSpace.common.util.logging.CQSPLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.apache.logging.log4j.Logger;
import org.hjson.JsonValue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.easyogg.OggClip;

/**
 *
 * @author EhWhoAmI
 */
public class MusicPlayer implements Runnable {

    private static final Logger LOGGER = CQSPLogger.getLogger(MusicPlayer.class.getName());

    private OggClip clip;
    private JSONArray musicArray;

    private boolean toPlay = true;

    Thread musicThread;
    boolean toStop = false;

    float volume = 0.8f;

    long songStart = 0;
    long songPause = 0;
    //How far into the song it is
    long songPlayLength = 0;

    public MusicPlayer() {
        toPlay = false;
        File f = new File("assets/music/music_list.txt");
        try ( FileInputStream fis = new FileInputStream(f);) {
            //Load music
            byte[] data = new byte[(int) f.length()];
            fis.read(data);
            fis.close();
            String text = new String(data);
            text = JsonValue.readHjson(text).toString();
            musicArray = new JSONArray(text);
            musicThread = new Thread(this);
            musicThread.setName("musicplayer");
        } catch (FileNotFoundException ex) {
            LOGGER.warn("No Music!", ex);
        } catch (IOException ex) {
            LOGGER.warn("No Music!", ex);
        }
    }

    public void stopMusic() {
        toStop = true;
        this.toPlay = false;
        if (clip != null && !clip.stopped()) {
            clip.stop();
        }
        if (musicThread.isAlive()) {
            musicThread.interrupt();
        }
        //And the thread ends
    }

    public void clean() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void playMusic() {
        stopMusic();

        if (Thread.State.NEW != musicThread.getState()) {
            //New thread
            musicThread = new Thread(this);
            musicThread.setName("musicplayer");
            this.toPlay = true;
            toStop = false;
            musicThread.start();
        } else {
            this.toPlay = true;
            toStop = false;
            musicThread.start();
        }
    }

    public boolean isPlaying() {
        return toPlay;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (clip != null) {
            clip.setGain(volume);
        }
    }

    public void run() {
        for (;;) {
            if (toPlay) {
                try {
                    int i = (int) (Math.random() * musicArray.length());
                    JSONObject obj = musicArray.getJSONObject(i);
                    clip = new OggClip(new FileInputStream("assets/music/" + obj.getString("file")));
                    //if (toPlay) {
                    clip.play();
                    clip.setGain(volume);
                    //}
                    //Thread.sleep(500);
                    double duration = calculateDuration(new File("assets/music/" + obj.getString("file")));

                    if (duration > 0) {
                        Thread.sleep((int) Math.ceil(duration));
                    } else {
                        LOGGER.warn("Skipped " + obj.getString("file"));
                    }
                    clip.stop();
                    clip.close();
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

    private double calculateDuration(final File oggFile) throws IOException {
        int rate = -1;
        int length = -1;

        int size = (int) oggFile.length();
        byte[] t = new byte[size];

        FileInputStream stream = new FileInputStream(oggFile);
        stream.read(t);

        for (int i = size - 1 - 8 - 2 - 4; i >= 0 && length < 0; i--) { //4 bytes for "OggS", 2 unused bytes, 8 bytes for length
            // Looking for length (value after last "OggS")
            if (t[i] == (byte) 'O'
                    && t[i + 1] == (byte) 'g'
                    && t[i + 2] == (byte) 'g'
                    && t[i + 3] == (byte) 'S') {
                byte[] byteArray = new byte[]{t[i + 6], t[i + 7], t[i + 8], t[i + 9], t[i + 10], t[i + 11], t[i + 12], t[i + 13]};
                ByteBuffer bb = ByteBuffer.wrap(byteArray);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                length = bb.getInt(0);
            }
        }
        for (int i = 0; i < size - 8 - 2 - 4 && rate < 0; i++) {
            // Looking for rate (first value after "vorbis")
            if (t[i] == (byte) 'v'
                    && t[i + 1] == (byte) 'o'
                    && t[i + 2] == (byte) 'r'
                    && t[i + 3] == (byte) 'b'
                    && t[i + 4] == (byte) 'i'
                    && t[i + 5] == (byte) 's') {
                byte[] byteArray = new byte[]{t[i + 11], t[i + 12], t[i + 13], t[i + 14]};
                ByteBuffer bb = ByteBuffer.wrap(byteArray);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                rate = bb.getInt(0);
            }

        }
        stream.close();

        double duration = (double) (length * 1000) / (double) rate;
        return duration;
    }
}
