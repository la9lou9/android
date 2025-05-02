package com.example.myapplication.Onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private Button buttonOnboardingAction;
    private TextView textSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if we should skip onboarding (for debugging you can force to always show)
        final boolean ALWAYS_SHOW_ONBOARDING = false; // Set to true during development to test

        // Check if onboarding has been completed before
        SharedPreferences preferences = getSharedPreferences("onboarding", MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);

        // If not the first time and not forcing to show, skip onboarding and go directly to MainActivity
        if (!isFirstTime && !ALWAYS_SHOW_ONBOARDING) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);
        textSkip = findViewById(R.id.textSkip);

        setupOnboardingItems();

        ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                } else {
                    completeOnboarding();
                }
            }
        });

        textSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeOnboarding();
            }
        });
    }

    private void setupOnboardingItems() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem welcomeItem = new OnboardingItem();
        welcomeItem.setTitle("Welcome to sa7tek fi jibek");
        welcomeItem.setDescription("Your pocket health companion to help you stay organized and healthy.");
        welcomeItem.setHighlight("Health management made easy");

        OnboardingItem appointmentsItem = new OnboardingItem();
        appointmentsItem.setTitle("Manage Appointments");
        appointmentsItem.setDescription("Never miss a doctor's appointment with our easy scheduling system.");
        appointmentsItem.setHighlight("Set reminders for all your medical visits");

        OnboardingItem medicationsItem = new OnboardingItem();
        medicationsItem.setTitle("Track Medications");
        medicationsItem.setDescription("Set reminders for your medications and track your intake.");
        medicationsItem.setHighlight("Take your medications on time, every time");

        OnboardingItem emergencyItem = new OnboardingItem();
        emergencyItem.setTitle("Emergency Contacts");
        emergencyItem.setDescription("Keep important contacts at your fingertips for quick access in emergencies.");
        emergencyItem.setHighlight("One-tap emergency calling");

        OnboardingItem prescriptionItem = new OnboardingItem();
        prescriptionItem.setTitle("Store Prescriptions");
        prescriptionItem.setDescription("Digitize and store all your medical prescriptions in one secure place.");
        prescriptionItem.setHighlight("No more lost or damaged prescriptions");

        onboardingItems.add(welcomeItem);
        onboardingItems.add(appointmentsItem);
        onboardingItems.add(medicationsItem);
        onboardingItems.add(emergencyItem);
        onboardingItems.add(prescriptionItem);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.tab_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int position) {
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.tab_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.tab_indicator_inactive
                ));
            }
        }

        if (position == onboardingAdapter.getItemCount() - 1) {
            buttonOnboardingAction.setText("Get Started");
        } else {
            buttonOnboardingAction.setText("Next");
        }
    }

    private void completeOnboarding() {
        // Set first-time preference to false
        SharedPreferences preferences = getSharedPreferences("onboarding", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();

        // Navigate to MainActivity
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}