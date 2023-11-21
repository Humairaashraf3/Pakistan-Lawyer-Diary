package com.example.pakistanlawyerdiary.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Pakistan_Lawyer_Diary.db";




    private static final String CREATE_TBL_CASE = "CREATE TABLE "
            + DatabaseContract.CASE.TABLE_NAME + " ("
            + DatabaseContract.CASE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.CASE.Col_CaseTitle + " TEXT NOT NULL, "
            + DatabaseContract.CASE.Col_CourtName + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_CaseType + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_CaseNumber + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_CaseYear+ " TEXT NOT NULL ,"
            + DatabaseContract.CASE.Col_OnBehalfOf + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_PartyName+ " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_ContactNumber + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_PartyAdvocateName+ " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_AdvocateContactNumber+ " TEXT NOT NULL ,"
            + DatabaseContract.CASE.Col_FiledUnderSection+ " TEXT NOT NULL ,"
            + DatabaseContract.CASE.Col_RespondantName + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_Status + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_Email + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_MeetingDate + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_MeetingTime + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_isLive + " TEXT NOT NULL,"
            + DatabaseContract.CASE.Col_isEditLive + " TEXT,"
            + DatabaseContract.CASE.Col_EditKey + " TEXT)";


    private static final String CREATE_TBL_CASETYPES = "CREATE TABLE "
            + DatabaseContract.CASETYPES.TABLE_NAME + " ("
            + DatabaseContract.CASETYPES._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.CASETYPES.Col_Email+ " TEXT NOT NULL, "
            + DatabaseContract.CASETYPES.Col_CaseType+ " TEXT ,"
           + DatabaseContract.CASETYPES.Col_isLive + " TEXT NOT NULL)";

    private static final String CREATE_TBL_COURTNAMES = "CREATE TABLE "
            + DatabaseContract.COURTNAMES.TABLE_NAME + " ("
            + DatabaseContract.COURTNAMES._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.COURTNAMES.Col_Email+ " TEXT NOT NULL, "
            + DatabaseContract.COURTNAMES.Col_CourtName+ " TEXT ,"
            + DatabaseContract.COURTNAMES.Col_isLive + " TEXT NOT NULL)";

    private static final String CREATE_TBL_CLIENT = "CREATE TABLE "
            + DatabaseContract.CLIENT.TABLE_NAME + " ("
            + DatabaseContract.CLIENT._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.CLIENT.Col_Name+ " TEXT NOT NULL, "
            + DatabaseContract.CLIENT.Col_Email+ " TEXT NOT NULL, "
            + DatabaseContract.CLIENT.Col_Phone+ " TEXT NOT NULL, "
            + DatabaseContract.CLIENT.Col_Address+ " TEXT NOT NULL, "
            + DatabaseContract.CLIENT.Col_LawyerEmail+ " TEXT NOT NULL,"
            + DatabaseContract.CLIENT.Col_isLive + " TEXT NOT NULL,"
            + DatabaseContract.CLIENT.Col_isEditLive + " TEXT,"
            + DatabaseContract.CLIENT.Col_EditKey + " TEXT)";


    private static final String CREATE_TBL_CASEHISTORY = "CREATE TABLE "
            + DatabaseContract.CASEHISTORY.TABLE_NAME + " ("
            + DatabaseContract.CASEHISTORY._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.CASEHISTORY.Col_CaseId+ " TEXT NOT NULL, "
            + DatabaseContract.CASEHISTORY.Col_Previousdate + " TEXT , "
            + DatabaseContract.CASEHISTORY.Col_Adjourndate + " TEXT NOT NULL,"
            + DatabaseContract.CASEHISTORY.Col_Step+ " TEXT ,"
            + DatabaseContract.CASEHISTORY.Col_HearingTime+ " TEXT  NOT NULL,"
            + DatabaseContract.CASEHISTORY.Col_LawyerEmail+ " TEXT NOT NULL,"
            + DatabaseContract.CASEHISTORY.Col_isLive + " TEXT NOT NULL)";

    private static final String CREATE_TBL_ADJOURNCASEREMINDER = "CREATE TABLE "
            + DatabaseContract.ADJOURNCASEREMINDER.TABLE_NAME + " ("
            + DatabaseContract.ADJOURNCASEREMINDER._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.ADJOURNCASEREMINDER.Col_Email+ " TEXT NOT NULL, "
            + DatabaseContract.ADJOURNCASEREMINDER.Col_Hour+ " INTEGER NOT NULL, "
            + DatabaseContract.ADJOURNCASEREMINDER.Col_Mints+ " INTEGER NOT NULL,"
            + DatabaseContract.ADJOURNCASEREMINDER.Col_Status+ " TEXT NOT NULL )";

    private static final String CREATE_TBL_CASEREMINDER = "CREATE TABLE "
            + DatabaseContract.CASEREMINDER.TABLE_NAME + " ("
            + DatabaseContract.CASEREMINDER._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.CASEREMINDER.Col_Email+ " TEXT NOT NULL, "
            + DatabaseContract.CASEREMINDER.Col_Title+ " TEXT NOT NULL, "
            + DatabaseContract.CASEREMINDER.Col_Client+ " TEXT NOT NULL,"
            + DatabaseContract.CASEREMINDER.Col_Date+ " TEXT NOT NULL,"
            + DatabaseContract.CASEREMINDER.Col_Time+ " TEXT NOT NULL,"
            + DatabaseContract.CASEREMINDER.Col_Active+ " TEXT NOT NULL )";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // db.execSQL(CREATE_TBL_USERS);
        db.execSQL(CREATE_TBL_CASE);
        db.execSQL(CREATE_TBL_CASETYPES);
        db.execSQL(CREATE_TBL_COURTNAMES);
        db.execSQL(CREATE_TBL_CLIENT);
        db.execSQL(CREATE_TBL_CASEHISTORY);
        db.execSQL(CREATE_TBL_ADJOURNCASEREMINDER);
        db.execSQL(CREATE_TBL_CASEREMINDER);

        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
     /*db.delete(DatabaseContract.Users.TABLE_NAME, null, null);
        db.delete(DatabaseContract.ADDCASE.TABLE_NAME, null, null);
        db.delete(DatabaseContract.CASEHISTORY.TABLE_NAME, null, null);
        onCreate(db);*/
        //  db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Users.TABLE_NAME );
        //db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.CASE.TABLE_NAME );
        //db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.CASEHISTORY.TABLE_NAME );

        // Create tables again
        //  onCreate(db);
    }



}
