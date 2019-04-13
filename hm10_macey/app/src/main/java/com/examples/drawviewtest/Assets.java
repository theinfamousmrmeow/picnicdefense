package com.examples.drawviewtest;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.content.Context;


public class Assets {
    public static SoundPool sp;
    public static MediaPlayer mp;
    public static int sfx_miss,sfx_squash;

    public static void tryPlaySound(Context context, int _sound, boolean _loop){
        //final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //boolean sfx_enabled = prefs.getBoolean("key_sound_enabled", true);
        mp = null;
        //Play Sound!
        mp = MediaPlayer.create(context,_sound);
        if ( mp!=null){
            mp.setLooping(_loop);
            mp.start();
        }
    }


}
