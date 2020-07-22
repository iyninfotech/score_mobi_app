package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ballu on 15/09/17.
 */

public class clsStockDetailsList {
    @SerializedName("getStockDetailsResult")

    @Expose
    private List<clsStockDetails> getStockDetailsResult = null;

    public List<clsStockDetails> getGetStockDetailsList() {
        return getStockDetailsResult;
    }

    public void setGetStockDetailsList(List<clsStockDetails> getStockDetailsResult) {
        this.getStockDetailsResult = getStockDetailsResult;
    }
}
