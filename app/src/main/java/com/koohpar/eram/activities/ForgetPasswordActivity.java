package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

public class ForgetPasswordActivity extends AppCompatActivity implements Response.Listener, IApiUrls,
        Response.ErrorListener, IAPIConstantants {

    private Typeface typeface;
    private TextView textTitr, txtDesc;
    private Button btnOk;
    private TextView txtNewPass, txtReNewPass;
    private ImageView back;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
    }

    private void initView() {
        txtNewPass = (TextView) findViewById(R.id.txtNewPass);
        txtReNewPass = (TextView) findViewById(R.id.txtReNewPass);
        textTitr = (TextView) findViewById(R.id.textView4);
        textTitr.setText(R.string.txt_change_password);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        btnOk = (Button) findViewById(R.id.btnOk);
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        btnOk.setTypeface(typeface);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                if (validate(v, txtNewPass.getText().toString(), txtReNewPass.getText().toString()))
//                    callChangePasswordWS();
            }
        });
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    private boolean validate(View v, String s, String s1) {
        if (s.isEmpty()) {
            CommonMethods.showSingleButtonAlert(ForgetPasswordActivity.this,getString(R.string.txt_attention), getString(R.string.bad_password), getString(R.string.pop_up_ok));
            return false;
        }
        if (s1.isEmpty()) {
            CommonMethods.showSingleButtonAlert(ForgetPasswordActivity.this,getString(R.string.txt_attention), getString(R.string.bad_repassword), getString(R.string.pop_up_ok));
            return false;
        }
        if (!s1.equalsIgnoreCase(s)) {
            CommonMethods.showSingleButtonAlert(ForgetPasswordActivity.this,getString(R.string.txt_attention), getString(R.string.bad_pass_repass), getString(R.string.pop_up_ok));
            return false;
        }
        return true;
    }

    public void callChangePasswordWS() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PHONE, ERAM.getOurInstance().getPhone());
            params.put(REQUEST_NEW_PASSWORD, txtNewPass.getText().toString());
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("ForgetPasswordActivity", response.toString());
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            CommonMethods.showSingleButtonAlert(ForgetPasswordActivity.this, getString(R.string.txt_attention),
                                    getString(R.string.txt_password_changed), getString(R.string.txt_login_app), new CommonMethods.IL() {
                                        @Override
                                        public void onSuccess() {
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        }

                                        @Override
                                        public void onCancel() {
                                        }
                                    });
                        } else {
                            CommonMethods.showSingleButtonAlert(ForgetPasswordActivity.this, getString(R.string.txt_attention), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_CHANGE_PASSWORD, params, listener, errorListener);
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

    @Override
    public void onBackPressed() {

    }
}
