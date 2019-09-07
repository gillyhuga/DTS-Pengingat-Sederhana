package com.application.pengingatsederhana.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.application.pengingatsederhana.Entity.Reminder;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.application.pengingatsederhana.Db.DatabaseContact.ReminderColumns.TITLE;
import static com.application.pengingatsederhana.Db.DatabaseContact.ReminderColumns.CLOCK;
import static com.application.pengingatsederhana.Db.DatabaseContact.ReminderColumns.DESC;
import static com.application.pengingatsederhana.Db.DatabaseContact.ReminderColumns.WAKTU;
import static com.application.pengingatsederhana.Db.DatabaseContact.TABLE_REMINDER;

public class ReminderHelper {private static String DATABASE_TABLE = TABLE_REMINDER;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public ReminderHelper(Context context){
        this.context = context;
    }

    public ReminderHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    /**
     * Gunakan method ini untuk ambil semua note yang ada
     * Otomatis di parsing ke dalam model Note
     * @return hasil query berbentuk array model note
     */
    public ArrayList<Reminder> query(){
        ArrayList<Reminder> arrayList = new ArrayList<Reminder>();
        Cursor cursor = database.query(DATABASE_TABLE,null,null,null,null,null,_ID +" DESC",null);
        cursor.moveToFirst();
        Reminder reminder;
        if (cursor.getCount()>0) {
            do {

                reminder = new Reminder();
                reminder.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                reminder.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                reminder.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(DESC)));
                reminder.setWaktu(cursor.getString(cursor.getColumnIndexOrThrow(WAKTU)));
                reminder.setClock(cursor.getString(cursor.getColumnIndexOrThrow(CLOCK)));

                arrayList.add(reminder);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * Gunakan method ini untuk query insert
     * @param note model note yang akan dimasukkan
     * @return id dari data yang baru saja dimasukkan
     */
    public long insert(Reminder reminder){
        ContentValues initialValues =  new ContentValues();
        initialValues.put(TITLE, reminder.getTitle());
        initialValues.put(DESC, reminder.getDesc());
        initialValues.put(WAKTU, reminder.getWaktu());
        initialValues.put(CLOCK, reminder.getClock());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Gunakan method ini untuk query update
     * @param note model note yang akan diubah
     * @return int jumlah dari row yang ter-update, jika tidak ada yang diupdate maka nilainya 0
     */
    public int update(Reminder reminder){
        ContentValues args = new ContentValues();
        args.put(TITLE, reminder.getTitle());
        args.put(DESC, reminder.getDesc());
        args.put(WAKTU, reminder.getWaktu());
        args.put(CLOCK, reminder.getClock());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + reminder.getId() + "'", null);
    }

    /**
     * Gunakan method ini untuk query delete
     * @param id id yang akan di delete
     * @return int jumlah row yang di delete
     */
    public int delete(int id){
        return database.delete(TABLE_REMINDER, _ID + " = '"+id+"'", null);
    }
}
