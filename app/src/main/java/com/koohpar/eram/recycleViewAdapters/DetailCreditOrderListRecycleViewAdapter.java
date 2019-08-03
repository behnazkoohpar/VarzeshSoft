package com.koohpar.eram.recycleViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.DetailCreditOrder;
import com.koohpar.eram.tools.NumberFormatter;

import java.util.List;

/**
 * Created by cmos on 11/28/2017.
 */

public class DetailCreditOrderListRecycleViewAdapter extends RecyclerView.Adapter<DetailCreditOrderListRecycleViewAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<DetailCreditOrder> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public DetailCreditOrderListRecycleViewAdapter(List<DetailCreditOrder> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView CreationTime, LockerNumber, EnterTime, ExitTime, UsedCreditChargeTotalAmount, CreditTotalAmount, OrganizationName;
        private ImageView info;

        public ViewHolder(final View itemView) {
            super(itemView);
            CreationTime = (TextView) itemView.findViewById(R.id.CreationTime);
            LockerNumber = (TextView) itemView.findViewById(R.id.LockerNumber);
            EnterTime = (TextView) itemView.findViewById(R.id.EnterTime);
            ExitTime = (TextView) itemView.findViewById(R.id.ExitTime);
            UsedCreditChargeTotalAmount = (TextView) itemView.findViewById(R.id.UsedCreditChargeTotalAmount);
            CreditTotalAmount = (TextView) itemView.findViewById(R.id.CreditTotalAmount);
            OrganizationName = (TextView) itemView.findViewById(R.id.organizationName);
        }
    }


    public DetailCreditOrderListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_credit_order_list, (ViewGroup) null);
        DetailCreditOrderListRecycleViewAdapter.ViewHolder viewHolder = new DetailCreditOrderListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DetailCreditOrderListRecycleViewAdapter.ViewHolder viewHolder, final int position) {

//        viewHolder.useDate.setText(this.stList.get(position).getChargeAmountNote());
        viewHolder.CreationTime.setText(this.stList.get(position).getCreationTime());
        viewHolder.CreditTotalAmount.setText(NumberFormatter.separator(this.stList.get(position).getCreditTotalAmount())+" ریال");
//        viewHolder.useNumber.setText(this.stList.get(position).getDescription());
//        viewHolder.useNumber.setText(this.stList.get(position).getElapsedMinutes());
        viewHolder.EnterTime.setText(this.stList.get(position).getEnterTime().substring(0,8));
        viewHolder.ExitTime.setText(this.stList.get(position).getExitTime().substring(0,8));
//        viewHolder.useNumber.setText(this.stList.get(position).getInputAmount());
        viewHolder.LockerNumber.setText(this.stList.get(position).getLockerNumber());
//        viewHolder.useNumber.setText(this.stList.get(position).getSalesInvoiceAmount());
//        viewHolder.useNumber.setText(this.stList.get(position).getSubServiceAmount());
//        viewHolder.useNumber.setText(this.stList.get(position).getTimeAmount());
        viewHolder.UsedCreditChargeTotalAmount.setText(NumberFormatter.separator(this.stList.get(position).getUsedCreditChargeTotalAmount())+" ریال");
        viewHolder.OrganizationName.setText(this.stList.get(position).getOrganizationName());
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
