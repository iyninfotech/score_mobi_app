package com.score3s.scoremobi;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalActivity extends Application {

    String skey1 = "77634";
    String skey2 = "77635";
    String skey3 = "77636";

    private String CurrDate;
    private String username;

    Integer key1 = 77636;
    Integer key2 = 77635;
    Integer key3 = 77634;

    Integer lenupto = 5;

    public List<clsDatabaseDetails> lstDatabaseDetails;
    public String strProductName;
    public String strIMEI;

    private clsDatabaseDetails clsLogInDatabase;

    public static final String ROOT_URL = "http://182.18.139.221:1913/";
    //public static final String ROOT_URL = "http://www.scoremobi.score3s.com/";
    private int appStartFlag = 0;

//    public void showSingleButtonMessage(Context cxt, String title, String msg) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
//        builder.setTitle(title);
//        builder.setIcon(R.drawable.ic_action_warning);
//        builder.setMessage(msg).setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // do things
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }

    public Retrofit getRetrofitAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public ScoreAPI getScoreAPI() {
        ScoreAPI api =  getRetrofitAPI().create(ScoreAPI.class);
        return api;
    }

    public boolean registerProduct(String idType, String id, String key) {
        SQLiteDatabase db = null;

        try {
            String dbpath = this.getDatabasePath("contractcrm.db").toString();
            db = SQLiteDatabase.openDatabase(dbpath, null,
                    SQLiteDatabase.OPEN_READWRITE);

            Integer generatedkey1 = getnulltxtInt(id.substring(id.length()
                    - lenupto, id.length()))
                    + key1 + key2 + key3;

            String generatedkey = generatedkey1.toString() + skey3 + skey2
                    + skey1;
            generatedkey = generatedkey.substring(0, id.length());

            if (generatedkey.equals(key)) {
                String str = "UPDATE SETTINGS SET LICENSE_IDTYPE='"
                        + idType
                        + "', LICENSE_ID='"
                        + id
                        + "', LICENSE_KEY='"
                        + key
                        + "', VALIDFROM_DATE='"
                        + getDATENumber(0)
                        + "', VALIDUPTO_DATE='"
                        + getDateAfterDaysNumber(
                        getDateAfterDaysString(getDATEString(0), 1,
                                "YEAR"), -1, "DAY") + "'";
                db.execSQL(str);
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        } catch (Exception e) {
            db.close();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public String getusername() {
        return username;
    }

    public void setusername(String aName) {

        if (aName.equals(null) || aName.equals("")) {
            username = "";
        } else {
            username = aName;
        }

    }

    public clsDatabaseDetails getLogInDatabaseDetails() {
        return clsLogInDatabase;
    }

    public void setLogInDatabaseDetails(clsDatabaseDetails ddl) {

        clsLogInDatabase = ddl;
    }

    public String retPlanDetails(String sendtype) {
        String result = "";

        SQLiteDatabase db = null;
        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        if (sendtype.equals("SMS")) {
            result = "AMC Plan Details";
            Cursor temp = db.rawQuery("SELECT PLANNAME, PLANFREQ, PLANAMOUNT, PLANTERM, PLANINCLUDES, PLANEXCLUDES, PLANTERMSCONDITIONS FROM PLANMAST WHERE CASE WHEN PLANAMOUNT ISNULL THEN 0 ELSE PLANAMOUNT END<>0", null);
            while (temp.moveToNext()) {
                result = result + "\n----------------";
                result = result + "\nPlan Name: " + getnullStr(temp.getString(temp.getColumnIndex("PLANNAME")));
                result = result + "\nService Frequency: " + getnullStr(temp.getString(temp.getColumnIndex("PLANFREQ")));
                result = result + "\nPlan Term: " + getnullStr(temp.getString(temp.getColumnIndex("PLANTERM")) + " Year(s)");
                if (!getnullStr(temp.getString(temp.getColumnIndex("PLANINCLUDES"))).isEmpty()) {
                    result = result + "\nPlan Includes: " + getnullStr(temp.getString(temp.getColumnIndex("PLANINCLUDES")));
                }

                if (!getnullStr(temp.getString(temp.getColumnIndex("PLANEXCLUDES"))).isEmpty()) {
                    result = result + "\nPlan Includes: " + getnullStr(temp.getString(temp.getColumnIndex("PLANEXCLUDES")));
                }

                if (!getnullStr(temp.getString(temp.getColumnIndex("PLANEXCLUDES"))).isEmpty()) {
                    result = result + "\nPlan Excludes: " + getnullStr(temp.getString(temp.getColumnIndex("PLANEXCLUDES")));
                }

                if (!getnullStr(temp.getString(temp.getColumnIndex("PLANTERMSCONDITIONS"))).isEmpty()) {
                    result = result + "\nPlan Terms & Conditions: " + getnullStr(temp.getString(temp.getColumnIndex("PLANTERMSCONDITIONS")));
                }

                result = result + "\nPlan Amount: " + String.format("%.2f", temp.getDouble(temp.getColumnIndex("PLANAMOUNT")));
            }

            result = result + "\n";
            result = result + getName("SELECT SMSSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0");
        } else {
            result = "Dear Sir,\n\nAs per your requirement, Please find below mentioned AMC Plan Details:";
            Cursor temp = db.rawQuery("SELECT PLANNAME, PLANFREQ, PLANAMOUNT, PLANTERM, PLANINCLUDES, PLANEXCLUDES, PLANTERMSCONDITIONS FROM PLANMAST WHERE CASE WHEN PLANAMOUNT ISNULL THEN 0 ELSE PLANAMOUNT END<>0", null);
            while (temp.moveToNext()) {
                result = result + "\n----------------";
                result = result + "\nPlan Name: " + getnullStr(temp.getString(temp.getColumnIndex("PLANNAME")));
                result = result + "\nService Frequency: " + getnullStr(temp.getString(temp.getColumnIndex("PLANFREQ")));
                result = result + "\nPlan Term: " + getnullStr(temp.getString(temp.getColumnIndex("PLANTERM")) + " Year(s)");
                if (!getnullStr(temp.getString(temp.getColumnIndex("PLANINCLUDES"))).isEmpty()) {
                    result = result + "\nPlan Includes: " + getnullStr(temp.getString(temp.getColumnIndex("PLANINCLUDES")));
                }

                if (!getnullStr(temp.getString(temp.getColumnIndex("PLANEXCLUDES"))).isEmpty()) {
                    result = result + "\nPlan Includes: " + getnullStr(temp.getString(temp.getColumnIndex("PLANEXCLUDES")));
                }

                if (!getnullStr(temp.getString(temp.getColumnIndex("PLANEXCLUDES"))).isEmpty()) {
                    result = result + "Plan Excludes: " + getnullStr(temp.getString(temp.getColumnIndex("PLANEXCLUDES")));
                }

                if (!getnullStr(temp.getString(temp.getColumnIndex("PLANTERMSCONDITIONS"))).isEmpty()) {
                    result = result + "\nPlan Terms & Conditions: " + getnullStr(temp.getString(temp.getColumnIndex("PLANTERMSCONDITIONS")));
                }

                result = result + "\nPlan Amount: " + String.format("%.2f", temp.getDouble(temp.getColumnIndex("PLANAMOUNT")));
            }

            result = result + "\n\n";
            result = result + "Waiting for your Positive Reply.";
            result = result + "\n";
            result = result + getName("SELECT MAILSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0");
        }
        return result;
    }

    public boolean ValidLicenseCheck() {
        SQLiteDatabase db = null;
        try {
            String dbpath = this.getDatabasePath("contractcrm.db").toString();
            db = SQLiteDatabase.openDatabase(dbpath, null,
                    SQLiteDatabase.OPEN_READONLY);

            String id = "";
            TelephonyManager tm = (TelephonyManager) getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            if (getName("SELECT LICENSE_IDTYPE FROM SETTINGS").equals(
                    "Mobile Number")) {
                id = getnullStr(tm.getLine1Number());
            } else {
                id = getnullStr(tm.getDeviceId());
            }

            if (id.isEmpty()) {
                db.close();
                return false;
            }

            Integer generatedkey1 = getnulltxtInt(id.substring(id.length()
                    - lenupto, id.length()))
                    + key1 + key2 + key3;

            String generatedkey = generatedkey1.toString() + skey3 + skey2
                    + skey1;
            generatedkey = generatedkey.substring(0, id.length());

            if (!getnullStr(
                    getName("SELECT ID FROM SURRENDER WHERE ID='" + id + "'"))
                    .equals("")) {
                db.close();
                return false;
            } else if (generatedkey
                    .equals(getName("SELECT LICENSE_KEY FROM SETTINGS"))) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        } catch (Exception e) {
            db.close();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public String getDateAfterDaysString(String dt, int daystoadd, String addIn) {
        String returndate = "";

        final Calendar cal = Calendar.getInstance();

        if (!dt.isEmpty()) {
            cal.set(Calendar.YEAR, Integer.valueOf(dt.substring(6, 10)));
            cal.set(Calendar.MONTH, Integer.valueOf(dt.substring(3, 5)) - 1);
            cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dt.substring(0, 2)));
        }

        if (addIn.equals("DAY")) {
            cal.add(Calendar.DATE, daystoadd);
        } else if (addIn.equals("MONTH")) {
            cal.add(Calendar.MONTH, daystoadd);
        } else if (addIn.equals("YEAR")) {
            cal.add(Calendar.YEAR, daystoadd);
        }

        String months = "0" + String.valueOf(cal.get(Calendar.MONTH) + 1);
        months = months.substring(months.length() - 2);

        String days = "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        days = days.substring(days.length() - 2);

        returndate = days + "/" + months + "/" + cal.get(Calendar.YEAR);

        return returndate;
    }

    public String getDateAfterDaysNumber(String dt, int daystoadd, String addIn) {
        String returndate = "";

        final Calendar cal = Calendar.getInstance();

        if (!dt.isEmpty()) {
            cal.set(Calendar.YEAR, Integer.valueOf(dt.substring(6, 10)));
            cal.set(Calendar.MONTH, Integer.valueOf(dt.substring(3, 5)) - 1);
            cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dt.substring(0, 2)));
        }

        if (addIn.equals("DAY")) {
            cal.add(Calendar.DATE, daystoadd);
        } else if (addIn.equals("MONTH")) {
            cal.add(Calendar.MONTH, daystoadd);
        } else if (addIn.equals("YEAR")) {
            cal.add(Calendar.YEAR, daystoadd);
        }

        String months = "0" + String.valueOf(cal.get(Calendar.MONTH) + 1);
        months = months.substring(months.length() - 2);

        String days = "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        days = days.substring(days.length() - 2);

        returndate = "" + cal.get(Calendar.YEAR) + months + days;

        return returndate;
    }

    public String getDATEString(int daystoadd) {
        String returndate = "";

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daystoadd);

        String months = "0" + String.valueOf(cal.get(Calendar.MONTH) + 1);
        months = months.substring(months.length() - 2);

        String days = "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        days = days.substring(days.length() - 2);

        returndate = days + "/" + months + "/" + cal.get(Calendar.YEAR);

        return returndate;
    }

    public String getDATENumber(int daystoadd) {
        String returndate = "";

        final Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, daystoadd);
        String months = "0" + String.valueOf(cal.get(Calendar.MONTH) + 1);
        months = months.substring(months.length() - 2);

        String days = "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        days = days.substring(days.length() - 2);

        returndate = "" + cal.get(Calendar.YEAR) + months + days;

        return returndate;
    }

    public int getSpinnerIndex(Spinner sp, String str) {
        int i=-1;
        for (i = 0; i < sp.getCount(); i++) {
            if (sp.getItemAtPosition(i).equals(str)) {
                return i;
            }
        }
        return i;
    }

    public ArrayAdapter<String> getInsuranceCompanyAdapter(Activity act,
                                                           String protype) {
        ArrayList<String> arrList = new ArrayList<String>();
        arrList.clear();
        Cursor insucomplist = null;
        SQLiteDatabase db = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        if (!protype.isEmpty()) {
            insucomplist = db
                    .rawQuery(
                            "SELECT FIRSTNAME, PARTY_KEY AS _id FROM PARTYMAST INNER JOIN PRODUCTMAST ON PARTYMAST.PARTY_KEY=PRODUCTMAST.INSU_PKEY WHERE PRODUCTMAST.PROTYPE='"
                                    + protype
                                    + "' AND PARTYTYPE=2 ORDER BY FIRSTNAME",
                            null);
        } else {
            insucomplist = db
                    .rawQuery(
                            "SELECT FIRSTNAME, PARTY_KEY AS _id FROM PARTYMAST WHERE PARTYTYPE=2 ORDER BY FIRSTNAME",
                            null);
        }
        while (insucomplist.moveToNext()) {
            arrList.add(getnullStr(insucomplist.getString(
                    insucomplist.getColumnIndex("FIRSTNAME")).toString()));
        }

        ArrayAdapter<String> spinnerAdapter = null;
        spinnerAdapter = new ArrayAdapter<String>(act,
                android.R.layout.simple_spinner_item, arrList);

        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        insucomplist.close();
        db.close();

        return spinnerAdapter;
    }

    public ArrayAdapter<String> getPartyAdapter(Activity act) {
        ArrayList<String> arrList = new ArrayList<String>();
        arrList.clear();
        Cursor partylist = null;
        SQLiteDatabase db = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        partylist = db
                .rawQuery(
                        "SELECT FIRSTNAME || ' ' || LASTNAME || CASE WHEN AREA || ''<>'' THEN ' (' || AREA || ')' ELSE '' END AS NAME, PARTY_KEY AS _id FROM PARTYMAST WHERE PARTYTYPE=1 ORDER BY FIRSTNAME || ' ' || LASTNAME",
                        null);

        while (partylist.moveToNext()) {
            arrList.add(getnullStr(partylist.getString(
                    partylist.getColumnIndex("NAME")).toString()));
        }

        ArrayAdapter<String> spinnerAdapter = null;
        spinnerAdapter = new ArrayAdapter<String>(act,
                android.R.layout.simple_spinner_item, arrList);

        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        partylist.close();
        db.close();

        return spinnerAdapter;

    }

    public ArrayAdapter<String> getProductNameAdapter(Activity act,
                                                      String protype, String insucomp) {
        ArrayList<String> arrList = new ArrayList<String>();
        arrList.clear();
        Cursor partylist = null;
        SQLiteDatabase db = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        if (!insucomp.isEmpty()) {
            partylist = db
                    .rawQuery(
                            "SELECT PRODUCTNAME, PRODUCT_KEY AS _id FROM PRODUCTMAST LEFT JOIN PARTYMAST ON PRODUCTMAST.INSU_PKEY=PARTYMAST.PARTY_KEY WHERE PRODUCTMAST.PROTYPE='"
                                    + protype
                                    + "' AND PARTYMAST.FIRSTNAME='"
                                    + insucomp + "' ORDER BY PRODUCTNAME", null);
        } else {
            partylist = db
                    .rawQuery(
                            "SELECT PRODUCTNAME, PRODUCT_KEY AS _id FROM PRODUCTMAST LEFT JOIN PARTYMAST ON PRODUCTMAST.INSU_PKEY=PARTYMAST.PARTY_KEY WHERE PRODUCTMAST.PROTYPE='"
                                    + protype + "' ORDER BY PRODUCTNAME", null);
        }
        while (partylist.moveToNext()) {
            arrList.add(getnullStr(partylist.getString(
                    partylist.getColumnIndex("PRODUCTNAME")).toString()));
        }

        ArrayAdapter<String> spinnerAdapter = null;
        spinnerAdapter = new ArrayAdapter<String>(act,
                android.R.layout.simple_spinner_item, arrList);

        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        partylist.close();
        db.close();

        return spinnerAdapter;

    }

