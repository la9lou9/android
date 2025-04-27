package com.example.myapplication.Prescription;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;


public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder> {

    private List<Prescription> prescriptions;

    public PrescriptionAdapter(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription, parent, false);
        return new PrescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        Prescription prescription = prescriptions.get(position);
        holder.textViewDoctorName.setText(prescription.getDoctorName());
        holder.textViewPrescriptionDetails.setText(prescription.getDetails());
        holder.textViewPrescriptionDate.setText(prescription.getDate());
    }

    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public static class PrescriptionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDoctorName;
        TextView textViewPrescriptionDetails;
        TextView textViewPrescriptionDate;

        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDoctorName = itemView.findViewById(R.id.textViewDoctorName);
            textViewPrescriptionDetails = itemView.findViewById(R.id.textViewPrescriptionDetails);
            textViewPrescriptionDate = itemView.findViewById(R.id.textViewPrescriptionDate);
        }
    }
}

