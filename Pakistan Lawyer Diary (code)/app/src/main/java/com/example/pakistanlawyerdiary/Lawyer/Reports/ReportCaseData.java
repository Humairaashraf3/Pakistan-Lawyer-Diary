package com.example.pakistanlawyerdiary.Lawyer.Reports;

public class ReportCaseData {
    private int cno;
    private String casetitle;
    private String courtname;
    private String casetype;
    private String casenumber;
    private String caseyear;
    private String onbehalf;
    private String party;
    private String partyNo;
    private String adverse;
    private String adverseNo;
    private String respo;
    private String usec;


    public ReportCaseData(int cno, String casetitle, String courtname, String casetype, String casenumber,String caseyear, String onbehalf, String party, String partyNo, String adverse, String adverseNo, String respo, String usec) {
        this.cno = cno;
        this.casetitle = casetitle;
        this.courtname = courtname;
        this.casetype = casetype;
        this.casenumber = casenumber;
        this.caseyear = caseyear;
        this.onbehalf = onbehalf;
        this.party = party;
        this.partyNo = partyNo;
        this.adverse = adverse;
        this.adverseNo = adverseNo;
        this.respo = respo;
        this.usec = usec;
    }
    public int getCno() {
        return cno;
    }

    public void setCno(int cno) {
        this.cno = cno;
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

    public String getCaseyear() {
        return caseyear;
    }
    public String getCasenumber() {
        return casenumber;
    }

    public void setCasenumber(String casenumber) {
        this.casenumber = casenumber;
    }

    public void setCaseyear(String caseyear) {
        this.caseyear = caseyear;
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

    public String getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(String partyNo) {
        this.partyNo = partyNo;
    }

    public String getAdverse() {
        return adverse;
    }

    public void setAdverse(String adverse) {
        this.adverse = adverse;
    }

    public String getAdverseNo() {
        return adverseNo;
    }

    public void setAdverseNo(String adverseNo) {
        this.adverseNo = adverseNo;
    }

    public String getRespo() {
        return respo;
    }

    public void setRespo(String respo) {
        this.respo = respo;
    }

    public String getAusec() {
        return usec;
    }

    public void setAusec(String ausec) {
        this.usec = ausec;
    }


}
