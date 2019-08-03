package com.koohpar.eram.recycleViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.Transaction;
import com.koohpar.eram.tools.DateUtil;
import com.koohpar.eram.tools.NumberFormatter;
import com.koohpar.eram.tools.util.PersianCalendar;

import java.util.Date;
import java.util.List;

/**
 * Created by cmos on 12/1/2017.
 */

public class TransactionListRecycleViewAdapter extends RecyclerView.Adapter<TransactionListRecycleViewAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<Transaction> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public TransactionListRecycleViewAdapter(List<Transaction> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView CreationTime, Amount, ReceiveType;

        public ViewHolder(final View itemView) {
            super(itemView);
            CreationTime = (TextView) itemView.findViewById(R.id.CreationTime);
            Amount = (TextView) itemView.findViewById(R.id.Amount);
            ReceiveType = (TextView) itemView.findViewById(R.id.ReceiveType);
        }
    }


    public TransactionListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_list, (ViewGroup) null);
        TransactionListRecycleViewAdapter.ViewHolder viewHolder = new TransactionListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TransactionListRecycleViewAdapter.ViewHolder viewHolder, final int position) {

        String ss[] = (this.stList.get(position).getCreationTime().substring(0,10)).split("-");

        viewHolder.CreationTime.setText(DateUtil.changeMiladiToFarsi(ss[0]+"/"+ss[1]+"/"+ss[2]));
        viewHolder.Amount.setText(NumberFormatter.separator(this.stList.get(position).getAmount()));
        if (stList.get(position).getReceiveType().equalsIgnoreCase("Naghd"))
            viewHolder.ReceiveType.setText("نقدی");
        if (stList.get(position).getReceiveType().equalsIgnoreCase("Pos"))
            viewHolder.ReceiveType.setText("دستگاه پوز");
        if (stList.get(position).getReceiveType().equalsIgnoreCase("Cheque"))
            viewHolder.ReceiveType.setText("چک");
        if (stList.get(position).getReceiveType().equalsIgnoreCase("KasrAzCharge"))
            viewHolder.ReceiveType.setText("کسر از شارژ");

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {

    }

    public int getItemCount() {
        if (this.stList != null)
            return this.stList.size();
        return 0;
    }

}
