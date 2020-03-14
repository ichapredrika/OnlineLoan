package com.arie.onlineloan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arie.onlineloan.models.User;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

public class NonCollateralActivity extends AppCompatActivity {
    ProgressDialog loading;
    private TextView tvName, tvNik, tvDetail;
    private TextInputEditText tvLoanInput;
    private SeekBar sbTime, sbAmount;
    private TextView tvMinAmount, tvMaxAmount, tvMinTime, tvMaxTime, tvTimePeriodEdit, tvAmountEdit;
    private TextView tvLoanAmount, tvTimePeriod, tvInterest, tvTotalLoan;
    private TextView tvInstallment;
    private CheckBox cbTerms;
    private Button btnApply;
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

    private DecimalFormat df = new DecimalFormat("#,###");

    User user;
    UserPreference mUserPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_collateral);

        mUserPreference = new UserPreference(this);
        user = mUserPreference.getUser();

        tvName = findViewById(R.id.txt_name);
        tvNik = findViewById(R.id.txt_nik);
        tvDetail = findViewById(R.id.txt_detail);
        //tvLoanInput = findViewById(R.id.txt_input_amount);
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

        tvName.setText(user.getFullname());
        tvNik.setText(user.getNik());

        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inProfile = new Intent(NonCollateralActivity.this, ProfileActivity.class);
                startActivity(inProfile);
            }
        });

       /* tvLoanInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("") || s.toString().length()!=0){
                    int amount = Integer.parseInt(s.toString());
                    Toast.makeText(NonCollateralActivity.this, s,Toast.LENGTH_SHORT).show();
                    tvLoanAmount.setText(getString(R.string.con_amount, df.format(amount)));
                    loanAmount = amount;
                    if (!tvTimePeriodEdit.getText().toString().isEmpty()){
                        int interestInt = loanAmount * loanTime;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("") || s.toString().length()!=0){
                    int amount = Integer.parseInt(s.toString());
                    if (amount<minLoanAmountInt && amount>maxLoanAmountInt){
                        tvLoanInput.setError(getString(R.string.msg_between_min_max));
                    }
                }

            }
        });*/

        sbAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int result = (progress* maxLoanAmountInt)/100;
                tvLoanAmount.setText(getString(R.string.con_amount,df.format(result)));
                tvAmountEdit.setText(getString(R.string.con_amount,df.format(result)));
                loanAmount=result;
                if (loanAmount>0 && loanTime>0 ){
                    calculateLoan();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int result = (progress* maxLoanTimeInt)/100;
                tvTimePeriod.setText(getString(R.string.con_month,result));
                tvTimePeriodEdit.setText(getString(R.string.con_month,result));
                loanTime = result;

                if (loanAmount>0 && loanTime>0 ){
                    calculateLoan();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loanAmount<minLoanAmountInt || loanAmount>maxLoanAmountInt){
                    Toast.makeText(NonCollateralActivity.this, getString(R.string.msg_amount_between_min_max), Toast.LENGTH_SHORT).show();
                }else if (loanTime<minLoanTimeInt || loanTime>maxLoanTimeInt){
                    Toast.makeText(NonCollateralActivity.this, getString(R.string.msg_time_between_min_max), Toast.LENGTH_SHORT).show();
                }else if(!cbTerms.isChecked()){
                    Toast.makeText(NonCollateralActivity.this, getString(R.string.msg_check_terms), Toast.LENGTH_SHORT).show();
                }else{
                    applyNonCollateral();
                }
            }
        });
        checkNonCollateral();

    }

    private void checkNonCollateral() {
        loading = ProgressDialog.show(NonCollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(NonCollateralActivity.this);

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
                        Toast.makeText(NonCollateralActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NonCollateralActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("USER_ID", user.getUserId());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void applyNonCollateral() {
        loading = ProgressDialog.show(NonCollateralActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(NonCollateralActivity.this);

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
                        Toast.makeText(NonCollateralActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NonCollateralActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
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

    private void calculateLoan(){
        double interest = interestDouble* loanTime*loanAmount;
        loanInterest = (int) interest;
        loanTotal = loanAmount+ loanInterest;
        loanInstallment = loanTotal/loanTime;

        tvInterest.setText(getString(R.string.con_amount, df.format(loanInterest)));
        tvTotalLoan.setText(getString(R.string.con_amount, df.format(loanTotal)));
        tvInstallment.setText(getString(R.string.con_amount, df.format(loanInstallment)));
    }

}
