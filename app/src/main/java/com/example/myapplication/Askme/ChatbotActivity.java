package com.example.myapplication.Askme;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private GeminiService geminiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.chatbot_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Health Assistant");

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_chat);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);

        // Initialize the Gemini service
        geminiService = new GeminiService(this);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Add welcome message
        Message welcomeMessage = new Message("Hello! I'm your health assistant. How can I help you today?", Message.TYPE_RECEIVED);
        messageList.add(welcomeMessage);
        messageAdapter.notifyDataSetChanged();

        // Set up send button click listener
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText);
                }
            }
        });
    }

    private void sendMessage(String messageText) {
        // Add the user message to the list
        Message userMessage = new Message(messageText, Message.TYPE_SENT);
        messageAdapter.addMessage(userMessage);

        // Scroll to the bottom
        recyclerView.smoothScrollToPosition(messageList.size() - 1);

        // Clear the input field
        editTextMessage.setText("");

        // Disable the send button while waiting for a response
        buttonSend.setEnabled(false);

        // Show typing indicator or loading state if needed
        // ...

        // Send the message to Gemini API
        geminiService.sendMessage(messageText, new GeminiService.GeminiResponseCallback() {
            @Override
            public void onResponse(String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Add the AI response to the message list
                        Message aiMessage = new Message(response, Message.TYPE_RECEIVED);
                        messageAdapter.addMessage(aiMessage);

                        // Scroll to the bottom
                        recyclerView.smoothScrollToPosition(messageList.size() - 1);

                        // Re-enable the send button
                        buttonSend.setEnabled(true);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Show error message
                        Toast.makeText(ChatbotActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                        // Add an error message to the chat
                        Message errorMsg = new Message("Sorry, I couldn't process your request. Please try again.", Message.TYPE_RECEIVED);
                        messageAdapter.addMessage(errorMsg);

                        // Scroll to the bottom
                        recyclerView.smoothScrollToPosition(messageList.size() - 1);

                        // Re-enable the send button
                        buttonSend.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}