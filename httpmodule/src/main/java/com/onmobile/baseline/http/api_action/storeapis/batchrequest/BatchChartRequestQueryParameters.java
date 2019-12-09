package com.onmobile.baseline.http.api_action.storeapis.batchrequest;

import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.util.List;

/**
 * Created by Shahbaz Akhtar on 24/04/2019.
 *
 * @author Shahbaz Akhtar
 */

public class BatchChartRequestQueryParameters {

    private int max, offset;
    private int imageWidth;

    public static class Builder {
        private int max, offset;
        private int imageWidth;

        public void setMax(int max) {
            this.max = max;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
        }

        public BatchChartRequestQueryParameters build() {
            return new BatchChartRequestQueryParameters(this);
        }
    }

    private BatchChartRequestQueryParameters(Builder builder) {
        max = builder.max;
        offset = builder.offset;
        imageWidth = builder.imageWidth;
    }

    public int getMax() {
        return max;
    }

    public int getOffset() {
        return offset;
    }

    public int getImageWidth() {
        return imageWidth;
    }
}
