package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class AccountMenuFragment extends Fragment {

    private TextView textViewAccountInfo;
    private TextView textViewAccountSecurity;
    private TextView textViewLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_menu, container, false);

        textViewAccountInfo = view.findViewById(R.id.textViewAccountInfo);
        textViewAccountSecurity = view.findViewById(R.id.textViewAccountSecurity);
        textViewLogout = view.findViewById(R.id.textViewLogout);

        textViewAccountInfo.setOnClickListener(v -> showAccountInfo());
        textViewAccountSecurity.setOnClickListener(v -> showAccountSecurity());
        textViewLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void showAccountInfo() {
        Toast.makeText(getActivity(), "Account Information Selected", Toast.LENGTH_SHORT).show();
        // Implement navigation to account information screen
    }

    private void showAccountSecurity() {
        Toast.makeText(getActivity(), "Account Security Selected", Toast.LENGTH_SHORT).show();
        // Implement navigation to account security screen
    }

    private void logout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to the LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
