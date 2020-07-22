using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.ServiceModel.Web;

namespace ScoreMobi
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IIRestScoreMobi" in both code and config file together.
    [ServiceContract]
    public interface IIRestScoreMobi
    {
        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/GetDatabaseDetails/?strProductName={_strProductName}&strIMEI={_strIMEI}"
            )
        ]
        List<clsDatabaseDetails> getDatabaseDetails(string _strProductName, string _strIMEI);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetBranchDetails/?strServerInstanceName={_strServerInstanceName}&strDatabaseName={_strDatabaseName}&strDatabaseUserName={_strDatabaseUserName}&strDatabasePassword={_strDatabasePassword}"
            )
        ]
        List<clsBranchDetails> getBranchDetails(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/GetDailyDetails/?strServerInstanceName={_strServerInstanceName}&strDatabaseName={_strDatabaseName}&strDatabaseUserName={_strDatabaseUserName}&strDatabasePassword={_strDatabasePassword}&dtFromDate={_dtFromDate}&dtToDate={_dtToDate}&strBranchID={_strBranchID}&strDivisionID={_strDivisionID}&strMainGroupID={_strMainGroupID}&strCategoryID={_strCategoryID}&strBrandID={_strBrandID}&strItemID={_strItemID}&strMRPID={_strMRPID}&strRouteID={_strRouteID}&strSalesmanID={_strSalesmanID}&strAccountID={_strAccountID}&strReportBy={_strReportBy}&strReportFor={_strReportFor}&strPaymentType={_strPaymentType}&strInvoiceType={_strInvoiceType}"
            )
        ]
        List<clsDailyDetails> getDailyDetails(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword, DateTime _dtFromDate, DateTime _dtToDate, string _strBranchID = "", string _strDivisionID = "", string _strMainGroupID = "", string _strCategoryID = "", string _strBrandID = "", string _strItemID = "", string _strMRPID = "", string _strRouteID = "", string _strSalesmanID = "", string _strAccountID = "", string _strReportBy = "Item wise", string _strReportFor = "", string _strPaymentType = "", string _strInvoiceType = "");

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/GetStockDetails/?strServerInstanceName={_strServerInstanceName}&strDatabaseName={_strDatabaseName}&strDatabaseUserName={_strDatabaseUserName}&strDatabasePassword={_strDatabasePassword}&dtFromDate={_dtFromDate}&dtToDate={_dtToDate}&strBranchID={_strBranchID}&strDivisionID={_strDivisionID}&strMainGroupID={_strMainGroupID}&strCategoryID={_strCategoryID}&strBrandID={_strBrandID}&strItemID={_strItemID}&strMRPID={_strMRPID}&strStockType={_strStockType}&strGodownID={_strGodownID}&strGroupBy={_strGroupBy}&strRateAsPer={_strRateAsPer}&strStockControlType={_strStockControlType}&strStockTally={_strStockTally}&strZeroStock={_strZeroStock}"
            )
        ]
        List<clsStockDetails> getStockDetails(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword, DateTime _dtFromDate, DateTime _dtToDate, string _strBranchID = "", string _strDivisionID = "", string _strMainGroupID = "", string _strCategoryID = "", string _strBrandID = "", string _strItemID = "", string _strMRPID = "", string _strStockType = "", string _strGodownID = "", string _strGroupBy = "Item wise", string _strRateAsPer = "As per Item Rate", string _strStockControlType = "Closing", string _strStockTally = "No", string _strZeroStock = "No");

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/GetAccountDetails/?strServerInstanceName={_strServerInstanceName}&strDatabaseName={_strDatabaseName}&strDatabaseUserName={_strDatabaseUserName}&strDatabasePassword={_strDatabasePassword}&strBranchID={_strBranchID}"
            )
        ]

        List<clsAccountDetails> getAccountDetails(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword, string _strBranchID = "");

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/GetAccountLedger/?strServerInstanceName={_strServerInstanceName}&strDatabaseName={_strDatabaseName}&strDatabaseUserName={_strDatabaseUserName}&strDatabasePassword={_strDatabasePassword}&dtLedgerDateFrom={_dtLedgerDateFrom}&dtLedgerDateTo={_dtLedgerDateTo}&strBranchID={_strBranchID}&strObjectID={_strObjectID}&strGroupID={_strGroupID}&strAccountIDI={_strAccountIDI}&bitforBank={_bitforBank}&bitforOpening={_bitforOpening}&strBalanceType={_strBalanceType}"
            )
        ]

        List<clsAccountLedger> getAccountLedger(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword, DateTime _dtLedgerDateFrom, DateTime _dtLedgerDateTo, string _strBranchID = "", string _strObjectID = "", string _strGroupID = "", string _strAccountIDI = "", Boolean _bitforBank = false, Boolean _bitforOpening = true, string _strBalanceType = "");
    }
}
