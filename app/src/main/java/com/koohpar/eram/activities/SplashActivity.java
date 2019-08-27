package com.koohpar.eram.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.koohpar.eram.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.koohpar.eram.BuildConfig;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.ERAM;
import com.koohpar.eram.tools.AppConstants;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity implements IAPIConstantants, IApiUrls {

    private Handler mHandler;
    private final int mInterval = 5000;
    private ProgressDialog dialogBar;
    private ProgressDialog prgDialog;
    private boolean stoped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(false);
        mHandler = new Handler();
        AppConstants.SERVER_IP = LoginActivity.getSavedObjectFromPreference(SplashActivity.this, "ERAM", "ServerAddress", String.class);
        startCheck();

    }

    private void startCheck() {
        networkStatusChecker.run();
    }

    private void stopCheck() {
        stoped = true;
        mHandler.removeCallbacks(networkStatusChecker);
    }


    Runnable networkStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                stopCheck();
                callGetlastVersion();
                if (!stoped)
                    mHandler.postDelayed(networkStatusChecker, mInterval);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void callGetlastVersion() {
        stopCheck();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        prgDialog.show();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("getLastVersion", response.toString());
                try {
                    stopCheck();
                    prgDialog.hide();
                    JSONObject jsonObject = response;
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    final int version = pInfo.versionCode;
                    if (!jsonObject.getString("AndroidVersion").isEmpty() && jsonObject.getInt("AndroidVersion") > version) {
                        invokeVersion(jsonObject.getString("AndroidFilePath"));
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PassWord", String.class) == null ||
                                        LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PhoneNumber", String.class) == null ||
                                        LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PassWord", String.class).equalsIgnoreCase("") ||
                                        LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PhoneNumber", String.class).equalsIgnoreCase("")) {
                                    Intent intent = new Intent(SplashActivity.this,
                                            LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    callLogin();
                                }
                            }
                        }, 7300);
                    }
                } catch (JSONException e) {
                    Toast.makeText(SplashActivity.this, R.string.lost_internet, Toast.LENGTH_LONG).show();
                    prgDialog.hide();
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(SplashActivity.this, R.string.lost_internet, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this, R.string.lost_internet, Toast.LENGTH_LONG).show();
                prgDialog.hide();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_LAST_VERSION, params, listener, errorListener);
        int socketTimeout = 5000; // 5 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);

    }

    private void callLogin() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PHONE, LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PhoneNumber", String.class));
            params.put(REQUEST_PASSWORD, LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PassWord", String.class));
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
//                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "PassWord", password.getText().toString());
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "PhoneNumber", jsonObject.getString("PhoneNumber"));
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "PersonName", jsonObject.getString("PersonName"));
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "PersonNameOnly", jsonObject.getString("PersonNameOnly"));
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "PersonID", jsonObject.getString("PersonID"));
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "CardNumber", jsonObject.getString("CardNumber"));
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "PersonImage", jsonObject.getString("PersonImage"));
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "Grade", jsonObject.getString("Grade"));
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "Organ", jsonObject.getString("Organ"));
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "PersianMembershipDate", jsonObject.getString("PersianMembershipDate"));

                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "NUMBER", "3");
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "SHARJ", "50000");
                            LoginActivity.saveObjectToSharedPreference(SplashActivity.this, "ERAM", "DAY", "10");

                            sendRegistrationToServer(LoginActivity.getSavedObjectFromPreference(SplashActivity.this, "ERAM", "TOKEN", String.class));
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            CommonMethods.showSingleButtonAlert(SplashActivity.this, getString(R.string.txt_attention), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok), new CommonMethods.IL() {
                                @Override
                                public void onSuccess() {
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                }

                                @Override
                                public void onCancel() {
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                }
                            });

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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(AppConstants.SERVER_IP +URL_LOGIN_VARZESH_SOFT, params, listener, errorListener);
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( AppConstants.SERVER_IP +URL_SEND_TOKEN_WITH_DEVICE_TYPE, params, listener, errorListener);
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

    private void invokeVersion(String url) {
        new UpdateApp().execute(url);
    }

    private class UpdateApp extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(0);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();

                String PATH = "/mnt/sdcard/download/";
                File file = new File(PATH);
                file.mkdir();

                String AppName = getString(R.string.app_name) + ".apk";
                File outputFile = new File(file, AppName);
                if (outputFile.exists())
                    outputFile.delete();
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                int lenght = c.getContentLength();
                byte[] buffer = new byte[1024];
                int len = 0;
                int total = 0;
                try {
                    while ((len = is.read(buffer)) != -1) {
                        total += len;
                        fos.write(buffer, 0, len);
                        publishProgress("" + (int) ((total * 100) / lenght));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fos.flush();
                fos.close();
                is.close();
                Uri contentUri;
//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    contentUri = FileProvider.getUriForFile(SplashActivity.this,getApplicationContext().getPackageName() + ".provider",file);
//                    Intent i = new Intent(Intent.ACTION_VIEW, contentUri);
//                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    startActivity(i);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile
//                            (new File(Environment.getExternalStorageDirectory() + "/download/" + AppName)), "application/vnd.android.package-archive");
//                    startActivity(intent);
//                }String AppName = getString(R.string.app_name) + ".apk";
                File toInstall = new File(PATH, getString(R.string.app_name) + ".apk");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile(SplashActivity.this, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    Uri apkUri = Uri.fromFile(toInstall);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... values) {
            dialogBar.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dismissDialog(0);
        }

    }

//    private class UpdateApp extends AsyncTask<String, String, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            stopCheck();
//            showDialog(0);
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            try {
//                URL url = new URL(params[0]);
//                HttpURLConnection c = (HttpURLConnection) url.openConnection();
//                c.setRequestMethod("GET");
//                c.connect();
//
//                String PATH = "/mnt/sdcard/download/";
//                File file = new File(PATH);
//                file.mkdir();
//
//                String AppName = getString(R.string.app_name) + ".apk";
//                File outputFile = new File(file, AppName);
//                if (outputFile.exists())
//                    outputFile.delete();
//                FileOutputStream fos = new FileOutputStream(outputFile);
//                InputStream is = c.getInputStream();
//                int lenght = c.getContentLength();
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                int total = 0;
//                try {
//                    while ((len = is.read(buffer)) != -1) {
//                        total += len;
//                        fos.write(buffer, 0, len);
//                        publishProgress("" + (int) ((total * 100) / lenght));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                fos.flush();
//                fos.close();
//                is.close();
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile
//                        (new File(Environment.getExternalStorageDirectory() + "/download/" + AppName)
//                        ), "application/vnd.android.package-archive");
//
//                startActivity(intent);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        protected void onProgressUpdate(String... values) {
//            dialogBar.setProgress(Integer.parseInt(values[0]));
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            dismissDialog(0);
//        }
//    }

    protected Dialog onCreateDialog(int id) {
        dialogBar = new ProgressDialog(SplashActivity.this);
        dialogBar.setMessage(getString(R.string.software_updating));
        dialogBar.setMax(100);
        dialogBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogBar.show();
        return dialogBar;
    }
}
