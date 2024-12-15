package com.example.gametrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

    private static final String DB_NOMBRE = "videogames.db";
    private static final int DB_VERSION = 1;

    public static final String TABLA_VIDEOGAMES = "videogames";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_PLATFORM = "platform";
    public static final String COLUMN_DEVELOPER = "developer";
    public static final String COLUMN_RELEASE_YEAR = "release_year";
    public static final String COLUMN_OWNED = "owned";

    public DBManager(Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i( "DBManager", "Creando BBDD " + DB_NOMBRE + " v" + DB_VERSION);

        try {
            db.beginTransaction();
            db.execSQL("CREATE TABLE " + TABLA_VIDEOGAMES + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_TITLE + " TEXT NOT NULL, "
                    + COLUMN_GENRE + " TEXT NOT NULL, "
                    + COLUMN_PLATFORM + " TEXT NOT NULL, "
                    + COLUMN_DEVELOPER + " TEXT NOT NULL, "
                    + COLUMN_RELEASE_YEAR + " INTEGER NOT NULL, "
                    + COLUMN_OWNED + " INTEGER NOT NULL)");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e( "DBManager.onCreate", e.getMessage() );
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(  "DBManager", "DB: " + DB_NOMBRE + ": v" + oldVersion + " -> v" + newVersion );

        try {
            db.beginTransaction();
            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_VIDEOGAMES );
            db.setTransactionSuccessful();
        }  catch(SQLException exc) {
            Log.e( "DBManager.onUpgrade", exc.getMessage() );
        }
        finally {
            db.endTransaction();
        }

        this.onCreate( db );
    }

    public long insertVideogame(String title, String genre, String platform, String developer, int releaseYear, boolean owned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_GENRE, genre);
        values.put(COLUMN_PLATFORM, platform);
        values.put(COLUMN_DEVELOPER, developer);
        values.put(COLUMN_RELEASE_YEAR, releaseYear);
        values.put(COLUMN_OWNED, owned ? 1 : 0);
        return db.insert(TABLA_VIDEOGAMES, null, values);
    }

    public int updateVideogame(long id, String title, String genre, String platform, String developer, int releaseYear, boolean owned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_GENRE, genre);
        values.put(COLUMN_PLATFORM, platform);
        values.put(COLUMN_DEVELOPER, developer);
        values.put(COLUMN_RELEASE_YEAR, releaseYear);
        values.put(COLUMN_OWNED, owned ? 1 : 0);
        return db.update(TABLA_VIDEOGAMES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteVideogame(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLA_VIDEOGAMES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllVideogames() {
        return this.getReadableDatabase().query(TABLA_VIDEOGAMES, null, null, null, null, null, null);
    }

    public Cursor getVideogameById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_VIDEOGAMES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }
}
