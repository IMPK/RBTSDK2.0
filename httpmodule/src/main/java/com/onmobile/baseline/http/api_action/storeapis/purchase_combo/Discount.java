package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Discount implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1460212859439918988L;

    @SerializedName("amount")
    private String amount;

//    @SerializedName("restrictions")
//    ArrayList<Restrictions> restrictions;


    // Getter Methods


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

//    public ArrayList<Restrictions> getRestrictions() {
//        return restrictions;
//    }
//
//    public void setRestrictions(ArrayList<Restrictions> restrictions) {
//        this.restrictions = restrictions;
//    }

    public class Restrictions{

        @SerializedName("values")
        private String values;

        @SerializedName("type")
        private String type;

        public String getValues() {
            return values;
        }

        public void setValues(String values) {
            this.values = values;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}