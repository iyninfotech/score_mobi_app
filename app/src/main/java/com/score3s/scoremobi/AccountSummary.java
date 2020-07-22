package com.score3s.scoremobi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountSummary extends AppCompatActivity {

    //Root URL of our web service
    public static final String ROOT_URL = "http://www.scoremobi.score3s.com/";

    //List view to show data
    private ListView resultlistView;

    private List<HashMap<String, String>> mAccountDetailsList = new ArrayList<>();

    GlobalActivity globalVariable = null;

    private Spinner spinnerCompanyDetails;
    private Spinner spinnerBranchDetails;

    private static final String KEY_PARTY = "party";
    private static final String KEY_PARTYID = "partyid";
    private static final String KEY_BALANCE = "balance";

    public List<clsAccountDetails> resultAccountDetails;

    String branchstr = "";
    String branchstrid = "";

    String strReportFor="";
    String strCompanyName="";
    String strBranchName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.accountsummary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_actionbar_contact);
        getSupportActionBar().setTitle("Account Summary");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#063b65")));

        globalVariable = (GlobalActivity) getApplicationContext();

        strCompanyName="";
        strBranchName="";

        resultlistView= (ListView) findViewById(R.id.resultlist);
        resultlistView.setOnItemClickListener(mResultListClickListener);

        spinnerCompanyDetails = (Spinner) findViewById(R.id.spinnerCompanyDetails);
        spinnerBranchDetails = (Spinner) findViewById(R.id.spinnerBranchDetails);

        spinnerCompanyDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                setReportDetails();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showCompanyList();

        //SET DEFAULT DATA
        if (strCompanyName!=null && !strCompanyName.equals("")) {
            spinnerCompanyDetails.setSelection(globalVariable.getSpinnerIndex(spinnerCompanyDetails, strCompanyName));
        }

        if (strBranchName!=null && !strBranchName.equals("")) {
            spinnerBranchDetails.setSelection(globalVariable.getSpinnerIndex(spinnerBranchDetails, strBranchName));
        }
    }

    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();
    }

    private AdapterView.OnItemClickListener mResultListClickListener= new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> mAdapterView, View mView,
                                int mPosition, long mLong) {
//            try {
//                if (!strReportBy.equals("Item wise")) {
//                    Intent i = new Intent(getApplicationContext(), AccountSummary.class);
//                    Bundle mBundle = new Bundle();
//                    mBundle.putString("strReportFor", strReportFor);
//                    mBundle.putString("strReportName", strReportName);
//                    mBundle.putString("strCompanyName", spinnerCompanyDetails.getSelectedItem().toString());
//                    mBundle.putString("strBranchName", spinnerBranchDetails.getSelectedItem().toString());
//                    mBundle.putString("strFromDate", edt_fromdate.getText().toString());
//                    mBundle.putString("strToDate", edt_todate.getText().toString());
//
//                    HashMap<String, String> objListView = (HashMap<String, String>) mAdapterView.getItemAtPosition(mPosition);
//                    String strID = (String) objListView.get("itemid").toString();
//
//                    if (strModules.equals("FMCG")) {
//                        if (strReportBy.equals("Division wise")) {
//                            mBundle.putString("strReportBy", "Main Group wise");
//                            mBundle.putString("strDivisionID", strID);
//                            mBundle.putString("strMainGroupID", "");
//                            mBundle.putString("strCategoryID", "");
//                            mBundle.putString("strBrandID", "");
//                        } else if (strReportBy.equals("Main Group wise")) {
//                            mBundle.putString("strReportBy", "Category wise");
//                            mBundle.putString("strDivisionID", strDivisionID);
//                            mBundle.putString("strMainGroupID", strID);
//                            mBundle.putString("strCategoryID", "");
//                            mBundle.putString("strBrandID", "");
//                        } else if (strReportBy.equals("Category wise")) {
//                            mBundle.putString("strReportBy", "Brand wise");
//                            mBundle.putString("strDivisionID", strDivisionID);
//                            mBundle.putString("strMainGroupID", strMainGroupID);
//                            mBundle.putString("strCategoryID", strID);
//                            mBundle.putString("strBrandID", "");
//                        } else if (strReportBy.equals("Brand wise")) {
//                            mBundle.putString("strReportBy", "Item wise");
//                            mBundle.putString("strDivisionID", strDivisionID);
//                            mBundle.putString("strMainGroupID", strMainGroupID);
//                            mBundle.putString("strCategoryID", strCategoryID);
//                            mBundle.putString("strBrandID", strID);
//                        }
//                    }
//                    else
//                    {
//                        mBundle.putString("strDivisionID", strDivisionID);
//                        mBundle.putString("strMainGroupID", strMainGroupID);
//
//                        if (strReportBy.equals("Brand wise")) {
//                            mBundle.putString("strReportBy", "Category wise");
//                            mBundle.putString("strCategoryID", "");
//                            mBundle.putString("strBrandID", strID);
//                        } else if (strReportBy.equals("Category wise")) {
//                            mBundle.putString("strReportBy", "Item wise");
//                            mBundle.putString("strBrandID", strBrandID);
//                            mBundle.putString("strCategoryID", strID);
//                        }
//                    }
//
//                    i.putExtras(mBundle);
//
//                    startActivity(i);
//                }
//            }
//            catch (Exception e) {
//                Toast.makeText(
//                        AccountSummary.this,
//                        "Something got Wrong, Click again", Toast.LENGTH_LONG)
//                        .show();
//            }
        }
    };

    public void btn_viewreport_click(View v) {
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Account Data","Please wait...",false,false);

        try
        {
            GsonBuilder gsonb = new GsonBuilder();
            final Gson gson = gsonb.create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ScoreAPI api = retrofit.create(ScoreAPI.class);

            clsDatabaseDetails _clsDatabaseDetails=null;

            //Traversing through the whole list to get all the names
            for (int i = 0; i < globalVariable.lstDatabaseDetails.size(); i++) {
                //Storing names to string array
                if (globalVariable.lstDatabaseDetails.get(i).getStrCompanyName().toString().equals(spinnerCompanyDetails.getSelectedItem().toString())) {
                    _clsDatabaseDetails = globalVariable.lstDatabaseDetails.get(i);
                    branchstr = globalVariable.lstDatabaseDetails.get(i).getStrBranchName();
                    branchstrid = globalVariable.lstDatabaseDetails.get(i).getStrBranchID();
                }
            }

            //BRANCH LIST
            String[] branchlist =branchstr.split(",");
            String[] branchidlist =branchstrid.split(",");

            Spinner spinnerBranchDetails = (Spinner) findViewById(R.id.spinnerBranchDetails);

            String strBranchID="";
            for (int j=0;j<=branchlist.length-1;j++) {
                if (spinnerBranchDetails.getSelectedItem().toString().equals(branchlist[j].toString()))
                {
                    if (strBranchID.equals(""))
                    {
                        strBranchID=branchidlist[j].toString();
                    }
                    else {
                        strBranchID=strBranchID + "," + branchidlist[j].toString();
                    }
                }
            }

            if (strBranchID.equals("0"))
            {
                strBranchID="";
            }

            if (_clsDatabaseDetails != null) {
                Call<clsAccountDetailsList> call = api.getAccountDetails(_clsDatabaseDetails.getStrServerInstanceName(), _clsDatabaseDetails.getStrDatabaseName(), _clsDatabaseDetails.getStrDatabaseUserName(), _clsDatabaseDetails.getStrDatabasePassword(), strBranchID);
                call.enqueue(new Callback<clsAccountDetailsList>() {
                    @Override
                    public void onResponse(Call<clsAccountDetailsList> call, Response<clsAccountDetailsList> response) {
                        if (response.isSuccessful()) {
                            resultAccountDetails = response.body().getGetAccountDetailsResult();

                            fillListView();
                            loading.dismiss();
                        } else {
                            loading.dismiss();

                            AlertDialog.Builder alert = new AlertDialog.Builder(AccountSummary.this);
                            alert.setTitle("Failure");
                            alert.setIcon(R.drawable.ic_action_warning);
                            alert.setMessage("Repsonse is Un-Successful" + response.body());
                            alert.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    System.exit(0);
                                }
                            });
                            alert.create().show();
                        }
                    }

                    @Override
                    public void onFailure(Call<clsAccountDetailsList> call, Throwable t) {
                        loading.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(AccountSummary.this);
                        alert.setTitle("Failure");
                        alert.setIcon(R.drawable.ic_action_warning);
                        alert.setMessage(t.getMessage());
                        alert.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        });
                        alert.create().show();
                    }
                });
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(AccountSummary.this,"Error in " + strReportFor + " View Report Click" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            loading.dismiss();
        }
    }

    private void fillListView()
    {
        try {

            mAccountDetailsList =new ArrayList<>();

            for (clsAccountDetails _result : resultAccountDetails) {

                HashMap<String, String> map = new HashMap<>();

                map.put(KEY_PARTY, globalVariable.getnullStr(_result.getStrAccountName()));
                map.put(KEY_PARTYID, globalVariable.getnullStr(_result.getIntAccountID().toString()));
                map.put(KEY_BALANCE, String.format("%.2f", _result.getDecClosing()) + " " + globalVariable.getnullStr(_result.getStrCrDb().toString()));

                mAccountDetailsList.add(map);
            }

            ListAdapter adapter = new SimpleAdapter(AccountSummary.this, mAccountDetailsList, R.layout.accountsummary_row,
                    new String[] {KEY_PARTY, KEY_BALANCE},
                    new int[] { R.id.party,R.id.balance});

            resultlistView.setAdapter(adapter);

        }
        catch(Exception ex)
        {
            Toast.makeText(AccountSummary.this,"Error in " + strReportFor + " Filling Account Details List View " + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    //Our method to show list
    private void showCompanyList() {
        //String array to store all the book names
        String[] companylist = new String[globalVariable.lstDatabaseDetails.size()];

        //Traversing through the whole list to get all the names
        for (int i = 0; i < globalVariable.lstDatabaseDetails.size(); i++) {
            //Storing names to string array
            companylist[i] = globalVariable.lstDatabaseDetails.get(i).getStrCompanyName();
        }

        //Creating an array adapter for Spinner
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                companylist);

        //Setting adapter to listview
        spinnerCompanyDetails.setAdapter(spinnerArrayAdapter);
        spinnerCompanyDetails.setSelection(0);

        if (companylist.length==1)
        {
            spinnerCompanyDetails.setEnabled(false);
        }
        else
        {
            spinnerCompanyDetails.setEnabled(true);
        }
    }

    //Our method to show list
    private void setReportDetails() {
        //Traversing through the whole list to get all the names
        for (int i = 0; i < globalVariable.lstDatabaseDetails.size(); i++) {
            //Storing names to string array
            if (globalVariable.lstDatabaseDetails.get(i).getStrCompanyName().toString().equals(spinnerCompanyDetails.getSelectedItem().toString())) {
                branchstr = globalVariable.lstDatabaseDetails.get(i).getStrBranchName();
                branchstrid = globalVariable.lstDatabaseDetails.get(i).getStrBranchID();
            }
        }

        //BRANCH LIST
        String[] branchlist =branchstr.replace("All Branch,", "").split(",");

        //Creating an array adapter for Spinner
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                branchlist);

        //Setting adapter to listview
        spinnerBranchDetails.setAdapter(spinnerArrayAdapter);
        spinnerBranchDetails.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
