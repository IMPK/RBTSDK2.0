package com.onmobile.baseline.http.api_action.storeapis.batchrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class ListOfRequestBatchItemsDTO implements Serializable {
    @SerializedName("batch_items")
    List<BatchRequestItemDTO> batch_items= new ArrayList<>();


    public List<BatchRequestItemDTO> getBatch_items() {
        return batch_items;
    }

    public void setBatch_items(List<BatchRequestItemDTO> batch_items) {
        this.batch_items = batch_items;
    }
}
