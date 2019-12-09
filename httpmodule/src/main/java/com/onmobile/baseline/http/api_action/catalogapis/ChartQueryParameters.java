package com.onmobile.baseline.http.api_action.catalogapis;

import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class ChartQueryParameters {

    private int max, offset;
    private APIRequestParameters.EMode type;
    private int imageWidth;
    private boolean showContent;
    private List<String> chartLanguages;

    public static class Builder {
        private int max, offset;
        private APIRequestParameters.EMode type;
        private int imageWidth;
        private boolean showContent;
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

        public void setShowContent(boolean showContent) {
            this.showContent = showContent;
        }

        public ChartQueryParameters build() {
            return new ChartQueryParameters(this);
        }

        public void setChartLanguages(List<String> chartLanguages) {
            this.chartLanguages = chartLanguages;
        }
    }

    private ChartQueryParameters(Builder builder) {
        max = builder.max;
        offset = builder.offset;
        type = builder.type;
        imageWidth = builder.imageWidth;
        showContent = builder.showContent;
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

    public boolean isShowContent() {
        return showContent;
    }

    public List<String> getChartLanguages() {
        return chartLanguages;
    }
}
