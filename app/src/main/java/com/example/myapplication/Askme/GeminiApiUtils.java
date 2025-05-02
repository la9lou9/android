package com.example.myapplication.Askme;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for Gemini API related functions
 */
public class GeminiApiUtils {

    private static final String PREFS_NAME = "GeminiPrefs";
    private static final String KEY_CONVERSATION_HISTORY = "conversation_history";

    // Maximum number of conversation turns to store
    private static final int MAX_HISTORY_TURNS = 10;

    /**
     * Save conversation history to SharedPreferences
     *
     * @param context Application context
     * @param conversationHistory String containing the conversation history
     */
    public static void saveConversationHistory(Context context, String conversationHistory) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_CONVERSATION_HISTORY, conversationHistory);
        editor.apply();
    }

    /**
     * Retrieve conversation history from SharedPreferences
     *
     * @param context Application context
     * @return String containing the conversation history
     */
    public static String getConversationHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_CONVERSATION_HISTORY, "");
    }

    /**
     * Add system instructions to guide the Gemini model's behavior
     *
     * @return String containing system instructions
     */
    public static String getSystemInstructions() {
        return "You are a helpful health assistant AI named Sa7tek fi Jibek. " +
                "Your primary role is to provide accurate, helpful health information while being conversational and empathetic. " +
                "Always clarify that you're an AI and not a medical professional. " +
                "For serious health concerns, recommend consulting a healthcare provider. " +
                "Keep your responses concise, informative, and easy to understand. " +
                "Focus on general health advice, wellness tips, medication reminders, and lifestyle guidance. " +
                "Avoid diagnosing conditions or recommending specific treatments.";
    }

    /**
     * Format a health-related prompt with system instructions
     *
     * @param userMessage The user's message
     * @return Formatted prompt with system instructions
     */
    public static String formatHealthPrompt(String userMessage) {
        return getSystemInstructions() + "\n\nUser: " + userMessage + "\n\nAssistant:";
    }
}