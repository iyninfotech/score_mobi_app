package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ballu on 26/07/17.
 */
public class clsDatabaseDetailsList {

    @SerializedName("getDatabaseDetailsResult")
    @Expose
    private List<clsDatabaseDetails> getDatabaseDetailsResult = null;

    public List<clsDatabaseDetails> getGetDatabaseDetailsResult() {
        return getDatabaseDetailsResult;
    }

    public void setGetDatabaseDetailsResult(List<clsDatabaseDetails> getDatabaseDetailsResult) {
        this.getDatabaseDetailsResult = getDatabaseDetailsResult;
    }
}