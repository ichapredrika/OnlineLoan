package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arie.onlineloan.models.User;

public class MainAdminActivity extends AppCompatActivity {
    User userModel;
    UserPreference mUserPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        mUserPreference = new UserPreference(this);
        userModel = mUserPreference.getUser();

        TextView tvWelcome = findViewById(R.id.txt_welcome);
        ImageView imgLogout = findViewById(R.id.img_logout);
        TextView tvProfile = findViewById(R.id.txt_profile);
        LinearLayout llReport = findViewById(R.id.ll_report);
        LinearLayout llTransaction = findViewById(R.id.ll_transaction);
        LinearLayout llProfile = findViewById(R.id.ll_profile);
        LinearLayout llApproval = findViewById(R.id.ll_approval);

        String welcome = getString(R.string.welcome, userModel.getFullname());
        tvWelcome.setText(welcome);

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdminActivity.this,LoginActivity.class);
                mUserPreference.logoutUser();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inReport = new Intent(MainAdminActivity.this, TransactionActivity.class);
                startActivity(inReport);
            }
        });

        llTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inTrans = new Intent(MainAdminActivity.this, TransactionActivity.class);
                inTrans.putExtra("type", TransactionActivity.LIST_ADMIN);
                inTrans.putExtra("origin", TransactionDetailActivity.ORIGIN_ADMIN);
                startActivity(inTrans);
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inProfile = new Intent(MainAdminActivity.this, ProfileActivity.class);
                startActivity(inProfile);
            }
        });

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inTrans = new Intent(MainAdminActivity.this, TransactionActivity.class);
                inTrans.putExtra("type", TransactionActivity.LIST_PENDING);
                inTrans.putExtra("origin", TransactionDetailActivity.ORIGIN_PENDING);
                startActivity(inTrans);
            }
        });
    }
}
