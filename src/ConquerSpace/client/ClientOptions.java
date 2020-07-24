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
package ConquerSpace.client;

import ConquerSpace.ConquerSpace;
import ConquerSpace.common.util.Version;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import java.util.Properties;
import org.apache.commons.lang3.LocaleUtils;

/**
 *
 * @author EhWhoAmI
 */
public class ClientOptions {

    private final String LAF_LOCALE_STRING = "locale";
    private Locale locale;
    private final Locale DEFAULT_LOCALE = ConquerSpace.DEFAULT_LOCALE;

    private final String LAF_VERSION_STRING = "version";
    private Version version;
    private final Version DEFAULT_VERSION = ConquerSpace.VERSION;

    private final String LAF_MUSIC_STRING = "music";
    private boolean playMusic;
    private final boolean DEFAULT_PLAY_MUSIC = true;

    private final String LAF_MUSIC_VOLUME_STRING = "music.volume";
    private float musicVolume;
    private final float DEFAULT_MUSIC_VOLUME = 0.8f;

    private final String LAF_OPTION_STRING = "laf";
    private String laf;
    private final String DEFAULT_LAF = "default";

    /**
     * Creates default options
     */
    public ClientOptions() {
        locale = DEFAULT_LOCALE;
        version = DEFAULT_VERSION;
        playMusic = DEFAULT_PLAY_MUSIC;
        musicVolume = DEFAULT_MUSIC_VOLUME;
        laf = DEFAULT_LAF;
    }

    public ClientOptions(Locale locale, Version version, boolean playMusic, float musicVolume, String laf) {
        this.locale = locale;
        this.version = version;
        this.playMusic = playMusic;
        this.musicVolume = musicVolume;
        this.laf = laf;
    }

    public String getLaf() {
        return laf;
    }

    public Locale getLocale() {
        return locale;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public Version getVersion() {
        return version;
    }

    public boolean isPlayMusic() {
        return playMusic;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public void setLaf(String laf) {
        this.laf = laf;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Properties toProperties() {
        Properties prop = new Properties();
        //Default settings
        prop.setProperty(LAF_LOCALE_STRING, locale.toString());

        //Version
        prop.setProperty(LAF_VERSION_STRING, version.toString());

        prop.setProperty(LAF_MUSIC_STRING, Boolean.toString(playMusic));
        prop.setProperty(LAF_MUSIC_VOLUME_STRING, Float.toString(musicVolume));

        prop.setProperty(LAF_OPTION_STRING, laf);
        return prop;
    }

    public void fromProperties(File f) {
        //Read from file...
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(f));
        } catch (IOException ex) {
        }
        fromProperties(prop);
    }

    public void fromProperties(String s) {
        Properties prop = new Properties();
        try {
            prop.load(new StringReader(s));
        } catch (IOException ex) {
            //No.
        }
        fromProperties(prop);
    }

    public void fromProperties(Properties prop) {
        String localeString = prop.getProperty(LAF_LOCALE_STRING, DEFAULT_LOCALE.toString());

        try {
            locale = LocaleUtils.toLocale(localeString);
        } catch (IllegalArgumentException iae) {
            locale = DEFAULT_LOCALE;
        }

        //Version
        String versionString = prop.getProperty(LAF_VERSION_STRING, DEFAULT_VERSION.toString());

        try {
            version = new Version(versionString);
        } catch (IllegalArgumentException iae) {
            version = DEFAULT_VERSION;
        }

        String playMusicString = prop.getProperty(LAF_MUSIC_STRING, Boolean.toString(DEFAULT_PLAY_MUSIC));

        try {
            playMusic = Boolean.parseBoolean(playMusicString);
        } catch (IllegalArgumentException iae) {
            playMusic = DEFAULT_PLAY_MUSIC;
        }

        String musicVolumeString = prop.getProperty(LAF_MUSIC_VOLUME_STRING, Float.toString(DEFAULT_MUSIC_VOLUME));
        try {
            musicVolume = Float.parseFloat(musicVolumeString);
        } catch (IllegalArgumentException iae) {
            musicVolume = DEFAULT_MUSIC_VOLUME;
        }

        laf = prop.getProperty(LAF_OPTION_STRING, DEFAULT_LAF);
    }
}
