package com.onmobile.rbt.baseline.deep;

public class DeepConstants {
    public enum DeepLinkParamKeys {
        ACTION("action"),
        CONTENT_ID("app_contentId"),
        CONTENT_TYPE("app_contentType"),
        NAMETUNE_SEARCH_QUERY("nameTuneSearchQuery");

        private final String key;

        DeepLinkParamKeys(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }

    public enum DeepLinkAction {
        GO_TO_CONTENT("GO_TO_CONTENT"),
        GO_TO_MyTunesTab("GO_TO_MyTunesTab"),
        GO_TO_MusicTab("GO_TO_MusicTab"),
        GO_TO_ProfileTab("GO_TO_ProfileTab"),
        GO_TO_NameTuneTab("GO_TO_NameTuneTab");

        private final String action;

        DeepLinkAction(String action) {
            this.action = action;
        }

        @Override
        public String toString() {
            return action;
        }
    }
}
