package com.test.readmelater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.readmelater.models.Book;

/**
 * Created by audreyeso on 9/6/16.
 */
public class OpenHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BOOK_DB";
    public static OpenHelper currentInstance;
    public static final String BOOK_TABLE_NAME= "BOOK";
    public static final String COL_ID= "_id";
    public static final String COL_BOOK_TITLE= "TITLE";
    public static final String COL_BOOK_AUTHOR= "AUTHOR";
    public static final String COL_BOOK_ISBN= "ISBN";
    public static final String COL_BOOK_IMAGE ="IMAGE";
    public static final String [] BOOK_COLUMNS = {COL_ID, COL_BOOK_TITLE, COL_BOOK_AUTHOR, COL_BOOK_ISBN, COL_BOOK_IMAGE};

    public static final String CREATE_BOOK_TABLE = "CREATE TABLE " + BOOK_TABLE_NAME +
            "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_BOOK_TITLE + " TEXT, " +
            COL_BOOK_AUTHOR + " TEXT, " +
            COL_BOOK_ISBN + " TEXT, " +
            COL_BOOK_IMAGE + " TEXT)";

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static OpenHelper getCurrentInstance(Context context) {
        if (currentInstance == null) {
            currentInstance = new OpenHelper(context.getApplicationContext());
        }
        return currentInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_BOOK_TABLE);
        this.onCreate(sqLiteDatabase);
    }

    public void addBook (Book book) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOK_TITLE, book.getTitle());
        contentValues.put(COL_BOOK_AUTHOR, book.getAuthor());
        contentValues.put(COL_BOOK_IMAGE, book.getBookImage());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(BOOK_TABLE_NAME,null,contentValues);
        db.close();
    }

    public Cursor getBooks() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(OpenHelper.BOOK_TABLE_NAME,
                null,
                OpenHelper.COL_ID + " = ?",
                new String [] {},
                 // new String[]{id + ""},
                null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void removeBooks(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(BOOK_TABLE_NAME, COL_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

}
