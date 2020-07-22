package com.score3s.scoremobi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
//import android.support.design.widget.FloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class DailySummary extends AppCompatActivity {

    //List view to show data
    private ListView resultlistView;

    //ArrayList that will hold the original Data
    ArrayList<HashMap<String, Object>> mDailyDetailsListResult;
    LayoutInflater inflater;

    GlobalActivity globalVariable = null;

    private TextView txtReportBy;
    private Button titleCompany;
    private TextView titleDuration;

    EditText edt_fromdate = null;
    EditText edt_todate = null;

    TextView totalqty = null;
    TextView totalgross = null;

    TextView totalqtyselected = null;
    TextView totalgrossselected = null;

    CheckBox checkAll= null;

    boolean boolConfirmClick=false;

    LinearLayout resulttotalselected=null;
    View viewselected=null;

    double _dblTotalQtySelected=0;
    double _dblTotalGrossSelected=0;

    String strID = "";

    static final int DATE_DIALOG_IDFD = 1;
    static final int DATE_DIALOG_IDTD = 2;

    private int year;
    private int month;
    private int day;

    private int yearto;
    private int monthto;
    private int dayto;

    public List<clsDailyDetails> resultDailyDetails;

    String branchstr = "";
    String branchstrid = "";
    String reportby = "";

    String strModules = "";

    String strReportFor="";
    String strReportName="";
    String strCompanyName="";
    String strBranchName="";
    String strFromDate="";
    String strToDate="";
    String strReportBy="";
    String strInvoiceType="";

    String strDivisionID="";
    String strMainGroupID="";
    String strCategoryID="";
    String strBrandID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dailysummary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_actionbar_contact);
        getSupportActionBar().setTitle("Daily Summary");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#063b65")));

        globalVariable = (GlobalActivity) getApplicationContext();

        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        resulttotalselected=(LinearLayout) findViewById(R.id.resulttotalselected);
        viewselected=(View) findViewById(R.id.viewselected);

        checkAll= (CheckBox) findViewById(R.id.checkAll);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                fillListView(isChecked);
            }
        });

        strCompanyName = "";
        strBranchName = "";
        strFromDate = globalVariable.getDATEString(0);
        strToDate = globalVariable.getDATEString(0);
        strReportBy = "";
        strInvoiceType = "";

        strDivisionID = "";
        strMainGroupID = "";
        strCategoryID = "";
        strBrandID = "";

        resultlistView = (ListView) findViewById(R.id.resultlist);
        resultlistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        resultlistView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox cb = (CheckBox) view.findViewById(R.id.checkMark);
                cb.performClick();
            }
        });

        txtReportBy= (TextView) findViewById(R.id.txtReportBy);

        txtReportBy.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count)
            {
                if (!boolConfirmClick) {
                    viewReport();
                }
            }

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after)
            {}

            @Override
            public void afterTextChanged( final Editable s)
            {
            }
        });

        titleCompany= (Button) findViewById(R.id.titleCompany);
        titleDuration= (TextView) findViewById(R.id.titleDuration);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getSupportActionBar().setTitle(extras.getString("strReportName"));
            strReportFor=extras.getString("strReportFor");
            strReportName=extras.getString("strReportName");
            strCompanyName=extras.getString("strCompanyName");
            strBranchName=extras.getString("strBranchName");
            strFromDate=extras.getString("strFromDate");
            strToDate=extras.getString("strToDate");
            strReportBy=extras.getString("strReportBy");
            strInvoiceType=extras.getString("strInvoiceType");

            strDivisionID=extras.getString("strDivisionID");
            strMainGroupID=extras.getString("strMainGroupID");
            strCategoryID=extras.getString("strCategoryID");
            strBrandID=extras.getString("strBrandID");
        }

