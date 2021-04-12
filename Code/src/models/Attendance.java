package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

//Employee ID, Employee Name,Designation, Required Attendance
public class Attendance {

    private final SimpleStringProperty attendanceName;
    private final SimpleStringProperty attendanceDesignation;
    private final SimpleIntegerProperty attendanceID;
    private final SimpleStringProperty attendancePresent;
    private final SimpleStringProperty attendanceDate;

    public Attendance(String attendanceName, String attendanceDesignation, Integer attendanceID, String attendancePresent,String attendanceDate) {
        super();
        this.attendanceName = new SimpleStringProperty(attendanceName);
        this.attendanceDesignation = new SimpleStringProperty(attendanceDesignation);
        this.attendanceID = new SimpleIntegerProperty(attendanceID);
        this.attendancePresent = new SimpleStringProperty(attendancePresent);
        this.attendanceDate = new SimpleStringProperty(attendanceDate);
    }


    public String getAttendanceName() {
        return attendanceName.get();
    }

    public String getAttendanceDesignation() { return attendanceDesignation.get();}

    public Integer getAttendanceID() {
        return attendanceID.get();
    }

    public String getAttendancePresent() {
        return attendancePresent.get();
    }

    public String getAttendanceDate() { return attendanceDate.get();}

}
