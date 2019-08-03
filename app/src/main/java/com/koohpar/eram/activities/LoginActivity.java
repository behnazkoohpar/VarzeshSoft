package com.koohpar.eram.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.ERAM;
import com.koohpar.eram.models.ServerGym;
import com.koohpar.eram.tools.AppConstants;
import com.koohpar.eram.tools.CommonMethods;

public class LoginActivity extends AppCompatActivity implements Response.Listener, IApiUrls,
        Response.ErrorListener, IAPIConstantants {
    private Typeface typeface;
    private TextView userId, password;
    private Button btnLogin;
    private TextView forgetPass, sign_in;
    private ProgressDialog prgDialog;
    final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private boolean exitParam = true;
    private String[][] serverArray;
    private ListView listState;
    private ExpandableRelativeLayout expandableLayoutMessage;
    private RelativeLayout ostanLayout;
    private TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(false);
        initView();

        callGetServer();
    }

    private void initView() {
        insertDummyContactWrapper();
        SignInActivity.isFromForgetPass = false;
        userId = (TextView) findViewById(R.id.userId);
        expandableLayoutMessage = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutState);
        ostanLayout = (RelativeLayout) findViewById(R.id.ostanLayout);
        state = (TextView) findViewById(R.id.state);
        password = (TextView) findViewById(R.id.password);
        if (getSavedObjectFromPreference(getApplicationContext(), "ERAM", "TelNumber", String.class) != null)
            userId.setText(getSavedObjectFromPreference(getApplicationContext(), "ERAM", "TelNumber", String.class));
        if (getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PassWord", String.class) != null)
            password.setText(getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PassWord", String.class));
        forgetPass = (TextView) findViewById(R.id.forgetPass);
        sign_in = (TextView) findViewById(R.id.sign_in);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        listState = (ListView) findViewById(R.id.listState);
        btnLogin.setTypeface(typeface);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    callLoginWS();
                }
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInActivity.isFromForgetPass = true;
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
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
                LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "Address", serverArray[position][2]);
                LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "Email", serverArray[position][5]);
                LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "SupporterName", serverArray[position][11]);
                LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "SupporterPhone", serverArray[position][12]);
                LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "ServerAddress", serverArray[position][10]);
                LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "PhoneNumber", serverArray[position][9]);
                LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "Name", serverArray[position][0]);
                LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "ID", serverArray[position][1]);

                AppConstants.SERVER_IP =  LoginActivity.getSavedObjectFromPreference(LoginActivity.this,"ERAM","ServerAddress",String.class);
            }
        });
    }

    private void callGetServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = "http://185.187.51.207:8000/WS/get_company_info";

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
                                        LoginActivity.this,
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

    public void callLoginWS() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PHONE, userId.getText().toString());
            params.put(REQUEST_PASSWORD, password.getText().toString());
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("LoginActivity", response.toString());
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            if (!jsonObject.getString("CardNumber").isEmpty()) {
                                ERAM.getOurInstance().setCardNumber(jsonObject.getString("CardNumber"));
                            }
                            if (!jsonObject.getString("PersonName").isEmpty()) {
                                ERAM.getOurInstance().setPersonName(jsonObject.getString("PersonName"));
                            }
                            if (!jsonObject.getString("PersonID").isEmpty()) {
                                ERAM.getOurInstance().setPersonID(jsonObject.getString("PersonID"));
                            }
                            if (!jsonObject.getString("PhoneNumber").isEmpty()) {
                                ERAM.getOurInstance().setPhoneNumber(jsonObject.getString("PhoneNumber"));
                            }
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "PassWord", password.getText().toString());
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "PhoneNumber", jsonObject.getString("PhoneNumber"));
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "PersonName", jsonObject.getString("PersonName"));
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "PersonNameOnly", jsonObject.getString("PersonNameOnly"));
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "PersonID", jsonObject.getString("PersonID"));
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "CardNumber", jsonObject.getString("CardNumber"));
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "PersonImage", jsonObject.getString("PersonImage"));
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "Grade", jsonObject.getString("Grade"));
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "Organ", jsonObject.getString("Organ"));
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "PersianMembershipDate", jsonObject.getString("PersianMembershipDate"));

                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "NUMBER", "10");
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "SHARJ", "5000000");
                            LoginActivity.saveObjectToSharedPreference(LoginActivity.this, "ERAM", "DAY", "30");

                            sendRegistrationToServer(LoginActivity.getSavedObjectFromPreference(LoginActivity.this, "ERAM", "TOKEN", String.class));
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            CommonMethods.showSingleButtonAlert(LoginActivity.this, getString(R.string.txt_attention), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_LOGIN_VARZESH_SOFT, params, listener, errorListener);
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


    private boolean validate() {
        if(state.getText().toString().isEmpty()){
            CommonMethods.showSingleButtonAlert(LoginActivity.this, getString(R.string.txt_attention), getString(R.string.server_not_selected), getString(R.string.pop_up_ok));
            return false;
        }

        if (userId.getText().toString().isEmpty() || !SignInActivity.isValidPhoneNumber(userId.getText().toString())) {
            CommonMethods.showSingleButtonAlert(LoginActivity.this, getString(R.string.txt_attention), getString(R.string.bad_phone_number), getString(R.string.pop_up_ok));
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            CommonMethods.showSingleButtonAlert(LoginActivity.this, getString(R.string.txt_attention), getString(R.string.password_is_null), getString(R.string.pop_up_ok));
            return false;
        }
        return true;
    }

    private void sendRegistrationToServer(final String refreshedToken) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PersonID", String.class));
            params.put(REQUEST_PHONE, LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PhoneNumber", String.class));
            params.put(REQUEST_TOKEN, refreshedToken);
            params.put(REQUEST_DEVICE_TYPE, "1");
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.d("MyFirebaseInstanceIDService  don't send tokenId", refreshedToken);
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_SEND_TOKEN_WITH_DEVICE_TYPE, params, listener, errorListener);
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

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.INTERNET))
            permissionsNeeded.add("اینترنت");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("نوشتن روی حافظه");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("خواندن از حافظه");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("موقعیت مکانی");
        //READ_GSERVICES
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("موقعیت مکانی");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_WIFI_STATE))
            permissionsNeeded.add("WIFI");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE))
            permissionsNeeded.add("مکان شبکه");
        if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
            permissionsNeeded.add("پیام ها");
        if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
            permissionsNeeded.add("پیام ها");
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("پیام ها");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("دوربین");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("موقعیت تلفن");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
                return;
            }
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(Object o) {

    }

    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (exitParam) {
            exitParam = false;
            CommonMethods.showToast(LoginActivity.this, "برای بستن برنامه یکبار دیگر بازگشت را بزنید");
        } else {
            exitParam = true;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
