package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.DetailCreditOrder;
import com.koohpar.eram.recycleViewAdapters.DetailCreditOrderListRecycleViewAdapter;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListDetailCreditOrderActivity extends AppCompatActivity implements View.OnClickListener, IAPIConstantants, IApiUrls {

    private ProgressDialog prgDialog;
    private TextView txtTitr;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView back;
    private List<DetailCreditOrder> detailCreateOrderList = new ArrayList<>();
    private Typeface typeface;
    public static String membership_file_id;
    public static String service_id;
    public static String serial_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_credit_order);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
        callOrderListWS();
    }

    private void initView() {
        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText("جزئیات سرویس اصلی");
        back = (ImageView) findViewById(R.id.back);
        recyclerView = (RecyclerView) findViewById(R.id.my_cost_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListner);

        back.setOnClickListener(this);
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListner = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            callOrderListWS();
        }
    };

    private void callOrderListWS() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_MEMBERSHIP_FILE_ID, membership_file_id);
            params.put(REQUEST_SERVICE_ID, service_id);
            params.put(REQUEST_SERIAL, serial_number);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("status").equalsIgnoreCase("true")) {
                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);

                            prgDialog.hide();
                            JSONArray obj = response.getJSONArray("Result");
                            Log.d("DetailCredit", response.toString());

                            detailCreateOrderList = null;
                            detailCreateOrderList = new ArrayList<>();
                            for (int i = 0; i < obj.length(); i++) {
                                DetailCreditOrder detailCreditOrder = new DetailCreditOrder(
                                        obj.getJSONObject(i).getString("CreationTime"),
                                        obj.getJSONObject(i).getString("LockerNumber"),
                                        obj.getJSONObject(i).getString("EnterTime"),
                                        obj.getJSONObject(i).getString("ExitTime"),
                                        obj.getJSONObject(i).getString("Description"),
                                        obj.getJSONObject(i).getString("ElapsedMinutes"),
                                        obj.getJSONObject(i).getString("InputAmount"),
                                        obj.getJSONObject(i).getString("TimeAmount"),
                                        obj.getJSONObject(i).getString("SalesInvoiceAmount"),
                                        obj.getJSONObject(i).getString("SubServiceAmount"),
                                        obj.getJSONObject(i).getString("UsedCreditChargeTotalAmount"),
                                        obj.getJSONObject(i).getString("CreditTotalAmount"),
                                        obj.getJSONObject(i).getString("ChargeAmountNote"),
                                        obj.getJSONObject(i).getString("OrganizationName")
                                );
                                detailCreateOrderList.add(detailCreditOrder);
                            }
                            recyclerView.setAdapter(new DetailCreditOrderListRecycleViewAdapter(detailCreateOrderList));
                        } else {
                            CommonMethods.showSingleButtonAlert(ListDetailCreditOrderActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
                            swipeRefreshLayout.setRefreshing(false);
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_CREDIT_SERVICE_DETAILS, params, listener, errorListener);
            int socketTimeout = 5000; // 5 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
