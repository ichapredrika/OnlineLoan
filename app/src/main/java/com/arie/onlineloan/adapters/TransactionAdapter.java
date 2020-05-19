package com.arie.onlineloan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arie.onlineloan.R;
import com.arie.onlineloan.TransactionDetailActivity;
import com.arie.onlineloan.models.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private ArrayList<Transaction> listTransaction;
    private Context context;
    private DecimalFormat df = new DecimalFormat("#,###.###");
    private String origin;

    public TransactionAdapter(Context context, ArrayList<Transaction> listTransaction, String origin) {
        this.context = context;
        this.listTransaction = listTransaction;
        this.origin = origin;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transaction, viewGroup, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        final Transaction transaction = listTransaction.get(position);

        String installment = context.getString(R.string.con_amount, df.format(Integer.parseInt(transaction.getInstallment())));
        String amount = context.getString(R.string.con_amount, df.format(Integer.parseInt(transaction.getLoanAmount())));
        holder.tvId.setText(transaction.getId());
        holder.tvStatus.setText(transaction.getStatus());
        holder.tvType.setText(transaction.getType());
        holder.tvInstallment.setText(installment);
        holder.tvAmount.setText(amount);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inTransDetail = new Intent(context, TransactionDetailActivity.class);
                inTransDetail.putExtra("transId", transaction.getId());
                inTransDetail.putExtra("origin", origin);
                inTransDetail.putExtra("transType", transaction.getType());
                context.startActivity(inTransDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTransaction.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        TextView tvType;
        TextView tvStatus;
        TextView tvAmount;
        TextView tvInstallment;

        TransactionViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.txt_trans_id);
            tvType = itemView.findViewById(R.id.txt_trans_type);
            tvStatus = itemView.findViewById(R.id.txt_trans_status);
            tvAmount = itemView.findViewById(R.id.txt_loan_amount);
            tvInstallment = itemView.findViewById(R.id.txt_installment);
        }
    }
}
