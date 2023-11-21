package com.example.pakistanlawyerdiary.Model;

public class CaseData {
    private int id;
    private String casetitle;
    private String courtname;
    private String casetype;
    private String no;
    private String onbehalf;
    private String party;
    private String previousdate;
    private String adjourndate;
    private String steps;
    private String email;
    private String caseYear;
    private String partyContact;
    private String advocateName;
    private String advocateNum;
    private String respondentName;
    private String fieldUnSection;
    private String status;
    private String u_id;
    private String meetingDate;
    private String meetingTime;

    public String getEditkey() {
        return editkey;
    }

    public void setEditkey(String editkey) {
        this.editkey = editkey;
    }

    private String editkey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCasetitle() {
        return casetitle;
    }

    public void setCasetitle(String casetitle) {
        this.casetitle = casetitle;
    }

    public String getCourtname() {
        return courtname;
    }

    public void setCourtname(String courtname) {
        this.courtname = courtname;
    }

    public String getCasetype() {
        return casetype;
    }

    public void setCasetype(String casetype) {
        this.casetype = casetype;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getOnbehalf() {
        return onbehalf;
    }

    public void setOnbehalf(String onbehalf) {
        this.onbehalf = onbehalf;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
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

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaseYear() {
        return caseYear;
    }

    public void setCaseYear(String caseYear) {
        this.caseYear = caseYear;
    }

    public String getPartyContact() {
        return partyContact;
    }

    public void setPartyContact(String partyContact) {
        this.partyContact = partyContact;
    }

    public String getAdvocateName() {
        return advocateName;
    }

    public void setAdvocateName(String advocateName) {
        this.advocateName = advocateName;
    }

    public String getAdvocateNum() {
        return advocateNum;
    }

    public void setAdvocateNum(String advocateNum) {
        this.advocateNum = advocateNum;
    }

    public String getRespondentName() {
        return respondentName;
    }

    public void setRespondentName(String respondentName) {
        this.respondentName = respondentName;
    }

    public String getFieldUnSection() {
        return fieldUnSection;
    }

    public void setFieldUnSection(String fieldUnSection) {
        this.fieldUnSection = fieldUnSection;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }


    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public CaseData()
    {

    }
    // to show data in list view

    public CaseData(int i, String ct, String cn, String t, String n, String ob, String p, String pd, String ad, String s)
    {
        this.id = i;
        this.casetitle = ct;
        this.courtname = cn;
        this.casetype = t;
        this.no = n;
        this.onbehalf = ob;
        this.party = p;
        this.previousdate = pd;
        this.adjourndate = ad;
        this.steps = s;
    }


// for saving data on server
    public CaseData(String casetitle, String courtname,  String casetype, String no,String caseYear,    String onbehalf, String party,
                    String partyContact, String advocateName,String advocateNum,String respondentName,String fieldUnSection,
                    String status, String email,String meetingDate, String meetingTime,String u_id,String editkey)
    {
        this.casetitle = casetitle;
        this.courtname = courtname;
        this.casetype = casetype;
        this.no = no;
        this.caseYear = caseYear;
        this.onbehalf = onbehalf;
        this.party = party;
        this.partyContact = partyContact;
        this.advocateName = advocateName;
        this.advocateNum = advocateNum;
        this.respondentName = respondentName;
        this.fieldUnSection = fieldUnSection;
        this.status = status;
        this.email = email;
        this.meetingDate=meetingDate;
        this.meetingTime=meetingTime;
        this.u_id = u_id;
        this.editkey=editkey;

    }

    // for retriving data from sqlite database to store it on server

    public CaseData(int id,String casetitle, String courtname,  String casetype, String no,String caseYear,    String onbehalf, String party,
                    String partyContact, String advocateName,String advocateNum,String respondentName,String fieldUnSection,
                    String status, String email,String meetingDate, String meetingTime,String u_id)
    {
        this.id=id;
        this.casetitle = casetitle;
        this.courtname = courtname;
        this.casetype = casetype;
        this.no = no;
        this.caseYear = caseYear;
        this.onbehalf = onbehalf;
        this.party = party;
        this.partyContact = partyContact;
        this.advocateName = advocateName;
        this.advocateNum = advocateNum;
        this.respondentName = respondentName;
        this.fieldUnSection = fieldUnSection;
        this.status = status;
        this.email = email;
        this.meetingDate=meetingDate;
        this.meetingTime=meetingTime;
        this.u_id = u_id;

    }



    /// for updating data on server
    public CaseData(String casetitle, String courtname,  String casetype,String caseYear,  String onbehalf, String party,
                    String partyContact, String advocateName,String advocateNum,String respondentName,String fieldUnSection,
                    String status,String meetingDate, String meetingTime,String editkey)
    {
        this.casetitle = casetitle;
        this.courtname = courtname;
        this.casetype = casetype;
        this.caseYear = caseYear;
        this.onbehalf = onbehalf;
        this.party = party;
        this.partyContact = partyContact;
        this.advocateName = advocateName;
        this.advocateNum = advocateNum;
        this.respondentName = respondentName;
        this.fieldUnSection = fieldUnSection;
        this.status = status;
        this.meetingDate=meetingDate;
        this.meetingTime=meetingTime;
        this.editkey=editkey;

    }



}
