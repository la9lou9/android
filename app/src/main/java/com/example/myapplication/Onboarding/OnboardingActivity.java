package com.example.myapplication.Onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout indicatorLayout;
    private Button skipButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if onboarding has been completed before
        if (isOnboardingCompleted()) {
            navigateToMainActivity();
            return;
        }

        setContentView(R.layout.onboarding_screen);

        indicatorLayout = findViewById(R.id.indicatorLayout);
        skipButton = findViewById(R.id.skipButton);
        nextButton = findViewById(R.id.nextButton);

        setupOnboardingItems();

        final ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupIndicators();
        setCurrentIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);

                if (position == onboardingAdapter.getItemCount() - 1) {
                    nextButton.setText("Get Started");
                } else {
                    nextButton.setText("Next");
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeOnboarding();
                navigateToMainActivity();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                } else {
                    completeOnboarding();
                    navigateToMainActivity();
                }
            }
        });
    }

    private void setupOnboardingItems() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem welcomeItem = new OnboardingItem(
                R.drawable.onboarding_welcome,
                "Welcome to Sa7tek fi jibek",
                "Your personal health assistant that helps you stay organized and healthy."
        );

        OnboardingItem appointmentsItem = new OnboardingItem(
                R.drawable.rendezvousf,
                "Track Medical Appointments",
                "Never miss a doctor's appointment again. Set reminders and keep all your medical visits in one place."
        );

        OnboardingItem medicationsItem = new OnboardingItem(
                R.drawable.pillsf,
                "Medication Reminders",
                "Set up reminders for your medication. Take your pills on time, every time."
        );

        OnboardingItem emergencyItem = new OnboardingItem(
                R.drawable.contactf,
                "Emergency Contacts",
                "Store your emergency contacts for quick access when you need them most."
        );

        OnboardingItem prescriptionsItem = new OnboardingItem(
                R.drawable.ordonnancef,
                "Digital Prescriptions",
                "Keep your prescriptions organized and accessible whenever you need them."
        );

        OnboardingItem chatbotItem = new OnboardingItem(
                R.drawable.userf,
                "Health Assistant",
                "Get answers to your health questions with our intelligent chatbot."
        );

        onboardingItems.add(welcomeItem);
        onboardingItems.add(appointmentsItem);
        onboardingItems.add(medicationsItem);
        onboardingItems.add(emergencyItem);
        onboardingItems.add(prescriptionsItem);
        onboardingItems.add(chatbotItem);

        onboardingAdapter = new OnboardingAdapter(getApplicationContext(), onboardingItems);
    }

    private void setupIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            indicatorLayout.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = indicatorLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) indicatorLayout.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.indicator_inactive
                ));
            }
        }
    }

    private boolean isOnboardingCompleted() {
        SharedPreferences sharedPreferences = getSharedPreferences("onboarding_pref", MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_completed", false);
    }

    private void completeOnboarding() {
        SharedPreferences sharedPreferences = getSharedPreferences("onboarding_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_completed", true);
        editor.apply();
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}