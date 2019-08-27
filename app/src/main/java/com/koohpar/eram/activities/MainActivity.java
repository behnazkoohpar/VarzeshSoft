package com.koohpar.eram.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.koohpar.eram.R;
import com.koohpar.eram.api.IAPIConstantants;
import com.koohpar.eram.api.IApiUrls;
import com.koohpar.eram.tools.AppConstants;
import com.koohpar.eram.tools.CommonMethods;
import com.koohpar.eram.tools.CustomTypefaceSpan;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAPIConstantants, IApiUrls {

    private ImageView imgMenu;
    private CircleImageView imagepic, image;
    public static HashMap<String, Typeface> typefaceHashMap = new HashMap<>();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button myListService;
    private ImageView btnCard;
    private RelativeLayout kartable;
    private Typeface typeface;
    private TextView toolbarTitr;
    private byte[] byteArray;
    private ProgressDialog prgDialog;
    private TextView countMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("لطفا منتظر بمانید ...");
        prgDialog.setCancelable(false);
        AppConstants.SERVER_IP =  LoginActivity.getSavedObjectFromPreference(MainActivity.this,"ERAM","ServerAddress",String.class);
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);
        flipper.startFlipping();
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        navigationView = (NavigationView) findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(this);
        ((NavigationMenuView) navigationView.getChildAt(0)).setPadding(0, 500, 0, 20);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        btnCard = (ImageView) findViewById(R.id.btnCard);
        countMessage = (TextView) findViewById(R.id.countMessage);
        image = (CircleImageView) findViewById(R.id.image);
        toolbarTitr = (TextView) findViewById(R.id.toolbarTitr);
        toolbarTitr.setText(LoginActivity.getSavedObjectFromPreference(MainActivity.this, "ERAM", "PersonName", String.class));
        myListService = (Button) findViewById(R.id.myListService);
        myListService.setTypeface(typeface);
        kartable = (RelativeLayout) findViewById(R.id.kartable);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCountMessage();
                String jsonObject = LoginActivity.getSavedObjectFromPreference(MainActivity.this, "ERAM", "PersonImage", String.class);
                if (jsonObject != null && !jsonObject.isEmpty() && !jsonObject.equalsIgnoreCase("0")) {
                    byte[] decodedString = Base64.decode(jsonObject, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image.setImageBitmap(decodedByte);
                }
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MembershipFileCard.class));
            }

        });
        myListService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListOrderActivity.class));
            }
        });
        kartable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListMessageActivity.class));
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkForCameraPermission()) {
                        openCaptureDialog();
                    }
                } else {
                    openCaptureDialog();
                }
            }
        });
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image.setImageURI(resultUri);
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                float ratio = (float) bmp.getWidth() / (float) bmp.getHeight();
                int w = bmp.getWidth() > bmp.getHeight() ? 512 : (int) (512 * ratio);
                int h = bmp.getWidth() < bmp.getHeight() ? 512 : (int) (512 * ratio);
                Bitmap resize = bmp.createScaledBitmap(bmp, w, h, false);
                resize.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArray = stream.toByteArray();
                callSetImageUser(byteArray);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @TargetApi(23)
    boolean checkForCameraPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA)) {
                CommonMethods.showTwoButtonAlert(MainActivity.this, getString(R.string.permission_required), getString(R.string.app_need_access_camera), getString(R.string.pop_up_ok), getString(R.string.cancel), new CommonMethods.IL() {
                    @Override
                    public void onSuccess() {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                AppConstants.MY_PERMISSIONS_REQUEST_CAMERA);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                return false;
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    AppConstants.MY_PERMISSIONS_REQUEST_CAMERA);

            return false;
        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CommonMethods.showToast(MainActivity.this, getString(R.string.permission_granted));
                    openCaptureDialog();

                } else {
                    CommonMethods.showToast(MainActivity.this, getString(R.string.permission_not_granted));
                }
                break;
        }
    }

    public void openCaptureDialog() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    public void callSetImageUser(byte[] bytes) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PHONE, LoginActivity.getSavedObjectFromPreference(MainActivity.this, "ERAM", "PhoneNumber", String.class));
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(MainActivity.this, "ERAM", "PersonID", String.class));
            final String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);

            params.put(REQUEST_IMAGE, encodedImage);
            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("MainActivity", response.toString());
                    try {
                        prgDialog.hide();
                        JSONObject jsonObject = response;
                        if (jsonObject.getBoolean("status")) {
                            LoginActivity.saveObjectToSharedPreference(MainActivity.this, "ERAM", "PersonImage", encodedImage);
                        } else {
                            CommonMethods.showSingleButtonAlert(MainActivity.this, getString(R.string.txt_attention), jsonObject.getString("errmessage"), getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
                        prgDialog.hide();
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prgDialog.hide();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( AppConstants.SERVER_IP +URL_SET_IMAGE_USER, params, listener, errorListener);
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

    private void callCountMessage() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put(REQUEST_PERSON_ID, LoginActivity.getSavedObjectFromPreference(MainActivity.this, "ERAM", "PersonID", String.class));
//            prgDialog.show();
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("LisMessage", response.toString());
                    try {
                        if (response.getString("status").equalsIgnoreCase("true")) {
                            countMessage.setText(response.getString("count"));
                        } else {
                            CommonMethods.showSingleButtonAlert(MainActivity.this, getString(R.string.txt_attention), response.getString("errmessage"), getString(R.string.pop_up_ok));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    prgDialog.hide();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( AppConstants.SERVER_IP +URL_GET_COUNT_MESSAGE_FROM_ADMIN, params, listener, errorListener);
            int socketTimeout = 5000; // 5 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
//            prgDialog.hide();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_sms) {
            startActivity(new Intent(MainActivity.this, ListOrderActivity.class));
        }
        if (id == R.id.nav_request) {
            startActivity(new Intent(MainActivity.this, RequestActivity.class));
        }
        if (id == R.id.nav_cash) {
            startActivity(new Intent(MainActivity.this, TransactionActivity.class));
        }
        if (id == R.id.nav_market) {
            CommonMethods.showSingleButtonAlert(MainActivity.this, getString(R.string.txt_attention), "این سرویس در حال حاضر فعال نیست.", null);
        }
        if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }

        if (id == R.id.nav_setting) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        }
        if (id == R.id.nav_exit) {

            LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "PhoneNumber", "");
            LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "PassWord", "");
            LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "PersonName", "");
            LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "PersonID", "");
            LoginActivity.saveObjectToSharedPreference(getApplicationContext(), "ERAM", "CardNumber", "");

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl);
        drawer.closeDrawer(Gravity.RIGHT);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
