package com.arie.onlineloan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arie.onlineloan.models.User;

public class MainCustomerActivity extends AppCompatActivity {

    User userModel;
    UserPreference mUserPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserPreference = new UserPreference(this);
        userModel = mUserPreference.getUser();

        TextView tvWelcome = findViewById(R.id.txt_welcome);
        ImageView imgLogout = findViewById(R.id.img_logout);
        TextView tvCollateral = findViewById(R.id.txt_collateral);
        TextView tvNonCollateral = findViewById(R.id.txt_non_collateral);
        TextView tvProfile = findViewById(R.id.txt_profile);
        LinearLayout llCollateral = findViewById(R.id.ll_collateral);
        LinearLayout llNonCollateral = findViewById(R.id.ll_non_collateral);
        LinearLayout llTransaction = findViewById(R.id.ll_transaction);
        LinearLayout llProfile = findViewById(R.id.ll_profile);

        String welcome = getString(R.string.welcome, userModel.getFullname());
        tvWelcome.setText(welcome);

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCustomerActivity.this, LoginActivity.class);
                mUserPreference.logoutUser();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llNonCollateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCustomerActivity.this, NonCollateralActivity.class);
                startActivity(intent);
            }
        });

        llCollateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCustomerActivity.this, CollateralActivity.class);
                startActivity(intent);
            }
        });

        llTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inTrans = new Intent(MainCustomerActivity.this, TransactionActivity.class);
                startActivity(inTrans);
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inProfile = new Intent(MainCustomerActivity.this, ProfileActivity.class);
                startActivity(inProfile);
            }
        });
    }


}
