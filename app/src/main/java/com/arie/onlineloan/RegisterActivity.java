package com.arie.onlineloan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arie.onlineloan.models.User;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    Toolbar toolbar;
    private TextView tvFullname;
    private TextView tvNik;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvPass1;
    private TextView tvPass2;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Register");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvFullname = findViewById(R.id.txt_fullname);
        tvNik = findViewById(R.id.txt_nik);
        tvEmail = findViewById(R.id.txt_email);
        tvPhone = findViewById(R.id.txt_phone);
        tvAddress = findViewById(R.id.txt_address);
        tvPass1 = findViewById(R.id.txt_pass);
        tvPass2 = findViewById(R.id.txt_repass);
        Button btnRegist = findViewById(R.id.btn_regist);

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    user = new User();
                    user.setFullname(tvFullname.getText().toString().trim());
                    user.setNik(tvNik.getText().toString().trim());
                    user.setEmail(tvEmail.getText().toString().trim());
                    user.setAddress(tvAddress.getText().toString().trim());
                    user.setPassword(tvPass1.getText().toString().trim());
                    user.setPhone(tvPhone.getText().toString().trim());

                    hitRegist(user);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValid() {
        boolean isValid = false;
        if (tvFullname.getText().toString().trim().isEmpty()) {
            tvFullname.setError("Fullname Can't be Empty!");
            tvFullname.requestFocus();
        } else if (tvNik.getText().toString().trim().isEmpty()) {
            tvNik.setError("NIK Field Can't be Empty!");
            tvNik.requestFocus();
        } else if (tvEmail.getText().toString().trim().isEmpty()) {
            tvEmail.setError("Email Field Can't be Empty!");
            tvEmail.requestFocus();
        } else if (tvPhone.getText().toString().trim().isEmpty()) {
            tvPhone.setError("Phone Number Field Can't be Empty!");
            tvPhone.requestFocus();
        } else if (tvAddress.getText().toString().trim().isEmpty()) {
            tvAddress.setError("Email Field Can't be Empty!");
            tvAddress.requestFocus();
        } else if (tvPass1.getText().toString().trim().isEmpty()) {
            tvPass1.setError("Password Field Can't be Empty!");
            tvPass1.requestFocus();
        } else if (tvPass2.getText().toString().isEmpty()) {
            tvPass2.setError("Password Field Can't be Empty!");
            tvPass2.requestFocus();
        } else if (!tvPass1.getText().toString().equals(tvPass2.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show();
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void hitRegist(final User user) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                if (!response.equals("Email Address Already Used")) {
                    Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(in);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("EMAIL", user.getEmail());
                params.put("FULLNAME", user.getFullname());
                params.put("NIK", user.getNik());
                params.put("PASSWORD", user.getPassword());
                params.put("ADDRESS", user.getAddress());
                params.put("PHONE", user.getPhone());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }
}
