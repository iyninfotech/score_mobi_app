package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ballu on 15/09/17.
 */

public class clsDailyDetailsList {
    @SerializedName("getDailyDetailsResult")
    @Expose
    private List<clsDailyDetails> getDailyDetailsResult = null;

    public List<clsDailyDetails> getGetDailyDetailsList() {
        return getDailyDetailsResult;
    }

    public void setGetDailyDetailsList(List<clsDailyDetails> getDailyDetailsResult) {
        this.getDailyDetailsResult = getDailyDetailsResult;
    }
}
