package com.arie.onlineloan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arie.onlineloan.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.Manifest.permission.CAMERA;

public class CollateralActivity extends AppCompatActivity {
    private ProgressDialog loading;
    private static final int PIC_ID = 123;
    private static final int REQUEST_CAMERA = 1;

    private LinearLayout llCm, llStnk, llBpkb, llLoanInput;
    private LinearLayout llHouse, llHousePhoto, llHouseCertificate;
    private TextView tvName, tvNik, tvDetail;
    private TextView tvCollateralCm, tvUploadStnk, tvUploadBpkb;
    private TextView tvCollateralHouse, tvLocation, tvSize, tvEstimatedPrice;
    private TextView tvUploadHousePhoto, tvUploadHouseCertificate;
    private TextView tvLoanInput;
    private SeekBar sbTime, sbAmount;
    private TextView tvMinAmount, tvMaxAmount, tvMinTime, tvMaxTime, tvTimePeriodEdit, tvAmountEdit;
    private TextView tvLoanAmount, tvTimePeriod, tvInterest, tvTotalLoan;
    private TextView tvInstallment;
    private CheckBox cbTerms;
    private Button btnApply;

    private Button btnCheckLoanCm, btnCheckLoanHouse;
    private ImageView imgStnk, imgBpkb;
    private ImageView imgHousePhoto, imgHouseCertificate;
    private Spinner spBrand, spModel, spManufactureYear, spHouseOwner;
    private RadioGroup rgCollateral;

    private ArrayAdapter<String> adapterBrand, adapterModel, adapterYear, adapterHouseOwner;
    private ArrayList<String> dataBrand = new ArrayList<>();
    private ArrayList<String> dataModel = new ArrayList<>();
    private ArrayList<String> dataYear = new ArrayList<>();
    private ArrayList<String> dataHouseOwner = new ArrayList<>();

    private Uri selectedImage;

    private String minLoanAmount;
    private String maxLoanAmount;
    private String minLoanTime;
    private String maxLoanTime;

    private String imageType;

    private int loanAmount;
    private int loanInterest;
    private int loanTime;
    private int loanInstallment;
    private int loanTotal;
    private int minLoanAmountInt;
    private int maxLoanAmountInt;
    private int minLoanTimeInt;
    private int maxLoanTimeInt;
    private double interestDouble;
    private String vehicleType;

    private ArrayList<String> listCarBrand = new ArrayList<>();
    private ArrayList<String> listMotorBrand = new ArrayList<>();
    private ArrayList<ArrayList<HashMap<String, String>>> listCar = new ArrayList<>();
    private ArrayList<ArrayList<HashMap<String, String>>> listMotor = new ArrayList<>();

    User user;
    UserPreference mUserPreference;

