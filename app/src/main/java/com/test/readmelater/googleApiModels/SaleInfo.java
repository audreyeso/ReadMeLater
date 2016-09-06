package com.test.readmelater.googleApiModels;

/**
 * Created by audreyeso on 8/9/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaleInfo {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("saleability")
    @Expose
    private String saleability;
    @SerializedName("isEbook")
    @Expose
    private Boolean isEbook;

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The saleability
     */
    public String getSaleability() {
        return saleability;
    }

    /**
     *
     * @param saleability
     * The saleability
     */
    public void setSaleability(String saleability) {
        this.saleability = saleability;
    }

    /**
     *
     * @return
     * The isEbook
     */
    public Boolean getIsEbook() {
        return isEbook;
    }

    /**
     *
     * @param isEbook
     * The isEbook
     */
    public void setIsEbook(Boolean isEbook) {
        this.isEbook = isEbook;
    }

}