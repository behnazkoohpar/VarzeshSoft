package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.models.ERAM;
import com.koohpar.eram.tools.AppConstants;
import com.koohpar.eram.tools.CommonMethods;
import com.koohpar.eram.tools.DemoBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChartActivity extends DemoBase implements OnChartValueSelectedListener, IApiUrls, IAPIConstantants {
    private PieChart chart, chart1;
    private ProgressDialog prgDialog;
    private String UnLimitedAmountsSum, UsedUnLimitedAmountsSum, UnLimitedWalletStock, UsedLimitedAmountsSum, LimitedAmountsSum, LimitedWalletStock;
    private String[] parties, parties1;
    private TextView textChart1, textChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(false);
        initCharts();
        callGetCreditAmountInfo();
    }


    public void initCharts() {

        textChart = (TextView) findViewById(R.id.textChart);
        chart = findViewById(R.id.chart);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextTypeface(tfLight);
        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);

        chart1 = findViewById(R.id.chart1);
        textChart1 = (TextView) findViewById(R.id.textChart1);
        chart1.setUsePercentValues(true);
        chart1.getDescription().setEnabled(false);
        chart1.setExtraOffsets(5, 10, 5, 5);

        chart1.setDragDecelerationFrictionCoef(0.95f);

        chart1.setCenterTextTypeface(tfLight);
        chart1.setCenterText(generateCenterSpannableText());

        chart1.setDrawHoleEnabled(true);
        chart1.setHoleColor(Color.WHITE);

        chart1.setTransparentCircleColor(Color.WHITE);
        chart1.setTransparentCircleAlpha(110);

        chart1.setHoleRadius(58f);
        chart1.setTransparentCircleRadius(61f);

        chart1.setDrawCenterText(true);

        chart1.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart1.setRotationEnabled(true);
        chart1.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart1.setOnChartValueSelectedListener(this);

        chart1.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend ll = chart1.getLegend();
        ll.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        ll.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        ll.setOrientation(Legend.LegendOrientation.VERTICAL);
        ll.setDrawInside(false);
        ll.setXEntrySpace(7f);
        ll.setYEntrySpace(0f);
        ll.setYOffset(0f);

        // entry label styling
        chart1.setEntryLabelColor(Color.WHITE);
        chart1.setEntryLabelTypeface(tfRegular);
        chart1.setEntryLabelTextSize(12f);

    }

    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "PieChartActivity");
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


    private void setData(int count, float range) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) ((Integer.parseInt(parties[i]) * range) + range / 5),
                    parties[i % parties.length]));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.argb(255, 59, 80, 206));
        colors.add(Color.argb(255, 255, 45, 111));
//        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }

    private void setData1(int count, float range) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) ((Integer.parseInt(parties1[i]) + range / 5)),
                    parties1[i % parties1.length]));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.argb(255, 59, 80, 206));
        colors.add(Color.argb(255, 255, 45, 111));
        //colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);
        chart1.setData(data);
        // undo all highlights
        chart1.highlightValues(null);
        chart1.invalidate();
    }

    private void callGetCreditAmountInfo() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(ChartActivity.this, "ERAM", "PersonID", String.class));
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("callGetCreditAmountInfo", response.toString());
                    prgDialog.hide();
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        UnLimitedAmountsSum = jsonObject.getString("UnLimitedAmountsSum");
                        UsedUnLimitedAmountsSum = jsonObject.getString("UsedUnLimitedAmountsSum");
                        UnLimitedWalletStock = jsonObject.getString("UnLimitedWalletStock");
                        LimitedAmountsSum = jsonObject.getString("LimitedAmountsSum");
                        UsedLimitedAmountsSum = jsonObject.getString("UsedLimitedAmountsSum");
                        LimitedWalletStock = jsonObject.getString("LimitedWalletStock");
                        parties = new String[]{LimitedWalletStock, UsedLimitedAmountsSum};
                        parties1 = new String[]{UnLimitedWalletStock, UsedUnLimitedAmountsSum};

                        if (!LimitedAmountsSum.equalsIgnoreCase("0"))
                            setData(2, 10f);
                        else {
                            chart.setVisibility(View.GONE);
                            textChart.setVisibility(View.VISIBLE);
                        }

                        if (!UnLimitedAmountsSum.equalsIgnoreCase("0"))
                            setData1(2, 10f);
                        else {
                            chart1.setVisibility(View.GONE);
                            textChart1.setVisibility(View.VISIBLE);
                        }



                    } catch (Exception e) {
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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_GET_CREDIT_AMOUNT_INFO, params, listener, errorListener);
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