//    public ArrayAdapter<String> getCallLogHelpAdapter(Activity act) {
//        ArrayList<String> arrListLogNumber = new ArrayList<String>();
//        arrListLogNumber.clear();
//
//        Cursor managedCursor = getContentResolver().query(
//                CallLog.Calls.CONTENT_URI,
//                new String[]{CallLog.Calls.NUMBER, CallLog.Calls.TYPE},
//                "TYPE=1 OR TYPE=3", null, "DATE DESC");
//
//        while (managedCursor.moveToNext()) {
//            arrListLogNumber.add(getnullStr(managedCursor
//                    .getString(managedCursor
//                            .getColumnIndex(CallLog.Calls.NUMBER))));
//        }
//
//        ArrayAdapter<String> spinnerAdapter = null;
//        spinnerAdapter = new ArrayAdapter<String>(act,
//                android.R.layout.simple_spinner_item, arrListLogNumber);
//
//        spinnerAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        managedCursor.close();
//
//        return spinnerAdapter;
//
//    }

    public void setDate() {
        int year;
        int month;
        int day;

        final Calendar c = Calendar.getInstance();
        String months = "";

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        months = "0" + String.valueOf(month + 1);

        String days = "0" + String.valueOf(day);

        days = days.substring(days.length() - 2);

        months = months.substring(months.length() - 2);

        String years = String.valueOf(year);

        CurrDate = days + "/" + months + "/" + years;

    }

    public String getCurrDate() {

        return CurrDate;
    }

    public Integer SetMaxCode(String tblname, String field, String condition) {

        Integer maxcode = 1;
        SQLiteDatabase db = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);
        String str = "SELECT MAX(" + field + ") as id FROM " + tblname
                + condition;

        Cursor temp = db.rawQuery(str, null);

        if (temp != null) {
            if (temp.getCount() > 0) {
                Log.d("count", String.valueOf(temp.getCount()));
                temp.moveToFirst();

                try {
                    maxcode = Integer.valueOf(temp.getString(temp
                            .getColumnIndex("id")));
                    maxcode = maxcode + 1;
                } catch (NumberFormatException ne) {
                    db.close();
                    return maxcode;
                }
            }
        }
        db.close();
        return maxcode;
    }

    public void formatAndGetOSInListView(SimpleCursorAdapter cursoradaptor,
                                         final int colno) {
        cursoradaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                // TODO Auto-generated method stub
                if (columnIndex == colno) {
                    TextView txt = (TextView) view;
                    if (cursor.getDouble(columnIndex) == 0) {
                        txt.setText("");
                    } else {
                        txt.setText("Rs. "
                                + String.format(
                                "%.2f",
                                cursor.getDouble(columnIndex)
                                        - cursor.getDouble(columnIndex + 1)));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void formatCRDBNumberInListView(SimpleCursorAdapter cursoradaptor,
                                           final int colno) {
        cursoradaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                // TODO Auto-generated method stub
                if (columnIndex == colno) {
                    TextView txt = (TextView) view;
                    if (cursor.getDouble(columnIndex) == 0) {
                        txt.setText("");
                    } else {
                        double amt = getnullDbl(cursor.getDouble(columnIndex));
                        if (amt > 0) {
                            txt.setText("Rs. " + String.format("%.2f", amt)
                                    + " Db.");
                        } else {
                            txt.setText("Rs. "
                                    + String.format("%.2f", amt * -1) + " Cr.");
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void retOpInListView(SimpleCursorAdapter cursoradaptor,
                                final int colno, final int opdt, final int colno1f) {
        cursoradaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                // TODO Auto-generated method stub
                if (columnIndex == colno) {
                    TextView txt = (TextView) view;
                    double opamt = getOpeningAmount(
                            cursor.getInt(cursor.getColumnIndex("_id")), opdt);
                    if (opamt == 0) {
                        txt.setText("Opening: 0.00");
                    } else {
                        if (opamt > 0) {
                            txt.setText("Opening: Rs. "
                                    + String.format("%.2f", opamt) + " Db.");
                        } else {
                            txt.setText("Opening: Rs. "
                                    + String.format("%.2f", opamt * -1)
                                    + " Cr.");
                        }
                    }
                    return true;
                } else if (columnIndex == colno1f) {
                    TextView txt = (TextView) view;
                    if (cursor.getDouble(columnIndex) == 0) {
                        txt.setText("");
                    } else {
                        double amt = getnullDbl(cursor.getDouble(columnIndex));
                        if (amt > 0) {
                            txt.setText("Rs. " + String.format("%.2f", amt)
                                    + " Db.");
                        } else {
                            txt.setText("Rs. "
                                    + String.format("%.2f", amt * -1) + " Cr.");
                        }
                    }
                    return true;
                } else {

                }
                return false;
            }
        });
    }

    public double getOpeningAmount(int partykey, int opdt) {
        return getnulltxtDbl(getName("SELECT SUM(AMOUNT) FROM LEDGER WHERE PARTY_KEY="
                + partykey + " AND TRAN_DATE<" + opdt));
    }

    public void formatCashBookListView(SimpleCursorAdapter cursoradaptor,
                                       final int colno, final int colno2, final int colno3) {
        cursoradaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                // TODO Auto-generated method stub
                if (columnIndex == colno) {
                    TextView txt = (TextView) view;
                    if (cursor.getDouble(columnIndex) == 0) {
                        txt.setText("In Rs. ");
                    } else {
                        txt.setText("In Rs. "
                                + String.format("%.2f", getnullDbl(cursor
                                .getDouble(columnIndex))));
                    }
                    return true;
                } else if (columnIndex == colno2) {
                    TextView txt = (TextView) view;
                    if (cursor.getDouble(columnIndex) == 0) {
                        txt.setText("Out Rs. ");
                    } else {
                        txt.setText("Out Rs. "
                                + String.format("%.2f", getnullDbl(cursor
                                .getDouble(columnIndex))));
                    }
                    return true;
                } else if (columnIndex == colno3) {
                    TextView txt = (TextView) view;
                    if (cursor.getDouble(columnIndex) == 0) {
                        txt.setText("Balance Rs. ");
                    } else {
                        txt.setText("Balance Rs. "
                                + String.format("%.2f", getnullDbl(cursor
                                .getDouble(columnIndex))));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void formatNumberInListView(SimpleCursorAdapter cursoradaptor,
                                       final int colno) {
        cursoradaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                // TODO Auto-generated method stub
                if (columnIndex == colno) {
                    TextView txt = (TextView) view;
                    if (cursor.getDouble(columnIndex) == 0) {
                        txt.setText("");
                    } else {
                        txt.setText("Rs. "
                                + String.format("%.2f", getnullDbl(cursor
                                .getDouble(columnIndex))));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void formatServiceListView(SimpleCursorAdapter cursoradaptor,
                                      final int colno, final int colnoattachdone) {
        cursoradaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                // TODO Auto-generated method stub
                if (columnIndex == colno) {
                    TextView txt = (TextView) view;
                    if (cursor.getInt(columnIndex) == 1) {
                        txt.setText(Html.fromHtml("1"
                                + "<sup><small>st</small></sup>" + " Service"));
                    } else if (cursor.getInt(columnIndex) == 2) {
                        txt.setText(Html.fromHtml("2"
                                + "<sup><small>nd</small></sup>" + " Service"));
                    } else if (cursor.getInt(columnIndex) == 3) {
                        txt.setText(Html.fromHtml("3"
                                + "<sup><small>rd</small></sup>" + " Service"));
                    } else {
                        txt.setText(Html.fromHtml(cursor.getInt(columnIndex)
                                + "<sup><small>th</small></sup>" + " Service"));
                    }
                    return true;
                } else if (columnIndex == colnoattachdone) {
                    TextView txt = (TextView) view;

                    File file = new File(Environment
                            .getExternalStorageDirectory(),
                            "/ContractCRM/Service Images/"
                                    + cursor.getString(columnIndex) + ".jpg");
                    if (file.exists()) {
                        txt.setVisibility(View.VISIBLE);
                    }

                    return true;
                }
                return false;
            }
        });
    }

    public void formatContractwiseServiceListView(
            SimpleCursorAdapter cursoradaptor, final int colnodonedate,
            final int colnointime, final int colnoouttime) {
        cursoradaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                // TODO Auto-generated method stub
                if (columnIndex == colnointime || columnIndex == colnoouttime) {
                    TableRow tr = (TableRow) view.getParent();
                    TextView txt = (TextView) view;
                    String str = getnullStr(cursor.getString(columnIndex));
                    if (str.isEmpty()) {
                        tr.setVisibility(View.GONE);
                    } else {
                        tr.setVisibility(View.VISIBLE);
                        if (str.isEmpty()) {
                            txt.setText("Not Specified");
                        } else {
                            txt.setText(str);
                        }
                    }
                    return true;
                } else if (columnIndex == colnodonedate) {
                    TableRow tr = (TableRow) view.getParent();
                    TextView txt = (TextView) view;
                    String str = getnullStr(cursor.getString(columnIndex));
                    tr.setVisibility(View.VISIBLE);
                    if (str.isEmpty()) {
                        txt.setText("Pending");
                        txt.setTextColor(Color.parseColor("#FF3333"));
                        txt.setTypeface(null, Typeface.BOLD);
                    } else {
                        txt.setText(str);
                        txt.setTextColor(Color.parseColor("#111111"));
                        txt.setTypeface(null, Typeface.NORMAL);
                    }
                    return true;
                }

                return false;
            }
        });
    }

    public String getName(String sql) {

        String getName = "";
        SQLiteDatabase db = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        Cursor temp = db.rawQuery(sql, null);
        if (temp != null) {
            if (temp.getCount() > 0) {
                temp.moveToFirst();
                getName = getnullStr(temp.getString(0));
            }
        }
        temp.close();
        db.close();
        return getName;
    }

    public void StockUpdation(String module, int invkey) {
        SQLiteDatabase db = null;
        int modicode = 0;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READWRITE);

        if (module.equals("Opening")) {
            db.execSQL("DELETE FROM STOCK WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("STOCK", "STOCK_KEY", "");

            if (getnulltxtDbl(getName("SELECT PRODOPENING FROM PRODUCTMAST WHERE PRODUCT_KEY="
                    + invkey)) != 0) {
                db.execSQL("INSERT INTO STOCK SELECT " + modicode
                        + ", PRODUCT_KEY, '', '" + module
                        + "', PRODOPENING, PRODUNITTYPE,0,0, " + invkey
                        + ", 0 FROM PRODUCTMAST WHERE PRODUCTMAST.PRODUCT_KEY="
                        + invkey);
            }
        } else if (module.equals("Purchase")) {
            db.execSQL("DELETE FROM STOCK WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("STOCK", "STOCK_KEY", "");

            db.execSQL("INSERT INTO STOCK SELECT "
                    + modicode
                    + ", PRODUCT_KEY, DOC_DATE, '"
                    + module
                    + "', QTY, RATE, "
                    + invkey
                    + ", 0 FROM PURDET LEFT JOIN PURMST ON PURDET.PURCHASE_KEY=PURMST.PURCHASE_KEY WHERE PURDET.PURCHASE_KEY="
                    + invkey);
        } else if (module.equals("Sales")) {
            db.execSQL("DELETE FROM STOCK WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("STOCK", "STOCK_KEY", "");

            db.execSQL("INSERT INTO STOCK SELECT "
                    + modicode
                    + ", PRODUCT_KEY, BILL_DATE, '"
                    + module
                    + "', QTY * -1, RATE, "
                    + invkey
                    + ", SALMST.AGAINSTPURCHASE_KEY FROM SALDET LEFT JOIN SALMST ON SALDET.SALES_KEY=SALMST.SALES_KEY WHERE SALDET.SALES_KEY="
                    + invkey);
        }

        db.close();
    }

    public void LedgerUpdation(String module, int invkey) {
        SQLiteDatabase db = null;
        int modicode = 0;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READWRITE);

        if (module.equals("Opening")) {
            db.execSQL("DELETE FROM LEDGER WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("LEDGER", "LEDGER_KEY", "");

            db.execSQL("INSERT INTO LEDGER SELECT "
                    + modicode
                    + ", "
                    + invkey
                    + ", '', '"
                    + module
                    + "', OPENING, "
                    + invkey
                    + " FROM PARTYMAST WHERE CASE WHEN OPENING ISNULL THEN 0 ELSE OPENING END <>0 AND PARTY_KEY="
                    + invkey);
        } else if (module.equals("Purchase")) {
            db.execSQL("DELETE FROM LEDGER WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("LEDGER", "LEDGER_KEY", "");

            db.execSQL("INSERT INTO LEDGER SELECT " + modicode
                    + ", SUPPLIER_KEY, DOC_DATE, '" + module
                    + "', NET_AMOUNT*-1, " + invkey
                    + " FROM PURMST WHERE PURMST.PURCHASE_KEY=" + invkey);
        } else if (module.equals("Contract")) {
            db.execSQL("DELETE FROM LEDGER WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("LEDGER", "LEDGER_KEY", "");

            db.execSQL("INSERT INTO LEDGER SELECT " + modicode
                    + ", CUSTOMER_KEY, CONTRACT_DATE, '" + module
                    + "', NET_AMOUNT, " + invkey
                    + " FROM CONTRACTMST WHERE CONTRACTMST.CONTRACT_KEY="
                    + invkey);
        } else if (module.equals("Sales")) {
            db.execSQL("DELETE FROM LEDGER WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("LEDGER", "LEDGER_KEY", "");

            db.execSQL("INSERT INTO LEDGER SELECT " + modicode
                    + ", CUSTOMER_KEY, BILL_DATE, '" + module
                    + "', NET_AMOUNT, " + invkey
                    + " FROM SALMST WHERE SALMST.SALES_KEY=" + invkey);
        } else if (module.equals("Receipt")) {
            db.execSQL("DELETE FROM LEDGER WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("LEDGER", "LEDGER_KEY", "");

            db.execSQL("INSERT INTO LEDGER SELECT " + modicode
                    + ", PARTY_KEY, RECEIPT_DATE, '" + module
                    + "', AMOUNT*-1, " + invkey
                    + " FROM RECEIPT WHERE RECEIPT.RECEIPT_KEY=" + invkey);
        } else if (module.equals("Payment")) {
            db.execSQL("DELETE FROM LEDGER WHERE MODULE='" + module
                    + "' AND INV_KEY=" + invkey);

            modicode = SetMaxCode("LEDGER", "LEDGER_KEY", "");

            db.execSQL("INSERT INTO LEDGER SELECT " + modicode
                    + ", PARTY_KEY, PAYMENT_DATE, '" + module + "', AMOUNT, "
                    + invkey + " FROM PAYMENT WHERE PAYMENT.PAYMENT_KEY="
                    + invkey);
        }
        db.close();
    }

    public int getAppStartFlag() {
        return appStartFlag;
    }

    public void setAppStartFlag(int value) {
        appStartFlag = value;
    }

    public String saveDate(String pass) {
        String savingdate = "";
        if (pass.equals(null) || pass.equals("")) {
            savingdate = "";
        } else {
            savingdate = pass.substring(6, 10);
            savingdate = savingdate + pass.substring(3, 5);
            savingdate = savingdate + pass.substring(0, 2);
        }
        return savingdate;
    }

    public String retDate(String pass) {
        String retrivingdate = "";
        if (pass.equals(null) || pass.equals("")) {
            retrivingdate = "";
        } else {
            retrivingdate = pass.substring(6, 8) + "/";
            retrivingdate = retrivingdate + pass.substring(4, 6) + "/";
            retrivingdate = retrivingdate + pass.substring(0, 4);
        }
        return retrivingdate;
    }

    public String getnullStr(String pass) {
        String passStr;
        if (pass != null) {
            passStr = pass.trim();
            return passStr;
        } else {
            return "";
        }
    }

//    public void setReminderAlarm() {
//
//        Intent intent = new Intent(this, AlarmNotifyService.class);
//
//			PendingIntent pendingIntent = PendingIntent.getService(this, 1,
//					intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
////        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1,
////                intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis() + 1000 * 15 * 60, pendingIntent);
//
//
////                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
////                System.currentTimeMillis() + 1000 * 1 * 60, 1000 * 1 * 60, pendingIntent);
//
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
////                System.currentTimeMillis() + 1000 * 60 * 60, 1000 * 60 * 60, pendingIntent);
//    }
//
//    public Date parseDate(String date) {
//
//        final String inputFormat = "HH:mm";
//        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
//        try {
//            return inputParser.parse(date);
//        } catch (java.text.ParseException e) {
//            return new Date(0);
//        }
//    }

    public String getEscapeStr(String str) {
        String escStr;
        escStr = str.replace("'", "''");
        return escStr;
    }

    public Integer getnullInt(Integer pass) {

        if (pass != null) {
            try {
                return Integer.valueOf(pass);
            } catch (NumberFormatException ne) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Double getnullDbl(Double pass) {

        if (pass != null) {
            try {
                return Double.valueOf(pass);
            } catch (NumberFormatException ne) {
                return 0.00;
            }
        } else {
            return 0.00;
        }
    }

    public Integer getnulltxtInt(String pass) {
        if (pass != null && pass != "") {
            try {
                return Integer.valueOf(pass);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Double getnulltxtDbl(String pass) {

        if (pass != null) {
            try {
                return Double.valueOf(pass);
            } catch (Exception e) {
                return 0.00;
            }
        } else {
            return 0.00;
        }
    }

    public void backupDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            String time = getDATEString(0);
            time = time.replace("/", "-");

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + getPackageName()
                        + "//databases//" + "contractcrm.db";

                String backupDBPath = "/ContractCRM/BackUp/ContractCRM-" + time
                        + ".bkp";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                @SuppressWarnings("resource")
                FileChannel src = new FileInputStream(currentDB).getChannel();

                @SuppressWarnings("resource")
                FileChannel dst = new FileOutputStream(backupDB).getChannel();

                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Backup Successfully!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Error in SD Card. Check if it exists or having free Space",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(),
                    "Backup Database Failed!" + e.toString(), Toast.LENGTH_LONG)
                    .show();
            Log.d("error", e.toString());
        }
    }

    public void restoreDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canRead()) {
                String currentDBPath = "//data//" + getPackageName()
                        + "//databases//" + "contractcrm.db";

                String restoreDBPath = "/ContractCRM/Restore/Restore.bkp"; // From
                // SD
                // directory.
                File currentDB = new File(data, currentDBPath);
                File restoreDB = new File(sd, restoreDBPath);

                if (restoreDB.exists()) {
                    @SuppressWarnings("resource")
                    FileChannel src = new FileInputStream(restoreDB)
                            .getChannel();
                    @SuppressWarnings("resource")
                    FileChannel dst = new FileOutputStream(currentDB)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getApplicationContext(),
                            "Restore Database Successfully!",
                            Toast.LENGTH_SHORT).show();

                    restoreDB.delete();

                    // SQLiteDatabase db = null;
                    //
                    // String dbpath =
                    // this.getDatabasePath("contractcrm.db").toString();
                    // db = SQLiteDatabase.openDatabase(dbpath, null,
                    // SQLiteDatabase.OPEN_READONLY);

                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Restore.bkp file not found in /ContractCRM/Restore folder in Storage!",
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(GlobalActivity.this, "Database Restore Failed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public File createDailyReportPdf(String Header, String Header2, String Header3, ListView listViewReport, String Footer, String Footer2, String Footer3, Boolean _checked, String SummaryQty, String SummaryGross) throws FileNotFoundException, DocumentException {
        try
        {
            File pdfFolder = new File(Environment.getExternalStorageDirectory(), "/Score 3s Cloud/PDF Reports/");

            //Create time stamp
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

            File myFile = new File(pdfFolder + timeStamp + ".pdf");

            OutputStream output = new FileOutputStream(myFile);

            Document document = new Document();

            document.setPageSize(PageSize.LETTER);
            document.setMargins(36, 36, 36, 36);
            document.setMarginMirroring(true);

            //Step 1

            //Step 2
            PdfWriter.getInstance(document, output);

            //Step 3
            document.open();

            Font fontHeader = new Font(Font.FontFamily.TIMES_ROMAN, 36, Font.BOLD, BaseColor.BLUE);
            Font fontHeader2= new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD, BaseColor.BLUE);
            Font fontDetail= new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.NORMAL, BaseColor.BLACK);

            //header1
            Paragraph para = new Paragraph();
            para.setAlignment(Element.ALIGN_CENTER);
            para.setFont(fontHeader);

            para.add(Header);
            document.add(para);

            para = new Paragraph();
            para.setAlignment(Element.ALIGN_CENTER);
            para.setFont(fontHeader2);
            para.add(" ");
            document.add(para);

            //header2
            para = new Paragraph();
            para.setAlignment(Element.ALIGN_CENTER);
            para.setFont(fontHeader2);

            para.add(Header2);
            document.add(para);

            para = new Paragraph();
            para.setAlignment(Element.ALIGN_CENTER);
            para.setFont(fontHeader2);
            para.add(" ");
            document.add(para);

            //header3
            para = new Paragraph();
            para.setAlignment(Element.ALIGN_CENTER);
            para.setFont(fontHeader2);

            para.add(Header3);
            document.add(para);

            para = new Paragraph();
            para.setAlignment(Element.ALIGN_CENTER);
            para.setFont(fontHeader2);
            para.add(" ");
            document.add(para);

            para = new Paragraph();
            para.setAlignment(Element.ALIGN_CENTER);
            para.setFont(fontHeader2);
            para.add(" ");
            document.add(para);

            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(new float[]{ 144, 72, 72 });
            table.setWidthPercentage(100);

            document.add(new LineSeparator());
            LineSeparator sep = new LineSeparator();
            sep.setOffset(5);
            document.add(sep);

            PdfPCell cell = new PdfPCell(new Phrase(Header2.replace(" wise Sales Summary", "").replace(" wise Purchase Summary", ""), fontHeader2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5.0f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Qty", fontHeader2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(5.0f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Gross", fontHeader2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(5.0f);
            table.addCell(cell);

            table.setHeaderRows(1);

            for (int i = 0; i < listViewReport.getCount(); i++) {
                //View v = listViewReport.getChildAt(i);
                View v = listViewReport.getAdapter().getView(i, null, null);
                CheckBox _chkMark = (CheckBox) v.findViewById(R.id.checkMark);
                TextView _txtItem = (TextView) v.findViewById(R.id.item);
                TextView _txtQty = (TextView) v.findViewById(R.id.qty);
                TextView _txtGross = (TextView) v.findViewById(R.id.gross);
                if (_checked) {
                    if (_chkMark.isChecked()) {
                        cell = new PdfPCell(new Phrase(_txtItem.getText().toString(), fontDetail));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setPadding(5.0f);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(_txtQty.getText().toString(), fontDetail));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setPadding(5.0f);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(_txtGross.getText().toString(), fontDetail));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setPadding(5.0f);
                        table.addCell(cell);
                    }
                }
                else {
                    cell = new PdfPCell(new Phrase(_txtItem.getText().toString(), fontDetail));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setPadding(5.0f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(_txtQty.getText().toString(), fontDetail));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPadding(5.0f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(_txtGross.getText().toString(), fontDetail));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPadding(5.0f);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("Total", fontHeader2));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5.0f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(SummaryQty, fontHeader2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(5.0f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(SummaryGross, fontHeader2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(5.0f);
            table.addCell(cell);

            document.add(table);

            //Step 5: Close the document
            document.close();

            return myFile;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(),
                    "PDF generation failed.",
                    Toast.LENGTH_LONG).show();

            return null;
        }
    }

    public void viewPdf(File myFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            getApplicationContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(),
                    "PDF Reader application is not installed in your device.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void copyAssetsFiles(String filename) {
        AssetManager assetmanager = getAssets();
        InputStream in = null;
        OutputStream out = null;

        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            try {
                in = assetmanager.open(filename);
                out = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/ContractCRM/Assets/" + filename);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                Toast.makeText(GlobalActivity.this,
                        "Copy Assets files Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdir()) {
                ret = false;
            }
        }
        return ret;
    }

    public String retTemplateDetails(int module, Integer code,
                                     String templatename) {
        String details = "";
        int i = 0;
        SQLiteDatabase db = null;
        String str = "";
        String reportstr = "";
        Cursor temp = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        String modtype = getName("SELECT TEMPLATE_TYPE FROM TEMPLATE WHERE TEMPLATE_NAME='"
                + templatename + "'");

        if (module == 4) {
            details = getName("SELECT BIRTHDAY_SMSTEMPLATE FROM SETTINGS");

            details = details
                    + "\n\n"
                    + getnullStr(getName("SELECT SMSSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));

            db.close();
            return details.trim();

        } else if (module == 5) {
            details = getName("SELECT ANNIVERSARY_SMSTEMPLATE FROM SETTINGS");

            details = details
                    + "\n\n"
                    + getnullStr(getName("SELECT SMSSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));

            db.close();
            return details.trim();

        } else if (module == 6) {
            details = getName("SELECT PROFILE_MAILTEMPLATE FROM SETTINGS");
            db.close();
            return details.trim();
        }

        switch (module) {
            case 1: // Quotation
                reportstr = "";
                str = "SELECT FIELDTEXT, FIELDSTR, TEMPLATE_TYPE FROM TEMPLATE WHERE MODULE=1 AND TEMPLATE_NAME='"
                        + templatename + "' ORDER BY SRNO, DEFAULT_TEMPLATE";
                temp = db.rawQuery(str, null);
                while (temp.moveToNext()) {
                    if (reportstr.isEmpty()) {
                        reportstr = "SELECT '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    } else {
                        reportstr = reportstr + ", '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    }
                }
                reportstr = reportstr
                        + " FROM QUOTATION LEFT JOIN PRODUCTMAST ON QUOTATION.PRODUCT_KEY=PRODUCTMAST.PRODUCT_KEY LEFT JOIN PARTYMAST ON PRODUCTMAST.INSU_PKEY=PARTYMAST.PARTY_KEY LEFT JOIN PARTYMAST AS PARTYMAST_1 ON QUOTATION.PARTY_KEY=PARTYMAST_1.PARTY_KEY WHERE QUOTATION.QUOTATION_KEY="
                        + code;
                temp.close();

                break;

            case 2: // Contract
                reportstr = "";
                str = "SELECT FIELDTEXT, FIELDSTR, TEMPLATE_TYPE FROM TEMPLATE WHERE MODULE=2 AND TEMPLATE_NAME='"
                        + templatename + "' ORDER BY SRNO, DEFAULT_TEMPLATE";
                temp = db.rawQuery(str, null);
                while (temp.moveToNext()) {
                    if (reportstr.isEmpty()) {
                        reportstr = "SELECT '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    } else {
                        reportstr = reportstr + ", '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    }
                }
                reportstr = reportstr
                        + " FROM CONTRACTMST LEFT JOIN PRODUCTMAST ON CONTRACTMST.PRODUCT_KEY=PRODUCTMAST.PRODUCT_KEY LEFT JOIN PARTYMAST ON CONTRACTMST.CUSTOMER_KEY=PARTYMAST.PARTY_KEY LEFT JOIN PLANMAST ON CONTRACTMST.PLAN_KEY=PLANMAST.PLAN_KEY WHERE CONTRACTMST.CONTRACT_KEY="
                        + code;
                temp.close();

                break;

            case 3: // Contract Renewal
                reportstr = "";
                str = "SELECT FIELDTEXT, FIELDSTR, TEMPLATE_TYPE FROM TEMPLATE WHERE MODULE=3 AND TEMPLATE_NAME='"
                        + templatename + "' ORDER BY SRNO, DEFAULT_TEMPLATE";
                temp = db.rawQuery(str, null);
                while (temp.moveToNext()) {
                    if (reportstr.isEmpty()) {
                        reportstr = "SELECT '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    } else {
                        reportstr = reportstr + ", '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    }
                }
                reportstr = reportstr
                        + " FROM CONTRACTMST LEFT JOIN PRODUCTMAST ON CONTRACTMST.PRODUCT_KEY=PRODUCTMAST.PRODUCT_KEY WHERE CONTRACTMST.CONTRACT_KEY="
                        + code;
                temp.close();

                break;

            case 7: // SERVICE
                reportstr = "";
                str = "SELECT FIELDTEXT, FIELDSTR, TEMPLATE_TYPE FROM TEMPLATE WHERE MODULE=7 AND TEMPLATE_NAME='"
                        + templatename + "' ORDER BY SRNO, DEFAULT_TEMPLATE";
                temp = db.rawQuery(str, null);
                while (temp.moveToNext()) {
                    if (reportstr.isEmpty()) {
                        reportstr = "SELECT '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    } else {
                        reportstr = reportstr + ", '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    }
                }
                reportstr = reportstr
                        + " FROM SERVICEMST INNER JOIN CONTRACTMST ON SERVICEMST.CONTRACT_KEY=CONTRACTMST.CONTRACT_KEY LEFT JOIN PRODUCTMAST ON CONTRACTMST.PRODUCT_KEY=PRODUCTMAST.PRODUCT_KEY LEFT JOIN PLANMAST ON CONTRACTMST.PLAN_KEY=PLANMAST.PLAN_KEY LEFT JOIN PARTYMAST ON CONTRACTMST.CUSTOMER_KEY=PARTYMAST.PARTY_KEY WHERE SERVICEMST.SERVICE_KEY="
                        + code;
                temp.close();

                break;
            case 8: // SERVICE REMINDER
                reportstr = "";
                str = "SELECT FIELDTEXT, FIELDSTR, TEMPLATE_TYPE FROM TEMPLATE WHERE MODULE=8 AND TEMPLATE_NAME='"
                        + templatename + "' ORDER BY SRNO, DEFAULT_TEMPLATE";
                temp = db.rawQuery(str, null);
                while (temp.moveToNext()) {
                    if (reportstr.isEmpty()) {
                        reportstr = "SELECT '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    } else {
                        reportstr = reportstr + ", '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    }
                }
                reportstr = reportstr
                        + " FROM CONTRACTMST INNER JOIN INSTALLMENT ON CONTRACTMST.CONTRACT_KEY=INSTALLMENT.CONTRACT_KEY LEFT JOIN PLANMAST ON CONTRACTMST.PLAN_KEY=PLANMAST.PLAN_KEY LEFT JOIN PARTYMAST ON CONTRACTMST.CUSTOMER_KEY=PARTYMAST.PARTY_KEY WHERE CONTRACTMST.CONTRACT_KEY="
                        + code;
                temp.close();

                break;

            default:
                break;
        }

        temp = db.rawQuery(reportstr, null);
        while (temp.moveToNext()) {
            for (i = 0; i < temp.getColumnCount(); i++) {
                if (temp.getString(i).equals("LINE")) {
                    details = details + "\n";
                } else if (temp.getString(i).equals("SIGNATURE")) {
                    if (modtype.equals("SMS") || modtype.equals("Share")) {
                        details = details
                                + "\n"
                                + getnullStr(getName("SELECT SMSSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));
                    } else if (modtype.equals("E-Mail")) {
                        details = details
                                + "\n\n"
                                + getnullStr(getName("SELECT MAILSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));
                    }
                } else {
                    details = details + getnullStr(temp.getString(i));
                }
            }
        }
        db.close();
        return details.trim();
    }

    public String retSalesAgainstPurchase(Integer code, String sendtype) {

        String details = "";

        SQLiteDatabase db = null;
        String str = "";
        Cursor temp = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        details = getName("SELECT 'Bill No. ' || BILL_NO || ' Dtd. ' || SUBSTR(BILL_DATE,7,2) || '/' || SUBSTR(BILL_DATE,5,2) || '/' || SUBSTR(BILL_DATE,1,4) AS BILLDETAILS FROM PURMST WHERE PURCHASE_KEY="
                + code);

        // details=details + "</b>";

        // details=details +
        // "Item Name                     Qty/Unit    Rate/Unit          Amount";
        details = details + "\n" + "====================";

        str = "SELECT PRODUCTMAST.PRODUCTNAME AS PRODUCTNAME, SUM(SALDET.QTY) AS QT, SALDET.RATE, SUM(SALDET.AMOUNT) AS AMT FROM SALDET LEFT JOIN SALMST ON SALDET.SALES_KEY=SALMST.SALES_KEY LEFT JOIN PRODUCTMAST ON SALDET.PRODUCT_KEY=PRODUCTMAST.PRODUCT_KEY WHERE SALMST.AGAINSTPURCHASE_KEY="
                + code
                + " GROUP BY PRODUCTMAST.PRODUCTNAME, SALDET.RATE ORDER BY PRODUCTMAST.PRODUCTNAME";

        double total = 0;
        double qty = 0;
        temp = db.rawQuery(str, null);
        while (temp.moveToNext()) {
            details = details + "\n";
            details = details
                    + padLMR(
                    temp.getString(temp.getColumnIndex("PRODUCTNAME")),
                    30, "L");
            details = details + "\n";
            details = details
                    + padLMR(
                    String.format("%.2f",
                            temp.getDouble(temp.getColumnIndex("QT")))
                            + " X "
                            + "Rs."
                            + String.format("%.2f", temp.getDouble(temp
                            .getColumnIndex("RATE")))
                            , 30,
                    "L");
            details = details + " =";
            details = details
                    + " Rs. "
                    + padLMR(
                    String.format("%.2f",
                            temp.getDouble(temp.getColumnIndex("AMT"))),
                    15, "R");
            details = details + "\n" + "====================";

            qty = qty
            + getnullDbl(temp.getDouble(temp.getColumnIndex("QT")));
            total = total + temp.getDouble(temp.getColumnIndex("AMT"));
        }

        details = details
                + padLMR("\n TOTAL " + String.format("%.2f", qty)
                        + " NOS X Rs." + String.format("%.2f", total / qty),
                30, "L") + " = Rs."
                + padLMR(String.format("%.2f", total), 15, "R");
        details = details + "\n" + "====================";

        if (sendtype.equals("SMS")) {
            details = details
                    + "\n"
                    + getnullStr(getName("SELECT SMSSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));
        } else {// if (sendtype.equals("E-Mail")) {
            details = details
                    + "\n\n"
                    + getnullStr(getName("SELECT MAILSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));
        }

        temp.close();
        db.close();
        return details;
    }

    public String retPartyLedger(Integer code, String sendtype) {

        String details = "";

        SQLiteDatabase db = null;
        String str = "";
        Cursor temp = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        details = "Ledger : "
                + getName("SELECT FIRSTNAME FROM PARTYMAST WHERE PARTY_KEY="
                + code);

        // details=details + "</b>";
        details = details + "\n" + "====================";
        details = details
                + "\n"
                + "DESCRIPTION                    CR           DB             BAL";
        details = details + "\n" + "====================";

        str = "SELECT 0 AS ORD, 'Opening:' AS DESC, CASE WHEN AMOUNT<0 THEN AMOUNT*-1 ELSE 0 END AS CR, CASE WHEN AMOUNT>0 THEN AMOUNT ELSE 0 END AS DB FROM LEDGER WHERE MODULE='Opening' AND PARTY_KEY="
                + code;
        str = str + " UNION ";
        str = str
                + "SELECT 1 AS ORD, 'Purchase:' AS DESC, CASE WHEN AMOUNT<0 THEN AMOUNT*-1 ELSE 0 END AS CR, CASE WHEN AMOUNT>0 THEN AMOUNT ELSE 0 END AS DB FROM LEDGER WHERE MODULE='Purchase' AND PARTY_KEY="
                + code;
        str = str + " UNION ";
        str = str
                + "SELECT 2 AS ORD, 'Sales:' AS DESC, CASE WHEN AMOUNT>0 THEN 0 ELSE AMOUNT*-1 END AS CR, CASE WHEN AMOUNT<0 THEN 0 ELSE AMOUNT END AS DB FROM LEDGER WHERE MODULE='Sales' AND PARTY_KEY="
                + code;
        str = str + " UNION ";
        str = str
                + "SELECT 4 AS ORD, 'Receipt:' AS DESC, CASE WHEN AMOUNT<0 THEN AMOUNT*-1 ELSE 0 END AS CR, CASE WHEN AMOUNT>0 THEN AMOUNT ELSE 0 END AS DB FROM LEDGER WHERE MODULE='Receipt' AND PARTY_KEY="
                + code;
        str = str + " UNION ";
        str = str
                + "SELECT 3 AS ORD, 'Payment:' AS DESC, CASE WHEN AMOUNT<0 THEN AMOUNT ELSE 0 END AS CR, CASE WHEN AMOUNT>0 THEN AMOUNT ELSE 0 END AS DB FROM LEDGER WHERE MODULE='Payment' AND PARTY_KEY="
                + code;
        str = str + " ORDER BY ORD";

        // double total=0;
        double bal = 0;
        temp = db.rawQuery(str, null);
        while (temp.moveToNext()) {
            details = details + "\n";
            details = details
                    + padLMR(temp.getString(temp.getColumnIndex("DESC")), 15,
                    "L");
            details = details
                    + " "
                    + padLMR(
                    String.format("%.2f",
                            temp.getDouble(temp.getColumnIndex("CR"))),
                    12, "R");
            details = details
                    + " "
                    + padLMR(
                    String.format("%.2f",
                            temp.getDouble(temp.getColumnIndex("DB"))),
                    12, "R");

            bal = bal + temp.getDouble(temp.getColumnIndex("CR"))
                    - temp.getDouble(temp.getColumnIndex("DB"));

            if (bal > 0) {
                details = details + " "
                        + padLMR(String.format("%.2f", bal), 15, "R");
                details = details + " CR";
            } else if (bal < 0) {
                details = details + " "
                        + padLMR(String.format("%.2f", bal * -1), 15, "R");
                details = details + " DB";
            } else {
                details = details + " " + padLMR("0.00", 15, "R");
                details = details + " DB";
            }
            // total=total+ temp.getDouble(temp.getColumnIndex("AMT"));
        }

        // details=details + "\n TOTAL Rs. " + padLMR(String.format("%.2f",
        // total),15,"L");
        // details=details + "\n" + "====================";

        if (sendtype.equals("SMS")) {
            details = details
                    + "\n\n"
                    + getnullStr(getName("SELECT SMSSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));
        } else {// if (sendtype.equals("E-Mail")) {
            details = details
                    + "\n\n"
                    + getnullStr(getName("SELECT MAILSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));
        }

        temp.close();
        db.close();
        return details;
    }

    public static String padLMR(String str, int len, String Align) {
        if (str == null || str == "0" || str == "" || (len - str.length()) == 0) {
            return "";
        }
        if (Align == "L") {
            return str
                    + String.format("%" + (len - str.length()) + "s", "")
                    .replace(" ", String.valueOf(" "));
        } else if (Align == "R") {
            return String.format("%" + (len - str.length()) + "s", "").replace(
                    " ", String.valueOf(" "))
                    + str;
        } else {
            return str;
        }
    }

    public int chkRenewalStatus() {
        int days = 9;

        int today = getnulltxtInt(getDATENumber(0));

        int rendatedue = getnulltxtInt(getDateAfterDaysNumber(
                retDate(getnullStr(getName("SELECT VALIDUPTO_DATE FROM SETTINGS"))),
                -8, "DAY"));

        int rendate = getnulltxtInt(getnullStr(getName("SELECT VALIDUPTO_DATE FROM SETTINGS")));

        if (today > rendate) {
            days = 0;
            return days;
        } else if (today >= rendatedue) {
            days = 1;
            return days;
        }

        return days;
    }

    public String chkReminderStatus() {
        String status = "";

        try {
            String currdate = getDateAfterDaysNumber(
                    "",
                    getnulltxtInt(getName("SELECT REMINDER_BEFOREDAYS FROM SETTINGS WHERE PARTY_KEY=1")) * -1,
                    "DAY");
            String nextDate = getDateAfterDaysNumber(
                    "",
                    getnulltxtInt(getName("SELECT REMINDER_FORDAYS FROM SETTINGS WHERE PARTY_KEY=1")),
                    "DAY");

            SQLiteDatabase db = null;

            String dbpath = this.getDatabasePath("contractcrm.db").toString();
            db = SQLiteDatabase.openDatabase(dbpath, null,
                    SQLiteDatabase.OPEN_READONLY);

            // RENEWAL
            Cursor c = null;

            String sqlstr = "SELECT CONTRACT_KEY AS _id, CONTRACTMST.TO_DATE AS ORDERDT";
            sqlstr = sqlstr + " FROM CONTRACTMST ";
            sqlstr = sqlstr + " WHERE CAST(TO_DATE AS INTEGER) >=" + currdate
                    + " AND CAST(TO_DATE AS INTEGER) <=" + nextDate;
            sqlstr = sqlstr + " AND CONTRACTMST.STATUS='Fully Serviced' ";

            // SERVICE
            sqlstr = sqlstr + " UNION ";
            sqlstr = sqlstr
                    + "SELECT INSTALLMENT.CONTRACT_KEY AS _id, INSTALLMENT.ISTART_DATE AS ORDERDT ";
            sqlstr = sqlstr
                    + " FROM INSTALLMENT INNER JOIN CONTRACTMST ON INSTALLMENT.CONTRACT_KEY=CONTRACTMST.CONTRACT_KEY ";
            sqlstr = sqlstr
                    + " WHERE CAST(INSTALLMENT.ISTART_DATE AS INTEGER) >="
                    + currdate
                    + " AND CAST(INSTALLMENT.ISTART_DATE AS INTEGER) <="
                    + nextDate;
            sqlstr = sqlstr + " AND INSTALLMENT.SERVICE_KEY=0 ";
            sqlstr = sqlstr + " AND CONTRACTMST.STATUS='In Force' ";

            sqlstr = sqlstr + " ORDER BY ORDERDT DESC";

            c = db.rawQuery(sqlstr, null);

            if (c != null && c.getCount() > 0) {
                status = "Renewal";
                db.close();
                return status;
            }
            c.close();

            currdate = getDATENumber(0);
            // BIRTHDAY
            c = db.rawQuery(
                    "SELECT PARTY_KEY AS _id FROM PARTYMAST WHERE PARTYTYPE=1 AND BIRTHDATE='"
                            + currdate + "' ORDER BY FIRSTNAME", null);
            if (c != null && c.getCount() > 0) {
                status = "Birth Day";
                db.close();
                return status;
            }
            c.close();

            // ANNIVERSARY
            c = db.rawQuery(
                    "SELECT FIRSTNAME || CASE WHEN AREA || ''<>'' THEN ' (' || AREA || ')' ELSE '' END AS NAME, SUBSTR(ANNIVERSARY,7,2) || '/' || SUBSTR(ANNIVERSARY,5,2) || '/' || SUBSTR(ANNIVERSARY,1,4) AS ANNIVERSARYDT, PARTY_KEY AS _id FROM PARTYMAST WHERE PARTYTYPE=1 AND ANNIVERSARY='"
                            + currdate + "' ORDER BY FIRSTNAME", null);
            if (c != null && c.getCount() > 0) {
                status = "Marriage";
                db.close();
                return status;
            }
            c.close();
            db.close();
            return status;
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            return status;
        }
    }

    // public void createBillingDetailsHTML(int contractkey) {
    // String details = "";
    //
    // SQLiteDatabase db = null;
    // String str = "";
    // Cursor temp = null;
    //
    // String dbpath = this.getDatabasePath("contractcrm.db").toString();
    // db = SQLiteDatabase.openDatabase(dbpath, null,
    // SQLiteDatabase.OPEN_READONLY);
    //
    // details="<HTML>";
    // details="<TABLE>";
    // details="<TR><TD> CONTRACT NO. </TD><TD> DATE </TD><TD> PARTY NAME </TD><TD> PRODUCT </TD><TD> PLAN </TD><TD> FREQUENCY </TD></TR>";
    // details="<TR><TD> 1 </TD><TD> 12/12/2016 </TD><TD> VIJAY PANCHASARA </TD><TD> KENT RO </TD><TD> AMC </TD><TD> QUARTERLY </TD></TR>";
    //
    //
    // temp=db.rawQuery("SELECT PARTYMAST.FIRSTNAME || ' (' || CONTRACT_TYPE || ')' AS NAME, 'Contract No. ' || CASE WHEN CONTRACT_MANUALNO='' THEN CONTRACT_KEY ELSE CONTRACT_MANUALNO END || ' Dtd. ' || SUBSTR(CONTRACT_DATE,7,2) || '/' || SUBSTR(CONTRACT_DATE,5,2) || '/' || SUBSTR(CONTRACT_DATE,1,4) AS CONNODATE, 'Period : ' || SUBSTR(FROM_DATE,7,2) || '/' || SUBSTR(FROM_DATE,5,2) || '/' || SUBSTR(FROM_DATE,1,4) || ' To ' || SUBSTR(TO_DATE,7,2) || '/' || SUBSTR(TO_DATE,5,2) || '/' || SUBSTR(TO_DATE,1,4) AS PERIOD, PLANMAST.PLANNAME || ' (' || PLANMAST.PLANFREQ || ')' AS PLAN, NET_AMOUNT, CONTRACTMST.STATUS, CONTRACT_KEY AS _id, CONTRACT_KEY AS ATTACH FROM CONTRACTMST LEFT JOIN PARTYMAST ON CONTRACTMST.CUSTOMER_KEY=PARTYMAST.PARTY_KEY LEFT JOIN PLANMAST ON CONTRACTMST.PLAN_KEY=PLANMAST.PLAN_KEY WHERE CONTRACTMST.CONTRACT_KEY="
    // + contractkey + "%' ORDER BY CONTRACTMST.STATUS DESC",null);
    //
    // // while (temp.moveToNext()) {
    // // details = details + "\n";
    // // details = details
    // // + padLMR(temp.getString(temp.getColumnIndex("DESC")), 15,
    // // "L");
    // // details = details
    // // + " "
    // // + padLMR(
    // // String.format("%.2f",
    // // temp.getDouble(temp.getColumnIndex("CR"))),
    // // 12, "R");
    // // details = details
    // // + " "
    // // + padLMR(
    // // String.format("%.2f",
    // // temp.getDouble(temp.getColumnIndex("DB"))),
    // // 12, "R");
    // //
    // // bal = bal + temp.getDouble(temp.getColumnIndex("CR"))
    // // - temp.getDouble(temp.getColumnIndex("DB"));
    // //
    // // if (bal > 0) {
    // // details = details + " "
    // // + padLMR(String.format("%.2f", bal), 15, "R");
    // // details = details + " CR";
    // // } else if (bal < 0) {
    // // details = details + " "
    // // + padLMR(String.format("%.2f", bal * -1), 15, "R");
    // // details = details + " DB";
    // // } else {
    // // details = details + " " + padLMR("0.00", 15, "R");
    // // details = details + " DB";
    // // }
    // // // total=total+ temp.getDouble(temp.getColumnIndex("AMT"));
    // // }
    // //
    // // // details=details + "\n TOTAL Rs. " + padLMR(String.format("%.2f",
    // // // total),15,"L");
    // // // details=details + "\n" + "====================";
    //
    // // temp.close();
    // db.close();
    // }

    public String retTemplateDetails(int module, Integer code,
                                     String templatename, String installmentdate, int installmentsrno) {
        String details = "";
        int i = 0;
        SQLiteDatabase db = null;
        String str = "";
        String reportstr = "";
        Cursor temp = null;

        String dbpath = this.getDatabasePath("contractcrm.db").toString();
        db = SQLiteDatabase.openDatabase(dbpath, null,
                SQLiteDatabase.OPEN_READONLY);

        String modtype = getName("SELECT TEMPLATE_TYPE FROM TEMPLATE WHERE TEMPLATE_NAME='"
                + templatename + "'");

        switch (module) {
            case 8: // SERVICE REMINDER
                reportstr = "";
                str = "SELECT FIELDTEXT, FIELDSTR, TEMPLATE_TYPE FROM TEMPLATE WHERE MODULE=8 AND TEMPLATE_NAME='"
                        + templatename + "' ORDER BY SRNO, DEFAULT_TEMPLATE";
                temp = db.rawQuery(str, null);
                while (temp.moveToNext()) {
                    if (reportstr.isEmpty()) {
                        reportstr = "SELECT '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    } else {
                        reportstr = reportstr + ", '"
                                + temp.getString(temp.getColumnIndex("FIELDTEXT"))
                                + "' || "
                                + temp.getString(temp.getColumnIndex("FIELDSTR"));
                    }
                }
                reportstr = reportstr
                        + " FROM CONTRACTMST INNER JOIN INSTALLMENT ON CONTRACTMST.CONTRACT_KEY=INSTALLMENT.CONTRACT_KEY LEFT JOIN PRODUCTMAST ON CONTRACTMST.PRODUCT_KEY=PRODUCTMAST.PRODUCT_KEY LEFT JOIN PLANMAST ON CONTRACTMST.PLAN_KEY=PLANMAST.PLAN_KEY LEFT JOIN PARTYMAST ON CONTRACTMST.CUSTOMER_KEY=PARTYMAST.PARTY_KEY WHERE CONTRACTMST.CONTRACT_KEY="
                        + code + " AND INSTALLMENT.SRNO=" + installmentsrno;
                temp.close();
                break;
            default:
                break;
        }

        temp = db.rawQuery(reportstr, null);
        if (temp != null && temp.getCount() > 0) {
            temp.moveToFirst();
            // while (temp.moveToNext()) {
            for (i = 0; i < temp.getColumnCount(); i++) {
                if (temp.getString(i).equals("LINE")) {
                    details = details + "\n";
                } else if (temp.getString(i).equals("SIGNATURE")) {
                    if (modtype.equals("SMS")) {
                        details = details
                                + "\n"
                                + getnullStr(getName("SELECT SMSSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));
                    } else if (modtype.equals("E-Mail")) {
                        details = details
                                + "\n\n"
                                + getnullStr(getName("SELECT MAILSIGNATURE FROM PARTYMAST WHERE PARTYTYPE=0"));
                    }
                } else {
                    details = details + getnullStr(temp.getString(i));
                }
            }
            // }
        }

        if (module == 8) {
            details = details.replace("XX/XX/XXXX", " " + installmentdate);
        }
        db.close();
        return details.trim();
    }
}