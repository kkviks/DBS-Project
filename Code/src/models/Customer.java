package models;

import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private final SimpleStringProperty staffName;
    private final SimpleStringProperty staffDesignation;
    private final SimpleStringProperty staffPhone;
    private final SimpleStringProperty staffShift;
    private final SimpleStringProperty staffAttendance;

    public Customer(String staffName, String staffDesignation, String staffPhone, String staffShift, String roomAvailability) {
        super();
        this.staffName = new SimpleStringProperty(staffName);
        this.staffDesignation = new SimpleStringProperty(staffDesignation);
        this.staffPhone = new SimpleStringProperty(staffPhone);
        this.staffShift = new SimpleStringProperty(staffShift);
        this.staffAttendance = new SimpleStringProperty(roomAvailability);
    }

    public String getStaffName() {
        return staffName.get();
    }

    public String getStaffDesignation() {
        return staffDesignation.get();
    }

    public String getStaffPhone() {
        return staffPhone.get();
    }

    public String getStaffShift() {
        return staffShift.get();
    }

    public String getStaffAttendance() {
        return staffAttendance.get();
    }
}
