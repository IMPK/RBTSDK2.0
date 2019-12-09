package com.onmobile.rbt.baseline.deep;

import java.util.HashMap;
import java.util.Map;

public enum ContentItemType {
    APPLICATION("app"), ALBUM("album"), TRACK("track"), GAME("game"), RING_TONE(
            "realtone"), RINGBACK_TONE("ringback"), BUNDLE("bundle"), ARTIST(
            "artist"), PLAYLIST("playlist"), RADIO("radio"), RINGBACK_STATION(
            "ringback_station"), RINGBACK_PLAYLIST("ringback_playlist"), VIDEO(
            "video"), CHART("chart"), RINGBACK_NAMETUNE("ringback_nametune"), RINGBACK_MUSICTUNE("ringback_musictune"), PROFILE_TUNE_CHART("profile_tune_chart");

    private static Map<String, ContentItemType> sTypeStringMap = new HashMap<String, ContentItemType>();

    static {
        for (ContentItemType periodType : values()) {
            sTypeStringMap.put(periodType.getTypeString(), periodType);
        }
    }

    private final String typeString;

    ContentItemType(String typeString) {
        this.typeString = typeString;
    }

    public static ContentItemType getContentItemType(String name) {
        return sTypeStringMap.get(name);
    }

    public static ContentItemType valueOfTypeString(String name) {
        return sTypeStringMap.get(name);
    }

    public String getTypeString() {
        return typeString;
    }

    @Override
    public String toString() {
        return typeString;
    }

}
