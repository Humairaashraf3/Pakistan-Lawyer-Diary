package com.example.pakistanlawyerdiary.Model;

public class PartyData {
    private int id;
    private String clientName;
    private String phone;
    private String clientemail;
    private String address;
    private String email;
    private String uid;
    private String editKey;

    public String getEditKey() {
        return editKey;
    }

    public void setEditKey(String editKey) {
        this.editKey = editKey;
    }

    public PartyData()
    {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClientemail() {
        return clientemail;
    }

    public void setClientemail(String clientemail) {
        this.clientemail = clientemail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


// to show data in list view
    public PartyData(int id, String name, String contactno) {
        this.id = id;
        this.clientName = name;
        this.phone = contactno;
    }





    // for saving data on server


    public PartyData(String clientName, String phone, String clientemail, String address, String email,String uid,String editKey)
    {
        this.clientName = clientName;
        this.phone = phone;
        this.clientemail = clientemail;
        this.address = address;
        this.email = email;
        this.uid = uid;
        this.editKey=editKey;
    }

    // for retriving data from sqlite database to store it on server
    public PartyData(int id,String clientName, String phone, String clientemail, String address, String email,String uid) {
        this.id=id;
        this.clientName = clientName;
        this.phone = phone;
        this.clientemail = clientemail;
        this.address = address;
        this.email = email;
        this.uid = uid;
        this.editKey=editKey;
    }


    /// for updating data on server


    public PartyData(String clientName, String phone, String clientemail, String address, String editKey) {
        this.clientName = clientName;
        this.phone = phone;
        this.clientemail = clientemail;
        this.address = address;
        this.editKey=editKey;
    }

}
