package com.onmobile.rbt.baseline.model;

public class UserActivityRbtDTO {

    private int type;
    private UserActivityItemDTO userActivityItemDTO;
    private boolean isHistory;

    public UserActivityRbtDTO(){

    }

    public UserActivityRbtDTO(int type, UserActivityItemDTO userActivityItemDTO, boolean isHistory) {
        this.type = type;
        this.userActivityItemDTO = userActivityItemDTO;
        this.isHistory = isHistory;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserActivityItemDTO getUserActivityItemDTO() {
        return userActivityItemDTO;
    }

    public void setUserActivityItemDTO(UserActivityItemDTO userActivityItemDTO) {
        this.userActivityItemDTO = userActivityItemDTO;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean history) {
        isHistory = history;
    }
}


