package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView eventsRecyclerView;
    private FloatingActionButton fabAddEvent;

    // This is a simple in-memory storage for events - in a real app you'd use a database
    private Map<String, List<CalendarEvent>> eventsByDate = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_calendar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Calendar");

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        eventsRecyclerView = findViewById(R.id.events_recycler_view);
        fabAddEvent = findViewById(R.id.fab_add_event);

        // Set up RecyclerView
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // For demonstration, let's add some sample events
        addSampleEvents();

        // Set up calendar date change listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Month is 0-based in Calendar
                String dateKey = formatDateKey(year, month, dayOfMonth);
                updateEventsForDate(dateKey);
            }
        });

        // Set up add event button
        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the currently selected date
                long dateMillis = calendarView.getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateMillis);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // In a real app, you would show a dialog to add a new event
                // For this example, we'll just add a hardcoded event
                String dateKey = formatDateKey(year, month, day);
                addEvent(dateKey, "New Health Event", "Default description");
                updateEventsForDate(dateKey);

                Toast.makeText(CalendarActivity.this, "New event added!", Toast.LENGTH_SHORT).show();
            }
        });

        // Show events for the current date
        Calendar calendar = Calendar.getInstance();
        String currentDateKey = formatDateKey(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        updateEventsForDate(currentDateKey);
    }

    private String formatDateKey(int year, int month, int day) {
        // Create a consistent date key format: YYYY-MM-DD
        return year + "-" + (month + 1) + "-" + day;
    }

    private void addEvent(String dateKey, String title, String description) {
        CalendarEvent event = new CalendarEvent();
        event.setTitle(title);
        event.setDescription(description);
        event.setDate(dateKey);

        if (!eventsByDate.containsKey(dateKey)) {
            eventsByDate.put(dateKey, new ArrayList<>());
        }

        eventsByDate.get(dateKey).add(event);
    }

    private void updateEventsForDate(String dateKey) {
        List<CalendarEvent> events = eventsByDate.getOrDefault(dateKey, new ArrayList<>());
        EventAdapter adapter = new EventAdapter(events);
        eventsRecyclerView.setAdapter(adapter);
    }

    private void addSampleEvents() {
        // Add some sample events for demonstration
        Calendar calendar = Calendar.getInstance();

        // Event for today
        String today = formatDateKey(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        addEvent(today, "Doctor Appointment", "Annual checkup with Dr. Smith");
        addEvent(today, "Take Medication", "Take prescription at 8 PM");

        // Event for tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrow = formatDateKey(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        addEvent(tomorrow, "Lab Test", "Blood work at City Hospital");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Model class for calendar events
    public static class CalendarEvent {
        private String title;
        private String description;
        private String date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    // Adapter for displaying events in the RecyclerView
    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        private List<CalendarEvent> events;

        public EventAdapter(List<CalendarEvent> events) {
            this.events = events;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            // Create a new view using the event_item layout (which we'll create next)
            android.view.View view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            CalendarEvent event = events.get(position);
            holder.eventTitle.setText(event.getTitle());
            holder.eventDescription.setText(event.getDescription());
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        class EventViewHolder extends RecyclerView.ViewHolder {
            android.widget.TextView eventTitle;
            android.widget.TextView eventDescription;

            public EventViewHolder(@NonNull android.view.View itemView) {
                super(itemView);
                eventTitle = itemView.findViewById(R.id.event_title);
                eventDescription = itemView.findViewById(R.id.event_description);
            }
        }
    }
}