    private DecimalFormat df = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collateral);

        llLoanInput = findViewById(R.id.ll_loan_input);
        llCm = findViewById(R.id.ll_cm);
        llStnk = findViewById(R.id.ll_stnk);
        llBpkb = findViewById(R.id.ll_bpkb);
        llHouse = findViewById(R.id.ll_house);
        llHousePhoto = findViewById(R.id.ll_house_photo);
        llHouseCertificate = findViewById(R.id.ll_house_certificate);
        tvName = findViewById(R.id.txt_name);
        tvNik = findViewById(R.id.txt_nik);
        tvDetail = findViewById(R.id.txt_detail);
        tvCollateralCm = findViewById(R.id.txt_collateral_cm);
        tvUploadStnk = findViewById(R.id.txt_upload_stnk);
        tvUploadBpkb = findViewById(R.id.txt_upload_bpkb);
        tvCollateralHouse = findViewById(R.id.txt_collateral_house);
        tvLocation = findViewById(R.id.txt_location);
        tvSize = findViewById(R.id.txt_size);
        tvEstimatedPrice = findViewById(R.id.txt_estimated_price);
        tvUploadHousePhoto = findViewById(R.id.txt_upload_house_photo);
        tvUploadHouseCertificate = findViewById(R.id.txt_upload_house_certificate);
        btnCheckLoanCm = findViewById(R.id.btn_check_loan_cm);
        btnCheckLoanHouse = findViewById(R.id.btn_check_loan_house);
        imgStnk = findViewById(R.id.img_stnk);
        imgBpkb = findViewById(R.id.img_bpkb);
        imgHousePhoto = findViewById(R.id.img_house);
        imgHouseCertificate = findViewById(R.id.img_house_certificate);
        rgCollateral = findViewById(R.id.rg_collateral);
        spHouseOwner = findViewById(R.id.sp_house_owner);
        spBrand = findViewById(R.id.sp_merk);
        spModel = findViewById(R.id.sp_type);
        spManufactureYear = findViewById(R.id.sp_manufacture_year);

        sbTime = findViewById(R.id.sb_time);
        sbAmount = findViewById(R.id.sb_amount);
        tvMinAmount = findViewById(R.id.txt_min_amount);
        tvMaxAmount = findViewById(R.id.txt_max_amount);
        tvMinTime = findViewById(R.id.txt_min_time);
        tvMaxTime = findViewById(R.id.txt_max_time);
        tvTimePeriodEdit = findViewById(R.id.txt_time_period_edit);
        tvAmountEdit = findViewById(R.id.txt_amount_edit);
        tvLoanAmount = findViewById(R.id.txt_loan_amount);
        tvTimePeriod = findViewById(R.id.txt_time_period);
        tvInterest = findViewById(R.id.txt_interest);
        tvTotalLoan = findViewById(R.id.txt_total_loan);
        tvInstallment = findViewById(R.id.txt_installment);
        cbTerms = findViewById(R.id.cb_terms);
        btnApply = findViewById(R.id.btn_apply);

        mUserPreference = new UserPreference(this);
        user = mUserPreference.getUser();

        llCm.setVisibility(View.GONE);
        llHouse.setVisibility(View.GONE);
        llLoanInput.setVisibility(View.GONE);
        tvName.setText(user.getFullname());
        tvNik.setText(user.getNik());

        dataHouseOwner.add("Select House Owner");
        dataHouseOwner.add("Borrower");
        dataHouseOwner.add("Spouse");
        dataHouseOwner.add("Child");
        dataHouseOwner.add("Parent");

        adapterHouseOwner = new ArrayAdapter<>(CollateralActivity.this, android.R.layout.simple_spinner_item, dataHouseOwner);
        adapterBrand = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataBrand);
        adapterModel = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataModel);
        adapterYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataYear);

        adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spHouseOwner.setAdapter(adapterHouseOwner);
        spBrand.setAdapter(adapterBrand);
        spModel.setAdapter(adapterModel);
        spManufactureYear.setAdapter(adapterYear);

        spBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spBrand.getSelectedItemPosition()!=0){
                    getVehicleModel(spBrand.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        imgStnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType="imgStnk";
                showPictureDialog(imageType);
            }
        });

        imgBpkb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType="imgBpkb";
            }
        });

        imgHouseCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType="imgHC";
            }
        });

        imgHousePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType="imgHP";
            }
        });

        rgCollateral.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dataBrand.clear();
                dataModel.clear();
                dataYear.clear();

                dataBrand.add("Select Brand");
                dataYear.add("Select Manufacture Year");
                dataModel.add("Select Type");

                switch (checkedId) {
                    case R.id.rb_car:
                        vehicleType = "Car";
                        getVehicleBrand(vehicleType);
                        tvCollateralCm.setText(vehicleType);
                        llCm.setVisibility(View.VISIBLE);
                        llHouse.setVisibility(View.GONE);
                        break;
                    case R.id.rb_motorcycle:
                        vehicleType = "Motorcycle";
                        getVehicleBrand(vehicleType);
                        tvCollateralCm.setText(vehicleType);
                        llCm.setVisibility(View.VISIBLE);
                        llHouse.setVisibility(View.GONE);
                        break;
                    case R.id.rb_house:
                        tvCollateralHouse.setText("House");
                        llCm.setVisibility(View.GONE);
                        llHouse.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inProfile = new Intent(CollateralActivity.this, ProfileActivity.class);
                startActivity(inProfile);
            }
        });

        btnCheckLoanCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //getCarBrand();
        //getVehicleBrand("Car");
        //getCarModel();
        //checkHouseLoan();
        //checkVehicleLoan();
    }

    private void getVehicleBrand(final String type) {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_GET_VEHICLE_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    dataBrand.clear();
                    dataBrand.add("Select Brand");
                    Log.d("Json getVehicleBrand", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        JSONArray brands = jo.getJSONArray("BRAND");
                        for (int i = 0; i < brands.length(); i++) {
                            JSONObject item = brands.getJSONObject(i);
                            dataBrand.add(item.getString("BRAND"));
                        }
                        adapterBrand.notifyDataSetChanged();
                    } else {
                        loading.dismiss();
                        String message = jo.getString("message");
                        Toast.makeText(CollateralActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    loading.dismiss();
                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.d("tag", String.valueOf(error));
                Toast.makeText(CollateralActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("TYPE", type);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void getVehicleModel(final String brand) {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_GET_VEHICLE_MODEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    dataModel.clear();
                    dataYear.clear();

                    dataYear.add("Select Manufacture Year");
                    dataModel.add("Select Type");
                    Log.d("Json getVehicleModel", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        JSONArray brands = jo.getJSONArray("data");
                        for (int i = 0; i < brands.length(); i++) {
                            JSONObject item = brands.getJSONObject(i);
                            String id = item.getString("ID");
                            String brand = item.getString("BRAND");
                            String model = item.getString("MODEL");
                            String year = item.getString("YEAR");
                            String price = item.getString("PRICE");

                            dataModel.add(model);
                            dataYear.add(year);
                        }
                        adapterBrand.notifyDataSetChanged();
                        adapterModel.notifyDataSetChanged();
                        adapterYear.notifyDataSetChanged();
                    } else {
                        loading.dismiss();
                        String message = jo.getString("message");
                        Toast.makeText(CollateralActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    loading.dismiss();
                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.d("tag", String.valueOf(error));
                Toast.makeText(CollateralActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("BRAND", brand);
                params.put("TYPE", vehicleType);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void checkHouseLoan() {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_CHECK_COLLATERAL_HOUSE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json checkHouseLoan", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        minLoanAmount = jo.getString("MIN_LOAN_AMOUNT");
                        maxLoanAmount = jo.getString("MAX_LOAN_AMOUNT");
                        minLoanTime = jo.getString("MIN_LOAN_TIME");
                        maxLoanTime = jo.getString("MAX_LOAN_TIME");
                        String interest = jo.getString("INTEREST");

                        interestDouble = Double.parseDouble(interest);
                        minLoanAmountInt = Integer.parseInt(minLoanAmount);
                        maxLoanAmountInt = Integer.parseInt(maxLoanAmount);
                        minLoanTimeInt = Integer.parseInt(minLoanTime);
                        maxLoanTimeInt = Integer.parseInt(maxLoanTime);

                        tvMinAmount.setText(getString(R.string.min_loan_amount, df.format(minLoanAmountInt)));
                        tvMaxAmount.setText(getString(R.string.max_loan_amount, df.format(maxLoanAmountInt)));
                        tvMinTime.setText(getString(R.string.min_loan_time, minLoanTime));
                        tvMaxTime.setText(getString(R.string.max_loan_time, maxLoanTime));
                        sbAmount.setProgress(100);
                        sbAmount.setProgress(100);
                    } else {
                        String message = jo.getString("message");
                        Toast.makeText(CollateralActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.d("tag", String.valueOf(error));
                Toast.makeText(CollateralActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                //TODO CHANGE PRICE
                params.put("PRICE", "100000000");
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void checkVehicleLoan() {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_CHECK_COLLATERAL_VEHICLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json checVehicleLoan", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        minLoanAmount = jo.getString("MIN_LOAN_AMOUNT");
                        maxLoanAmount = jo.getString("MAX_LOAN_AMOUNT");
                        minLoanTime = jo.getString("MIN_LOAN_TIME");
                        maxLoanTime = jo.getString("MAX_LOAN_TIME");
                        String interest = jo.getString("INTEREST");

                        interestDouble = Double.parseDouble(interest);
                        minLoanAmountInt = Integer.parseInt(minLoanAmount);
                        maxLoanAmountInt = Integer.parseInt(maxLoanAmount);
                        minLoanTimeInt = Integer.parseInt(minLoanTime);
                        maxLoanTimeInt = Integer.parseInt(maxLoanTime);

                        tvMinAmount.setText(getString(R.string.min_loan_amount, df.format(minLoanAmountInt)));
                        tvMaxAmount.setText(getString(R.string.max_loan_amount, df.format(maxLoanAmountInt)));
                        tvMinTime.setText(getString(R.string.min_loan_time, minLoanTime));
                        tvMaxTime.setText(getString(R.string.max_loan_time, maxLoanTime));
                        sbAmount.setProgress(100);
                        sbAmount.setProgress(100);
                    } else {
                        String message = jo.getString("message");
                        Toast.makeText(CollateralActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.d("tag", String.valueOf(error));
                Toast.makeText(CollateralActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                //TODO CHANGE PRICE
               /* params.put("BRAND", "Daihatsu");
                params.put("MODEL", "Xenia");
                params.put("YEAR", "2016");
                params.put("TYPE", "Car");*/

                params.put("BRAND", "Honda");
                params.put("MODEL", "Revo");
                params.put("YEAR", "2016");
                params.put("TYPE", "Motorcycle");
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void applyCollateral() {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_CHECK_NON_COLLATERAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json checkNonCollateral", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                       /* minLoanAmount = jo.getString("MIN_LOAN_AMOUNT");
                        maxLoanAmount = jo.getString("MAX_LOAN_AMOUNT");
                        minLoanTime = jo.getString("MIN_LOAN_TIME");
                        maxLoanTime = jo.getString("MAX_LOAN_TIME");
                        String interest = jo.getString("INTEREST");

                        interestDouble = Double.parseDouble(interest);
                        minLoanAmountInt = Integer.parseInt(minLoanAmount);
                        maxLoanAmountInt = Integer.parseInt(maxLoanAmount);
                        minLoanTimeInt = Integer.parseInt(minLoanTime);
                        maxLoanTimeInt = Integer.parseInt(maxLoanTime);

                        tvMinAmount.setText(getString(R.string.min_loan_amount, df.format(minLoanAmountInt)));
                        tvMaxAmount.setText(getString(R.string.max_loan_amount, df.format(maxLoanAmountInt)));
                        tvMinTime.setText(getString(R.string.min_loan_time, minLoanTime));
                        tvMaxTime.setText(getString(R.string.max_loan_time, maxLoanTime));
                        sbAmount.setProgress(100);
                        sbAmount.setProgress(100);*/

                    } else {
                        String message = jo.getString("message");
                        Toast.makeText(CollateralActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.d("tag", String.valueOf(error));
                Toast.makeText(CollateralActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("USER_ID", user.getUserId());
                params.put("LOAN_TYPE", user.getUserId());
                params.put("LOAN_AMOUNT", user.getUserId());
                params.put("TIME_PERIOD", user.getUserId());
                params.put("INTEREST", user.getUserId());
                params.put("ADDRESS", user.getUserId());
                params.put("INSTALLMENT", user.getUserId());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void takePhotoFromCamera(String type) {
        if(checkPermission()){
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, PIC_ID);
        }else{
            Toast.makeText(CollateralActivity.this, "Please allow camera permission!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void showPictureDialog(final String type){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary(type);
                                break;
                            case 1:
                                takePhotoFromCamera(type);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary(String type) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent,0);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Bitmap photoBitmap;
            if (requestCode == 0) {
                selectedImage = data.getData();
                try {
                    photoBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    imgStnk.setImageBitmap(photoBitmap);
                    BitmapHelper.getInstance().setBitmap(photoBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PIC_ID) {
                photoBitmap = (Bitmap) data.getExtras().get("data");
                imgStnk.setImageBitmap(photoBitmap);
                BitmapHelper.getInstance().setBitmap(photoBitmap);
            }
        }
    }

    public  boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 1);                return false;
            }
        }
        else {
            return true;
        }
    }

   /* private void calculateLoan(){
        double interest = interestDouble* loanTime*loanAmount;
        loanInterest = (int) interest;
        loanTotal = loanAmount+ loanInterest;
        loanInstallment = loanTotal/loanTime;

        tvInterest.setText(getString(R.string.con_amount, df.format(loanInterest)));
        tvTotalLoan.setText(getString(R.string.con_amount, df.format(loanTotal)));
        tvInstallment.setText(getString(R.string.con_amount, df.format(loanInstallment)));
    }*/
}
