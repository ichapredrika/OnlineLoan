package com.arie.onlineloan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    Toolbar toolbar;
    private TextView tvFullname;
    private TextView tvDob;
    private TextView tvNik;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvPass1;
    private TextView tvPass2;
    private LinearLayout llData;
    private User user;
    private boolean isEligible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Register");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvFullname = findViewById(R.id.txt_fullname);
        tvDob = findViewById(R.id.txt_dob);
        tvNik = findViewById(R.id.txt_nik);
        tvEmail = findViewById(R.id.txt_email);
        tvPhone = findViewById(R.id.txt_phone);
        tvAddress = findViewById(R.id.txt_address);
        tvPass1 = findViewById(R.id.txt_pass);
        tvPass2 = findViewById(R.id.txt_repass);
        llData = findViewById(R.id.ll_data);
        Button btnRegist = findViewById(R.id.btn_regist);
        Button btnCheckNik = findViewById(R.id.btn_check_nik);

        llData.setVisibility(View.GONE);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidRegist() && isEligible) {
                    user = new User();
                    user.setNik(tvNik.getText().toString().trim());
                    user.setEmail(tvEmail.getText().toString().trim());
                    user.setPassword(tvPass1.getText().toString().trim());

                    hitRegist(user);
                }
            }
        });

        btnCheckNik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidNik()) {
                    user = new User();
                    user.setNik(tvNik.getText().toString().trim());
                    user.setEmail(tvEmail.getText().toString().trim());
                    checkNik(user);
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

    private boolean isValidNik() {
        boolean isValid = false;
        if (tvNik.getText().toString().trim().isEmpty()) {
            tvNik.setError("NIK Field Can't be Empty!");
            tvNik.requestFocus();
        } else if (tvEmail.getText().toString().trim().isEmpty()) {
            tvEmail.setError("Email Field Can't be Empty!");
            tvEmail.requestFocus();
        } else {
            isValid = true;
        }

        return isValid;
    }

    private boolean isValidRegist() {
        boolean isValid = false;
        if (tvPass1.getText().toString().trim().isEmpty()) {
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

    private void checkNik(final User user) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_CHECK_NIK, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, s);
                try {
                    Log.d("Json checkNik", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    JSONObject jo = result.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");


                    if (response.equals("1")) {
                        isEligible = true;
                        llData.setVisibility(View.VISIBLE);
                        JSONArray data = jo.getJSONArray("DATA");
                        JSONObject dataObject = data.getJSONObject(0);

                        tvFullname.setText(dataObject.getString("FULLNAME"));
                        tvDob.setText(dataObject.getString("DATE_OF_BIRTH"));
                        tvPhone.setText(dataObject.getString("PHONE"));
                        tvAddress.setText(dataObject.getString("ADDRESS"));
                    }else{
                        llData.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, jo.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
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
                params.put("NIK", user.getNik());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void hitRegist(final User user) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, s);
                try {
                    Log.d("Json register", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("STATUS");
                    String message = jo.getString("message");

                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();

                    if (response.equals("1")) {
                        Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(in);
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
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
                params.put("NIK", user.getNik());
                params.put("PASSWORD", user.getPassword());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }
}
