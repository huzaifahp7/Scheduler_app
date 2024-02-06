package com.example.scheduler_app;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.database.Cursor;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getApplicationContext());
        dbHelper.openDatabase();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String tomorrowDate = getTomorrowDate();

        checkAssignments(dbHelper, currentDate, tomorrowDate);
        checkExams(dbHelper, currentDate, tomorrowDate);
        checkToDos(dbHelper, currentDate, tomorrowDate);
        checkCourses(dbHelper);
        // For courses, you may need to adjust the logic based on day of the week

        return Result.success();
    }

    private void checkAssignments(MyDatabaseHelper dbHelper, String currentDate, String tomorrowDate) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM Assignment WHERE date = ? OR date = ?", new String[]{currentDate, tomorrowDate});

        while (cursor.moveToNext()) {
            int nameIdx = cursor.getColumnIndex("name");
            int subjectIdx = cursor.getColumnIndex("subject");
            int dateIdx = cursor.getColumnIndex("date");
            int timeIdx = cursor.getColumnIndex("time");

            if (nameIdx != -1 && subjectIdx != -1 && dateIdx != -1 && timeIdx != -1) {
                String name = cursor.getString(nameIdx);
                String subject = cursor.getString(subjectIdx);
                String date = cursor.getString(dateIdx);
                String time = cursor.getString(timeIdx);

                sendNotification("Assignment Due", "Your assignment '" + name + "' in " + subject + " is due on " + date + " at " + time);
            }
        }

        cursor.close();
    }

    private void checkExams(MyDatabaseHelper dbHelper, String currentDate, String tomorrowDate) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM Exam WHERE date = ? OR date = ?", new String[]{currentDate, tomorrowDate});

        while (cursor.moveToNext()) {
            int nameIdx = cursor.getColumnIndex("name");
            int venueIdx = cursor.getColumnIndex("location");
            int dateIdx = cursor.getColumnIndex("date");
            int timeIdx = cursor.getColumnIndex("time");

            if (nameIdx != -1 && venueIdx != -1 && dateIdx != -1 && timeIdx != -1) {
                String name = cursor.getString(nameIdx);
                String venue = cursor.getString(venueIdx);
                String date = cursor.getString(dateIdx);
                String time = cursor.getString(timeIdx);

                sendNotification( "Exam", "Your Exam" + name + " at " + venue + " is on " + date + " at " + time);
            }
        }

        cursor.close();
    }

    private void checkToDos(MyDatabaseHelper dbHelper, String currentDate, String tomorrowDate) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM ToDo WHERE date = ? OR date = ?", new String[]{currentDate, tomorrowDate});

        while (cursor.moveToNext()) {
            int nameIdx = cursor.getColumnIndex("name");
            int dateIdx = cursor.getColumnIndex("date");
            int timeIdx = cursor.getColumnIndex("time");

            if (nameIdx != -1 && dateIdx != -1 && timeIdx != -1) {
                String name = cursor.getString(nameIdx);
                String date = cursor.getString(dateIdx);
                String time = cursor.getString(timeIdx);

                sendNotification("Task Due", "Your task '" + name  + " is due on " + date + " at " + time);
            }
        }

        cursor.close();
    }
    private void checkCourses(MyDatabaseHelper dbHelper) {
        String nextDay = getNextWeekday();

        if (nextDay != null) {
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                    "SELECT * FROM Course WHERE days LIKE ? ORDER BY startTime ASC LIMIT 1", new String[]{"%" + nextDay + "%"});

            if (cursor.moveToFirst()) {
                int nameIdx = cursor.getColumnIndex("name");
                int professorIdx = cursor.getColumnIndex("professor");
                int venueIdx = cursor.getColumnIndex("venue");
                int startTimeIdx = cursor.getColumnIndex("startTime");

                if (nameIdx != -1 && professorIdx != -1 && venueIdx != -1 && startTimeIdx != -1) {
                    String name = cursor.getString(nameIdx);
                    String professor = cursor.getString(professorIdx);
                    String venue = cursor.getString(venueIdx);
                    String startTime = cursor.getString(startTimeIdx);

                    sendNotification("Course Tomorrow", "Your course '" + name + "' with " + professor + " is at " + venue + " tomorrow starting from " + startTime);
                }
            }

            cursor.close();
        }
    }

    private String getNextWeekday() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek >= Calendar.MONDAY && dayOfWeek < Calendar.THURSDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return dayFormat.format(calendar.getTime());
        }

        return null; // No next day query for Friday, Saturday, and Sunday
    }


    private String getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    private void sendNotification(String title, String text) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "reminder_channel_id";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your notification icon
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}

