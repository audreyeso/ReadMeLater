package com.test.readmelater.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.test.readmelater.CustomCursorAdapterBooks;
import com.test.readmelater.OpenHelper;
import com.test.readmelater.R;
import com.test.readmelater.googleApiModels.Example;
import com.test.readmelater.interfaces.GoogleBooksAPI;
import com.test.readmelater.models.Book;

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
    private OpenHelper openHelperDb;
    private CustomCursorAdapterBooks customCursorAdapterBooks;
    //private ProgressBar progressBar;
    private long idBook;
    private ListView bookResultsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bookResultsListView = (ListView) findViewById(R.id.main_activity_result_book_listview);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        setTitle("");

        bookArrayList = new ArrayList<>();
        openHelperDb = OpenHelper.getCurrentInstance(this);

        Cursor cursor = openHelperDb.getBooks();
        customCursorAdapterBooks = new CustomCursorAdapterBooks(this, cursor);
        bookResultsListView.setAdapter(customCursorAdapterBooks);




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
//                        if (imageUrl == null) {
//                            imageUrl = "http://images.clipartpanda.com/booklet-clipart-open-book-vector-icon_small.jpg";
//                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra(SCAN_RESULT);
                DatabaseAsyncTask dbTask = new DatabaseAsyncTask();
                dbTask.execute();
                Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);// change this to splash screen
                startActivity(intent);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.no_wifi, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class DatabaseAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getBookDescription();
            return null;
        }

        @Override
        protected void onPostExecute(Void value) {
            super.onPostExecute(value);
            //progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void setUpBooks() {

        if (title == null) {
            Toast.makeText(MainActivity.this, R.string.scan_new_book, Toast.LENGTH_LONG).show();

        } else {
            book = new Book(title, author, imageUrl, idBook);
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(contents);
            book.setId(idBook);
            book.setBookImage(imageUrl);
            bookArrayList.add(book);


            Log.d("Response", "title" + book.getTitle());
            Log.d("Response","author" +book.getAuthor());


            book.setBookArrayList(bookArrayList);
            openHelperDb.addBook(book);
            customCursorAdapterBooks.swapCursor(openHelperDb.getBooks());

            Toast.makeText(this,book.getTitle() + " added.", Toast.LENGTH_LONG).show();

        }
    }

}
