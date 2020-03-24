package com.arie.onlineloan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

public class TransactionDetailActivity extends AppCompatActivity {

    private static String ORIGIN_APPLY = "apply";
    private static String ORIGIN_USER = "list";
    private static String ORIGIN_ADMIN = "admin";
    private String transId;
    private String origin;
    private String transType;

    private ProgressDialog loading;

    private LinearLayout llApproval;
    private Button btnReject, btnApprove;

    private TextView tvCollateral;
    private TextView tvName, tvNik, tvAccountNumber;
    private TextView tvLoanAmount, tvTimePeriod, tvInstallment, tvStatus, tvLoanTotal;

    private CardView cvHouse;
    private TextView tvLocation, tvAreaSize, tvEstimatedPrice;
    private ImageView imgHouseCertificate, imgHousePhoto;

    private CardView cvVehicle;
    private TextView tvBrand, tvModel, tvManufactureYear;
    private ImageView imgStnk, imgBpkb;

    private CardView cvPayment;

    private DecimalFormat df = new DecimalFormat("#,###");

    private int loanAmount;
    private int loanInterest;
    private int loanTime;
    private int loanInstallment;
    private int loanTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        Intent intent = getIntent();
        transId = intent.getStringExtra("transId");
        origin = intent.getStringExtra("origin");
        transType = intent.getStringExtra("transType");

        llApproval = findViewById(R.id.ll_approval);
        btnApprove = findViewById(R.id.btn_approve);
        btnReject = findViewById(R.id.btn_reject);

        tvCollateral = findViewById(R.id.txt_collateral);
        tvName = findViewById(R.id.txt_name);
        tvNik = findViewById(R.id.txt_nik);
        tvAccountNumber = findViewById(R.id.txt_account_number);
        tvLoanAmount = findViewById(R.id.txt_loan_amount);
        tvTimePeriod = findViewById(R.id.txt_time_period);
        tvInstallment = findViewById(R.id.txt_installment);
        tvStatus = findViewById(R.id.txt_status);
        tvLoanTotal = findViewById(R.id.txt_loan_total);

        cvHouse = findViewById(R.id.cv_house);
        tvLocation = findViewById(R.id.txt_location);
        tvAreaSize = findViewById(R.id.txt_area_size);
        tvEstimatedPrice = findViewById(R.id.txt_estimated_price);
        imgHouseCertificate = findViewById(R.id.img_house_certificate);
        imgHousePhoto = findViewById(R.id.img_house_photo);

        cvVehicle = findViewById(R.id.cv_vehicle);
        tvBrand = findViewById(R.id.txt_brand);
        tvModel = findViewById(R.id.txt_model);
        tvManufactureYear = findViewById(R.id.txt_manufacture_year);
        imgBpkb = findViewById(R.id.img_bpkb);
        imgStnk = findViewById(R.id.img_stnk);

        cvPayment = findViewById(R.id.cv_payment);

        if (origin.equals(ORIGIN_APPLY)) {
            llApproval.setVisibility(View.GONE);
            cvPayment.setVisibility(View.VISIBLE);
        } else if (origin.equals(ORIGIN_USER)) {
            llApproval.setVisibility(View.GONE);
            cvPayment.setVisibility(View.VISIBLE);
        } else if (origin.equals(ORIGIN_ADMIN)) {
            llApproval.setVisibility(View.VISIBLE);
            cvPayment.setVisibility(View.GONE);
        }

        reqTransDetail();
    }

    private void reqTransDetail() {
        loading = ProgressDialog.show(TransactionDetailActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(TransactionDetailActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_GET_DETAIL_TRANSACTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json getDetailTrans", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        String ID = jo.getString("ID");
                        String status = jo.getString("STATUS");
                        String fullname = jo.getString("FULLNAME");
                        String nik = jo.getString("NIK");
                        String bankUserId = jo.getString("BANK_USER_ID");
                        String loanType = jo.getString("LOAN_TYPE");
                        String loanAmountStr = jo.getString("LOAN_AMOUNT");
                        String timePeriodStr = jo.getString("PERIOD_TIME");
                        String loanTotalStr = jo.getString("TOTAL_LOAN");
                        String installmentStr = jo.getString("INSTALLMENT");

                        loanAmount = Integer.parseInt(loanAmountStr);
                        loanTime = Integer.parseInt(timePeriodStr);
                        loanTotal = Integer.parseInt(loanTotalStr);
                        loanInstallment = Integer.parseInt(installmentStr);

                        tvStatus.setText(status);
                        tvName.setText(fullname);
                        tvNik.setText(nik);
                        tvAccountNumber.setText(bankUserId);
                        tvLoanAmount.setText(getString(R.string.con_amount,df.format(loanAmount)));
                        tvTimePeriod.setText(getString(R.string.con_month,loanTime));
                        tvLoanTotal.setText(getString(R.string.con_amount,df.format(loanTotal)));
                        tvInstallment.setText(getString(R.string.con_amount,df.format(loanInstallment)));

                        if(loanType.equals("Non Collateral")){
                            cvHouse.setVisibility(View.GONE);
                            cvVehicle.setVisibility(View.GONE);
                            tvCollateral.setVisibility(View.GONE);
                        }else if(loanType.equals("Collateral Motorcycle") || loanType.equals("Collateral Car")){
                            cvHouse.setVisibility(View.GONE);
                            String vehicleType = jo.getString("VEHICLE_TYPE");
                            String brand = jo.getString("BRAND");
                            String model = jo.getString("MODEL");
                            String manufactureYear = jo.getString("MANUFACTURE_YEAR");
                            String stnk = jo.getString("STNK");
                            String bpkb = jo.getString("BPKB");

                            tvBrand.setText(brand);
                            tvModel.setText(model);
                            tvManufactureYear.setText(manufactureYear);
                            //todo set image for stnk bpkb

                        }else if(loanType.equals("Collateral House")){
                            cvVehicle.setVisibility(View.GONE);
                            String location = jo.getString("LOCATION");
                            String areaSize = jo.getString("AREA_SIZE");
                            String estimatedPrice = jo.getString("ESTIMATED_PRICE");
                            String houseCertificate = jo.getString("HOUSE_CERTIFICATE");
                            String housePhoto = jo.getString("HOUSE_PHOTO");

                            tvLocation.setText(location);
                            tvAreaSize.setText(areaSize);
                            tvEstimatedPrice.setText(estimatedPrice);
                            //todo set image for certificate and photo
                        }

                    } else {
                        loading.dismiss();
                        String message = jo.getString("message");
                        Toast.makeText(TransactionDetailActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TransactionDetailActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("TRX_ID", transId);
                params.put("TRX_TYPE", transType);
                Log.d("tag", params.toString());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }
}
