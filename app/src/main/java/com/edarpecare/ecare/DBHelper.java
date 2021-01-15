package com.edarpecare.ecare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HomeVisitDB.db";
    public static final String HOMEVISIT_TABLE_NAME = "nyumbani";
    public static final String HOMEVISIT_COLUMN_ID = "id";
    public static final String HOMEVISIT_COLUMN_PILLCOUNT = "pillcount";
    public static final String HOMEVISIT_COLUMN_TCA = "next_tca";
    public static final String HOMEVISIT_COLUMN_TCAMATCH = "matching_tca";
    public static final String HOMEVISIT_COLUMN_HAVEADR = "have_adr";
    public static final String HOMEVISIT_COLUMN_SPECIFYADR = "yes_adr_specify";
    public static final String HOMEVISIT_COLUMN_NEWCLIENTPARTNERTESTED = "partner_tested";
    public static final String HOMEVISIT_COLUMN_NEWISSUEDSELFKIT = "selfkit_issued";
    public static final String HOMEVISIT_COLUMN_NEWSELFKITRESULTS = "selfkit_results";
    public static final String HOMEVISIT_COLUMN_STRESSFULSITUATION = "stressful";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table nyumbani " +
                        "(id integer primary key, pillcount integer,next_tca interger,matching_tca text, have_adr text,yes_adr_specify text, partner_tested text, selfkit_issued text, selfkit_results text," +
                        "stressful text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS nyumbani");
        onCreate(db);
    }

    public boolean insertNyumbani (String pillcount, String next_tca, String matching_tca, String have_adr,String yes_adr_specify,String partner_tested,String selfkit_issued,String selfkit_results,String stressful) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pillcount", pillcount);
        contentValues.put("next_tca", next_tca);
        contentValues.put("matching_tca", matching_tca);
        contentValues.put("have_adr", have_adr);
        contentValues.put("yes_adr_specify", yes_adr_specify);
        contentValues.put("partner_tested", partner_tested);
        contentValues.put("selfkit_issued", selfkit_issued);
        contentValues.put("selfkit_results", selfkit_results);
        contentValues.put("stressful", stressful);
        db.insert("nyumbani", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from nyumbani where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, HOMEVISIT_TABLE_NAME);
        return numRows;
    }

    public boolean updateNyumbani (Integer id, String pillcount, String next_tca, String matching_tca, String have_adr,String yes_adr_specify,String partner_tested,String selfkit_issued,String selfkit_results,String stressful) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pillcount", pillcount);
        contentValues.put("next_tca", next_tca);
        contentValues.put("matching_tca", matching_tca);
        contentValues.put("have_adr", have_adr);
        contentValues.put("yes_adr_specify", yes_adr_specify);
        contentValues.put("partner_tested", partner_tested);
        contentValues.put("selfkit_issued", selfkit_issued);
        contentValues.put("selfkit_results", selfkit_results);
        contentValues.put("stressful", stressful);
        db.update("nyumbani", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteNyumbani (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("nyumbani",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from nyumbani", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(HOMEVISIT_COLUMN_PILLCOUNT)));
            res.moveToNext();
        }
        return array_list;
    }
}

