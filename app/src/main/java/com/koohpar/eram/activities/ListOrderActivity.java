package com.koohpar.eram.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import com.koohpar.eram.models.Order;
import com.koohpar.eram.recycleViewAdapters.OrderListRecycleViewAdapter;
import com.koohpar.eram.tools.CommonMethods;
import com.koohpar.eram.tools.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListOrderActivity extends AppCompatActivity implements View.OnClickListener, IAPIConstantants, IApiUrls {

    private ProgressDialog prgDialog;
    private TextView txtTitr, txtmoratab, txt2moratab, txtfilter, txt2filter;
    private RelativeLayout filter, moratab;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView back;
    private List<Order> orderList = new ArrayList<>();
    private ImageView imgmoratab, imgfilter;
    private int filtertype = 2;
    private int sortType = 0;
    private boolean sortAsced = true;
    private RadioButton allUnitRadio, selectUnitRadio, myUnitRadio;
    private Button btnFilter;
    private Typeface typeface;
    private RadioButton roleUnitRadio, metrajUnitRadio, numUnitRadio;
    private Switch switch1, switch2, switch3;
    private Button btnSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
        callOrderListWS(sortType, sortAsced, filtertype);
    }

    private void initView() {
        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText("لیست سرویس های من");
        back = (ImageView) findViewById(R.id.back);
        recyclerView = (RecyclerView) findViewById(R.id.my_cost_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListner);

        txtmoratab = (TextView) findViewById(R.id.txtmoratab);
        txt2moratab = (TextView) findViewById(R.id.txt2moratab);
        txtfilter = (TextView) findViewById(R.id.txtfilter);
        txt2filter = (TextView) findViewById(R.id.txt2filter);
        imgmoratab = (ImageView) findViewById(R.id.imgmoratab);
        imgfilter = (ImageView) findViewById(R.id.imgfilter);
        moratab = (RelativeLayout) findViewById(R.id.moratab);
        filter = (RelativeLayout) findViewById(R.id.filter);

        txtmoratab.setOnClickListener(this);
        txt2moratab.setOnClickListener(this);
        txtfilter.setOnClickListener(this);
        txt2filter.setOnClickListener(this);
        filter.setOnClickListener(this);
        imgfilter.setOnClickListener(this);
        moratab.setOnClickListener(this);
        imgmoratab.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListner = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            callOrderListWS(sortType, sortAsced, filtertype);
        }
    };

    private void showFilterDialog() {
        final Dialog dialogFilter = new Dialog(ListOrderActivity.this);
        dialogFilter.setContentView(R.layout.layout_filter);

        allUnitRadio = (RadioButton) dialogFilter.findViewById(R.id.allUnitRadio);
        myUnitRadio = (RadioButton) dialogFilter.findViewById(R.id.myUnitRadio);
        selectUnitRadio = (RadioButton) dialogFilter.findViewById(R.id.selectUnitRadio);
//        userUnitChecked = (CheckBox) dialogFilter.findViewById(R.id.userUnitChecked);
//        userUnitRoleChecked = (CheckBox) dialogFilter.findViewById(R.id.userUnitRoleChecked);
//        userUnitRole2Checked = (CheckBox) dialogFilter.findViewById(R.id.userUnitRole2Checked);
//        userUnitRole3Checked = (CheckBox) dialogFilter.findViewById(R.id.userUnitRole3Checked);
        btnFilter = (Button) dialogFilter.findViewById(R.id.btnFilter);
//        linearlayout = (LinearLayout) dialogFilter.findViewById(R.id.linearlayout);
        btnFilter.setTypeface(typeface);
        TextView onvan = (TextView) dialogFilter.findViewById(R.id.textView4);
        ImageView back = (ImageView) dialogFilter.findViewById(R.id.back);
        onvan.setText(R.string.txt_filter);
        dialogFilter.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFilter.dismiss();
            }
        });
        allUnitRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt2filter.setText("نمایش همه");
                allUnitRadio.setChecked(true);
                filtertype = 0;
                myUnitRadio.setChecked(false);
                selectUnitRadio.setChecked(false);
//                linearlayout.setVisibility(View.GONE);
            }
        });
        myUnitRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt2filter.setText("غیر قابل استفاده");
                allUnitRadio.setChecked(false);
                myUnitRadio.setChecked(true);
                filtertype = 1;
                selectUnitRadio.setChecked(false);
//                linearlayout.setVisibility(View.GONE);
            }
        });
        selectUnitRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt2filter.setText("قابل استفاده");
                allUnitRadio.setChecked(false);
                myUnitRadio.setChecked(false);
                selectUnitRadio.setChecked(true);
                filtertype = 2;
