# Sa7tek fi jibek (Health in your Pocket)

## Overview

Sa7tek fi jibek (Health in your Pocket) is a comprehensive Android application designed to help users manage their health information effectively and access AI-powered health assistance. The app features appointment scheduling, medication tracking, emergency contacts, prescription management, a calendar, and a Gemini-powered health assistant chatbot.

## Features

- **Appointments Management**: Schedule and track medical appointments
- **Medication Reminders**: Keep track of medications and dosages
- **Emergency Contacts**: Quick access to important contacts
- **Prescription Tracker**: Store and manage prescriptions
- **Health Assistant Chatbot**: AI-powered health advice using Google's Gemini
- **Calendar**: Integrated health event calendar

## Screenshots

*[Add screenshots of your app here]*

## Technology Stack

- Java
- Android SDK
- Google's Gemini AI API
- AndroidX Libraries
- Material Design Components

## Gemini AI Integration

The application uses Google's Gemini AI to provide a conversational health assistant that can:
- Answer health-related questions
- Provide general medical information
- Offer wellness tips and advice
- Direct users to appropriate medical resources

## Requirements

- Android Studio Arctic Fox or newer
- Android SDK level 24+ (Android 7.0 Nougat or higher)
- Google Gemini API key
- JDK 8+

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/sa7tek-fi-jibek.git
   cd sa7tek-fi-jibek
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned repository and open

3. **Configure Gemini API Key**
   - Get your API key from [Google AI Studio](https://ai.google.dev/)
   - Open `app/build.gradle`
   - Replace `YOUR_GEMINI_API_KEY_HERE` with your actual API key:
     ```gradle
     buildConfigField "String", "GEMINI_API_KEY", "\"YOUR_ACTUAL_API_KEY\""
     ```

4. **Install Dependencies**
   - Add Lottie animation library to app-level `build.gradle` (for typing indicators):
     ```gradle
     implementation 'com.airbnb.lottie:lottie-android:5.2.0'
     ```

5. **Run the Application**
   - Connect your Android device or use an emulator
   - Click the "Run" button in Android Studio

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/sa7tekfijibek/
│   │   ├── MainActivity.java          # Main screen with feature grid
│   │   ├── ChatbotActivity.java       # Gemini chatbot interface
│   │   ├── ChatAdapter.java           # Adapter for chat messages
│   │   ├── ChatMessage.java           # Data model for chat messages
│   │   └── GeminiService.java         # Gemini AI integration service
│   │
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml      # Main grid layout
│   │   │   ├── activity_chatbot.xml   # Chat interface layout
│   │   │   ├── item_chat_user.xml     # User message layout
│   │   │   └── item_chat_bot.xml      # Bot message layout
│   │   │
│   │   ├── drawable/                  # Icons and backgrounds
│   │   └── values/                    # Colors, strings, styles
│   │
└── build.gradle                       # App-level build config
```

## How to Use

1. **Launch the App**
   - Open the app to see the main grid with feature buttons

2. **Access the Chatbot**
   - Tap the chatbot icon (user icon) from the main grid
   - Type your health question in the input field
   - Press the send button to receive AI responses

3. **Main Features**
   - **Appointments**: Track doctor visits and medical appointments
   - **Medications**: Set reminders for taking medications
   - **Emergency Contacts**: Store important medical contacts
   - **Prescriptions**: Keep digital copies of prescriptions
   - **Calendar**: View all health events in calendar format

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the [MIT License](LICENSE.md) - see the LICENSE file for details.

## Acknowledgements

- Google Gemini API for AI capabilities
- Material Design for UI components
- Android Jetpack libraries
- [List any other libraries or resources used]

## Contact

Your Name - [@your_twitter](https://twitter.com/your_twitter) - email@example.com

Project Link: [https://github.com/yourusername/sa7tek-fi-jibek](https://github.com/yourusername/sa7tek-fi-jibek)
