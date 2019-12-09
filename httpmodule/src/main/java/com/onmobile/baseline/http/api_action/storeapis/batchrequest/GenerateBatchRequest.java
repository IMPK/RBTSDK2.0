package com.onmobile.baseline.http.api_action.storeapis.batchrequest;

import com.onmobile.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;

import retrofit2.Call;

/**
 * Created by Nikita Gurwani .
 */
public class GenerateBatchRequest extends BaseAPIStoreRequestAction {


    protected Call<ListOfResponseBatchItemsDTO> call;

    @Override
    public void cancel() {
        if (call != null) {
            try {
                call.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute() {

    }

    @Override
    public void initCall() {
        call = getApi().getResponseOfBatchRequest(getBaseStoreURLRequest(), getBatchRequest(), requestBody());
    }

    public ListOfRequestBatchItemsDTO requestBody() {
        ListOfRequestBatchItemsDTO body = new ListOfRequestBatchItemsDTO();

        return body;
    }



}


