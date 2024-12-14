package com.example.gametrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL("CREATE TABLE " + TABLA_COMMENTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GAME_ID + " INTEGER NOT NULL, "
                + COLUMN_COMMENT + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + COLUMN_GAME_ID + ") REFERENCES videogames(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_COMMENTS);
        onCreate(db);
    }

    public long insertComment(long gameId, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_ID, gameId);
        values.put(COLUMN_COMMENT, comment);
        return db.insert(TABLA_COMMENTS, null, values);
    }

    public Cursor getCommentByGameId(long gameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_COMMENTS, null, COLUMN_GAME_ID + " = ?", new String[]{String.valueOf(gameId)}, null, null, null);
    }
}

