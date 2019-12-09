package com.onmobile.rbt.baseline.event;

import java.util.List;

public class NewRecommendation {
    private String recommendationId;

    public NewRecommendation(String recommendationId) {
        this.recommendationId = recommendationId;
    }

    public String getId() {
        return recommendationId;
    }

    public void setId(String recommendationId) {
        this.recommendationId = recommendationId;
    }
}
