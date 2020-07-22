using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.Configuration;
using System.Data.SqlClient;
using System.Data;

namespace ScoreMobi
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "IRestScoreMobi" in code, svc and config file together.
    public class IRestScoreMobi : IIRestScoreMobi
    {
        public List<clsDatabaseDetails> getDatabaseDetails(string _strProductName, string _strIMEI)
        {
            try
            {
                string connect = ConfigurationManager.ConnectionStrings["myconnectionString"].ConnectionString;

                List<clsDatabaseDetails> DatabaseDetailsList = new List<clsDatabaseDetails>();

                using (SqlConnection connection = new SqlConnection(connect))
                {
                    using (SqlCommand command = new SqlCommand())
                    {
                        command.Connection = connection;
                        command.CommandText = "sp_tblClientDetailsGetByIMEI";
                        command.CommandType = CommandType.StoredProcedure;
                        command.CommandTimeout = 600;

                        command.Parameters.AddWithValue("@strProductName", _strProductName);
                        command.Parameters.AddWithValue("@strIMEI", _strIMEI);
                        
                        connection.Open();

                        using (SqlDataReader dataReader = command.ExecuteReader())
                        {
                            if (dataReader != null)
                            {
                                while (dataReader.Read())
                                {
                                    DatabaseDetailsList.Add(new clsDatabaseDetails
                                    {
                                        strCompanyName = dataReader["strCompanyName"].ToString(),
                                        strServerInstanceName = dataReader["strServerInstanceName"].ToString(),
                                        strDatabaseName = dataReader["strDatabaseName"].ToString(),
                                        strDatabaseUserName = dataReader["strDatabaseUserName"].ToString(),
                                        strDatabasePassword = dataReader["strDatabasePassword"].ToString(),
                                        strUserName = dataReader["strUserName"].ToString(),
                                        strModules = dataReader["strModules"].ToString(),
                                        strBranchID = dataReader["strBranchID"].ToString(),
                                        strBranchName = dataReader["strBranchName"].ToString()
                                    });
                                }
                            }
                        }
                    }
                }

                return DatabaseDetailsList;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public List<clsBranchDetails> getBranchDetails(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword)
        {
            try
            {
                //Build connectionstring
                SqlConnectionStringBuilder ConnBuilder = new SqlConnectionStringBuilder();

                // For SQL Server Authentication
                ConnBuilder.DataSource = _strServerInstanceName; // Name of the Sql Server instance
                ConnBuilder.InitialCatalog = _strDatabaseName; // Database Name
                ConnBuilder.UserID = _strDatabaseUserName;
                ConnBuilder.Password = _strDatabasePassword;
                ConnBuilder.ConnectTimeout = 0;
                ConnBuilder.IntegratedSecurity = false;

                List<clsBranchDetails> BranchDetailsList = new List<clsBranchDetails>();

                using (SqlConnection connection = new SqlConnection(ConnBuilder.ConnectionString))
                {
                    using (SqlCommand command = new SqlCommand())
                    {
                        command.Connection = connection;
                        command.CommandText = "sp_GetBranchDetails";
                        command.CommandType = CommandType.StoredProcedure;
                        command.CommandTimeout = 600;

                        connection.Open();

                        using (SqlDataReader dataReader = command.ExecuteReader())
                        {
                            if (dataReader != null)
                            {
                                while (dataReader.Read())
                                {
                                    BranchDetailsList.Add(new clsBranchDetails
                                    {
                                        strBranchName = dataReader["strBranchName"].ToString(),
                                        strBranchID= dataReader["strBranchID"].ToString()
                                    });
                                }
                            }
                        }
                    }
                }

                return BranchDetailsList;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public List<clsDailyDetails> getDailyDetails(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword, DateTime _dtFromDate, DateTime _dtToDate, string _strBranchID = "", string _strDivisionID = "", string _strMainGroupID = "", string _strCategoryID = "", string _strBrandID = "", string _strItemID = "", string _strMRPID = "", string _strRouteID = "", string _strSalesmanID = "", string _strAccountID = "", string _strReportBy = "Item wise", string _strReportFor = "", string _strPaymentType = "", string _strInvoiceType = "")
        {
            try
            {
                //Build connectionstring
                SqlConnectionStringBuilder ConnBuilder = new SqlConnectionStringBuilder();

                // For SQL Server Authentication
                ConnBuilder.DataSource = _strServerInstanceName; // Name of the Sql Server instance
                ConnBuilder.InitialCatalog = _strDatabaseName; // Database Name
                ConnBuilder.UserID = _strDatabaseUserName;
                ConnBuilder.Password = _strDatabasePassword;
                ConnBuilder.ConnectTimeout = 0;
                ConnBuilder.IntegratedSecurity = false;

                List<clsDailyDetails> DailyDetailsList = new List<clsDailyDetails>();

                using (SqlConnection connection = new SqlConnection(ConnBuilder.ConnectionString))
                {
                    using (SqlCommand command = new SqlCommand())
                    {
                        command.Connection = connection;
                        command.CommandText = "sp_rptDailySummary";
                        command.CommandType = CommandType.StoredProcedure;
                        command.CommandTimeout = 600;

                        command.Parameters.AddWithValue("@dtFromDate", _dtFromDate);
                        command.Parameters.AddWithValue("@dtToDate", _dtToDate);
                        command.Parameters.AddWithValue("@strBranchID", _strBranchID);
                        command.Parameters.AddWithValue("@strDivisionID", _strDivisionID);
                        command.Parameters.AddWithValue("@strMainGroupID", _strMainGroupID);
                        command.Parameters.AddWithValue("@strCategoryID", _strCategoryID);
                        command.Parameters.AddWithValue("@strBrandID", _strBrandID);
                        command.Parameters.AddWithValue("@strItemID", _strItemID);
                        command.Parameters.AddWithValue("@strMRPID", _strMRPID);
                        command.Parameters.AddWithValue("@strRouteID", _strRouteID);
                        command.Parameters.AddWithValue("@strSalesmanID", _strSalesmanID);
                        command.Parameters.AddWithValue("@strAccountID", _strAccountID);
                        command.Parameters.AddWithValue("@strReportBy", _strReportBy);
                        command.Parameters.AddWithValue("@strReportFor", _strReportFor);
                        command.Parameters.AddWithValue("@strPaymentType", _strPaymentType);
                        command.Parameters.AddWithValue("@strInvoiceType", _strInvoiceType);
                        
                        connection.Open();

                        using (SqlDataReader dataReader = command.ExecuteReader())
                        {
                            if (dataReader != null)
                            {
                                while (dataReader.Read())
                                {
                                    DailyDetailsList.Add(new clsDailyDetails
                                    {
                                        ITEM = dataReader["ITEM"].ToString(),
                                        ITEMID = dataReader["ITEMID"].ToString(),
                                        decTotalQty = dataReader["decTotalQty"].ToString(),
                                        decGross = (dataReader["decGross"].ToString() == "" ? 0 : (decimal)dataReader["decGross"]),
                                        decTaxableAmount = (dataReader["decTaxableAmount"].ToString() == "" ? 0 : (decimal)dataReader["decTaxableAmount"]),
                                        decTaxAmount = (dataReader["decTaxAmount"].ToString() == "" ? 0 : (decimal)dataReader["decTaxAmount"]),
                                        decItemTotal = (dataReader["decItemTotal"].ToString() == "" ? 0 : (decimal)dataReader["decItemTotal"]),
                                    });
                                }
                            }
                        }
                    }
                }

                return DailyDetailsList;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public List<clsStockDetails> getStockDetails(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword, DateTime _dtFromDate, DateTime _dtToDate, string _strBranchID = "", string _strDivisionID = "", string _strMainGroupID = "", string _strCategoryID = "", string _strBrandID = "", string _strItemID = "", string _strMRPID = "", string _strStockType = "", string _strGodownID = "", string _strGroupBy = "Item wise", string _strRateAsPer = "As per Item Rate", string _strStockControlType = "Closing", string _strStockTally = "No", string _strZeroStock = "No")
        {
            try
            {
                //Build connectionstring
                SqlConnectionStringBuilder ConnBuilder = new SqlConnectionStringBuilder();

                // For SQL Server Authentication
                ConnBuilder.DataSource = _strServerInstanceName; // Name of the Sql Server instance
                ConnBuilder.InitialCatalog = _strDatabaseName; // Database Name
                ConnBuilder.UserID = _strDatabaseUserName;
                ConnBuilder.Password = _strDatabasePassword;
                ConnBuilder.ConnectTimeout = 0;
                ConnBuilder.IntegratedSecurity = false;

                List<clsStockDetails> StockDetailsList = new List<clsStockDetails>();

                using (SqlConnection connection = new SqlConnection(ConnBuilder.ConnectionString))
                {
                    using (SqlCommand command = new SqlCommand())
                    {
                        command.Connection = connection;
                        command.CommandText = "sp_rptStockSummary";
                        command.CommandType = CommandType.StoredProcedure;
                        command.CommandTimeout = 600;

                        command.Parameters.AddWithValue("@df", _dtFromDate);
                        command.Parameters.AddWithValue("@dt", _dtToDate);
                        command.Parameters.AddWithValue("@strBranchID", _strBranchID);
                        command.Parameters.AddWithValue("@strDivisionID", _strDivisionID);
                        command.Parameters.AddWithValue("@strMainGroupID", _strMainGroupID);
                        command.Parameters.AddWithValue("@strCategoryID", _strCategoryID);
                        command.Parameters.AddWithValue("@strBrandID", _strBrandID);
                        command.Parameters.AddWithValue("@strItemID", _strItemID);
                        command.Parameters.AddWithValue("@strMRPID", _strMRPID);
                        command.Parameters.AddWithValue("@strStockType", _strStockType);
                        command.Parameters.AddWithValue("@strGodownID", _strGodownID);
                        command.Parameters.AddWithValue("@strGroupBy", _strGroupBy);
                        command.Parameters.AddWithValue("@strRateAsPer", _strRateAsPer);
                        command.Parameters.AddWithValue("@strStockControlType", _strStockControlType);
                        command.Parameters.AddWithValue("@strStockTally", _strStockTally);
                        command.Parameters.AddWithValue("@strZeroStock", _strZeroStock);
                       
                        connection.Open();

                        using (SqlDataReader dataReader = command.ExecuteReader())
                        {
                            if (dataReader != null)
                            {
                                while (dataReader.Read())
                                {
                                    StockDetailsList.Add(new clsStockDetails
                                    {
                                        ITEM = dataReader["ITEM"].ToString(),
                                        ITEMID = dataReader["ITEMID"].ToString(),
                                        Opening = dataReader["Opening"].ToString(),
                                        OpeningRate = dataReader["OpeningRate"].ToString(),
                                        OpeningValue = dataReader["OpeningValue"].ToString(),
                                        Inward = dataReader["Inward"].ToString(),
                                        InwardRate = dataReader["InwardRate"].ToString(),
                                        InwardValue = dataReader["InwardValue"].ToString(),
                                        Outward = dataReader["Outward"].ToString(),
                                        OutwardRate = dataReader["OutwardRate"].ToString(),
                                        OutwardValue = dataReader["OutwardValue"].ToString(),
                                        ActualClosing = dataReader["ActualClosing"].ToString(),
                                        Variation = dataReader["Variation"].ToString(),
                                        Closing = dataReader["Closing"].ToString(),
                                        ClosingRate = dataReader["ClosingRate"].ToString(),
                                        ClosingValue = dataReader["ClosingValue"].ToString()

                                    });
                                }
                            }
                        }
                    }
                }

                return StockDetailsList;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public List<clsAccountDetails> getAccountDetails(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword, string _strBranchID = "")
        {
            try
            {
                //Build connectionstring
                SqlConnectionStringBuilder ConnBuilder = new SqlConnectionStringBuilder();

                // For SQL Server Authentication
                ConnBuilder.DataSource = _strServerInstanceName; // Name of the Sql Server instance
                ConnBuilder.InitialCatalog = _strDatabaseName; // Database Name
                ConnBuilder.UserID = _strDatabaseUserName;
                ConnBuilder.Password = _strDatabasePassword;
                ConnBuilder.ConnectTimeout = 0;
                ConnBuilder.IntegratedSecurity = false;

                List<clsAccountDetails> AccountDetailsList = new List<clsAccountDetails>();

                using (SqlConnection connection = new SqlConnection(ConnBuilder.ConnectionString))
                {
                    using (SqlCommand command = new SqlCommand())
                    {
                        command.Connection = connection;
                        command.CommandText = "sp_rptAccountRegister";
                        command.CommandType = CommandType.StoredProcedure;
                        command.CommandTimeout = 600;

                        command.Parameters.AddWithValue("@strBranchID", _strBranchID);
                        
                        connection.Open();

                        using (SqlDataReader dataReader = command.ExecuteReader())
                        {
                            if (dataReader != null)
                            {
                                while (dataReader.Read())
                                {
                                    AccountDetailsList.Add(new clsAccountDetails
                                    {
                                        intAccountID = (Int32) dataReader["intAccountID"],
                                        strAccountName = dataReader["strAccountName"].ToString(),
                                        strGroupName = dataReader["strGroupName"].ToString(),
                                        strAccountAddress = dataReader["strAccountAddress"].ToString(),
                                        strAccountPhone = dataReader["strAccountPhone"].ToString(),
                                        strAccountMobile = dataReader["strAccountMobile"].ToString(),
                                        strAccountEmail = dataReader["strAccountEmail"].ToString(),
                                        strCityName = dataReader["strCityName"].ToString(),
                                        decClosing = (dataReader["decClosing"].ToString() == "" ? 0 : (decimal)dataReader["decClosing"]),
                                        strCrDb = dataReader["strCrDb"].ToString()
                                    });
                                }
                            }
                        }
                    }
                }

                return AccountDetailsList;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public List<clsAccountLedger> getAccountLedger(string _strServerInstanceName, string _strDatabaseName, string _strDatabaseUserName, string _strDatabasePassword, DateTime _dtLedgerDateFrom, DateTime _dtLedgerDateTo, string _strBranchID = "", string _strObjectID = "", string _strGroupID = "", string _strAccountIDI = "", Boolean _bitforBank = false, Boolean _bitforOpening = true, string _strBalanceType= "")
        {
            try
            {
                //Build connectionstring
                SqlConnectionStringBuilder ConnBuilder = new SqlConnectionStringBuilder();

                // For SQL Server Authentication
                ConnBuilder.DataSource = _strServerInstanceName; // Name of the Sql Server instance
                ConnBuilder.InitialCatalog = _strDatabaseName; // Database Name
                ConnBuilder.UserID = _strDatabaseUserName;
                ConnBuilder.Password = _strDatabasePassword;
                ConnBuilder.ConnectTimeout = 0;
                ConnBuilder.IntegratedSecurity = false;

                List<clsAccountLedger> AccountLedgerList = new List<clsAccountLedger>();

                using (SqlConnection connection = new SqlConnection(ConnBuilder.ConnectionString))
                {
                    using (SqlCommand command = new SqlCommand())
                    {
                        command.Connection = connection;
                        command.CommandText = "sp_rptAccountLedger";
                        command.CommandType = CommandType.StoredProcedure;
                        command.CommandTimeout = 600;

                        command.Parameters.AddWithValue("@dtLedgerDateFrom", _dtLedgerDateFrom);
                        command.Parameters.AddWithValue("@dtLedgerDateTo", _dtLedgerDateTo);
                        command.Parameters.AddWithValue("@strBranchID", _strBranchID);
                        command.Parameters.AddWithValue("@strObjectID", _strObjectID);
                        command.Parameters.AddWithValue("@strGroupID", _strGroupID);
                        command.Parameters.AddWithValue("@strAccountIDI", _strAccountIDI);
                        command.Parameters.AddWithValue("@bitforBank", _bitforBank);
                        command.Parameters.AddWithValue("@bitforOpening", _bitforOpening);
                        command.Parameters.AddWithValue("@strBalanceType", _strBalanceType);

                        connection.Open();

                        using (SqlDataReader dataReader = command.ExecuteReader())
                        {
                            if (dataReader != null)
                            {
                                while (dataReader.Read())
                                {
                            
                                    AccountLedgerList.Add(new clsAccountLedger
                                    {
                                        strAccountName = dataReader["strAccountName"].ToString(),
                                        strAccountNameII = dataReader["strAccountNameII"].ToString(),
                                        dtLedgerDate = dataReader["dtLedgerDate"].ToString(),
                                        strLedgerNarration = dataReader["strLedgerNarration"].ToString(),
                                        strLedgerReference = dataReader["strLedgerReference"].ToString(),
                                        strObjectBooks = dataReader["strObjectBooks"].ToString(),
                                        decLedgerAmountD = (dataReader["decLedgerAmountD"].ToString() == "" ? 0 : (decimal)dataReader["decLedgerAmountD"]),
                                        decLedgerAmountC = (dataReader["decLedgerAmountC"].ToString() == "" ? 0 : (decimal)dataReader["decLedgerAmountC"])
                                    });
                                }
                            }
                        }
                    }
                }

                return AccountLedgerList;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
    }
}
