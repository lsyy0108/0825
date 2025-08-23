package model;

import java.time.LocalDateTime;

public class Sale {
    private int id;
    private int memberId;
    private int employeeId;
    private int drugId;
    private int qty;
    private double unitPrice;
    private double total;
    private LocalDateTime createdAt;
    private String orderId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public int getDrugId() { return drugId; }
    public void setDrugId(int drugId) { this.drugId = drugId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    
    public double getTotal() { return total; }       
    public void setTotal(double total) { this.total = total; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getOrderId() {return orderId;}
    public void setOrderId(String orderId) {this.orderId = orderId;}

    public double getTotalPrice(boolean isVIP) {
        double total = unitPrice * qty;
        if(isVIP) total *= 0.8; // VIP 8折
        return total;
    }
}
