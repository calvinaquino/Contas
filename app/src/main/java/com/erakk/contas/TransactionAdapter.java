package com.erakk.contas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TransactionAdapter extends BaseAdapter {

	TransactionManager transactionManager;
	
    private final List<Transaction> rows;
    int month;

    public TransactionAdapter(final Context context, final int itemResId,
            final List<Transaction> items, int month) {
        this.rows = items;
        this.month = month;
		transactionManager = new TransactionManager(context);
    }

    public int getCount() {
        return this.rows.size();
    }

    public Object getItem(int position) {
        return this.rows.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * Set the content for a row here
     */

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Transaction row = this.rows.get(position);
        View itemView = null;
        
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        if (convertView == null) {
//        	LayoutInflater inflater = (LayoutInflater) parent.getContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.transaction_row, null);
        } else {
            itemView = convertView;
        }
        
        final View headerView = inflater.inflate(R.layout.transaction_header, null);
        final TextView txtTotal = (TextView)headerView.findViewById(R.id.transactionTotalValue);;
        
        Button dltButton = (Button) itemView.findViewById(R.id.deleteEntry);
        // Set the text of the row
        TextView txtName = (TextView) itemView.findViewById(R.id.transactionName);
        txtName.setText(row.getName());
        
        TextView txtType = (TextView) itemView.findViewById(R.id.transactionType);
        if (row.getType() == 1)
        	txtType.setText("Recebido");
        else if (row.getType() == 0)
        	txtType.setText("Pago");
        
        TextView txtValue = (TextView) itemView.findViewById(R.id.transactionValue);
        
		
//        txtValue.setText("R$ " + Double.toString(row.getValue()));
        txtValue.setText(String.format( "R$ %.2f", row.getValue() ));
        
        
        dltButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
        		transactionManager.delete(row.getId());
        		deleteRow(row);
        		txtTotal.setText(String.format( "R$ %.2f", Double.parseDouble(transactionManager.getTotal(month)) ));
        		notifyDataSetChanged();
            }
        });

//        itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                transactionManager.delete(row.getId());
//                deleteRow(row);
//                txtTotal.setText(String.format( "R$ %.2f", Double.parseDouble(transactionManager.getTotal()) ));
//                notifyDataSetChanged();
//                return false;
//            }
//        });

        
        
        return itemView;

    }

    /**
     * Delete a row from the list of rows
     * @param row row to be deleted
     */
    public void deleteRow(Transaction row) {
        
        if(this.rows.contains(row)) {
            this.rows.remove(row);
        }
    }
}
