package com.erakk.contas;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

public class TransactionManager {

    private DBHelper dbHelper;
    
    private static final String TABLE = "transacion";
    private static final String COLUMN_ID = "_id";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private static final String MONTH = "month";
	
	private static final String SELECT_TRANSACTIONS = "SELECT * FROM " + TABLE;
	
	public TransactionManager(Context context) {
		dbHelper = new DBHelper(context, TABLE,
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ NAME + " TEXT, "
				+ TYPE + " INTEGER, "
				+ VALUE + " DOUBLE, "
                + MONTH + " INTEGER ");
	}
	
	public boolean insert(String name, int type, double value, int month) {
        try {

            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

            ContentValues initialValues = new ContentValues();

            initialValues.put(NAME, name);
            initialValues.put(TYPE, type);
            initialValues.put(VALUE, value);
            initialValues.put(MONTH, month);

            sqlite.insert(TABLE, null, initialValues);

        } catch (SQLException sqlerror) {

            Log.v("Insert into table error", sqlerror.getMessage());

            return false;
        }

        return true;

    }
	
	public boolean delete(String id) {
        try {

            Log.d("MANAGER","deleting "+id);
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            
            sqlite.delete(TABLE, COLUMN_ID + "=" + id, null);

        } catch (SQLException sqlerror) {

            Log.v("Delete into table error", sqlerror.getMessage());

            return false;
        }

        return true;

    }

    public boolean update(String id, String name, int type, double value, int month) {
        try {

            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

//            sqlite.delete(TABLE, COLUMN_ID + "=" + id, null);

            ContentValues initialValues = new ContentValues();

            initialValues.put(COLUMN_ID, id);
            initialValues.put(NAME, name);
            initialValues.put(TYPE, type);
            initialValues.put(VALUE, value);
            initialValues.put(MONTH, month);

//            sqlite.insert(TABLE, null, initialValues);
            sqlite.replace(TABLE, null, initialValues);

        } catch (SQLException sqlerror) {

            Log.v("Update into table error", sqlerror.getMessage());

            return false;
        }

        return true;

    }
	
	public String getTotal(int month) {

		ArrayList<Transaction> transactions = getTransactions(month);
		
		double total = 0;
		for (int i = 0; i < transactions.size(); i++){
			Transaction trans = transactions.get(i);
			if (trans.getType() == 1)
				total += trans.getValue();
			else
				total -= trans.getValue();
		}
		return Double.toString(total);
	}
	
	public ArrayList<Transaction> getTransactions(int month) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        SQLiteDatabase sqliteDB = dbHelper.getReadableDatabase();

        Cursor crsr = sqliteDB.rawQuery(SELECT_TRANSACTIONS + " WHERE " + MONTH + " = " + month, null);

        crsr.moveToFirst();

        for (int i = 0; i < crsr.getCount(); i++)
        {
        	transactions.add(new Transaction(crsr.getString(0),crsr.getString(1), crsr.getInt(2), crsr.getDouble(3), crsr.getInt(4)));

            crsr.moveToNext();
        }

        return transactions;
    }
	
	public void clear() {

        try {

            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

            sqlite.delete(TABLE, "", null);
            
        } catch (SQLException sqlerror) {

            Log.v("delete from table error", sqlerror.getMessage());

        }    
    }

}
