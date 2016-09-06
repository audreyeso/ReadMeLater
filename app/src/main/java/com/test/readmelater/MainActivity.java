package com.test.readmelater;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.test.readmelater.googleApiModels.Example;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    static final String SCAN_MODE = "SCAN_MODE";
    static final String SCAN_RESULT = "SCAN_RESULT";
    static final String SAVE_HISTORY = "SAVE_HISTORY";
    static final String PRODUCT_MODE = "PRODUCT_MODE";
    private String baseUrl = "https://www.googleapis.com/books/v1/";
    private String contents, title, author, full, imageUrl;
    private ArrayList<Book> bookArrayList;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.setAction(ACTION_SCAN);
                intent.putExtra(SAVE_HISTORY, true);
                intent.putExtra(SCAN_MODE, PRODUCT_MODE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void getBookDescription() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GoogleBooksAPI googleBooksAPI = retrofit.create(GoogleBooksAPI.class);

            full = "ISBN" + contents;

            Call<Example> call = googleBooksAPI.getBookDescription(full);

            call.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    try {
                        title = response.body().getItems().get(0).getVolumeInfo().getTitle();
                        author = response.body().getItems().get(0).getVolumeInfo().getAuthors().get(0).toString();
                        imageUrl = response.body().getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail();

                        setUpBooks();

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (imageUrl == null) {
                            imageUrl = "http://images.clipartpanda.com/booklet-clipart-open-book-vector-icon_small.jpg";
                        }
                    }
                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, R.string.no_wifi_connection, Toast.LENGTH_LONG).show();
        }

    }

    public void setUpBooks() {

        if (title == null) {
            Toast.makeText(MainActivity.this, R.string.scan_new_book, Toast.LENGTH_LONG).show();

        } else {
            book = new Book(title, author, imageUrl);
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(contents);
            book.setBookImage(imageUrl);
            bookArrayList.add(book);
            //student.setBookArrayList(bookArrayList);
            //db.addBook(book);
            //customCursorAdapterBooks.swapCursor(db.getBooks(idStudent));
        }
    }

}
