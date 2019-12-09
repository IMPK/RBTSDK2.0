package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nikita Gurwani .
 */
public class CallingParty implements Serializable {

        @SerializedName("id")
        private String id;

        @SerializedName("type")
        private String type;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }



}
