package com.example.waterbottlesaver;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.math.*;

public class MainActivity extends Activity {
	
	private WaterBottleSaverDB sqlDB;
	private DBTestAdapter dbTest;
	//private static final float avgBottleSize = 20; //20 oz.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sqlDB = new WaterBottleSaverDB(this);
		
		dbTest = new DBTestAdapter(this);
		
		dbTest.createDatabase();
		dbTest.open();
		Log.d("onCreate", "BEFORE DB TEST");
		Cursor cursor = dbTest.getTestData();
		Log.d("onCreate", "AFTER DB TEST");
		dbTest.close();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final EditText mEditFillBottleText = (EditText)findViewById(R.id.editText1);
		final EditText mEditBottleSize = (EditText)findViewById(R.id.editText2);
		final TextView  totalFilledText = (TextView)findViewById(R.id.textView2);
		final TextView avgBottlesText = (TextView)findViewById(R.id.textView5);
		
		totalFilledText.setText(Integer.toString(sqlDB.getTotalFills()));
		avgBottlesText.setText(Float.toString(sqlDB.getTotalSaved()));
		
		final Button enterBottleSize = (Button) findViewById(R.id.button2);
		enterBottleSize.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				float bottleSize = Float.parseFloat((mEditBottleSize.getText().toString()));
				if(sqlDB.adjustBottleSize(bottleSize)){ 
					Log.d("Click Bottle Size", "Entered new size" + Float.toString(bottleSize));
					Toast.makeText(getApplicationContext(), "Adjusted bottle size", Toast.LENGTH_SHORT).show();
				} 
			}
		});
		
		final Button enterBottleFill = (Button) findViewById(R.id.button1);
		enterBottleFill.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int insert = Integer.parseInt(mEditFillBottleText.getText().toString());
				if(sqlDB.insertWaterFill(insert)){
					Log.d("insertWaterFill", "INSERTED PARSED NEW BOTTLE COUNT");
				}
				int newVal = sqlDB.getTotalFills();
				totalFilledText.setText(Integer.toString(newVal));
				if(sqlDB.adjustTotalSaved(insert)){
					Log.d("adjustTotalSaved", "ADJUSTED NEW AVG");
				}
				
				float newAvg = sqlDB.getTotalSaved();
				avgBottlesText.setText((Float.toString(newAvg)).substring(0, 3));
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_layout, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final TextView  totalFilledText = (TextView)findViewById(R.id.textView2);
		final TextView totalSavedText = (TextView)findViewById(R.id.textView5);
		switch(item.getItemId()){
			case(R.id.clear):
				sqlDB.clearTotalFills();
				sqlDB.clearBottlesSaved();
				totalFilledText.setText(Integer.toString(sqlDB.getTotalFills()));
				totalSavedText.setText(Float.toString(sqlDB.getTotalSaved()));
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}

}
