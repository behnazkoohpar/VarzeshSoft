package com.koohpar.eram.tools;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.koohpar.eram.BuildConfig;
import com.koohpar.eram.R;
import com.koohpar.eram.custom.CustomTextView;

public class PopUpUtils {

    CustomPopClickListener popClickListener;

    public interface CustomPopClickListener {

        public void onCancel();

        public void onOkay();
    }

    public void showPopUp(Context mContext, String title, final String cancel, String ok, String message, CustomPopClickListener customPopClickListener, boolean isCancelable) {
        try {
            this.popClickListener = customPopClickListener;
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            CustomTextView tvTitle, tvMsg;
            Button tvCancel, tvOK;
            LinearLayout llBtn;

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.layout_dialog_common, null);
            llBtn = (LinearLayout) view.findViewById(R.id.ll_btn);
            tvCancel = (Button) view.findViewById(R.id.tv_cancel);
            tvOK = (Button) view.findViewById(R.id.tv_ok);
            tvTitle = (CustomTextView) view.findViewById(R.id.tv_title);
            tvMsg = (CustomTextView) view.findViewById(R.id.tv_msg);
            dialog.setContentView(view);
            dialog.setCancelable(isCancelable);
//            WindowManager.LayoutParams windowLayout = dialog.getWindow().getAttributes();
//            windowLayout.gravity = Gravity.CENTER;
//            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                    mContext);
//
//            alertDialogBuilder.setCancelable(false);
//            alertDialogBuilder.setView(view);
//
//            alertDialogBuilder.setCancelable(isCancelable);

            tvCancel.setText(cancel);
            tvOK.setText(ok);
            tvTitle.setText(title);
            tvMsg.setText(message);

            if (cancel.equalsIgnoreCase("")) {
                // llBtn.setWeightSum(1);
                tvCancel.setVisibility(View.GONE);
            }

            if (title.equalsIgnoreCase("")) {
                tvTitle.setVisibility(View.GONE);
            }
            // create alert dialog

//            final AlertDialog alertDialog = alertDialogBuilder.create();
//
//            //alertDialogBuilder.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//            WindowManager.LayoutParams windowLayout = alertDialog.getWindow().getAttributes();
//            windowLayout.gravity = Gravity.CENTER;
//
//            // show it
//            alertDialog.show();
            dialog.show();
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (popClickListener != null)
                        popClickListener.onCancel();
                }
            });

            tvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (popClickListener != null)
                        popClickListener.onOkay();
                }
            });

        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }


}
