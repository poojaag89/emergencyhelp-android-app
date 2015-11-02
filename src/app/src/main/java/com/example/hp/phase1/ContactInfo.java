package com.example.hp.phase1;

/**
 * Created by HP on 2/24/15.
 */
public class ContactInfo {

    private String contactName;
    private String contactNumber;

    public ContactInfo(String contactName, String contactNumber){
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
