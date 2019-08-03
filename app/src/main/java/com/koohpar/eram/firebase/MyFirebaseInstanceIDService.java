package com.koohpar.eram.firebase;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.koohpar.eram.activities.LoginActivity;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by Behnaz on 06/10/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements IAPIConstantants, IApiUrls {

    private String refreshedToken;

    @Override
    public void onTokenRefresh() {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "TOKEN", refreshedToken);
            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            if (LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PersonID", String.class) != null &&
                    !LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PersonID", String.class).equalsIgnoreCase(""))
                sendRegistrationToServer(refreshedToken);
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
                    Log.d("MyFirebaseInstanceIDService", refreshedToken);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MyFirebaseInstanceIDService  don't send tokenId", refreshedToken);
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

}