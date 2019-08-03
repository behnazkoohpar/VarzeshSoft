package com.koohpar.eram.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.koohpar.eram.R;
import com.koohpar.eram.tools.CommonMethods;

public class AboutActivity extends AppCompatActivity {

        private TextView textView15,textView4,connectUs;
        private Typeface typeface;
        private ImageView linkedIn, telegram, address;
        private RelativeLayout web, email, tel;
        private TextView textView1;
        private ImageView backButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            try {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_about);
                typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
                textView4 = (TextView) findViewById(R.id.textView4);
                textView1 = (TextView) findViewById(R.id.textView1);
                textView4.setText("درباره ما");
                backButton = (ImageView) findViewById(R.id.back);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.back:
                                AboutActivity.this.finish();
                                break;
                        }
                    }
                });
                try {
                    PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;
                    textView1.setText("نسخه برنامه: "+version);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
//                ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);
//                flipper.startFlipping();
//            linkedIn = (ImageView) findViewById(R.id.imageButton);
//                telegram = (ImageView) findViewById(R.id.imageButton2);
//                address = (ImageView) findViewById(R.id.address);
             //   web = (RelativeLayout) findViewById(R.id.web);
             //   email = (RelativeLayout) findViewById(R.id.email);
             //   tel = (RelativeLayout) findViewById(R.id.tel);

//            linkedIn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    switch (v.getId()) {
//                        case R.id.imageButton:
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
//                            startActivity(browserIntent);
//                            break;
//                    }
//                }
//            });
//                telegram.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/VarzeshSoft"));
//                                startActivity(browserIntent);
//
//                    }
//                });
//            address.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    switch (v.getId()) {
//                        case R.id.imageButton3:
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/GhadirGroup"));
//                            startActivity(browserIntent);
//                            break;
//                    }
//                }
//            });
                web.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.varzeshsoft.com/"));
                                startActivity(browserIntent);

                    }
                });
                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@VarzeshSoft.com"});
                                i.putExtra(Intent.EXTRA_SUBJECT, "");
                                i.putExtra(Intent.EXTRA_TEXT   , "");
                                try {
                                    startActivity(Intent.createChooser(i, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(AboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                }

                    }
                });
                tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                                CommonMethods.showSingleButtonAlert(AboutActivity.this, getString(R.string.txt_attention), getString(R.string.text_support), getString(R.string.pop_up_ok));

                                Intent i = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("tel:02191007751"));
                                startActivity(i);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

