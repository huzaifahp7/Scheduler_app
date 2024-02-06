package com.example.scheduler_app;

import android.app.Application;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.scheduler_app.ReminderWorker;

import java.util.concurrent.TimeUnit;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PeriodicWorkRequest reminderRequest = new PeriodicWorkRequest.Builder(ReminderWorker.class, 1, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueue(reminderRequest);
    }
}
