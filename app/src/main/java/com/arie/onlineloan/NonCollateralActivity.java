package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class NonCollateralActivity extends AppCompatActivity {

    private TextView tvName, tvNik, tvDetail;
    private TextView tvLoanInput;
    private SeekBar sbTime;
    private TextView tvMinAmount, tvMaxAmount, tvMinTime, tvMaxTime;
    private TextView tvLoanAmount, tvTimePeriod, tvInterest, tvTotalLoan;
    private TextView tvInstallment;
    private CheckBox cbTerms;
    private Button btnApply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_collateral);

        tvName = findViewById(R.id.txt_name);
        tvNik = findViewById(R.id.txt_nik);
        tvDetail = findViewById(R.id.txt_detail);
        tvLoanInput = findViewById(R.id.txt_input_amount);
        sbTime = findViewById(R.id.sb_time);
        tvMinAmount = findViewById(R.id.txt_min_amount);
        tvMaxAmount = findViewById(R.id.txt_max_amount);
        tvMinTime = findViewById(R.id.txt_min_time);
        tvMaxTime = findViewById(R.id.txt_max_time);
        tvLoanAmount = findViewById(R.id.txt_loan_amount);
        tvTimePeriod = findViewById(R.id.txt_time_period);
        tvInterest = findViewById(R.id.txt_interest);
        tvTotalLoan = findViewById(R.id.txt_total_loan);
        tvInstallment = findViewById(R.id.txt_installment);
        cbTerms = findViewById(R.id.cb_terms);
        btnApply = findViewById(R.id.btn_apply);

    }
}
