package com.koohpar.eram.tools;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.koohpar.eram.R;
import com.koohpar.eram.custom.CustomTextView;

public class CommonMethods {

    private static final int DEFAULT_ANIM_DURATION = 600;

    public static void showToast(Activity context, String message) {
        //Creating the LayoutInflater instance
        LayoutInflater li = context.getLayoutInflater();
        //Getting the View object as defined in the layout_custom_toast.xml file
        View layout = li.inflate(R.layout.layout_custom_toast,
                (ViewGroup) context.findViewById(R.id.custom_toast_layout));
        ((TextView) layout.findViewById(R.id.toast_msg)).setText(message);
        //Creating the Toast object
        Toast toast = new Toast(context.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();
    }

    public static boolean isTextContainOnlyNumber(String text) {
        return text.matches("^[+]?[0-9]+");
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqheight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqheight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqheight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void collapse(final View v) {
        collapse(v, false);
    }

    public static void expand(final View v) {
        expand(v, false);
    }

    public static void expand(final View v, boolean isFastAnimation) {
        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? ActionBar.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        int duration = ((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 4;
        a.setDuration(isFastAnimation ? DEFAULT_ANIM_DURATION : duration);
        v.startAnimation(a);
    }

    public static void collapse(final View v, boolean isFastAnimation) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        int duration = ((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)) * 4;
        a.setDuration(isFastAnimation ? DEFAULT_ANIM_DURATION : duration);
        v.startAnimation(a);
    }


    //*********************SETUP FOR HANDLING DIALOG VIEWS***********************
    private static Dialog dialog;

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static boolean isDialogOpen() {
        if (dialog != null)
            return dialog.isShowing();
        else
            return false;
    }

    public static void showDialogWithView(Context activity, final View view) {
        showDialogWithView(activity, view, null);
    }

    public static void showDialogWithView(Context activity, final View view, final IL listner) {
        try {
            dismissDialog();
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        if (listner != null)
                            listner.onCancel();
                    }
                    return false;
                }
            });
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showSingleButtonAlert(Context context, String title, String msg, String buttonTitle) {
        showSingleButtonAlert(context, title, msg, buttonTitle, null);
    }

    public static void showSingleButtonAlert(Context context, String title, String msg, String buttonTitle, final IL listner) {
        try {

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
            CustomTextView txtDialogTitle, txtDialogMessage;
            Button btnThanksOK;
            View signUpThanksDialog = LayoutInflater.from(context).inflate(R.layout.login_congrats_dialog, null);
            txtDialogTitle = (CustomTextView) signUpThanksDialog.findViewById(R.id.txtDialogTitle);
            txtDialogMessage = (CustomTextView) signUpThanksDialog.findViewById(R.id.txtDialogMessage);
            btnThanksOK = (Button) signUpThanksDialog.findViewById(R.id.btnLoginOK);
            btnThanksOK.setTypeface(typeface);
            if (TextUtils.isEmpty(title))
                txtDialogTitle.setVisibility(View.GONE);
            else {
                txtDialogTitle.setVisibility(View.VISIBLE);
                txtDialogTitle.setText(title);
            }

            if (!TextUtils.isEmpty(msg))
                txtDialogMessage.setText(msg);

            btnThanksOK.setText(TextUtils.isEmpty(buttonTitle) ? context.getString(R.string.pop_up_ok) : buttonTitle);
            btnThanksOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    if (listner != null)
                        listner.onSuccess();
                }
            });
            showDialogWithView(context, signUpThanksDialog, listner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTwoButtonAlert(Context context, String title, String msg, String successButtonTitle, String cancelButtonTitle, final IL listner) {
        try {

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
            TextView txtDialogTitle, txtDialogMessage;
            Button btnVerify, btnCancel;
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.mobile_verification_alert_dialog);
            txtDialogTitle = (TextView) dialog.findViewById(R.id.txtDialogTitle);

            txtDialogMessage = (TextView) dialog.findViewById(R.id.txtDialogMessage);
            btnVerify = (Button) dialog.findViewById(R.id.btnVerify);
            btnVerify.setTypeface(typeface);
            btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            btnCancel.setTypeface(typeface);

            if (TextUtils.isEmpty(title))
                txtDialogTitle.setVisibility(View.GONE);
            else {
                txtDialogTitle.setVisibility(View.VISIBLE);
                txtDialogTitle.setText(title);
            }

            if (!TextUtils.isEmpty(msg))
                txtDialogMessage.setText(msg);
            if (!TextUtils.isEmpty(successButtonTitle))
                btnVerify.setText(successButtonTitle);
            if (!TextUtils.isEmpty(cancelButtonTitle))
                btnCancel.setText(cancelButtonTitle);

            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    if (listner != null)
                        listner.onSuccess();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    if (listner != null)
                        listner.onCancel();
                }
            });
            dialog.show();
//            showDialogWithView(context, signUpThanksDialog, listner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface IL {
        void onSuccess();

        void onCancel();
    }


    public static void testExpand(final View v, final int target) {
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? ActionBar.LayoutParams.WRAP_CONTENT : (int) (target * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        int duration = ((int) (target / v.getContext().getResources().getDisplayMetrics().density)) * 4;
        a.setDuration(duration);
        v.startAnimation(a);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    @SuppressLint("NewApi")
    public static String getRealPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static int getCameraPhotoOrientation(Context context, String imagePath) {
        int rotate = 0;
        try {
            //context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exif.getAttribute(ExifInterface.TAG_DATETIME);


            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    //***********************************************************************

    /**
     * @param activity
     */
    public static void hideSoftKeyBoard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();

            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/taxidarbast/apks");
            if (dir.exists() == false) {
                dir.mkdirs();
            }
            String saveFilePath = dir.getAbsolutePath() + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            httpConn.disconnect();

            System.out.println("File downloaded");
            return saveFilePath;

        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            httpConn.disconnect();
            return "none";

        }

    }

    public static int getVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }
}

