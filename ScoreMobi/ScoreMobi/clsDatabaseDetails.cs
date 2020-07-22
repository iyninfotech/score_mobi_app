using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ScoreMobi
{
    public class clsDatabaseDetails
    {
        public string strCompanyName;
        public string strServerInstanceName;
        public string strDatabaseName;
        public string strDatabaseUserName;
        public string strDatabasePassword;
        public string strUserName;
        public string strModules;
        public string strBranchID;
        public string strBranchName;
    }

    public class clsBranchDetails
    {
        public string strBranchName;
        public string strBranchID;
    }

    public class clsDailyDetails
    {
        public string ITEM;
        public string ITEMID;
        public string decTotalQty;

        public decimal decGross;
        public decimal decTaxableAmount;
        public decimal decTaxAmount;
        public decimal decItemTotal;
    }

    public class clsStockDetails
    {
        public string ITEM;
        public string ITEMID;
        public string Opening;
        public string OpeningRate;
        public string OpeningValue;
        public string Inward;
        public string InwardRate;
        public string InwardValue;
        public string Outward;
        public string OutwardRate;
        public string OutwardValue;
        public string ActualClosing;
        public string Variation;
        public string Closing;
        public string ClosingRate;
        public string ClosingValue;
    }

    public class clsAccountDetails
    {
        public Int32 intAccountID;
        
        public string strAccountName;
        public string strGroupName;
        public string strAccountAddress;
        public string strAccountPhone;
        public string strAccountMobile;
        public string strAccountEmail;
        public string strCityName;
        
        public decimal decClosing;

        public string strCrDb;
    }

    public class clsAccountLedger
    {
        public string strAccountName;
        public string strAccountNameII;
        public string dtLedgerDate;
        public string strLedgerNarration;
        public string strLedgerReference;
        public string strObjectBooks;
        public decimal decLedgerAmountD;
        public decimal decLedgerAmountC;
    }
}