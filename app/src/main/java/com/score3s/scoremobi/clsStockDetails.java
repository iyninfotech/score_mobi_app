package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ballu on 15/09/17.
 */

public class clsStockDetails {
    @SerializedName("ActualClosing")
    @Expose
    private String actualClosing;
    @SerializedName("Closing")
    @Expose
    private String closing;
    @SerializedName("ClosingRate")
    @Expose
    private String closingRate;
    @SerializedName("ClosingValue")
    @Expose
    private String closingValue;
    @SerializedName("ITEM")
    @Expose
    private String iTEM;
    @SerializedName("ITEMID")
    @Expose
    private String iTEMID;
    @SerializedName("Inward")
    @Expose
    private String inward;
    @SerializedName("InwardRate")
    @Expose
    private String inwardRate;
    @SerializedName("InwardValue")
    @Expose
    private String inwardValue;
    @SerializedName("Opening")
    @Expose
    private String opening;
    @SerializedName("OpeningRate")
    @Expose
    private String openingRate;
    @SerializedName("OpeningValue")
    @Expose
    private String openingValue;
    @SerializedName("Outward")
    @Expose
    private String outward;
    @SerializedName("OutwardRate")
    @Expose
    private String outwardRate;
    @SerializedName("OutwardValue")
    @Expose
    private String outwardValue;
    @SerializedName("Variation")
    @Expose
    private String variation;

    public String getActualClosing() {
        return actualClosing;
    }

    public void setActualClosing(String actualClosing) {
        this.actualClosing = actualClosing;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getClosingRate() {
        return closingRate;
    }

    public void setClosingRate(String closingRate) {
        this.closingRate = closingRate;
    }

    public String getClosingValue() {
        return closingValue;
    }

    public void setClosingValue(String closingValue) {
        this.closingValue = closingValue;
    }

    public String getITEM() {
        return iTEM;
    }

    public void setITEM(String iTEM) {
        this.iTEM = iTEM;
    }

    public String getITEMID() {
        return iTEMID;
    }

    public void setITEMID(String iTEMID) {
        this.iTEMID = iTEMID;
    }

    public String getInward() {
        return inward;
    }

    public void setInward(String inward) {
        this.inward = inward;
    }

    public String getInwardRate() {
        return inwardRate;
    }

    public void setInwardRate(String inwardRate) {
        this.inwardRate = inwardRate;
    }

    public String getInwardValue() {
        return inwardValue;
    }

    public void setInwardValue(String inwardValue) {
        this.inwardValue = inwardValue;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getOpeningRate() {
        return openingRate;
    }

    public void setOpeningRate(String openingRate) {
        this.openingRate = openingRate;
    }

    public String getOpeningValue() {
        return openingValue;
    }

    public void setOpeningValue(String openingValue) {
        this.openingValue = openingValue;
    }

    public String getOutward() {
        return outward;
    }

    public void setOutward(String outward) {
        this.outward = outward;
    }

    public String getOutwardRate() {
        return outwardRate;
    }

    public void setOutwardRate(String outwardRate) {
        this.outwardRate = outwardRate;
    }

    public String getOutwardValue() {
        return outwardValue;
    }

    public void setOutwardValue(String outwardValue) {
        this.outwardValue = outwardValue;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }
}
