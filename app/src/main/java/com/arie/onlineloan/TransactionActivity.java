package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arie.onlineloan.adapters.TransactionAdapter;
import com.arie.onlineloan.models.Transaction;
import com.arie.onlineloan.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionActivity extends AppCompatActivity {
    private RecyclerView rvTransaction;
    private Transaction transaction;
    private TransactionAdapter transactionAdapter;
    private ProgressDialog loading;

    User user;
    UserPreference mUserPreference;

    public static String LIST_USER = "1";
    public static String LIST_ADMIN = "2";
    public static String LIST_PENDING = "3";

    private String origin;

    private ArrayList<Transaction> listTransaction = new ArrayList<>();
    private ArrayList<Transaction> listTransactionToAdapter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mUserPreference = new UserPreference(this);
        user = mUserPreference.getUser();

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        origin = intent.getStringExtra("origin");
        rvTransaction = findViewById(R.id.rv_transaction);

        transactionAdapter = new TransactionAdapter(TransactionActivity.this, listTransactionToAdapter, origin);
        transactionAdapter.notifyDataSetChanged();
        rvTransaction.setHasFixedSize(true);
        rvTransaction.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
        rvTransaction.setAdapter(transactionAdapter);

        if (type.equals(LIST_USER)) {
            getUserTrans();
        } else if (type.equals(LIST_ADMIN)) {
            getAllTrans();
        } else if (type.equals(LIST_PENDING)) {
            getPendingTrans();
        }
    }

    private void getUserTrans() {
        loading = ProgressDialog.show(TransactionActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(TransactionActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_GET_USER_TRANSACTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    Log.d("Json getUserTrans", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        JSONArray transCast = jo.getJSONArray("TRX");
                        for (int i = 0; i < transCast.length(); i++) {
                            JSONObject object = transCast.getJSONObject(i);
                            transaction = new Transaction(object);
                            listTransaction.add(transaction);
                        }
                        updateAdapter(listTransaction);
                    } else {
                        loading.dismiss();
                        String message = jo.getString("message");
                        Toast.makeText(TransactionActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TransactionActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("USER_ID", user.getUserId());
                Log.d("tag", params.toString());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void getAllTrans() {
        loading = ProgressDialog.show(TransactionActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(TransactionActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, phpConf.URL_GET_ALL_TRANSACTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    Log.d("Json getAllTrans", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        JSONArray transCast = jo.getJSONArray("TRX");
                        for (int i = 0; i < transCast.length(); i++) {
                            JSONObject object = transCast.getJSONObject(i);
                            transaction = new Transaction(object);
                            listTransaction.add(transaction);
                        }
                        updateAdapter(listTransaction);
                    } else {
                        loading.dismiss();
                        String message = jo.getString("message");
                        Toast.makeText(TransactionActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TransactionActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void getPendingTrans() {
        loading = ProgressDialog.show(TransactionActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(TransactionActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, phpConf.URL_GET_PENDING_TRANSACTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    Log.d("Json getPendingTrans", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        JSONArray transCast = jo.getJSONArray("TRX");
                        for (int i = 0; i < transCast.length(); i++) {
                            JSONObject object = transCast.getJSONObject(i);
                            transaction = new Transaction(object);
                            listTransaction.add(transaction);
                        }
                        updateAdapter(listTransaction);
                    } else {
                        loading.dismiss();
                        String message = jo.getString("message");
                        Toast.makeText(TransactionActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TransactionActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void updateAdapter(ArrayList<Transaction> list) {
        listTransactionToAdapter.clear();
        listTransactionToAdapter.addAll(list);
        transactionAdapter.notifyDataSetChanged();
    }
}
