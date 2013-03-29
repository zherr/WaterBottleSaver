package com.example.waterbottlesaver;

import android.provider.BaseColumns;

public class WBF_DBContract {
	
	/**Prevent this class from being instantiated */
	private WBF_DBContract(){ }
	
	public static abstract class WaterBottleFillEntry implements BaseColumns{
		public static final String TABLE1 = "waterbottlefill";
		public static final String KEY_ID1 = "fill_id";
		public static final String KEY_FILLVAL = "fill_val";
		public static final String KEY_BOTTLESIZE = "bottle_size";
		public static final String KEY_TOTALSAVED = "total_saved";
	}
	
	public static abstract class WaterBottleHistoryEntry implements BaseColumns{
		public static final String TABLE1 = "history";
		public static final String KEY_ID1 = "hist_id";
		public static final String TIME = "node_time";
		public static final String TOTAL = "node_total";
		public static final String SAVED = "node_saved";
	}
	
}
