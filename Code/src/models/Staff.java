package models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Staff {
    private final SimpleStringProperty staffName;
    private final SimpleStringProperty staffDesignation;
    private final SimpleStringProperty staffPhone;
    private final SimpleStringProperty staffShift;
    private final SimpleStringProperty staffAttendance;
    private SimpleIntegerProperty staffSalary;

    public Staff(String staffName, String staffDesignation, String staffPhone, String staffShift, String staffAttendance, Integer staffSalary){
        //TODO: figure out constructor overloading
        super();
        this.staffName = new SimpleStringProperty(staffName);
        this.staffDesignation = new SimpleStringProperty(staffDesignation);
        this.staffPhone = new SimpleStringProperty(staffPhone);
        this.staffShift = new SimpleStringProperty(staffShift);
        this.staffAttendance = new SimpleStringProperty(staffAttendance);
        this.staffSalary =new SimpleIntegerProperty(staffSalary);
    }

    public Staff(String staffName, String staffDesignation, String staffPhone, String staffShift, String staffAttendance) {
        super();
        this.staffName = new SimpleStringProperty(staffName);
        this.staffDesignation = new SimpleStringProperty(staffDesignation);
        this.staffPhone = new SimpleStringProperty(staffPhone);
        this.staffShift = new SimpleStringProperty(staffShift);
        this.staffAttendance = new SimpleStringProperty(staffAttendance);
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

    public Integer getStaffSalary() { return staffSalary.get();}
}