//                linearlayout.setVisibility(View.VISIBLE);
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFilter.dismiss();
                if (allUnitRadio.isChecked())
                    filtertype = 0;
                if (myUnitRadio.isChecked())
                    filtertype = 1;
                if (selectUnitRadio.isChecked())
                    filtertype = 2;
                callOrderListWS(sortType, sortAsced, filtertype);
            }
        });
    }

    private void showSortDialog() {
        final Dialog dialogSort = new Dialog(ListOrderActivity.this);
        dialogSort.setContentView(R.layout.layout_sort);

        numUnitRadio = (RadioButton) dialogSort.findViewById(R.id.numUnitRadio);
        metrajUnitRadio = (RadioButton) dialogSort.findViewById(R.id.metrajUnitRadio);
        roleUnitRadio = (RadioButton) dialogSort.findViewById(R.id.roleUnitRadio);
        switch1 = (Switch) dialogSort.findViewById(R.id.switch1);
        switch2 = (Switch) dialogSort.findViewById(R.id.switch2);
        switch3 = (Switch) dialogSort.findViewById(R.id.switch3);
        btnSort = (Button) dialogSort.findViewById(R.id.btnSort);
        btnSort.setTypeface(typeface);
        TextView onvan = (TextView) dialogSort.findViewById(R.id.textView4);
        ImageView back = (ImageView) dialogSort.findViewById(R.id.back);
        onvan.setText(R.string.txt_sort);
        dialogSort.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSort.dismiss();
            }
        });
        numUnitRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numUnitRadio.setChecked(true);
                sortType = 0;
                metrajUnitRadio.setChecked(false);
                roleUnitRadio.setChecked(false);
                switch1.setEnabled(true);
                switch2.setEnabled(false);
                switch2.setChecked(false);
                switch3.setEnabled(false);
                switch3.setChecked(false);
            }
        });
        metrajUnitRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numUnitRadio.setChecked(false);
                metrajUnitRadio.setChecked(true);
                sortType = 1;
                roleUnitRadio.setChecked(false);
                switch1.setEnabled(false);
                switch1.setChecked(false);
                switch2.setEnabled(true);
                switch3.setEnabled(false);
                switch3.setChecked(false);
            }
        });
        roleUnitRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numUnitRadio.setChecked(false);
                metrajUnitRadio.setChecked(false);
                roleUnitRadio.setChecked(true);
                sortType = 2;
                switch1.setEnabled(false);
                switch1.setChecked(false);
                switch2.setEnabled(false);
                switch2.setChecked(false);
                switch3.setEnabled(true);
            }
        });

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch1.isChecked())
                    sortAsced = true;
                else
                    sortAsced = false;
            }
        });
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch2.isChecked())
                    sortAsced = true;
                else
                    sortAsced = false;
            }
        });
        switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch3.isChecked())
                    sortAsced = true;
                else
                    sortAsced = false;
            }
        });
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSort.dismiss();
                callOrderListWS(sortType, sortAsced, filtertype);
            }
        });

    }


    private void callOrderListWS(int sortType, boolean sortAsced, int filtertype) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "PersonID", String.class));
            params.put(REQUEST_SORT_TYPE, sortType);
            params.put(REQUEST_SORT_ASCED, sortAsced);
            params.put(REQUEST_FILTER_TYPE, filtertype);
            String monthOk="";
            String dayOk="";
            int month = DateUtil.getCurrentMonth();
            if(month<10)
                monthOk= "0"+month;
            else
                monthOk = String.valueOf(month);
            int day  = DateUtil.getCurrentDay();
            if(day<10)
                dayOk = "0"+day;
            else
                dayOk = String.valueOf(day);
            params.put(REQUEST_SHAMSI_DATE, DateUtil.getCurrentYear() + "/"+monthOk+"/" +dayOk);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("status").equalsIgnoreCase("true")) {
                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);

                            prgDialog.hide();
                            JSONArray obj = response.getJSONArray("Result");
                            Log.d("OrderListActivity", response.toString());

                            orderList = null;
                            orderList = new ArrayList<>();
                            for (int i = 0; i < obj.length(); i++) {
                                Order order = new Order(
                                        obj.getJSONObject(i).getString("ID"),
                                        obj.getJSONObject(i).getString("ServiceID"),
                                        obj.getJSONObject(i).getString("MembershipFileID"),
                                        obj.getJSONObject(i).getString("ServiceTitle"),
                                        obj.getJSONObject(i).getString("ServiceType_Text"),
                                        obj.getJSONObject(i).getString("RegistrationSerial"),
                                        obj.getJSONObject(i).getString("ExpireDate"),
                                        obj.getJSONObject(i).getString("RegisterationUsedSessionsCount"),
                                        obj.getJSONObject(i).getString("RegisterationRemainedSessionsCount"),
                                        obj.getJSONObject(i).getString("CreditChargeUsedAmount"),
                                        obj.getJSONObject(i).getString("CreditChargeRemainedAmount"),
                                        obj.getJSONObject(i).getString("ServiceTotalAmount")
                                );
                                orderList.add(order);
                            }
                            recyclerView.setAdapter(new OrderListRecycleViewAdapter(orderList));
                        } else {
                            CommonMethods.showSingleButtonAlert(ListOrderActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_ALL_SERVICES, params, listener, errorListener);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgfilter:
            case R.id.txtfilter:
            case R.id.txt2filter:
            case R.id.filter:
                showFilterDialog();
                break;
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
