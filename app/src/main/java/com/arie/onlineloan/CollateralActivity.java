package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CollateralActivity extends AppCompatActivity {
    ProgressDialog loading;
    private LinearLayout llCm,llStnk, llBpkb, llLoanInput;
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
    private Spinner spMerk, spType, spManufactureYear, spHouseOwner;
    private RadioGroup rgCollateral;

    private ArrayAdapter<String> adapterMerk, adapterType, adapterYear, adapterHouseOwner;
    private ArrayList<String> dataMerk = new ArrayList<>();
    private ArrayList<String> dataType = new ArrayList<>();
    private ArrayList<String> dataYear = new ArrayList<>();
    private ArrayList<String> dataHouseOwner = new ArrayList<>();

    private String minLoanAmount;
    private String maxLoanAmount;
    private String minLoanTime;
    private String maxLoanTime;

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
        spMerk = findViewById(R.id.sp_merk);
        spType = findViewById(R.id.sp_type);
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
        llLoanInput.setVisibility(View.GONE);
        tvName.setText(user.getFullname());
        tvNik.setText(user.getNik());

        dataHouseOwner.add("Select House Owner");
        dataHouseOwner.add("Borrower");
        dataHouseOwner.add("Spouse");
        dataHouseOwner.add("Child");
        dataHouseOwner.add("Parent");
        dataMerk.add("Select Merk");
        dataYear.add("Select Manufacture Year");
        dataType.add("Select Type");

        adapterHouseOwner = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dataHouseOwner);
        spHouseOwner.setAdapter(adapterHouseOwner);
        adapterMerk = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dataMerk);
        spMerk.setAdapter(adapterMerk);
        adapterType = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dataType);
        spType.setAdapter(adapterType);
        adapterYear = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dataYear);
        spManufactureYear.setAdapter(adapterYear);

        rgCollateral.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_car:
                        llCm.setVisibility(View.VISIBLE);
                        llHouse.setVisibility(View.GONE);
                        break;
                    case R.id.rb_motorcycle:
                        llCm.setVisibility(View.VISIBLE);
                        llHouse.setVisibility(View.GONE);
                        break;
                    case R.id.rb_house:
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
        //getCarModel();
        //checkHouseLoan();
        checkVehicleLoan();
    }

    private void getCarBrand() {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, phpConf.URL_GET_CAR_MODEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json getCarBrand", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        String id = jo.getString("ID");
                        String brand = jo.getString("BRAND");

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
        });
        mRequestQueue.add(mStringRequest);
    }

    private void getCarModel() {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_GET_CAR_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json getCarModel", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        String id = jo.getString("ID");
                        String brand = jo.getString("BRAND");
                        String model = jo.getString("MODEL");
                        String year = jo.getString("YEAR");
                        String price = jo.getString("PRICE");
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
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                //todo change this
                params.put("BRAND", "Honda");
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void getMotorBrand() {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, phpConf.URL_GET_MOTOR_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json getMotorBrand", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        String id = jo.getString("ID");
                        String brand = jo.getString("BRAND");

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
        });
        mRequestQueue.add(mStringRequest);
    }

    private void getMotorModel() {
        loading = ProgressDialog.show(CollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CollateralActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_GET_MOTOR_MODEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json getCarBrand", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        String id = jo.getString("ID");
                        String brand = jo.getString("BRAND");
                        String model = jo.getString("MODEL");
                        String year = jo.getString("YEAR");
                        String price = jo.getString("PRICE");
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
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                //todo change this
                params.put("BRAND", "Honda");
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
