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
import com.koohpar.eram.models.DetailFactor;
import com.koohpar.eram.tools.DateUtil;
import com.koohpar.eram.tools.NumberFormatter;

import java.util.List;

/**
 * Created by cmos on 02/02/2018.
 */

public class DetailFactorListRecycleViewAdapter extends RecyclerView.Adapter<DetailFactorListRecycleViewAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<DetailFactor> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public DetailFactorListRecycleViewAdapter(List<DetailFactor> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView useDate, number, total, unit, text,usetime;

        public ViewHolder(final View itemView) {
            super(itemView);
            useDate = (TextView) itemView.findViewById(R.id.useDate);
            usetime = (TextView) itemView.findViewById(R.id.useTime);
            number = (TextView) itemView.findViewById(R.id.number);
            total = (TextView) itemView.findViewById(R.id.total);
            unit = (TextView) itemView.findViewById(R.id.unit);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }


    public DetailFactorListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_factor_list, (ViewGroup) null);
        DetailFactorListRecycleViewAdapter.ViewHolder viewHolder = new DetailFactorListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DetailFactorListRecycleViewAdapter.ViewHolder viewHolder, final int position) {

        int year = Integer.parseInt(stList.get(position).getCreationTime().substring(0, 4));
        int month = Integer.parseInt(stList.get(position).getCreationTime().substring(5, 7));
        int day = Integer.parseInt(stList.get(position).getCreationTime().substring(8, 10));
        String time = stList.get(position).getCreationTime().substring(11, 16);
//        JalaliCalendar.YearMonthDate yearMonthDate = new JalaliCalendar.YearMonthDate(year, month, day);
//        viewHolder.dateRequest.setText(JalaliCalendar.gregorianToJalali(yearMonthDate).toString());
        viewHolder.useDate.setText(DateUtil.changeMiladiToFarsi(year + "/" + month + "/" + day));
        viewHolder.usetime.setText(time);
//        viewHolder.useDate.setText(this.stList.get(position).getCreationTime());
        viewHolder.number.setText(this.stList.get(position).getValue());
        viewHolder.text.setText(this.stList.get(position).getStuff_Text());
        viewHolder.unit.setText(NumberFormatter.separator(this.stList.get(position).getUnitPrice()));
        viewHolder.total.setText(NumberFormatter.separator(this.stList.get(position).getTotalPrice()));
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
