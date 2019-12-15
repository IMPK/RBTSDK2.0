package com.onmobile.rbt.baseline.http.api_action.storeapis;

/**
 * Created by Nikita Gurwani .
 */
public class CreateUserDefinedPlaylistQueryParams {

    private String name;
    private String extra_info;



    public static class Builder {
        private String name;
        private String extra_info;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExtra_info() {
            return extra_info;
        }

        public void setExtra_info(String extra_info) {
            this.extra_info = extra_info;
        }

        public CreateUserDefinedPlaylistQueryParams build() {
            return new CreateUserDefinedPlaylistQueryParams(this);
        }
    }

    private CreateUserDefinedPlaylistQueryParams(Builder builder) {
        name = builder.name;
        extra_info = builder.extra_info;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }
}
