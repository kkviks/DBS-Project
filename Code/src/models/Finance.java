package models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Finance {

    private final SimpleStringProperty financeDate;
    private final SimpleDoubleProperty financeWage;
    private final SimpleDoubleProperty financeRent;
    private final SimpleDoubleProperty financeProfit;
    private final SimpleDoubleProperty financeCredit;

    public Finance(String financeDate, Double financeWage, Double financeRent, Double financeProfit, Double financeCredit) {
        super();
        this.financeDate = new SimpleStringProperty(financeDate);
        this.financeWage = new SimpleDoubleProperty(financeWage);
        this.financeRent = new SimpleDoubleProperty(financeRent);
        this.financeProfit = new SimpleDoubleProperty(financeProfit);
        this.financeCredit = new SimpleDoubleProperty(financeCredit);
    }

    public String getFinanceDate() {
        return financeDate.get();
    }

    public Double getFinanceWage() {
        return financeWage.get();
    }

    public Double getFinanceRent() {
        return financeRent.get();
    }

    public Double getFinanceProfit() {
        return financeProfit.get();
    }

    public Double getFinanceCredit() {
        return financeCredit.get();
    }
}
