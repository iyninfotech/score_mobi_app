package com.score3s.scoremobi;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
	SQLiteDatabase db=null;
	GlobalActivity globalVariable=null;

	EditText edt_imei=null;
	EditText edt_fcmtoken=null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.settings);

        globalVariable = (GlobalActivity) getApplicationContext();

        getWindow().setSoftInputMode(
			    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_setting);
		getSupportActionBar().setTitle("Settings");
		//getSupportActionBar().setSubtitle("App. Configuration");

		edt_imei=(EditText)findViewById(R.id.edt_imei);
		edt_fcmtoken=(EditText)findViewById(R.id.edt_fcmtoken);

		edt_imei.setText(globalVariable.strIMEI);

		SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.getString("regId", token);
//
//        String s=pref.getString("regId","");
		edt_fcmtoken.setText(pref.getString("regId",""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menusettings, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
		finish();
    	super.onBackPressed();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id==R.id.close) {
        	finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
}
