package com.example.exam14noviembre;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {
    private TextView tvTitle, tvAuthor, tvPublisher, tvPages, tvIsbn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublisher = findViewById(R.id.tvPublisher);
        tvPages = findViewById(R.id.tvPages);
        tvIsbn = findViewById(R.id.tvIsbn);
        dbHelper = new DatabaseHelper(this);

        int bookId = getIntent().getIntExtra("bookId", -1);
        displayBookDetails(bookId);
    }

    private void displayBookDetails(int bookId) {
        Cursor cursor = dbHelper.getBookById(bookId);
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");
            int authorIndex = cursor.getColumnIndex("author");
            int publisherIndex = cursor.getColumnIndex("publisher");
            int pagesIndex = cursor.getColumnIndex("pages");
            int isbnIndex = cursor.getColumnIndex("isbn");

            if (titleIndex >= 0 && authorIndex >= 0 && publisherIndex >= 0 && pagesIndex >= 0 && isbnIndex >= 0) {
                tvTitle.setText(cursor.getString(titleIndex));
                tvAuthor.setText(cursor.getString(authorIndex));
                tvPublisher.setText(cursor.getString(publisherIndex));
                tvPages.setText(String.valueOf(cursor.getInt(pagesIndex)));
                tvIsbn.setText(cursor.getString(isbnIndex));
            } else {
                Toast.makeText(this, "Error al obtener los datos del libro", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Libro no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}