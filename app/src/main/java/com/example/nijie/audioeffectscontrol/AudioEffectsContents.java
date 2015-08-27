package com.example.nijie.audioeffectscontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nijie on 8/26/15.
 */
public class AudioEffectsContents {
    /**
     * An array of sample (dummy) items.
     */
    public static List<AudioEffects> ITEMS = new ArrayList<AudioEffects>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, AudioEffects> ITEM_MAP = new HashMap<String, AudioEffects>();

    static {
        // Add 3 sample items.
        addItem(new AudioEffects("Equalizer", "Equalizer"));
        addItem(new AudioEffects("Virtualizer", "Virtualizer"));
        addItem(new AudioEffects("Reverb", "Reverb"));
    }

    private static void addItem(AudioEffects item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class AudioEffects {
        public String id;
        public String content;

        public AudioEffects(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
