package com.application.pengingatsederhana.Db;

import android.provider.BaseColumns;

public class DatabaseContact {
    static String TABLE_REMINDER = "reminder";

    static final class ReminderColumns implements BaseColumns {

        //Note title
        static String TITLE = "title";
        //Note description
        static String DESC = "desc";
        //Note waktu
        static String WAKTU = "waktu";
        //Note clock
        static String CLOCK = "clock";

    }

}

