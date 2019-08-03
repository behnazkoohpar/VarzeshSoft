package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.ERAM;
import com.koohpar.eram.tools.CommonMethods;
//import retrofit.RetrofitError;

public class EnterMobileActivity extends AppCompatActivity implements Response.Listener, IApiUrls,
        Response.ErrorListener, IAPIConstantants {

    private Button btnOk;
    private Typeface typeface;
    private TextView mobileNumber, txtTitr, txtDesc;
    private ProgressDialog prgDialog;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        initView();
    }

    private void initView() {

        mobileNumber = (TextView) findViewById(R.id.mobileNumber);

        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText(R.string.txt_forget_password);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setTypeface(typeface);
        back = (ImageView) findViewById(R.id.back);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecieveSmsCodeActivity.class));
//                    callSignOutWS(v, mobileNumber.getText().toString());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void callSignOutWS(final View v, final String phone) {
        if (phone.length() != 11) {
            CommonMethods.showSingleButtonAlert(EnterMobileActivity.this, getString(R.string.txt_attention), getString(R.string.bad_phone_number), getString(R.string.pop_up_ok));
            return;
        } else {
            String s = phone.substring(0, 2);
            if (!s.equalsIgnoreCase("09")) {
                CommonMethods.showSingleButtonAlert(EnterMobileActivity.this, getString(R.string.txt_attention), getString(R.string.bad_phone_09), getString(R.string.pop_up_ok));
                return;
            }

        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PHONE, phone);
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("EnterMobileActivity", response.toString());
                    prgDialog.hide();
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            prgDialog.hide();
                            ERAM.getOurInstance().setPhone(phone);
                            startActivity(new Intent(getApplicationContext(), RecieveSmsCodeActivity.class));
                        } else {
                            CommonMethods.showSingleButtonAlert(getApplicationContext(), getString(R.string.txt_error), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_SIGN_OUT, params, listener, errorListener);
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

    public void callForgetPasswordWS(final View v, final String phone, final String nationalCode) {

        if (phone.length() != 11) {
            CommonMethods.showSingleButtonAlert(EnterMobileActivity.this, getString(R.string.txt_attention), getString(R.string.bad_phone_number), getString(R.string.pop_up_ok));
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PHONE, phone);
            params.put(REQUEST_NATIONAL_CODE, nationalCode);
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("LoginActivity", response.toString());
                    prgDialog.hide();
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            prgDialog.hide();
                            ERAM.getOurInstance().setPhone(phone);
                            startActivity(new Intent(getApplicationContext(), RecieveSmsCodeActivity.class));
                        } else {
                            CommonMethods.showSingleButtonAlert(getApplicationContext(), getString(R.string.txt_error), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_FORGET_PASSWORD, params, listener, errorListener);
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

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(Object o) {

    }

}
