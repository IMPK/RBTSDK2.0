package com.onmobile.baseline.http.api_action.dtos.pricing.availability;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class AvailabilityDTO implements Serializable {



    /**
     *
     */
    @SerializedName("short_description")
    private String shortDescription;
    @SerializedName("description")
    private String description;

    private static final long serialVersionUID = 5845045182144186556L;
    private AvailabilityRights right;
    private CodecDTO codec;

    @SerializedName("individual")
    private List<PricingIndividualDTO> individual = new ArrayList<PricingIndividualDTO>();

    private List<BundlePriceDTO> bundlePrice = new ArrayList<BundlePriceDTO>();
    private List<SubscriptionDTO> subscriptions = new ArrayList<SubscriptionDTO>();
    private List<ContentAvailabilityDTO> content = new ArrayList<ContentAvailabilityDTO>();




    private List<RestrictionDTO> restrictions=new ArrayList<RestrictionDTO>();

    public AvailabilityDTO(AvailabilityDTO original) {
        this.right = original.right;
        this.codec = new CodecDTO(original.codec);

        this.individual = new ArrayList<PricingIndividualDTO>();
        this.bundlePrice = new ArrayList<BundlePriceDTO>();

        for (PricingIndividualDTO originalPricingIndividualDto : original.individual) {
            this.individual.add(new PricingIndividualDTO(originalPricingIndividualDto));
        }

        for (BundlePriceDTO originalBundlePriceDTO : original.bundlePrice) {
            this.bundlePrice.add(new BundlePriceDTO(originalBundlePriceDTO));
        }

        this.subscriptions = new ArrayList<SubscriptionDTO>();
        for (SubscriptionDTO originalSubscriptionDto : original.subscriptions) {
            this.subscriptions
                    .add(new SubscriptionDTO(originalSubscriptionDto));
        }

        this.content = new ArrayList<ContentAvailabilityDTO>();
        for (ContentAvailabilityDTO originalContentDto : original.content) {
            this.content.add(new ContentAvailabilityDTO(originalContentDto));
        }
    }

    public AvailabilityDTO() {
    }

    public AvailabilityRights getRight() {
        return right;
    }

    public void setRight(AvailabilityRights right) {
        this.right = right;
    }

    public CodecDTO getCodec() {
        return codec;
    }

    public void setCodec(CodecDTO codec) {
        this.codec = codec;
    }

    public List<PricingIndividualDTO> getIndividual() {
        return individual;
    }

    public void setIndividual(List<PricingIndividualDTO> individual) {
        this.individual = individual;
    }

    public List<BundlePriceDTO> getBundlePrice() {
        return bundlePrice;
    }

    public void setBundlePrice(List<BundlePriceDTO> bundlePrice) {
        this.bundlePrice = bundlePrice;
    }

    public List<SubscriptionDTO> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionDTO> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<ContentAvailabilityDTO> getContent() {
        return content;
    }

    public void setContent(List<ContentAvailabilityDTO> content) {
        this.content = content;
    }

     
    public enum AvailabilityRights {
        DOWNLOAD, RENTAL, download, rental
    }

     
    public static class ContentAvailabilityDTO implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 622534546094408572L;



        private AvailabilityRights right;

        private CodecDTO codec;

        public ContentAvailabilityDTO() {
        }

        public ContentAvailabilityDTO(ContentAvailabilityDTO original) {
            this.right = original.right;
            this.codec = new CodecDTO(original.codec);
        }



        public AvailabilityRights getRight() {
            return right;
        }

        public void setRight(AvailabilityRights right) {
            this.right = right;
        }

        public CodecDTO getCodec() {
            return codec;
        }

        public void setCodec(CodecDTO codec) {
            this.codec = codec;
        }

    }
    public List<RestrictionDTO> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<RestrictionDTO> restrictions) {
        this.restrictions = restrictions;
    }
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
