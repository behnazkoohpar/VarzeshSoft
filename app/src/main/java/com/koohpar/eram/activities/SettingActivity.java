package com.koohpar.eram.activities;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.koohpar.eram.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText day, number, sharj;
    private Button btn;
    private TextView textView4;
    private ImageView back;
    private Typeface typefaces;
    private CheckBox check_day, check_number, check_sharj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        typefaces = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        initView();
    }

    private void initView() {
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText("تنظیمات");
        day = (EditText) findViewById(R.id.day);
        day.setText(LoginActivity.getSavedObjectFromPreference(SettingActivity.this, "ERAM", "DAY", String.class));
        number = (EditText) findViewById(R.id.number);
        number.setText(LoginActivity.getSavedObjectFromPreference(SettingActivity.this, "ERAM", "NUMBER", String.class));
        sharj = (EditText) findViewById(R.id.sharj);
        sharj.setText(LoginActivity.getSavedObjectFromPreference(SettingActivity.this, "ERAM", "SHARJ", String.class));
        btn = (Button) findViewById(R.id.btn);
        check_day = (CheckBox) findViewById(R.id.check_day);
        if (LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "CHECK_DAY", boolean.class) == null) {
            check_day.setChecked(true);
        } else if (LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "CHECK_DAY", boolean.class))
            check_day.setChecked(true);
        else {
            day.setEnabled(false);
            check_day.setChecked(false);
        }
        check_number = (CheckBox) findViewById(R.id.check_number);
        if (LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "CHECK_NUMBER", boolean.class) == null)
            check_number.setChecked(true);
        else if (LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "CHECK_NUMBER", boolean.class))
            check_number.setChecked(true);
        else {
            check_number.setChecked(false);
            number.setEnabled(false);
        }
        check_sharj = (CheckBox) findViewById(R.id.check_sharj);
        if (LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "CHECK_SHARJ", boolean.class) == null)
            check_sharj.setChecked(true);
        else if (LoginActivity.getSavedObjectFromPreference(getApplicationContext(), "ERAM", "CHECK_SHARJ", boolean.class))
            check_sharj.setChecked(true);
        else {
            check_sharj.setChecked(false);
            sharj.setEnabled(false);
        }
        btn.setTypeface(typefaces);
        back = (ImageView) findViewById(R.id.back);

        btn.setOnClickListener(this);
        back.setOnClickListener(this);
        check_day.setOnClickListener(this);
        check_number.setOnClickListener(this);
        check_sharj.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:

                if (!check_sharj.isChecked() || sharj.getText().toString().isEmpty())
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "SHARJ", "0");
                else
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "SHARJ", sharj.getText().toString());
                if (!check_number.isChecked() || number.getText().toString().isEmpty())
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "NUMBER", "0");
                else
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "NUMBER", number.getText().toString());
                if (!check_day.isChecked() || day.getText().toString().isEmpty())
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "DAY", 0);
                else
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "DAY", day.getText().toString());

                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                break;
            case R.id.back:
                finish();
                break;

            case R.id.check_day:
                if (check_day.isChecked()) {
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "CHECK_DAY", true);
                    day.setEnabled(true);
                } else {
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "CHECK_DAY", false);
                    day.setEnabled(false);
                }
                break;

            case R.id.check_number:
                if (check_number.isChecked()) {
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "CHECK_NUMBER", true);
                    number.setEnabled(true);
                } else {
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "CHECK_NUMBER", false);
                    number.setEnabled(false);
                }
                break;

            case R.id.check_sharj:
                if (check_sharj.isChecked()) {
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "CHECK_SHARJ", true);
                    sharj.setEnabled(true);
                } else {
                    LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "CHECK_SHARJ", false);
                    sharj.setEnabled(false);
                }
                break;
        }

    }
}
