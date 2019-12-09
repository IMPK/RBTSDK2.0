package com.onmobile.baseline.http.httpmodulemanagers;

import com.onmobile.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;

import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public interface IPreBuyUDSCheck {

    void showUDSUpdatePopUp(List<PricingIndividualDTO> pricingIndividualDTOS);

    void showNoPopUp(List<PricingIndividualDTO> pricingIndividualDTOS);
}
