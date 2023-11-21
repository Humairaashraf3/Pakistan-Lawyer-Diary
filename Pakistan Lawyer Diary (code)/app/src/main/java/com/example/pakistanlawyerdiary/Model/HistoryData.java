package com.example.pakistanlawyerdiary.Model;

import android.graphics.Bitmap;

public class HistoryData
{
    private String casenumber;
    private String previousdate;
    private String adjourndate;
    private String step;
    private Bitmap img;
    private String uid;
    private String hiringTime;
    private String Lawyer_email;

    public HistoryData()
    {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLawyer_email() {
        return Lawyer_email;
    }

    public void setLawyer_email(String lawyer_email) {
        Lawyer_email = lawyer_email;
    }

    public String getHiringTime() {
        return hiringTime;
    }

    public void setHiringTime(String hiringTime) {
        this.hiringTime = hiringTime;
    }
    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
    


    public HistoryData(String casenumber, String previousdate, String adjourndate, String step)
    {
        this.casenumber=casenumber;
        this.previousdate=previousdate;
        this.adjourndate=adjourndate;
        this.step=step;
    }
    
    public HistoryData(String caseId, String previousDate, String adjournDate, String step,String adTime,String uid,String lawyer_email)
    {
        this.casenumber = caseId;
        this.previousdate = previousDate;
        this.adjourndate = adjournDate;
        this.step = step;
        this.uid=uid;
        this.hiringTime=adTime;
        this.Lawyer_email = lawyer_email;
        //this.image = image;

    }

    public String getCasenumber() {
        return casenumber;
    }

    public void setCasenumber(String casenumber) {
        this.casenumber = casenumber;
    }

    public String getPreviousdate() {
        return previousdate;
    }

    public void setPreviousdate(String previousdate) {
        this.previousdate = previousdate;
    }

    public String getAdjourndate() {
        return adjourndate;
    }

    public void setAdjourndate(String adjourndate) {
        this.adjourndate = adjourndate;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
