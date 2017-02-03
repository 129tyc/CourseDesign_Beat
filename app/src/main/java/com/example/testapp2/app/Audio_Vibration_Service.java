package com.example.testapp2.app;

import android.content.Context;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Code on 2016/8/20 0020.
 *
 * @deprecated 与系统SoundPool冲突，未正确调试，无法使用
 * FIXME
 */
public class Audio_Vibration_Service {
    private SoundPool.Builder poolBuilder;
    private SoundPool pool;
    private Context context;
    private Map<String, Integer> soundMap;
    private Map<String, Integer> streamMap;
    private float leftVolume = FALSE;
    private float rightVolume = FALSE;
    private final static int FALSE = -1;
    private final static int PLAYFALSE = 0;

    public Audio_Vibration_Service(Context context, int maxStreams) {
        this.context = context;
        if (poolBuilder == null) {
            poolBuilder = new SoundPool.Builder();
        }
        poolBuilder.setMaxStreams(maxStreams);
        if (pool == null) {
            pool = poolBuilder.build();
        }
        if (soundMap == null) {
            soundMap = new HashMap<String, Integer>();
        }
        if (streamMap == null) {
            streamMap = new HashMap<String, Integer>();
        }
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
    }

    public boolean addAudio(String tag, int resid, int priority) {
        if (soundMap == null) {
            return false;
        }
        soundMap.put(tag, pool.load(context, resid, priority));
        return true;
    }

    public boolean playAudio(String streamTag, String tag, int priority, int loop, float rate) {
        if (leftVolume == FALSE || rightVolume == FALSE) {
            return false;
        }
        return playAudio(streamTag, tag, leftVolume, rightVolume, priority, loop, rate);
    }

    private int getSoundID(String tag) {
        if (soundMap == null) {
            return FALSE;
        }
        if (soundMap.containsKey(tag)) {
            return soundMap.get(tag);
        }
        return FALSE;
    }

    public SoundPool getSoundPool() {
        if (pool != null) {
            return pool;
        }
        return null;
    }

    public boolean playAudio(String streamTag, String tag, float leftVolume, float rightVolume, int priority, int loop, float rate) {
        if (pool == null) {
            return false;
        }
        if (getSoundID(tag) == FALSE) {
            return false;
        }
        if (streamMap == null) {
            return false;
        }
        int soundId = getSoundID(tag);
        streamMap.put(streamTag, pool.play(soundId, leftVolume, rightVolume, priority, loop, rate));
        return streamMap.get(streamTag) != PLAYFALSE;
    }
}
