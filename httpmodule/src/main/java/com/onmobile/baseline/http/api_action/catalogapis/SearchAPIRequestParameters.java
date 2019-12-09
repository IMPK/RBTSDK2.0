package com.onmobile.baseline.http.api_action.catalogapis;

import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class SearchAPIRequestParameters {

    private   String query;
    private List<String> language;
    private Integer imageWidth;
    private APIRequestParameters.SearchCategoryType searchCategoryType;
    private String resultSetSize;
    private   Integer offset;
    private   Integer max;

    public static class Builder {
        private   String query;
        private List<String> language;
        private Integer imageWidth;
        private APIRequestParameters.SearchCategoryType searchCategoryType;
        private String resultSetSize;
        private   Integer offset;
        private   Integer max;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public List<String> getLanguage() {
            return language;
        }

        public void setLanguage(List<String> language) {
            this.language = language;
        }

        public Integer getImageWidth() {
            return imageWidth;
        }

        public void setImageWidth(Integer imageWidth) {
            this.imageWidth = imageWidth;
        }

        public APIRequestParameters.SearchCategoryType getSearchCategoryType() {
            return searchCategoryType;
        }

        public void setSearchCategoryType(APIRequestParameters.SearchCategoryType searchCategoryType) {
            this.searchCategoryType = searchCategoryType;
        }

        public String getResultSetSize() {
            return resultSetSize;
        }

        public void setResultSetSize(String resultSetSize) {
            this.resultSetSize = resultSetSize;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getMax() {
            return max;
        }

        public void setMax(Integer max) {
            this.max = max;
        }

        public SearchAPIRequestParameters build() {
            return new SearchAPIRequestParameters(this);
        }
    }

    private SearchAPIRequestParameters(SearchAPIRequestParameters.Builder builder) {
        max = builder.max;
        offset = builder.offset;
        query = builder.query;
        imageWidth = builder.imageWidth;
        language = builder.language;
        resultSetSize = builder.resultSetSize;
        searchCategoryType = builder.searchCategoryType;
    }

    public String getQuery() {
        return query;
    }

    public List<String> getLanguage() {
        return language;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public APIRequestParameters.SearchCategoryType getSearchCategoryType() {
        return searchCategoryType;
    }

    public String getResultSetSize() {
        return resultSetSize;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getMax() {
        return max;
    }

    public static int SEARCH_SONG_ITEM_LIMIT = 8;
    public static int SEARCH_ARTIST_ITEM_LIMIT = 8;
    public static int SEARCH_ALBUM_ITEM_LIMIT = 8;
    public static String getResultSetSizeForSearchAPI() {
        return SEARCH_SONG_ITEM_LIMIT + "|" + SEARCH_ARTIST_ITEM_LIMIT + "|" + SEARCH_ALBUM_ITEM_LIMIT;
    }
}
