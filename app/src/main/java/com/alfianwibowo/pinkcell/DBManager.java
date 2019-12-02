package com.alfianwibowo.pinkcell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

class DBManager {

    private SQLiteDatabase sqlDB;
    private static final String DBName = "pink_cell";
    private static final String TName = "data_hp";
    static final String id = "id";
    static final String merk = "merk";
    static final String type = "type";
    static final String ram = "ram";
    static final String internal = "internal";
    static final String kelengkapan = "kelengkapan";
    static final String jaringan = "jaringan";
    static final String deskripsi = "deskripsi";
    static final String harga_jual = "harga_jual";
    static final String harga_jual_maks = "harga_jual_maks";
    private static final int DBVersion = 1;

    private static final String CreateTable = "Create table IF NOT EXISTS " + TName +
            "(id integer PRIMARY KEY AUTOINCREMENT,"+ merk +
            " text," + type + " text," + ram + " text," + internal + " text," + kelengkapan +
            " text," + jaringan + " text," + deskripsi + " text," + harga_jual +
            " text," + harga_jual_maks + " text);";

    static class DatabaseHelperUser extends SQLiteOpenHelper {

        Context context;
        DatabaseHelperUser(Context context) {
            super(context,DBName,null,DBVersion);
            this.context=context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table IF EXISTS "+ TName);
            onCreate(db);
        }
    }

    DBManager(Context context) {
        DatabaseHelperUser db = new DatabaseHelperUser(context);
        sqlDB=db.getWritableDatabase();
    }

    public void Insert(ContentValues values) {
        sqlDB.insert(TName, "", values);
    }

    public Cursor Query(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TName);
        return queryBuilder.query(sqlDB, Projection, Selection, SelectionArgs,
                null, null, SortOrder);
    }

    public int Delete(String selection, String[] selectionargs) {
        return sqlDB.delete(TName, selection, selectionargs);
    }

    public int Update(ContentValues contentValues, String selection, String[] selectionArgs) {
        return sqlDB.update(TName, contentValues, selection, selectionArgs);
    }

}
