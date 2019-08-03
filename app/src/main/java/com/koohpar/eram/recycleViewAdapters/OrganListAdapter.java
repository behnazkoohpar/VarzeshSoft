package com.koohpar.eram.recycleViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.cardview.widget.CardView;
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
import com.koohpar.eram.models.OrganizationUnit;

import java.util.List;

/**
 * Created by cmos on 04/03/2019.
 */

public class OrganListAdapter extends RecyclerView.Adapter<OrganListAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<OrganizationUnit> stList;
    public static Context context;
    private ProgressDialog prgDialog;
    private OnItemClickListener listener;

    public OrganListAdapter(List<OrganizationUnit> SlistS) {
        this.stList = SlistS;
    }

    public interface OnItemClickListener {
        void onClick(int position,String title,String id);
    }

    public void setOnitemclickListener(OnItemClickListener onitemclickListener) {
        listener = onitemclickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private TextView title;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.organName);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }
    }


    public OrganListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organ, (ViewGroup) null);
        OrganListAdapter.ViewHolder viewHolder = new OrganListAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OrganListAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.title.setText(this.stList.get(position).getName());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        String value = viewHolder.title.getText().toString();
                        listener.onClick(position, value,stList.get(position).getID());
                    }
                }
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
