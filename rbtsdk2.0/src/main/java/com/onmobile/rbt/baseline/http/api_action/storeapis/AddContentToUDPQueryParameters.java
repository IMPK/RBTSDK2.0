package com.onmobile.rbt.baseline.http.api_action.storeapis;

import java.io.Serializable;

import com.onmobile.rbt.baseline.http.api_action.dtos.Subtype;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;

/**
 * Created by Nikita Gurwani .
 */
public class AddContentToUDPQueryParameters implements Serializable {


    private String id;

    private APIRequestParameters.EMode type;

    private Subtype subtype;




    public static class Builder {

        private String id;

        private APIRequestParameters.EMode type;

        private Subtype subtype;

        public void setId(String id) {
            this.id = id;
        }

        public void setType(APIRequestParameters.EMode type) {
            this.type = type;
        }

        public void setSubtype(Subtype subtype) {
            this.subtype = subtype;
        }

        public AddContentToUDPQueryParameters build() {
            return new AddContentToUDPQueryParameters(this);
        }
    }

    private AddContentToUDPQueryParameters(Builder builder) {
        id = builder.id;
        subtype = builder.subtype;
        type = builder.type;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public APIRequestParameters.EMode getType() {
        return type;
    }

    public void setType(APIRequestParameters.EMode type) {
        this.type = type;
    }

    public Subtype getSubtype() {
        return subtype;
    }

    public void setSubtype(Subtype subtype) {
        this.subtype = subtype;
    }
}
