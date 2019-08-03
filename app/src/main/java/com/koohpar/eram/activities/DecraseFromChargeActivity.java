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
import android.widget.ExpandableListView;
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
import com.koohpar.eram.models.DecreaseFromCharge;
import com.koohpar.eram.models.OrganizationUnit;
import com.koohpar.eram.recycleViewAdapters.ExpandableListAdapter;
import com.koohpar.eram.recycleViewAdapters.OrganListAdapter;
import com.koohpar.eram.tools.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DecraseFromChargeActivity extends AppCompatActivity implements View.OnClickListener, IAPIConstantants, IApiUrls {

    private ProgressDialog prgDialog;
    private TextView txtTitr, txtmoratab, txt2moratab;
    private RelativeLayout moratab;
    private RecyclerView recyclerView, recyclerView1;
    private LinearLayoutManager layoutManager, layoutManager1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView back;
    private List<DecreaseFromCharge> orderList = new ArrayList<>();
    private ImageView imgmoratab;
    private Typeface typeface;
    private Button btnSort;
    private ArrayList<OrganizationUnit> organizationUnitList = new ArrayList<>();
    private OrganListAdapter mAdapter;
    public static OrganizationUnit organSelected;
    ExpandableListView expandableListView;
    android.widget.ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    TreeMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrase_from_charge);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        initView();
        callGetOrganizationUnitWS();

    }


    private void initView() {
        txtTitr = (TextView) findViewById(R.id.textView4);
        txtTitr.setText("تعرفه کسر از شارژ");
        back = (ImageView) findViewById(R.id.back);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

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
        final Dialog dialogSort = new Dialog(DecraseFromChargeActivity.this);
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
            Bundle bundle = getIntent().getExtras();
            params.put(REQUEST_CHARGE_SERVICE_ID, bundle.getString("service_id"));
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
                            Log.d("DecreaseFromCharge", response.toString());

                            orderList = null;
                            orderList = new ArrayList<>();
                            for (int i = 0; i < obj.length(); i++) {
                                DecreaseFromCharge decreaseFromCharge = new DecreaseFromCharge(
                                        obj.getJSONObject(i).getString("FromTime"),
                                        obj.getJSONObject(i).getString("ToTime"),
                                        obj.getJSONObject(i).getString("InputAmount"),
                                        obj.getJSONObject(i).getString("DayNo"),
                                        obj.getJSONObject(i).getString("FromExtraTime"),
                                        obj.getJSONObject(i).getString("ExtraTimeAmount")
                                );
                                orderList.add(decreaseFromCharge);
                            }
                            setCategory(orderList);
                        } else {
                            CommonMethods.showSingleButtonAlert(DecraseFromChargeActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_FORMULA_FRACTION_OF_CHARGE, params, listener, errorListener);
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
                        Log.d("CashServiceActivity", response.toString());

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
                        CommonMethods.showSingleButtonAlert(DecraseFromChargeActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
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

    public void setCategory(List<DecreaseFromCharge> categoryResponses) {
        TreeMap<String, List<String>> ParentItem = new TreeMap<String, List<String>>();

        //select parent name
        for (int i = 0; i < categoryResponses.size(); i++) {
            if (categoryResponses.get(i).getDayNo().equals("0")) {
                List<String> childs2 = new ArrayList<>();
                for (int k = 0; k < categoryResponses.size(); k++) {
                    if (categoryResponses.get(k).getDayNo() != null && categoryResponses.get(k).getDayNo().equalsIgnoreCase("0"))
                        childs2.add(categoryResponses.get(k).getFromTime() + "/" + categoryResponses.get(k).getToTime()+ "/" + categoryResponses.get(k).getInputAmount());
                }
                ParentItem.put("0", childs2);
            }

            else if (categoryResponses.get(i).getDayNo().equals("1")) {
                List<String> childs2 = new ArrayList<>();
                for (int k = 0; k < categoryResponses.size(); k++) {
                    if (categoryResponses.get(k).getDayNo() != null && categoryResponses.get(k).getDayNo().equalsIgnoreCase("1"))
                        childs2.add(categoryResponses.get(k).getFromTime() + "/" + categoryResponses.get(k).getToTime()+ "/" + categoryResponses.get(k).getInputAmount());
                }
                ParentItem.put("1", childs2);
            }

            else if (categoryResponses.get(i).getDayNo().equals("2")) {
                List<String> childs2 = new ArrayList<>();
                for (int k = 0; k < categoryResponses.size(); k++) {
                    if (categoryResponses.get(k).getDayNo() != null && categoryResponses.get(k).getDayNo().equalsIgnoreCase("2"))
                        childs2.add(categoryResponses.get(k).getFromTime() + "/" + categoryResponses.get(k).getToTime()+ "/" + categoryResponses.get(k).getInputAmount());
                }
                ParentItem.put("2", childs2);
            }
            else if (categoryResponses.get(i).getDayNo().equals("3")) {
                List<String> childs2 = new ArrayList<>();
                for (int k = 0; k < categoryResponses.size(); k++) {
                    if (categoryResponses.get(k).getDayNo() != null && categoryResponses.get(k).getDayNo().equalsIgnoreCase("3"))
                        childs2.add(categoryResponses.get(k).getFromTime() + "/" + categoryResponses.get(k).getToTime()+ "/" + categoryResponses.get(k).getInputAmount());
                }
                ParentItem.put("3", childs2);
            }
            else if (categoryResponses.get(i).getDayNo().equals("4")) {
                List<String> childs2 = new ArrayList<>();
                for (int k = 0; k < categoryResponses.size(); k++) {
                    if (categoryResponses.get(k).getDayNo() != null && categoryResponses.get(k).getDayNo().equalsIgnoreCase("4"))
                        childs2.add(categoryResponses.get(k).getFromTime() + "/" + categoryResponses.get(k).getToTime()+ "/" + categoryResponses.get(k).getInputAmount());
                }
                ParentItem.put("4", childs2);
            }
            else if (categoryResponses.get(i).getDayNo().equals("5")) {
                List<String> childs2 = new ArrayList<>();
                for (int k = 0; k < categoryResponses.size(); k++) {
                    if (categoryResponses.get(k).getDayNo() != null && categoryResponses.get(k).getDayNo().equalsIgnoreCase("5"))
                        childs2.add(categoryResponses.get(k).getFromTime() + "/" + categoryResponses.get(k).getToTime()+ "/" + categoryResponses.get(k).getInputAmount());
                }
                ParentItem.put("5", childs2);
            }
            else if (categoryResponses.get(i).getDayNo().equals("6")) {
                List<String> childs2 = new ArrayList<>();
                for (int k = 0; k < categoryResponses.size(); k++) {
                    if (categoryResponses.get(k).getDayNo() != null && categoryResponses.get(k).getDayNo().equalsIgnoreCase("6"))
                        childs2.add(categoryResponses.get(k).getFromTime() + "/" + categoryResponses.get(k).getToTime()+ "/" + categoryResponses.get(k).getInputAmount());
                }
                ParentItem.put("6", childs2);
            }
        }
        TreeMap<String, List<String>> sorted = new TreeMap<>();
        sorted.putAll(ParentItem);
        sorted.entrySet();
        expandableListDetail = sorted;
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.expandGroup(0, true);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                String nama[] = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).split("/");
//                Intent intent = CategoryStuffActivity.getStartIntent(CategoryActivity.this, nama[1],null,null);
//                startActivity(intent);
                return false;
            }
        });
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
