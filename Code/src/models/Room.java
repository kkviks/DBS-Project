package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Room {
    private final SimpleIntegerProperty roomNum;
    private final SimpleStringProperty roomType;
    private final SimpleIntegerProperty roomBeds;
    private final SimpleIntegerProperty roomPrice;
    private final SimpleStringProperty roomAvailability;

    public Room(Integer roomNum, String roomType, Integer roomBeds, Integer roomPrice, String roomAvailability) {
        super();
        this.roomNum = new SimpleIntegerProperty(roomNum);
        this.roomType = new SimpleStringProperty(roomType);
        this.roomBeds = new SimpleIntegerProperty(roomBeds);
        this.roomPrice = new SimpleIntegerProperty(roomPrice);
        this.roomAvailability = new SimpleStringProperty(roomAvailability);
    }

    public Integer getRoomNum() {
        return roomNum.get();
    }

    public String getRoomType() {
        return roomType.get();
    }

    public Integer getRoomBeds() {
        return roomBeds.get();
    }

    public Integer getRoomPrice() {
        return roomPrice.get();
    }

    public String getRoomAvailability() {
        return roomAvailability.get();
    }
}
