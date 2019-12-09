package com.onmobile.rbt.baseline.event;

import java.util.List;

public class ContentCountChangeEvent {
    private int totalCount;
    private int currentCount;
    private Object content;

    public ContentCountChangeEvent() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ContentCountChangeEvent setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return obj();
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public ContentCountChangeEvent setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
        return obj();
    }

    public Object getContent() {
        return content;
    }

    public ContentCountChangeEvent setContent(Object content) {
        this.content = content;
        return obj();
    }

    private ContentCountChangeEvent obj(){
        return this;
    }
}
