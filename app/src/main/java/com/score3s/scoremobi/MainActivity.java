package com.score3s.scoremobi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
//import android.support.annotation.ColorRes;
//import android.support.v4.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.provider.Settings.Secure;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    GlobalActivity globalVariable = null;

    Button me = null;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#063b65")));

        globalVariable = (GlobalActivity) getApplicationContext();

        me = (Button) findViewById(R.id.me);

        showNotificationMessage();

        if (!GlobalActivity
                .createDirIfNotExists("/Score 3s Cloud/")) {
            checkPermission();
            /*Toast.makeText(MainActivity.this,
                    "Score 3s Cloud folder creation failed.",
                    Toast.LENGTH_LONG).show();*/
        }

        if (!GlobalActivity
                .createDirIfNotExists("/Score 3s Cloud/PDF Reports/")) {
            checkPermission();
            /*Toast.makeText(MainActivity.this,
                    "PDF Reports folder creation failed.",
                    Toast.LENGTH_LONG).show();*/
        }else
        {
            showNoConnectionDialog(this);
        }



//        BarChart chart = (BarChart) findViewById(R.id.chart);
//
//        BarData data = new BarData(getXAxisValues(), getDataSet());
//        chart.setData(data);
//        chart.setDescription("Sales Summary");
//        chart.animateXY(2000, 2000);
//        chart.invalidate();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showNotificationMessage();
//    }


    private void showNotificationMessage() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();



        if (pref.getString("strMessageBody", "") != null && pref.getString("strMessageBody", "") != "") {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Sales Invoice");
            alert.setIcon(R.mipmap.ic_launcher);
            alert.setCancelable(false);

            alert.setMessage(pref.getString("strMessageBody", ""));

            alert.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.create().show();

            editor.putString("strMessageBody", "");
            editor.commit();
        }
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

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
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNoConnectionDialog(final Context c) {
        if (globalVariable.isNetworkAvailable()) {
            //getDatabaseDetails();
            GetFCMToken();
            loadIMEI();
            return;
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(c);
            alert.setTitle("No Connection");
            alert.setIcon(R.drawable.ic_action_warning);
            alert.setCancelable(false);
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void GetFCMToken(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (pref.getString("regId", "") == null || pref.getString("regId", "") == "") {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                //Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            storeRegIdInPref(token);
                            //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();

        } else {
            // READ_PHONE_STATE permission is already been granted.
            doPermissionGrantedStuffs();
        }
    }

    // Function to check and request permission.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestWRITEEXTERNALSTORAGEPermission();
        } else {
            GlobalActivity.createDirIfNotExists("/Score 3s Cloud/");
            GlobalActivity.createDirIfNotExists("/Score 3s Cloud/PDF Reports/");
            showNoConnectionDialog(this);

        }
    }
    /**
     * Requests the READ_PHONE_STATE permission.
     * If the permission has been denied previously, a dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    // .setIcon(R.drawable.logo)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    private void requestWRITEEXTERNALSTORAGEPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE);
                        }
                    })
                    // .setIcon(R.drawable.logo)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE ) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                //alertAlert(getString(R.string.permision_available_read_phone_state));
                doPermissionGrantedStuffs();
            }


            else {
                alertAlert("Permisssion Required");
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE ) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                //alertAlert(getString(R.string.permision_available_read_phone_state));
                GlobalActivity.createDirIfNotExists("/Score 3s Cloud/");
                GlobalActivity.createDirIfNotExists("/Score 3s Cloud/PDF Reports/");
                showNoConnectionDialog(this);
            }

        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                    }
                })
                //.setIcon(R.drawable.logo)
                .show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        /*String tmpMobile = tm.getLine1Number().trim();
        if(tmpMobile.contains("+"))
        {
            globalVariable.strIMEI = tm.getLine1Number();
        }else
        {
            globalVariable.strIMEI = "+" + tm.getLine1Number();
        }*/

        globalVariable.strIMEI = androidId.trim();

        getDatabaseDetails();


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDatabaseDetails() {
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Authorizing User Data", "Please wait...", false, false);

        ScoreAPI api = globalVariable.getScoreAPI();

       /* TelephonyManager tm = (TelephonyManager) getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);*/

        globalVariable.strProductName = this.getString(R.string.strProductName);
      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/


        Call<clsDatabaseDetailsList> call = api.getDatabaseDetails(globalVariable.strProductName, globalVariable.strIMEI);
        call.enqueue(new Callback<clsDatabaseDetailsList>() {
            @Override
            public void onResponse(Call<clsDatabaseDetailsList>call, Response<clsDatabaseDetailsList> response) {

                if (response.isSuccessful()) {
                    globalVariable.lstDatabaseDetails =response.body().getGetDatabaseDetailsResult();

                    if (globalVariable.lstDatabaseDetails == null || globalVariable.lstDatabaseDetails.size()<=0)
                    {
                        loading.dismiss();

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Un-Registered");
                        alert.setIcon(R.drawable.ic_action_warning);
                        alert.setCancelable(false);
                        alert.setMessage("Your ID: " + globalVariable.strIMEI);
                        alert.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        });
                        alert.create().show();
                    }
                    else {
                        me.setText(globalVariable.lstDatabaseDetails.get(0).getStrUserName().toString());
                        loading.dismiss();
                    }
                }
                else {
                    loading.dismiss();

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Failure");
                    alert.setIcon(R.drawable.ic_action_warning);
                    alert.setCancelable(false);
                    alert.setMessage(response.message().toString() +"Repsonse is Un-Successful_2");
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
            public void onFailure(Call<clsDatabaseDetailsList>call, Throwable t) {
                loading.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Failure");
                alert.setIcon(R.drawable.ic_action_warning);
                alert.setMessage(t.getMessage());
                alert.setCancelable(false);
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

    public void btn_accounts_click(View v) {
        try
        {
            showNotificationMessage();
            Intent i = new Intent(MainActivity.this , AccountSummary.class);
            startActivity(i);
        }
        catch(Exception ex)
        {
            Toast.makeText(MainActivity.this,"Error in Account Details Click" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void btn_products_click(View v) {
        try
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            String _strPreferences="";

            Intent i = new Intent(MainActivity.this ,StockSummary.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("strReportName", "Stock Details");

            _strPreferences=pref.getString("strCompanyName", "");
            mBundle.putString("strCompanyName", _strPreferences);

            _strPreferences=pref.getString("strBranchName", "");
            mBundle.putString("strBranchName", _strPreferences);

            mBundle.putBoolean("boolIsLoad", true);

            if (globalVariable.lstDatabaseDetails.get(0).getStrModules().toString().equals("FMCG"))
            {
                mBundle.putString("strReportBy", "Division wise");
            }
            else
            {
                mBundle.putString("strReportBy", "Brand wise");
            }

            mBundle.putString("strStockType", "");

            mBundle.putString("strDivisionID", "");
            mBundle.putString("strMainGroupID", "");
            mBundle.putString("strCategoryID", "");
            mBundle.putString("strBrandID", "");

            i.putExtras(mBundle);
            startActivity(i);
        }
        catch(Exception ex)
        {
            Toast.makeText(MainActivity.this,"Error in Stock Details Click" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void btn_sale_click(View v) {
        try
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            String _strPreferences="";

            Intent i = new Intent(MainActivity.this ,DailySummary.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("strReportFor", "Material Sales,POS Sales");
            mBundle.putString("strReportName", "Sales Summary");

            _strPreferences=pref.getString("strCompanyName", "");
            mBundle.putString("strCompanyName", _strPreferences);

            _strPreferences=pref.getString("strBranchName", "");
            mBundle.putString("strBranchName", _strPreferences);

            mBundle.putString("strFromDate", "");
            mBundle.putString("strToDate", "");

            mBundle.putBoolean("boolIsLoad", true);

            if (globalVariable.lstDatabaseDetails.get(0).getStrModules().toString().equals("FMCG"))
            {
                mBundle.putString("strReportBy", "Division wise");
            }
            else
            {
                mBundle.putString("strReportBy", "Brand wise");
            }

            mBundle.putString("strInvoiceType", "");

            mBundle.putString("strDivisionID", "");
            mBundle.putString("strMainGroupID", "");
            mBundle.putString("strCategoryID", "");
            mBundle.putString("strBrandID", "");

            i.putExtras(mBundle);
            startActivity(i);
        }
        catch(Exception ex)
        {
            Toast.makeText(MainActivity.this,"Error in Sales Summary Click" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        exitApp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.exit) {
            exitApp();
        }
        else if (id == R.id.settings) {
            try {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this,
                        "Can't Open Settings Page" + ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void btn_purchase_click(View v) {
        try
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            String _strPreferences="";

            Intent i = new Intent(MainActivity.this ,DailySummary.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("strReportFor", "Material Purchase");
            mBundle.putString("strReportName", "Purchase Summary");

            _strPreferences=pref.getString("strCompanyName", "");
            mBundle.putString("strCompanyName", _strPreferences);

            _strPreferences=pref.getString("strBranchName", "");
            mBundle.putString("strBranchName", _strPreferences);

            mBundle.putString("strFromDate", "");
            mBundle.putString("strToDate", "");

            mBundle.putBoolean("boolIsLoad", true);

            if (globalVariable.lstDatabaseDetails.get(0).getStrModules().toString().equals("FMCG"))
            {
                mBundle.putString("strReportBy", "Division wise");
            }
            else
            {
                mBundle.putString("strReportBy", "Brand wise");
            }

            mBundle.putString("strDivisionID", "");
            mBundle.putString("strMainGroupID", "");
            mBundle.putString("strCategoryID", "");
            mBundle.putString("strBrandID", "");

            i.putExtras(mBundle);
            startActivity(i);
        }
        catch(Exception ex)
        {
            Toast.makeText(MainActivity.this,"Error in Purchase Summary Click" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void exitApp()
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("Exit");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to Exit from Application ?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.exit);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User pressed YES button. Write Logic Here
                        finish();
                        System.exit(0);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
