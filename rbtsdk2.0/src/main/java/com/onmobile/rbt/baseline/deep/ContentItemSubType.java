package com.onmobile.rbt.baseline.deep;

public enum ContentItemSubType {
        RINGBACK_PROFILE("RINGBACK_PROFILE"),
        RINGBACK_NAMETUNE("RINGBACK_NAMETUNE"),
        RINGBACK_COOLTUNE("RINGBACK_COOLTUNE"),
        RINGBACK_DEVOTIONAL("RINGBACK_DEVOTIONAL"),
        RINGBACK_MUSICTUNE("RINGBACK_MUSICTUNE"),
        RINGBACK_NONMUSICTUNE("RINGBACK_NONMUSICTUNE"),
        RINGBACK_AZAN("RINGBACK_AZAN");

        private final String name;

        ContentItemSubType(String s) {
            name = s;
        }

        public String toString() {
            return name;
        }

        public boolean equals(String other) {
            return (other != null) && name.equals(other);
        }
    }