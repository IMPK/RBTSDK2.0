package com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class ListOfAvailabilityDTO implements Serializable {

    @SerializedName("availability")
    List<AvailabilityDTO> availabilityDTOS;

    public List<AvailabilityDTO> getAvailabilityDTOS() {
        return availabilityDTOS;
    }

    public void setAvailabilityDTOS(List<AvailabilityDTO> availabilityDTOS) {
        this.availabilityDTOS = availabilityDTOS;
    }
}
