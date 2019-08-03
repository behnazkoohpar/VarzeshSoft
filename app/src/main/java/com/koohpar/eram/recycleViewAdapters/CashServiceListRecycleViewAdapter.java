package com.koohpar.eram.recycleViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.koohpar.eram.R;
import com.koohpar.eram.activities.CashServiceActivity;
import com.koohpar.eram.activities.LoginActivity;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.CashService;
import com.koohpar.eram.tools.CommonMethods;
import com.koohpar.eram.tools.NumberFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cmos on 03/03/2019.
 */

public class CashServiceListRecycleViewAdapter extends RecyclerView.Adapter<CashServiceListRecycleViewAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<CashService> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public CashServiceListRecycleViewAdapter(List<CashService> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final Button btn;
        private TextView title, maliat, price, percent, pricepercent, pay;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            maliat = (TextView) itemView.findViewById(R.id.maliat);
            price = (TextView) itemView.findViewById(R.id.price);
            percent = (TextView) itemView.findViewById(R.id.percent);
            pricepercent = (TextView) itemView.findViewById(R.id.pricepercent);
            pay = (TextView) itemView.findViewById(R.id.pay);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            btn = (Button) itemView.findViewById(R.id.btn);
        }
    }


    public CashServiceListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cash_service, (ViewGroup) null);
        CashServiceListRecycleViewAdapter.ViewHolder viewHolder = new CashServiceListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CashServiceListRecycleViewAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.title.setText(this.stList.get(position).getTitle());
        viewHolder.maliat.setText(NumberFormatter.separator(this.stList.get(position).getTaxAndTollAmount()));
        viewHolder.price.setText(NumberFormatter.separator(this.stList.get(position).getTotalAmount()));
        viewHolder.percent.setText(NumberFormatter.separator(this.stList.get(position).getMemberGradeDiscountPercent()));
        viewHolder.pricepercent.setText(NumberFormatter.separator(this.stList.get(position).getDiscountAmount()));
        viewHolder.pay.setText(NumberFormatter.separator(this.stList.get(position).getPayableTotalAmount()));

        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.showTwoButtonAlert(context, context.getString(R.string.txt_attention), "آیا از خرید خود اطمینان دارید؟", context.getString(R.string.pop_up_ok), context.getString(R.string.cancel), new CommonMethods.IL() {
                    @Override
                    public void onSuccess() {
                        callSetPackageWS(stList.get(position));
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }

    private void callSetPackageWS(CashService cashService) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(context, "ERAM", "PersonID", String.class));
            params.put(REQUEST_ORGANIZATION_UNIT, CashServiceActivity.organSelected.getID());
            params.put(REQUEST_SERVICE_ID, cashService.getID());
            params.put(REQUEST_SERVICE_TYPE, cashService.getServiceType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("Result").equalsIgnoreCase("0")) {
                        prgDialog.hide();
                        CommonMethods.showSingleButtonAlert(context, context.getString(R.string.txt_attention), response.getString("ResultMessage"), context.getString(R.string.pop_up_ok));

                        Log.d("CashServiceActivity", response.toString());
                    } else {
                        CommonMethods.showSingleButtonAlert(context, context.getString(R.string.txt_attention), response.getString("ResultMessage"), context.getString(R.string.pop_up_ok));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                    Log.d("MyFirebaseInstanceIDService  don't send tokenId", refreshedToken);
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_SET_PACKAGE_REGISTRATION, params, listener, errorListener);
        int socketTimeout = 5000; // 5 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
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
