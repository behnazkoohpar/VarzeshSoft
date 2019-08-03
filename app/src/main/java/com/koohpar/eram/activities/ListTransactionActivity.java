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
import com.koohpar.eram.models.Transaction;
import com.koohpar.eram.recycleViewAdapters.TransactionListRecycleViewAdapter;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListTransactionActivity extends AppCompatActivity implements View.OnClickListener, IAPIConstantants, IApiUrls {

    private ProgressDialog prgDialog;
    private TextView txtTitr;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView back;
    private List<Transaction> transactionList = new ArrayList<>();
    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaction);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
        callOrderListWS();
    }

    private void initView() {
        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText("لیست تراکنش های مالی");
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
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(ListTransactionActivity.this, "ERAM", "PersonID", String.class));
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("status").equalsIgnoreCase("true")) {
                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);

                            prgDialog.hide();
                            JSONArray obj = response.getJSONArray("Result");
                            Log.d("ListTransactionActivity", response.toString());

                            transactionList = null;
                            transactionList = new ArrayList<>();
                            for (int i = 0; i < obj.length(); i++) {
                                Transaction transaction = new Transaction(
                                        obj.getJSONObject(i).getString("ID"),
                                        obj.getJSONObject(i).getString("CreationTime"),
                                        obj.getJSONObject(i).getString("MembershipFile"),
                                        obj.getJSONObject(i).getString("Amount"),
                                        obj.getJSONObject(i).getString("ReceiveType")
                                );
                                transactionList.add(transaction);
                            }
                            recyclerView.setAdapter(new TransactionListRecycleViewAdapter(transactionList));
                        } else {
                            CommonMethods.showSingleButtonAlert(ListTransactionActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_TRANSACTION, params, listener, errorListener);
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
