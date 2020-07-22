package com.score3s.scoremobi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ballu on 15/09/17.
 */

public class clsAccountDetails {
    @SerializedName("decClosing")
    @Expose
    private Double decClosing;
    @SerializedName("intAccountID")
    @Expose
    private Integer intAccountID;
    @SerializedName("strAccountAddress")
    @Expose
    private String strAccountAddress;
    @SerializedName("strAccountEmail")
    @Expose
    private String strAccountEmail;
    @SerializedName("strAccountMobile")
    @Expose
    private String strAccountMobile;
    @SerializedName("strAccountName")
    @Expose
    private String strAccountName;
    @SerializedName("strAccountPhone")
    @Expose
    private String strAccountPhone;
    @SerializedName("strCityName")
    @Expose
    private String strCityName;
    @SerializedName("strCrDb")
    @Expose
    private String strCrDb;
    @SerializedName("strGroupName")
    @Expose
    private String strGroupName;

    public Double getDecClosing() {
        return decClosing;
    }

    public void setDecClosing(Double decClosing) {
        this.decClosing = decClosing;
    }

    public Integer getIntAccountID() {
        return intAccountID;
    }

    public void setIntAccountID(Integer intAccountID) {
        this.intAccountID = intAccountID;
    }

    public String getStrAccountAddress() {
        return strAccountAddress;
    }

    public void setStrAccountAddress(String strAccountAddress) {
        this.strAccountAddress = strAccountAddress;
    }

    public String getStrAccountEmail() {
        return strAccountEmail;
    }

    public void setStrAccountEmail(String strAccountEmail) {
        this.strAccountEmail = strAccountEmail;
    }

    public String getStrAccountMobile() {
        return strAccountMobile;
    }

    public void setStrAccountMobile(String strAccountMobile) {
        this.strAccountMobile = strAccountMobile;
    }

    public String getStrAccountName() {
        return strAccountName;
    }

    public void setStrAccountName(String strAccountName) {
        this.strAccountName = strAccountName;
    }

    public String getStrAccountPhone() {
        return strAccountPhone;
    }

    public void setStrAccountPhone(String strAccountPhone) {
        this.strAccountPhone = strAccountPhone;
    }

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
    }

    public String getStrCrDb() {
        return strCrDb;
    }

    public void setStrCrDb(String strCrDb) {
        this.strCrDb = strCrDb;
    }

    public String getStrGroupName() {
        return strGroupName;
    }

    public void setStrGroupName(String strGroupName) {
        this.strGroupName = strGroupName;
    }
}
