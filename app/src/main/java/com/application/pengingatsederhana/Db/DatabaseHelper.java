package com.application.pengingatsederhana.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.application.pengingatsederhana.Db.DatabaseContact.ReminderColumns;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbreminderapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_REMINDER = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContact.TABLE_REMINDER,
            ReminderColumns._ID,
            ReminderColumns.TITLE,
            ReminderColumns.DESC,
            ReminderColumns.WAKTU,
            ReminderColumns.CLOCK
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_REMINDER);
    }

    /*
    Method onUpgrade akan di panggil ketika terjadi perbedaan versi
    Gunakan method onUpgrade untuk melakukan proses migrasi data
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        Drop table tidak dianjurkan ketika proses migrasi terjadi dikarenakan data user akan hilang,
         */
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContact.TABLE_REMINDER);
        onCreate(db);
    }

}
