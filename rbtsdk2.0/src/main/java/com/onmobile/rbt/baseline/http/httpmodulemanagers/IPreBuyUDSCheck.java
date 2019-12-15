package com.onmobile.rbt.baseline.http.httpmodulemanagers;

import java.util.List;

import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;

/**
 * Created by Nikita Gurwani .
 */
public interface IPreBuyUDSCheck {

    void showUDSUpdatePopUp(List<PricingIndividualDTO> pricingIndividualDTOS);

    void showNoPopUp(List<PricingIndividualDTO> pricingIndividualDTOS);
}
