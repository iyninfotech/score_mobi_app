package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ballu on 26/07/17.
 */
public class clsBranchDetailsList {

    @SerializedName("getBranchDetailsList")
    @Expose
    private List<clsBranchDetails> getBranchDetailsList = null;

    public List<clsBranchDetails> getBranchDetailsList() {
        return getBranchDetailsList;
    }

    public void setBranchDetailsList(List<clsBranchDetails> getBranchDetailsList) {
        this.getBranchDetailsList = getBranchDetailsList;
    }

}