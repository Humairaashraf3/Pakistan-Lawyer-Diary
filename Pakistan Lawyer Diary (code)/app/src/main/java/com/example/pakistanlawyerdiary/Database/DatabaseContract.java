package com.example.pakistanlawyerdiary.Database;

import android.provider.BaseColumns;

public class DatabaseContract {
    public DatabaseContract() {}

    /* Inner class that defines the table contents */
    /*public static abstract class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COL_FULLNAME = "fullname";
        public static final String COL_EMAIL = "email";
        public static final String COL_Password= "password";
    }*/
    public static abstract class CASE implements BaseColumns {
        public static final String TABLE_NAME = "Addcase";
        public static final String Col_CaseTitle = "CaseTitle";
        public static final String Col_CourtName="CourtName";
        public static final String Col_CaseType = "CaseType";
        public static final String Col_CaseNumber = "CaseNumber";
        public static final String Col_CaseYear="CaseYear";
        public static final String Col_OnBehalfOf = "OnBehalfOf";
        public static final String Col_PartyName = "PartyName";
        public static final String Col_ContactNumber = "ContactNumber";
        public static final String Col_PartyAdvocateName = "PartyAdvocateName";
        public static final String Col_AdvocateContactNumber = "AdvocateContactNumber";
        public static final String Col_RespondantName= "RespondantName";
        public static final String Col_FiledUnderSection="FeldUnderSection";
        public static final String Col_Status="Status";
        public static final String Col_Email = "Email";
        public static final String Col_MeetingDate = "MeetingDate";
        public static final String Col_MeetingTime = "MeetingTime";
        public static final String Col_isLive = "isLive";
        public static final String Col_isEditLive = "isEditLive";
        public static final String Col_EditKey = "EditKey";

    }

    public static abstract class CASETYPES implements BaseColumns
    {
        public static final String TABLE_NAME = "CaseTypes";
        public static final String Col_Email = "Email";
        public static final String Col_CaseType = "CaseType";
        public static final String Col_isLive = "isLive";
    }
    public static abstract class COURTNAMES implements BaseColumns {
        public static final String TABLE_NAME = "CourtNames";
        public static final String Col_Email = "Email";
        public static final String Col_CourtName = "CourtName";
        public static  final String Col_isLive = "isLive";
    }
    public static abstract class CLIENT implements BaseColumns {
        public static final String TABLE_NAME = "Client";
        public static final String Col_Name = "Name";
        public static final String Col_Email = "Email";
        public static final String Col_Phone= "Phone";
        public static final String Col_Address= "Address";
        public static final String Col_LawyerEmail = "LawyerEmail";
        public static  final String Col_isLive = "isLive";
        public static final String Col_isEditLive = "isEditLive";
        public static final String Col_EditKey = "EditKey";
    }


    public static abstract class CASEHISTORY implements BaseColumns
    {
        public static final String TABLE_NAME = "casehistory";
        public static final String Col_CaseId = "CaseId";
        public static final String Col_Previousdate = "PreviousDate";
        public static final String Col_Adjourndate = "AdjournDate";
        public static final String Col_Step= "Step";
        public static final String Col_Image= "Image";
        public static  final String Col_HearingTime = "HiringTime";
        public static final String Col_LawyerEmail = "LawyerEmail";
        public static final String Col_isLive = "isLive";
    }

    public static abstract class ADJOURNCASEREMINDER implements BaseColumns
    {
        public static final String TABLE_NAME = "AdjournCaseReminder";
        public static final String Col_Email = "Email";
        public static final String Col_Status = "Status";
        public static final String Col_Hour = "Hours";
        public static final String Col_Mints = "Mints";
    }

    public static abstract class CASEREMINDER implements BaseColumns
    {
        public static final String TABLE_NAME = "CaseReminder";
        public static final String Col_Email = "Email";
        public static final String Col_Title = "Title";
        public static final String Col_Client= "client";
        public static final String Col_Date = "date";
        public static final String Col_Time = "time";
        public static final String Col_Active = "active";
    }
}