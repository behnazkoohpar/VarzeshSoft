package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.koohpar.eram.firebase.MyFirebaseMessagingService;
import com.koohpar.eram.models.Messages;
import com.koohpar.eram.recycleViewAdapters.MessageListRecycleViewAdapter;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

public class ListMessageActivity extends AppCompatActivity implements IAPIConstantants, IApiUrls, View.OnClickListener {

    private ProgressDialog prgDialog;
    private Typeface typeface;
    private ArrayList<Messages> messagesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TextView txtTitr;
    private ImageView back, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_message);
        MyFirebaseMessagingService.badgeCount = 0;
        ShortcutBadger.removeCount(ListMessageActivity.this); //for 1.1.4+
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(false);
        initView();
        callGetMessage();
    }

    private void initView() {
        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText("لیست پیام های من");
        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        recyclerView = (RecyclerView) findViewById(R.id.my_cost_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    private void callGetMessage() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(ListMessageActivity.this, "ERAM", "PersonID", String.class));
//            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("LisMessage", response.toString());
                    try {
                        if (response.getString("status").equalsIgnoreCase("true")) {
//                            prgDialog.hide();
                            JSONArray obj = response.getJSONArray("Result");
                            messagesList = null;
                            messagesList = new ArrayList<>();
                            for (int i = 0; i < obj.length(); i++) {
                                Messages messages = new Messages(
                                        obj.getJSONObject(i).getString("ID"),
                                        obj.getJSONObject(i).getString("SendDate"),
                                        obj.getJSONObject(i).getString("SendTime"),
                                        obj.getJSONObject(i).getString("Title"),
                                        obj.getJSONObject(i).getString("seened"),
                                        obj.getJSONObject(i).getString("MessageBody")
                                );
                                messagesList.add(messages);
                            }

                            ShortcutBadger.removeCount(ListMessageActivity.this);
                            recyclerView.setAdapter(new MessageListRecycleViewAdapter(messagesList));
                        } else {
                            CommonMethods.showSingleButtonAlert(ListMessageActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    prgDialog.hide();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_ALL_MESSAGE_FROM_ADMIN, params, listener, errorListener);
            int socketTimeout = 5000; // 5 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
//            prgDialog.hide();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                startActivity(new Intent(ListMessageActivity.this,SendMessageActivity.class ));
                break;
        }
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(ListMessageActivity.this,MainActivity.class));
    }

}
