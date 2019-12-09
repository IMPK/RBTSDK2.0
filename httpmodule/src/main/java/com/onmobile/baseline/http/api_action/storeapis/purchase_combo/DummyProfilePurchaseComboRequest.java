package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.onmobile.baseline.http.Configuration;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.util.Map;

/**
 * Created by Nikita Gurwani .
 */
public class DummyProfilePurchaseComboRequest extends PurchaseDummyComboRequest {



    public DummyProfilePurchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap, ComboApiBillingInfoDto comboApiBillingInfoDto ) {


        super(purchaseComboRequestParameters, callback, extraInfoMap, comboApiBillingInfoDto);

    }

    @Override
    public ScheduleDTO getSchedule() {
        ScheduleDTO schedule = new ScheduleDTO(APIRequestParameters.ScheduleType.PLAYRANGE, Configuration.scheduleType);
        schedule.setId(String.valueOf(Configuration.scheduleID));
        schedule.setPlayDuration(purchaseComboRequestParameters.getProfile_range());
        return schedule;
    }
}
