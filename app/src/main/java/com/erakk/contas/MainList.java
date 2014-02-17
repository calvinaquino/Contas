package com.erakk.contas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainList extends ListActivity implements OnClickListener {

	private TransactionAdapter transactionAdapter;
	TransactionManager transactionManager;
    Spinner monthSpinner;
	boolean footerOk;
	boolean headerOk;
	int countt;
    int editing;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
        monthSpinner = (Spinner)findViewById(R.id.monthSpinner);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        monthSpinner.setSelection(calendar.get(Calendar.MONTH));
        Log.d("MONTH",""+calendar.get(Calendar.MONTH));

        footerOk = false;
		countt = 0;
        editing = 0;
		
		try {
			updateContent();			
		} catch (Exception ex) {
			Log.e(ex.toString(), ex.toString());
		}
	}

	public void updateContent() {
		transactionManager = new TransactionManager(getApplicationContext());
		
		// get the values from database
        ArrayList<Transaction> transactions = transactionManager.getTransactions(monthSpinner.getSelectedItemPosition());

		transactionAdapter = new TransactionAdapter(
				getApplicationContext(), R.layout.transaction_row, transactions,monthSpinner.getSelectedItemPosition());
		
		if (!footerOk) {
			View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.transaction_footer, null, false);
			getListView().addFooterView(footerView);
			footerOk = true;
		}
		if (!headerOk) {
			View headerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.transaction_header, null, false);
			getListView().addHeaderView(headerView);
			headerOk = true;
		}
		
		setListAdapter(transactionAdapter);
		
		TextView totalValue = (TextView) findViewById(R.id.transactionTotalValue);
		
		totalValue.setText(String.format( "R$ %.2f", Double.parseDouble(transactionManager.getTotal(monthSpinner.getSelectedItemPosition())) ));

        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                editTransaction(i-1);
                return false;
            }
        });
//		totalValue.setText("R$ " + transactionManager.getTotal());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_list, menu);
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.update:    	
        		updateContent();
    			return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	public void clearDatabase(View view) {
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Tem certeza que deseja zerar a lista?");
		alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	    		transactionManager.clear();
	    		updateContent();
	        }
	    });

	    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.cancel();
	        }
	    });
	    alert.show();
	}
	
	public void addDebt (View view) {
		showInputDialog(0);
//		transactionManager.insert(entry[0], 0, Integer.getInteger(entry[1]));
		updateContent();
	}
	public void addBonus (View view) {
		showInputDialog(1);
//		transactionManager.insert(entry[0], 1, Integer.getInteger(entry[1]));
		updateContent();
	}

    public void editTransaction (int i) {
        editing = i;
        transactionManager = new TransactionManager(getApplicationContext());
        // get the values from database
        ArrayList<Transaction> transactions = transactionManager.getTransactions(monthSpinner.getSelectedItemPosition());

        Log.d("LISTVIEW","long clicked " + i + " of type " + transactions.get(i).getType());
        showInputDialog(2);
    }

    public Transaction getTransaction(int id) {
        transactionManager = new TransactionManager(getApplicationContext());
        // get the values from database
        ArrayList<Transaction> transactions = transactionManager.getTransactions(monthSpinner.getSelectedItemPosition());

        return transactions.get(id);
    }
	
	public void showInputDialog (final int type) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		final String[] result = new String[2];
		
		FrameLayout fl = (FrameLayout) findViewById(android.R.id.custom);
		LayoutInflater inflater = getLayoutInflater();
		View input = inflater.inflate(R.layout.dialog_input, fl, false);
	    alert.setView(input);
	    
	    final EditText inputName = (EditText) input.findViewById(R.id.inputName);
	    final EditText inputValue= (EditText) input.findViewById(R.id.inputValue);

        inputName.setLines(1);
        inputValue.setLines(1);
        inputName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("KEY",""+keyEvent);
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        inputValue.requestFocus();
                        inputValue.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        inputName.setText("");
        if (type == 2) {
            Transaction t = getTransaction(editing);
            inputName.setText(t.getName());
            inputValue.setText(t.getValue()+"");
        }

	    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	    	    result[0] = inputName.getText().toString();
	    	    result[1] = inputValue.getText().toString();
                if (type == 2) {
                    final Transaction t = getTransaction(editing);
                    editTransaction(editing,result[0],t.getType(),Double.parseDouble(result[1]));
                }
	    		else {
                    insertTransaction(result[0], type, Double.parseDouble(result[1]));
                }
//                insertTransaction(result[0], type, Double.parseDouble(result[1]));
	    		updateContent();
	        }
	    });

	    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.cancel();
	        }
	    });

	    alert.show();
        inputName.requestFocus();
	}
	
	public void insertTransaction(String nome,int tipo, double valor) {
        transactionManager.insert(nome, tipo, valor,monthSpinner.getSelectedItemPosition());
    }

    public void editTransaction(int id, String nome,int tipo, double valor) {
        Log.d("UI","editing "+getTransaction(id).getId());
//        transactionManager.delete(getTransaction(id).getId());
        transactionManager.update(getTransaction(id).getId(),nome,tipo, valor,monthSpinner.getSelectedItemPosition());
//        transactionAdapter.deleteRow(getTransaction(id));
        updateContent();
    }
}

	
	
	
//	@Override
//	public void onClick(View v) {
//		ImageButton button = (ImageButton) v;
//		Stock row = (Stock) button.getTag();
//		
//		tableRowAdapter.deleteRow(row);
//		tableRowAdapter.notifyDataSetChanged();
//		
//	}
//}
