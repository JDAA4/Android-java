
package com.example.exam14noviembre;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etTitle, etAuthor, etPublisher, etPages, etIsbn;
    private DatabaseHelper dbHelper;
    private Button btnSearch, btnList, btnAdd, btnDelete, btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etPublisher = findViewById(R.id.etPublisher);
        etPages = findViewById(R.id.etPages);
        etIsbn = findViewById(R.id.etIsbn);
        btnSearch = findViewById(R.id.btnSearch);
        btnList = findViewById(R.id.btnList);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        dbHelper = new DatabaseHelper(this);

        btnSearch.setOnClickListener(view -> searchBook());

        btnList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListBooksActivity.class);
            startActivity(intent);
        });

        btnAdd.setOnClickListener(view -> addBook());

        btnDelete.setOnClickListener(view -> deleteBook());

        btnUpdate.setOnClickListener(view -> updateBook());
    }

    private void searchBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();

        Cursor cursor = dbHelper.searchBook(title, author);
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");
            int authorIndex = cursor.getColumnIndex("author");
            int publisherIndex = cursor.getColumnIndex("publisher");
            int pagesIndex = cursor.getColumnIndex("pages");
            int isbnIndex = cursor.getColumnIndex("isbn");

            if (titleIndex >= 0 && authorIndex >= 0 && publisherIndex >= 0 && pagesIndex >= 0 && isbnIndex >= 0) {
                etTitle.setText(cursor.getString(titleIndex));
                etAuthor.setText(cursor.getString(authorIndex));
                etPublisher.setText(cursor.getString(publisherIndex));
                etPages.setText(String.valueOf(cursor.getInt(pagesIndex)));
                etIsbn.setText(cursor.getString(isbnIndex));
            } else {
                Toast.makeText(this, "Error al obtener los datos del libro", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Libro no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void addBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String publisher = etPublisher.getText().toString().trim();
        int pages = Integer.parseInt(etPages.getText().toString().trim());
        String isbn = etIsbn.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("author", author);
        values.put("publisher", publisher);
        values.put("pages", pages);
        values.put("isbn", isbn);

        long newRowId = dbHelper.getWritableDatabase().insert("books", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Libro agregado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al agregar el libro", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBook() {
        String title = etTitle.getText().toString().trim();
        int rowsDeleted = dbHelper.deleteBookByTitle(title);
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Libro eliminado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar el libro", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBook() {
        String isbn = etIsbn.getText().toString().trim();
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String publisher = etPublisher.getText().toString().trim();
        int pages = Integer.parseInt(etPages.getText().toString().trim());

        int rowsUpdated = dbHelper.updateBookByIsbn(isbn, title, author, publisher, pages);
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Libro actualizado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar el libro", Toast.LENGTH_SHORT).show();
        }
    }
}