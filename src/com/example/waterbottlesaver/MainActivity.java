package com.example.waterbottlesaver;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private WaterBottleSaverDB sqlDB;
	private DBTestAdapter dbTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sqlDB = new WaterBottleSaverDB(this);
		
		dbTest = new DBTestAdapter(this);
		
		dbTest.createDatabase();
		dbTest.open();
		
		Cursor cursor = dbTest.getTestData();
		dbTest.close();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final EditText mEditFillBottleText = (EditText)findViewById(R.id.editText1);
		final TextView  testView = (TextView)findViewById(R.id.textView2);
		
		testView.setText(Integer.toString(sqlDB.getTotalFills().getInt(0)));
		
		final Button testDBButton = (Button) findViewById(R.id.button1);
		testDBButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final int insert = Integer.parseInt(mEditFillBottleText.getText().toString());
				sqlDB.insertWaterFill(insert);
				int newVal = sqlDB.getTotalFills().getInt(0);
				testView.setText(Integer.toString(newVal));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
