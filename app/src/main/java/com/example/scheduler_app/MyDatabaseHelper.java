package com.example.scheduler_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your tables here
        String createTableQuery = "CREATE TABLE IF NOT EXISTS mytable ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "course TEXT,"
                + "professor TEXT,"
                + "time TEXT,"
                + "date TEXT,"
                + "location TEXT);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades here
        db.execSQL("DROP TABLE IF EXISTS mytable");
        onCreate(db);
    }
}
