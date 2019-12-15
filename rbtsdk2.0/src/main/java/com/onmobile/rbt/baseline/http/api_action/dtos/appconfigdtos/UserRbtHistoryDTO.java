package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nikita.gurwani on 10/31/2017.
 */

public class UserRbtHistoryDTO implements Serializable{

    @SerializedName("getparameters")
    GetParametersUserHistoryDTO getparameters;

    public GetParametersUserHistoryDTO getGetParameters() {
        return getparameters;
    }

    public void setGetParameters(GetParametersUserHistoryDTO getparameters) {
        this.getparameters = getparameters;
    }

    public class GetParametersUserHistoryDTO implements Serializable{

        @SerializedName("content_subtype")
        String content_subtype;

        @SerializedName("user_status")
        String user_status;

        @SerializedName("content_type")
        String content_type;

        public String getContent_subtype() {
            return content_subtype;
        }

        public void setContent_subtype(String content_subtype) {
            this.content_subtype = content_subtype;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getContent_type() {
            return content_type;
        }

        public void setContent_type(String content_type) {
            this.content_type = content_type;
        }
    }

}
