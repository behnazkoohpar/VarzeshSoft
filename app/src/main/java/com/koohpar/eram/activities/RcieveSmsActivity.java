package com.koohpar.eram.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.koohpar.eram.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.ERAM;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONException;
import org.json.JSONObject;


public class RcieveSmsActivity extends AppCompatActivity implements Response.Listener, IApiUrls,
        Response.ErrorListener, IAPIConstantants, View.OnClickListener {
    private Typeface typeface;
    private TextView txtTitr, timerText, txtNotRecieveCode, txtCallCenter;
    private Button btnOk;
    private TextView code;
    public static boolean fromForgetPassword;
    private Handler mHandler;
    private final int mInterval = 2000;
    private ProgressDialog prgDialog;
    private LinearLayout smsNotRecieve;
    private RelativeLayout smsTime;
    private ImageView back;
    public static String codeSmsRecieved = "";
    private CountDownTimer countDownTimer;
    private int numberCallSms = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcieve_sms);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
        mHandler = new Handler();
//        startCheck();
        setCountDownTimer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initView() {
        code = (TextView) findViewById(R.id.code);
        txtTitr = (TextView) findViewById(R.id.textView4);
        timerText = (TextView) findViewById(R.id.timerText);
        smsNotRecieve = (LinearLayout) findViewById(R.id.smsNotRecieve);
        smsTime = (RelativeLayout) findViewById(R.id.smsTime);
        txtTitr.setText(R.string.txt_activation);
        txtNotRecieveCode = (TextView) findViewById(R.id.txtNotRecieveCode);
        txtCallCenter = (TextView) findViewById(R.id.txtCallCenter);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setTypeface(typeface);
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);

        back.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        txtNotRecieveCode.setOnClickListener(this);
        txtCallCenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
//                if (fromForgetPassword) {
//                    callCheckActivationCode();
//                } else {
                    if (!code.getText().toString().isEmpty())
                        callCheckActivationCode();
                    else
                        CommonMethods.showSingleButtonAlert(RcieveSmsActivity.this, getString(R.string.txt_attention), getString(R.string.enter_code), getString(R.string.pop_up_ok));
//                }
                break;
            case R.id.txtNotRecieveCode:
                numberCallSms++;
                if (numberCallSms < 2) {
                    callSendSmsWS();
                    smsNotRecieve.setVisibility(View.GONE);
                    smsTime.setVisibility(View.VISIBLE);
                    setCountDownTimer();
                } else {
                    CommonMethods.showSingleButtonAlert(RcieveSmsActivity.this, getString(R.string.txt_attention), getString(R.string.number_request_more_than_three), getString(R.string.pop_up_ok));
                    smsNotRecieve.setVisibility(View.GONE);
                    smsTime.setVisibility(View.GONE);
                }
                break;
            case R.id.txtCallCenter:
                Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("tel:0212967"));
                startActivity(i);
                break;
            case R.id.back:
                fromForgetPassword = false;
                finish();
                break;
        }
    }

//    private void startCheck() {
//        smsChecker.run();
//    }
//
//    private void stopCheck() {
//        mHandler.removeCallbacks(smsChecker);
//    }

//    Runnable smsChecker = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                if (!codeSmsRecieved.isEmpty()) {
//                    stopCheck();
//                    code.setText(codeSmsRecieved);
//                    callCheckActivationCode();
//                } else {
//                    mHandler.postDelayed(smsChecker, mInterval);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };

    public void callSendSmsWS() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PHONE, ERAM.getOurInstance().getPhone());
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("RecieveSmsCodeActivity", response.toString());
                    prgDialog.hide();
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            prgDialog.hide();
                        } else {
                            CommonMethods.showSingleButtonAlert(getApplicationContext(), getString(R.string.txt_error), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
                            smsTime.setVisibility(View.GONE);
                            smsNotRecieve.setVisibility(View.VISIBLE);
                            countDownTimer.cancel();
                        }
                    } catch (JSONException e) {
                        prgDialog.hide();
                        CommonMethods.showSingleButtonAlert(getApplicationContext(), getString(R.string.txt_error), getString(R.string.txt_error_in_server), getString(R.string.pop_up_ok));
                        smsTime.setVisibility(View.GONE);
                        smsNotRecieve.setVisibility(View.VISIBLE);
                        countDownTimer.cancel();
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prgDialog.hide();
                    CommonMethods.showSingleButtonAlert(getApplicationContext(), getString(R.string.login_failed_server_error), getString(R.string.txt_error_in_server), getString(R.string.pop_up_ok));
                    smsTime.setVisibility(View.GONE);
                    smsNotRecieve.setVisibility(View.VISIBLE);
                    countDownTimer.cancel();
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

    public void callCheckActivationCode() {
//        stopCheck();
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
                            if (!jsonObject.getString("FirstName").isEmpty()) {
                                ERAM.getOurInstance().setFirstName(jsonObject.getString("FirstName"));
                            }
                            if (!jsonObject.getString("LastName").isEmpty()) {
                                ERAM.getOurInstance().setLastName(jsonObject.getString("LastName"));
                            }
                            if (!jsonObject.getString("Gender").isEmpty()) {
                                ERAM.getOurInstance().setGender(jsonObject.getString("Gender"));
                            }
                            if (!jsonObject.getString("PersonID").isEmpty()) {
                                ERAM.getOurInstance().setPersonID(jsonObject.getString("PersonID"));
                            }
                            if (!jsonObject.getString("PhoneNumber").isEmpty()) {
                                ERAM.getOurInstance().setPhoneNumber(jsonObject.getString("PhoneNumber"));
                            }

                            startActivity(new Intent(RcieveSmsActivity.this, PersonInfoActivity.class));

                        } else {
                            CommonMethods.showSingleButtonAlert(RcieveSmsActivity.this, getString(R.string.txt_attention), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
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

        final Dialog dialogStatus = new Dialog(RcieveSmsActivity.this);
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
                CommonMethods.showTwoButtonAlert(RcieveSmsActivity.this, getString(R.string.txt_attention), getString(R.string.txt_dialog_deactive), getString(R.string.txt_ok), getString(R.string.txt_cancel), new CommonMethods.IL() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    private void setCountDownTimer() {
        countDownTimer = new CountDownTimer((30000), 1000) {
            private int minutes, seconds;
            private String minutesStr, secondsStr;

            public void onTick(long millisUntilFinished) {
                seconds = (int) (millisUntilFinished / 1000 % 60);
                minutes = (int) (millisUntilFinished / 1000 / 60);
                minutesStr = String.valueOf(minutes);
                secondsStr = String.valueOf(seconds);

                if (seconds < 10) secondsStr = "0" + secondsStr;
                if (minutes < 10) minutesStr = "0" + minutesStr;
                timerText.setText(minutesStr + ":" + secondsStr);
            }

            public void onFinish() {
                smsNotRecieve.setVisibility(View.VISIBLE);
                smsTime.setVisibility(View.GONE);
            }

        }.start();
    }

    @Override
    public void onBackPressed() {
        fromForgetPassword = false;
    }

}
