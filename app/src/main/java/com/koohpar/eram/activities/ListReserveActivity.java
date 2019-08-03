package com.koohpar.eram.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.koohpar.eram.models.OrganizationUnit;
import com.koohpar.eram.models.Reserve;
import com.koohpar.eram.recycleViewAdapters.OrganListAdapter;
import com.koohpar.eram.recycleViewAdapters.ReserveListRecycleViewAdapter;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListReserveActivity extends AppCompatActivity implements View.OnClickListener, IAPIConstantants, IApiUrls {
    private ProgressDialog prgDialog;
    private TextView txtTitr, txtmoratab, txt2moratab;
    private RelativeLayout moratab;
    private RecyclerView recyclerView, recyclerView1;
    private LinearLayoutManager layoutManager, layoutManager1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView back;
    private List<Reserve> orderList = new ArrayList<>();
    private ImageView imgmoratab;
    private Typeface typeface;
    private Button btnSort;
    private ArrayList<OrganizationUnit> organizationUnitList = new ArrayList<>();
    private OrganListAdapter mAdapter;
    public static OrganizationUnit organSelected ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reserve);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();

        callGetOrganizationUnitWS();
    }

    private void initView() {
        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText("رزرو خدمات");
        back = (ImageView) findViewById(R.id.back);
        recyclerView = (RecyclerView) findViewById(R.id.my_cost_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListner);

        txtmoratab = (TextView) findViewById(R.id.txtmoratab);
        txt2moratab = (TextView) findViewById(R.id.txt2moratab);
        imgmoratab = (ImageView) findViewById(R.id.imgmoratab);
        moratab = (RelativeLayout) findViewById(R.id.moratab);

        txtmoratab.setOnClickListener(this);
        txt2moratab.setOnClickListener(this);
        moratab.setOnClickListener(this);
        imgmoratab.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListner = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            callCashOrderListWS();
        }
    };

    private void showSortDialog() {
        final Dialog dialogSort = new Dialog(ListReserveActivity.this);
        dialogSort.setContentView(R.layout.layout_organ);

        recyclerView1 = (RecyclerView) dialogSort.findViewById(R.id.my_organ_list_recycler_view);
        layoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager1);
        mAdapter = new OrganListAdapter(organizationUnitList);
        recyclerView1.setAdapter(mAdapter);
        TextView onvan = (TextView) dialogSort.findViewById(R.id.textView4);
        ImageView back = (ImageView) dialogSort.findViewById(R.id.back);
        onvan.setText(R.string.txt_organ);
        dialogSort.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSort.dismiss();
            }
        });
        mAdapter.setOnitemclickListener(new OrganListAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position, String title, String id) {
                dialogSort.dismiss();
                organSelected = organizationUnitList.get(position);
                txt2moratab.setText(organSelected.getName());
                callCashOrderListWS();
            }
        });
    }

    private void callCashOrderListWS() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PersonID", String.class));
            params.put(REQUEST_ORGANIZATION_UNIT, organSelected.getID());
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("status").equalsIgnoreCase("true")) {
                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);

                            prgDialog.hide();
                            JSONArray obj = response.getJSONArray("Result");
                            Log.d("Reserve", response.toString());

                            orderList = null;
                            orderList = new ArrayList<>();
                            for (int i = 0; i < obj.length(); i++) {
                                Reserve reserve = new Reserve(
                                        obj.getJSONObject(i).getString("ID"),
                                        obj.getJSONObject(i).getString("Title"),
                                        obj.getJSONObject(i).getString("Code")
                                );
                                orderList.add(reserve);
                            }
                            recyclerView.setAdapter(new ReserveListRecycleViewAdapter(orderList));
                        } else {
                            CommonMethods.showSingleButtonAlert(ListReserveActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
                            swipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.d("MyFirebaseInstanceIDService  don't send tokenId", refreshedToken);
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_RESERVE, params, listener, errorListener);
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

    private void callGetOrganizationUnitWS() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equalsIgnoreCase("true")) {
                        prgDialog.hide();
                        JSONArray obj = response.getJSONArray("Result");
                        Log.d("Reserve", response.toString());

                        orderList = null;
                        orderList = new ArrayList<>();
                        for (int i = 0; i < obj.length(); i++) {
                            OrganizationUnit organizationUnit = new OrganizationUnit(
                                    obj.getJSONObject(i).getString("ID"),
                                    obj.getJSONObject(i).getString("Name")
                            );
                            organizationUnitList.add(organizationUnit);
                        }
                        organSelected = organizationUnitList.get(0);
                        txt2moratab.setText(organSelected.getName());
                        callCashOrderListWS();
                    } else {
                        CommonMethods.showSingleButtonAlert(ListReserveActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                    Log.d("MyFirebaseInstanceIDService  don't send tokenId", refreshedToken);
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_ORGANIZATION_UNIT, params, listener, errorListener);
        int socketTimeout = 5000; // 5 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.moratab:
            case R.id.imgmoratab:
            case R.id.txtmoratab:
            case R.id.txt2moratab:
                showSortDialog();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
