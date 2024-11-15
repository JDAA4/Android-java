package com.example.exam14noviembre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BOOKS = "books";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "author TEXT,"
                + "publisher TEXT,"
                + "pages INTEGER,"
                + "isbn TEXT)";
        db.execSQL(CREATE_TABLE_BOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    public Cursor searchBook(String title, String author) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE title LIKE ? AND author LIKE ?";
        return db.rawQuery(query, new String[]{"%" + title + "%", "%" + author + "%"});
    }

    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
    }

    public Cursor getBookById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(id)});
    }

    public int deleteBookByTitle(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_BOOKS, "title = ?", new String[]{title});
    }

    public int updateBookByIsbn(String isbn, String title, String author, String publisher, int pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("author", author);
        values.put("publisher", publisher);
        values.put("pages", pages);
        return db.update(TABLE_BOOKS, values, "isbn = ?", new String[]{isbn});
    }
}