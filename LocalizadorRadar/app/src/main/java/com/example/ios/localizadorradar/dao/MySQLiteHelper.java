package com.example.ios.localizadorradar.dao;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ios.localizadorradar.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mark on 25/11/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "tarefa.db";
    private Context mContext;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Resources resources = mContext.getResources();
            InputStream is = resources.openRawResource(R.raw.create);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null;

            while ((line = br.readLine()) != null){
                db.execSQL(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Resources resources = mContext.getResources();
            InputStream is = resources.openRawResource(R.raw.delete);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null;

            while ((line = br.readLine()) != null){
                db.execSQL(line);
            }

            onCreate(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
