package com.erakk.contas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;

    private String createStatement = "";

	public DBHelper(Context context, String tableName, String fields) {

        super(context, tableName, null, DATABASE_VERSION);
        
        this.createStatement  = "CREATE TABLE ";
        this.createStatement += tableName + " (";
        this.createStatement += fields + ");";

    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(this.createStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
