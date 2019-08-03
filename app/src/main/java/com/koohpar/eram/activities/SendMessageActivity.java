package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendMessageActivity extends AppCompatActivity implements IAPIConstantants, IApiUrls {

    private ImageButton btn, btn2;
    private TextView message, text_me;
    private Button btnOk;
    private ImageView back;
    private RelativeLayout messageLayout;
    private ExpandableRelativeLayout expandableLayoutMessage;
    private ListView listMessageType;
    private String[][] MessageTypeArray;
    private String messageTypeSelected;
    JSONArray messageTypeList = new JSONArray();
    private Typeface typeface;
    private ProgressDialog prgDialog;
    private TextView txtTitr;
    private int sizeMessageTypeList = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
        callMessageType();
    }

    private void callMessageType() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        prgDialog.show();
        // prepare the Request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_GET_MESSAGE_TYPE, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        prgDialog.hide();
                        JSONArray obj = null;
                        Log.d("call Message Type", response.toString());
                        try {
                            obj = response;
                            for (int i = 0; i < obj.length(); i++) {
                                messageTypeList.put(obj.getJSONObject(i));
                                sizeMessageTypeList++;
                            }
                            MessageTypeArray = new String[sizeMessageTypeList][sizeMessageTypeList];
                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    SendMessageActivity.this,
                                    R.layout.select_list_radio);
                            int s = 0;
                            for (int i = 0; i < sizeMessageTypeList; i++) {
                                MessageTypeArray[i][0] = messageTypeList.getJSONObject(i).getString("Name");
                                MessageTypeArray[i][1] = messageTypeList.getJSONObject(i).getString("ID");
                                MessageTypeArray[i][2] = messageTypeList.getJSONObject(i).getString("Activated");
                                arrayAdapter.add(MessageTypeArray[i][0]);
                                s++;
                            }
                            ViewGroup.LayoutParams params = expandableLayoutMessage.getLayoutParams();
                            int height = (s * 110);
                            params.height = height;
                            expandableLayoutMessage.setLayoutParams(params);
                            listMessageType.setAdapter(arrayAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prgDialog.hide();
                        Log.d("Error.Response", String.valueOf(error));
                    }
                });
        int socketTimeout = 5000; // 5 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        requestQueue.add(jsonArrayRequest);
    }

    private void initView() {
        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText(R.string.txt_message_send);
        message = (TextView) findViewById(R.id.message);
        text_me = (TextView) findViewById(R.id.text_me);
        btn = (ImageButton) findViewById(R.id.btn);
        btn2 = (ImageButton) findViewById(R.id.btn2);
        messageLayout = (RelativeLayout) findViewById(R.id.messageLayout);
        expandableLayoutMessage = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutMessage);
        listMessageType = (ListView) findViewById(R.id.listMessage);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setTypeface(typeface);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                    callNewSendMessageWS();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayoutMessage.toggle();
                btn.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.VISIBLE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayoutMessage.toggle();
                btn.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.INVISIBLE);
            }
        });
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayoutMessage.toggle();
                if (btn.getVisibility() == View.VISIBLE) {
                    btn.setVisibility(View.INVISIBLE);
                    btn2.setVisibility(View.VISIBLE);
                } else {
                    btn.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.INVISIBLE);
                }
            }
        });
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listMessageType.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                expandableLayoutMessage.toggle();
                btn.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.INVISIBLE);
                message.setText(MessageTypeArray[position][0]);
                messageTypeSelected = MessageTypeArray[position][1];

            }
        });
    }

    private boolean validate() {

        if (messageTypeSelected == null) {
            CommonMethods.showSingleButtonAlert(SendMessageActivity.this, getString(R.string.txt_attention), "لطفا نوع پیام خود را انتخاب نمائبد", getString(R.string.pop_up_ok));
            return false;
        }
        if (text_me.getText().toString().isEmpty()) {
            CommonMethods.showSingleButtonAlert(SendMessageActivity.this, getString(R.string.txt_attention), "لطفا پیام خود را وارد نمائبد", getString(R.string.pop_up_ok));
            return false;
        }
        return true;
    }


    public void callNewSendMessageWS() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(SendMessageActivity.this, "ERAM", "PersonID", String.class));
            params.put(REQUEST_TYPE_MESSAGE, messageTypeSelected);
            params.put(REQUEST_MESSAGE, text_me.getText().toString());

            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Send Message ", response.toString());
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            CommonMethods.showSingleButtonAlert(SendMessageActivity.this, getString(R.string.txt_attention), "با تشکر پیام شما با موفقیت ارسال شد", getString(R.string.pop_up_ok), new CommonMethods.IL() {
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
                            CommonMethods.showSingleButtonAlert(SendMessageActivity.this, getString(R.string.txt_attention), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_SEND_TO_ADMIN_MESSAGE, params, listener, errorListener);
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
