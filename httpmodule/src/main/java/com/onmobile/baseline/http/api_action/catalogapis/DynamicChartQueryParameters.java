package com.onmobile.baseline.http.api_action.catalogapis;

import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class DynamicChartQueryParameters {

    private int max, offset;
    private APIRequestParameters.EMode type;
    private int imageWidth;
    private boolean showDynamicContent;
    private int dynamicContentSize;
    private List<String> chartLanguages;

    public static class Builder {
        private int max, offset;
        private APIRequestParameters.EMode type;
        private int imageWidth;
        private boolean showDynamicContent;
        private int dynamicContentSize;
        private List<String> chartLanguages;

        public void setMax(int max) {
            this.max = max;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void setType(APIRequestParameters.EMode type) {
            this.type = type;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
        }

        public void setShowDynamicContent(boolean showDynamicContent) {
            this.showDynamicContent = showDynamicContent;
        }

        public void setDynamicContentSize(int dynamicContentSize) {
            this.dynamicContentSize = dynamicContentSize;
        }

        public void setChartLanguages(List<String> chartLanguages) {
            this.chartLanguages = chartLanguages;
        }

        public DynamicChartQueryParameters build() {
            return new DynamicChartQueryParameters(this);
        }
    }

    private DynamicChartQueryParameters(Builder builder) {
        max = builder.max;
        offset = builder.offset;
        type = builder.type;
        imageWidth = builder.imageWidth;
        showDynamicContent = builder.showDynamicContent;
        dynamicContentSize = builder.dynamicContentSize;
        chartLanguages = builder.chartLanguages;
    }

    public int getMax() {
        return max;
    }

    public int getOffset() {
        return offset;
    }

    public APIRequestParameters.EMode getType() {
        return type;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public boolean isShowDynamicContent() {
        return showDynamicContent;
    }

    public int getDynamicContentSize() {
        return dynamicContentSize;
    }

    public List<String> getChartLanguages() {
        return chartLanguages;
    }
}
