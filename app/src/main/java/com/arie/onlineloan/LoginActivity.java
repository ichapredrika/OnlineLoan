package com.arie.onlineloan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private TextView tvEmail, tvPassword;
    private User userModel;
    private UserPreference mUserPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvEmail = findViewById(R.id.txt_email);
        tvPassword = findViewById(R.id.txt_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.txt_register);

        mUserPreference = new UserPreference(this);
        userModel = mUserPreference.getUser();
        if (userModel.getUserId() != null) {
            hitLogin(userModel.getUserEmail(), userModel.getUserPassword());
        }
        userModel = new User();

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inReg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(inReg);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = tvEmail.getText().toString();
                String userPass = tvPassword.getText().toString();

                if (userEmail.isEmpty()) {
                    tvEmail.setError("Email Can't be Empty");
                    tvEmail.requestFocus();
                    Toast.makeText(LoginActivity.this, "Email Can't be Empty", Toast.LENGTH_SHORT).show();
                } else if (userPass.isEmpty()) {
                    tvPassword.setError("Password Can't be Empty");
                    tvPassword.requestFocus();
                    Toast.makeText(LoginActivity.this, "Password Can't be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    hitLogin(userEmail, userPass);
                }
            }
        });
    }

    private void hitLogin(final String userEmail, final String userPassword) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(LoginActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json Login", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");
                    String userId = jo.getString("ID");
                    String userName = jo.getString("FULLNAME");
                    String userEmail = jo.getString("EMAIL");
                    String userPass = jo.getString("PASSWORD");
                    String userPhone = jo.getString("PHONE");
                    String userAddress = jo.getString("ADDRESS");
                    //String userRole = jo.getString("ROLE");
                    String message = jo.getString("message");

                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    if (response.equals("1")) {
                        saveUser(userId, userPass, userName, userEmail, userAddress, userPhone);
                        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        Toast.makeText(LoginActivity.this, "Welcome, "+ userName, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("EMAIL", userEmail);
                params.put("PASSWORD", userPassword);

                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    void saveUser(String userId, String userPassword, String userFullname, String userEmail,
                  String userAddress, String userPhone) {
        UserPreference userPreference = new UserPreference(this);
        userModel.setUserId(userId);
        userModel.setUserPassword(userPassword);
        userModel.setUserFullName(userFullname);
        userModel.setUserEmail(userEmail);
        userModel.setUserPhone(userPhone);
        userModel.setUserAddress(userAddress);
        //userModel.setUserRole(userRole);

        userPreference.setUser(userModel);
    }
}
