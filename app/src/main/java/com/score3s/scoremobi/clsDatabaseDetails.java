package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class clsDatabaseDetails {

    @SerializedName("strBranchID")
    @Expose
    private String strBranchID;

    @SerializedName("strBranchName")
    @Expose
    private String strBranchName;

    @SerializedName("strCompanyName")
    @Expose
    private String strCompanyName;
    @SerializedName("strDatabaseName")
    @Expose
    private String strDatabaseName;
    @SerializedName("strDatabasePassword")
    @Expose
    private String strDatabasePassword;
    @SerializedName("strDatabaseUserName")
    @Expose
    private String strDatabaseUserName;
    @SerializedName("strModules")
    @Expose
    private String strModules;
    @SerializedName("strServerInstanceName")
    @Expose
    private String strServerInstanceName;
    @SerializedName("strUserName")
    @Expose
    private String strUserName;

    public String getStrBranchID() {
        return strBranchID;
    }

    public void setStrBranchID(String strBranchID) {
        this.strBranchID = strBranchID;
    }

    public String getStrBranchName() {
        return strBranchName;
    }

    public void setStrBranchName(String strBranchName) {
        this.strBranchName = strBranchName;
    }

    public String getStrCompanyName() {
        return strCompanyName;
    }

    public void setStrCompanyName(String strCompanyName) {
        this.strCompanyName = strCompanyName;
    }

    public String getStrDatabaseName() {
        return strDatabaseName;
    }

    public void setStrDatabaseName(String strDatabaseName) {
        this.strDatabaseName = strDatabaseName;
    }

    public String getStrDatabasePassword() {
        return strDatabasePassword;
    }

    public void setStrDatabasePassword(String strDatabasePassword) {
        this.strDatabasePassword = strDatabasePassword;
    }

    public String getStrDatabaseUserName() {
        return strDatabaseUserName;
    }

    public void setStrDatabaseUserName(String strDatabaseUserName) {
        this.strDatabaseUserName = strDatabaseUserName;
    }

    public String getStrModules() {
        return strModules;
    }

    public void setStrModules(String strModules) {
        this.strModules = strModules;
    }

    public String getStrServerInstanceName() {
        return strServerInstanceName;
    }

    public void setStrServerInstanceName(String strServerInstanceName) {
        this.strServerInstanceName = strServerInstanceName;
    }

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }
}
