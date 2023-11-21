package com.example.pakistanlawyerdiary.Model;

public class ScheduleData {


    String id;
    String title;
    String type;
    String Caseno;
    String PartyName;
    String date;
    String time;
    String stitle;

    public String getIstoday() {
        return istoday;
    }

    public void setItoday(String isstoday) {
        this.istoday = istoday;
    }

    String istoday;

    public String getCaseno() {
        return Caseno;
    }

    public void setCaseno(String caseno) {
        Caseno = caseno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
    }

    public String getStitle() {
        return stitle;
    }

    public void setStitle(String stitle) {
        this.stitle = stitle;
    }

    public ScheduleData(String id, String title, String type, String caseno, String partyName, String date, String time, String stitle,String istoday) {
        this.id = id;
        this.title = title;
        this.type = type;
        Caseno = caseno;
        PartyName = partyName;
        this.date = date;
        this.time = time;
        this.stitle = stitle;
        this.istoday = istoday;
    }

    public ScheduleData()
    {

    }
}
