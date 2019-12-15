package com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends;

/**
 * Created by hitesh.p on 1/3/2019.
 */

public class ChildOperationResponseDTO {
    public static final String SUCCESS = "SUCCESS";

    private String status;
    private String code;
    private String sub_code;
    private String description;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
