package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.koohpar.eram.models.ERAM;
import com.koohpar.eram.tools.AppConstants;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONException;
import org.json.JSONObject;

public class RateActivity extends AppCompatActivity implements View.OnClickListener, IAPIConstantants, IApiUrls {

    public static String newString;
    private Typeface typeface;
    private ProgressDialog prgDialog;
    private ImageView back;
    private TextView textView4,thanks;
    private EditText desc;
    private RatingBar personelRate, rate;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.noti);//Here is FILE_NAME is the name of file that you want to play
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                newString= null;
//            } else {
//                newString= extras.getString(AppConstants.KEY_NOTIFICATION_DATE);
//            }
//        } else {
//            newString= (String) savedInstanceState.getSerializable(AppConstants.KEY_NOTIFICATION_DATE);
//        }
        iniView();
    }

    private void iniView() {
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        textView4 = (TextView) findViewById(R.id.textView4);
        thanks = (TextView) findViewById(R.id.thanks);
        thanks.setText(String.format("با تشکر از حضور و استفاده از مجموعه ورزشی %s در تاریخ %s لطفا نظر خود را از سرویس ارائه شده بیان فرمائید.", LoginActivity.getSavedObjectFromPreference(RateActivity.this, "ERAM", "Name", String.class), newString));
        personelRate = (RatingBar) findViewById(R.id.personelRate);
        rate = (RatingBar) findViewById(R.id.rate);
        desc = (EditText) findViewById(R.id.desc);
        btn = (Button) findViewById(R.id.btn);
        btn.setTypeface(typeface);
        textView4.setText("نظر سنجی");
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                if(validate())
                callSetRatingWS();
                break;
        }
    }

    private boolean validate() {
        if (personelRate.getRating() == 0) {
            CommonMethods.showSingleButtonAlert(RateActivity.this, getString(R.string.txt_attention), getString(R.string.person_rate), getString(R.string.pop_up_ok));
            return false;
        }
        if (rate.getRating() == 0) {
            CommonMethods.showSingleButtonAlert(RateActivity.this, getString(R.string.txt_attention), getString(R.string.rate), getString(R.string.pop_up_ok));
            return false;
        }
        return true;
    }

    private void callSetRatingWS() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(RateActivity.this, "ERAM", "PersonID", String.class));
            params.put(REQUEST_RATE_1, personelRate.getRating());
            params.put(REQUEST_RATE_2, rate.getRating());
            params.put(REQUEST_RATE_3, "0");
            params.put(REQUEST_DESCRIPTION, desc.getText().toString());
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("RateActivity", response.toString());
                    prgDialog.hide();
                    try {
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {

                            CommonMethods.showSingleButtonAlert(RateActivity.this, getString(R.string.txt_attention), getString(R.string.rate_set), getString(R.string.pop_up_ok), new CommonMethods.IL() {
                                @Override
                                public void onSuccess() {
                                    startActivity(new Intent(RateActivity.this, MainActivity.class));
                                }

                                @Override
                                public void onCancel() {

                                    startActivity(new Intent(RateActivity.this, MainActivity.class));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( AppConstants.SERVER_IP +URL_SET_EVALUATE_RATE, params, listener, errorListener);
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
        CommonMethods.showTwoButtonAlert(RateActivity.this, getString(R.string.txt_attention), getString(R.string.exit_yes), getString(R.string.txt_ok), getString(R.string.no),new CommonMethods.IL() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(RateActivity.this, MainActivity.class));
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
