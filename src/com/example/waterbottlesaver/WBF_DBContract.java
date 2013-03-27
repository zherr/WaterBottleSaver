package com.example.waterbottlesaver;

import android.provider.BaseColumns;

public class WBF_DBContract {
	
	/**Prevent this class from being instantiated */
	private WBF_DBContract(){ }
	
	public static abstract class WaterBottleFillEntry implements BaseColumns{
		public static final String TABLE1 = "waterbottlefill";
		public static final String KEY_ID1 = "fill_id";
		public static final String KEY_FILLVAL = "fill_val";
	}
}
