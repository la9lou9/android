package com.example.myapplication.Askme;

import android.content.Context;
import android.util.Log;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiService {
    private static final String TAG = "GeminiService";
    private final GenerativeModelFutures model;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Context context;
    private StringBuilder conversationHistory;

    // Initialize with your Gemini API key
    private static final String API_KEY = "AIzaSyCxLiCIwLbPJpLpQXoR46wpo44V-ILc-ZU"; // Replace with your actual API key

    public GeminiService(Context context) {
        this.context = context;

        // Create the GenerativeModel with appropriate settings
        GenerativeModel baseModel = new GenerativeModel(
                "gemini-2.0-flash", // Use the model version you want
                API_KEY
        );

        model = GenerativeModelFutures.from(baseModel);

        // Initialize conversation history
        conversationHistory = new StringBuilder(GeminiApiUtils.getSystemInstructions());
    }

    public interface GeminiResponseCallback {
        void onResponse(String response);
        void onError(String errorMessage);
    }

    public void sendMessage(String prompt, GeminiResponseCallback callback) {
        // Update conversation history with user's message
        conversationHistory.append("\nUser: ").append(prompt);
        conversationHistory.append("\nAssistant: ");

        // Create content with the conversation history
        Content content = new Content.Builder()
                .addText(conversationHistory.toString())
                .build();

        // Generate content asynchronously
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        // Handle the response
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                // Extract the text response
                String responseText = result.getText();
                if (responseText != null && !responseText.isEmpty()) {
                    // Update conversation history with AI response
                    conversationHistory.append(responseText);

                    // Save conversation history
                    GeminiApiUtils.saveConversationHistory(context, conversationHistory.toString());

                    callback.onResponse(responseText);
                } else {
                    callback.onError("Empty response received from Gemini");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Error generating content", t);
                callback.onError("Error: " + t.getMessage());
            }
        }, executor);
    }
}