package com.example.myapplication.Askme;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    public static final int TYPE_SENT = 0;
    public static final int TYPE_RECEIVED = 1;

    private String messageText;
    private int messageType;
    private String messageTime;

    public Message(String messageText, int messageType) {
        this.messageText = messageText;
        this.messageType = messageType;

        // Set the message time as current time
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        this.messageTime = sdf.format(new Date());
    }

    public String getMessageText() {
        return messageText;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getMessageTime() {
        return messageTime;
    }
}