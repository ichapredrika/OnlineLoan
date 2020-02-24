package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WithCollateralActivity extends AppCompatActivity {

    private LinearLayout llCm,llStnk, llBpkb;
    private LinearLayout llHouse, llHousePhoto, llHouseCertificate;
    private TextView tvName, tvNik, tvDetail;
    private TextView tvCollateralCm, tvUploadStnk, tvUploadBpkb;
    private TextView tvCollateralHouse, tvLocation, tvSize, tvEstimatedPrice;
    private TextView tvUploadHousePhoto, tvUploadHouseCertificate;
    private TextView tvLoanInput;
    private SeekBar sbTime;
    private TextView minAmount, maxAmount, minTime, maxTime;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_collateral);

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


    }
}
