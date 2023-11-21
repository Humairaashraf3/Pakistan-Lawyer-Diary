package com.example.pakistanlawyerdiary.Lawyer.Reminder;

public class ReminderData
{
    String Id;
    String reminder_title;
    String client;
    String date;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    String month;
    String time;
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }



    public String getReminder_title() {
        return reminder_title;
    }

    public void setReminder_title(String reminder_title) {
        this.reminder_title = reminder_title;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ReminderData(String Id,String reminder_title, String client, String date,String Month, String time) {
        this.Id=Id;
        this.reminder_title = reminder_title;
        this.client = client;
        this.date = date;
        this.month = Month;
        this.time = time;
    }
}
