package com.example.sih;

import com.google.firebase.database.ValueEventListener;

public class Model {

    private String ComplaintLocation,Desc,issue,Name,Phone,Support,EventID,Report,Completed,Attending,Image;
    private String Status,id;

    public Model() {
    }



    public Model(String complaintLocation, String desc, String issue,
                 String name, String phone, String support, String eventID,
                 String report, String completed, String status, String attending, String image,String id) {
        Report=report;
        Attending = attending;
        Status = status;
        this.id = id;
        Image = image;
        Completed = completed;
        ComplaintLocation = complaintLocation;
        Desc = desc;
        this.issue = issue;
        Name = name;
        Phone = phone;
        Support = support;
        EventID = eventID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAttending() {
        return Attending;
    }

    public void setAttending(String attending) {
        Attending = attending;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCompleted() {
        return Completed;
    }

    public void setCompleted(String completed) {
        Completed = completed;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getSupport() {
        return Support;
    }

    public void setSupport(String support) {
        Support = support;
    }

    public String getComplaintLocation() {
        return ComplaintLocation;
    }

    public void setComplaintLocation(String complaintLocation) {
        ComplaintLocation = complaintLocation;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
