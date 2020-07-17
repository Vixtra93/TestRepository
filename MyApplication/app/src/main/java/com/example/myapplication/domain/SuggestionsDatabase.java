package com.example.myapplication.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SuggestionsDatabase {

    public static final String DB_SUGGESTION = "SUGGESTION_DB";
    public final static String TABLE_SUGGESTION = "SUGGESTION_TB";
    public final static String FIELD_ID = "_id";
    public final static String FIELD_SUGGESTION = "suggestion";

    private SQLiteDatabase db;
    private Helper helper;
    private Context ctx;

    public SuggestionsDatabase(Context context) {

        ctx = context;
        helper = new Helper(context, DB_SUGGESTION, null, 1);
        db = helper.getWritableDatabase();
    }


    public boolean isSearchQueryRepeat(String newSearchQuery) {

        String[] params = new String[]{newSearchQuery};
        Cursor c = db.rawQuery("select * from " + TABLE_SUGGESTION +
                        " WHERE " + FIELD_SUGGESTION + " = ?",
                params);


        return c.moveToNext();


    }

    public long insertSuggestion(String text) {
        Cursor mCount = db.rawQuery("select count(*) from " + TABLE_SUGGESTION, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        if (count >= 10) {
            Cursor cursorDelete = getSuggestions();
            cursorDelete.moveToFirst();
            deleteSuggestion(cursorDelete.getString(0));
        }


        ContentValues values = new ContentValues();
        values.put(FIELD_SUGGESTION, text.trim());
        return !isSearchQueryRepeat(text.trim()) ? db.insert(TABLE_SUGGESTION, null, values) : -1;


    }


    public long deleteSuggestion(String id) {
        return db.delete(TABLE_SUGGESTION, FIELD_ID + "= " + id, null);
    }

    public Cursor getSuggestions() {


        Cursor cursorSuggestion = db.rawQuery("select * from " + TABLE_SUGGESTION, null);


        return cursorSuggestion;


    }


    private class Helper extends SQLiteOpenHelper {

        public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_SUGGESTION + " (" +
                    FIELD_ID + " integer primary key autoincrement, " + FIELD_SUGGESTION + " text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

}