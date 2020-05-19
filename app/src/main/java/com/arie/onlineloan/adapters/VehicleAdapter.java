package com.arie.onlineloan.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arie.onlineloan.R;
import com.arie.onlineloan.models.Vehicle;

import java.text.DecimalFormat;
import java.util.ArrayList;

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
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = listVehicle.get(position);

        String price = context.getString(R.string.con_amount, df.format((vehicle.getPrice())));
        holder.tvType.setText(vehicle.getType());
        holder.tvModel.setText(vehicle.getModel());
        holder.tvBrand.setText(vehicle.getModel());
        holder.tvYear.setText(vehicle.getYear());
        holder.tvPrice.setText(price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpDialog.setContentView(R.layout.pop_up_edit_price);
                TextView tvPrice = popUpDialog.findViewById(R.id.txt_price);
                Button btnUpdate = popUpDialog.findViewById(R.id.btn_update);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
}
