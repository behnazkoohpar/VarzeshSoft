package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.koohpar.eram.models.ERAM;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONException;
import org.json.JSONObject;

import static com.koohpar.eram.api.IAPIConstantants.REQUEST_NEW_PASSWORD;
import static com.koohpar.eram.api.IAPIConstantants.REQUEST_PASSWORD;
import static com.koohpar.eram.api.IAPIConstantants.REQUEST_PERSON_ID;
import static com.koohpar.eram.api.IApiUrls.URL_SET_PASSWORD;

public class PersonInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView telManager, first_name, last_name, gender, textView4;
    private EditText password, confirmPass;
    private Button btnOk;
    private ImageView back;
    private Typeface typeface;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();

    }

    private void initView() {
        telManager = (TextView) findViewById(R.id.telManager);
        telManager.setText(ERAM.getOurInstance().getPhoneNumber());
        first_name = (TextView) findViewById(R.id.first_name);
        first_name.setText(ERAM.getOurInstance().getFirstName());
        last_name = (TextView) findViewById(R.id.last_name);
        last_name.setText(ERAM.getOurInstance().getLastName());
        gender = (TextView) findViewById(R.id.gender);
        gender.setText(ERAM.getOurInstance().getGender());
        password = (EditText) findViewById(R.id.password);
        confirmPass = (EditText) findViewById(R.id.confirmPass);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setTypeface(typeface);
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText("کلمه عبور");

        btnOk.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                if (validate()) {
                    callSetPassword();
                }
                break;
            case R.id.back:

                break;
        }
    }

    private boolean validate() {
        if (password.getText().toString().isEmpty()) {
            CommonMethods.showSingleButtonAlert(PersonInfoActivity.this, getString(R.string.txt_attention), getString(R.string.bad_password), getString(R.string.pop_up_ok));
            return false;
        }
        if (confirmPass.getText().toString().isEmpty()) {
            CommonMethods.showSingleButtonAlert(PersonInfoActivity.this, getString(R.string.txt_attention), getString(R.string.bad_repassword), getString(R.string.pop_up_ok));
            return false;
        }
        if (!password.getText().toString().equalsIgnoreCase(confirmPass.getText().toString())) {
            CommonMethods.showSingleButtonAlert(PersonInfoActivity.this, getString(R.string.txt_attention), getString(R.string.bad_pass_repass), getString(R.string.pop_up_ok));
            return false;
        }
        return true;
    }

    private void callSetPassword() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, ERAM.getOurInstance().getPersonID());
            params.put(REQUEST_NEW_PASSWORD, password.getText().toString());
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("PersonInfoActivity", response.toString());
                    prgDialog.hide();
                    try {
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {

                            LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "PassWord", password.getText().toString());
                            LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "PhoneNumber", telManager.getText().toString());
                            CommonMethods.showSingleButtonAlert(PersonInfoActivity.this, getString(R.string.txt_attention), getString(R.string.lets_go), getString(R.string.pop_up_ok), new CommonMethods.IL() {
                                @Override
                                public void onSuccess() {
                                    startActivity(new Intent(PersonInfoActivity.this, LoginActivity.class));
                                }

                                @Override
                                public void onCancel() {

                                    startActivity(new Intent(PersonInfoActivity.this, LoginActivity.class));
                                }
                            });

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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_SET_PASSWORD, params, listener, errorListener);
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
    public void onBackPressed(){

    }
}
