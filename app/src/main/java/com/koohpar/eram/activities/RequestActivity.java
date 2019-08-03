package com.koohpar.eram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koohpar.eram.R;
import com.koohpar.eram.tools.CommonMethods;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener{

    private Typeface typeface;
    private ProgressDialog prgDialog;
    private TextView txtTitr;
    private ImageView back;
    private RelativeLayout salel,reservel,progl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(true);
        setContentView(R.layout.activity_request);

        txtTitr = (TextView) findViewById(R.id.textView4);
        back = (ImageView) findViewById(R.id.back);
        salel = (RelativeLayout) findViewById(R.id.salel);
        reservel = (RelativeLayout) findViewById(R.id.reservel);
        progl = (RelativeLayout) findViewById(R.id.progl);
        txtTitr.setText(R.string.txt_request);
        salel.setOnClickListener(this);
        reservel.setOnClickListener(this);
        progl.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.salel:
                startActivity(new Intent(RequestActivity.this,CashServiceActivity.class));
                break;

            case R.id.reservel:
                startActivity(new Intent(RequestActivity.this, ListReserveActivity.class));
                break;

            case R.id.progl:
                CommonMethods.showSingleButtonAlert(RequestActivity.this, getString(R.string.txt_attention), "این بخش به زودی راه اندازی می شود.", getString(R.string.pop_up_ok));

//                startActivity(new Intent(RequestActivity.this, ListTransactionActivity.class));
                break;

            case R.id.back:
                finish();
                break;
        }

    }
}
