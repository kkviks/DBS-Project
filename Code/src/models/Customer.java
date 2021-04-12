package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

//Room No, Customer Name, Service Type, #Occupants, Arrival time or date, Amount Due, Special Request
public class Customer {

    private final SimpleIntegerProperty customerRoomNo;
    private final SimpleStringProperty customerName;
    private final SimpleStringProperty customerServiceType;
    private final SimpleIntegerProperty customerOccupants;
    private final SimpleStringProperty customerArrivalTime;
    private final SimpleIntegerProperty customerAmountDue;
    private final SimpleStringProperty customerSpecialRequest;

    public Customer(Integer customerRoomNo, String customerName, String customerServiceType, Integer customerOccupants, String customerArrivalTime, Integer customerAmountDue, String customerSpecialRequest) {
        super();
        this.customerRoomNo = new SimpleIntegerProperty(customerRoomNo);
        this.customerName = new SimpleStringProperty(customerName);
        this.customerServiceType = new SimpleStringProperty(customerServiceType);
        this.customerOccupants = new SimpleIntegerProperty(customerOccupants);
        this.customerArrivalTime = new SimpleStringProperty(customerArrivalTime);
        this.customerAmountDue = new SimpleIntegerProperty(customerAmountDue);
        this.customerSpecialRequest = new SimpleStringProperty(customerSpecialRequest);
    }
    // TODO: 08-04-2021 Overload new constructor for new field

    //public Customer(Integer customerRoomNo, String customerName, String customerServiceType, Integer customerOccupants, String customerArrivalTime, Integer customerAmountDue, String customerSpecialRequest)

    public Integer getCustomerRoomNo() {
        return customerRoomNo.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public String getCustomerServiceType() {
        return customerServiceType.get();
    }

    public Integer getCustomerOccupants() {
        return customerOccupants.get();
    }

    public String getCustomerArrivalTime() {
        return customerArrivalTime.get();
    }

    public Integer getCustomerAmountDue() {
        return customerAmountDue.get();
    }

    public String getCustomerSpecialRequest() {
        return customerSpecialRequest.get();
    }
    // TODO: 08-04-2021  Add getter for new field
}
