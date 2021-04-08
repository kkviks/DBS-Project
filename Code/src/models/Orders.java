package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
//Room No, Special Requests, Inventories, Extra Charges, Time, BellBoy
public class Orders
{
    private final SimpleIntegerProperty orderRoomNo;
    private final SimpleStringProperty orderSpecialRequest;
    private final SimpleStringProperty orderInventories;
    private final SimpleIntegerProperty orderExtraCharges;
    private final SimpleStringProperty orderTime;
    private final SimpleStringProperty orderBellBoy;

    public Orders(Integer orderRoom, String orderSpecialRequest, String orderInventories, Integer orderExtraCharges, String orderTime, String orderBellBoy ) {
        super();
        this.orderRoomNo = new SimpleIntegerProperty(orderRoom);
        this.orderSpecialRequest = new SimpleStringProperty(orderSpecialRequest);
        this.orderInventories = new SimpleStringProperty(orderInventories);
        this.orderExtraCharges = new SimpleIntegerProperty(orderExtraCharges);
        this.orderTime = new SimpleStringProperty(orderTime);
        this.orderBellBoy = new SimpleStringProperty(orderBellBoy);
    }

    public Integer getOrderRoomNo() {
        return orderRoomNo.get();
    }

    public String getOrderSpecialRequest() {
        return orderSpecialRequest.get();
    }

    public String getOrderInventories() {
        return orderInventories.get();
    }

    public Integer getOrderExtraCharges() {
        return orderExtraCharges.get();
    }

    public String getOrderTime() {
        return orderTime.get();
    }

    public String getOrderBellBoy() {
        return orderBellBoy.get();
    }


}