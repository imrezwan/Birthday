package com.example.birthday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class BirthDatabase {
    SQLiteDatabase sqLiteDatabase;
    DBOpenHelper sqLiteOpenHelper;

    public static final String CONTACT_DATABASE = "Contact Database";

    public BirthDatabase(Context context) {
        sqLiteOpenHelper = new DBOpenHelper(context);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteOpenHelper.close();
    }

    public boolean insertContact(ContactModel contactModel) {
        this.open();
        ContentValues contentValues = new ContentValues();
        Log.d(CONTACT_DATABASE, "insert staret");
        contentValues.put(DBOpenHelper.COL_NAME, contactModel.getName());
        contentValues.put(DBOpenHelper.COL_CONTACT, contactModel.getContact());
        contentValues.put(DBOpenHelper.COL_DAY, contactModel.getDay());
        contentValues.put(DBOpenHelper.COL_MONTH, contactModel.getMonth());
        contentValues.put(DBOpenHelper.COL_YEAR, contactModel.getYear());

        long inserted = sqLiteDatabase.insert(sqLiteOpenHelper.DB_NAME,null, contentValues);
        this.close();
        Log.d(CONTACT_DATABASE, "insert end");
        if (inserted > 0) {
            return true;
        }
        return false;
    }


    public ArrayList<ContactModel> getAllContact() {
        this.open();
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        //Log.d(CONTACT_DATABASE, "get all start");

        Cursor cursor = sqLiteDatabase.query(DBOpenHelper.DB_NAME, null, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id_idx = cursor.getColumnIndex(DBOpenHelper.COL_ID);
                int name_idx = cursor.getColumnIndex(DBOpenHelper.COL_NAME);
                int contact_idx = cursor.getColumnIndex(DBOpenHelper.COL_CONTACT);
                int day_idx = cursor.getColumnIndex(DBOpenHelper.COL_DAY);
                int month_idx = cursor.getColumnIndex(DBOpenHelper.COL_MONTH);
                int year_idx = cursor.getColumnIndex(DBOpenHelper.COL_YEAR);
                arrayList.add(new ContactModel(
                        cursor.getInt(id_idx),
                        cursor.getString(name_idx),
                        cursor.getString(contact_idx),
                        cursor.getInt(day_idx),
                        cursor.getInt(month_idx),
                        cursor.getInt(year_idx)
                    )
                );
            } while (cursor.moveToNext());
        }
        this.close();
        cursor.close();
        //Log.d(CONTACT_DATABASE, "get all end");
        return arrayList;
    }

    public boolean deleteItem(int id) {
        this.open();
        int deleted = sqLiteDatabase.delete(DBOpenHelper.DB_NAME, sqLiteOpenHelper.COL_ID + "=?", new String[]{String.valueOf(id)});
        this.close();
        if (deleted > 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean updateItem(ContactModel contactModel){
        this.open();

        ContentValues contentValues = new ContentValues();
        Log.d(CONTACT_DATABASE, "update start");
        contentValues.put(DBOpenHelper.COL_NAME, contactModel.getName());
        contentValues.put(DBOpenHelper.COL_CONTACT, contactModel.getContact());
        contentValues.put(DBOpenHelper.COL_DAY, contactModel.getDay());
        contentValues.put(DBOpenHelper.COL_MONTH, contactModel.getMonth());
        contentValues.put(DBOpenHelper.COL_YEAR, contactModel.getYear());

        int updated =   sqLiteDatabase.update(DBOpenHelper.DB_NAME,contentValues,DBOpenHelper.COL_ID + "= ?",new String[]{String.valueOf(contactModel.getId())});
        this.close();
        Log.d(CONTACT_DATABASE, "update end "+ updated);
        if (updated > 0) {
            return true;
        } else {
            return false;
        }
    }
}