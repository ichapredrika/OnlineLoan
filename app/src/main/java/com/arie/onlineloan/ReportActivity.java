package com.arie.onlineloan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity implements DatePickerFragment.DialogDateListener {
    private ProgressDialog loading;
    final String START_DATE_TAG = "StartDate";
    final String END_DATE_TAG = "EndDate";

    private ArrayList<String> reportList = new ArrayList<>();
    private ArrayList<String> chartList = new ArrayList<>();

    private Spinner spinnerReport;
    private Spinner spinnerChart;
    private ImageButton btnStartDate;
    private ImageButton btnEndDate;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private Button btnGenerate;

    private LinearLayout llChart;

    private String startDate, endDate;
    private DecimalFormat df = new DecimalFormat("#,###.##");

    BarChart barChart;
    PieChart pieChart;

    ArrayList<BarEntry> listTotalBar = new ArrayList<BarEntry>();
    ArrayList<Entry> listTotalPie = new ArrayList<Entry>();
    ArrayList<String> listTransaction = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        spinnerReport = findViewById(R.id.sp_report_selection);
        spinnerChart = findViewById(R.id.sp_chart_type);
        btnGenerate = findViewById(R.id.btn_generate);
        btnStartDate = findViewById(R.id.btn_start_date);
        btnEndDate = findViewById(R.id.btn_end_date);
        tvStartDate = findViewById(R.id.txt_start_date);
        tvEndDate = findViewById(R.id.txt_end_date);
        pieChart = findViewById(R.id.piechart);
        barChart = findViewById(R.id.barchart);

        reportList.add("Loan Categorized By Type");
        //reportList.add("Loan Categorized By Amount");
        reportList.add("Loan Categorized By Time Period");
        chartList.add("Bar Chart");
        chartList.add("Pie Chart");

        ArrayAdapter<String> reportAdapter = new ArrayAdapter<>(ReportActivity.this,
                android.R.layout.simple_spinner_item, reportList);

        ArrayAdapter<String> chartAdapter = new ArrayAdapter<>(ReportActivity.this,
                android.R.layout.simple_spinner_item, chartList);

        spinnerReport.setAdapter(reportAdapter);
        spinnerChart.setAdapter(chartAdapter);


        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check spinner, start date, end date
               /* if (spinnerChart.getSelectedItemPosition() ==0){
                    barChart.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                }else{
                    barChart.setVisibility(View.GONE);
                    pieChart.setVisibility(View.VISIBLE);
                }*/
                if (spinnerReport.getSelectedItemPosition() == 0) {
                    if (checkDate() && setChartVisibility()) {
                        getTypeReport();
                    }
                } else if (spinnerReport.getSelectedItemPosition() == 1) {
                    if (checkDate()) {
                        getTimeReport();
                    }
                } else {
                    Toast.makeText(ReportActivity.this, "Please choose report to be generated!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), START_DATE_TAG);
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), END_DATE_TAG);
            }
        });

    }

    private void getTimeReport() {
        loading = ProgressDialog.show(ReportActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(ReportActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_REPORT_TIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    listTransaction.clear();
                    listTotalPie.clear();
                    listTotalBar.clear();

                    Log.d("Json report Amount", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        JSONArray brands = jo.getJSONArray("SUMMARY");
                        for (int i = 0; i < brands.length(); i++) {
                            JSONObject item = brands.getJSONObject(i);
                            listTotalPie.add(new Entry(Float.parseFloat(item.getString("TOTAL")), i));
                            listTransaction.add("Time Period: "+ item.getString("PERIOD_TIME"));
                            listTotalBar.add(new BarEntry(Float.parseFloat(item.getString("TOTAL")), i));
                        }

                        createBarChart("Loan Report Categorized By Time");
                        createPieChart("Loan Report Categorized By Time");
                    } else {
                        loading.dismiss();
                        String message = jo.getString("message");
                        Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReportActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("STARTDATE", startDate);
                params.put("ENDDATE", endDate);
                Log.d("tag", params.toString());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void getTypeReport() {
        loading = ProgressDialog.show(ReportActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(ReportActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_REPORT_AMOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    listTransaction.clear();
                    listTotalPie.clear();
                    listTotalBar.clear();

                    Log.d("Json report Amount", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");

                    if (response.equals("1")) {
                        JSONArray brands = jo.getJSONArray("SUMMARY");
                        for (int i = 0; i < brands.length(); i++) {
                            JSONObject item = brands.getJSONObject(i);
                            listTotalPie.add(new Entry(Float.parseFloat(item.getString("TOTAL")), i));
                            listTransaction.add(item.getString("LOAN_TYPE"));
                            listTotalBar.add(new BarEntry(Float.parseFloat(item.getString("TOTAL")), i));
                        }
                        createBarChart("Loan Report Categorized By Type");
                        createPieChart("Loan Report Categorized By Type");

                    } else {
                        loading.dismiss();
                        String message = jo.getString("message");
                        Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReportActivity.this, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("STARTDATE", startDate);
                params.put("ENDDATE", endDate);
                Log.d("tag", params.toString());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private boolean setChartVisibility() {
        if (spinnerChart.getSelectedItemPosition() == 0) {
            barChart.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            return true;
        } else if (spinnerChart.getSelectedItemPosition() == 1) {
            pieChart.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
            return true;
        } else {
            Toast.makeText(ReportActivity.this, "Please choose chart type!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkDate() {
        if (startDate == null || startDate.isEmpty()) {
            Toast.makeText(ReportActivity.this, "Please input the start date!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (endDate == null || endDate.isEmpty()) {
            Toast.makeText(ReportActivity.this, "Please input the end date!", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    private void createPieChart(String title) {
        PieDataSet dataSet = new PieDataSet(listTotalPie, title);
        PieData data = new PieData(listTransaction, dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setContentDescription(null);
        pieChart.setDescription(null);
        pieChart.animateXY(3000, 3000);
    }

    private void createBarChart(String title) {
        BarDataSet bardataset = new BarDataSet(listTotalBar, title);
        barChart.animateY(3000);
        BarData data = new BarData(listTransaction, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(data);
        barChart.setDescription(null);
    }

    @Override
    public void onDialogDateSet(String tag, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        if (tag.equals(START_DATE_TAG)) {
            tvStartDate.setText(dateFormat.format(calendar.getTime()));
            startDate = dateFormat.format(calendar.getTime());
        } else if (tag.equals(END_DATE_TAG)) {
            tvEndDate.setText(dateFormat.format(calendar.getTime()));
            endDate = dateFormat.format(calendar.getTime());
        }
    }
}
