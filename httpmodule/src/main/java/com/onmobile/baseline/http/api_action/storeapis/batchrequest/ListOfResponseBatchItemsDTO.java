package com.onmobile.baseline.http.api_action.storeapis.batchrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class ListOfResponseBatchItemsDTO implements Serializable {
    @SerializedName("batch_items")
    List<BatchItemResponseDTO> batch_items;


    public List<BatchItemResponseDTO> getBatch_items() {
        return batch_items;
    }

    public void setBatch_items(List<BatchItemResponseDTO> batch_items) {
        this.batch_items = batch_items;
    }
}
