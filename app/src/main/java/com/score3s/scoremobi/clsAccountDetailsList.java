package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ballu on 15/09/17.
 */

public class clsAccountDetailsList {
    @SerializedName("getAccountDetailsResult")
    @Expose
    private List<clsAccountDetails> getAccountDetailsResult = null;

    public List<clsAccountDetails> getGetAccountDetailsResult() {
        return getAccountDetailsResult;
    }

    public void setGetAccountDetailsResult(List<clsAccountDetails> getAccountDetailsResult) {
        this.getAccountDetailsResult = getAccountDetailsResult;
    }
}
