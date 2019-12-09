package com.onmobile.rbt.baseline.event;

import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class MeetingListUpdateEvent {

    private boolean isSelected;

    public MeetingListUpdateEvent(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
