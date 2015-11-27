package com.example.ios.localizadorradar.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mark on 25/11/2015.
 */
public abstract class GenericDAO<T> implements IDatabaseOperations<T> {
    protected SQLiteDatabase db;

    public GenericDAO(Context context) {
        this.db = new MySQLiteHelper(context).getWritableDatabase();
    }

    public void close() {
        db.close();
    }
}
