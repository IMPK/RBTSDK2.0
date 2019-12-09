package com.onmobile.rbt.baseline.event;

import java.util.List;

public class RBTStatus {
    private List<String> ids;
    private boolean isSelected;

    public RBTStatus(boolean isSelected, List<String> ids) {
        this.ids = ids;
        this.isSelected = isSelected;
    }

    public List<String> getIds() {
        return ids;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
