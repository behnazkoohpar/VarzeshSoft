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
import com.koohpar.eram.activities.ListReserveActivity;
import com.koohpar.eram.activities.LoginActivity;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.Reserve;
import com.koohpar.eram.tools.CommonMethods;
import com.koohpar.eram.tools.DateUtil;
import com.koohpar.eram.tools.util.PersianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ReserveListRecycleViewAdapter extends RecyclerView.Adapter<ReserveListRecycleViewAdapter.ViewHolder>
        implements IAPIConstantants, Response.Listener, IApiUrls, Response.ErrorListener {
    private List<Reserve> stList;
    public static Context context;
    private ProgressDialog prgDialog;

    public ReserveListRecycleViewAdapter(List<Reserve> SlistS) {
        this.stList = SlistS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final Button btn;
        private TextView title, date, increase, decrease, number;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            increase = (TextView) itemView.findViewById(R.id.increase);
            decrease = (TextView) itemView.findViewById(R.id.decrease);
            number = (TextView) itemView.findViewById(R.id.number);
            date = (TextView) itemView.findViewById(R.id.date);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            btn = (Button) itemView.findViewById(R.id.btn);
        }
    }

    public ReserveListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_reserve, (ViewGroup) null);
        ReserveListRecycleViewAdapter.ViewHolder viewHolder = new ReserveListRecycleViewAdapter.ViewHolder(itemLayoutView);
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ReserveListRecycleViewAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.title.setText(this.stList.get(position).getTitle());
        final String[] currentDate = {DateUtil.getCurrentDate()};
        String day = DateUtil.PersianWeekName[DateUtil.Farsi_Week_Day(Integer.parseInt(currentDate[0].substring(0, 4)), Integer.parseInt(currentDate[0].substring(5, 7)), Integer.parseInt(currentDate[0].substring(8, 10)))];
        viewHolder.date.setText(day.concat(" ").concat(currentDate[0].substring(8, 10)).concat(" ").concat(DateUtil.PersianMonthName[Integer.parseInt(currentDate[0].substring(5, 7))]).concat(" ماه ").concat(currentDate[0].substring(0, 4)));
        viewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d = Integer.parseInt(viewHolder.number.getText().toString());
                if (d < 10 && d >= 0) {
                    d = d + 1;
                    viewHolder.number.setText(String.valueOf(d));
                    currentDate[0] = DateUtil.AddDate(currentDate[0], +1);
                    String day = DateUtil.PersianWeekName[DateUtil.Farsi_Week_Day(Integer.parseInt(currentDate[0].substring(0, 4)), Integer.parseInt(currentDate[0].substring(5, 7)), Integer.parseInt(currentDate[0].substring(8, 10)))];
                    viewHolder.date.setText(day.concat(" ").concat(currentDate[0].substring(8, 10)).concat(" ").concat(DateUtil.PersianMonthName[Integer.parseInt(currentDate[0].substring(5, 7))]).concat(" ماه ").concat(currentDate[0].substring(0, 4)));
                }
            }
        });

        viewHolder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d = Integer.parseInt(viewHolder.number.getText().toString());
                if (d <= 10 && d > 0) {
                    d = d - 1;
                    viewHolder.number.setText(String.valueOf(d));
                    currentDate[0] = DateUtil.AddDate(currentDate[0], -1);
                    String day = DateUtil.PersianWeekName[DateUtil.Farsi_Week_Day(Integer.parseInt(currentDate[0].substring(0, 4)), Integer.parseInt(currentDate[0].substring(5, 7)), Integer.parseInt(currentDate[0].substring(8, 10)))];
                    viewHolder.date.setText(day.concat(" ").concat(currentDate[0].substring(8, 10)).concat(" ").concat(DateUtil.PersianMonthName[Integer.parseInt(currentDate[0].substring(5, 7))]).concat(" ماه ").concat(currentDate[0].substring(0, 4)));
                }
            }
        });

        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.showTwoButtonAlert(context, context.getString(R.string.txt_attention), "آیا از درخواست خود اطمینان دارید؟", context.getString(R.string.pop_up_ok), context.getString(R.string.cancel), new CommonMethods.IL() {
                    @Override
                    public void onSuccess() {
                        callSetPackageWS(stList.get(position), DateUtil.changeFarsiToMiladi(currentDate[0]), currentDate[0]);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }

    private void callSetPackageWS(Reserve reserve, String miladi, String shamsi) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(context, "ERAM", "PersonID", String.class));
            params.put(REQUEST_RESERVABLE_SERVICE_ID, reserve.getID());
            params.put(REQUEST_RESERVE_DATE, arabicToDecimal(miladi));
            params.put(REQUEST_RESERVE_SHAMSI_DATE, arabicToDecimal(shamsi));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("Result").equalsIgnoreCase("true")) {
                        prgDialog.hide();
                        CommonMethods.showSingleButtonAlert(context, context.getString(R.string.txt_attention), "سرویس شما با موفقیت رزرو شد.\n جهت نهایی کردن سفارش، همکاران ما با شما تماس خواهند گرفت.\n با تشکر", context.getString(R.string.pop_up_ok));

                        Log.d("Reserve", response.toString());
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_SABT_RESERVE, params, listener, errorListener);
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

    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }
}