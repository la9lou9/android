package com.example.myapplication.Appointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;

    public AppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.doctorName.setText(appointment.getDoctorName());
        holder.address.setText(appointment.getAddress());
        holder.phoneNumber.setText(appointment.getPhoneNumber());
        holder.date.setText(appointment.getDate());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        notifyItemInserted(appointments.size() - 1);
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, address, phoneNumber, date;
        CardView cardView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.textViewDoctorName);
            address = itemView.findViewById(R.id.textViewAddress);
            phoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            date = itemView.findViewById(R.id.textViewDate);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
