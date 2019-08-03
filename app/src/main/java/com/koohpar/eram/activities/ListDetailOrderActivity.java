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
import com.koohpar.eram.models.DetailOrder;
import com.koohpar.eram.recycleViewAdapters.DetailOrderListRecycleViewAdapter;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListDetailOrderActivity extends AppCompatActivity implements View.OnClickListener, IAPIConstantants, IApiUrls {

    public static int method;
    private ProgressDialog prgDialog;
    private TextView txtTitr;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView back;
    private List<DetailOrder> detailOrderList = new ArrayList<>();
    private Typeface typeface;
    public static String membership_file_id;
    public static String service_id;
    public static String serial_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_order);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
        if (method == 1)
            callOrderListWS();
        if (method == 2)
            callOrderListWS2();
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
            if (method == 1)
                callOrderListWS();
            if (method == 2)
                callOrderListWS2();
        }
    };

    private void callOrderListWS() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_SERVICE_ID, service_id);
            params.put(REQUEST_MEMBERSHIP_FILE_ID, membership_file_id);
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
                            Log.d("DetailOrderListActivity", response.toString());

                            detailOrderList = null;
                            detailOrderList = new ArrayList<>();
                            for (int i = 0; i < obj.length(); i++) {
                                DetailOrder detailOrder = new DetailOrder(
                                        obj.getJSONObject(i).getString("ReceptionDate"),
                                        obj.getJSONObject(i).getString("UsedSessionsCount"),
                                        obj.getJSONObject(i).getString("RemainedSessionsCount"),
                                        obj.getJSONObject(i).getString("OrganizationName")
                                );
                                detailOrderList.add(detailOrder);
                            }
                            recyclerView.setAdapter(new DetailOrderListRecycleViewAdapter(detailOrderList));
                        } else {
                            CommonMethods.showSingleButtonAlert(ListDetailOrderActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_MAIN_SERVICE_DETAILS, params, listener, errorListener);
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

    private void callOrderListWS2() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_SERVICE_ID, service_id);
            params.put(REQUEST_MEMBERSHIP_FILE_ID, membership_file_id);
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
                            Log.d("DetailOrderListActivity", response.toString());

                            detailOrderList = null;
                            detailOrderList = new ArrayList<>();
                            for (int i = 0; i < obj.length(); i++) {
                                DetailOrder detailOrder = new DetailOrder(
                                        obj.getJSONObject(i).getString("ReceptionDate"),
                                        obj.getJSONObject(i).getString("UsedSessionsCount"),
                                        obj.getJSONObject(i).getString("RemainedSessionsCount"),
                                        obj.getJSONObject(i).getString("OrganizationName")
                                );
                                detailOrderList.add(detailOrder);
                            }
                            recyclerView.setAdapter(new DetailOrderListRecycleViewAdapter(detailOrderList));
                        } else {
                            CommonMethods.showSingleButtonAlert(ListDetailOrderActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_SUB_SERVICE_DETAILS, params, listener, errorListener);
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
