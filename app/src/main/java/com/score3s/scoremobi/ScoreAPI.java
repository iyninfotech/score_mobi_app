package com.score3s.scoremobi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ScoreAPI {

    /*@Headers({
            "Accept: application/json; charset=UTF-8",
            "Content-Type: application/json"
    })*/

    //@GET("IRestScoreMobi.svc/getDatabaseDetails/{strProductName}/{strIMEI}")

    @GET("IRestScoreMobi.svc/getDatabaseDetails/")
    Call <clsDatabaseDetailsList> getDatabaseDetails(@Query("strProductName") String _strProductName, @Query("strIMEI") String _strIMEI);

    @GET("IRestScoreMobi.svc/getBranchDetails/")
    Call <clsBranchDetailsList> getBranchDetails(@Query("strServerInstanceName") String _strServerInstanceName, @Query("strDatabaseName") String _strDatabaseName, @Query("strDatabaseUserName") String _strDatabaseUserName, @Query("strDatabasePassword") String _strDatabasePassword);

    @GET("IRestScoreMobi.svc/getDailyDetails/")
    Call <clsDailyDetailsList> getDailyDetails(@Query("strServerInstanceName") String _strServerInstanceName, @Query("strDatabaseName") String _strDatabaseName, @Query("strDatabaseUserName") String _strDatabaseUserName, @Query("strDatabasePassword") String _strDatabasePassword, @Query("dtFromDate") String _dtFromDate, @Query("dtToDate") String _dtToDate, @Query("strBranchID") String _strBranchID, @Query("strDivisionID") String _strDivisionID, @Query("strMainGroupID") String _strMainGroupID, @Query("strCategoryID") String _strCategoryID, @Query("strBrandID") String _strBrandID, @Query("strItemID") String _strItemID, @Query("strMRPID") String _strMRPID, @Query("strRouteID") String _strRouteID, @Query("strSalesmanID") String _strSalesmanID, @Query("strAccountID") String _strAccountID, @Query("strReportBy") String _strReportBy, @Query("strReportFor") String _strReportFor, @Query("strPaymentType") String _strPaymentType, @Query("strInvoiceType") String _strInvoiceType);

    @GET("IRestScoreMobi.svc/getStockDetails/")
    Call <clsStockDetailsList> getStockDetails(@Query("strServerInstanceName") String _strServerInstanceName, @Query("strDatabaseName") String _strDatabaseName, @Query("strDatabaseUserName") String _strDatabaseUserName, @Query("strDatabasePassword") String _strDatabasePassword, @Query("dtFromDate") String _dtFromDate, @Query("dtToDate") String _dtToDate, @Query("strBranchID") String _strBranchID, @Query("strDivisionID") String _strDivisionID, @Query("strMainGroupID") String _strMainGroupID, @Query("strCategoryID") String _strCategoryID, @Query("strBrandID") String _strBrandID, @Query("strItemID") String _strItemID, @Query("strMRPID") String _strMRPID, @Query("strStockType") String _strStockType, @Query("strGodownID") String _strGodownID, @Query("strGroupBy") String _strGroupBy, @Query("strRateAsPer") String _strRateAsPer, @Query("strStockControlType") String _strStockControlType, @Query("strStockTally") String _strStockTally, @Query("strZeroStock") String _strZeroStock);

    @GET("IRestScoreMobi.svc/getAccountDetails/")
    Call <clsAccountDetailsList> getAccountDetails(@Query("strServerInstanceName") String _strServerInstanceName, @Query("strDatabaseName") String _strDatabaseName, @Query("strDatabaseUserName") String _strDatabaseUserName, @Query("strDatabasePassword") String _strDatabasePassword, @Query("strBranchID") String _strBranchID);
}
