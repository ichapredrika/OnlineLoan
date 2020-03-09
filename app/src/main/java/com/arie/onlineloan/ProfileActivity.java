package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final LinearLayout llPersonal = findViewById(R.id.ll_personal);
        final LinearLayout llPersonalEdit = findViewById(R.id.ll_personal_edit);
        TextView tvName = findViewById(R.id.txt_name);
        TextView tvNik = findViewById(R.id.txt_nik);
        TextView tvDob = findViewById(R.id.txt_dob);
        TextView tvAddress = findViewById(R.id.txt_address);
        TextView tvPhone = findViewById(R.id.txt_phone);
        TextView tvEmail = findViewById(R.id.txt_email);
        TextView tvNameEdit = findViewById(R.id.txt_name_edit);
        TextView tvNikEdit = findViewById(R.id.txt_nik_edit);
        TextView tvDobEdit = findViewById(R.id.txt_dob_edit);
        TextView tvAddressEdit = findViewById(R.id.txt_address_edit);
        TextView tvPhoneEdit = findViewById(R.id.txt_phone_edit);
        TextView tvEmailEdit = findViewById(R.id.txt_email_edit);
        TextView tvAccNumber = findViewById(R.id.txt_account_number);
        TextView tvJob = findViewById(R.id.txt_job);
        TextView tvInstitution = findViewById(R.id.txt_institution);
        TextView tvSalary = findViewById(R.id.txt_salary);

        ImageView imgEdit = findViewById(R.id.img_edit);
        Button btnSaveProfile = findViewById(R.id.btn_save_profile);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llPersonal.setVisibility(View.GONE);
                llPersonalEdit.setVisibility(View.VISIBLE);
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llPersonal.setVisibility(View.VISIBLE);
                llPersonalEdit.setVisibility(View.GONE);
            }
        });

    }
}
