package com.koohpar.eram.recycleViewAdapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.koohpar.eram.R;
import com.koohpar.eram.activities.ListMessageActivity;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.Messages;
import com.koohpar.eram.tools.AppConstants;
import com.koohpar.eram.tools.CommonMethods;
import com.koohpar.eram.tools.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by cmos on 16/01/2018.
 */

public class MessageListRecycleViewAdapter extends RecyclerView.Adapter<MessageListRecycleViewAdapter.ViewHolder> implements IAPIConstantants, IApiUrls {
    private List<Messages> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public MessageListRecycleViewAdapter(List<Messages> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView card_view;
        private TextView title, date, time;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
        }
    }


    public MessageListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, (ViewGroup) null);
        MessageListRecycleViewAdapter.ViewHolder viewHolder = new MessageListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MessageListRecycleViewAdapter.ViewHolder viewHolder, final int position) {

        if (stList.get(position).getSeened().equalsIgnoreCase("false"))
            viewHolder.card_view.setCardBackgroundColor(Color.rgb(255, 255, 255));
        viewHolder.title.setText(this.stList.get(position).getTitle());
        int year = Integer.parseInt(stList.get(position).getSendDate().substring(0, 4));
        int month = Integer.parseInt(stList.get(position).getSendDate().substring(5, 7));
        int day = Integer.parseInt(stList.get(position).getSendDate().substring(8, 10));
//        JalaliCalendar.YearMonthDate yearMonthDate = new JalaliCalendar.YearMonthDate(year, month, day);
//        viewHolder.dateRequest.setText(JalaliCalendar.gregorianToJalali(yearMonthDate).toString());
        viewHolder.date.setText(DateUtil.changeMiladiToFarsi(year + "/" + month + "/" + day));
        viewHolder.time.setText(this.stList.get(position).getSendTime());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.showSingleButtonAlert(context, stList.get(position).getTitle(), stList.get(position).getMessageBody(), context.getString(R.string.txt_ok), new CommonMethods.IL() {
                    @Override
                    public void onSuccess() {
                        if (stList.get(position).getSeened().equalsIgnoreCase("false"))
                            callSeenMessage(stList.get(position).getID());
                    }

                    @Override
                    public void onCancel() {
                        if (stList.get(position).getSeened().equalsIgnoreCase("false"))
                            callSeenMessage(stList.get(position).getID());
                    }
                });
            }
        });
    }

    private void callSeenMessage(String id) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_MESSAGE_ID, id);

            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("seen message", response.toString());
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            context.startActivity(new Intent(context, ListMessageActivity.class));
                        } else {
                            CommonMethods.showSingleButtonAlert(context, context.getString(R.string.txt_attention), jsonObject.getString("errmessage"), context.getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
                        prgDialog.hide();
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prgDialog.hide();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( AppConstants.SERVER_IP +URL_SEEN_MESSAGE, params, listener, errorListener);
            int socketTimeout = 5000; // 5 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            prgDialog.hide();
            e.printStackTrace();
        }

    }

    public int getItemCount() {
        if (this.stList != null)
            return this.stList.size();
        return 0;
    }

}
