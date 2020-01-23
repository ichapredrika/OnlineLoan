package com.arie.onlineloan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText etFullname, etEmail, etPhone, etAddress, etPass, etRetypePass;
    String userName, userEmail, userPhone, userAddress, userPass, userRepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullname = findViewById(R.id.txt_username);
        etEmail = findViewById(R.id.txt_email);
        etPhone = findViewById(R.id.txt_phone);
        etAddress = findViewById(R.id.txt_address);
        etPass = findViewById(R.id.txt_pass);
        etRetypePass = findViewById(R.id.txt_repass);

        Button btnRegister = findViewById(R.id.btn_regist);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = etFullname.getText().toString();
                userEmail = etEmail.getText().toString();
                userPhone = etPhone.getText().toString();
                userAddress = etAddress.getText().toString();
                userPass = etPass.getText().toString();
                userRepass = etRetypePass.getText().toString();

                if(userName.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Username Can't be Empty", Toast.LENGTH_SHORT).show();
                }else if(userEmail.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Email Can't be Empty", Toast.LENGTH_SHORT).show();
                }else if(userPhone.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Phone Can't be Empty", Toast.LENGTH_SHORT).show();
                }else if(userAddress.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Address Can't be Empty", Toast.LENGTH_SHORT).show();
                }else if(userPass.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Password Can't be Empty", Toast.LENGTH_SHORT).show();
                }else if(!userPass.equals(userRepass)){
                    Toast.makeText(RegisterActivity.this, "Password DOesn't Match!", Toast.LENGTH_SHORT).show();
                }else{
                    register();
                }

            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Register");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void register(){

        class register extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this,"Registering User...","Please Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_LONG).show();
                if(!s.equals("Email Address Already Used")){
                    Intent in = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(in);
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("EMAIL",userEmail);
                params.put("PASSWORD",userPass);
                params.put("FULLNAME",userName);
                params.put("ADDRESS",userAddress);
                params.put("PHONE",userPhone);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(phpConf.URL_REGISTER, params);
                return res;
            }
        }

        register add = new register();
        add.execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
