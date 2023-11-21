package com.example.pakistanlawyerdiary.Model;

public class TypeCourtData {
    String id, name,dataype;
String email,uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public TypeCourtData()
    {

    }
    public TypeCourtData(String id, String name,String dataype) {
        this.id = id;
        this.name = name;
        this.dataype=dataype;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataype() {
        return dataype;
    }

    public void setDataype(String dataype) {
        this.dataype = dataype;
    }


    // for saving data on server
     public  void saveData(String name,String email,String uid)

     {
         this.name = name;
         this.email=email;
         this.uid=uid;
     }
}
