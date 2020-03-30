package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class InterestSettingActivity extends AppCompatActivity {
    ProgressDialog loading;
    User userModel;
    UserPreference mUserPreference;

    private TextView tvNonCollateral, tvCollateralCar, tvCollateralMotorcycle, tvCollateralHouse;
    private TextView tvNonCollateralEdit, tvCollateralCarEdit, tvCollateralMotorcycleEdit, tvCollateralHouseEdit;
    private Button btnSaveInterest;
    private LinearLayout llInterest, llEdit;
    private ImageView imgEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_setting);

        llInterest = findViewById(R.id.ll_interest);
        llEdit = findViewById(R.id.ll_interest_edit);
        tvNonCollateral = findViewById(R.id.txt_non_collateral);
        tvCollateralCar = findViewById(R.id.txt_collateral_car);
        tvCollateralMotorcycle = findViewById(R.id.txt_collateral_motorcycle);
        tvCollateralHouse = findViewById(R.id.txt_collateral_house);
        tvNonCollateralEdit = findViewById(R.id.txt_non_collateral_edit);
        tvCollateralCarEdit = findViewById(R.id.txt_collateral_car_edit);
        tvCollateralMotorcycleEdit = findViewById(R.id.txt_collateral_motorcycle_edit);
        tvCollateralHouseEdit = findViewById(R.id.txt_collateral_house_edit);
        btnSaveInterest = findViewById(R.id.btn_save_interest);
        imgEdit = findViewById(R.id.img_edit);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llInterest.setVisibility(View.GONE);
                llEdit.setVisibility(View.VISIBLE);
                tvNonCollateralEdit.setText(tvNonCollateral.getText().toString());
                tvCollateralCarEdit.setText(tvCollateralCar.getText().toString());
                tvCollateralMotorcycleEdit.setText(tvCollateralMotorcycle.getText().toString());
                tvCollateralHouseEdit.setText(tvCollateralHouse.getText().toString());
            }
        });

        btnSaveInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitUpdateInterest();
            }
        });
        hitInterest();

    }

    private void hitInterest() {
        loading = ProgressDialog.show(InterestSettingActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(InterestSettingActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, phpConf.URL_GET_INTEREST, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json Interest", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");


                    if (response.equals("1")) {
                        JSONArray jsonData = jo.getJSONArray("DATA");
                        for(int i=0; i<jsonData.length();i++){
                            JSONObject object = jsonData.getJSONObject(i);
                            if (object.getString("LOAN").equals("COLLATERAL_CAR")){
                                String collateralCar = object.getString("INTEREST");
                                tvCollateralCar.setText(collateralCar);
                            }else  if (object.getString("LOAN").equals("COLLATERAL_HOUSE")){
                                String collateralHouse = object.getString("INTEREST");
                                tvCollateralHouse.setText(collateralHouse);
                            }else  if (object.getString("LOAN").equals("COLLATERAL_MOTORCYCLE")){
                                String collateralMotorcycle = object.getString("INTEREST");
                                tvCollateralMotorcycle.setText(collateralMotorcycle);
                            }else  if (object.getString("LOAN").equals("NON_COLLATERAL")){
                                String nonCollateral = object.getString("INTEREST");
                                tvNonCollateral.setText(nonCollateral);
                            }
                        }

                        llInterest.setVisibility(View.VISIBLE);
                        llEdit.setVisibility(View.GONE);
                    } else {
                        String message = jo.getString("message");
                        Toast.makeText(InterestSettingActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InterestSettingActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void hitUpdateInterest() {
        loading = ProgressDialog.show(InterestSettingActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(InterestSettingActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_UPDATE_INTEREST, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json Update Interest", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");

                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");
                    String message = jo.getString("message");
                    Toast.makeText(InterestSettingActivity.this, message, Toast.LENGTH_SHORT).show();

                    if (response.equals("1")) {
                        loading.dismiss();
                        hitInterest();
                        llInterest.setVisibility(View.VISIBLE);
                        llEdit.setVisibility(View.GONE);
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
                Toast.makeText(InterestSettingActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("NON_COLLATERAL", tvNonCollateralEdit.getText().toString().trim());
                params.put("COLLATERAL_CAR", tvCollateralCarEdit.getText().toString().trim());
                params.put("COLLATERAL_MOTORCYCLE", tvCollateralMotorcycleEdit.getText().toString().trim());
                params.put("COLLATERAL_HOUSE", tvCollateralHouseEdit.getText().toString().trim());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }
}
