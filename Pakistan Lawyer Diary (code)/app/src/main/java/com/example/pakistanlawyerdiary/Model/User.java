package com.example.pakistanlawyerdiary.Model;

public class User
{
    private String uid;
    private String fullname;
    private String email;
    private String passsword;
    private String phone;
    private String address;

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    private String about;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;
    private int userType;
    private String status;
    private double ratingValue;
    private long ratingCount;
    public User()
    {

    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue)
    {
        this.ratingValue = ratingValue;
    }

    public long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public User(String uid, String fn, String e, String p, String phn, String add, int u, String status, double ratingValue, long ratingCount,String image,String about)
    {
        this.uid=uid;
        this.fullname=fn;
        this.email=e;
        this.passsword=p;
        this.phone=phn;
        this.address=add;
        this.userType=u;
        this.status=status;
        this.ratingValue=ratingValue;
        this.ratingCount=ratingCount;
        this.image=image;
        this.about=about;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserType() {
        return userType;
    }



    public void setUserType(int userType) {
        this.userType = userType;
    }



}
