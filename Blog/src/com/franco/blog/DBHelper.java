package com.franco.blog;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by franco on 13-7-24.
 */
public class DBHelper extends SQLiteOpenHelper {

        public static final String TAG = "franco--->DBHelper";
        public static final String TB_NAME = "Drafts";
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";

        public DBHelper(Context context, String name,
                CursorFactory factory, int version) {
            super(context, name, factory, version);
            this.getWritableDatabase();
        }

    public void close() {
        this.getWritableDatabase().close();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TB_NAME + " ("
                + ID + " INTEGER PRIMARY KEY,"
                + TITLE + " VARCHAR,"
                + CONTENT + " VARCHAR)");
    }

    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public void addDraft(String title, String content) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.TITLE, title);
        values.put(DBHelper.CONTENT, content);
        this.getWritableDatabase().insert(
                DBHelper.TB_NAME, DBHelper.ID, values);
    }

    public void updateDraft(String title, String content, long id) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.TITLE, title);
        values.put(DBHelper.CONTENT, content);
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        this.getWritableDatabase().update(TB_NAME, values, whereClause, whereArgs);
    }

    public void delDraft(long id) {
        this.getWritableDatabase().delete(
                DBHelper.TB_NAME, DBHelper.ID + " = " + id, null);
    }

    public void delAllDrafts() {
        this.getWritableDatabase().delete(
                DBHelper.TB_NAME, null, null);
    }
}