//        if (strReportBy.equals("Item wise")) {
//            checkAll.setVisibility(View.GONE);
//        }
//        else {
//            checkAll.setVisibility(View.VISIBLE);
//        }

        setDefaultValues();

        if (strReportFor.contains("Purchase"))
        {
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_actionbar_purchase);
        }
        else if (strReportFor.contains("Material Sales"))
        {
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_actionbar_sale);
        }

        totalqty = (TextView) findViewById(R.id.totalqty);
        totalgross = (TextView) findViewById(R.id.totalgross);

        totalqtyselected = (TextView) findViewById(R.id.totalqtyselected);
        totalgrossselected = (TextView) findViewById(R.id.totalgrossselected);

        showNoConnectionDialog(this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();
    }

    private void viewSubReport() {
        try {
            if (!strReportBy.equals("Item wise")) {
                Intent i = new Intent(getApplicationContext(), DailySummary.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("strReportFor", strReportFor);
                mBundle.putString("strReportName", strReportName);
                mBundle.putString("strCompanyName", strCompanyName);
                mBundle.putString("strBranchName", strBranchName);
                mBundle.putString("strFromDate", strFromDate);
                mBundle.putString("strToDate", strToDate);
                mBundle.putBoolean("boolIsLoad", false);
                mBundle.putString("strInvoiceType", strInvoiceType);

                if (strModules.equals("FMCG")) {
                    if (strReportBy.equals("Division wise")) {
                        mBundle.putString("strReportBy", "Main Group wise");
                        mBundle.putString("strDivisionID", strID);
                        mBundle.putString("strMainGroupID", "");
                        mBundle.putString("strCategoryID", "");
                        mBundle.putString("strBrandID", "");
                    } else if (strReportBy.equals("Main Group wise")) {
                        mBundle.putString("strReportBy", "Category wise");
                        mBundle.putString("strDivisionID", strDivisionID);
                        mBundle.putString("strMainGroupID", strID);
                        mBundle.putString("strCategoryID", "");
                        mBundle.putString("strBrandID", "");
                    } else if (strReportBy.equals("Category wise")) {
                        mBundle.putString("strReportBy", "Brand wise");
                        mBundle.putString("strDivisionID", strDivisionID);
                        mBundle.putString("strMainGroupID", strMainGroupID);
                        mBundle.putString("strCategoryID", strID);
                        mBundle.putString("strBrandID", "");
                    } else if (strReportBy.equals("Brand wise")) {
                        mBundle.putString("strReportBy", "Item wise");
                        mBundle.putString("strDivisionID", strDivisionID);
                        mBundle.putString("strMainGroupID", strMainGroupID);
                        mBundle.putString("strCategoryID", strCategoryID);
                        mBundle.putString("strBrandID", strID);
                    }
                }
                else
                {
                    mBundle.putString("strDivisionID", strDivisionID);
                    mBundle.putString("strMainGroupID", strMainGroupID);

                    if (strReportBy.equals("Brand wise")) {
                        mBundle.putString("strReportBy", "Category wise");
                        mBundle.putString("strCategoryID", "");
                        mBundle.putString("strBrandID", strID);
                    } else if (strReportBy.equals("Category wise")) {
                        mBundle.putString("strReportBy", "Item wise");
                        mBundle.putString("strBrandID", strBrandID);
                        mBundle.putString("strCategoryID", strID);
                    }
                }

                i.putExtras(mBundle);

                startActivity(i);
            }
        }
        catch (Exception e) {
            Toast.makeText(
                    DailySummary.this,
                    "Sub Report Click Failed, Click again", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void viewReport()
    {
        resulttotalselected.setVisibility(View.GONE);
        viewselected.setVisibility(View.GONE);

//        if (strReportBy.equals("Item wise")) {
//            checkAll.setVisibility(View.GONE);
//        }
//        else {
//            checkAll.setVisibility(View.VISIBLE);
//        }

        strID="";
        //strReportBy=globalVariable.getnullStr(this.spinnerReportBy.getSelectedItem().toString());
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Report Data","Please wait...",false,false);

        try
        {
            String _fromDate = strFromDate.substring(6,10) + "-" + strFromDate.substring(3,5) + "-" + strFromDate.substring(0,2);
            String _toDate = strToDate.substring(6,10) + "-" + strToDate.substring(3,5) + "-" + strToDate.substring(0,2);

            ScoreAPI api = globalVariable.getScoreAPI();

            clsDatabaseDetails _clsDatabaseDetails=null;

            //Traversing through the whole list to get all the names
            for (int i = 0; i < globalVariable.lstDatabaseDetails.size(); i++) {
                //Storing names to string array
                if (globalVariable.lstDatabaseDetails.get(i).getStrCompanyName().toString().equals(strCompanyName)) {
                    _clsDatabaseDetails = globalVariable.lstDatabaseDetails.get(i);
                    branchstr = globalVariable.lstDatabaseDetails.get(i).getStrBranchName();
                    branchstrid = globalVariable.lstDatabaseDetails.get(i).getStrBranchID();
                    strModules= globalVariable.lstDatabaseDetails.get(i).getStrModules();
                }
            }

            //BRANCH LIST
            String[] branchlist =branchstr.split(",");
            String[] branchidlist =branchstrid.split(",");

            String strBranchID="";
            for (int j=0;j<=branchlist.length-1;j++) {
                if (strBranchName.equals(branchlist[j].toString()))
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
                Call<clsDailyDetailsList> call = api.getDailyDetails(_clsDatabaseDetails.getStrServerInstanceName(), _clsDatabaseDetails.getStrDatabaseName(), _clsDatabaseDetails.getStrDatabaseUserName(), _clsDatabaseDetails.getStrDatabasePassword(), _fromDate, _toDate, strBranchID, strDivisionID, strMainGroupID, strCategoryID, strBrandID, "", "", "", "", "", strReportBy, strReportFor,"",strInvoiceType);
                call.enqueue(new Callback<clsDailyDetailsList>() {
                    @Override
                    public void onResponse(Call<clsDailyDetailsList> call, Response<clsDailyDetailsList> response) {
                        if (response.isSuccessful()) {
                            resultDailyDetails = response.body().getGetDailyDetailsList();

                            fillListView(checkAll.isChecked() ? true : false);
                            loading.dismiss();
                        } else {
                            loading.dismiss();

                            AlertDialog.Builder alert = new AlertDialog.Builder(DailySummary.this);
                            alert.setTitle("Failure");
                            alert.setIcon(R.drawable.ic_action_warning);
                            alert.setMessage("Repsonse is Un-Successful" + response.errorBody().toString());
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
                    public void onFailure(Call<clsDailyDetailsList> call, Throwable t) {
                        loading.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(DailySummary.this);
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
            Toast.makeText(DailySummary.this,"Error in " + strReportFor + " View Report Click" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            loading.dismiss();
        }
    }
//    public void btn_viewreport_click(View v) {
//        viewReport();
//    }

    public void btn_viewsubreport_click(View v) {
        viewSubReport();
    }

    private void fillListView(boolean _allCheck)
    {
        try {

            double _dblTotalQty=0;
            double _dblTotalGross=0;

            mDailyDetailsListResult=new ArrayList<HashMap<String,Object>>();

            HashMap<String , Object> temp;

            for (clsDailyDetails _result : resultDailyDetails) {
                temp=new HashMap<String, Object>();

                temp.put("item", globalVariable.getnullStr(_result.getITEM()));
                temp.put("itemid", globalVariable.getnullStr(_result.getITEMID()));
                temp.put("qty", globalVariable.getnullStr(_result.getDecTotalQty()));
                temp.put("gross", globalVariable.getnullStr(_result.getDecGross().toString()));

                _dblTotalQty=_dblTotalQty + globalVariable.getnulltxtDbl(_result.getDecTotalQty().toString());
                _dblTotalGross=_dblTotalGross + globalVariable.getnulltxtDbl(_result.getDecGross().toString());

                mDailyDetailsListResult.add(temp);
            }

            final CustomAdapter adapter=new CustomAdapter(this, R.layout.resultlist_row, mDailyDetailsListResult, _allCheck);

            //finally,set the adapter to the default ListView
            resultlistView.setAdapter(adapter);

            if((_dblTotalQty-(int)_dblTotalQty)!=0) {
                totalqty.setText(String.valueOf(_dblTotalQty));
            }
            else {
                totalqty.setText(String.format("%.0f",_dblTotalQty));
            }

            totalgross.setText(String.format("%.2f",_dblTotalGross));
        }
        catch(Exception ex)
        {
            Toast.makeText(DailySummary.this,"Error in " + strReportFor + " Filling List View " + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
    @SuppressWarnings("deprecation")
    public void openFromDateDialog(View v) {
        try {
            showDialog(DATE_DIALOG_IDFD);
        } catch (Exception ex) {
            Toast.makeText(this,
                    "From Date Picker failed" + ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("deprecation")
    public void openToDateDialog(View v) {
        try {
            showDialog(DATE_DIALOG_IDTD);
        } catch (Exception ex) {
            Toast.makeText(this,
                    "To Date Picker failed" + ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // display current date
    public void setFromDateOnView(String dt, DatePicker dpFromDate) {

        final Calendar c = Calendar.getInstance();

        if (!dt.equals("")) {
            year = Integer.valueOf(dt.substring(6, 10));
            month = Integer.valueOf(dt.substring(3, 5)) - 1;
            day = Integer.valueOf(dt.substring(0, 2));
        } else {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        // set current date into datepicker
        dpFromDate.init(year, month, day, null);
    }

    public void setToDateOnView(String dt, DatePicker dpToDate) {

        final Calendar ce = Calendar.getInstance();

        if (!dt.equals("")) {
            yearto = Integer.valueOf(dt.substring(6, 10));
            monthto = Integer.valueOf(dt.substring(3, 5)) - 1;
            dayto = Integer.valueOf(dt.substring(0, 2));
        } else {
            yearto = ce.get(Calendar.YEAR);
            monthto = ce.get(Calendar.MONTH);
            dayto = ce.get(Calendar.DAY_OF_MONTH);
        }

        // set current date into datepicker
        dpToDate.init(yearto, monthto, dayto, null);
    }

    public void showNoConnectionDialog(final Context c) {
        if(globalVariable.isNetworkAvailable())
        {
            return;
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(c);
            alert.setTitle("No Connection");
            alert.setIcon(R.drawable.ic_action_warning);
            alert.setMessage("Cannot connect to the Internet!");
            alert.setPositiveButton("Retry", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!globalVariable.isNetworkAvailable())
                        showNoConnectionDialog(c);
                }
            });
            alert.setNegativeButton("Exit", new AlertDialog.OnClickListener() {
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_IDFD:
                // set date picker as current date
                DatePickerDialog drd = new DatePickerDialog(this,
                        datePickerListenerFD, year, month, day);
                drd.setTitle("From Date");
                return drd;

            case DATE_DIALOG_IDTD:
                // set date picker as current date
                DatePickerDialog ded = new DatePickerDialog(this,
                        datePickerListenerTD, yearto, monthto, dayto);
                ded.setTitle("To Date");
                return ded;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerFD = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            String months = "0" + String.valueOf(selectedMonth + 1);
            String days = "0" + String.valueOf(day);

            days = days.substring(days.length() - 2);
            months = months.substring(months.length() - 2);

            String years = String.valueOf(year);

            edt_fromdate.setText(days + "/" + months + "/" + years);

            // set selected date into datepicker also
            view.init(year, month, day, null);

            if (globalVariable.getnullStr(edt_fromdate.getText().toString())
                    .isEmpty()) {
                edt_fromdate.performClick();
            } else {
                edt_todate.requestFocus();
            }
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerTD = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            yearto = selectedYear;
            monthto = selectedMonth;
            dayto = selectedDay;

            String months = "0" + String.valueOf(selectedMonth + 1);
            String days = "0" + String.valueOf(dayto);

            days = days.substring(days.length() - 2);
            months = months.substring(months.length() - 2);

            String years = String.valueOf(yearto);

            edt_todate.setText(days + "/" + months + "/" + years);

            // set selected date into datepicker also
            view.init(yearto, monthto, dayto, null);

            if (globalVariable.getnullStr(edt_todate.getText().toString())
                    .isEmpty()) {
                edt_todate.performClick();
            }
        }
    };

    //Our method to show list
    private void showCompanyList(Spinner spCompanyDetails) {
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
        spCompanyDetails.setAdapter(spinnerArrayAdapter);
        spCompanyDetails.setSelection(0);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        String _strCompanyName=pref.getString("strCompanyName", "");
        if (!_strCompanyName.equals(""))
        {
            spCompanyDetails.setSelection(globalVariable.getSpinnerIndex(spCompanyDetails, _strCompanyName));
        }

        if (strCompanyName!=null && !strCompanyName.equals("")) {
            spCompanyDetails.setSelection(globalVariable.getSpinnerIndex(spCompanyDetails, strCompanyName));
        }
    }

    //Our method to show list
    private void setReportDetails(Spinner spCompanyDetails, Spinner spBranchDetails, Spinner spReportBy) {
        //Traversing through the whole list to get all the names
        for (int i = 0; i < globalVariable.lstDatabaseDetails.size(); i++) {
            //Storing names to string array
            if (globalVariable.lstDatabaseDetails.get(i).getStrCompanyName().toString().equals(spCompanyDetails.getSelectedItem().toString())) {
                branchstr = globalVariable.lstDatabaseDetails.get(i).getStrBranchName();
                branchstrid = globalVariable.lstDatabaseDetails.get(i).getStrBranchID();
                reportby = globalVariable.lstDatabaseDetails.get(i).getStrModules();
            }
        }

        //BRANCH LIST
        String[] branchlist =branchstr.split(",");

        //Creating an array adapter for Spinner
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                branchlist);

        //Setting adapter to listview
        spBranchDetails.setAdapter(spinnerArrayAdapter);
        spBranchDetails.setSelection(0);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        String _strBranchName="";
        _strBranchName=pref.getString("strCompanyName", "");

        if (spCompanyDetails.getSelectedItem().toString().equals(_strBranchName))
        {
            _strBranchName=pref.getString("strBranchName", "");
            if (!_strBranchName.equals(""))
            {
                spBranchDetails.setSelection(globalVariable.getSpinnerIndex(spBranchDetails, _strBranchName));
            }
        }

        //REPORT BY
        if (reportby.equals("FMCG"))
        {
            String[] reportbylist ={"Division wise","Main Group wise","Category wise","Brand wise","Item wise"};

            //Creating an array adapter for Spinner
            ArrayAdapter spinnerArrayAdapterReportby = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    reportbylist);

            //Setting adapter to listview
            spReportBy.setAdapter(spinnerArrayAdapterReportby);
        }
        else
        {
            String[] reportbylist ={"Brand wise","Category wise","Item wise"};
            //Creating an array adapter for Spinner
            ArrayAdapter spinnerArrayAdapterReportby = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    reportbylist);

            //Setting adapter to listview
            spReportBy.setAdapter(spinnerArrayAdapterReportby);
        }

        spReportBy.setSelection(0);

        if (strBranchName!=null && !strBranchName.equals("")) {
            spBranchDetails.setSelection(globalVariable.getSpinnerIndex(spBranchDetails, strBranchName));
        }

        if (strReportBy!=null && !strReportBy.equals("")) {
            spReportBy.setSelection(globalVariable.getSpinnerIndex(spReportBy, strReportBy));
        }
    }

    private void  setDefaultValues() {
        //String array to store all the book names
        String[] companylist = new String[globalVariable.lstDatabaseDetails.size()];

        //Traversing through the whole list to get all the names
        for (int i = 0; i < globalVariable.lstDatabaseDetails.size(); i++) {
            //Storing names to string array
            companylist[i] = globalVariable.lstDatabaseDetails.get(i).getStrCompanyName();
        }

        if (strCompanyName==null || strCompanyName.equals("")) {
            strCompanyName=companylist[0].toString();
        }

        //Traversing through the whole list to get all the names
        for (int i = 0; i < globalVariable.lstDatabaseDetails.size(); i++) {
            //Storing names to string array
            if (globalVariable.lstDatabaseDetails.get(i).getStrCompanyName().toString().equals(strCompanyName)) {
                branchstr = globalVariable.lstDatabaseDetails.get(i).getStrBranchName();
                branchstrid = globalVariable.lstDatabaseDetails.get(i).getStrBranchID();
                reportby = globalVariable.lstDatabaseDetails.get(i).getStrModules();
            }
        }

        //BRANCH LIST
        String[] branchlist =branchstr.split(",");

        if (strBranchName==null || strBranchName.equals("")) {
            strBranchName=branchlist[0].toString();
        }

        //REPORT BY
        if (strReportBy==null || strReportBy.equals("")) {
            if (reportby.equals("FMCG")) {
                strReportBy = "Division wise";
            } else {
                strReportBy = "Brand wise";
            }
        }

        if (strFromDate==null || strFromDate.equals("")) {
            strFromDate = globalVariable.getDATEString(0);
        }

        if (strToDate==null || strToDate.equals("")) {
            strToDate = globalVariable.getDATEString(0);
        }

        txtReportBy.setText(strReportBy.replace(" wise", ""));

        String temp="";
        if (strBranchName!="All Branch") {
            temp= " (" + strBranchName + ")";
        }
        titleCompany.setText(strCompanyName + temp);
        titleDuration.setText("Period : From " + strFromDate + " To " + strToDate);

        FloatingActionButton fabViewSubReport = (FloatingActionButton) findViewById(R.id.fab_viewsubreport);

        if (strReportBy.equals("Item wise")) {
            fabViewSubReport.setVisibility(View.GONE);
        }
        else {
            fabViewSubReport.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnudailysummary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.refresh) {
            //showParameterDialog();
            viewReport();
        }
        else if (id == R.id.pdf) {
            //showChartDialog();
            try {

                TextView totalqtyselected=(TextView) findViewById(R.id.totalqtyselected);
                TextView totalgrossselected=(TextView) findViewById(R.id.totalgrossselected);

                final File filename = globalVariable.createDailyReportPdf(titleCompany.getText().toString(), txtReportBy.getText().toString() + " wise " + strReportName, titleDuration.getText().toString(), resultlistView, "", "", "", ((globalVariable.getnulltxtDbl(totalqtyselected.getText().toString()) !=0 || globalVariable.getnulltxtDbl(totalgrossselected.getText().toString()) !=0 ) ? true : false), (globalVariable.getnulltxtDbl(totalqtyselected.getText().toString()) !=0 || globalVariable.getnulltxtDbl(totalgrossselected.getText().toString()) !=0 ) ? totalqtyselected.getText().toString() : totalqty.getText().toString(), (globalVariable.getnulltxtDbl(totalqtyselected.getText().toString()) !=0 || globalVariable.getnulltxtDbl(totalgrossselected.getText().toString()) !=0 ) ? totalgrossselected.getText().toString(): totalgross.getText().toString());

                // TODO Auto-generated method stub
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                // Setting Dialog Title
                alertDialog.setTitle("Open PDF");

                // Setting Dialog Message
                alertDialog.setMessage("Do you want to Open PDF File ?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_action_picture_as_pdfdark);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User pressed YES button. Write Logic Here
                                globalVariable.viewPdf(filename);
                            }
                        });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // Showing Alert Message
                alertDialog.show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public void showChartDialog() {
        final Dialog parameterDialog = new Dialog(DailySummary.this);
        parameterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        parameterDialog.setContentView(R.layout.dialog_chart);

        parameterDialog.setTitle(Html
                .fromHtml("<b> Chart </b>"));
        parameterDialog.show();
//        parameterDialog.setCancelable(false);
//        parameterDialog.setCanceledOnTouchOutside(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        parameterDialog.getWindow().setLayout(width-50,width-50);

        BarChart chart = (BarChart) parameterDialog.findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription(strReportName);
        chart.animateXY(2000, 2000);
        chart.canScrollHorizontally(View.LAYOUT_DIRECTION_INHERIT);
        chart.invalidate();
    }

    private ArrayList<BarDataSet> getDataSet() {

        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        int i=0;

        BarEntry v1e1 = null;
        BarEntry v2e1 = null;

        for (clsDailyDetails _result : resultDailyDetails) {
            v1e1 = new BarEntry(60.000f, i);
            valueSet1.add(v1e1);

            v2e1 = new BarEntry(60.000f, i);
            valueSet2.add(v2e1);

            i=i+1;
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Qty");
        barDataSet1.setColor(Color.parseColor("#063b65"));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Gross");
        barDataSet2.setColor(Color.parseColor("#CC6600"));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {

        ArrayList<String> xAxis = new ArrayList<>();

        for (clsDailyDetails _result : resultDailyDetails) {
            xAxis.add(_result.getITEM());

            //temp.put("qty", globalVariable.getnullStr(_result.getDecTotalQty()));
            //temp.put("gross", globalVariable.getnullStr(_result.getDecGross().toString()));
        }

        return xAxis;
    }

    public void showParameterDialog() {
        final Dialog parameterDialog = new Dialog(DailySummary.this);
        parameterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        parameterDialog.setContentView(R.layout.dialog_parameterdailysummary);

        parameterDialog.setTitle(Html
                .fromHtml("<b> Parameters </b>"));
        parameterDialog.show();
        parameterDialog.setCancelable(false);
        parameterDialog.setCanceledOnTouchOutside(false);

        final Spinner spinnerCompanyDetails = (Spinner) parameterDialog
                .findViewById(R.id.spinnerCompanyDetails);
        final Spinner spinnerBranchDetails = (Spinner) parameterDialog
                .findViewById(R.id.spinnerBranchDetails);
        edt_fromdate = (EditText) parameterDialog
                .findViewById(R.id.edt_fromdate);
        final DatePicker dpFromDate = (DatePicker) parameterDialog
                .findViewById(R.id.dpFromDate);
        edt_todate = (EditText) parameterDialog
                .findViewById(R.id.edt_todate);
        final DatePicker dpToDate = (DatePicker) parameterDialog
                .findViewById(R.id.dpToDate);
        final Spinner spinnerInvoiceType = (Spinner) parameterDialog
                .findViewById(R.id.spinnerInvoiceType);
        final Spinner spinnerReportBy = (Spinner) parameterDialog
                .findViewById(R.id.spinnerReportBy);

        final ImageButton headerview = (ImageButton) parameterDialog
                .findViewById(R.id.btnFavourite);

        headerview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

//                editor.remove("strCompanyName");
//                editor.remove("strBranchName");
//                editor.remove("strInvoiceType");

                editor.putString("strCompanyName", spinnerCompanyDetails.getSelectedItem().toString());
                editor.putString("strBranchName", spinnerBranchDetails.getSelectedItem().toString());
                editor.putString("strInvoiceType", spinnerInvoiceType.getSelectedItem().toString());

                editor.commit();

                Toast.makeText(getApplicationContext(), "Favourites added Successfullly", Toast.LENGTH_LONG).show();
            }
        });

        showCompanyList(spinnerCompanyDetails);

        String[] invoicetypelist ={"B2B+B2C","B2B","B2C"};

        //Creating an array adapter for Spinner
        ArrayAdapter spinnerArrayAdapterInvoideType = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                invoicetypelist);

        //Setting adapter to listview
        spinnerInvoiceType.setAdapter(spinnerArrayAdapterInvoideType);
        spinnerInvoiceType.setSelection(0);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        String _strInvoiceType=pref.getString("strInvoiceType", "");
        if (!_strInvoiceType.equals("") && !strReportFor.contains("Purchase"))
        {
            spinnerInvoiceType.setSelection(globalVariable.getSpinnerIndex(spinnerInvoiceType, _strInvoiceType));
        }

        final String tempBranch=strBranchName;

        spinnerCompanyDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                strBranchName="";
                setReportDetails(spinnerCompanyDetails, spinnerBranchDetails, spinnerReportBy);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edt_fromdate.setText(globalVariable.getDATEString(0));
        edt_todate.setText(globalVariable.getDATEString(0));

        String _strBranchName=pref.getString("strBranchName", "");
        if (!_strBranchName.equals(""))
        {
            spinnerBranchDetails.setSelection(globalVariable.getSpinnerIndex(spinnerBranchDetails, _strBranchName));
        }

        if (strBranchName!=null && !strBranchName.equals("")) {
            spinnerBranchDetails.setSelection(globalVariable.getSpinnerIndex(spinnerBranchDetails, strBranchName));
        }

        if (strFromDate!=null && !strFromDate.equals("")) {
            edt_fromdate.setText(strFromDate);
        }

        if (strToDate!=null && !strToDate.equals("")) {
            edt_todate.setText(strToDate);
        }

        if (strReportBy!=null && !strReportBy.equals("")) {
            spinnerReportBy.setSelection(globalVariable.getSpinnerIndex(spinnerReportBy, strReportBy));
        }

        setFromDateOnView(edt_fromdate.getText().toString(), dpFromDate);
        setToDateOnView(edt_todate.getText().toString(), dpToDate);

        Button btnConfirm = (Button) parameterDialog
                .findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                parameterDialog.dismiss();

                strCompanyName=spinnerCompanyDetails.getSelectedItem().toString();
                strBranchName=spinnerBranchDetails.getSelectedItem().toString();
                strFromDate=edt_fromdate.getText().toString();
                strToDate=edt_todate.getText().toString();
                strReportBy=spinnerReportBy.getSelectedItem().toString();

                if (spinnerInvoiceType.getSelectedItem().toString().equals("B2B+B2C"))
                {
                    strInvoiceType = "";
                }
                else {
                    strInvoiceType = spinnerInvoiceType.getSelectedItem().toString();
                }

                boolConfirmClick=true;
                txtReportBy.setText(strReportBy.replace(" wise", ""));
                boolConfirmClick=false;

                String temp="";
                if (strBranchName!="All Branch") {
                    temp= " (" + strBranchName + ")";
                }
                titleCompany.setText(strCompanyName + temp);
                titleDuration.setText("Period : From " + strFromDate + " To " + strToDate);

                FloatingActionButton fabViewSubReport = (FloatingActionButton) findViewById(R.id.fab_viewsubreport);

                if (strReportBy.equals("Item wise")) {
                    fabViewSubReport.setVisibility(View.GONE);
                }
                else {
                    fabViewSubReport.setVisibility(View.VISIBLE);
                }

                viewReport();
            }
        });

        Button btnCancel = (Button) parameterDialog
                .findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                strBranchName=tempBranch;
                parameterDialog.dismiss();
            }
        });
    }

    public void btn_viewparameter_click(View v) {
        showParameterDialog();
    }

    //define your custom adapter
    private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>>
    {
        // boolean array for storing
        //the state of each CheckBox
        boolean[] checkBoxState;

        ViewHolder viewHolder;

        public CustomAdapter(Context context, int textViewResourceId,
                             ArrayList<HashMap<String, Object>> resultlist, boolean _allCheck) {

            //let android do the initializing :)
            super(context, textViewResourceId, resultlist);

            //create the boolean array with
            //initial state as false
            checkBoxState=new boolean[resultlist.size()];

            _dblTotalQtySelected=0;
            _dblTotalGrossSelected=0;
            strID="";

            for (int j=0; j < resultlist.size(); j++) {
                checkBoxState[j]=_allCheck;
                if (_allCheck) {
                    if (strID.equals("")) {
                        strID = mDailyDetailsListResult.get(j).get("itemid").toString();
                    }
                    else {
                        strID = strID + "," + mDailyDetailsListResult.get(j).get("itemid").toString();
                    }

                    _dblTotalQtySelected = _dblTotalQtySelected + globalVariable.getnulltxtDbl(mDailyDetailsListResult.get(j).get("qty").toString());
                    _dblTotalGrossSelected = _dblTotalGrossSelected + globalVariable.getnulltxtDbl(mDailyDetailsListResult.get(j).get("gross").toString());
                }
            }

            if((_dblTotalQtySelected-(int)_dblTotalQtySelected)!=0) {
                totalqtyselected.setText(String.valueOf(_dblTotalQtySelected));
            }
            else {
                totalqtyselected.setText(String.format("%.0f",_dblTotalQtySelected));
            }

            totalgrossselected.setText(String.format("%.2f",_dblTotalGrossSelected));

            if (_dblTotalQtySelected>0 || _dblTotalGrossSelected>0) {
                resulttotalselected.setVisibility(View.VISIBLE);
                viewselected.setVisibility(View.VISIBLE);
            }
            else {
                resulttotalselected.setVisibility(View.GONE);
                viewselected.setVisibility(View.GONE);
            }
        }

        //class for caching the views in a row
        private class ViewHolder
        {
            TextView item,itemid,qty,gross;
            CheckBox checkMark;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.resultlist_row, null);
                viewHolder=new ViewHolder();

                //cache the views
                viewHolder.item=(TextView) convertView.findViewById(R.id.item);
                viewHolder.itemid=(TextView) convertView.findViewById(R.id.itemid);
                viewHolder.qty=(TextView) convertView.findViewById(R.id.qty);
                viewHolder.gross=(TextView) convertView.findViewById(R.id.gross);
                viewHolder.checkMark=(CheckBox) convertView.findViewById(R.id.checkMark);

                //link the cached views to the convertview
                convertView.setTag( viewHolder);
            }
            else
                viewHolder=(ViewHolder) convertView.getTag();

                //set the data to be displayed
                viewHolder.item.setText(mDailyDetailsListResult.get(position).get("item").toString());
                viewHolder.itemid.setText(mDailyDetailsListResult.get(position).get("itemid").toString());
                viewHolder.qty.setText(mDailyDetailsListResult.get(position).get("qty").toString());
                viewHolder.gross.setText(String.format("%.2f",globalVariable.getnulltxtDbl(mDailyDetailsListResult.get(position).get("gross").toString())));

                //VITAL PART!!! Set the state of the
                //CheckBox using the boolean array
                viewHolder.checkMark.setChecked(checkBoxState[position]);

                viewHolder.checkMark.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        if(((CheckBox)v).isChecked()) {
                            checkBoxState[position] = true;
                            if (strID.equals("")) {
                                strID = mDailyDetailsListResult.get(position).get("itemid").toString();
                            }
                            else {
                                strID = strID + "," + mDailyDetailsListResult.get(position).get("itemid").toString();
                            }
                            _dblTotalQtySelected=_dblTotalQtySelected + globalVariable.getnulltxtDbl(mDailyDetailsListResult.get(position).get("qty").toString());
                            _dblTotalGrossSelected=_dblTotalGrossSelected + globalVariable.getnulltxtDbl(mDailyDetailsListResult.get(position).get("gross").toString());
                        }
                        else {
                            checkBoxState[position] = false;
                            strID="," + strID + ",";
                            strID = strID.replace("," + mDailyDetailsListResult.get(position).get("itemid").toString() + ",", ",");

                            if (strID.length()==1) {
                                strID="";
                            }
                            else {
                                strID=strID.substring(1,strID.length()-1);
                            }
                            _dblTotalQtySelected=_dblTotalQtySelected - globalVariable.getnulltxtDbl(mDailyDetailsListResult.get(position).get("qty").toString());
                            _dblTotalGrossSelected=_dblTotalGrossSelected - globalVariable.getnulltxtDbl(mDailyDetailsListResult.get(position).get("gross").toString());
                        }

                        if((_dblTotalQtySelected-(int)_dblTotalQtySelected)!=0) {
                            totalqtyselected.setText(String.valueOf(_dblTotalQtySelected));
                        }
                        else {
                            totalqtyselected.setText(String.format("%.0f",_dblTotalQtySelected));
                        }

                        totalgrossselected.setText(String.format("%.2f",_dblTotalGrossSelected));

                        if (_dblTotalQtySelected>0 || _dblTotalGrossSelected>0) {
                            resulttotalselected.setVisibility(View.VISIBLE);
                            viewselected.setVisibility(View.VISIBLE);
                        }
                        else {
                            resulttotalselected.setVisibility(View.GONE);
                            viewselected.setVisibility(View.GONE);
                        }
                    }
                });

                //return the view to be displayed
                return convertView;
        }
    }
}

