package com.example.myapplication.Medication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {

    private List<Medication> medications;

    public MedicationAdapter(List<Medication> medications) {
        this.medications = medications;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = medications.get(position);
        holder.textViewMedicationName.setText(medication.getMedicationName());
        holder.textViewDosage.setText(medication.getDosage());
        holder.textViewFrequency.setText(medication.getFrequency());
        holder.textViewStartDate.setText(medication.getStartDate());
        holder.textViewEndDate.setText(medication.getEndDate());
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMedicationName;
        TextView textViewDosage;
        TextView textViewFrequency;
        TextView textViewStartDate;
        TextView textViewEndDate;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMedicationName = itemView.findViewById(R.id.textViewMedicationName);
            textViewDosage = itemView.findViewById(R.id.textViewDosage);
            textViewFrequency = itemView.findViewById(R.id.textViewFrequency);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);
        }
    }
}
