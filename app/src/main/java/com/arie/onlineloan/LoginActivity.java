package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    UserModel userModel;
    String userEmail,userPass;
    EditText etEmail,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.txt_register);
        etEmail = findViewById(R.id.txt_email);
        etPassword = findViewById(R.id.txt_pass);

        UserPreference mUserPreference = new UserPreference(this);
        userModel = mUserPreference.getUser();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = etEmail.getText().toString();
                userPass = etPassword.getText().toString();

                if(userEmail.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email Can't be Empty", Toast.LENGTH_SHORT).show();
                }else if(userPass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Password Can't be Empty", Toast.LENGTH_SHORT).show();
                }else{
                    login();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inReg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(inReg);
            }
        });
    }

    private void login() {

        class login extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Loading Data...", "Please Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                try {
                    Log.d("Json Login", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String userId = jo.getString("ID");
                    String userName = jo.getString("FULLNAME");
                    String userEmail = jo.getString("EMAIL");
                    String userAddress = jo.getString("ADDRESS");
                    String userPhone = jo.getString("PHONE");
                    String response = jo.getString("response");
                    String message = jo.getString("message");

                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (response.equals("1")) {
                        saveUser(userId, userPass, userName, userEmail, userAddress, userPhone,
                                response, message);

                        Intent in = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(in);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("EMAIL", userEmail);
                params.put("PASSWORD", userPass);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(phpConf.URL_LOGIN, params);
                return res;
            }
        }

        login add = new login();
        add.execute();
    }

    void saveUser(String userId, String userPassword, String userName, String userEmail, String userAddress, String userPhone,
                  String response, String message) {
        UserPreference userPreference = new UserPreference(this);
        userModel.setUserId(userId);
        userModel.setUserPassword(userPassword);
        userModel.setUserName(userName);
        userModel.setUserEmail(userEmail);
        userModel.setUserPhone(userPhone);
        userModel.setUserAddress(userAddress);
        userModel.setUserResponse(response);
        userModel.setUserMessage(message);

        userPreference.setUser(userModel);
    }
}
