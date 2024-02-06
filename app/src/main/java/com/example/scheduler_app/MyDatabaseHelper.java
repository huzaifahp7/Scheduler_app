package com.example.scheduler_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 3;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your tables here
        db.execSQL("CREATE TABLE IF NOT EXISTS Assignment (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "subject TEXT," +
                "date TEXT," +
                "time TEXT);");

        // Table for the 'Exam' fragment
        db.execSQL("CREATE TABLE IF NOT EXISTS Exam (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "date TEXT," +
                "time TEXT," +
                "location TEXT);");

        // Table for the 'To Do' fragment
        db.execSQL("CREATE TABLE IF NOT EXISTS ToDo (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "date TEXT," +
                "time TEXT);");

        // Table for the 'Course' fragment
        db.execSQL("CREATE TABLE IF NOT EXISTS Course (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "professor TEXT," +
                "venue TEXT," +
                "days TEXT," +
                "startTime TEXT," +
                "endTime TEXT);");
    }
    public void openDatabase() {
        this.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades here
        // Drop the old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS Assignment");
        db.execSQL("DROP TABLE IF EXISTS Exam");
        db.execSQL("DROP TABLE IF EXISTS ToDo");
        db.execSQL("DROP TABLE IF EXISTS Course");
        onCreate(db);
    }


    //Code for COURSES
    public void addCourse(String name, String professor, String venue, String days, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("professor", professor);
        values.put("venue", venue);
        values.put("days", days);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        db.insert("Course", null, values);
        db.close();
    }
    public Cursor getAllCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Course", null);
    }
    public int updateCourse(int id, String name, String professor, String venue, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("professor", professor);
        values.put("venue", venue);
        values.put("startTime", startTime);
        values.put("endTime", endTime);

        return db.update("Course", values, "_id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteCourse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Course", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public List<CourseModel> getCoursesForDay(String day) {
        List<CourseModel> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for courses on the specified day
        Cursor cursor = db.rawQuery("SELECT * FROM Course WHERE days LIKE ?", new String[]{"%" + day + "%"});



        // Process the cursor and populate the courses list
        if (cursor.moveToFirst()) {
            do {
                int dayIdx = cursor.getColumnIndex("days");
                String days = "";
                if (dayIdx!=-1) {
                    days = cursor.getString(dayIdx);
                }
                if (days != null && days.contains(day)) {
                    int id = 0;
                    String name = "";
                    String professor = "";
                    String venue = "";
                    String startTime = "";
                    String endTime = "";

                    // Assuming CourseModel has a constructor like CourseModel(id, name, professor, time, day, venue)
                    int idIdx = (cursor.getColumnIndex("_id"));
                    int nameIdx = cursor.getColumnIndex("name");
                    int professorIdx = (cursor.getColumnIndex("professor"));
                    int startTimeIdx = (cursor.getColumnIndex("startTime"));
                    int endTimeIdx = (cursor.getColumnIndex("endTime"));
                    int venueIdx = (cursor.getColumnIndex("venue"));


                    if (idIdx != -1) {
                        id = (cursor.getInt(idIdx));
                    }
                    if (nameIdx != -1) {
                        name = cursor.getString(nameIdx);
                    }
                    if (professorIdx != -1) {
                        professor = cursor.getString(professorIdx);
                    }
                    if (venueIdx != -1) {
                        venue = cursor.getString(venueIdx);
                    }
                    if (startTimeIdx != -1) {
                        startTime = cursor.getString(startTimeIdx);
                    }
                    if (startTimeIdx != -1) {
                        endTime = cursor.getString(endTimeIdx);
                    }

                    courses.add(new CourseModel(id, name, professor, venue, days, startTime, endTime));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return courses;
    }

    //CODE for EXAMS
    public void addExam(String name, String time, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        values.put("date", date);
        values.put("location", location);
        db.insert("Exam", null, values);
        db.close();
    }
    public Cursor getAllExams() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Exam", null);
    }
    public int updateExam(int id, String name, String time, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        values.put("date", date);
        values.put("location", location);

        return db.update("Exam", values, "_id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteExam(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Exam", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    //CODE FOR ASSIGNMENTS:
    public void addAssignment(String name, String subject, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("subject", subject);
        values.put("time", time);
        values.put("date", date);
        db.insert("Assignment", null, values);
        db.close();
    }
    public Cursor getAllAssignments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Assignment ORDER BY date ASC", null);
    }
    public int updateAssignment(int id, String name, String subject, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("subject", subject);
        values.put("time", time);
        values.put("date", date);

        return db.update("Assignment", values, "_id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteAssignment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Assignment", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    //Code for ToDos:
    public void addToDo(String name, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        values.put("date", date);
        db.insert("ToDo", null, values);
        db.close();
    }
    public Cursor getToDo() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM ToDo", null);
    }
    public int updateToDo(int id, String name, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        values.put("date", date);

        return db.update("ToDo", values, "_id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteToDo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ToDo", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
