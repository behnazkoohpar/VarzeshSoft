package com.koohpar.eram.activities;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BarcodeActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;

    private Handler mHandler;
    private final int mInterval = 2000;
    private ImageView imageView;
    private TextView timerText,serviceName;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        imageView = (ImageView) findViewById(R.id.image);
        timerText = (TextView) findViewById(R.id.timerText);
        serviceName = (TextView) findViewById(R.id.serviceName);
        timerText = (TextView) findViewById(R.id.timerText);
        mHandler = new Handler();
        setCountDownTimer();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
        serviceName.setText(bundle.getString("service_title"));

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Date myDate = new Date();
        String NowDateTime = timeStampFormat.format(myDate);


        text = null; // Whatever you need to encode in the QR code

            text = "{" +
                    "\"A\":\"" + bundle.getString("membershipfile_id") + "\"," +
                    "\"B\":\"" + bundle.getString("id") + "\"," +
                    "\"C\":\"" + NowDateTime + "\"}";
        }
        byte[] encodeValue = Base64.encode(text.getBytes(), Base64.DEFAULT);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(new String(encodeValue), BarcodeFormat.QR_CODE, 800, 800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void setCountDownTimer() {
        countDownTimer = new CountDownTimer((120000), 1000) {
            private int minutes, seconds, miliseconds;
            private String minutesStr, secondsStr, milisecondStr;

            public void onTick(long millisUntilFinished) {
                seconds = (int) (millisUntilFinished / 1000 % 60);
                minutes = (int) (millisUntilFinished / 1000 / 60);
//                miliseconds = (int) seconds % 100;
                minutesStr = String.valueOf(minutes);
                secondsStr = String.valueOf(seconds);
                milisecondStr = String.valueOf(miliseconds);

                if (seconds < 10) secondsStr = "0" + secondsStr;
                if (minutes < 10) minutesStr = "0" + minutesStr;
//                if (miliseconds < 10) milisecondStr = "0" + milisecondStr;
                timerText.setText(minutesStr + ":" + secondsStr );//+ ":" + milisecondStr);
            }

            public void onFinish() {
                finish();
            }

        }.start();
    }

}
