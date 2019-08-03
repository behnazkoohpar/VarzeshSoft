package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.koohpar.eram.tools.AppConstants;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GuestActivity extends AppCompatActivity implements View.OnClickListener, IApiUrls, IAPIConstantants {

    private EditText day, user, telNumber;
    private Button btn;
    private TextView textView4;
    private ImageView back;
    private Typeface typefaces;
    private CheckBox check_guest;
    private ProgressDialog prgDialog;
    private TextView serviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        typefaces = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        initView();
    }

    private void initView() {
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText("معرفی میهمان");
        btn = (Button) findViewById(R.id.btn);
        btn.setTypeface(typefaces);
        back = (ImageView) findViewById(R.id.back);
        day = (EditText) findViewById(R.id.day);
        user = (EditText) findViewById(R.id.user);
        telNumber = (EditText) findViewById(R.id.telNumber);
        check_guest = (CheckBox) findViewById(R.id.check_guest);
        serviceName = (TextView) findViewById(R.id.serviceName);
        Bundle bundle = getIntent().getExtras();
        serviceName.setText(bundle.getString("service_title"));
        btn.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                if (validate())
                    callSetNewGuest();
                break;
            case R.id.back:
                finish();
                break;
        }

    }

    private void callSetNewGuest() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            Bundle bundle = getIntent().getExtras();

            params.put(REQUEST_MEMBERSHIP_FILE_ID, bundle.getString("membershipfile_id"));
            params.put(REQUEST_ID, bundle.getString("id"));
            params.put(REQUEST_SERVICE_TYPE, bundle.getString("ServiceType_Text"));
            params.put(REQUEST_GUEST_PHONE, telNumber.getText().toString());
            params.put(REQUEST_GUEST_COUNT, user.getText().toString());
            params.put(REQUEST_GUEST_EXPIRE, day.getText().toString());
            params.put(REQUEST_IS_CREDIT, check_guest.isChecked());
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        prgDialog.hide();
                        if (response.getString("status").equalsIgnoreCase("true")) {
                            CommonMethods.showSingleButtonAlert(GuestActivity.this, getString(R.string.txt_attention), "میهمان معرفی شده با موفقیت ثبت شد", getString(R.string.pop_up_ok), new CommonMethods.IL() {
                                @Override
                                public void onSuccess() {
                                   finish();
                                }

                                @Override
                                public void onCancel() {
                                    finish();
                                }
                            });

                        } else {
                            CommonMethods.showSingleButtonAlert(GuestActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prgDialog.hide();
                    CommonMethods.showSingleButtonAlert(GuestActivity.this, getString(R.string.txt_attention), getString(R.string.txt_error), getString(R.string.pop_up_ok));
//                    Log.d("MyFirebaseInstanceIDService  don't send tokenId", refreshedToken);
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_SET_NEW_GUEST, params, listener, errorListener);
            int socketTimeout = 5000; // 5 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            prgDialog.hide();
            CommonMethods.showSingleButtonAlert(GuestActivity.this, getString(R.string.txt_attention), getString(R.string.txt_error), getString(R.string.pop_up_ok));
            e.printStackTrace();
        }
    }

    private boolean validate() {
        if (telNumber.getText().toString().isEmpty() || !SignInActivity.isValidPhoneNumber(telNumber.getText().toString())) {
            CommonMethods.showSingleButtonAlert(GuestActivity.this, getString(R.string.txt_attention), getString(R.string.bad_phone_number), getString(R.string.pop_up_ok));
            return false;
        }
        if (day.getText().toString().isEmpty() || Integer.parseInt(day.getText().toString()) > 7) {
            CommonMethods.showSingleButtonAlert(GuestActivity.this, getString(R.string.txt_attention), getString(R.string.day_is_not_correct), getString(R.string.pop_up_ok));
            return false;
        }

        if (user.getText().toString().isEmpty() || Integer.parseInt(user.getText().toString()) > 10) {
            CommonMethods.showSingleButtonAlert(GuestActivity.this, getString(R.string.txt_attention), getString(R.string.user_is_not_correct), getString(R.string.pop_up_ok));
            return false;
        }
        return true;
    }
}
