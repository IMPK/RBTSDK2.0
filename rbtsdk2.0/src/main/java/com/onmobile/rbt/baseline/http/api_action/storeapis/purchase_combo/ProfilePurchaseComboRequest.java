package com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo;

import java.util.Map;

import com.onmobile.rbt.baseline.http.Configuration;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;

/**
 * Created by Nikita Gurwani .
 */
public class ProfilePurchaseComboRequest extends PurchaseComboRequest {



    public ProfilePurchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap, ComboApiBillingInfoDto comboApiBillingInfoDto ) {


        super(purchaseComboRequestParameters, callback,extraInfoMap, comboApiBillingInfoDto);

    }

    @Override
    public ScheduleDTO getSchedule() {
        ScheduleDTO schedule = new ScheduleDTO(APIRequestParameters.ScheduleType.PLAYRANGE, Configuration.scheduleType);
        schedule.setId(String.valueOf(Configuration.scheduleID));
        schedule.setPlayDuration(purchaseComboRequestParameters.getProfile_range());
        return schedule;
    }
}
