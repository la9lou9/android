package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ContactViewHolder> {

    private List<EmergencyContact> contacts;
    private OnContactInteractionListener listener;

    public EmergencyContactAdapter(List<EmergencyContact> contacts, OnContactInteractionListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emergency_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        EmergencyContact contact = contacts.get(position);
        holder.textViewContactName.setText(contact.getName());
        holder.buttonCall.setOnClickListener(v -> listener.onCallClick(contact));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public List<EmergencyContact> getContacts() {
        return contacts;
    }

    public void addContact(EmergencyContact contact) {
        contacts.add(contact);
        notifyItemInserted(contacts.size() - 1);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContactName;
        Button buttonCall;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContactName = itemView.findViewById(R.id.textViewContactName);
            buttonCall = itemView.findViewById(R.id.buttonCall);
        }
    }

    public interface OnContactInteractionListener {
        void onCallClick(EmergencyContact contact);
    }
}
