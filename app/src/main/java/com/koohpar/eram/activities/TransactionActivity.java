package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONException;
import org.json.JSONObject;

public class TransactionActivity extends AppCompatActivity implements IApiUrls, IAPIConstantants, View.OnClickListener {

    private Typeface typeface;
    private ProgressDialog prgDialog;
    private TextView txtTitr;
    private String UnLimitedAmountsSum, UsedUnLimitedAmountsSum, UnLimitedWalletStock, UsedLimitedAmountsSum, LimitedAmountsSum, LimitedWalletStock;
    private TextView price, rate;
    private RelativeLayout layalti, factorl, transactionl;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        setContentView(R.layout.activity_transaction);

        txtTitr = (TextView) findViewById(R.id.textView4);
        price = (TextView) findViewById(R.id.price);
        rate = (TextView) findViewById(R.id.rate);
        back = (ImageView) findViewById(R.id.back);
        layalti = (RelativeLayout) findViewById(R.id.layalti);
        factorl = (RelativeLayout) findViewById(R.id.factorl);
        transactionl = (RelativeLayout) findViewById(R.id.transactionl);
        txtTitr.setText(R.string.txt_request);
        layalti.setOnClickListener(this);
        factorl.setOnClickListener(this);
        transactionl.setOnClickListener(this);
        back.setOnClickListener(this);
        callGetCreditAmountInfo();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layalti:
//                startActivity(new Intent(TransactionActivity.this,CashServiceActivity.class));
                startActivity(new Intent(TransactionActivity.this,ChartActivity.class));
                break;

            case R.id.factorl:
                startActivity(new Intent(TransactionActivity.this,ListFactorActivity.class));
                break;

            case R.id.transactionl:
                startActivity(new Intent(TransactionActivity.this, ListTransactionActivity.class));
                break;

            case R.id.back:
                finish();
                break;
        }

    }

    private void callGetCreditAmountInfo() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(TransactionActivity.this, "ERAM", "PersonID", String.class));
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("callGetCreditAmountInfo", response.toString());
                    prgDialog.hide();
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        UnLimitedAmountsSum = jsonObject.getString("UnLimitedAmountsSum");
//                        UsedUnLimitedAmountsSum = jsonObject.getString("UsedUnLimitedAmountsSum");
//                        UnLimitedWalletStock = jsonObject.getString("UnLimitedWalletStock");
                        LimitedAmountsSum = jsonObject.getString("LimitedAmountsSum");
//                        UsedLimitedAmountsSum = jsonObject.getString("UsedLimitedAmountsSum");
//                        LimitedWalletStock = jsonObject.getString("LimitedWalletStock");
                        price.setText(UnLimitedAmountsSum);
                        rate.setText(LimitedAmountsSum);

                    } catch (Exception e) {
                        prgDialog.hide();
                        CommonMethods.showSingleButtonAlert(getApplicationContext(), getString(R.string.txt_error), getString(R.string.txt_error_in_server), getString(R.string.pop_up_ok));
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prgDialog.hide();
                    CommonMethods.showSingleButtonAlert(getApplicationContext(), getString(R.string.login_failed_server_error), getString(R.string.txt_error_in_server), getString(R.string.pop_up_ok));
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_CREDIT_AMOUNT_INFO, params, listener, errorListener);
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
}
