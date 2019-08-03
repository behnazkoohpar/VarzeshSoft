package com.koohpar.eram.recycleViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.koohpar.eram.R;
import com.koohpar.eram.activities.BarcodeActivity;
import com.koohpar.eram.activities.DecraseFromChargeActivity;
import com.koohpar.eram.activities.GuestActivity;
import com.koohpar.eram.activities.ListDetailCreditOrderActivity;
import com.koohpar.eram.activities.ListDetailOrderActivity;
import com.koohpar.eram.activities.LoginActivity;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.Order;
import com.koohpar.eram.tools.DateUtil;
import com.koohpar.eram.tools.NumberFormatter;
import com.koohpar.eram.tools.util.PersianCalendar;

import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cmos on 11/27/2017.
 */

public class OrderListRecycleViewAdapter extends RecyclerView.Adapter<OrderListRecycleViewAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<Order> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public OrderListRecycleViewAdapter(List<Order> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private TextView serviceName, expireDate, serial, useNumber, remainNumber, shargeUsed, chargeRemain;
        private ImageView info, sale, guest, img_detail;

        public ViewHolder(final View itemView) {
            super(itemView);
            serviceName = (TextView) itemView.findViewById(R.id.serviceName);
            info = (ImageView) itemView.findViewById(R.id.info);
            sale = (ImageView) itemView.findViewById(R.id.sale);
            guest = (ImageView) itemView.findViewById(R.id.guest);
            img_detail = (ImageView) itemView.findViewById(R.id.img_detail);
            expireDate = (TextView) itemView.findViewById(R.id.expireDate);
            serial = (TextView) itemView.findViewById(R.id.serial);
            useNumber = (TextView) itemView.findViewById(R.id.useNumber);
            remainNumber = (TextView) itemView.findViewById(R.id.remainNumber);
            chargeRemain = (TextView) itemView.findViewById(R.id.chargeRemain);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }


    public OrderListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, (ViewGroup) null);
        OrderListRecycleViewAdapter.ViewHolder viewHolder = new OrderListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderListRecycleViewAdapter.ViewHolder viewHolder, final int position) {
        String day = LoginActivity.getSavedObjectFromPreference(context, "ERAM", "DAY", String.class);
        String number = LoginActivity.getSavedObjectFromPreference(context, "ERAM", "NUMBER", String.class);
        String sharj = LoginActivity.getSavedObjectFromPreference(context, "ERAM", "SHARJ", String.class);
        PersianCalendar pCalendar = new PersianCalendar();
        String ss[] = stList.get(position).getExpireDate().split("/");
        Date dateExpire = new Date(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]));
        Date dateCurrent = new Date(pCalendar.getPersianYear(), pCalendar.getPersianMonth(), pCalendar.getPersianDay());
        String diffrent = DateUtil.getDateDifferenceInDDMMYYYY(dateCurrent, dateExpire);
        String rr[] = diffrent.split("/");

        if (Integer.parseInt(stList.get(position).getRegisterationRemainedSessionsCount()) < Integer.parseInt(number) ||
                (Integer.parseInt(rr[2]) < Integer.parseInt(day)) ||
                Integer.parseInt(stList.get(position).getCreditChargeRemainedAmount()) < Integer.parseInt(sharj))
            viewHolder.cardView.setCardBackgroundColor(Color.rgb(252, 189, 189));
        else
            viewHolder.cardView.setCardBackgroundColor(Color.rgb(188, 254, 144));
        viewHolder.serviceName.setText(this.stList.get(position).getServiceTitle());
        viewHolder.serial.setText("سریال: " + this.stList.get(position).getRegistrationSerial());
        viewHolder.expireDate.setText(this.stList.get(position).getExpireDate());
        viewHolder.remainNumber.setText(this.stList.get(position).getRegisterationRemainedSessionsCount());
        viewHolder.useNumber.setText(this.stList.get(position).getRegisterationUsedSessionsCount());
        viewHolder.chargeRemain.setText(NumberFormatter.separator(this.stList.get(position).getCreditChargeRemainedAmount()));
        if (Integer.parseInt(stList.get(position).getRegisterationRemainedSessionsCount()) > 1 ||
                Integer.parseInt(stList.get(position).getCreditChargeRemainedAmount()) > 1) {
            viewHolder.guest.setVisibility(View.VISIBLE);
            viewHolder.sale.setVisibility(View.VISIBLE);
        } else {
            viewHolder.guest.setVisibility(View.GONE);
            viewHolder.sale.setVisibility(View.GONE);
        }


        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stList.get(position).getServiceType_Text().equalsIgnoreCase("خدمات اصلی")) {
                    ListDetailOrderActivity.method = 1;
                    ListDetailOrderActivity.service_id = stList.get(position).getServiceID();
                    ListDetailOrderActivity.membership_file_id = stList.get(position).getMembershipFileID();
                    ListDetailOrderActivity.serial_number = stList.get(position).getRegistrationSerial();
                    v.getContext().startActivity(new Intent(v.getContext(), ListDetailOrderActivity.class));
                }
                if (stList.get(position).getServiceType_Text().equalsIgnoreCase("خدمات فرعی")) {
                    ListDetailOrderActivity.method = 2;
                    ListDetailOrderActivity.service_id = stList.get(position).getServiceID();
                    ListDetailOrderActivity.membership_file_id = stList.get(position).getMembershipFileID();
                    ListDetailOrderActivity.serial_number = stList.get(position).getRegistrationSerial();
                    v.getContext().startActivity(new Intent(v.getContext(), ListDetailOrderActivity.class));
                }
                if (stList.get(position).getServiceType_Text().equalsIgnoreCase("شارژ اعتباری")) {
                    ListDetailCreditOrderActivity.membership_file_id = stList.get(position).getMembershipFileID();
                    ListDetailCreditOrderActivity.service_id = stList.get(position).getServiceID();
                    ListDetailCreditOrderActivity.serial_number = stList.get(position).getRegistrationSerial();
                    v.getContext().startActivity(new Intent(v.getContext(), ListDetailCreditOrderActivity.class));
                }

                return;
            }
        });

        viewHolder.sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("id", stList.get(position).getID());
                bundle.putString("membershipfile_id", stList.get(position).getMembershipFileID());
                bundle.putString("expire_date", stList.get(position).getExpireDate());
                bundle.putString("service_id", stList.get(position).getServiceID());
                bundle.putString("service_title", stList.get(position).getServiceTitle());
                bundle.putString("remain_charge", stList.get(position).getCreditChargeRemainedAmount());
                bundle.putString("serial", stList.get(position).getRegistrationSerial());
                context.startActivity(new Intent(context, BarcodeActivity.class).putExtras(bundle));
            }
        });

        viewHolder.guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("membershipfile_id", stList.get(position).getMembershipFileID());
                bundle.putString("service_id", stList.get(position).getServiceID());
                bundle.putString("id", stList.get(position).getID());
                bundle.putString("service_title", stList.get(position).getServiceTitle());
                bundle.putString("ServiceType_Text", stList.get(position).getServiceType_Text());
                bundle.putString("serial", stList.get(position).getRegistrationSerial());
                context.startActivity(new Intent(context, GuestActivity.class).putExtras(bundle));
            }
        });

        viewHolder.img_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("membershipfile_id", stList.get(position).getMembershipFileID());
                bundle.putString("service_id", stList.get(position).getServiceID());
                bundle.putString("id", stList.get(position).getID());
                bundle.putString("service_title", stList.get(position).getServiceTitle());
                bundle.putString("ServiceType_Text", stList.get(position).getServiceType_Text());
                bundle.putString("serial", stList.get(position).getRegistrationSerial());
                context.startActivity(new Intent(context, DecraseFromChargeActivity.class).putExtras(bundle));
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
