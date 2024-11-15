package com.example.exam14noviembre;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListBooksActivity extends AppCompatActivity {
    private Spinner spinnerBooks;
    private List<Book> bookList;
    private DatabaseHelper databaseHelper;
    private boolean isSpinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_books);

        spinnerBooks = findViewById(R.id.spinnerBooks);

        databaseHelper = new DatabaseHelper(this);
        bookList = getBooksFromCursor(databaseHelper.getAllBooks());

        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBooks.setAdapter(adapter);

        spinnerBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerInitialized) {
                    Book selectedBook = (Book) parent.getItemAtPosition(position);
                    // Crear un Intent para BookDetailsActivity y pasar los datos del libro seleccionado
                    Intent intent = new Intent(ListBooksActivity.this, BookDetailsActivity.class);
                    intent.putExtra("bookId", selectedBook.getId());
                    startActivity(intent);
                } else {
                    isSpinnerInitialized = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private List<Book> getBooksFromCursor(Cursor cursor) {
        List<Book> books = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int titleIndex = cursor.getColumnIndex("title");
            int authorIndex = cursor.getColumnIndex("author");
            int publisherIndex = cursor.getColumnIndex("publisher");
            int pagesIndex = cursor.getColumnIndex("pages");
            int isbnIndex = cursor.getColumnIndex("isbn");

            if (idIndex >= 0 && titleIndex >= 0 && authorIndex >= 0 && publisherIndex >= 0 && pagesIndex >= 0 && isbnIndex >= 0) {
                do {
                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String author = cursor.getString(authorIndex);
                    String publisher = cursor.getString(publisherIndex);
                    int pages = cursor.getInt(pagesIndex);
                    String isbn = cursor.getString(isbnIndex);
                    books.add(new Book(id, title, author, publisher, pages, isbn));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return books;
    }
}