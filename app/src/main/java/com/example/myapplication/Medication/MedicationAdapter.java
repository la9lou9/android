package com.example.myapplication.Medication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class MedicationAdapter extends BaseAdapter {

    private Context context;
    private List<Medication> medicationList;
    private LayoutInflater inflater;

    public MedicationAdapter(Context context, List<Medication> medicationList) {
        this.context = context;
        this.medicationList = medicationList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return medicationList.size();
    }

    @Override
    public Object getItem(int position) {
        return medicationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return medicationList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_medication, parent, false);
            holder = new ViewHolder();
            holder.tvMedicationName = convertView.findViewById(R.id.tvMedicationName);
            holder.tvDosage = convertView.findViewById(R.id.tvDosage);
            holder.tvFrequency = convertView.findViewById(R.id.tvFrequency);
            holder.tvReminderTime = convertView.findViewById(R.id.tvReminderTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Medication medication = medicationList.get(position);

        holder.tvMedicationName.setText(medication.getMedicationName());
        holder.tvDosage.setText("Dosage: " + medication.getDosage());
        holder.tvFrequency.setText("Frequency: " + medication.getFrequency());
        holder.tvReminderTime.setText("Reminder: " + medication.getReminderTime());

        return convertView;
    }

    static class ViewHolder {
        TextView tvMedicationName;
        TextView tvDosage;
        TextView tvFrequency;
        TextView tvReminderTime;
    }
}
