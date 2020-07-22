package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class clsBranchDetails {

    @SerializedName("strBranchName")
    @Expose
    private String strBranchName;

    @SerializedName("strBranchID")
    @Expose
    private String strBranchID;

    public String getStrBranchName() {
        return strBranchName;
    }

    public void setStrBranchName(String strBranchName) {
        this.strBranchName = strBranchName;
    }

    public String getStrBranchID() {
        return strBranchID;
    }

    public void setStrBranchID(String strBranchID) {
        this.strBranchID = strBranchID;
    }
}

