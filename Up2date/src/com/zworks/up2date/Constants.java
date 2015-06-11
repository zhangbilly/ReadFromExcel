package com.zworks.up2date;

import android.provider.BaseColumns;

public final class Constants {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "database.db";
	private static final String TEXT_TYPE = " TEXT ";
	private static final String INT_TYPE = " INT ";
	private static final String COMMA_SEP = ",";
	
	 // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private Constants() {}

    public static abstract class Program implements BaseColumns {
		public static final String TABLE_NAME = "program";
		public static final String PROGRAM_NAME = "programname";
		public static final String UPDATE_TIME = "updatetime";
		public static final String BEGIN_TIME = "begintime";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PROGRAM_NAME + TEXT_TYPE + "NOT NULL"+COMMA_SEP +
                UPDATE_TIME + TEXT_TYPE + "NOT NULL"+COMMA_SEP +
                BEGIN_TIME + INT_TYPE + "NOT NULL"+ " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    
    
    public static abstract class Record implements BaseColumns {
		public static final String TABLE_NAME = "record";
		public static final String PROGRAM_NAME = "programname";
		public static final String RECORD_TIME = "recordtime";
		public static final String ISNEW = "isnew";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PROGRAM_NAME + INT_TYPE + "NOT NULL"+COMMA_SEP +
                RECORD_TIME + INT_TYPE + "NOT NULL"+COMMA_SEP +
                ISNEW + INT_TYPE + "NOT NULL"+COMMA_SEP +
                " FOREIGN KEY ("+PROGRAM_NAME+") REFERENCES "+Program.TABLE_NAME+" ("+Program._ID+"));"; 
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    
    
}
