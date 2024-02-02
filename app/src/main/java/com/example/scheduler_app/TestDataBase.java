//package com.example.scheduler_app;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import java.util.ArrayList;
//import java.util.List;
//import com.example.scheduler_app.MyDatabaseHelper;
//
//public class TestDataBase {
//    public List<String> getExistingTables(SQLiteDatabase db) {
//        List<String> tableNames = new ArrayList<>();
//        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' AND name!='sqlite_sequence'";
//        Cursor cursor = db.rawQuery(query, null);
//
//        if (cursor.moveToFirst()) {
//            while (!cursor.isAfterLast()) {
//                //tableNames.add(cursor.getString(cursor.getColumnIndex()));
//                cursor.moveToNext();
//            }
//        }
//        cursor.close();
//        return tableNames;
//    }
//
//    // Assuming DBHelper is your SQLiteOpenHelper subclass
//    public static void main(String[] args) {
//
//    }
//    public void print(){
//        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
//
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        List<String> tables = getExistingTables(db);
//// Now 'tables' contains the names of all existing tables
//        for (String table : tables){
//            Log.d("Database Table", table);
//        }}
//}

