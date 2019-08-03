package com.koohpar.eram.recycleViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.koohpar.eram.R;
import com.koohpar.eram.activities.ListDetailFactorActivity;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.Factor;
import com.koohpar.eram.tools.NumberFormatter;

import java.util.List;

/**
 * Created by cmos on 11/30/2017.
 */

public class FactorListRecycleViewAdapter extends RecyclerView.Adapter<FactorListRecycleViewAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<Factor> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public FactorListRecycleViewAdapter(List<Factor> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ReceptionUnit_Text, LockerNumber, TotalPrice, InvoiceDate, FactorNo;

        public ViewHolder(final View itemView) {
            super(itemView);
            ReceptionUnit_Text = (TextView) itemView.findViewById(R.id.ReceptionUnit_Text);
            LockerNumber = (TextView) itemView.findViewById(R.id.LockerNumber);
            TotalPrice = (TextView) itemView.findViewById(R.id.TotalPrice);
            InvoiceDate = (TextView) itemView.findViewById(R.id.InvoiceDate);
            FactorNo = (TextView) itemView.findViewById(R.id.FactorNo);
        }
    }


    public FactorListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_factor_list, (ViewGroup) null);
        FactorListRecycleViewAdapter.ViewHolder viewHolder = new FactorListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FactorListRecycleViewAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.ReceptionUnit_Text.setText(this.stList.get(position).getReceptionUnit_Text());
        viewHolder.LockerNumber.setText(this.stList.get(position).getLockerNumber());
        viewHolder.TotalPrice.setText(NumberFormatter.separator(this.stList.get(position).getTotalPrice()));
        viewHolder.InvoiceDate.setText(this.stList.get(position).getInvoiceDate());
        viewHolder.FactorNo.setText(this.stList.get(position).getFactorNo());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListDetailFactorActivity.factor_id = stList.get(position).getFactorID();
                context.startActivity(new Intent(context, ListDetailFactorActivity.class));
            }
        });
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
