package com.arie.onlineloan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ProfileActivity extends AppCompatActivity {
    ProgressDialog loading;
    User userModel;
    UserPreference mUserPreference;

    TextView tvName;
    TextView tvNik;
    TextView tvDob;
    TextView tvAddress;
    TextView tvPhone;
    TextView tvEmail;
    TextView tvNameEdit;
    TextView tvAddressEdit;
    TextView tvPhoneEdit;
    TextView tvEmailEdit;
    TextView tvAccNumber;
    TextView tvJob;
    TextView tvInstitution;
    TextView tvSalary;

    LinearLayout llPersonal;
    LinearLayout llPersonalEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUserPreference = new UserPreference(this);
        userModel = mUserPreference.getUser();

        llPersonal = findViewById(R.id.ll_personal);
        llPersonalEdit = findViewById(R.id.ll_personal_edit);
        tvName = findViewById(R.id.txt_name);
        tvNik = findViewById(R.id.txt_nik);
        tvDob = findViewById(R.id.txt_dob);
        tvAddress = findViewById(R.id.txt_address);
        tvPhone = findViewById(R.id.txt_phone);
        tvEmail = findViewById(R.id.txt_email);
        tvNameEdit = findViewById(R.id.txt_name_edit);
        tvAddressEdit = findViewById(R.id.txt_address_edit);
        tvPhoneEdit = findViewById(R.id.txt_phone_edit);
        tvEmailEdit = findViewById(R.id.txt_email_edit);
        tvAccNumber = findViewById(R.id.txt_account_number);
        tvJob = findViewById(R.id.txt_job);
        tvInstitution = findViewById(R.id.txt_institution);
        tvSalary = findViewById(R.id.txt_salary);

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
                hitUpdateProfile();
            }
        });
        hitProfile();
    }

    private void hitProfile() {
        loading = ProgressDialog.show(ProfileActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_GET_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json Profile", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");
                    String userId = jo.getString("ACCOUNT_ID");
                    String userName = jo.getString("FULLNAME");
                    String userNik = jo.getString("NIK");
                    String userEmail = jo.getString("EMAIL");
                    String userPhone = jo.getString("PHONE");
                    String userAddress = jo.getString("ADDRESS");
                    String userDob = jo.getString("BIRTHDATE");
                    String userSalary = jo.getString("SALARY");
                    String userCompany = jo.getString("COMPANY");
                    String userJob = jo.getString("JOB");

                    if (response.equals("1")) {
                        tvName.setText(userName);
                        tvNik.setText(userNik);
                        tvDob.setText(userDob);
                        tvAddress.setText(userAddress);
                        tvPhone.setText(userPhone);
                        tvEmail.setText(userEmail);
                        tvNameEdit.setText(userName);
                        tvAddressEdit.setText(userAddress);
                        tvPhoneEdit.setText(userPhone);
                        tvEmailEdit.setText(userEmail);
                        tvAccNumber.setText(userId);
                        tvJob.setText(userJob);
                        tvInstitution.setText(userCompany);
                        tvSalary.setText(userSalary);
                        llPersonal.setVisibility(View.VISIBLE);
                        llPersonalEdit.setVisibility(View.GONE);
                    } else {
                        String message = jo.getString("message");
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

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
                Toast.makeText(ProfileActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("USER_ID", userModel.getUserId());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void hitUpdateProfile() {
        loading = ProgressDialog.show(ProfileActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_UPDATE_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json Update Profile", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");
                    String message = jo.getString("message");
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                    if (response.equals("1")) {
                        loading.dismiss();
                        hitProfile();
                        llPersonal.setVisibility(View.VISIBLE);
                        llPersonalEdit.setVisibility(View.GONE);
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
                Toast.makeText(ProfileActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("USER_ID", userModel.getUserId());
                params.put("FULLNAME", tvNameEdit.getText().toString());
                params.put("ADDRESS", tvAddressEdit.getText().toString());
                params.put("PHONE_NUMBER", tvPhoneEdit.getText().toString());
                params.put("EMAIL", tvEmailEdit.getText().toString());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }
}
