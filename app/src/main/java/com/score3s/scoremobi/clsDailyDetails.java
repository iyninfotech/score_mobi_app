package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ballu on 15/09/17.
 */

public class clsDailyDetails {
    @SerializedName("ITEM")
    @Expose
    private String ITEM;

    @SerializedName("ITEMID")
    @Expose
    private String ITEMID;

    @SerializedName("decGross")
    @Expose
    private Double decGross;
    @SerializedName("decItemTotal")
    @Expose
    private Double decItemTotal;
    @SerializedName("decTaxAmount")
    @Expose
    private Double decTaxAmount;
    @SerializedName("decTaxableAmount")
    @Expose
    private Double decTaxableAmount;
    @SerializedName("decTotalQty")
    @Expose
    private String decTotalQty;

    public String getITEM() {
        return ITEM;
    }

    public void setITEM(String ITEM) {
        this.ITEM = ITEM;
    }

    public String getITEMID() {
        return ITEMID;
    }

    public void setITEMID(String ITEMID) {
        this.ITEMID = ITEMID;
    }

    public Double getDecGross() {
        return decGross;
    }

    public void setDecGross(Double decGross) {
        this.decGross = decGross;
    }

    public Double getDecItemTotal() {
        return decItemTotal;
    }

    public void setDecItemTotal(Double decItemTotal) {
        this.decItemTotal = decItemTotal;
    }

    public Double getDecTaxAmount() {
        return decTaxAmount;
    }

    public void setDecTaxAmount(Double decTaxAmount) {
        this.decTaxAmount = decTaxAmount;
    }

    public Double getDecTaxableAmount() {
        return decTaxableAmount;
    }

    public void setDecTaxableAmount(Double decTaxableAmount) {
        this.decTaxableAmount = decTaxableAmount;
    }

    public String getDecTotalQty() {
        return decTotalQty;
    }

    public void setDecTotalQty(String decTotalQty) {
        this.decTotalQty = decTotalQty;
    }
}
