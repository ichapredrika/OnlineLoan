package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arie.onlineloan.models.User;

public class MainActivity extends AppCompatActivity {

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

        String welcome = getString(R.string.welcome, userModel.getUserFullName());
        tvWelcome.setText(welcome);

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                mUserPreference.logoutUser();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
