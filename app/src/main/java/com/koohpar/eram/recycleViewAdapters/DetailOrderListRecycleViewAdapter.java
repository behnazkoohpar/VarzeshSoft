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
import com.koohpar.eram.models.DetailOrder;
import com.koohpar.eram.models.Order;
import com.koohpar.eram.tools.NumberFormatter;

import java.util.List;

/**
 * Created by cmos on 11/27/2017.
 */

public class DetailOrderListRecycleViewAdapter extends RecyclerView.Adapter<DetailOrderListRecycleViewAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<DetailOrder> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public DetailOrderListRecycleViewAdapter(List<DetailOrder> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView useDate, useNumber, remainNumber, organizationName;
        private ImageView info;

        public ViewHolder(final View itemView) {
            super(itemView);
            useDate = (TextView) itemView.findViewById(R.id.useDate);
            useNumber = (TextView) itemView.findViewById(R.id.useNumber);
            remainNumber = (TextView) itemView.findViewById(R.id.remainNumber);
            organizationName = (TextView) itemView.findViewById(R.id.organizationName);
        }
    }


    public DetailOrderListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_order_list, (ViewGroup) null);
        DetailOrderListRecycleViewAdapter.ViewHolder viewHolder = new DetailOrderListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DetailOrderListRecycleViewAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.useDate.setText(this.stList.get(position).getReceptionDate());
        viewHolder.remainNumber.setText(NumberFormatter.separator(this.stList.get(position).getRemainedSessionsCount()));
        viewHolder.useNumber.setText(NumberFormatter.separator(this.stList.get(position).getUsedSessionsCount()));
        viewHolder.organizationName.setText(this.stList.get(position).getOranizationName());
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
