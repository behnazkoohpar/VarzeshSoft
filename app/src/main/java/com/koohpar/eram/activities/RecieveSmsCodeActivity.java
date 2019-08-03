package com.koohpar.eram.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
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

public class RecieveSmsCodeActivity extends AppCompatActivity implements Response.Listener, IApiUrls,
        Response.ErrorListener, IAPIConstantants {

    private Typeface typeface;
    private TextView txtTitr, txtNotRecieveCode, txtCallCenter;
    private Button btnOk;
    private TextView code;
    private Handler mHandler;
    private final int mInterval = 2000;
    private ProgressDialog prgDialog;
    private ImageView back;
    public static String codeSmsRecieved = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_sms_code);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
        mHandler = new Handler();
        startCheck();
    }

    private void initView() {
        code = (TextView) findViewById(R.id.code);
        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText(R.string.txt_activation);
        txtNotRecieveCode = (TextView) findViewById(R.id.txtNotRecieveCode);
        txtCallCenter = (TextView) findViewById(R.id.txtCallCenter);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setTypeface(typeface);
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
//                if (!code.getText().toString().isEmpty())
//                    callCheckActivationCode();
//                else
//                    CommonMethods.showSingleButtonAlert(RecieveSmsCodeActivity.this, getString(R.string.txt_attention), getString(R.string.enter_code), getString(R.string.pop_up_ok));
            }
        });

        txtNotRecieveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        txtCallCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void startCheck() {
        smsChecker.run();
    }

    private void stopCheck() {
        mHandler.removeCallbacks(smsChecker);
    }

    Runnable smsChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (!codeSmsRecieved.isEmpty()) {
                    stopCheck();
                    code.setText(codeSmsRecieved);
                    callCheckActivationCode();
                } else {
                    mHandler.postDelayed(smsChecker, mInterval);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void callCheckActivationCode() {
        stopCheck();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            codeSmsRecieved = "";
            params.put(REQUEST_PHONE, ERAM.getOurInstance().getPhone());
            params.put(REQUEST_CODE_RECIEVE, code.getText().toString());
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("RecieveSmsCodeActivity", response.toString());
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            if (jsonObject.getBoolean("existuser")) {
                                showDialogStatusAccountUser(jsonObject.getString("firstname").toString(), jsonObject.getString("lastname").toString());
                            }
//                            else
//                                startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                        } else {
                            CommonMethods.showSingleButtonAlert(RecieveSmsCodeActivity.this, getString(R.string.txt_attention), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_CHECK_ACTIVATION_CODE, params, listener, errorListener);
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

    private void showDialogStatusAccountUser(String name, String family) {

        final Dialog dialogStatus = new Dialog(RecieveSmsCodeActivity.this);
        dialogStatus.setContentView(R.layout.dialog_status_account_user);

        Button btnEnterAccount = (Button) dialogStatus.findViewById(R.id.btnEnterAccount);
        Button btnNewAccount = (Button) dialogStatus.findViewById(R.id.btnNewAccount);
        TextView onvan = (TextView) dialogStatus.findViewById(R.id.textView4);
        TextView nameAccount = (TextView) dialogStatus.findViewById(R.id.nameAccount);
        onvan.setText(R.string.txt_status_accout);
        nameAccount.setText(name + " " + family);
        back = (ImageView) findViewById(R.id.back);
        btnNewAccount.setTypeface(typeface);
        btnEnterAccount.setTypeface(typeface);
        dialogStatus.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnEnterAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogStatus.hide();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogStatus.dismiss();
                CommonMethods.showTwoButtonAlert(RecieveSmsCodeActivity.this, getString(R.string.txt_attention), getString(R.string.txt_dialog_deactive), getString(R.string.txt_ok), getString(R.string.txt_cancel), new CommonMethods.IL() {
                    @Override
                    public void onSuccess() {
//                        startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                    }

                    @Override
                    public void onCancel() {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });
            }
        });

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(Object o) {

    }

    @Override
    public void onBackPressed() {

    }
}
