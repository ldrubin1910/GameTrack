package com.example.gametrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CommentDBManager extends SQLiteOpenHelper {

    private static final String DB_NOMBRE = "comments.db";
    private static final int DB_VERSION = 1;

    public static final String TABLA_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GAME_ID = "game_id";
    public static final String COLUMN_COMMENT = "comment";

    public CommentDBManager(Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("CREATE TABLE " + TABLA_COMMENTS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_GAME_ID + " INTEGER NOT NULL, "
                    + COLUMN_COMMENT + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + COLUMN_GAME_ID + ") REFERENCES videogames(_id))");
            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("CommentDBManager.onCreate", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_COMMENTS);
            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("CommentDBManager.onUpgrade", exc.getMessage());
        } finally {
            db.endTransaction();
        }
        this.onCreate(db);
    }

    public void insertComment(long gameId, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_ID, gameId);
        values.put(COLUMN_COMMENT, comment);

        try {
            db.beginTransaction();
            db.insert(TABLA_COMMENTS, null, values);
            db.setTransactionSuccessful();

        }  catch (SQLException exc) {
            Log.e("CommentDBManager.insert", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public String getCommentByGameId(long gameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLA_COMMENTS,
                new String[]{COLUMN_COMMENT},
                COLUMN_GAME_ID + " = ?",
                new String[]{String.valueOf(gameId)},
                null, null, null, null);

        String comment = null;
        if (cursor != null && cursor.moveToFirst()) {
            comment = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT));
            cursor.close();
        }
        return comment;
    }

    public void deleteCommentByGameId(long gameId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            db.delete(TABLA_COMMENTS, COLUMN_GAME_ID + " = ?", new String[]{String.valueOf(gameId)});
            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("CommentDBManager.delete", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

}

