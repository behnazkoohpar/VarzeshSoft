package com.koohpar.eram.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.koohpar.eram.R;
import com.koohpar.eram.tools.DateUtil;
import com.koohpar.eram.tools.util.PersianCalendar;

public class MembershipFileCard extends AppCompatActivity implements View.OnClickListener {

    private String text = "";
    private ImageView imageView, back, img;
    private TextView orgOzv, date, time, dateStartOzv, dateEndOzv, name, ozviyat, number, textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_file_card);
        img = (ImageView) findViewById(R.id.img);
        imageView = (ImageView) findViewById(R.id.barcodeImage);
        back = (ImageView) findViewById(R.id.back);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText("کارت عضویت");
        orgOzv = (TextView) findViewById(R.id.orgOzv);
        orgOzv.setText(LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "Organ", String.class));
        date = (TextView) findViewById(R.id.date);
        PersianCalendar pCalendar = new PersianCalendar();
        date.setText(String.format(" تاریخ امروز:  %s/%s/%s  ", pCalendar.getPersianYear(), pCalendar.getPersianMonth(), pCalendar.getPersianDay()));
        time = (TextView) findViewById(R.id.time);
        time.setText(String.format("  ساعت: %s  ", DateUtil.getCurrentTime()));
        dateStartOzv = (TextView) findViewById(R.id.dateStartOzv);
        dateStartOzv.setText(LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "PersianMembershipDate", String.class));
        dateEndOzv = (TextView) findViewById(R.id.dateEndOzv);
        int year = Integer.parseInt(LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "PersianMembershipDate", String.class).substring(0, 4));
        year = year + 1;
        int month = Integer.parseInt(LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "PersianMembershipDate", String.class).substring(5, 7));
        int day = Integer.parseInt(LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "PersianMembershipDate", String.class).substring(8, 10));
        dateEndOzv.setText(String.valueOf(year).concat("/").concat(String.valueOf(month)).concat("/").concat(String.valueOf(day)));
        name = (TextView) findViewById(R.id.name);
        name.setText(LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "PersonNameOnly", String.class));
        ozviyat = (TextView) findViewById(R.id.ozviyat);
        ozviyat.setText(LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "Grade", String.class));
        number = (TextView) findViewById(R.id.number);
        number.setText(LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "CardNumber", String.class));
        text = LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "CardNumber", String.class);
        String jsonObject = LoginActivity.getSavedObjectFromPreference(MembershipFileCard.this, "ERAM", "PersonImage", String.class);
        if (jsonObject != null && !jsonObject.isEmpty() && !jsonObject.equalsIgnoreCase("0")) {
            byte[] decodedString = Base64.decode(jsonObject, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img.setImageBitmap(decodedByte);
        }
        back.setOnClickListener(MembershipFileCard.this);
        setBarcode();
    }

    public void setBarcode() {
        byte[] encodeValue = Base64.encode(text.getBytes(), Base64.DEFAULT);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODE_128, 800, 800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (
                WriterException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
