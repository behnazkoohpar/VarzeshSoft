package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.ERAM;
import com.koohpar.eram.models.ServerGym;
import com.koohpar.eram.tools.AppConstants;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener, IApiUrls, IAPIConstantants {

    public static boolean isFromForgetPass;
    private Button btnOk;
    private Typeface typeface;
    private ProgressDialog prgDialog;
    private EditText telNumber, cardNumber;
    private TextView textView4;
    private ImageView back;
    private ExpandableRelativeLayout expandableLayoutMessage;
    private RelativeLayout ostanLayout;
    private TextView state;
    private ListView listState;
    private String[][] serverArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(false);
        initView();
        callGetServer();

    }

    private void initView() {
        btnOk = (Button) findViewById(R.id.btnOk);
        telNumber = (EditText) findViewById(R.id.telNumber);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        textView4 = (TextView) findViewById(R.id.textView4);
        expandableLayoutMessage = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutState);
        ostanLayout = (RelativeLayout) findViewById(R.id.ostanLayout);
        state = (TextView) findViewById(R.id.state);
        listState = (ListView) findViewById(R.id.listState);
        if (isFromForgetPass) {
            textView4.setText(R.string.txt_forget_password);
        } else
            textView4.setText(R.string.sign_in);
        back = (ImageView) findViewById(R.id.back);
        btnOk.setTypeface(typeface);
        btnOk.setOnClickListener(this);
        back.setOnClickListener(this);

        ostanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup.LayoutParams params = expandableLayoutMessage.getLayoutParams();
                int height = (serverArray.length * 110);
                params.height = height;
                expandableLayoutMessage.setLayoutParams(params);
                expandableLayoutMessage.toggle();
            }
        });

        listState.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                expandableLayoutMessage.toggle();
                state.setText(serverArray[position][0]);
                LoginActivity.saveObjectToSharedPreference(SignInActivity.this, "ERAM", "Address", serverArray[position][2]);
                LoginActivity.saveObjectToSharedPreference(SignInActivity.this, "ERAM", "Email", serverArray[position][5]);
                LoginActivity.saveObjectToSharedPreference(SignInActivity.this, "ERAM", "SupporterName", serverArray[position][11]);
                LoginActivity.saveObjectToSharedPreference(SignInActivity.this, "ERAM", "SupporterPhone", serverArray[position][12]);
                LoginActivity.saveObjectToSharedPreference(SignInActivity.this, "ERAM", "ServerAddress", serverArray[position][10]);
                LoginActivity.saveObjectToSharedPreference(SignInActivity.this, "ERAM", "PhoneNumber", serverArray[position][9]);
                LoginActivity.saveObjectToSharedPreference(SignInActivity.this, "ERAM", "Name", serverArray[position][0]);
                LoginActivity.saveObjectToSharedPreference(SignInActivity.this, "ERAM", "ID", serverArray[position][1]);

                AppConstants.SERVER_IP =  LoginActivity.getSavedObjectFromPreference(SignInActivity.this,"ERAM","ServerAddress",String.class);
            }
        });
    }

    private void callGetServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = "http://94.182.183.196:8000/WS/get_company_info";

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // display response
                            List<ServerGym> serverGyms = new ArrayList<>();
                            Log.d("Response", response.toString());
                            JSONObject jsonObject = response;
                            JSONObject settings = jsonObject.getJSONObject("settings");
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (settings.getString("success").equalsIgnoreCase("1")) {
                                for (int i = 0; i < data.length(); i++) {
                                    ServerGym serverGym = new ServerGym();
                                    JSONObject jsonObject1 = (JSONObject) data.get(i);
                                    serverGym.setAddress(jsonObject1.getString("Address"));
                                    serverGym.setCreateDateTime(jsonObject1.getString("CreateDateTime"));
                                    serverGym.setCreateShamsiDate(jsonObject1.getString("CreateShamsiDate"));
                                    serverGym.setEmail(jsonObject1.getString("Email"));
                                    serverGym.setExpireShamsiDate(jsonObject1.getString("ExpireShamsiDate"));
                                    serverGym.setExpireDate(jsonObject1.getString("ExpireDate"));
                                    serverGym.setGenderType(jsonObject1.getString("GenderType"));
                                    serverGym.setID(jsonObject1.getString("ID"));
                                    serverGym.setName(jsonObject1.getString("Name"));
                                    serverGym.setPhoneNumber(jsonObject1.getString("PhoneNumber"));
                                    serverGym.setServerAddress(jsonObject1.getString("ServerAddress"));
                                    serverGym.setSupporterName(jsonObject1.getString("SupporterName"));
                                    serverGym.setSupporterPhone(jsonObject1.getString("SupporterPhone"));
                                    serverGyms.add(serverGym);
                                }

                                serverArray = new String[serverGyms.size()][13];

                                final ArrayAdapter<String> arrayAdapterState = new ArrayAdapter<String>(
                                        SignInActivity.this,
                                        R.layout.select_list_radio);
                                for (int i = 0; i < serverGyms.size(); i++) {
                                    serverArray[i][0] = serverGyms.get(i).getName();
                                    serverArray[i][1] = serverGyms.get(i).getID();
                                    serverArray[i][2] = serverGyms.get(i).getAddress();
                                    serverArray[i][3] = serverGyms.get(i).getCreateDateTime();
                                    serverArray[i][4] = serverGyms.get(i).getCreateShamsiDate();
                                    serverArray[i][5] = serverGyms.get(i).getEmail();
                                    serverArray[i][6] = serverGyms.get(i).getExpireDate();
                                    serverArray[i][7] = serverGyms.get(i).getExpireShamsiDate();
                                    serverArray[i][8] = serverGyms.get(i).getGenderType();
                                    serverArray[i][9] = serverGyms.get(i).getPhoneNumber();
                                    serverArray[i][10] = serverGyms.get(i).getServerAddress();
                                    serverArray[i][11] = serverGyms.get(i).getSupporterName();
                                    serverArray[i][12] = serverGyms.get(i).getSupporterPhone();
                                    arrayAdapterState.add(serverArray[i][0]);
                                }
                                listState.setAdapter(arrayAdapterState);
                                expandableLayoutMessage.toggle();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        requestQueue.add(getRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                callSignInWS();
                break;
            case R.id.back:
                startActivity(new Intent(SignInActivity.this, LoginActivity.class));
                isFromForgetPass =false;
                break;
        }
    }

    private void callSignInWS() {
        if (validate()) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject params = new JSONObject();
            try {
                params.put(REQUEST_PHONE, telNumber.getText().toString());
                params.put(REQUEST_CARD_NUMBER, cardNumber.getText().toString());
                prgDialog.show();
                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SignInActivity", response.toString());
                        try {
                            prgDialog.hide();
                            JSONObject jsonObject = response;
                            if (jsonObject.getBoolean("status")) {
                                ERAM.getOurInstance().setPhone(telNumber.getText().toString());
                                startActivity(new Intent(SignInActivity.this, RcieveSmsActivity.class));
                            } else {
                                CommonMethods.showSingleButtonAlert(SignInActivity.this, getString(R.string.txt_attention), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
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

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( AppConstants.SERVER_IP +URL_SIGN_OUT, params, listener, errorListener);
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



    private boolean validate() {
        if(state.getText().toString().isEmpty()){
            CommonMethods.showSingleButtonAlert(SignInActivity.this, getString(R.string.txt_attention), getString(R.string.server_not_selected), getString(R.string.pop_up_ok));
            return false;
        }
        if (telNumber.getText().toString().isEmpty() || !isValidPhoneNumber(telNumber.getText().toString())) {
            CommonMethods.showSingleButtonAlert(SignInActivity.this, getString(R.string.txt_attention), getString(R.string.bad_phone_number), getString(R.string.pop_up_ok));
            return false;
        }
        if (cardNumber.getText().toString().isEmpty()) {
            CommonMethods.showSingleButtonAlert(SignInActivity.this, getString(R.string.txt_attention), getString(R.string.card_number_is_null), getString(R.string.pop_up_ok));
            return false;
        }
        return true;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        } else {
            return phoneNumber.matches("^[+]?[0-9]{10,14}$");
            // ^[+]?[0-9]{10,13}$
        }
    }

    @Override
    public void onBackPressed() {
        isFromForgetPass =false;
        finish();
    }
}
