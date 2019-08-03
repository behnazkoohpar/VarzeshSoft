package com.koohpar.eram.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.koohpar.eram.R;
import com.koohpar.eram.activities.MainActivity;


public class CustomRadioButton extends RadioButton {

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomRadioButton(Context context) {
        super(context);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            // Ignore if within Eclipse
            return;
        }
        String font = getContext().getString(R.string.font_aurulent_sans_regular);
        if (attrs != null) {
            // Look up any layout-defined attributes
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.CustomTextView_customFont:
                        font = a.getString(attr);
                        break;
                }
            }
            a.recycle();
        }
        Typeface tf = null;
        try {
            tf = MainActivity.typefaceHashMap.get(font);
        } catch (Exception e) {
            Log.e("CustomTextViewLog", "Could not get typeface: " + e.getMessage());
        }
        setTypeface(tf);
    }

}
