package com.arie.onlineloan.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arie.onlineloan.R;
import com.arie.onlineloan.models.Vehicle;
import com.arie.onlineloan.phpConf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private ArrayList<Vehicle> listVehicle;
    private Context context;
    private DecimalFormat df = new DecimalFormat("#,###.###");
    private Dialog popUpDialog;

    public VehicleAdapter(Context context, ArrayList<Vehicle> listVehicle) {
        this.context = context;
        this.listVehicle = listVehicle;
        popUpDialog = new Dialog(context);
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vehicle_price, viewGroup, false);
        return new VehicleAdapter.VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, final int position) {
        final Vehicle vehicle = listVehicle.get(position);

        String price = context.getString(R.string.con_amount, df.format((vehicle.getPrice())));
        holder.tvType.setText(vehicle.getType());
        holder.tvModel.setText(vehicle.getModel());
        holder.tvBrand.setText(vehicle.getBrand());
        holder.tvYear.setText(vehicle.getYear());
        holder.tvPrice.setText(price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpDialog.setContentView(R.layout.pop_up_edit_price);
                final TextView tvPrice = popUpDialog.findViewById(R.id.txt_price);
                Button btnUpdate = popUpDialog.findViewById(R.id.btn_update);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(tvPrice.getText())) {
                            Toast.makeText(context, "Please input price!", Toast.LENGTH_SHORT).show();
                        } else {
                            vehicle.setPrice(Double.parseDouble(tvPrice.getText().toString().trim()));
                            changePrice(vehicle, position);
                            popUpDialog.dismiss();
                        }

                    }
                });
                if (popUpDialog.getWindow() != null) {
                    popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popUpDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                popUpDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVehicle.size();
    }

    class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        TextView tvBrand;
        TextView tvModel;
        TextView tvYear;
        TextView tvPrice;

        VehicleViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.txt_type);
            tvBrand = itemView.findViewById(R.id.txt_brand);
            tvModel = itemView.findViewById(R.id.txt_model);
            tvYear = itemView.findViewById(R.id.txt_year);
            tvPrice = itemView.findViewById(R.id.txt_price);
        }
    }

    private void changePrice(final Vehicle vehicle, final int position) {
        final RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        final StringRequest mStringRequest = new StringRequest(Request.Method.POST, phpConf.URL_UPDATE_VEHICLE_PRICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("Json Update Price", s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("result");
                    JSONObject jo = data.getJSONObject(0);

                    Log.d("tagJsonObject", jo.toString());
                    String response = jo.getString("response");
                    String message = jo.getString("message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    if (response.equals("1")) {
                        listVehicle.get(position).setPrice(vehicle.getPrice());
                        notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", String.valueOf(error));
                Toast.makeText(context, context.getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<>();
                params.put("ID", vehicle.getId());
                params.put("PRICE", Double.toString(vehicle.getPrice()));
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }
}
