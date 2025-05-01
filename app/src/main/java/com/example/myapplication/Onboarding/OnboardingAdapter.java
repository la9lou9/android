package com.example.myapplication.Onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private Context context;
    private List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(Context context, List<OnboardingItem> onboardingItems) {
        this.context = context;
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.onboarding_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText;
        private TextView descriptionText;
        private ImageView imageView;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.onboardingTitle);
            descriptionText = itemView.findViewById(R.id.onboardingDescription);
            imageView = itemView.findViewById(R.id.onboardingImage);
        }

        void setOnboardingData(OnboardingItem onboardingItem) {
            titleText.setText(onboardingItem.getTitle());
            descriptionText.setText(onboardingItem.getDescription());
            imageView.setImageResource(onboardingItem.getImage());
        }
    }
}