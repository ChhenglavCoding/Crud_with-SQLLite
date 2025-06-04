package com.crud_with_sqllite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crud_with_sqllite.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "UserDB", TABLE_NAME = "users";

    public DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "firstName TEXT, lastName TEXT, gender TEXT, email TEXT, address TEXT, phone TEXT, status TEXT)";
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firstName", user.getFirstName());
        cv.put("lastName", user.getLastName());
        cv.put("gender", user.getGender());
        cv.put("email", user.getEmail());
        cv.put("address", user.getAddress());
        cv.put("phone", user.getPhone());
        cv.put("status", user.getStatus());
        db.insert(TABLE_NAME, null, cv);
    }

    public void updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firstName", user.getFirstName());
        cv.put("lastName", user.getLastName());
        cv.put("gender", user.getGender());
        cv.put("email", user.getEmail());
        cv.put("address", user.getAddress());
        cv.put("phone", user.getPhone());
        cv.put("status", user.getStatus());
        db.update(TABLE_NAME, cv, "id=?", new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(int id) {
        getWritableDatabase().delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            list.add(new User(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
        }
        cursor.close();
        return list;
    }
}
